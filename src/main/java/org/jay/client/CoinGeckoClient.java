package org.jay.client;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.*;
import org.apache.kafka.common.protocol.types.Field;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import jakarta.ws.rs.core.MediaType;
import org.jay.models.cryptoCoin;

import java.util.List;


@RegisterRestClient(baseUri = "https://api.coingecko.com/api/v3")
public interface CoinGeckoClient {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/simple/price")
    String getCoinPrice(
            @QueryParam("ids") String ids,
            @QueryParam("vs_currencies") String vs_currencies
    );

    @GET
    @Path("/coins/markets")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    List<cryptoCoin> getAllCoinData(
        @QueryParam("vs_currency") String vs_currency,
        @QueryParam("order") String order,
        @QueryParam("per_page") String per_page,
        @QueryParam("page") String page
    );
}
