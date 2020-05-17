package com.h2o_execution.streams;

import com.h2o_execution.domain.Quote;
import org.apache.flink.shaded.guava18.com.google.common.collect.Sets;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;

import static com.h2o_execution.streams.StreamingConstants.FLINK_PORT;
import static com.h2o_execution.streams.StreamingConstants.LOCAL_HOST;

public class MarketDataStreamManager implements IoIAware, IMarketDataStreamManager
{
    private static final String HOURLY = "30,0 9-16 * * MON-FRI";
    private final ConcurrentMap<Symbol, Integer> symbolSubscriberTable;
    private final DataStream<Quote> quoteDataStream;
    private final QuestradeSymbolService questradeSymbolService;
    private Set<Symbol> activeSymbols;
    private Set<Symbol> toDeactivateSymbols;
    private Map<Symbol, String> symbolToId;

    public MarketDataStreamManager(final QuestradeSymbolService questradeSymbolService)
    {
        this.questradeSymbolService = questradeSymbolService;
        this.symbolSubscriberTable = new ConcurrentHashMap<Symbol, Integer>();
        this.toDeactivateSymbols = new ConcurrentSkipListSet<>();
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
        if (!toDeactivateSymbols.isEmpty())
        {
            activeSymbols = Sets.difference(activeSymbols, toDeactivateSymbols);
            toDeactivateSymbols.clear();
            for (Symbol sym : activeSymbols)
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
            toDeactivateSymbols.add(symbol);
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
