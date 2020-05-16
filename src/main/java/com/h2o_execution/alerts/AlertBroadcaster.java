package com.h2o_execution.alerts;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class AlertBroadcaster implements IAlertBroadcaster
{
    private final EventLoopGroup eventLoopGroup;
    private final Bootstrap bootstrap;

    public AlertBroadcaster(final AlertEncoder alertEncoder) throws Exception
    {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(alertEncoder);
    }

    @Override
    public void broadcast(final String message) throws InterruptedException
    {
        final Channel ch = bootstrap.connect().sync().channel();
        ch.writeAndFlush(message);
        ch.close();
    }

    @Override
    public void stop()
    {
        eventLoopGroup.shutdownGracefully();
    }
}
