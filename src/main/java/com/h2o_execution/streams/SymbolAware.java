package com.h2o_execution.streams;

import com.h2o_execution.domain.Quote;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.util.Set;

public interface SymbolAware
{
    void setActiveSymbols(Set<Symbol> symbols);

    void setListener(SinkFunction<Quote> dataSubscriber);
}
