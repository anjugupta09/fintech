package com.reloadly.kafka;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class TransactionNotificationPayload {

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
}
