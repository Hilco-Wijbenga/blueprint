package org.cavebeetle.primes.impl;

import org.cavebeetle.primes.PrimeGenerator;
import org.cavebeetle.primes.Primes;

public final class DefaultPrimes
        implements
            Primes
{
    private final PrimeGenerator primeGenerator;
    private int primeIndex;

    public DefaultPrimes(final PrimeGenerator primeGenerator)
    {
        this.primeGenerator = primeGenerator;
    }

    @Override
    public boolean hasNext()
    {
        return primeIndex <= primeGenerator.lastPrimeIndex();
    }

    @Override
    public Integer next()
    {
        return Integer.valueOf(primeGenerator.primeAtIndex(primeIndex++));
    }

    @Override
    public void skip(final int skipCount)
    {
        primeIndex += skipCount;
    }
}
