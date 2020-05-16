package com.h2o_execution.alerts;

import com.indigo.base.Ticket;
import com.indigo.message.ZOrder;
import com.indigo.utils.Inbox;

import java.util.List;

public class TicketManager
{
    public Inbox flushToInbox(List<? extends Ticket> tickets) throws Exception
    {
        Inbox ibx = Inbox.getInstance();
        for (Ticket tx : tickets)
        {
            ibx.acknowledge(tx);
        }
        return ibx;
    }

    public long txOfProp(List<? extends Ticket> tickets, String suffix) throws Exception
    {
        return tickets
                .stream()
                .filter(ticket ->
                {
                    try
                    {
                        return (boolean) Ticket.class.getMethod("is" + suffix).invoke(ticket);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    return false;
                })
                .count();
    }
}
