package com.h2o_execution.streams;

import com.h2o_execution.alerts.Threshold;
import com.h2o_execution.domain.EnhancedQuote;

import java.io.IOException;

public interface MarketDataProxy
{
    double getPrice(String symbol, Threshold.Target target) throws IOException;

    EnhancedQuote getEnhancedQuote(String symbol);
}
