package com.h2o_execution.alerts;

import lombok.Data;

@Data
class VWAPConfig
{
    private double[] histPrice;
    private double histVolume[];
}

public enum OutputConfiguration
{
    VWAP {
        @Override
        public double getPrice(Object cfg)
        {
            VWAPConfig vwapConfig = (VWAPConfig) cfg;
            return 0;
        }
    };

    public abstract double getPrice(Object metadata);

}
