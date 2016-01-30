package org.cavebeetle.maven.vcw.impl;

import org.cavebeetle.maven.vcw.FieldDto;
import com.squareup.javapoet.ClassName;

public final class Field
{
    private final ClassName type;
    private final String name;

    public Field(final FieldDto field)
    {
        type = ClassName.get(field.type.packageName, field.type.simpleName, field.type.simpleNames);
        name = field.name;
    }

    public ClassName type()
    {
        return type;
    }

    public String name()
    {
        return name;
    }
}
