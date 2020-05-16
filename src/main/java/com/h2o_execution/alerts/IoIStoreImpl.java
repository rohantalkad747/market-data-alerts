package com.h2o_execution.alerts;

import net.openhft.chronicle.map.ChronicleMap;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IoIStoreImpl implements IoIStore
{
    private final ExecutorService executorService;
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
        executorService = Executors.newCachedThreadPool();
    }

    @Override
    public synchronized List<IoI> getMatchingIoIs(Threshold.Target target)
    {
        List<IoI> matchingIoI = new ArrayList<>();
        Set<Map.Entry<String, List<IoI>>> entries = this.chronicleMap.entrySet();
        for (Map.Entry<String, List<IoI>> ioiList :  entries) {
            List<IoI> collect = ioiList
                    .getValue()
                    .stream()
                    .filter(ioi -> ioi.getThreshold().getTarget() == target)
                    .collect(Collectors.toList());
            matchingIoI.addAll(collect);
        }
        return matchingIoI;
    }

    @Override
    public synchronized boolean removeEntry(String symbol, long id)
    {
        List<IoI> ioIS = this.chronicleMap.get(symbol);
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
    public synchronized void addEntry(IoI ioi)
    {
        List<IoI> ioIS = this.chronicleMap.get(ioi.getSymbol());
        if (ioIS == null)
        {
            ioIS = new ArrayList<>();
        }
        ioIS.add(ioi);
        this.chronicleMap.put(ioi.getSymbol(), ioIS);
    }
}
