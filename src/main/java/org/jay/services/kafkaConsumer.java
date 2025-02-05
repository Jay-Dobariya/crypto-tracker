package org.jay.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.reactive.messaging.kafka.IncomingKafkaRecord;
//import io.vertx.redis.client.impl.RedisClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jay.cryptoCoinMongoDb.cryptoDataStore;
import org.jay.models.cryptoCoin;
import io.quarkus.redis.client.RedisClient;


import java.util.List;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class kafkaConsumer {
    @Inject
    ObjectMapper objectMapper;

    @Inject
    cryptoDataStore mongoTable;

    @Inject
    RedisClient redisclient;

    @Incoming("crypto-price-channel")
    @Transactional
    public CompletionStage<Void> consumeData(IncomingKafkaRecord<String,String> Record){
        try{
            String key = Record.getKey();
            String value = Record.getPayload();

            // Check if objectMapper is injected properly
            if (objectMapper == null) {
                throw new IllegalStateException("ObjectMapper is not initialized!");
            }
            // Check if the response is empty
            if (value == null) {
                System.out.println("Error: Empty or Null response received.");
                throw new IllegalStateException("Empty or Null response received.");
            }

            // Convert the cryptoCoin object to a MongoDB document and insert into MongoDB
            cryptoCoin deserializedResponse = objectMapper.readValue(value,cryptoCoin.class);

            //If data is already there it will update the current data o/w store new key:value pair
            String id = deserializedResponse.getId();
            String price = deserializedResponse.getCurrentPrice().toString();
            redisclient.set(List.of(id, price));
            System.out.println("Stored in Redis Catch: " + id + " -> " + price);

            //Storing data in MongoDB
            mongoTable.saveData(deserializedResponse);

            return Record.ack().thenRun(() -> {
                System.out.println("Acknowledged message with key " + key + " and value " + value);
            });

        }catch (Exception e){
            System.out.println("Error in consumeData(): " + e.getMessage());
            return Record.nack(e); // NAcknowledge the message
        }
    }
}
