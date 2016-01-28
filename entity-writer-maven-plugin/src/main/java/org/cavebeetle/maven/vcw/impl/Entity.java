package org.cavebeetle.maven.vcw.impl;

import java.util.Iterator;
import java.util.List;
import org.cavebeetle.maven.vcw.EntityDto;
import org.cavebeetle.maven.vcw.FieldDto;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public final class Entity
        implements
            Iterable<Field>
{
    private final String name;
    private final List<Field> fields;

    public Entity(final EntityDto entity)
    {
        name = entity.name;
        final List<Field> fields_ = Lists.newArrayListWithCapacity(entity.fields.size());
        for (final FieldDto field : entity.fields)
        {
            fields_.add(new Field(field));
        }
        fields = ImmutableList.copyOf(fields_);
    }

    public String name()
    {
        return name;
    }

    public List<Field> fields()
    {
        return fields;
    }

    @Override
    public Iterator<Field> iterator()
    {
        return fields.iterator();
    }
}
