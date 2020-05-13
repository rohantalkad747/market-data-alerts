package com.h2o_execution.alerts;

import com.h2o_execution.domain.EnhancedSecurity;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.springframework.stereotype.Service;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.List;

@Service
public class AlertEncoder extends MessageToMessageEncoder<EnhancedSecurity>
{
    private final InetAddress inetAddress;
    private final int port;
    private final ISecurityAlertSnapshotSerializer securityAlertSnapshotSerializer;

    public AlertEncoder(InetAddress inetAddress, int port, ISecurityAlertSnapshotSerializer securityAlertSnapshotSerializer)
    {
        this.inetAddress = inetAddress;
        this.port = port;
        this.securityAlertSnapshotSerializer = securityAlertSnapshotSerializer;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, EnhancedSecurity securityAlertSnapshot, List<Object> list) throws Exception
    {
        byte[] mssg = securityAlertSnapshotSerializer.serialize(securityAlertSnapshot);
        DatagramPacket datagramPacket = new DatagramPacket(mssg, mssg.length, inetAddress, port);
        list.add(datagramPacket);
    }
}
