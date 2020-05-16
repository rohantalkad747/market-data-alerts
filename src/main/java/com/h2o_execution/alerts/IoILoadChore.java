package com.h2o_execution.alerts;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IoILoadChore
{
    private static final String AFTER_CLOSE ="0 0 4 * * ?";
    private static final String ON_OPEN = "0 0/30 4 * * ?";
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
        List<IoI> matchingIoIs = ioIStore.getMatchingIoIs(Threshold.Target.CLOSE);
        injectPx(matchingIoIs);
    }

    @Scheduled(cron = ON_OPEN)
    public void setOnOpen()
    {
        List<IoI> matchingIoIs = ioIStore.getMatchingIoIs(Threshold.Target.OPEN);
        injectPx(matchingIoIs);
    }

    private void injectPx(List<IoI> matchingIoIs)
    {
        for (IoI ioi : matchingIoIs)
        {
            Threshold th = ioi.getThreshold();
            double px = marketDataProxy.getMidPrice(ioi.getSymbol());
            Threshold.Direction dir  = th.getDirection();
            double target = dir.getTarget(px, th.getPctValue());
            ioi.getThreshold().setAbsValue(target);
        }
    }

}
