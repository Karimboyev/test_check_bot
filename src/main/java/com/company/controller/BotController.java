package com.company.controller;

import com.company.service.BotService;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.Update;

@Data
@NoArgsConstructor
@Controller
public class BotController {

    public void controller(Update update) {

        BotService service = new BotService();

        switch (update.getMessage().getText()){
            case "/start":{
                service.cleanTest(update);
                service.firstMenu(update);
                break;
            }
            case "Yangi test yaratish":{
                service.addTestMenu(update);
                break;
            }
            case "Javoblarni jo'natish":{
                service.sendAnswerMenu(update);
                break;
            }
            case "Asosiy menuga qaytish":{
                service.firstMenu(update);
            }
        }


        //path
        switch (service.getPath(update.getMessage().getChatId())) {
            case "/start/add": {
                if (!update.getMessage().getText().equals("Yangi test yaratish")) {
                    service.addTest(update);
                }
                break;
            }
            case "/start/send":{
                if(!update.getMessage().getText().equals("Javoblarni jo'natish")){
                    service.sendAnswer(update);
                }
            }
            case "/start/add/run":{
                if(update.getMessage().getText().equals("Natijalarni ko'rish")){
                    service.getResualt(update);
                }
                if(update.getMessage().getText().equals("Test tekshirishni tugatish!")){
                    service.getResualt(update);
                    service.cleanTest(update);
                    service.firstMenu(update);
                }
                break;
            }
        }

    }
}
