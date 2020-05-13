package com.h2o_execution.alerts;

public interface IndicationOfInterest<T extends ISpecification>
{
    String getSymbol();

    void setSymbol(String symbol);

    ISpecification getSpecification();

    void setSpecification(ISpecification specification);
}
