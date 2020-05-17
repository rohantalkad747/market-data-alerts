package com.h2o_execution.streams;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.h2o_execution.misc.QuestradeAuthorizationManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Exchanger;

@Service
public class QuestradeSymbolServiceImpl implements QuestradeSymbolService
{
    private static final String SYM_SEARCH = "https://api01.iq.questrade.com/v1/symbols/";
    private final QuestradeAuthorizationManager questradeAuthorizationManager;

    public QuestradeSymbolServiceImpl(QuestradeAuthorizationManager questradeAuthorizationManager)
    {
        this.questradeAuthorizationManager = questradeAuthorizationManager;
    }

    @Data
    @AllArgsConstructor
    private static class QuestradeSymbol
    {
        private String symbol;
        private String symbolId;
        private String listingExchange;
    }

    @Override
    public String getId(Symbol symbol) throws IOException
    {
        String sym = symbol.getSymbol();
        Exchange exchange = symbol.getExchange();
        final HttpUriRequest request = RequestBuilder
                .get(SYM_SEARCH)
                .addHeader("Authorization", "Bearer " + questradeAuthorizationManager.getAccessToken())
                .addHeader("Accept", "application/json")
                .build();
        final HttpResponse response = HttpClientBuilder.create().build().execute(request);
        final String jsonString = EntityUtils.toString(response.getEntity());
        List<QuestradeSymbol> questradeSymbols = Arrays.asList(new ObjectMapper().readValue(jsonString, QuestradeSymbol[].class));
        return questradeSymbols.stream()
                .filter(s -> s.getSymbol().equals(sym) && s.getListingExchange().equals(exchange.toString()))
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .symbolId;
    }
}
