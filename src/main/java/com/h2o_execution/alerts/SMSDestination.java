package com.h2o_execution.alerts;

import lombok.Data;

@Data
public class SMSDestination implements IDestination
{
    private int phNumber;

    @Override
    public void send(IndicationOfInterest ioi)
    {

    }
}
