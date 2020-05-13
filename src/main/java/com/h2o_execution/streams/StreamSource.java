package com.h2o_execution.streams;

import com.h2o_execution.Security;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Random;

public class StreamSource implements SourceFunction<Security>
{
    private Double price;
    private String symbol;
    private Integer sigma;
    private boolean cancelled;

    public StreamSource(String symbol, Integer sigma) {
        this.symbol = symbol;
        this.sigma = sigma;
    }

    @Override
    public void run(SourceContext<Security> sourceContext) throws InterruptedException
    {
        Random r = new Random();
        while ( !cancelled )
        {
            price += sigma * r.nextGaussian();
            Security stockPrice = new Security(symbol, price);
            sourceContext.collect(stockPrice);
            Thread.sleep(r.nextInt(200));
        }
    }

    @Override
    public void cancel()
    {
        this.cancelled = true;
    }
}
