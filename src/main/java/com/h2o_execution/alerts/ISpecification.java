package com.h2o_execution.alerts;

import com.h2o_execution.Security;

public interface ISpecification<T>
{
    boolean isSatisfied(T context, Security security);
}
