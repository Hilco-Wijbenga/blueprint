package org.cavebeetle.indentation.impl;

import org.cavebeetle.indentation.Indentation;
import org.cavebeetle.indentation.IndentationType;

public final class DefaultIndentation
        implements
            Indentation
{
    private final Indentation previous;
    private final IndentationType type;
    private final String indentation;

    public DefaultIndentation(final IndentationType type)
    {
        previous = this;
        this.type = type;
        indentation = "";
    }

    public DefaultIndentation(final DefaultIndentation previous)
    {
        this.previous = previous;
        type = previous.type;
        indentation = previous.show() + type.show();
    }

    @Override
    public Indentation indent()
    {
        return new DefaultIndentation(this);
    }

    @Override
    public Indentation dedent()
    {
        return previous;
    }

    @Override
    public String show()
    {
        return indentation;
    }
}
