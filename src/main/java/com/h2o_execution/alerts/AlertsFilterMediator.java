package com.h2o_execution.alerts;

import com.h2o_execution.domain.EnhancedQuote;
import com.h2o_execution.domain.Quote;
import com.h2o_execution.static_data.IEnhancedQuoteService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

@Slf4j
@AllArgsConstructor
public class AlertsFilterMediator implements Subscriber<Quote>
{
    private final IEnhancedQuoteService enhancedQuoteService;
    private final IoIStore ioIStore;

    @Override
    public void onNext(Quote q)
    {
        List<IoI> ioIsBySymbol = ioIStore.getIoIsBySymbol(q.getSymbol());
        for (IoI ioi : ioIsBySymbol)
        {
            Threshold th = ioi.getThreshold();
            Threshold.Direction drxn = th.getDirection();
            double threshAbsValue = th.getAbsValue();
            if (drxn.isSatisfied(threshAbsValue, q))
            {
                EnhancedQuote enhancedSnapshot = enhancedQuoteService.getEnhancedQuote(q);
                ioi.getDestination().send(enhancedSnapshot, th);
                if (th.getType() == Threshold.Type.ABSOLUTE)
                {
                    this.ioIStore.removeEntry(ioi.getSymbol(), ioi.getId());
                }
            }
        }
    }

    @Override
    public void onSubscribe(Subscription subscription)
    {
        subscription.request(100);
    }


    @Override
    public void onError(Throwable throwable)
    {
        log.error("Subscriber Error >> %s", throwable);
    }

    @Override
    public void onComplete()
    {
        log.info("Market data done for day");
    }
}
