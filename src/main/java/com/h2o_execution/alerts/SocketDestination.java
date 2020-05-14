package com.h2o_execution.alerts;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SocketDestination implements IDestination
{
    TransportType transportType;
    String ipAddress;
    int port;
    boolean connected;

    @Override
    public synchronized void send(IndicationOfInterest ioi)
    {

        if (transportType == TransportType.UDP)
        {

        }
        else
        {

        }
    }

    public enum TransportType
    {
        TCP,
        UDP
    }
}
