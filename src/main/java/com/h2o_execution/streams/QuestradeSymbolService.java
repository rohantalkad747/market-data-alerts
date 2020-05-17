package com.h2o_execution.streams;

import com.h2o_execution.misc.QTAccessKeyAware;

import java.io.IOException;

public interface QuestradeSymbolService extends QTAccessKeyAware
{
    String getId(Symbol symbol) throws IOException;
}
