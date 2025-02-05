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
import java.time.ZonedDateTime;
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
                coin.setImage(doc.getString("image"));
                coin.setCurrentPrice(doc.getDouble("current_price"));
                coin.setMarketCap(doc.getLong("market_cap"));
                coin.setMarketCapRank(doc.getInteger("market_cap_rank"));
                coin.setTotalVolume(doc.getLong("total_volume"));
                coin.setHigh24h(doc.getDouble("high_24h"));
                coin.setLow24h(doc.getDouble("low_24h"));
                coin.setPriceChangePercentage24h(doc.getDouble("price_change_percentage_24h"));
                coin.setMarketCapChangePercentage24h(doc.getDouble("market_cap_change_percentage_24h"));
                coin.setTotalSupply(doc.getDouble("total_supply"));
                coin.setCirculatingSupply(doc.getDouble("circulating_supply"));
                coin.setAth(doc.getDouble("ath"));
                coin.setAthChangePercentage(doc.getDouble("ath_schange_percentage"));
                coin.setAthDate(doc.getString("ath_date"));
                coin.setAtl(doc.getDouble("atl"));
                coin.setAtlChangePercentage(doc.getDouble("atl_change_percentage"));
                coin.setAtlDate(doc.getString("atl_date"));
                cryptoCoins.add(coin);
            });

            return cryptoCoins;
        } catch (Exception e) {
            LOGGER.severe("Error fetching data from MongoDB: " + e.getMessage());
            return Collections.emptyList();
        }
    }

}
