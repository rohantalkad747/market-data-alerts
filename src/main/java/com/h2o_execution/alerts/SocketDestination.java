package com.h2o_execution.alerts;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class SocketDestination extends AbstractDestination
{
    private final AlertEncoder encoder;

    public SocketDestination(String ipAddress, int port) throws UnknownHostException
    {
        InetAddress inetAddress = InetAddress.getByName(ipAddress);
        this.encoder = new AlertEncoder(inetAddress, port);
    }

    @Override
    protected void sendToDestination(String formattedIoI)
    {
        try
        {
            AlertBroadcaster alertBroadcaster = new AlertBroadcaster(encoder);
            alertBroadcaster.broadcast(formattedIoI);
        }
        catch (Exception e)
        {
            log.error("Failed to send alert to socket", e);
        }
    }
}
