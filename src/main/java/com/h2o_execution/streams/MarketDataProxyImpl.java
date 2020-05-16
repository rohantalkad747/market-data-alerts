package com.h2o_execution.streams;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.h2o_execution.alerts.Threshold;
import com.h2o_execution.domain.EQMapper;
import com.h2o_execution.domain.EnhancedQuote;
import com.h2o_execution.domain.TradierEnhancedPartialQuote;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Slf4j
@Service
public class MarketDataProxyImpl implements MarketDataProxy
{
    private final EQMapper eqMapper;
    @Value("${tradier_api_key}")
    private String TRADIER_API_KEY;

    public MarketDataProxyImpl(final EQMapper eqMapper)
    {
        this.eqMapper = eqMapper;
    }

    @Override
    public double getPrice(final String symbol, final Threshold.Target target) throws IOException
    {
        final TradierEnhancedPartialQuote tepq = getTepq(symbol);
        if (target == Threshold.Target.CLOSE)
        {
            return tepq.getClose();
        }
        return tepq.getOpen();
    }

    private TradierEnhancedPartialQuote getTepq(final String symbol) throws IOException
    {
        final HttpUriRequest request = RequestBuilder
                .get("https://api.tradier.com/v1/markets/quotes")
                .addHeader("Authorization", "Bearer " + TRADIER_API_KEY)
                .addHeader("Accept", "application/json")
                .addParameter("symbols", symbol)
                .addParameter("greeks", "true")
                .build();
        final HttpResponse response = HttpClientBuilder.create().build().execute(request);
        final String jsonString = EntityUtils.toString(response.getEntity());
        return new ObjectMapper().readValue(jsonString, TradierEnhancedPartialQuote.class);
    }

    @Override
    public EnhancedQuote getEnhancedQuote(final String symbol)
    {
        return null;
    }
}
