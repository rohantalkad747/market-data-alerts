package com.h2o_execution.domain;

import com.h2o_execution.alerts.IDestination;
import com.h2o_execution.alerts.Threshold;
import com.h2o_execution.streams.Exchange;
import lombok.*;

import java.io.Serializable;

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
public class IoI implements Serializable
{
    private long id;
    private IDestination destination;
    private String symbol;
    private Threshold threshold;
    private Exchange exchange;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private boolean activeStatus;

    public IoI(final long id, final IDestination destination, final String symbol, final Threshold threshold, final Exchange exchange)
    {
        this.id = id;
        this.destination = destination;
        this.symbol = symbol;
        this.threshold = threshold;
        this.exchange = exchange;
        this.activeStatus = true;
    }

    public void activate()
    {
        this.activeStatus = true;
    }

    public void deactivate()
    {
        this.activeStatus = false;
    }
}
