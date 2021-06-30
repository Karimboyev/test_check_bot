package com.company.service;

import com.company.bot.Bot;
import com.company.entity.Admin;
import com.company.entity.Pupil;
import com.company.repository.AdminRepository;
import com.company.repository.PupilRepasitory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BotService {

    public AdminRepository adminRepository = GetAdminRepository.adminRepository;
    public PupilRepasitory pupilRepasitory = GetPupilRepository.pupilRepasitory;
    public static Map<Long, String> path = new HashMap<>();

    SendMessage send = new SendMessage();

    // home menu
    public void firstMenu(Update update) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        //config button
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        //first button
        KeyboardRow first = new KeyboardRow();
        first.add("Yangi test yaratish");

        //second button
        KeyboardRow second = new KeyboardRow();
        second.add("Javoblarni jo'natish");

        //add keyboards
        keyboardRows.add(first);
        keyboardRows.add(second);
        keyboardMarkup.setKeyboard(keyboardRows);

        send.setText("Test tekshirish botiga Xush kelibsiz!");
        send.setReplyMarkup(keyboardMarkup);
        send.setChatId(update.getMessage().getChatId());

        //send message
        path.put(update.getMessage().getChatId(), "/start");
        Bot bot = new Bot();
        bot.sendMessage(send);
    }

    //add test menu
    public void addTestMenu(Update update) {
        Bot bot = new Bot();
        bot.sendMessage(new SendMessage()
                .setText("To'g'ri javoblarni kiriting!\n(Kod/Javoblar)\n\nMasalan:\n123/abcaaabb...")
                .setChatId(update.getMessage().getChatId()));
        path.replace(update.getMessage().getChatId(), "/start/add");
    }

    //add test
    public void addTest(Update update) {
        String updateText = update.getMessage().getText();
        if(updateText.contains("/")){
            String code = updateText.substring(0, updateText.indexOf('/'));
            String answers = updateText.substring(updateText.indexOf('/') + 1);
            if (!code.isEmpty() && !answers.isEmpty()) {
                if (adminRepository.existsByCode(code)) {
                    Bot bot = new Bot();
                    bot.sendMessage(send.setText("Bunday kod mavjud! Qayta kiriting.")
                            .setChatId(update.getMessage().getChatId()));
                    addTestMenu(update);
                } else {
                    adminRepository.save(new Admin(update.getMessage().getFrom().getUserName(),
                            update.getMessage().getChatId(), code, answers));

                    ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

                    //config button
                    keyboardMarkup.setSelective(true);
                    keyboardMarkup.setResizeKeyboard(true);
                    keyboardMarkup.setOneTimeKeyboard(true);

                    List<KeyboardRow> keyboardRows = new ArrayList<>();

                    // natijalarni ko'rish
                    KeyboardRow resualt = new KeyboardRow();
                    resualt.add("Natijalarni ko'rish");

                    //stop button
                    KeyboardRow stop = new KeyboardRow();
                    stop.add("Test tekshirishni tugatish!");

                    keyboardRows.add(resualt);
                    keyboardRows.add(stop);

                    keyboardMarkup.setKeyboard(keyboardRows);

                    Bot bot = new Bot();
                    send.setText("Test qabuli boshlandi.").setChatId(update.getMessage().getChatId())
                            .setReplyMarkup(keyboardMarkup);
                    bot.sendMessage(send);
                    path.replace(update.getMessage().getChatId(), "/start/add/run");
                }
            }
        } else {
            Bot bot = new Bot();
            bot.sendMessage(send.setText("Xato! Qayta kiriting.")
                    .setChatId(update.getMessage().getChatId()));
        }
    }

    //send answer menu
    public void sendAnswerMenu(Update update) {
        Bot bot = new Bot();
        bot.sendMessage(new SendMessage()
                .setText("Javoblaringizni kiriting!\n(Kod/Javoblar)\n\nMasalan:\n123/abcaaabb...")
                .setChatId(update.getMessage().getChatId()));
        path.replace(update.getMessage().getChatId(), "/start/send");
    }

    //send answer
    public void sendAnswer(Update update) {
        String updateText = update.getMessage().getText();

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        //config button
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        // natijalarni ko'rish
        KeyboardRow orqaga = new KeyboardRow();
        orqaga.add("Asosiy menuga qaytish");
        keyboardRows.add(orqaga);
        keyboardMarkup.setKeyboard(keyboardRows);

        if (updateText.contains("/")) {
            String code = updateText.substring(0, updateText.indexOf('/'));
            String answers = updateText.substring(updateText.indexOf('/') + 1);
            String exampleAnswer = adminRepository.existsByCode(code) ? adminRepository.findAdminByCode(code).getAnswers() : "";
            String fullName = update.getMessage().getFrom().getFirstName();


            if (fullName != null) {
                if (update.getMessage().getFrom().getLastName() != null) {
                    fullName.concat(" ").concat(update.getMessage().getFrom().getLastName());
                }
            } else fullName = "";
            if (!code.isEmpty() && !answers.isEmpty()) {
                if (pupilRepasitory.existsByFullNameAndCode(fullName, code)) {
                    Bot bot = new Bot();
                    bot.sendMessage(send.setText("Siz javoblaringizni jo'natib bo'lgansiz!")
                            .setChatId(update.getMessage().getChatId()).setReplyMarkup(keyboardMarkup));
                } else if (adminRepository.existsByCode(code)) {
                    Pupil pupil = new Pupil(fullName, checkTest(exampleAnswer, answers), code);
                    pupilRepasitory.save(pupil);
                    Bot bot = new Bot();
                    bot.sendMessage(send.setText("Javobingiz qabul qilindi!")
                            .setChatId(update.getMessage().getChatId()).setReplyMarkup(keyboardMarkup));
                } else {
                    Bot bot = new Bot();
                    bot.sendMessage(send.setText("Xato! Kodingizni tekshirib ko'ring")
                            .setChatId(update.getMessage().getChatId()).setReplyMarkup(keyboardMarkup));
                }
            }
        } else {
            Bot bot = new Bot();
            bot.sendMessage(send.setText("Xato! Kodingizni tekshirib ko'ring")
                    .setChatId(update.getMessage().getChatId()).setReplyMarkup(keyboardMarkup));
        }
    }

    // view resualt
    public void getResualt(Update update) {
        Admin admin = adminRepository.findAdminByUserName(update.getMessage().getFrom().getUserName());
        String resualtList = "";
        List<Pupil> pupilsList = pupilRepasitory.findAllByCodeOrderByTrueAnswersCountDesc(admin.getCode());
        for (int i = 0; i < pupilsList.size(); i++) {
            resualtList = resualtList.concat(String.valueOf(i + 1)).concat(". ")
                    .concat(pupilsList.get(i).getFullName()).concat("  -  ")
                    .concat(String.valueOf(pupilsList.get(i).getTrueAnswersCount())).concat("\n");
        }
        Bot bot = new Bot();
        bot.sendMessage(send.setText(resualtList).setChatId(update.getMessage().getChatId()));
    }

    // get path
    public String getPath(Long chatId) {
        return path.get(chatId);
    }

    //check test
    public Integer checkTest(String example, String answer) {
        while (example.length() > answer.length()) {
            answer.concat(" ");
        }
        Integer trueAnswers = 0;
        for (int i = 0; i < example.length(); i++) {
            if (example.substring(i, i + 1).equals(answer.substring(i, i + 1))) {
                trueAnswers++;
            }
        }
        return trueAnswers;
    }

    //clean data
    public void cleanTest(Update update) {
        if(adminRepository.existsByUserName(update.getMessage().getFrom().getUserName())){
            String code = adminRepository.findAdminByUserName(update
                    .getMessage().getFrom().getUserName()).getCode();
            if(pupilRepasitory.existsByCode(code)){
                pupilRepasitory.deletePupilsByCode(code);
            }
            adminRepository.deleteAdminByUserName(update.getMessage().getFrom().getUserName());
        }
    }

    @Scheduled(fixedRate = 180000)
    public void autoQuery(){
        try {
            URL url=new URL("https://www.google.com/");
            HttpURLConnection connection=(HttpURLConnection) url.openConnection();
            connection.connect();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
