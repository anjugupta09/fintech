package com.reloadly.notification.kafka;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class TransactionNotificationPayload implements Serializable  {

    private String transactionId;
    private Long senderAccountNumber;
    private String senderEmail;
    private String senderMobileNumber;
    private BigDecimal senderAccountBalance;
    private Long receiverAccountNumber;
    private String receiverEmail;
    private String receiverPhoneNumber;
    private BigDecimal receiverAccountBalance;
    private BigDecimal amountTransferred;

    public TransactionNotificationPayload() {

    }
}
