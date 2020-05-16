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

    public AlertEncoder(InetAddress inetAddress, int port)
    {
        this.inetAddress = inetAddress;
        this.port = port;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String str, List<Object> list) throws Exception
    {
        byte[] mssg = str.getBytes(StandardCharsets.UTF_8);
        DatagramPacket datagramPacket = new DatagramPacket(mssg, mssg.length, inetAddress, port);
        list.add(datagramPacket);
    }
}
