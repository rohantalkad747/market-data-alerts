package com.h2o_execution.alerts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class SMSDestination extends AbstractDestination
{
    private long phNumber;

    @Override
    protected void sendToDestination(String formattedIoI)
    {

    }
}
