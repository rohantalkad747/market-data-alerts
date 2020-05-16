package com.h2o_execution.alerts;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SocketDestination implements IDestination
{
    String ipAddress;
    int port;

    @Override
    public synchronized void send(IoI ioi)
    {

    }
}
