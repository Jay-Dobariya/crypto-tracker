package org.jay.cryptoCoinMongoDb;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.Document;
import jakarta.inject.Inject;
import org.jay.models.timeSeriesPriceData;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.logging.Logger;

@ApplicationScoped
public class cryptoTimeSeriesDataStore {
    private static final Logger LOG = Logger.getLogger(cryptoTimeSeriesDataStore.class.getName());

    @Inject
    MongoClient mongoClient;

    @Inject
    ObjectMapper objectMapper;

    private MongoCollection<Document> getCollection() {
        try {
            LOG.info("Connecting to MongoDB...");
            MongoDatabase database = mongoClient.getDatabase("crypto-tracker");
            LOG.info("Connected to database: " + database.getName());
            return database.getCollection("cryptoTimeSeriesData");
        } catch (Exception e) {
            LOG.severe("MongoDB Connection Error: " + e.getMessage());
            throw e;
        }
    }

    public void saveTimeSeriesData(String jsonResponse){
        try{
            if(objectMapper==null){
                throw new IllegalStateException("ObjectMapper is not initialized!");
            }
            if(jsonResponse==null || jsonResponse.isEmpty()){
                LOG.severe("Error: Empty or Null response received.");
                return;
            }

            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            LOG.info(jsonResponse);
            rootNode.fieldNames().forEachRemaining(coinId -> {
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Format it as "yyyy-MM-dd HH:mm:ss"
                String formattedDateTime = now.format(formatter);


                JsonNode coinData = rootNode.get(coinId);
                timeSeriesPriceData priceData = new timeSeriesPriceData();
                priceData.setId(coinId);
                priceData.setPrice(coinData.path("inr").numberValue());
                priceData.setTimestamp(formattedDateTime);

                priceData.printObject();

                Document coinDocument =  objectMapper.convertValue(priceData, Document.class);
                // Insert into MongoDB
                getCollection().insertOne(coinDocument);
                LOG.info("Data saved successfully for " + coinId);
            });
        } catch (Exception e) {
            LOG.severe("Error saving data to MongoDB(TimeSeries Data): " + e.getMessage());
        }
    }
}
