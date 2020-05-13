package com.h2o_execution.alerts;

import com.h2o_execution.domain.EnhancedQuote;

public interface TransportAlertBroadcaster
{
    void broadcast(String message, EnhancedQuote enhancedSecurity);

    void stop();
}
