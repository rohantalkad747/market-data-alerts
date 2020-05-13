package com.h2o_execution.streams;

import com.h2o_execution.domain.Quote;
import lombok.AllArgsConstructor;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Random;

@AllArgsConstructor
public class StreamSource implements SourceFunction<Quote>
{
    private Double price;
    private final String symbol;
    private final Integer sigma;
    private boolean cancelled;

    @Override
    public void run(SourceContext<Quote> sourceContext) throws InterruptedException
    {
        Random r = new Random();
        while ( !cancelled )
        {
            price += sigma * r.nextGaussian();
            Quote stockPrice = new Quote(symbol, price);
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
