package org.cavebeetle.maven.vcw.impl;

import org.cavebeetle.maven.vcw.FieldDto;

public final class Field
{
    private final String type;
    private final String name;

    public Field(final FieldDto field)
    {
        type = field.type;
        name = field.name;
    }

    public String type()
    {
        return type;
    }

    public String name()
    {
        return name;
    }
}
