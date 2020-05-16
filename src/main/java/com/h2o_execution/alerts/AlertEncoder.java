package com.h2o_execution.alerts;

import com.h2o_execution.domain.EnhancedQuote;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.springframework.stereotype.Service;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.List;

@Service
public abstract class AlertEncoder extends MessageToMessageEncoder<EnhancedQuote>
{
    protected final InetAddress inetAddress;
    protected final int port;

    public AlertEncoder(InetAddress inetAddress, int port)
    {
        this.inetAddress = inetAddress;
        this.port = port;
    }

    public byte[] serialize(EnhancedQuote quote)
    {
        return quote.toString().getBytes();
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, EnhancedQuote securityAlertSnapshot, List<Object> list) throws Exception
    {
        byte[] mssg = serialize(securityAlertSnapshot);
        DatagramPacket datagramPacket = new DatagramPacket(mssg, mssg.length, inetAddress, port);
        list.add(datagramPacket);
    }
}
