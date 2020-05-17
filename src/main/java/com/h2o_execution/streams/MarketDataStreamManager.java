package com.h2o_execution.streams;

import com.h2o_execution.domain.Quote;
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

import static com.h2o_execution.streams.StreamingConstants.FLINK_PORT;
import static com.h2o_execution.streams.StreamingConstants.LOCAL_HOST;

@Slf4j
@Service
public class MarketDataStreamManager implements IoIAware, IMarketDataStreamManager
{
    private static final String HOURLY = "30,0 9-16 * * MON-FRI";
    private final ConcurrentMap<Symbol, Integer> symbolSubscriberTable;
    private final DataStream<Quote> quoteDataStream;
    private final QuestradeSymbolService questradeSymbolService;
    private Set<Symbol> activeSymbols;
    private Set<Symbol> deactivateSymbols;
    private Map<Symbol, String> symbolToId;
    private MarketDataAccess marketDataAccess;

    public MarketDataStreamManager(final QuestradeSymbolService questradeSymbolService)
    {
        this.questradeSymbolService = questradeSymbolService;
        this.symbolSubscriberTable = new ConcurrentHashMap<>();
        this.deactivateSymbols = new CopyOnWriteArraySet();
        this.symbolToId = new HashMap<>();
        quoteDataStream = StreamExecutionEnvironment.getExecutionEnvironment()
                .socketTextStream(LOCAL_HOST, FLINK_PORT)
                .map(value ->
                {
                    final String[] tokens = value.split(",");
                    return new Quote(tokens[0], Double.parseDouble(tokens[1]));
                });
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
                catch (IOException e)
                {
                    return null;
                }
            });
        }
    }

    private void changeSubscriptionList()
    {
        List<String> subscriptionList = activeSymbols
                .stream()
                .map(s -> symbolToId.get(s))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        marketDataAccess.updateSubscriptionList(subscriptionList);
    }

    @Override
    public void setListener(final SinkFunction<Quote> dataSubscriber)
    {
        quoteDataStream.addSink(dataSubscriber);
    }

    @Override
    public void onDeactivation(final Symbol symbol)
    {
        if (!symbolSubscriberTable.containsKey(symbol))
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
    }
}
