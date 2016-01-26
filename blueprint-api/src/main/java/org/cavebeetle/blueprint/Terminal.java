package org.cavebeetle.blueprint;

import org.cavebeetle.text.Show;

/**
 * A {@code Terminal} is an element that simply stores a single line of text.
 *
 * @param <T> the actual type of this {@code Terminal}.
 */
public interface Terminal<T>
        extends
            Comparable<T>,
            Show
{
    /**
     * Returns this {@code Terminal}'s value.
     *
     * @return this {@code Terminal}'s value.
     */
    String value();
}
