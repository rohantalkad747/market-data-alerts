package com.h2o_execution.streams;

import com.h2o_execution.misc.AbstractQTAccessAware;
import com.h2o_execution.misc.QTAccessKeyRegistry;
import lombok.Getter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MarketDataSourceStreamAdapterImpl extends AbstractQTAccessAware implements MarketDataSourceStreamAdapter
{
    @Getter
    private final MarketDataSourceStream delegate;
    private final QuestradeSymbolService questradeSymbolService;
    private final Map<Symbol, String> symbolToId;

    protected MarketDataSourceStreamAdapterImpl(final QTAccessKeyRegistry registry)
    {
        super(registry);
        this.questradeSymbolService = new QuestradeSymbolServiceImpl(registry);
        this.delegate = new MarketDataSourceStream(registry);
        this.symbolToId = new ConcurrentHashMap<>();
    }

    @Override
    public void updateAccessKey(final String accessKey)
    {
        delegate.updateAccessKey(accessKey);
    }

    private void populateSymTable(final Set<Symbol> activeSymbols)
    {
        for (final Symbol sym : activeSymbols)
        {
            symbolToId.computeIfAbsent(sym, k ->
            {
                try
                {
                    return questradeSymbolService.getId(sym);
                }
                catch (final IOException e)
                {
                    return null;
                }
            });
        }
    }

    @Override
    public void initiateSubscriptionConnection(final Set<Symbol> activeSymbols)
    {
        populateSymTable(activeSymbols);
        final List<String> subscriptionList = activeSymbols
                .stream()
                .map(s -> symbolToId.get(s))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        delegate.initiateSubscriptionConnection(subscriptionList);
    }
}
