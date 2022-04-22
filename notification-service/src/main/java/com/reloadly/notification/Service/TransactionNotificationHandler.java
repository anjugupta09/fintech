package com.reloadly.notification.Service;

import com.reloadly.notification.Channel.NotificationChannelFactory;
import com.reloadly.notification.kafka.TransactionNotificationPayload;
import com.reloadly.notification.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@Service
public class TransactionNotificationHandler {

    @Autowired
    private NotificationChannelFactory channelFactory;

    public void sendNotification(TransactionNotificationPayload payload) {
        //sending notification to sender account user
        CompletableFuture.runAsync(() -> {
            Message debitNotification = createMessage(payload.getSenderEmail(),
                    getMessageBodyForDebit(payload.getAmountTransferred(),
                            payload.getSenderAccountBalance(),
                            payload.getSenderAccountNumber(),
                            payload.getReceiverAccountNumber()));
            channelFactory.notifyAll(debitNotification);
        });

        //sending notification to receiver account user
        CompletableFuture.runAsync(() -> {
            Message creditNotification = createMessage(payload.getReceiverEmail(),
                    getMessageBodyForCredit(payload.getAmountTransferred(),
                            payload.getReceiverAccountBalance(),
                            payload.getReceiverAccountNumber(),
                            payload.getSenderAccountNumber()));
            channelFactory.notifyAll(creditNotification);
        });
    }

    private Message createMessage(String toEmail, String body) {
        Message message = new Message();
        message.setEmail(toEmail);
        message.setSubject("Alert: Update on your bank account");
        message.setBody(body);
        return message;
    }

    private String getMessageBodyForDebit(BigDecimal amount, BigDecimal availableBalance, Long senderAccountNumber, Long receiverAccountNumber) {
        return "Your Account Number '" + senderAccountNumber + "' is debited by " + amount + " euro to Account Number '" + receiverAccountNumber + "' . Your new balance is: " + availableBalance;
    }

    private String getMessageBodyForCredit(BigDecimal amount, BigDecimal availableBalance, Long receiverAccountNumber, Long senderAccountNumber) {
        return "Your AccountNumber '" + receiverAccountNumber + "' is credited with " + amount + " euro by Account Number '" + senderAccountNumber + "' .Your new balance is: " + availableBalance;
    }
}
