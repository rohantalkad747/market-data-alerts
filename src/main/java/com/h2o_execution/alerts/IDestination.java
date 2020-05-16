package com.h2o_execution.alerts;

import java.io.Serializable;

public interface IDestination extends Serializable
{
    void send(IoI ioi);
}
