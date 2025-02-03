package org.jay.cryptoCoinMongoDb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;
import org.jay.models.cryptoCoin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class fetchCryptoDataFromMongo {

    private static final Logger LOGGER = Logger.getLogger(fetchCryptoDataFromMongo.class.getName());

    @Inject
    MongoClient mongoClient;


    private MongoCollection<Document> getCollection() {
        try {
            LOGGER.info("Connecting to MongoDB...");
            MongoDatabase database = mongoClient.getDatabase("crypto-tracker");
            LOGGER.info("Connected to database: " + database.getName());
            return database.getCollection("cryptoPrices");
        } catch (Exception e) {
            LOGGER.severe("MongoDB Connection Error: " + e.getMessage());
            throw e; // Ensure the error is propagated
        }
    }


    public List<cryptoCoin> fetchData(){
        return new ArrayList<>();
    }

}
