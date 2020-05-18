package com.h2o_execution.streams;

import java.util.List;

public interface QTStreamSink
{
    void initiateSubscriptionConnection(List<String> symbolIds);
}
