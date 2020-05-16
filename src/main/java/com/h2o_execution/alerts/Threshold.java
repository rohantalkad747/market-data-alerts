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

    private Threshold(final Direction direction, final Type type, final Target target)
    {
        this.direction = direction;
        this.type = type;
        this.target = target;
    }

    public Threshold(final Direction direction, final Type type, final Target target, final double v)
    {
        this(direction, type, target);
        if (type == Type.ABSOLUTE)
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
        POSITIVE("up")
                {
                    public boolean isSatisfied(final double px, final Quote quote)
                    {
                        return quote.getPrice() > px;
                    }

                    @Override
                    public double getTarget(final double px, final double pct)
                    {
                        return px * (1 + pct);
                    }
                },
        NEGATIVE("down")
                {
                    public boolean isSatisfied(final double px, final Quote quote)
                    {
                        return quote.getPrice() < px;
                    }

                    @Override
                    public double getTarget(final double px, final double pct)
                    {
                        return px * (1 - pct);
                    }
                };

        private final String drxn;

        Direction(final String drxn)
        {
            this.drxn = drxn;
        }

        public String getName()
        {
            return drxn;
        }

        public abstract boolean isSatisfied(double px, Quote quote);

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
