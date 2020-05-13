package com.h2o_execution.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EnhancedSecurity extends Security
{
    private double bid;
    private double ask;
    private double high;
    private double low;
    private int volume;
    private double fiftyTwoWeekHigh;
    private double fiftyTwoWeekLow;
    private double yield;
    private double peRatio;
}
