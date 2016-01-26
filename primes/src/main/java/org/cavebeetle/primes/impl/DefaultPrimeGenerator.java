package org.cavebeetle.primes.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.sort;
import java.util.List;
import org.cavebeetle.primes.PrimeGenerator;

public final class DefaultPrimeGenerator
        implements
            PrimeGenerator
{
    public static final class DefaultMaker
            implements
                Maker
    {
        private final IntGenerator.Maker intGeneratorMaker;

        public DefaultMaker(final IntGenerator.Maker intGeneratorMaker)
        {
            this.intGeneratorMaker = intGeneratorMaker;
        }

        @Override
        public PrimeGenerator make(final int primeCount)
        {
            return new DefaultPrimeGenerator(intGeneratorMaker, primeCount);
        }

        @Override
        public PrimeGenerator make()
        {
            return new DefaultPrimeGenerator(intGeneratorMaker);
        }
    }

    private final IntGenerator.Maker intGeneratorMaker;
    private final List<IntGenerator> generators;
    private int[] primes;

    public DefaultPrimeGenerator(final IntGenerator.Maker intGeneratorMaker, final int primeCount)
    {
        this.intGeneratorMaker = intGeneratorMaker;
        generators = newArrayList();
        primes = new int[primeCount];
        primes[0] = 2;
        for (int i = 1; i < primeCount; i++)
        {
            generators.add(intGeneratorMaker.make(primes[i - 1] + primes[i - 1], primes[i - 1]));
            primes[i] = initNextPrime(primes[i - 1] + 1);
        }
        generators.isEmpty();
    }

    public DefaultPrimeGenerator(final IntGenerator.Maker intGeneratorMaker)
    {
        this.intGeneratorMaker = intGeneratorMaker;
        generators = newArrayList();
        primes = new int[16];
        primes[0] = 2;
        for (int i = 1; i < 16; i++)
        {
            generators.add(intGeneratorMaker.make(primes[i - 1] + primes[i - 1], primes[i - 1]));
            primes[i] = initNextPrime(primes[i - 1] + 1);
        }
    }

    @Override
    public int lastPrimeIndex()
    {
        return generators.isEmpty() ? primes.length - 1 : Integer.MAX_VALUE;
    }

    @Override
    public int primeAtIndex(final int index)
    {
        if (generators.isEmpty())
        {
            checkArgument(0 <= index && index < primes.length, "Index out of bounds.");
        }
        else
        {
            while (!(index < primes.length))
            {
                final int[] newPrimes = new int[primes.length * 2];
                System.arraycopy(primes, 0, newPrimes, 0, primes.length);
                for (int i = primes.length; i < newPrimes.length; i++)
                {
                    generators.add(intGeneratorMaker.make(newPrimes[i - 1] + newPrimes[i - 1], newPrimes[i - 1]));
                    newPrimes[i] = initNextPrime(newPrimes[i - 1] + 1);
                }
                primes = newPrimes;
            }
        }
        return primes[index];
    }

    private int initNextPrime(final int nextPrimeGuess)
    {
        if (nextPrimeGuess != generators.get(0).current())
        {
            return nextPrimeGuess;
        }
        else
        {
            int nextPrime = nextPrimeGuess;
            while (nextPrime == generators.get(0).current())
            {
                while (true)
                {
                    final IntGenerator generator = generators.get(0);
                    if (nextPrime != generator.current())
                    {
                        break;
                    }
                    generator.next();
                    sort(generators);
                }
                nextPrime++;
            }
            return nextPrime;
        }
    }
}
