package org.cavebeetle.blueprint;

import com.google.common.base.Preconditions;
import com.google.common.collect.Ordering;

public final class ComponentOrdering
        extends
            Ordering<Component<?>>
{
    @Override
    public int compare(final Component<?> left, final Component<?> right)
    {
        Preconditions.checkArgument(left instanceof Property || left instanceof Group, "Invalid type: " + left.getClass() + ".");
        Preconditions.checkArgument(right instanceof Property || right instanceof Group, "Invalid type: " + right.getClass() + ".");
        if (left instanceof Property)
        {
            return right instanceof Group ? -1 : ((Property) left).compareTo((Property) right);
        }
        else
        {
            return right instanceof Property ? +1 : ((Group) left).compareTo((Group) right);
        }
    }
}
