package com.h2o_execution.persistence;

import com.h2o_execution.alerts.Threshold;
import com.h2o_execution.domain.IoI;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class AbstractIoIStore implements IoIStore
{
    private List<IoI> getMatchingIoIs(final Predicate<? super IoI> fun)
    {
        final List<IoI> matchingIoI = new ArrayList<>();
        final Set<Map.Entry<String, List<IoI>>> entries = entrySet();
        for (final Map.Entry<String, List<IoI>> ioiList : entries)
        {
            final List<IoI> collect = ioiList
                    .getValue()
                    .stream()
                    .filter(fun)
                    .collect(Collectors.toList());
            matchingIoI.addAll(collect);
        }
        return matchingIoI;
    }

    @Override
    public synchronized List<IoI> getIoIsByTarget(final Threshold.Target target)
    {
        return getMatchingIoIs(ioi -> ioi.getThreshold().getTarget() == target);
    }

    @Override
    public List<IoI> getIoIsBySymbol(final String symbol)
    {
        return getMatchingIoIs(ioi -> ioi.getSymbol().equals(symbol));
    }

    @Override
    public synchronized boolean removeEntry(final String symbol, final long id)
    {
        final List<IoI> ioIS = get(symbol);
        if (ioIS != null)
        {
            final Iterator<IoI> iterator = ioIS.iterator();
            while (iterator.hasNext())
            {
                if (iterator.next().getId() == id)
                {
                    iterator.remove();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public synchronized void addEntry(final IoI ioi)
    {
        List<IoI> ioIS = get(ioi.getSymbol());
        if (ioIS == null)
        {
            ioIS = new ArrayList<>();
        }
        ioIS.add(ioi);
        put(ioi.getSymbol(), ioIS);
    }

    protected abstract Set<Map.Entry<String, List<IoI>>> entrySet();

    protected abstract List<IoI> get(String symbol);

    protected abstract void put(String symbol, List<IoI> ioIS);
}
