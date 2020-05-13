package com.h2o_execution.alerts;

public interface IDestination
{
    void send(IndicationOfInterest ioi);
}
