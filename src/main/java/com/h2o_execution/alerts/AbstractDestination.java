package com.h2o_execution.alerts;

import com.h2o_execution.domain.EnhancedQuote;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class AbstractDestination implements IDestination
{
    @Override
    public final void send(EnhancedQuote quote, Threshold th)
    {
        String formattedIoI = formatIoI(quote, th);
        this.sendToDestination(formattedIoI);
    }

    protected abstract void sendToDestination(String formattedIoI);

    private String formatIoI(EnhancedQuote quote, Threshold th)
    {
        String direction = th.getDirection().getName();
        StringBuilder reason = new StringBuilder("Went " + direction);
        if (th.getType() == Threshold.Type.ABSOLUTE)
        {
            reason.append(" to $").append(th.getAbsValue());
        }
        else
        {
            reason.append(" by ").append(th.getPctValue()).append(" %");
        }
        reason.append("\n\n").append(quote.toString());
        return reason.toString();
    }
}
