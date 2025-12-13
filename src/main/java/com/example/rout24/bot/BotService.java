package com.example.rout24.bot;

import com.example.rout24.entity.User;
import com.example.rout24.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
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
    private final ConcurrentHashMap<Long, String> waitingForAdminMessage = new ConcurrentHashMap<>();

    public BotService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String getBotUsername() { return BOT_USERNAME; }

    @Override
    public String getBotToken() { return BOT_TOKEN; }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message msg = update.getMessage();
            Long chatId = msg.getChatId();

            if (msg.hasText()) {
                String text = msg.getText();

                if (waitingForAdminMessage.containsKey(chatId)) {
                    broadcastToAllUsers(text);
                    waitingForAdminMessage.remove(chatId);
                    sendMessage(chatId, "✅ Xabaringiz barcha foydalanuvchilarga yuborildi!");
                    return;
                }

                if (text.equals("/start")) {
                    User user = userRepository.findById(String.valueOf(chatId)).orElse(null);
                    if (user != null) {
                        sendMessage(chatId, "👋 Salom " + user.getFullName() + "! Xush kelibsiz! 🎉", getUserMenu());
                    } else {
                        sendMessageWithWebAppButton(chatId, "🌐 Salom! Siz hali ro'yxatdan o'tmagansiz. Saytimiz orqali ro'yxatdan o'tishingiz mumkin:", "https://rout24.online");
                    }
                } else if (text.equals("Qo'llab-quvvatlash")) {
                    sendSupportMenu(chatId);
                } else if (text.equals("Admin guruh") && !chatId.equals(ADMIN_CHAT_ID)) {
                    sendMessage(chatId, "✉️ #help dan keyin savolingizni yozing. 24 soat ichida javob olasiz!", getUserMenu());
                } else if (text.equals("AI") && !chatId.equals(ADMIN_CHAT_ID)) {
                    sendMessage(chatId, "🤖 AI chatbot tez kunda ishga tushadi!", getUserMenu());
                } else if (text.equals("Xabar yuborish") && chatId.equals(ADMIN_CHAT_ID)) {
                    waitingForAdminMessage.put(chatId, "waiting");
                    sendMessage(chatId, "📤 Iltimos yuboriladigan xabarni kiriting:");
                }
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
        message.setReplyMarkup(keyboard);
        message.setParseMode("HTML");
        try { execute(message); } catch (TelegramApiException e) { e.printStackTrace(); }
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
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton webButton = new KeyboardButton("🌐 Rout24 Online");
        webButton.setWebApp(info);
        row.add(webButton);
        rows.add(row);
        keyboard.setKeyboard(rows);
        message.setReplyMarkup(keyboard);

        try { execute(message); } catch (TelegramApiException e) { e.printStackTrace(); }
    }

    private void broadcastToAllUsers(String text) {
        List<User> users = userRepository.findAll();
        for (User u : users) {
            sendMessage(Long.valueOf(u.getChatId()), "📢 " + text);
        }
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

    private void sendSupportMenu(Long chatId) {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setResizeKeyboard(true);
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Admin guruh");
        row.add("AI");
        rows.add(row);
        keyboard.setKeyboard(rows);
        sendMessage(chatId, "🛠 Qo'llab-quvvatlash menyusi:", keyboard);
    }
}
