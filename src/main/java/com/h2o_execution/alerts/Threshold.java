package com.h2o_execution.alerts;

import com.h2o_execution.domain.Quote;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
public class Threshold
{
    private Direction direction;
    private Type type;
    private Target target;
    private double absValue;
    private double pctValue;

    private Threshold(Direction direction, Type type, Target target)
    {
        this.direction = direction;
        this.type = type;
        this.target = target;
    }

    public Threshold(Direction direction, Type type, Target target, double v)
    {
        this(direction, type, target);
        if ( type == Type.ABSOLUTE )
        {
            this.absValue = v;
        }
        else
        {
            this.pctValue = v;
        }
    }

    public enum Direction
    {
        POSITIVE
                {
                    public boolean getPxThreshold(double px, Quote quote)
                    {
                        return quote.getPrice() > px;
                    }

                    @Override
                    public double getTarget(double px, double pct)
                    {
                        return px * ( 1 + pct );
                    }
                },
        NEGATIVE
                {
                    public boolean getPxThreshold(double px, Quote quote)
                    {
                        return quote.getPrice() < px;
                    }

                    @Override
                    public double getTarget(double px, double pct)
                    {
                        return px * ( 1 - pct );
                    }
                };

        public abstract boolean getPxThreshold(double px, Quote quote);
        public abstract double getTarget(double px, double pct);
    }

    public enum Target
    {
        OPEN,
        CLOSE,
    }

    /**
     * A threshold type describes when an alert should be triggered.
     * For example, a threshold type of {@link Type#PERCENT} indicates
     * that the security must deviate by x% from a {@link Target#CLOSE} or
     * {@link Target#OPEN}. Whereas absolute is simply a crossing point for a security.
     *
     * @author Rohan
     */
    public enum Type
    {
        /**
         * Percent of price deviation for the security.
         */
        PERCENT,
        /**
         * Price crossing point.
         */
        ABSOLUTE
    }
}
