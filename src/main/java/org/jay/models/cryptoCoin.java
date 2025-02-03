package org.jay.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class cryptoCoin {

    @JsonProperty("id")
    private String id;

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("name")
    private String name;

    @JsonProperty("web_slug")
    private String webSlug;

    @JsonProperty("block_time_in_minutes")
    private Integer blockTimeInMinutes;

    @JsonProperty("hashing_algorithm")
    private String hashingAlgorithm;

    @JsonProperty("categories")
    private List<String> categories;

    @JsonProperty("localization")
    private Map<String, String> localization;

    @JsonProperty("image")
    private Map<String, String> image;

    @JsonProperty("market_data")
    private MarketData marketData;

    // Inner class to represent `current_price` data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MarketData {
        @JsonProperty("current_price")
        private Map<String, Double> currentPrice;

        public Map<String, Double> getCurrentPrice() {
            return currentPrice;
        }

        public void setCurrentPrice(Map<String, Double> currentPrice) {
            this.currentPrice = currentPrice;
        }
    }

    public cryptoCoin() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebSlug() {
        return webSlug;
    }

    public void setWebSlug(String webSlug) {
        this.webSlug = webSlug;
    }

    public Integer getBlockTimeInMinutes() {
        return blockTimeInMinutes;
    }

    public void setBlockTimeInMinutes(Integer blockTimeInMinutes) {
        this.blockTimeInMinutes = blockTimeInMinutes;
    }

    public String getHashingAlgorithm() {
        return hashingAlgorithm;
    }

    public void setHashingAlgorithm(String hashingAlgorithm) {
        this.hashingAlgorithm = hashingAlgorithm;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public Map<String, String> getLocalization() {
        return localization;
    }

    public void setLocalization(Map<String, String> localization) {
        this.localization = localization;
    }

    public Map<String, String> getImage() {
        return image;
    }

    public void setImage(Map<String, String> image) {
        this.image = image;
    }

    public MarketData getMarketData() {
        return marketData;
    }

    public void setMarketData(MarketData marketData) {
        this.marketData = marketData;
    }

    public void printCoinDetails(){
        //Write function to print all the things of object
        System.out.println("ID: " + id);
        System.out.println("Symbol: " + symbol);
        System.out.println("Name: " + name);
        System.out.println("Web Slug: " + webSlug);
        System.out.println("Block Time in Minutes: " + blockTimeInMinutes);
        System.out.println("Hashing Algorithm: " + hashingAlgorithm);
        System.out.println("Categories: " + categories);
        System.out.println("Localization: " + localization);
        System.out.println("Image: " + image);
        System.out.println("Market Data: " + marketData);
    }
}
