package com.h2o_execution.alerts;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class EmailDestination implements IDestination
{
    String email;

    @Override
    public void send(IndicationOfInterest ioi)
    {

    }
}
