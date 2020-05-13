package com.h2o_execution.streams;

import com.h2o_execution.domain.Quote;
import org.apache.flink.streaming.api.datastream.DataStream;

public interface IMarketDataStreamManager
{
    void addIndicationOfInterest();

    void removeIndicationOfInterest();

    DataStream<Quote> getMarketDataStream();
}
