package org.cavebeetle.blueprint;

import java.util.Collections;
import java.util.List;
import org.cavebeetle.text.Show;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.Root;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

@Root
public final class Configuration
        implements
            Comparable<Configuration>,
            Show
{
    public static final Configuration MISSING = new Configuration();

    @ElementListUnion({
        @ElementList(entry = "property", inline = true, type = Property.class, required = false),
        @ElementList(entry = "group", inline = true, type = Group.class, required = false)
    })
    private final List<Component> components;

    public Configuration()
    {
        components = Collections.emptyList();
    }

    public Configuration(
            @ElementListUnion({
                @ElementList(entry = "property", inline = true, type = Property.class, required = false),
                @ElementList(entry = "group", inline = true, type = Group.class, required = false)
            }) final List<Component> components)
    {
        this.components = components;
    }

    @Override
    public int compareTo(final Configuration other)
    {
        return ComparisonChain
                .start()
                .compare(components, other.components, Ordering.<Component> natural().lexicographical())
                .result();
    }

    @Override
    public int hashCode()
    {
        final int prime = 7;
        return prime + components.hashCode();
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
        final Configuration other = (Configuration) object;
        return compareTo(other) == 0;
    }

    @Override
    public String show()
    {
        final StringBuilder text = new StringBuilder();
        text.append("<configuration>").append('\n');
        for (final Component component : components)
        {
            text.append("    ").append(component.show().replace("\n", "\n    ")).append('\n');
        }
        text.append("</configuration>");
        return text.toString();
    }
}
