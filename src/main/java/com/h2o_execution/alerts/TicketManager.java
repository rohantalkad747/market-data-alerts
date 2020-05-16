package com.h2o_execution.alerts;

import com.indigo.base.Ticket;
import com.indigo.utils.Inbox;

import java.util.List;

public class TicketManager
{
    public Inbox flushToInbox(final List<? extends Ticket> tickets) throws Exception
    {
        final Inbox ibx = Inbox.getInstance();
        for (final Ticket tx : tickets)
        {
            ibx.acknowledge(tx);
        }
        return ibx;
    }

    public long txOfProp(final List<? extends Ticket> tickets, final String suffix) throws Exception
    {
        return tickets
                .stream()
                .filter(ticket ->
                {
                    try
                    {
                        return (boolean) Ticket.class.getMethod("is" + suffix).invoke(ticket);
                    }
                    catch (final Exception e)
                    {
                        e.printStackTrace();
                    }
                    return false;
                })
                .count();
    }
}
