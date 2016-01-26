package org.cavebeetle.indentation;

import org.cavebeetle.text.Show;

public enum IndentationType
        implements
            Show
{
    USE_4_SPACES("    "),
    USE_2_SPACES("  "),
    USE_TABS("\t");
    private final String indentationStep;

    private IndentationType(final String indentationStep)
    {
        this.indentationStep = indentationStep;
    }

    @Override
    public String show()
    {
        return indentationStep;
    }
}
