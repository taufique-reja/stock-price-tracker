package com.stock_price.tracker.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock_price.tracker.model.StockData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StockService {

    @Value("${alphavantage.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, CachedStock> cache = new ConcurrentHashMap<>();

    private static class CachedStock {
        StockData data;
        Instant time;
        CachedStock(StockData data) { this.data = data; this.time = Instant.now(); }
    }

    public StockData getStockData(String symbolRaw) {
        String symbol = symbolRaw.toUpperCase();
        CachedStock cached = cache.get(symbol);
        if (cached != null && Instant.now().minusSeconds(300).isBefore(cached.time)) {
            System.out.println("♻️ Returning cached data for: " + symbol);
            return cached.data;
        }

        try {
            String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol="
                    + symbol + "&apikey=" + apiKey;

            String response = restTemplate.getForObject(url, String.class);
            JsonNode node = mapper.readTree(response).path("Global Quote");

            if (node.isMissingNode() || node.size() == 0)
                throw new RuntimeException("No data found for symbol: " + symbol);

            StockData sd = new StockData();
            sd.setSymbol(symbol);
            sd.setCompanyName(symbol);
            sd.setPrice(node.path("05. price").asDouble());
            sd.setChange(node.path("09. change").asDouble());
            sd.setChangePercent(node.path("10. change percent").asText());
            sd.setHigh(node.path("03. high").asDouble());
            sd.setLow(node.path("04. low").asDouble());
            double p = sd.getPrice();
            sd.setChart(new double[]{p - 10, p - 5, p, p + 5, p + 10});

            cache.put(symbol, new CachedStock(sd));
            System.out.println("✅ Data fetched from Alpha Vantage for: " + symbol);
            return sd;

        } catch (Exception e) {
            System.err.println("⚠️ Alpha Vantage failed for " + symbol + ": " + e.getMessage());
            if (cache.get(symbol) != null) {
                System.out.println("🕒 Returning cached data for: " + symbol);
                return cache.get(symbol).data;
            }
            throw new RuntimeException("Failed to fetch data for " + symbol + ": " + e.getMessage(), e);
        }
    }
}
