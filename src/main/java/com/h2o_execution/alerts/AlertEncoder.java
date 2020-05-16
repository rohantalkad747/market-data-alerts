package com.h2o_execution.alerts;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.springframework.stereotype.Service;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class AlertEncoder extends MessageToMessageEncoder<String>
{
    protected final InetAddress inetAddress;
    protected final int port;

    public AlertEncoder(final InetAddress inetAddress, final int port)
    {
        this.inetAddress = inetAddress;
        this.port = port;
    }

    @Override
    protected void encode(final ChannelHandlerContext channelHandlerContext, final String str, final List<Object> list) throws Exception
    {
        final byte[] mssg = str.getBytes(StandardCharsets.UTF_8);
        final DatagramPacket datagramPacket = new DatagramPacket(mssg, mssg.length, inetAddress, port);
        list.add(datagramPacket);
    }
}
