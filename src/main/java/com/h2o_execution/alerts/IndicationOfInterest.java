package com.h2o_execution.alerts;

import lombok.Data;

@Data
public abstract class IndicationOfInterest
{
    protected IDestination output;
    protected String symbol;
    protected Threshold threshold;
}
