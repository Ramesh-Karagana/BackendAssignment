package com.example.BackendAssignment.Service;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;


@Service
public class CryptoService {
    @Value("${coinmarketcap.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public CryptoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getCoinData(String symbol) {
        // Build URI for the CoinMarketCap API
        URI uri = UriComponentsBuilder.fromUriString("https://pro-api.coinmarketcap.com/v1/cryptocurrency/quotes/latest")
                .queryParam("symbol", symbol)
                .build()
                .toUri();

        // Build headers with API key
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CMC_PRO_API_KEY", apiKey);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // Build the request entity with URL, HttpMethod, and headers
        RequestEntity<?> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, uri);

        // Make the API call
        String response = restTemplate.exchange(requestEntity, String.class).getBody();

        // You can handle the response or log it as needed
        System.out.println("CoinMarketCap API Response: " + response);

        return response;
    }


}
