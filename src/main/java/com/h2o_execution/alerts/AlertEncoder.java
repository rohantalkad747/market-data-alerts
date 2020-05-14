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
    protected final IEnhancedQuoteSerializer securityAlertSnapshotSerializer;
    protected final InetAddress inetAddress;
    protected final int port;

    public AlertEncoder(IEnhancedQuoteSerializer securityAlertSnapshotSerializer, InetAddress inetAddress, int port)
    {
        this.securityAlertSnapshotSerializer = securityAlertSnapshotSerializer;
        this.inetAddress = inetAddress;
        this.port = port;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, EnhancedQuote securityAlertSnapshot, List<Object> list) throws Exception
    {
        byte[] mssg = securityAlertSnapshotSerializer.serialize(securityAlertSnapshot);
        Object data = getData(mssg);
        list.add(data);
    }

    protected abstract Object getData(byte[] mssg);

    public static class UDPAlertEncoder extends AlertEncoder
    {

        public UDPAlertEncoder(IEnhancedQuoteSerializer securityAlertSnapshotSerializer, InetAddress inetAddress, int port)
        {
            super(securityAlertSnapshotSerializer, inetAddress, port);
        }

        @Override
        protected Object getData(byte[] mssg)
        {
            return new DatagramPacket(mssg, mssg.length, inetAddress, port);
        }
    }

    public static class TCPAlertEncoder extends AlertEncoder
    {

        public TCPAlertEncoder(IEnhancedQuoteSerializer securityAlertSnapshotSerializer, InetAddress inetAddress, int port)
        {
            super(securityAlertSnapshotSerializer, inetAddress, port);
        }

        @Override
        protected Object getData(byte[] mssg)
        {
            return mssg;
        }
    }
}
