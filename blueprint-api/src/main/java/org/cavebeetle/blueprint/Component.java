package org.cavebeetle.blueprint;

import org.cavebeetle.text.Show;

/**
 * A {@code Component} is part of a {@code Configuration} instance. It represents either a {@code Property} or a
 * {@code Group}.
 *
 * @param <T> the actual type of this {@code Component}.
 */
public interface Component<T>
        extends
            Comparable<T>,
            Show
{
    /**
     * Returns this {@code Component}'s key.
     *
     * @return this {@code Component}'s key.
     */
    String key();
}
