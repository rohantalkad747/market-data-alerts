package com.h2o_execution.streams;

import com.h2o_execution.domain.Quote;
import org.apache.flink.shaded.guava18.com.google.common.collect.Sets;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;

import static com.h2o_execution.streams.StreamingConstants.FLINK_PORT;
import static com.h2o_execution.streams.StreamingConstants.LOCAL_HOST;

public class MarketDataStreamManager implements IoIAware, IMarketDataStreamManager
{
    private static final String EVERY_ONE_HOUR_MARKET_HOURS = "30,0 9-16 * * MON-FRI";
    private final ConcurrentMap<String, Integer> symbolSubscriberTable;
    private final DataStream<Quote> quoteDataStream;
    private Set<String> activeSymbols;
    private ConcurrentSkipListSet<String> toDeactivateSymbols;

    public MarketDataStreamManager()
    {
        symbolSubscriberTable = new ConcurrentHashMap<>();
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        quoteDataStream = env
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
    @Scheduled(cron = EVERY_ONE_HOUR_MARKET_HOURS)
    public void hourlyChore()
    {
        activeSymbols = Sets.difference(activeSymbols, toDeactivateSymbols);
        toDeactivateSymbols.clear();
    }

    @Override
    public void setListener(final SinkFunction<Quote> dataSubscriber)
    {
        quoteDataStream.addSink(dataSubscriber);
    }

    @Override
    public void onDeactivation(final String symbol)
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
    public void onActivation(final String symbol)
    {
        final int cnt = symbolSubscriberTable.merge(symbol, 1, Integer::sum);
        if (cnt == 1)
        {
            activeSymbols.add(symbol);
        }
    }

    @Override
    public void setActiveSymbols(final Set<String> symbols)
    {
        activeSymbols = symbols;
    }
}
