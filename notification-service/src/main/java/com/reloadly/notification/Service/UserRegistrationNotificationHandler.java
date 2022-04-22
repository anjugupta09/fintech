package com.reloadly.notification.Service;

import com.reloadly.notification.Channel.NotificationChannelFactory;
import com.reloadly.notification.kafka.UserRegistrationPayload;
import com.reloadly.notification.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class UserRegistrationNotificationHandler {

    @Autowired
    private NotificationChannelFactory channelFactory;

    public void sendNotification(UserRegistrationPayload payload) {
        //sending notification to sender
        CompletableFuture.runAsync(() -> {
            Message registrationNotification = createMessage(payload.getEmail(), payload.getMobileNumber(),
                    getMessageBody(payload.getFirstName(), payload.getAccountNumber()));

            channelFactory.notifyAll(registrationNotification);
        });
    }

    private Message createMessage(String email, String mobileNumber, String body) {
        Message msg = new Message();
        msg.setEmail(email);
        msg.setBody(mobileNumber);
        msg.setBody(body);
        msg.setSubject("Congrats! Welcome to our bank");
        return msg;
    }

    private String getMessageBody(String firstName, Long accountNumber) {
        return "Dear " + firstName + ", you have been successfully registered with us. Your new account number is " + accountNumber;
    }

}
