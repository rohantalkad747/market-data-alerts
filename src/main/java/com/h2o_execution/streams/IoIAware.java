package com.h2o_execution.streams;

import java.util.List;
import java.util.Set;

public interface IoIAware
{
    void onDeactivation(String symbol);
    void onActivation(String symbol);
}
