package com.h2o_execution.streams;

import lombok.Data;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

@Data
public abstract class AbstractFeedFilter<T> implements IFeedFilter<T>
{
    private StreamExecutionEnvironment streamExecutionEnvironment;
    private boolean cancelled = false;
    protected DataStream<T> inputStream;
    protected DataStream<T> outputStream;

    public AbstractFeedFilter()
    {
        streamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
    }

    @Override
    public final void filter()
    {
        while (!cancelled)
        {
            triggerFilterOperation();
        }
    }

    protected abstract void triggerFilterOperation();

    @Override
    public void stop()
    {
        cancelled = true;
    }
}
