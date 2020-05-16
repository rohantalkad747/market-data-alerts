package com.h2o_execution.serializers;

import com.h2o_execution.domain.Quote;

import java.nio.ByteBuffer;

public class ByteBufferQuoteSerdes implements IQuoteSerializer
{
    @Override
    public byte[] serialize(final Quote quote)
    {
        final byte[] buffer = new byte[1024];
        final ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
        final char[] symbolArr = quote.getSymbol().toCharArray();
        byteBuffer.putInt(symbolArr.length);
        byteBuffer.asCharBuffer().put(symbolArr);
        byteBuffer.position(byteBuffer.position() + (symbolArr.length * 2));
        byteBuffer.putDouble(quote.getPrice());
        return buffer;
    }

    @Override
    public Quote deserialize(final byte[] bytes)
    {
        final ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        final int length = byteBuffer.getInt();
        final char[] symbolArr = new char[length];
        populateCharArray(byteBuffer, length, symbolArr);
        final double price = byteBuffer.getDouble();
        return new Quote(new String(symbolArr), price);
    }

    private void populateCharArray(ByteBuffer byteBuffer, int length, char[] symbolArr)
    {
        for (int i = 0; i < length; i++)
        {
            symbolArr[i] = byteBuffer.getChar();
        }
    }
}
