package org.jay.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class timeSeriesPriceData {
    @JsonProperty("timestamp")
    String timestamp;

    @JsonProperty("id")
    String id;

    @JsonProperty("inr")
    Number price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Number getPrice() {
        return price;
    }

    public void setPrice(Number price) {
        this.price = price;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void printObject(){
        System.out.println("Printing Object: ");
        System.out.println("ID: " + id + " Price: " + price + " Timestamp: " + timestamp);
        System.out.println("End of Object");
    }
}
