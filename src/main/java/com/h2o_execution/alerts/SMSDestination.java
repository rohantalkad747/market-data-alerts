package com.h2o_execution.alerts;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SMSDestination implements IDestination
{
    private long phNumber;

    @Override
    public void send(IoI ioi)
    {

    }
}
