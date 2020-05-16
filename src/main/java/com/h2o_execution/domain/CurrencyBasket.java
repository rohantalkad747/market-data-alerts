package com.h2o_execution.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A currency basket is comprised of a mix of several currencies with different
 * weightings. It is often used to set the market value of another currency,
 * a practice commonly known as a currency peg. This currency basket takes
 * into account purchasing power parity, market value, and inflation expectations,
 * of the underlying currencies against the given peg.
 *
 * @author Rohan Talkad
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyBasket implements Serializable
{
    private Currency peg;
    private String currencyIndexName;
    private Currency[] currencies;
    private double[] weights;

    /**
     * Constructs a currency basket.
     *
     * @param currencyIndexName The name of this currency index.
     * @param peg               The currency to hedge against.
     * @param currencies        The currencies in this basket.
     */
    public CurrencyBasket(final String currencyIndexName, final Currency peg, final Currency[] currencies)
    {
        this.currencyIndexName = currencyIndexName;
        this.peg = peg;
        this.currencies = currencies;
    }

    /**
     * Updates the weights for this basket.
     */
    public void updateWeights()
    {
        final double[] nextYearInflation = calcNextYearInflation();
        final double[] simpleBigMarket = getSimpleBigMarket();
        final double[] correction = calcCorrection(nextYearInflation);
        calcCurrencyWeight(simpleBigMarket, correction);
    }

    private void calcCurrencyWeight(final double[] simpleBigMarket, final double[] correction)
    {
        final double[] unadjustedWeights = new double[currencies.length];
        double totalWeight = 0;
        for (int i = 0; i < currencies.length; i++)
        {
            unadjustedWeights[i] = 0.20 * (currencies[i].getMarketValueAgainst(peg) / simpleBigMarket[i]) * correction[i];
            totalWeight += unadjustedWeights[i];
        }
        if (totalWeight == 0)
        {
            throw new RuntimeException("Total weight cannot be zero!");
        }
        final double[] adjustedWeights = new double[currencies.length];
        for (int i = 0; i < currencies.length; i++)
        {
            adjustedWeights[i] = unadjustedWeights[i] / totalWeight;
        }

        this.weights = adjustedWeights;
    }

    private double[] calcCorrection(final double[] nextYearInflation)
    {
        final double[] correction = new double[nextYearInflation.length];
        for (int i = 0; i < nextYearInflation.length; i++)
        {
            correction[i] = Math.pow((1 + nextYearInflation[i]), 2);
        }
        return correction;
    }

    private double[] calcNextYearInflation()
    {
        final double[] nextYearInflation = new double[currencies.length];
        for (int i = 0; i < currencies.length; i++)
        {
            final double presentYearInflation = currencies[i].getPresentYearInflation();
            final double lastYearInflation = currencies[i].getLastYearInflation();
            nextYearInflation[i] = presentYearInflation + (presentYearInflation - lastYearInflation);
        }
        return nextYearInflation;
    }

    private double[] getSimpleBigMarket()
    {
        final double[] simpleBigMarket = new double[currencies.length];
        for (int i = 0; i < currencies.length; i++)
        {
            simpleBigMarket[i] = (currencies[i].getPurchasingPowerParityAgainst(peg) + currencies[i].getMarketValueAgainst(peg)) / 2;
        }
        return simpleBigMarket;
    }
}