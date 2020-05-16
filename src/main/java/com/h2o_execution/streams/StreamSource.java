package com.h2o_execution.streams;

import com.h2o_execution.domain.Quote;
import lombok.AllArgsConstructor;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Random;

@AllArgsConstructor
public class StreamSource implements SourceFunction<Quote>
{
    private final String symbol;
    private final Integer sigma;
    private Double price;
    private boolean cancelled;

    @Override
    public void run(final SourceContext<Quote> sourceContext) throws InterruptedException
    {
        final Random r = new Random();
        while (!cancelled)
        {
            price += sigma * r.nextGaussian();
            final Quote stockPrice = new Quote(symbol, price);
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
