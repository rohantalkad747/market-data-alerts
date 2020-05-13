package com.h2o_execution.streams;

import com.h2o_execution.Security;
import lombok.AllArgsConstructor;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Random;

@AllArgsConstructor
public class StreamSource implements SourceFunction<Security>
{
    private Double price;
    private final String symbol;
    private final Integer sigma;
    private boolean cancelled;

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
