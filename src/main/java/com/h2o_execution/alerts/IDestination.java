package com.h2o_execution.alerts;

import com.h2o_execution.domain.EnhancedQuote;

import java.io.Serializable;

public interface IDestination extends Serializable
{
    void send(EnhancedQuote quote, Threshold th);
}
