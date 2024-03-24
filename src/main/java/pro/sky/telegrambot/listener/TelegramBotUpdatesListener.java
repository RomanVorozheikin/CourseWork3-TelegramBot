package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Notifications;
import pro.sky.telegrambot.repository.NotificationsRepository;
import pro.sky.telegrambot.service.NotificationService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private static final String MESSAGE_PATTERN = "([0-9\\.\\:\\s]{16})(\\s)([\\W\\+]+)";
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NotificationsRepository notificationsRepository;
    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            Message message = update.message();
            Long chatId = update.message().chat().id();
            if (message != null && message.text().equals("/start")) {
                notificationService.searchAndSend(chatId,
                        "Привет! Я бот. Напиши когда и какое уведомление прислать. Например: 14.12.2023 20:00 Встреча в кафе!");
            } else {
                Pattern pattern = Pattern.compile(MESSAGE_PATTERN);
                Matcher matcher = pattern.matcher(message.text());
                if (matcher.matches()) {
                    String dataTime = matcher.group(1);
                    String reminderText = matcher.group(3);

                    Notifications notifications = new Notifications();
                    notifications.setNotificationText(reminderText);
                    notifications.setNotificationDatetime(LocalDateTime
                            .parse(dataTime, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
                    notifications.setChatId(message.chat().id().intValue());

                    notificationsRepository.save(notifications);

                    notificationService.searchAndSend(chatId,"Хорошо, напомню:)");
                }else {
                    notificationService.searchAndSend(chatId,"Неправильный формат!");
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
