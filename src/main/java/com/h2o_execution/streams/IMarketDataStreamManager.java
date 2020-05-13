package com.h2o_execution.streams;

import com.h2o_execution.domain.Security;
import org.apache.flink.streaming.api.datastream.DataStream;

public interface IMarketDataStreamManager
{
    void addIndicationOfInterest();

    void removeIndicationOfInterest();

    DataStream<Security> getMarketDataStream();
}
