package org.jay.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jay.cryptoCoinMongoDb.fetchCryptoDataFromMongo;
import org.jay.models.cryptoCoin;

import java.util.List;
import java.util.logging.Logger;

import java.util.Collections;

@ApplicationScoped
public class cryptoGetAllData {
    private static final Logger LOGGER = Logger.getLogger(cryptoGetAllData.class.getName());

    @Inject
    fetchCryptoDataFromMongo obj;

    public List<cryptoCoin> data(){
        try {
            List<cryptoCoin> outputData = obj.fetchData();
            if(outputData.isEmpty()){
                LOGGER.info("No data found in the database.");
                return Collections.emptyList();
            }
            return outputData;
        } catch (Exception e) {
            LOGGER.info("Error in cryptogetalldata: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
