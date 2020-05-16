package com.h2o_execution.static_data;

import com.h2o_execution.domain.EnhancedQuote;
import com.h2o_execution.domain.Quote;

public interface IEnhancedQuoteService
{
    EnhancedQuote getEnhancedQuote(Quote security);
}
