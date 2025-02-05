package org.jay.cryptoCoinMongoDb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;
import org.jay.models.cryptoCoin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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


    public List<cryptoCoin> fetchData() {
        try {
            List<cryptoCoin> cryptoCoins = new ArrayList<>();

            getCollection().find().forEach(doc -> {
                cryptoCoin coin = new cryptoCoin();
                coin.setId(doc.getString("id"));
                coin.setSymbol(doc.getString("symbol"));
                coin.setName(doc.getString("name"));
                coin.setWebSlug(doc.getString("web_slug"));
                coin.setBlockTimeInMinutes(doc.getInteger("block_time_in_minutes", 0));
                coin.setHashingAlgorithm(doc.getString("hashing_algorithm"));
                coin.setCategories(doc.getList("categories", String.class));
                coin.setLocalization(doc.get("localization", Map.class));
                coin.setImage(doc.get("image", Map.class));

                // Handling MarketData
                Document marketDataDoc = doc.get("market_data", Document.class);
                if (marketDataDoc != null) {
                    cryptoCoin.MarketData marketData = new cryptoCoin.MarketData();
                    marketData.setCurrentPrice(marketDataDoc.get("current_price", Map.class));
                    coin.setMarketData(marketData);
                }

                cryptoCoins.add(coin);
            });

            return cryptoCoins;
        } catch (Exception e) {
            LOGGER.severe("Error fetching data from MongoDB: " + e.getMessage());
            return Collections.emptyList();
        }
    }

}
