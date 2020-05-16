package com.h2o_execution.alerts;

import net.openhft.chronicle.map.ChronicleMap;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IoIStoreImpl implements IoIStore
{
    private final ChronicleMap<String, List<IoI>> chronicleMap;

    public IoIStoreImpl() throws IOException
    {
        final List<IoI> averageValue = Stream
                .generate(IoIStoreImpl::random)
                .limit(1000)
                .collect(Collectors.toList());
        chronicleMap = ChronicleMap
                .of(String.class, (Class<List<IoI>>) (Class) List.class)
                .name("ioi-map")
                .averageKey("AAPL")
                .averageValue(averageValue)
                .entries(10000)
                .createPersistedTo(new File("ioi.dat"));
    }

    public static IoI random()
    {
        return new IoI((int) Math.random(),
                new SMSDestination(4166938981L),
                "AAPL",
                new Threshold(Threshold.Direction.POSITIVE, Threshold.Type.PERCENT, Threshold.Target.OPEN, 10));
    }

    private List<IoI> getMatchingIoIs(final Predicate<? super IoI> fun)
    {
        final List<IoI> matchingIoI = new ArrayList<>();
        final Set<Entry<String, List<IoI>>> entries = chronicleMap.entrySet();
        for (final Entry<String, List<IoI>> ioiList : entries)
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
        final List<IoI> ioIS = chronicleMap.get(symbol);
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
    public void bulkRemove(final List<IoI> iois)
    {
        final Map<String, List<IoI>> deletionCandidates = iois.stream().collect(Collectors.groupingBy(IoI::getSymbol));
        for (final Entry<String, List<IoI>> entry : deletionCandidates.entrySet())
        {

        }
    }

    @Override
    public synchronized void addEntry(final IoI ioi)
    {
        List<IoI> ioIS = chronicleMap.get(ioi.getSymbol());
        if (ioIS == null)
        {
            ioIS = new ArrayList<>();
        }
        ioIS.add(ioi);
        chronicleMap.put(ioi.getSymbol(), ioIS);
    }
}
