package com.h2o_execution.streams;

import java.util.List;

public interface QTStreamSink
{
    void updateSubscriptionList(List<String> symbolIds);
}
