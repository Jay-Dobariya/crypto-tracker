package org.jay.cryptoCoinMongoDb;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import com.mongodb.client.model.Filters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jay.models.cryptoCoin;
import org.jboss.logging.Logger;
import static com.mongodb.client.model.Filters.eq;


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

            //Check if the coin with same name exist in our database or not
            Document existingCoin = getCollection().find(eq("id",response.getId())).first();
            if(existingCoin!=null){

                getCollection().updateOne(
                        Filters.eq("id", response.getId()),
                        Updates.combine(
                                Updates.set("symbol", response.getSymbol()),
                                Updates.set("name", response.getName()),
                                Updates.set("image", response.getImage()),
                                Updates.set("currentPrice", response.getCurrentPrice()),
                                Updates.set("marketCap", response.getMarketCap()),
                                Updates.set("marketCapRank", response.getMarketCapRank()),
                                Updates.set("totalVolume", response.getTotalVolume()),
                                Updates.set("high24h", response.getHigh24h()),
                                Updates.set("low24h", response.getLow24h()),
                                Updates.set("priceChangePercentage24h", response.getPriceChangePercentage24h()),
                                Updates.set("marketCapChangePercentage24h", response.getMarketCapChangePercentage24h()),
                                Updates.set("totalSupply", response.getTotalSupply()),
                                Updates.set("circulatingSupply", response.getCirculatingSupply()),
                                Updates.set("ath", response.getAth()),
                                Updates.set("athChangePercentage", response.getAthChangePercentage()),
                                Updates.set("athDate", response.getAthDate()),
                                Updates.set("atl", response.getAtl()),
                                Updates.set("atlChangePercentage", response.getAtlChangePercentage()),
                                Updates.set("atlDate", response.getAtlDate()),
                                Updates.set("lastUpdated", response.getLastUpdated())
                        )
                );
                LOGGER.info("Data updated successfully for " + response.getId());
            }
            else {
                getCollection().insertOne(doc);
                LOGGER.info("Data saved successfully for " + response.getId());
            }
        } catch (Exception e) {
            LOGGER.error("Error saving crypto data to MongoDB", e);
        }
    }
}
