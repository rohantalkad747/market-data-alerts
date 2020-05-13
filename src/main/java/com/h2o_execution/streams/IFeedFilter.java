package com.h2o_execution.streams;

import org.apache.flink.streaming.api.datastream.DataStreamSource;

/**
 * A feed filter is a link in the data stream. It may processes data from
 * its input stream and pipe the formatted data to the output stream.
 * @param <T>
 */
public interface IFeedFilter<I, T>
{
    void setInputStream(DataStreamSource<T> inputStream);

    void applyFilter();
}
