package com.h2o_execution.streams;

import org.apache.flink.streaming.api.datastream.DataStream;

/**
 * A feed filter is a link in the data stream. It may processes data from
 * its input stream and pipe the formatted data to the output stream.
 * @param <T>
 */
public interface IFeedFilter<T>
{
    void setInputStream(DataStream<T> is);

    void setOutputStream(DataStream<T> os);

    void filter();

    void stop();
}
