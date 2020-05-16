package com.h2o_execution.alerts;

import com.h2o_execution.streams.MarketDataProxy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IoILoadChore
{
    // Account for 15 minute delay ...
    private static final String AFTER_CLOSE = "0 0/15 4 * * MON-FRI";
    private static final String ON_OPEN = "0 0/45 9 * * MON-FRI";
    private final IoIStore ioIStore;
    private final MarketDataProxy marketDataProxy;

    public IoILoadChore(final IoIStore ioIStore, final MarketDataProxy marketDataProxy)
    {
        this.ioIStore = ioIStore;
        this.marketDataProxy = marketDataProxy;
    }

    @Scheduled(cron = AFTER_CLOSE)
    public void setAfterClose()
    {
        final List<IoI> matchingIoIs = ioIStore.getIoIsByTarget(Threshold.Target.CLOSE);
        injectPx(matchingIoIs);
    }

    @Scheduled(cron = ON_OPEN)
    public void setOnOpen()
    {
        final List<IoI> matchingIoIs = ioIStore.getIoIsByTarget(Threshold.Target.OPEN);
        injectPx(matchingIoIs);
    }

    private void injectPx(final List<IoI> matchingIoIs)
    {
        for (final IoI ioi : matchingIoIs)
        {
            final Threshold th = ioi.getThreshold();
            final double px = marketDataProxy.getPrice(ioi.getSymbol(), th.getTarget());
            final Threshold.Direction dir = th.getDirection();
            final double target = dir.getTarget(px, th.getPctValue());
            ioi.getThreshold().setAbsValue(target);
        }
    }

}
