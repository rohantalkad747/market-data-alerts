package com.h2o_execution.streams;

import io.netty.channel.socket.SocketChannel;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.List;

public class MarketDataAccess implements SubscriptionAware
{
    private SocketChannel socketChannel;
    private static final String MD_URL = "https://api01.iq.questrade.com/v1/markets/quotes?ids=%s\"stream=true\"mode=RawSocket";

    private String getConnString(Collection<String> symbolIds)
    {
        return String.format(MD_URL, String.join(",", symbolIds));
    }

    @Override
    public void updateSubscriptionList(List<String> symbolIds)
    {
        String connStr = getConnString(symbolIds);
    }
}
