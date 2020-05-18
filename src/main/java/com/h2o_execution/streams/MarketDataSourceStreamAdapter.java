package com.h2o_execution.streams;

import java.util.Set;

public interface MarketDataSourceStreamAdapter
{
    void initiateSubscriptionConnection(Set<Symbol> activeSymbols);

    MarketDataSourceStream getDelegate();
}
