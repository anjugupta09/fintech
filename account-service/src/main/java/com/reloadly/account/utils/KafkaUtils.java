package com.reloadly.account.utils;

import com.reloadly.kafka.EventPayload;
import com.reloadly.kafka.UserRegistrationPayload;
import com.reloadly.model.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class KafkaUtils {

    //Creating the kafka payload for sending notification
    public static EventPayload<UserRegistrationPayload> createUserRegistrationPayload(User user, String correlationId) {
        EventPayload<UserRegistrationPayload> eventPayload = new EventPayload<>();
        eventPayload.setCorrelationId(correlationId);
        eventPayload.setEventType("USER_REGISTRATION");
        eventPayload.setTimestamp(LocalDateTime.now().toString());
        eventPayload.setPayload(UserRegistrationPayload.builder().
                firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail()).accountNumber(user.getAccounts().get(0).getId()).build());
        return eventPayload;
    }
}
