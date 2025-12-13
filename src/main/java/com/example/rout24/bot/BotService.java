package com.example.rout24.bot;

import com.example.rout24.entity.User;
import com.example.rout24.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BotService extends TelegramLongPollingBot {

    private final String BOT_USERNAME = "@rout24bot";
    private final String BOT_TOKEN = "8538261386:AAECXqvug4jESHqObhur7CxD_D11FXjj4x4";
    private final Long ADMIN_CHAT_ID = 7193645528L;

    private final UserRepository userRepository;
    private final ConcurrentHashMap<Long, Boolean> waitingForBroadcast = new ConcurrentHashMap<>();

    public BotService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String getBotUsername() { return BOT_USERNAME; }

    @Override
    public String getBotToken() { return BOT_TOKEN; }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage()) return;

        Message msg = update.getMessage();
        Long chatId = msg.getChatId();

        if (waitingForBroadcast.containsKey(chatId) && chatId.equals(ADMIN_CHAT_ID)) {
            broadcastMessageToAllUsers(msg);
            waitingForBroadcast.remove(chatId);
            sendMessage(chatId, "✅ Xabar barcha foydalanuvchilarga muvaffaqiyatli yuborildi!");
            sendMessage(chatId, "🎉 Admin panelga xush kelibsiz!", getAdminMenu());
            return;
        }

        if (!msg.hasText()) return;

        String text = msg.getText();

        if (text.equals("/start")) {
            handleStartCommand(chatId);
        } else if (chatId.equals(ADMIN_CHAT_ID)) {
            handleAdminCommands(text, chatId);
        } else {
            handleUserCommands(text, chatId);
        }
    }

    private void handleStartCommand(Long chatId) {
        if (chatId.equals(ADMIN_CHAT_ID)) {
            sendMessage(chatId, "🎉 Admin panelga xush kelibsiz!", getAdminMenu());
            return;
        }

        User user = userRepository.findById(chatId.toString()).orElse(null);

        if (user != null) {
            sendMessage(chatId, "👋 Salom, " + user.getFullName() + "!\n🎉 Rout24 botga xush kelibsiz!", getUserMenu());
        } else {
            sendMessageWithWebAppButton(chatId,
                    "🌟 Salom! Rout24 xizmatlaridan foydalanish uchun avval ro'yxatdan o'tishingiz kerak.\n\n"
                            + "Quyidagi tugma orqali saytga o'tib, tezkor ro'yxatdan o'ting:",
                    "https://rout24.online");
        }
    }

    private void handleAdminCommands(String text, Long chatId) {
        if (text.equals("Xabar yuborish")) {
            waitingForBroadcast.put(chatId, true);
            sendMessage(chatId, "📤 Barcha foydalanuvchilarga yuboriladigan xabarni yuboring.\n"
                    + "Matn, rasm, video, ovozli xabar, hujjat — har qanday turdagi xabar qabul qilinadi.");
        }
    }

    private void handleUserCommands(String text, Long chatId) {
        if (text.equals("Qo'llab-quvvatlash")) {
            sendSupportMenu(chatId);
        } else if (text.equals("Admin guruh")) {
            sendMessage(chatId, "✉️ Savolingizni #help belgisidan keyin yozing.\n"
                    + "Masalan: #help Yuk tashish narxi qancha?\n\n"
                    + "Javob 24 soat ichida keladi.", getUserMenu());
        } else if (text.equals("AI")) {
            sendMessage(chatId, "🤖 AI yordamchi chatbot tez orada ishga tushadi!\n"
                    + "Sabr qiling, yangiliklardan xabardor bo'lib turamiz.", getUserMenu());
        }
    }

    private void broadcastMessageToAllUsers(Message originalMessage) {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            Long targetChatId = Long.valueOf(user.getChatId());

            String caption = originalMessage.getCaption();
            String broadcastCaption = caption != null ? "📢 " + caption : "📢 Yangi xabar";

            if (originalMessage.hasText()) {
                sendMessage(targetChatId, "📢 " + originalMessage.getText());
            } else if (originalMessage.hasPhoto()) {
                String fileId = originalMessage.getPhoto().get(originalMessage.getPhoto().size() - 1).getFileId();
                sendPhoto(targetChatId, fileId, broadcastCaption);
            } else if (originalMessage.hasVideo()) {
                String fileId = originalMessage.getVideo().getFileId();
                sendVideo(targetChatId, fileId, broadcastCaption);
            } else if (originalMessage.hasVoice()) {
                String fileId = originalMessage.getVoice().getFileId();
                sendVoice(targetChatId, fileId);
            } else if (originalMessage.hasAudio()) {
                String fileId = originalMessage.getAudio().getFileId();
                sendAudio(targetChatId, fileId, broadcastCaption);
            } else if (originalMessage.hasDocument()) {
                String fileId = originalMessage.getDocument().getFileId();
                sendDocument(targetChatId, fileId, broadcastCaption);
            } else if (originalMessage.hasAnimation()) {
                String fileId = originalMessage.getAnimation().getFileId();
                sendAnimation(targetChatId, fileId, broadcastCaption);
            } else if (originalMessage.hasSticker()) {
                String fileId = originalMessage.getSticker().getFileId();
                sendSticker(targetChatId, fileId);
            } else {
                ForwardMessage forward = new ForwardMessage();
                forward.setChatId(targetChatId.toString());
                forward.setFromChatId(originalMessage.getChatId().toString());
                forward.setMessageId(originalMessage.getMessageId());
                try { execute(forward); } catch (TelegramApiException e) { e.printStackTrace(); }
            }
        }
    }

    public void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        message.setParseMode("HTML");
        try { execute(message); } catch (TelegramApiException e) { e.printStackTrace(); }
    }

    private void sendMessage(Long chatId, String text, ReplyKeyboardMarkup keyboard) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        message.setParseMode("HTML");
        message.setReplyMarkup(keyboard);
        try { execute(message); } catch (TelegramApiException e) { e.printStackTrace(); }
    }

    private void sendPhoto(Long chatId, String fileId, String caption) {
        SendPhoto photo = new SendPhoto();
        photo.setChatId(chatId.toString());
        photo.setPhoto(new InputFile(fileId));
        photo.setCaption(caption);
        photo.setParseMode("HTML");
        try { execute(photo); } catch (TelegramApiException e) { e.printStackTrace(); }
    }

    private void sendVideo(Long chatId, String fileId, String caption) {
        SendVideo video = new SendVideo();
        video.setChatId(chatId.toString());
        video.setVideo(new InputFile(fileId));
        video.setCaption(caption);
        video.setParseMode("HTML");
        try { execute(video); } catch (TelegramApiException e) { e.printStackTrace(); }
    }

    private void sendVoice(Long chatId, String fileId) {
        SendVoice voice = new SendVoice();
        voice.setChatId(chatId.toString());
        voice.setVoice(new InputFile(fileId));
        try { execute(voice); } catch (TelegramApiException e) { e.printStackTrace(); }
    }

    private void sendDocument(Long chatId, String fileId, String caption) {
        SendDocument doc = new SendDocument();
        doc.setChatId(chatId.toString());
        doc.setDocument(new InputFile(fileId));
        doc.setCaption(caption);
        doc.setParseMode("HTML");
        try { execute(doc); } catch (TelegramApiException e) { e.printStackTrace(); }
    }

    private void sendAudio(Long chatId, String fileId, String caption) {
        SendAudio audio = new SendAudio();
        audio.setChatId(chatId.toString());
        audio.setAudio(new InputFile(fileId));
        audio.setCaption(caption);
        audio.setParseMode("HTML");
        try { execute(audio); } catch (TelegramApiException e) { e.printStackTrace(); }
    }

    private void sendAnimation(Long chatId, String fileId, String caption) {
        SendAnimation anim = new SendAnimation();
        anim.setChatId(chatId.toString());
        anim.setAnimation(new InputFile(fileId));
        anim.setCaption(caption);
        anim.setParseMode("HTML");
        try { execute(anim); } catch (TelegramApiException e) { e.printStackTrace(); }
    }

    private void sendSticker(Long chatId, String fileId) {
        SendSticker sticker = new SendSticker();
        sticker.setChatId(chatId.toString());
        sticker.setSticker(new InputFile(fileId));
        try { execute(sticker); } catch (TelegramApiException e) { e.printStackTrace(); }
    }

    private void sendMessageWithWebAppButton(Long chatId, String text, String url) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        message.setParseMode("HTML");

        WebAppInfo info = new WebAppInfo();
        info.setUrl(url);

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(false);

        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton webButton = new KeyboardButton("🌐 Rout24 saytida ro'yxatdan o'tish");
        webButton.setWebApp(info);
        row.add(webButton);
        rows.add(row);

        keyboard.setKeyboard(rows);
        message.setReplyMarkup(keyboard);

        try { execute(message); } catch (TelegramApiException e) { e.printStackTrace(); }
    }

    private ReplyKeyboardMarkup getUserMenu() {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setResizeKeyboard(true);

        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Qo'llab-quvvatlash");
        rows.add(row);

        keyboard.setKeyboard(rows);
        return keyboard;
    }

    private ReplyKeyboardMarkup getAdminMenu() {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setResizeKeyboard(true);

        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Xabar yuborish");
        rows.add(row);

        keyboard.setKeyboard(rows);
        return keyboard;
    }

    private void sendSupportMenu(Long chatId) {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setResizeKeyboard(true);

        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Admin guruh");
        row.add("AI");
        rows.add(row);

        keyboard.setKeyboard(rows);
        sendMessage(chatId, "🛠 Qo'llab-quvvatlash bo'limi:\nSavollaringiz bo'lsa, biz yordam beramiz!", keyboard);
    }
}