package com.example.rout24.config;

import com.example.rout24.bot.BotService;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class BotConfig {

   private final BotService botService;

   public BotConfig(BotService botService) {
       this.botService = botService;
   }

   @PostConstruct
   public void init() {
       try {
           TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
           botsApi.registerBot(botService);
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
}
