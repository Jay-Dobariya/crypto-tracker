package org.jay.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jay.client.CoinGeckoClient;
import org.jay.cryptoCoinMongoDb.cryptoTimeSeriesDataStore;


import java.util.logging.Logger;


@ApplicationScoped
public class priceService {

    private static final Logger LOG = Logger.getLogger(CryptoService.class.getName());

    @RestClient
    @Inject
    CoinGeckoClient coinGeckoClient;

    @Inject
    cryptoTimeSeriesDataStore datasotre;

    public String getCoinsPrices(String ids, String vs_currencies){
        try {
            String jsonResponse = coinGeckoClient.getCoinPrice(ids, vs_currencies);
            datasotre.saveTimeSeriesData(jsonResponse);
            return "Data saved in TimeSeries Collection.....";
        }catch (Exception e){
            LOG.severe("Code Fattaaaaa in getCoinPrice(): " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }
}
