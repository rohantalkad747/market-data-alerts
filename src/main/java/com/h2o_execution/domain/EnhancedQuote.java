package com.h2o_execution.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EnhancedQuote extends Quote
{
    private double bid;
    private double ask;
    private double high;
    private double low;
    private int volume;
    private double highPrice52;
    private double lowPrice52;
    private double pe;
    private double dividend;
    private double yield;
    private long marketCap;
}
