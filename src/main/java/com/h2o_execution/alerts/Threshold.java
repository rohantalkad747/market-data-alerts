package com.h2o_execution.alerts;

import com.h2o_execution.domain.Quote;

public interface Threshold
{
    void setDirection(ThresholdSign qualifier);

    void setQualifier(ThresholdType qualifier);

    boolean isSatisfied(Double contextPx, Quote security);
}
