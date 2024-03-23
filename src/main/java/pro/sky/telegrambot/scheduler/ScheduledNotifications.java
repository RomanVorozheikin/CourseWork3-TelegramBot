package pro.sky.telegrambot.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.model.Notifications;
import pro.sky.telegrambot.repository.NotificationsRepository;
import pro.sky.telegrambot.sender.NotificationSender;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class ScheduledNotifications {
    private final NotificationsRepository notificationsRepository;
    private final NotificationSender notificationSender;

    @Autowired
    public ScheduledNotifications(NotificationsRepository notificationsRepository, NotificationSender notificationSender) {
        this.notificationsRepository = notificationsRepository;
        this.notificationSender = notificationSender;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void executeEveryMinute() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        List<Notifications> notifications = notificationsRepository.findByNotificationDatetime(now);

        for (Notifications notification : notifications) {
            notificationSender.sendNotification(Long.toString(notification.getChatId()), notification.getNotificationText());
        }
        notificationsRepository.deleteAll(notifications);
    }
}
