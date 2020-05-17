package com.h2o_execution.persistence;

import com.h2o_execution.alerts.Threshold;
import com.h2o_execution.domain.IoI;

import java.util.List;

public interface IoIStore
{
    List<IoI> getIoIsByTarget(Threshold.Target target);

    List<IoI> getIoIsBySymbol(String symbol);

    boolean removeEntry(String symbol, long id);

    void addEntry(IoI ioi);
}
