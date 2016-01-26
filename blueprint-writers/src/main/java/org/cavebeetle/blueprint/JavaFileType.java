package org.cavebeetle.blueprint;

public enum JavaFileType
{
    TERMINAL("terminal"),
    //
    ;
    private final String text;

    private JavaFileType(final String text)
    {
        this.text = text;
    }
}
