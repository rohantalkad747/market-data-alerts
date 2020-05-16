package com.h2o_execution.serializers;

import com.h2o_execution.domain.Quote;


public interface IQuoteSerializer
{
    byte[] serialize(Quote quote) throws Exception;

    Quote deserialize(byte[] bytes) throws Exception;
}
