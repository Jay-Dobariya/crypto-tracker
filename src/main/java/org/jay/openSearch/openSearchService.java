package org.jay.openSearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jay.models.cryptoCoin;


import org.opensearch.action.delete.DeleteRequest;
import org.opensearch.action.delete.DeleteResponse;
import org.opensearch.action.get.GetRequest;
import org.opensearch.action.get.GetResponse;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.index.IndexResponse;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.common.xcontent.XContentType;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.index.reindex.DeleteByQueryRequest;
import org.opensearch.search.SearchHit;
import org.opensearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Singleton                      //Initialized when the application is started
public class openSearchService {

    @Inject
    openSearchClient openSearchClient;

    private static final Logger LOGGER = Logger.getLogger(openSearchService.class.getName());

    private static final String INDEX_NAME = "crypto_prices";

    public String createIndex(cryptoCoin data){
        try {
            RestHighLevelClient client = openSearchClient.getClient();
            String ID=data.getId();
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(data);
            LOGGER.info("Creating index for: " + data.getId());
            LOGGER.info(json);

            // Create index for the data
            IndexRequest request = new IndexRequest(INDEX_NAME)
                    .id(ID)
                    .source(json, XContentType.JSON);

            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            return response.getId() + "Indexed Successfully";
        } catch (Exception e) {
            LOGGER.severe("Error in creating index: " + e.getMessage());
            return "Error in creating Index";
        }
    }

    public boolean documentExist(cryptoCoin data) throws IOException{
        RestHighLevelClient client = openSearchClient.getClient();
        GetRequest request = new GetRequest(INDEX_NAME, data.getId());
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        return response.isExists();
    }


    public List<String> searchQuery(String queryText) throws IOException {
        RestHighLevelClient client = openSearchClient.getClient();


        //Create the search request for particular INDEX
        SearchRequest request = new SearchRequest(INDEX_NAME);

        //Creating source builder for creating query
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.queryStringQuery("*" + queryText + "*"));
        request.source(sourceBuilder);

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        //Extracting the result from the response
        List<String> results = new ArrayList<>();
        for (SearchHit hit : response.getHits().getHits()) {
            results.add(hit.getSourceAsString()); // Returns the full document as JSON
        }
        System.out.println(results);
        return results;

    }


}
