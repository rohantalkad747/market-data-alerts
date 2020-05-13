package com.h2o_execution.static_data;

import com.h2o_execution.domain.Quote;
import com.h2o_execution.domain.EnhancedQuote;

public interface IEnhancedSecurityService
{
    EnhancedQuote getEnhancedSnapshot(Quote security);
}
