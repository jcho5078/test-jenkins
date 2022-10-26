package com.rds.testrds.msg;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PublishRequest;
import com.google.pubsub.v1.PubsubMessage;
import com.rds.testrds.entity.OrderEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

@Component
public class PubSubMessagePublisher {

    @Autowired
    private PubSubTemplate pubSubTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${spring.cloud.gcp.topic-name}")
    private String PUB_NM;

    //orderEntity 전송
    public void publish(Object obj) throws ExecutionException, InterruptedException, JsonProcessingException {

        String message = objectMapper.writeValueAsString(obj);

        String result = pubSubTemplate.publish(this.PUB_NM, message).get();

        //ListenableFuture<String> result = pubSubTemplate.publish(this.PUB_NM, objectMapper.convertValue(message, Map.class));
        System.out.println(result);
    }
}