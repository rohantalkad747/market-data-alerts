package com.h2o_execution.alerts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IoI
{
    private long id;
    private IDestination output;
    private String symbol;
    private Threshold threshold;

    public static IoI random()
    {
        return new IoI((int) Math.random(),
                new SMSDestination(4166938981L),
                "AAPL",
                new Threshold(Threshold.Direction.POSITIVE, Threshold.Type.PERCENT, Threshold.Target.OPEN, 10));
    }
}
