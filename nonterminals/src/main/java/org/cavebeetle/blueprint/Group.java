package org.cavebeetle.blueprint;

import java.util.Collections;
import java.util.List;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.Root;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

@Root
public final class Group
        implements
            Component<Group>
{
    @Attribute
    private final String key;
    @ElementListUnion({
        @ElementList(entry = "property", inline = true, type = Property.class, required = false),
        @ElementList(entry = "group", inline = true, type = Group.class, required = false)
    })
    private final List<Component<?>> components;

    public Group(@Attribute(name = "key") final String key)
    {
        this.key = key;
        components = Collections.emptyList();
    }

    public Group(
            @Attribute(name = "key") final String key,
            @ElementListUnion({
                @ElementList(entry = "property", inline = true, type = Property.class, required = false),
                @ElementList(entry = "group", inline = true, type = Group.class, required = false)
            }) final List<Component<?>> components)
    {
        this.key = key;
        this.components = components;
    }

    @Override
    public String key()
    {
        return key;
    }

    @Override
    public int compareTo(final Group other)
    {
        final Ordering<Component<?>> ordering = new ComponentOrdering();
        return ComparisonChain
                .start()
                .compare(components, other.components, ordering.lexicographical())
                .result();
    }

    @Override
    public int hashCode()
    {
        final int prime = 11;
        int result = 1;
        result = prime * result + key.hashCode();
        result = prime * result + components.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (object == null || getClass() != object.getClass())
        {
            return false;
        }
        final Group other = (Group) object;
        return compareTo(other) == 0;
    }

    @Override
    public String show()
    {
        final StringBuilder text = new StringBuilder();
        text.append('<').append(key).append('>').append('\n');
        for (final Component operation : components)
        {
            text.append("    ").append(operation.show().replace("\n", "\n    ")).append('\n');
        }
        text.append('<').append('/').append(key).append('>');
        return text.toString();
    }
}
