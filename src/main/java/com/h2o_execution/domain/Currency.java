package com.h2o_execution.domain;

import java.io.Serializable;

public interface Currency extends Serializable, Cloneable
{
    double getMarketValueAgainst(Currency peg);

    double getPresentYearInflation();

    double getLastYearInflation();

    double getPurchasingPowerParityAgainst(Currency peg);
}
