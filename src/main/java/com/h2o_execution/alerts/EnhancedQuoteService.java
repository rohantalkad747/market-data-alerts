package com.h2o_execution.alerts;

import com.h2o_execution.domain.EnhancedQuote;

public interface EnhancedQuoteService
{
    EnhancedQuote getEnhancedQuote(String symbol);
}
