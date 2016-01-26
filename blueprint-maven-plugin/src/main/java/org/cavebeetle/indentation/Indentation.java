package org.cavebeetle.indentation;

import org.cavebeetle.text.Show;

public interface Indentation
        extends
            Show
{
    public interface Maker
    {
        Indentation makeIndentation(IndentationType type);
    }

    Indentation indent();

    Indentation dedent();
}
