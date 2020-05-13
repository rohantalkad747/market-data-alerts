package com.h2o_execution.alerts;

import com.h2o_execution.domain.Quote;

import java.util.function.BiFunction;

public enum ThresholdSign
{
    POSITIVE
            {
                @Override
                public BiFunction<Double, Quote, Boolean> getPxThreshold()
                {
                    return (px, sec) -> sec.getPrice() > px;
                }
            },
    NEGATIVE
            {
                @Override
                public BiFunction<Double, Quote, Boolean> getPxThreshold()
                {
                    return (px, sec) -> sec.getPrice() < px;
                }
            },
    EQUAL
            {
                @Override
                public BiFunction<Double, Quote, Boolean> getPxThreshold()
                {
                    return (px, sec) -> sec.getPrice() == px;
                }

            };

    public abstract BiFunction<Double, Quote, Boolean> getPxThreshold();
}
