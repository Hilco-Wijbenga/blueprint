package org.cavebeetle.primes;

public interface PrimeGenerator
{
    public static interface Maker
    {
        PrimeGenerator make(int primeCount);

        PrimeGenerator make();
    }

    int lastPrimeIndex();

    int primeAtIndex(int index);
}
