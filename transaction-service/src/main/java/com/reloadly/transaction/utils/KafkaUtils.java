package com.reloadly.transaction.utils;

import com.reloadly.kafka.EventPayload;
import com.reloadly.kafka.TransactionNotificationPayload;
import com.reloadly.model.entity.Account;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class KafkaUtils {

    public static EventPayload<TransactionNotificationPayload> createTransactionNotificationPayload(String correlationId, String transactionId, BigDecimal amount, Account fromAccount, Account toAccount) {
        EventPayload<TransactionNotificationPayload> eventPayload = new EventPayload<>();
        eventPayload.setCorrelationId(correlationId);
        eventPayload.setEventType("FUND_TRANSFER");
        eventPayload.setTimestamp(LocalDateTime.now().toString());

        eventPayload.setPayload(TransactionNotificationPayload.builder()
                .transactionId(transactionId)
                .senderAccountNumber(fromAccount.getId())
                .senderEmail(fromAccount.getUser().getEmail())
                .senderMobileNumber(fromAccount.getUser().getMobileNumber())
                .senderAccountBalance(fromAccount.getBalance())
                .receiverAccountNumber(toAccount.getId())
                .receiverPhoneNumber(toAccount.getUser().getMobileNumber())
                .receiverEmail(toAccount.getUser().getEmail())
                .receiverAccountBalance(toAccount.getBalance())
                .amountTransferred(amount).build());
        return eventPayload;
    }
}
