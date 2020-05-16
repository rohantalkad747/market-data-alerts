package com.h2o_execution.alerts;

import java.net.InetAddress;

public interface IAlertBroadcaster
{
    void broadcast(String message) throws InterruptedException;

    void stop();
}
