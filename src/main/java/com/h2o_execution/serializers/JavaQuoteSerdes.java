package com.h2o_execution.serializers;

import com.h2o_execution.domain.Quote;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class JavaQuoteSerdes implements IQuoteSerializer
{
    @Override
    public byte[] serialize(final Quote quote) throws Exception
    {
        try
                (
                        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        final ObjectOutputStream oos = new ObjectOutputStream(bos)
                )
        {
            oos.writeObject(quote);
            return bos.toByteArray();
        }
    }

    @Override
    public Quote deserialize(final byte[] bytes) throws Exception
    {
        try
                (
                        final ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                        final ObjectInputStream ois = new ObjectInputStream(bais)
                )
        {
            return (Quote) ois.readObject();
        }
    }
}
