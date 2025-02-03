package org.jay.cryptoCoinMongoDb;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jay.models.cryptoCoin;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class cryptoDataStore {
    private static final Logger LOGGER = Logger.getLogger(cryptoDataStore.class);

    @Inject
    MongoClient mongoClient;  // MongoDB Client

    @Inject
    ObjectMapper objectMapper;  // Jackson Object Mapper

    private MongoCollection<Document> getCollection() {
        try {
            LOGGER.info("Connecting to MongoDB...");
            MongoDatabase database = mongoClient.getDatabase("crypto-tracker");
            LOGGER.info("Connected to database: " + database.getName());
            return database.getCollection("cryptoPrices");
        } catch (Exception e) {
            LOGGER.error("MongoDB Connection Error: " + e.getMessage(), e);
            throw e; // Ensure the error is propagated
        }
    }


    public void saveData(cryptoCoin response) {
        try {
            // Check if objectMapper is injected properly
            if (objectMapper == null) {
                throw new IllegalStateException("ObjectMapper is not initialized!");
            }
            // Check if the response is empty
            if (response == null) {
                LOGGER.error("Error: Empty or Null response received.");
                return;
            }


            // Convert the cryptoCoin object to a MongoDB document and insert into MongoDB
            Document doc = objectMapper.convertValue(response, Document.class);
            getCollection().insertOne(doc);
            LOGGER.info("Data saved successfully for " + response.getId());
        } catch (Exception e) {
            LOGGER.error("Error saving crypto data to MongoDB", e);
        }
    }
}
