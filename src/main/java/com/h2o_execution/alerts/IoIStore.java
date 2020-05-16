package com.h2o_execution.alerts;

import java.util.List;

public interface IoIStore
{
    List<IoI> getMatchingIoIs(Threshold.Target target);

    boolean removeEntry(String symbol, long id);

    void addEntry(IoI ioi);
}
