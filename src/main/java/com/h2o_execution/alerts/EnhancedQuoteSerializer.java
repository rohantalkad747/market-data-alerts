package com.h2o_execution.alerts;

import com.h2o_execution.domain.EnhancedQuote;
import org.springframework.stereotype.Service;

@Service
public class EnhancedQuoteSerializer implements IEnhancedQuoteSerializer
{
    public byte[] serialize(EnhancedQuote quote)
    {
        byte[] buffer = new byte[1024];
        UnsafeMemory byteBuffer = new UnsafeMemory(buffer);
        byteBuffer.putCharArray(quote.toString().toCharArray());
        return buffer;
    }
}
