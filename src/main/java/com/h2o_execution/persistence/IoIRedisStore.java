package com.h2o_execution.persistence;

import com.h2o_execution.domain.IoI;
import lombok.AllArgsConstructor;
import org.apache.flink.shaded.curator.org.apache.curator.shaded.com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Qualifier(StoreName.REDIS)
public class IoIRedisStore extends AbstractIoIStore
{
    private final IoIRedisRepository ioiRedisRepository;

    @Override
    protected Set<Map.Entry<String, List<IoI>>> entrySet()
    {
        return ImmutableList
                .copyOf(ioiRedisRepository.findAll().iterator())
                .stream()
                .flatMap(list -> list.getIoIs().stream())
                .collect(Collectors.groupingBy(IoI::getSymbol))
                .entrySet();
    }

    @Override
    protected List<IoI> get(String symbol)
    {
        return ioiRedisRepository.findById(symbol).orElseThrow(RuntimeException::new).getIoIs();
    }

    @Override
    protected void put(String symbol, List<IoI> ioIS)
    {
        ioiRedisRepository.save(new IoIRedisList(symbol, ioIS));
    }
}
