package com.reloadly.transaction.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reloadly.constant.Constants;
import com.reloadly.kafka.EventPayload;
import com.reloadly.kafka.TransactionNotificationPayload;
import com.reloadly.kafka.producer.KafkaProducer;
import com.reloadly.model.entity.Account;
import com.reloadly.repository.AccountRepository;
import com.reloadly.transaction.exception.InsufficientBalanceException;
import com.reloadly.transaction.exception.InvalidAccountNumber;
import com.reloadly.transaction.model.Transaction;
import com.reloadly.transaction.model.TransactionType;
import com.reloadly.transaction.repository.TransactionRepository;
import com.reloadly.transaction.request.FundTransferRequest;
import com.reloadly.transaction.response.FundTransferResponse;
import com.reloadly.transaction.service.TransactionService;
import com.reloadly.transaction.utils.KafkaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Value(value = "${kafka.topic.name}")
    private String topic;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Override
    @Transactional
    public FundTransferResponse fundTransfer(FundTransferRequest fundTransferRequest) throws Exception {
        String correlationId = ThreadContext.get(Constants.X_CORRELATION_ID);
        log.info("Received the fund transfer request for correlationId: {}",correlationId);
        Account fromBankAccount = accountRepository.
                findById(fundTransferRequest.getFromAccount()).orElseThrow(() -> new InvalidAccountNumber("Error! Invalid Account Number"));

        Account toBankAccount = accountRepository.findById(fundTransferRequest.getToAccount())
                .orElseThrow(() -> new InvalidAccountNumber("Error! Invalid Account Number"));

        //validating account balances
        validateBalance(fromBankAccount, fundTransferRequest.getAmount());

        String transactionId = initiateFundTransfer(fromBankAccount, toBankAccount, fundTransferRequest.getAmount());

        publishToKafkaTopic(correlationId, transactionId, fundTransferRequest.getAmount(), fromBankAccount, toBankAccount);
        return FundTransferResponse.builder().message("Transaction successfully completed").transactionId(transactionId).build();
    }

    private void validateBalance(Account bankAccount, BigDecimal amount) throws InsufficientBalanceException {
        if (bankAccount.getBalance().compareTo(BigDecimal.ZERO) < 0 || bankAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Error! Your Bank account does not have sufficient funds to complete this request");
        }
    }

    private String initiateFundTransfer(Account fromBankAccount, Account toBankAccount, BigDecimal amount) {
        log.info("Initiating the fund transfer request");
        String transactionId = UUID.randomUUID().toString();

        fromBankAccount.setBalance(fromBankAccount.getBalance().subtract(amount));
        accountRepository.save(fromBankAccount);

        toBankAccount.setBalance(toBankAccount.getBalance().add(amount));
        accountRepository.save(toBankAccount);

        transactionRepository.save(Transaction.builder()
                .transactionType(TransactionType.FUND_TRANSFER)
                .transactionId(transactionId)
                .creditAccount(toBankAccount)
                .debitAccount(fromBankAccount)
                .amount(amount).build());

        return transactionId;
    }

    private void publishToKafkaTopic(String correlationId, String transactionId, BigDecimal amount, Account fromAccount, Account toAccount) {
        CompletableFuture.runAsync(() -> {
            log.info("Pushing the message with correlationId: {} to Kafka topic: {} ", topic, correlationId);
            EventPayload<TransactionNotificationPayload> payload = KafkaUtils.createTransactionNotificationPayload(correlationId, transactionId, amount, fromAccount, toAccount);
            ObjectMapper objectMapper = new ObjectMapper();
            String json = null;
            try {
                json = objectMapper.writeValueAsString(payload);
            } catch (JsonProcessingException e) {
                log.error("Some exception occurred while parsing the json");
            }
            kafkaProducer.sendMessage(topic, json);
        });

    }
}
