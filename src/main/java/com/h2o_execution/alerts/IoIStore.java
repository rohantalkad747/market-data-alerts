package com.h2o_execution.alerts;

import java.util.List;

public interface IoIStore
{
    List<IoI> getIoIsByTarget(Threshold.Target target);

    List<IoI> getIoIsBySymbol(String symbol);

    boolean removeEntry(String symbol, long id);

    void bulkRemove(List<IoI> iois);

    void addEntry(IoI ioi);
}
