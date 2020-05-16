package com.h2o_execution.serializers;

public abstract class SerdesFactory
{
    public static IQuoteSerializer getInstance(final SerdesType serdesType)
    {
        final IQuoteSerializer quoteSerializer;
        switch (serdesType)
        {
            case JAVA:
                quoteSerializer = new JavaQuoteSerdes();
                break;
            case UNSAFE:
                quoteSerializer = new UnsafeQuoteSerdes();
                break;
            case BYTE_BUFFER:
                quoteSerializer = new ByteBufferQuoteSerdes();
                break;
            default:
                throw new RuntimeException("Unsupported serialization type!");
        }
        return quoteSerializer;
    }

    enum SerdesType
    {
        JAVA,
        UNSAFE,
        BYTE_BUFFER
    }
}
