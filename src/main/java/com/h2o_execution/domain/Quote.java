package com.h2o_execution.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Quote implements Serializable
{
    private String symbol;
    private double price;
}
