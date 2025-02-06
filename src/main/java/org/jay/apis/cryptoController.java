package org.jay.apis;

//import io.vertx.redis.client.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;
import org.jay.models.cryptoCoin;
import org.jay.services.CryptoService;
import org.jay.services.cryptoGetAllData;
import org.jay.openSearch.openSearchService;
import org.jay.openSearch.openSearchClient;


import java.util.ArrayList;
import java.util.List;

@Path("/crypto")
public class cryptoController {

        @Inject
        CryptoService cryptoData;

        @Inject
        cryptoGetAllData cryptogetalldata;

        @Inject
        openSearchService serviceClient;

        @Inject
        openSearchClient client;

        @Inject
        ObjectMapper objectmapper;



    @GET
        @Path("/fetchCryptoDataFromAPI")
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        public Response fetchCryptoData(){
            //Store in DB
            try {
                cryptoData.saveDataToMongoUsingKafka();
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

        @GET
        @Path("/search/{query}")
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        public Response search(@PathParam("query") String query){
            List<cryptoCoin> finalData = new ArrayList<>();

            try {
                List<String> openSearchResult = serviceClient.searchQuery(query);
                openSearchResult.forEach(child->{
                    try {
                        cryptoCoin data = objectmapper.readValue(child, cryptoCoin.class);
                        finalData.add(data);
                    }
                    catch (Exception e) {
                        throw new RuntimeException("Failed to search coin data", e);
                    }
                });
                return Response.ok(finalData).build();
            } catch (Exception e) {
                return Response.ok("Error: " + e.getMessage()).build();
            }

        }
}
