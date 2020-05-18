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

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
public class MarketDataStreamMediator implements IoIAware, SymbolAware
{
    private static final String HOURLY = "30,0 9-16 * * MON-FRI";
    private final Map<Symbol, Integer> symbolSubscriberTable;
    private final DataStream<Quote> quoteDataStream;
    private final MarketDataSourceStreamAdapter marketDataSourceStream;
    private final Set<Symbol> deactivateSymbols;
    private final ObjectMapper objectMapper;
    private Set<Symbol> activeSymbols;

    public MarketDataStreamMediator(final QTAccessKeyRegistry qtAccessKeyRegistry)
    {
        this.marketDataSourceStream = new MarketDataSourceStreamAdapterImpl(qtAccessKeyRegistry);
        this.symbolSubscriberTable = new ConcurrentHashMap<>();
        this.deactivateSymbols = new CopyOnWriteArraySet<>();
        this.objectMapper = new ObjectMapper();
        quoteDataStream = StreamExecutionEnvironment.getExecutionEnvironment()
                .addSource(marketDataSourceStream.getDelegate())
                .map(value -> objectMapper.readValue(value, Quote.class));
    }

    @Scheduled(cron = HOURLY)
    public void updateStreamSubscriptionListEveryHour()
    {
        if (!deactivateSymbols.isEmpty())
        {
            activeSymbols = Sets.difference(activeSymbols, deactivateSymbols);
            deactivateSymbols.clear();
            marketDataSourceStream.initiateSubscriptionConnection(activeSymbols);
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
        final int newCount = symbolSubscriberTable.compute(symbol, (k, v) -> v == null ? 0 : (v == 0 ? 0 : v - 1));
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
        this.marketDataSourceStream.initiateSubscriptionConnection(activeSymbols);
    }
}
