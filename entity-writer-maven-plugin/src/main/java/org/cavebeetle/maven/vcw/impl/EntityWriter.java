package org.cavebeetle.maven.vcw.impl;

import javax.lang.model.element.Modifier;
import org.cavebeetle.maven.vcw.Misc;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

public final class EntityWriter
{
    private final Entities entities;

    public EntityWriter(final Entities entities)
    {
        this.entities = entities;
    }

    public void execute()
    {
        for (final Entity entity : entities)
        {
            final JavaFile javaFile = JavaFile
                    .builder("org.cavebeetle", createTerminalType(entity))
                    .indent("    ")
                    .skipJavaLangImports(true)
                    .build();
            Misc.writeJavaFile(javaFile, entities.generatedSourcesMainJavaDir());
        }
    }

    public TypeSpec createTerminalType(final Entity entity)
    {
        return TypeSpec
                .classBuilder(entity.name())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .build();
    }
}
