package com.h2o_execution.alerts;

import com.h2o_execution.Security;

import java.util.List;

public interface IAlertsHistoryManager
{
    void addRecord(ISpecification<Security> specification);

    List<ISpecification<Security>> getRecords();
}
