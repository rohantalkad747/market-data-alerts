package com.h2o_execution.alerts;

import com.h2o_execution.domain.EnhancedQuote;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class AbstractDestination implements IDestination
{
    @Override
    public final void send(final EnhancedQuote quote, final Threshold th)
    {
        final String formattedIoI = formatIoI(quote, th);
        this.sendToDestination(formattedIoI);
    }

    protected abstract void sendToDestination(String formattedIoI);

    private String formatIoI(final EnhancedQuote quote, final Threshold th)
    {
        final String direction = th.getDirection().getName();
        final StringBuilder reason = new StringBuilder("Went " + direction);
        if (th.getType() == Threshold.Type.ABSOLUTE)
        {
            reason.append(" to $").append(th.getAbsValue());
        }
        else
        {
            reason.append(" by ").append(th.getPctValue()).append(" %");
        }
        reason.append(th.getDirection().getDate()).append("\n\n").append(quote.toString());
        return reason.toString();
    }
}
