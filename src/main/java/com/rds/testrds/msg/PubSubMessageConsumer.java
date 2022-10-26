package com.rds.testrds.msg;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.pubsub.v1.PubsubMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.integration.AckMode;
import org.springframework.cloud.gcp.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import org.springframework.cloud.gcp.pubsub.support.BasicAcknowledgeablePubsubMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.function.Consumer;

@Component
public class PubSubMessageConsumer {

    @Value("${spring.cloud.gcp.topic-name-sub}")
    private String SUB_NM;

    @Autowired
    private PubSubTemplate pubSubTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(PubSubMessagePublisher.class);

    @Bean
    public MessageChannel inboundMessageChannel(){
        return new PublishSubscribeChannel();
    }

    @Bean
    public PubSubInboundChannelAdapter pubSubInboundChannelAdapter(@Qualifier("inboundMessageChannel") MessageChannel messageChannel){
        PubSubInboundChannelAdapter adapter
                = new PubSubInboundChannelAdapter(pubSubTemplate, SUB_NM);
        adapter.setOutputChannel(messageChannel);
        adapter.setAckMode(AckMode.MANUAL);

        //adapter.setPayloadType(T);
        return adapter;
    }

    /*@ServiceActivator(inputChannel = "inboundMessageChannel")
    public void messageReceiver(
            Map msg,
            @Header(GcpPubSubHeaders.ORIGINAL_MESSAGE) BasicAcknowledgeablePubsubMessage message) {
        logger.info("Message: {}", msg);
        // TODO: implement your code here
        // 메시지 처리 완료 응답
        logger.info("data: {}", message.getPubsubMessage().getData());
        message.ack();
    }*/

    public void consume(BasicAcknowledgeablePubsubMessage acknowledgeablePubsubMessage){
        PubsubMessage message = acknowledgeablePubsubMessage.getPubsubMessage();
        Map<String, String> map = new HashMap<>();
        String str = message.getData().toStringUtf8().replaceAll("\\\\", "");
        str = str.substring(1, str.length()-1);

        try{
            logger.info("get message: " + message.getData().toString());
            logger.info("get message: " + message.getData().toStringUtf8());
            logger.info("map: " + str);

            //List<Map<String, String>> mapList = objectMapper.readValue(message.getData().toString(), new TypeReference<List<Map<String, String>>>() {});
            map = objectMapper.readValue(str, Map.class);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("error message data: " + message.getData().toString());
        }

        acknowledgeablePubsubMessage.ack();
    }

    public Consumer<BasicAcknowledgeablePubsubMessage> consumer(){
        return acknowledgeablePubsubMessage -> {
            consume(acknowledgeablePubsubMessage);
        };
    }

    @EventListener(ApplicationReadyEvent.class)
    public void subscribe(){
        logger.info("subscribe: " + this.getClass().getSimpleName());
        pubSubTemplate.subscribe(SUB_NM, consumer());
    }
}
