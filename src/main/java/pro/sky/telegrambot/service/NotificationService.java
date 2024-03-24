package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final TelegramBot telegramBot;

    public NotificationService(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void searchAndSend(Long chatId,String text) {
            SendMessage sendMessage = new SendMessage(chatId,text);
            telegramBot.execute(sendMessage);
    }

    public void searchAndSend(int chatId,String text) {
        SendMessage sendMessage = new SendMessage(chatId,text);
        telegramBot.execute(sendMessage);
    }
}
