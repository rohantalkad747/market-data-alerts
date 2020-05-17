package com.h2o_execution.streams;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.h2o_execution.misc.AbstractQTAccessAware;
import com.h2o_execution.misc.QTAccessKeyRegistry;
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

@Service
public class QuestradeSymbolServiceImpl extends AbstractQTAccessAware implements QuestradeSymbolService
{
    private static final String SYM_SEARCH = "https://api01.iq.questrade.com/v1/symbols/";
    private String accessKey;

    protected QuestradeSymbolServiceImpl(QTAccessKeyRegistry registry)
    {
        super(registry);
    }

    @Override
    public String getId(final Symbol symbol) throws IOException
    {
        final String sym = symbol.getSymbol();
        final Exchange exchange = symbol.getExchange();
        final HttpUriRequest request = RequestBuilder
                .get(SYM_SEARCH)
                .addHeader("Authorization", "Bearer " + accessKey)
                .addHeader("Accept", "application/json")
                .build();
        final HttpResponse response = HttpClientBuilder.create().build().execute(request);
        final String jsonString = EntityUtils.toString(response.getEntity());
        final List<QuestradeSymbol> questradeSymbols = Arrays.asList(new ObjectMapper().readValue(jsonString, QuestradeSymbol[].class));
        return questradeSymbols.stream()
                .filter(s -> s.getSymbol().equals(sym) && s.getListingExchange().equals(exchange.toString()))
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .symbolId;
    }

    @Override
    public void updateAccessKey(String accessKey)
    {
        this.accessKey = accessKey;
    }

    @Data
    @AllArgsConstructor
    private static class QuestradeSymbol
    {
        private String symbol;
        private String symbolId;
        private String listingExchange;
    }
}
