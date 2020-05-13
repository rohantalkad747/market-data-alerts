package com.h2o_execution.alerts;

import com.h2o_execution.alerts.IDestination;
import com.h2o_execution.alerts.IndicationOfInterest;
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
