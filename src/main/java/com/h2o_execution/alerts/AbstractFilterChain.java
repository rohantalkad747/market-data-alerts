package com.h2o_execution.alerts;

import java.util.List;

/**
 * @author Rohan Talkad
 */
public abstract class AbstractFilterChain<U, P>
{
    private AbstractFilterChain<U, P> next;

    public static <X, Y> void linkFilters(final List<AbstractFilterChain<X, Y>> filters)
    {
        if (filters == null || filters.isEmpty())
        {
            throw new IllegalArgumentException("Filters must have at least one item!");
        }
        AbstractFilterChain<X, Y> curHead = filters.get(0);
        for (int i = 1; i < filters.size(); i++)
        {
            final AbstractFilterChain<X, Y> next = filters.get(i);
            curHead.setNextFilter(next);
            curHead = next;
        }
    }

    public void setNextFilter(final AbstractFilterChain<U, P> filter)
    {
        next = filter;
    }

    public void execute(final U upstream, final P payload)
    {
        doFilter(upstream, payload);
        if (next != null)
        {
            next.doFilter(upstream, payload);
        }
    }

    protected abstract void doFilter(U upstream, P payload);
}
