package org.jay.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.Response;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jay.client.CoinGeckoClient;
import org.jay.cryptoCoinMongoDb.cryptoDataStore;
import org.jay.models.cryptoCoin;

import java.util.logging.Logger;


@ApplicationScoped
public class CryptoService {

    //Create Logger
    private static final Logger LOG = Logger.getLogger(CryptoService.class.getName());

    @RestClient
    @Inject
    CoinGeckoClient coinGeckoClient;

    @Inject
    kafkaProducer producer;


    public String saveDataToMongoUsingKafka(String id){
        try{
            cryptoCoin coindata = new cryptoCoin();
            coindata = coinGeckoClient.getCoinInfo(id);
            producer.produceData(coindata);
            return "Data Saved Successfully in coinData Collection.....";
        }catch (Exception e){
            LOG.severe("Code Fattaaaaa in saveDataToMongo(): " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

}
