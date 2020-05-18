package com.h2o_execution.alerts;

import com.h2o_execution.domain.IoI;
import com.h2o_execution.persistence.IoIStore;
import com.h2o_execution.streams.MarketDataProxy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class IoILoadChore
{
    private static final String AFTER_CLOSE = "0 0 4 * * MON-FRI";
    private static final String ON_OPEN = "0 0/30 9 * * MON-FRI";
    private final IoIStore ioIStore;
    private final MarketDataProxy marketDataProxy;

    public IoILoadChore(final IoIStore ioIStore, final MarketDataProxy marketDataProxy)
    {
        this.ioIStore = ioIStore;
        this.marketDataProxy = marketDataProxy;
    }

    @Scheduled(cron = AFTER_CLOSE)
    public void setAfterClose() throws IOException
    {
        final List<IoI> matchingIoIs = ioIStore.getIoIsByTarget(Threshold.Target.CLOSE);
        injectPx(matchingIoIs);
    }

    @Scheduled(cron = ON_OPEN)
    public void setOnOpen() throws IOException
    {
        final List<IoI> matchingIoIs = ioIStore.getIoIsByTarget(Threshold.Target.OPEN);
        injectPx(matchingIoIs);
    }

    private void injectPx(final List<IoI> matchingIoIs) throws IOException
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
