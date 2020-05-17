package com.h2o_execution.misc;

import lombok.Getter;

import javax.annotation.PostConstruct;

public abstract class AbstractQTAccessAware implements QTAccessKeyAware
{
    private final QTAccessKeyRegistry registry;
    @Getter
    protected String accessKey;

    protected AbstractQTAccessAware(final QTAccessKeyRegistry registry)
    {
        this.registry = registry;
    }

    @PostConstruct
    public void register()
    {
        this.registry.register(this);
    }
}
