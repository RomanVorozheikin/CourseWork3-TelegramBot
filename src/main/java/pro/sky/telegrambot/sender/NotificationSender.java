package pro.sky.telegrambot.sender;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class NotificationSender {
    private final TelegramBot telegramBot;

    public NotificationSender(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void sendNotification(String chatId, String text) {
        SendMessage sendMessage = new SendMessage(chatId, text);
        telegramBot.execute(sendMessage);
    }
}
