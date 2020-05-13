package com.h2o_execution.alerts;

import com.h2o_execution.Security;

import java.util.function.BiFunction;

public interface Threshold
{
    void setEvaluator(BiFunction<Double, Security, Boolean> evaluator);

    boolean isSatisfied(Double contextPx, Security security);
}
