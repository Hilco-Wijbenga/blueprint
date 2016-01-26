package org.cavebeetle.primes.impl;

public final class DefaultIntGenerator
        implements
            IntGenerator
{
    public static final class DefaultMaker
            implements
                Maker
    {
        @Override
        public IntGenerator make(final int first, final int step)
        {
            return new DefaultIntGenerator(first, step);
        }
    }

    private final int step;
    private int current;

    public DefaultIntGenerator(final int first, final int step)
    {
        this.step = step;
        current = first;
    }

    @Override
    public int step()
    {
        return step;
    }

    @Override
    public int current()
    {
        return current;
    }

    @Override
    public void next()
    {
        current += step;
    }

    @Override
    public int compareTo(final IntGenerator otherIntGenerator)
    {
        return current == otherIntGenerator.current()
            ? step == otherIntGenerator.step()
                ? 0
                : step < otherIntGenerator.step()
                    ? -1
                    : +1
            : current < otherIntGenerator.current()
                ? -1
                : +1;
    }
}
