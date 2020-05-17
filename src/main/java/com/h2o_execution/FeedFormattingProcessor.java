package com.h2o_execution;

import com.h2o_execution.domain.Quote;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;


public class FeedFormattingProcessor
{
    public static void main(final String[] args) throws Exception
    {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        final DataStream<Quote> socketStockStream = env
                .socketTextStream("localhost", 9999)
                .map(new MapFunction<String, Quote>()
                {
                    private String[] tokens;

                    @Override
                    public Quote map(final String value)
                    {
                        tokens = value.split(",");
                        return new Quote(tokens[0], Double.parseDouble(tokens[1]));
                    }
                });
//        DataStream<Security> SPX_stream = env.addSource(new StreamSource("SPX", 10));
//        DataStream<Security> FTSE_stream = env.addSource(new StreamSource("FTSE", 20));
//        DataStream<Security> DJI_stream = env.addSource(new StreamSource("DJI", 30));
//        DataStream<Security> BUX_stream = env.addSource(new StreamSource("BUX", 40));
//        DataStream<Security> stockStream = socketStockStream.union(SPX_stream, FTSE_stream, DJI_stream, BUX_stream);
//
//
//        stockStream.print();
//        env.execute();

    }
}
