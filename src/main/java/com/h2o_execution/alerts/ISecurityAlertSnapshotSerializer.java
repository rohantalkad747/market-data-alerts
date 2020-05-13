package com.h2o_execution.alerts;

import com.h2o_execution.domain.EnhancedQuote;

public interface ISecurityAlertSnapshotSerializer
{
    byte[] serialize(EnhancedQuote quote);
}
