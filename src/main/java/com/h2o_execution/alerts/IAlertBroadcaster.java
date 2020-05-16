package com.h2o_execution.alerts;

public interface IAlertBroadcaster
{
    void broadcast(String message) throws InterruptedException;

    void stop();
}
