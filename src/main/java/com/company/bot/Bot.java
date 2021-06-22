package com.company.bot;

import com.company.controller.BotController;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {

    @Override
    public String getBotToken() {
        return "1828193431:AAGrheF_f5aOABfYGu_LN9qBI6kegycyk-M";
    }

    @Override
    public String getBotUsername() {
        return "check_testX";
    }

    @Override
    public void onUpdateReceived(Update update) {
        BotController botController=new BotController();
        botController.controller(update);
    }

    public void sendMessage(SendMessage sendMessage){
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
