package org.cavebeetle.blueprint;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;
import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;

/**
 * The &lt;property&gt; element.
 */
@Root
public final class Property
        implements
            Component<Property>,
            Terminal<Property>
{
    /**
     * Represents a missing {@code Property}.
     */
    public static final Property MISSING = new Property();
    @Attribute(name = "key")
    private final String key;
    @Text(required = false)
    private final String value;

    /**
     * Creates a new {@code Property} instance.
     *
     * @param key the key that uniquely identifiers the {@code Property}.
     * @param value the value represented by this {@code Property}.
     */
    public Property(@Attribute(name = "key") final String key, @Text(required = false) final String value)
    {
        Preconditions.checkNotNull(key, "Missing 'key'.");
        Preconditions.checkNotNull(value, "Missing 'value'.");
        this.key = key.trim();
        this.value = value.trim();
    }

    private Property()
    {
        key = "";
        value = "";
    }

    @Override
    public String key()
    {
        return key;
    }

    @Override
    public String value()
    {
        return value;
    }

    @Override
    public int compareTo(final Property other)
    {
        return ComparisonChain
                .start()
                .compare(key, other.key)
                .compare(value, other.value)
                .result();
    }

    @Override
    public int hashCode()
    {
        final int prime = 5;
        int result = 1;
        result = prime * result + key.hashCode();
        result = prime * result + value.hashCode();
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
        final Property other = (Property) object;
        return key.equals(other.key) && value.equals(other.value);
    }

    @Override
    public String show()
    {
        return String.format("<%s>%s</%s>", key, value, key);
    }
}
