package com.h2o_execution.streams;

import java.io.IOException;

public interface QuestradeSymbolService
{
    String getId(Symbol symbol) throws IOException;
}
