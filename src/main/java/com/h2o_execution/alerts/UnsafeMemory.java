package com.h2o_execution.alerts;

import jdk.internal.misc.Unsafe;

import java.lang.reflect.Field;

class UnsafeMemory
{
    private static Unsafe unsafe;
    private static final long byteArrayOffset = unsafe.arrayBaseOffset(byte[].class);
    private static final long charArrayOffset = unsafe.arrayBaseOffset(char[].class);
    private static final int SIZE_OF_INT = 4;
    private static final int SIZE_OF_LONG = 8;
    private static final int SIZE_OF_DOUBLE = 8;

    static
    {
        try
        {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private final byte[] buffer;
    private int pos = 0;

    public UnsafeMemory(final byte[] buffer)
    {
        this.buffer = buffer;
    }

    public void reset()
    {
        this.pos = 0;
    }

    public void putInt(final int value)
    {
        unsafe.putInt(buffer, byteArrayOffset + pos, value);
        pos += SIZE_OF_INT;
    }

    public int getInt()
    {
        int value = unsafe.getInt(buffer, byteArrayOffset + pos);
        pos += SIZE_OF_INT;
        return value;
    }

    public void putLong(final long value)
    {
        unsafe.putLong(buffer, byteArrayOffset + pos, value);
        pos += SIZE_OF_LONG;
    }

    public long getLong()
    {
        long value = unsafe.getLong(buffer, byteArrayOffset + pos);
        pos += SIZE_OF_LONG;
        return value;
    }

    public void putDouble(final double value)
    {
        unsafe.putDouble(buffer, byteArrayOffset + pos, value);
        pos += SIZE_OF_DOUBLE;
    }

    public double getDouble()
    {
        double value = unsafe.getDouble(buffer, byteArrayOffset + pos);
        pos += SIZE_OF_DOUBLE;
        return value;
    }

    public void putCharArray(final char[] values)
    {
        putInt(values.length);

        long bytesToCopy = values.length << 3;
        unsafe.copyMemory(values, charArrayOffset, buffer, byteArrayOffset + pos, bytesToCopy);
        pos += bytesToCopy;
    }

    public char[] getCharArray()
    {
        int arraySize = getInt();
        char[] values = new char[arraySize];

        long bytesToCopy = values.length << 3;
        unsafe.copyMemory(buffer, byteArrayOffset + pos, values, charArrayOffset, bytesToCopy);
        pos += bytesToCopy;
        return values;
    }

}
