package com.h2o_execution.domain;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface EQMapper
{
    EnhancedQuote map(QuestradeEnhancedPartialQuote s1, TradierEnhancedPartialQuote s2);
}
