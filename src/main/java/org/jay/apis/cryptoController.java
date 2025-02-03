package org.jay.apis;

//import io.vertx.redis.client.Response;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;
import org.jay.services.CryptoService;
import org.jay.services.priceService;
import java.util.List;
import java.util.Map;

@Path("/crypto")
public class cryptoController {

        @Inject
        CryptoService cryptoPriceGetter;

        @Inject
        priceService priceService;

        @GET
        @Path("/info/{id}")
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        public Response fetCryptoInfo(@PathParam("id") String id){
            return Response.ok(Map.of("Message",cryptoPriceGetter.saveDataToMongoUsingKafka(id))).build();
        }

        @GET
        @Path("/timeseries/price")
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        public Response fetchTimeSeriesData(@QueryParam("ids") String ids){
                String vs_currency = "inr";
                return Response.ok(Map.of("Message",priceService.getCoinsPrices(ids,"inr"))).build();
        }
}
