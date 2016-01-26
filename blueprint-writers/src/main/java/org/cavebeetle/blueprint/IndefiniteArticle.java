package org.cavebeetle.blueprint;

import org.cavebeetle.text.Show;

/**
 * Represents either "a" or "an".
 */
public enum IndefiniteArticle implements
        Show
{
    /** The indefinite article "a". */
    A("a"),
    /** The indefinite article "an". */
    AN("an")
    //
    ;
    private final String text;

    private IndefiniteArticle(final String text)
    {
        this.text = text;
    }

    @Override
    public String show()
    {
        return text;
    }
}
