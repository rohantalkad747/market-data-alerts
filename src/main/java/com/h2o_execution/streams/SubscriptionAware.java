package com.h2o_execution.streams;

import java.util.List;

public interface SubscriptionAware
{
    void updateSubscriptionList(List<String> symbolIds);
}
