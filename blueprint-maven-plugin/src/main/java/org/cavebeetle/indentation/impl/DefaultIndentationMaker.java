package org.cavebeetle.indentation.impl;

import org.cavebeetle.indentation.Indentation;
import org.cavebeetle.indentation.IndentationType;

public final class DefaultIndentationMaker
        implements
            Indentation.Maker
{
    private final Indentation INDENT_WITH_4_SPACES = new DefaultIndentation(IndentationType.USE_4_SPACES);
    private final Indentation INDENT_WITH_2_SPACES = new DefaultIndentation(IndentationType.USE_2_SPACES);
    private final Indentation INDENT_WITH_TABS = new DefaultIndentation(IndentationType.USE_TABS);

    @Override
    public Indentation makeIndentation(final IndentationType type)
    {
        switch (type)
        {
            case USE_4_SPACES:
                return INDENT_WITH_4_SPACES;
            case USE_2_SPACES:
                return INDENT_WITH_2_SPACES;
            case USE_TABS:
                return INDENT_WITH_TABS;
            default:
                throw new IllegalStateException("This should never happen.");
        }
    }
}
