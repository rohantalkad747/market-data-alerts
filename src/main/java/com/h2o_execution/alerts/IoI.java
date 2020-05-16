package com.h2o_execution.alerts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Indication of interest is configuration for a market data alert.
 * There are two types of IoIs: permanent and ephemeral. The former is specified by
 * the {@linkplain com.h2o_execution.alerts.Threshold.Type#PERCENT} and is updated daily
 * based on the specified {@linkplain com.h2o_execution.alerts.Threshold.Target target}. However,
 * if the threshold type is set to {@linkplain com.h2o_execution.alerts.Threshold.Type#ABSOLUTE},
 * the IoI is deactivated upon reaching the crossing the specified value. An IoI can be currently sent
 * through three media: TCP, SMS, or SMTP. An IoI can be cancelled at any time given the id and symbol.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class IoI
{
    private long id;
    private IDestination destination;
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
