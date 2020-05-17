package com.h2o_execution.serializers;

import com.h2o_execution.domain.Quote;
import org.springframework.stereotype.Service;

@Service
public class UnsafeQuoteSerdes implements IQuoteSerializer
{
    public byte[] serialize(final Quote quote)
    {
        final byte[] buffer = new byte[1024];
        final UnsafeMemory byteBuffer = new UnsafeMemory(buffer);
        byteBuffer.putCharArray(quote.getSymbol().toCharArray());
        byteBuffer.putDouble(quote.getLastTradePrice());
        return buffer;
    }

    @Override
    public Quote deserialize(final byte[] bytes)
    {
        final UnsafeMemory byteBuffer = new UnsafeMemory(bytes);
        final char[] symbolArr = byteBuffer.getCharArray();
        final double px = byteBuffer.getDouble();
        return new Quote(new String(symbolArr), px);
    }
}
