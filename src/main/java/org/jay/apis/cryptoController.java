package org.jay.apis;

//import io.vertx.redis.client.Response;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;
import org.jay.models.cryptoCoin;
import org.jay.services.CryptoService;
import org.jay.services.cryptoGetAllData;

import java.util.List;

@Path("/crypto")
public class cryptoController {

        @Inject
        CryptoService cryptoData;

        @Inject
        cryptoGetAllData cryptogetalldata;


        @GET
        @Path("/fetchCryptoDataFromAPI")
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        public Response fetchCryptoData(){
            //Store in DB
            try {
                String response = cryptoData.saveDataToMongoUsingKafka();
                return Response.ok("Data Saved Successfully....").build();
            } catch (Exception e) {
                return Response.ok("Error: " + e.getMessage()).build();
            }
        }


        @GET
        @Path("/getAllData")
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        public Response fetchAllData(){
             List<cryptoCoin> data = cryptogetalldata.data();
             return Response.ok(data).build();
        }
}
