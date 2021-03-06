package com.h2o_execution.persistence;

import com.h2o_execution.alerts.SMSDestination;
import com.h2o_execution.alerts.Threshold;
import com.h2o_execution.domain.IoI;
import com.h2o_execution.streams.Exchange;
import net.openhft.chronicle.map.ChronicleMap;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Qualifier(StoreName.CHRONICLE)
public class IoIChronicleStore extends AbstractIoIStore
{
    private final ChronicleMap<String, List<IoI>> chronicleMap;

    public IoIChronicleStore() throws IOException
    {
        final List<IoI> averageValue = Stream
                .generate(IoIChronicleStore::random)
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
                new Threshold(Threshold.Direction.POSITIVE, Threshold.Type.PERCENT, Threshold.Target.OPEN, 10),
                Exchange.NYSE);
    }

    @Override
    protected Set<Entry<String, List<IoI>>> entrySet()
    {
        return chronicleMap.entrySet();
    }

    @Override
    protected List<IoI> get(final String symbol)
    {
        return chronicleMap.get(symbol);
    }

    @Override
    protected void put(final String symbol, final List<IoI> ioIS)
    {
        chronicleMap.put(symbol, ioIS);
    }
}
