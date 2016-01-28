package org.cavebeetle.maven.vcw.impl;

import org.cavebeetle.maven.vcw.FieldDto;

public final class Field
{
    private final String name;

    public Field(final FieldDto field)
    {
        name = field.name;
    }

    public String name()
    {
        return name;
    }
}
