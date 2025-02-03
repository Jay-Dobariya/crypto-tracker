package org.jay.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jay.models.cryptoCoin;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.concurrent.CompletionStage;
import java.util.logging.Logger;

@ApplicationScoped
public class kafkaProducer {

    private static final Logger logger = Logger.getLogger(kafkaProducer.class.getName());

    @Inject
    ObjectMapper objectMapper;

    @Channel("crypto-price-producer")
    Emitter<String> emitter;

    public void produceData(cryptoCoin data){
        try{
            if(objectMapper==null){
                throw new IllegalStateException("ObjectMapper is not initialized!");
            }
            if(data==null){
                logger.severe("Error: Empty or Null response received.");
                return;
            }

            String serializedData = objectMapper.writeValueAsString(data);
            CompletionStage<Void> ack= emitter.send(serializedData); //Something that will be done in the future
            ack.whenComplete((result, error) -> {
                if(error != null){
                    logger.severe("Error sending data to Kafka: " + error.getMessage());
                }else{
                    logger.info("Data sent to Kafka successfully: " + serializedData);
                }
            });
        } catch(Exception e) {
            logger.severe("Error sending data to Kafka: " + e.getMessage());
        }
    }
}
