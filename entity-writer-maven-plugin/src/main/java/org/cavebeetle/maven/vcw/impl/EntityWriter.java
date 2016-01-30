package org.cavebeetle.maven.vcw.impl;

import java.util.List;
import javax.lang.model.element.Modifier;
import org.cavebeetle.maven.vcw.Misc;
import com.google.common.collect.Lists;
import com.squareup.javapoet.FieldSpec;
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
                    .builder(entities.packageName(), createTerminalType(entity))
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
                .addFields(createFields(entity))
                .build();
    }

    public List<FieldSpec> createFields(final Entity entity)
    {
        final List<FieldSpec> result = Lists.newArrayList();
        for (final Field field : entity)
        {
            result.add(FieldSpec
                    .builder(field.type(), field.name(), Modifier.PRIVATE, Modifier.FINAL)
                    .build());
        }
        return result;
    }
}
