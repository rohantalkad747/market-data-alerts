package com.h2o_execution.streams;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.h2o_execution.domain.Quote;
import com.h2o_execution.misc.QTAccessKeyRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.shaded.guava18.com.google.common.collect.Sets;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

@Slf4j
public class MarketDataStreamMediator implements IoIAware, SymbolAware
{
    private static final String HOURLY = "30,0 9-16 * * MON-FRI";
    private final ConcurrentMap<Symbol, Integer> symbolSubscriberTable;
    private final DataStream<Quote> quoteDataStream;
    private final QuestradeSymbolService questradeSymbolService;
    private final MarketDataSourceStream marketDataSourceStream;
    private Set<Symbol> activeSymbols;
    private final Set<Symbol> deactivateSymbols;
    private final Map<Symbol, String> symbolToId;
    private final ObjectMapper objectMapper;

    public MarketDataStreamMediator(QTAccessKeyRegistry qtAccessKeyRegistry)
    {
        this.questradeSymbolService = new QuestradeSymbolServiceImpl(qtAccessKeyRegistry);
        this.marketDataSourceStream = new MarketDataSourceStream(qtAccessKeyRegistry);
        this.symbolSubscriberTable = new ConcurrentHashMap<>();
        this.deactivateSymbols = new CopyOnWriteArraySet<>();
        this.symbolToId = new HashMap<>();
        this.objectMapper = new ObjectMapper();
        quoteDataStream = StreamExecutionEnvironment.getExecutionEnvironment()
                .addSource(marketDataSourceStream)
                .map(value -> objectMapper.readValue(value, Quote.class));
    }

    /**
     * Changes the list of subscriptions.
     */
    @Scheduled(cron = HOURLY)
    public void hourlyChore()
    {
        if (!deactivateSymbols.isEmpty())
        {
            updateSymbolSets();
            populateSymTable();
            changeSubscriptionList();
        }
    }

    private void updateSymbolSets()
    {
        activeSymbols = Sets.difference(activeSymbols, deactivateSymbols);
        deactivateSymbols.clear();
    }

    private void populateSymTable()
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

    private void changeSubscriptionList()
    {
        final List<String> subscriptionList = activeSymbols
                .stream()
                .map(s -> symbolToId.get(s))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        marketDataSourceStream.updateSubscriptionList(subscriptionList);
    }

    @Override
    public void setListener(final SinkFunction<Quote> dataSubscriber)
    {
        quoteDataStream.addSink(dataSubscriber);
    }

    @Override
    public void onDeactivation(final Symbol symbol)
    {
        if (symbolSubscriberTable.get(symbol) == null)
        {
            throw new RuntimeException("Symbol not part of subscriber table!");
        }
        final int newCount = symbolSubscriberTable.compute(symbol, (k, v) -> v == 0 ? 0 : v - 1);
        if (newCount == 0)
        {
            deactivateSymbols.add(symbol);
        }
    }

    @Override
    public void onActivation(final Symbol symbol)
    {
        final int cnt = symbolSubscriberTable.merge(symbol, 1, Integer::sum);
        if (cnt == 1)
        {
            activeSymbols.add(symbol);
        }
    }

    @Override
    public void setActiveSymbols(final Set<Symbol> symbols)
    {
        activeSymbols = symbols;
        this.changeSubscriptionList();
    }
}
