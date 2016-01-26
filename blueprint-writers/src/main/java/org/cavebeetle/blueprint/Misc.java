package org.cavebeetle.blueprint;

import java.io.File;
import java.io.IOException;
import com.squareup.javapoet.JavaFile;

public final class Misc
{
    public static final void writeJavaFile(final JavaFile javaFile, final File dir)
    {
        try
        {
            javaFile.writeTo(dir);
        }
        catch (final IOException e)
        {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
