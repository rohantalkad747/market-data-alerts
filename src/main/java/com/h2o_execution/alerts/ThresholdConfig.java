package com.h2o_execution.alerts;

import lombok.Data;

@Data
public class ThresholdConfig
{
    private ThresholdSign direction;
    private ThresholdType qualifier;
    private ThresholdTarget target;
}
