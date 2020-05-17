package com.h2o_execution.streams;

public interface IoIAware
{
    void onDeactivation(Symbol symbol);

    void onActivation(Symbol symbol);
}
