package com.h2o_execution.alerts;

import com.h2o_execution.domain.EnhancedSecurity;

public interface ISecurityAlertSnapshotSerializer
{
    byte[] serialize(EnhancedSecurity quote);
}
