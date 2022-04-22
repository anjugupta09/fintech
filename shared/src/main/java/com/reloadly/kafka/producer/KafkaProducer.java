package com.reloadly.kafka.producer;

import com.reloadly.kafka.EventPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;


@Component
@Slf4j
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, String kafkaPayload) {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, kafkaPayload) ;
        future.addCallback(new ListenableFutureCallback<>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.debug("Message {} has been sent", kafkaPayload.toString());
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Something went wrong with the message {} ", kafkaPayload.toString());
            }
        }) ;
    }
}


