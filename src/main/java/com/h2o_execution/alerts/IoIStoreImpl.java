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
        List<IoI> averageValue = Stream
                .generate(IoI::random)
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

    private List<IoI> getMatchingIoIs(Predicate<? super IoI> fun)
    {
        List<IoI> matchingIoI = new ArrayList<>();
        Set<Entry<String, List<IoI>>> entries = chronicleMap.entrySet();
        for (Entry<String, List<IoI>> ioiList : entries)
        {
            List<IoI> collect = ioiList
                    .getValue()
                    .stream()
                    .filter(fun)
                    .collect(Collectors.toList());
            matchingIoI.addAll(collect);
        }
        return matchingIoI;
    }

    @Override
    public synchronized List<IoI> getIoIsByTarget(Threshold.Target target)
    {
        return getMatchingIoIs(ioi -> ioi.getThreshold().getTarget() == target);
    }

    @Override
    public List<IoI> getIoIsBySymbol(String symbol)
    {
        return getMatchingIoIs(ioi -> ioi.getSymbol().equals(symbol));
    }

    @Override
    public synchronized boolean removeEntry(String symbol, long id)
    {
        List<IoI> ioIS = chronicleMap.get(symbol);
        if (ioIS != null)
        {
            Iterator<IoI> iterator = ioIS.iterator();
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
    public void bulkRemove(List<IoI> iois)
    {
        Map<String, List<IoI>> deletionCandidates = iois.stream().collect(Collectors.groupingBy(IoI::getSymbol));
        for (Entry<String, List<IoI>> entry : deletionCandidates.entrySet())
        {

        }
    }

    @Override
    public synchronized void addEntry(IoI ioi)
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
