package com.h2o_execution.alerts;

import lombok.*;

/**
 * Indication of interest is the configuration for a market data alert.
 * IoIs can be permanent or ephemeral. The former is specified by
 * {@link com.h2o_execution.alerts.Threshold.Type#PERCENT}, is updated daily
 * based on the specified {@linkplain com.h2o_execution.alerts.Threshold.Target target}, and
 * executes only once daily. However, if the threshold type is set to {@linkplain com.h2o_execution.alerts.Threshold.Type#ABSOLUTE},
 * the IoI is deactivated forever after crossing the specified value. An IoI can currently be sent
 * through three media: TCP, SMS, or SMTP. An IoI can be cancelled at any time given the id and symbol.
 *
 * @author Rohan Talkad
 */
@NoArgsConstructor
@Data
public class IoI
{
    private long id;
    private IDestination destination;
    private String symbol;
    private Threshold threshold;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private boolean active;

    public IoI(final long id, final IDestination destination, final String symbol, final Threshold threshold)
    {
        this.id = id;
        this.destination = destination;
        this.symbol = symbol;
        this.threshold = threshold;
        this.active = true;
    }

    public boolean isActive()
    {
        return this.active;
    }

    public void setActive()
    {
        this.active = true;
    }

    public void setInactivate()
    {
        this.active = false;
    }
}
