package com.h2o_execution.alerts;

import lombok.Data;

@Data
public abstract class IndicationOfInterest<V>
{
    protected V outputConfiguration;
    protected String symbol;
    protected Threshold threshold;
}
