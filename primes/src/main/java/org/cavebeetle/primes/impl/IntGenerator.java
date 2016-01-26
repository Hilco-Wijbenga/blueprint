package org.cavebeetle.primes.impl;

public interface IntGenerator
        extends
            Comparable<IntGenerator>
{
    public interface Maker
    {
        IntGenerator make(int first, int step);
    }

    int step();

    int current();

    void next();
}
