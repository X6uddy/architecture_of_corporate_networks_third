package com.example.restapp.messaging;

import com.example.restapp.model.ChangeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class NotificationListener
{
    private static final Logger log = LoggerFactory.getLogger(NotificationListener.class);

    private final JavaMailSender mailSender;
    private final List<String> recipients;
    private final int pictureYearThreshold;

    public NotificationListener(JavaMailSender mailSender,
                                @Value("${app.notifications.recipients}") String recipients,
                                @Value("${app.notifications.picture-year-threshold:2000}") int pictureYearThreshold)
    {
        this.mailSender = mailSender;
        this.recipients = Arrays.stream(recipients.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .toList();
        this.pictureYearThreshold = pictureYearThreshold;
    }

    @JmsListener(destination = "${app.messaging.change-events-topic}", containerFactory = "jmsListenerContainerFactory")
    public void onChangeEvent(ChangeEvent event)
    {
        if (event == null || event.getEntityName() == null)
        {
            log.warn("Skip notification: empty event {}", event);
            return;
        }

        if (!shouldNotify(event))
        {
            return;
        }

        if (recipients.isEmpty())
        {
            log.warn("No recipients configured for notifications, skip sending for {}", event);
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipients.toArray(new String[0]));
        message.setSubject("Entity change: " + event.getEntityName());
        message.setText(buildBody(event));
        mailSender.send(message);
        log.info("Notification sent for {} {}", event.getEntityName(), event.getEntityId());
    }

    private boolean shouldNotify(ChangeEvent event)
    {
        if (!"Picture".equalsIgnoreCase(event.getEntityName()))
        {
            return false;
        }

        if (event.getChangeType() == ChangeType.DELETE)
        {
            return true;
        }

        Map<String, Object> payload = event.getPayload();
        if (payload == null)
        {
            return false;
        }

        Object yearObj = payload.get("year");
        if (yearObj instanceof Number number)
        {
            return number.intValue() >= pictureYearThreshold;
        }
        return false;
    }

    private String buildBody(ChangeEvent event)
    {
        return """
                Изменена сущность %s
                Тип изменения: %s
                Идентификатор: %s
                Данные: %s
                """
                .formatted(event.getEntityName(), event.getChangeType(), event.getEntityId(), event.getPayload());
    }
}


