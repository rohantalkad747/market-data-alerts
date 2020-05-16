package com.h2o_execution.alerts;

import com.h2o_execution.streams.MarketDataProxy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IoILoadChore
{
    // Account for 15 minute delay ...
    private static final String AFTER_CLOSE = "0 0/15 4 * * ?";
    private static final String ON_OPEN = "0 0/45 9 * * ?";
    private final IoIStore ioIStore;
    private final MarketDataProxy marketDataProxy;

    public IoILoadChore(IoIStore ioIStore, MarketDataProxy marketDataProxy)
    {
        this.ioIStore = ioIStore;
        this.marketDataProxy = marketDataProxy;
    }

    @Scheduled(cron = AFTER_CLOSE)
    public void setAfterClose()
    {
        List<IoI> matchingIoIs = ioIStore.getIoIsByTarget(Threshold.Target.CLOSE);
        injectPx(matchingIoIs);
    }

    @Scheduled(cron = ON_OPEN)
    public void setOnOpen()
    {
        List<IoI> matchingIoIs = ioIStore.getIoIsByTarget(Threshold.Target.OPEN);
        injectPx(matchingIoIs);
    }

    private void injectPx(List<IoI> matchingIoIs)
    {
        for (IoI ioi : matchingIoIs)
        {
            Threshold th = ioi.getThreshold();
            double px = marketDataProxy.getPrice(ioi.getSymbol(), th.getTarget());
            Threshold.Direction dir = th.getDirection();
            double target = dir.getTarget(px, th.getPctValue());
            ioi.getThreshold().setAbsValue(target);
        }
    }

}
