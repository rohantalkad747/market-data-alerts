package com.h2o_execution.streams;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Symbol
{
    private final String symbol;
    private final Exchange exchange;
}
