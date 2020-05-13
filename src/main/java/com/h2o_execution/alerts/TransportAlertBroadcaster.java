package com.h2o_execution.alerts;

import com.h2o_execution.domain.EnhancedSecurity;

public interface TransportAlertBroadcaster
{
    void broadcast(String message, EnhancedSecurity enhancedSecurity);

    void stop();
}
