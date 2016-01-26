package org.cavebeetle.blueprint;

import com.google.common.base.Preconditions;

public final class Merge
{
    public static final Property merge(final Property left, final Property right)
    {
        Preconditions.checkArgument(left.key().equals(right.key()), "Invalid merge: left.key != right.key.");
        if (left.equals(right) || right.equals(Property.MISSING))
        {
            return left;
        }
        if (left.equals(Property.MISSING))
        {
            return right;
        }
        throw new IllegalStateException("Invalid merge: left.value != right.value.");
    }
}
