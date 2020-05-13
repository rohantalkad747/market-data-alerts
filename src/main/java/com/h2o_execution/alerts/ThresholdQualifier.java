package com.h2o_execution.alerts;

import com.h2o_execution.Security;

import java.util.function.BiFunction;

public enum ThresholdQualifier
{
    POSITIVE
            {
                @Override
                public BiFunction<Double, Security, Boolean> getPxThreshold()
                {
                    return (px, sec) -> sec.price > px;
                }
            },
    NEGATIVE
            {
                @Override
                public BiFunction<Double, Security, Boolean> getPxThreshold()
                {
                    return (px, sec) -> sec.price < px;
                }
            },
    EQUAL
            {
                @Override
                public BiFunction<Double, Security, Boolean> getPxThreshold()
                {
                    return (px, sec) -> sec.price == px;
                }
            };

    public abstract BiFunction<Double, Security, Boolean> getPxThreshold();
}
