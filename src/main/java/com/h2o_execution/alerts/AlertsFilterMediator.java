package com.h2o_execution.alerts;

import com.h2o_execution.domain.EnhancedQuote;
import com.h2o_execution.domain.IoI;
import com.h2o_execution.domain.Quote;
import com.h2o_execution.persistence.IoIStore;
import com.h2o_execution.streams.MarketDataProxy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class AlertsFilterMediator implements SinkFunction<Quote>
{
    private final MarketDataProxy marketDataProxy;
    private final IoIStore ioIStore;

    @Override
    public void invoke(final Quote q, final Context context)
    {
        final List<IoI> ioIsBySymbol = ioIStore.getIoIsBySymbol(q.getSymbol());
        for (final IoI ioi : ioIsBySymbol)
        {
            final Threshold th = ioi.getThreshold();
            final Threshold.Direction drxn = th.getDirection();
            final double threshAbsValue = th.getAbsValue();
            if (drxn.isSatisfied(threshAbsValue, q))
            {
                final EnhancedQuote enhancedQuote = marketDataProxy.getEnhancedQuote(q.getSymbol());
                ioi.getDestination().send(enhancedQuote, th);
                if (th.getType() == Threshold.Type.ABSOLUTE)
                {
                    this.ioIStore.removeEntry(ioi.getSymbol(), ioi.getId());
                }
                else 
                {
                    ioi.deactivate();
                }
            }
        }
    }
}
