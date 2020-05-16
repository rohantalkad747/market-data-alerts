package com.h2o_execution.streams;

public interface IoIAware
{
    void onDeactivation(String symbol);

    void onActivation(String symbol);
}
