package org.jay.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.scheduler.Scheduled;
import jakarta.ws.rs.core.Response;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jay.client.CoinGeckoClient;
import org.jay.cryptoCoinMongoDb.cryptoDataStore;
import org.jay.models.cryptoCoin;

import java.util.List;
import java.util.concurrent.CompletionStage;
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


//    @Scheduled(every = "1m")
    public void saveDataToMongoUsingKafka(){
        try{
            List<cryptoCoin> coindata = coinGeckoClient.getAllCoinData("inr","market_cap_desc","100","1");
            for(cryptoCoin coin : coindata){
                producer.produceData(coin);
            }
            LOG.info("Data Saved Successfully in coinData Collection.....");
        }catch (Exception e){
            LOG.severe("Code Fattaaaaa in saveDataToMongoUsingKafka(): " + e.getMessage());
        }
    }

}
