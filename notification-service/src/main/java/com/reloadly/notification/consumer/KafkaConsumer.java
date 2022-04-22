package com.reloadly.notification.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reloadly.notification.Service.TransactionNotificationHandler;
import com.reloadly.notification.Service.UserRegistrationNotificationHandler;
import com.reloadly.notification.kafka.EventPayload;
import com.reloadly.notification.kafka.TransactionNotificationPayload;
import com.reloadly.notification.kafka.UserRegistrationPayload;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumer {

    @Autowired
    private TransactionNotificationHandler transactionNotificationHandler;

    @Autowired
    private UserRegistrationNotificationHandler userRegistrationNotificationHandler;

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "notificationGroup")
    public void listener(String message) throws JsonProcessingException {
        log.info("Message received {} ", message);
        ObjectMapper mapper = new ObjectMapper();
        EventPayload<?> eventPayload = mapper.readValue(message, EventPayload.class);

        if ("USER_REGISTRATION".equals(eventPayload.getEventType())) {

            EventPayload<UserRegistrationPayload> payload = mapper.reader()
                    .forType(new TypeReference<EventPayload<UserRegistrationPayload>>() {})
                    .readValue(message);
            UserRegistrationPayload userRegistrationPayload = payload.getPayload();
            userRegistrationNotificationHandler.sendNotification(userRegistrationPayload);
        } else {
            EventPayload<TransactionNotificationPayload> payload = mapper.reader()
                    .forType(new TypeReference<EventPayload<TransactionNotificationPayload>>() {})
                    .readValue(message);
            TransactionNotificationPayload transactionNotificationPayload = payload.getPayload();
            transactionNotificationHandler.sendNotification(transactionNotificationPayload);
        }
        log.info("Message has been processed successfully");
    }
}
