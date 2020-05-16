package com.h2o_execution.domain;

import lombok.Data;

@Data
public class TradierEnhancedPartialQuote
{
    private double last;
    private double bid;
    private double ask;
    private double high;
    private double low;
    private double open;
    private double close;
    private long volume;
}
