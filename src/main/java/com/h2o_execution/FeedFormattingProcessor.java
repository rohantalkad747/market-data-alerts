//package com.h2o_execution;
//
//import org.apache.flink.api.common.functions.MapFunction;
//import org.apache.flink.streaming.api.datastream.DataStream;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//
//public class FeedFormattingProcessor
//{
//    public static void main(String[] args) throws Exception
//    {
//        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        DataStream<Security> socketStockStream = env
//                .socketTextStream("localhost", 9999)
//                .map(new MapFunction<String, Security>() {
//                    private String[] tokens;
//                    @Override
//                    public Security map(String value)
//                    {
//                        tokens = value.split(",");
//                        return new Security(tokens[0], Double.parseDouble(tokens[1]));
//                    }
//                });
//        DataStream<Security> SPX_stream = env.addSource(new Security("SPX", 10));
//        DataStream<Security> FTSE_stream = env.addSource(new Security("FTSE", 20));
//        DataStream<Security> DJI_stream = env.addSource(new Security("DJI", 30));
//        DataStream<Security> BUX_stream = env.addSource(new Security("BUX", 40));
//        DataStream<Security> stockStream = socketStockStream.union(SPX_stream, FTSE_stream, DJI_stream, BUX_stream);
//
//        stockStream.print();
//        env.execute();
//
//    }
//}
