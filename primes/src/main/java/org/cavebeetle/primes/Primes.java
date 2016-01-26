package org.cavebeetle.primes;

import java.util.Iterator;
import org.cavebeetle.primes.impl.DefaultIntGenerator;
import org.cavebeetle.primes.impl.DefaultPrimeGenerator;
import org.cavebeetle.primes.impl.DefaultPrimes;
import org.cavebeetle.primes.impl.IntGenerator;

public interface Primes
        extends
            Iterator<Integer>
{
    public final class Maker
    {
        public static final Primes make()
        {
            final PrimeGenerator primeGenerator = new DefaultPrimeGenerator(Lazy.INT_GENERATOR_MAKER, 1000);
            return new DefaultPrimes(primeGenerator);
        }

        public static interface Lazy
        {
            IntGenerator.Maker INT_GENERATOR_MAKER = new DefaultIntGenerator.DefaultMaker();
            PrimeGenerator.Maker PRIME_GENERATOR_MAKER = new DefaultPrimeGenerator.DefaultMaker(INT_GENERATOR_MAKER);
        }
    }

    void skip(int skipCount);
}
