package com.h2o_execution.alerts;

import com.h2o_execution.domain.Security;

public interface Threshold
{
    void setDirection(ThresholdSign qualifier);

    void setQualifier(ThresholdType qualifier);

    boolean isSatisfied(Double contextPx, Security security);
}
