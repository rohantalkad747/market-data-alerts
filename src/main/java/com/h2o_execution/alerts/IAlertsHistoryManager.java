package com.h2o_execution.alerts;

import java.util.List;

public interface IAlertsHistoryManager
{
    void addRecord(Threshold PxThreshold);

    List<Threshold> getRecords();
}
