package org.cavebeetle.maven.vcw.impl;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import org.cavebeetle.maven.vcw.EntityDto;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public final class Entities
        implements
            Iterable<Entity>
{
    private final File generatedSourcesMainJavaDir;
    private final File generatedSourcesTestJavaDir;
    private final String packageName;
    private final List<Entity> entities;

    public Entities(
            final File generatedSourcesMainJavaDir,
            final File generatedSourcesTestJavaDir,
            final String packageName,
            final List<EntityDto> entityDtos)
    {
        this.generatedSourcesMainJavaDir = generatedSourcesMainJavaDir;
        this.generatedSourcesTestJavaDir = generatedSourcesTestJavaDir;
        this.packageName = packageName;
        final List<Entity> entities = Lists.newArrayListWithCapacity(entityDtos.size());
        for (final EntityDto entity_ : entityDtos)
        {
            final Entity entity = new Entity(entity_);
            entities.add(entity);
        }
        this.entities = ImmutableList.copyOf(entities);
    }

    public File generatedSourcesMainJavaDir()
    {
        return generatedSourcesMainJavaDir;
    }

    public File generatedSourcesTestJavaDir()
    {
        return generatedSourcesTestJavaDir;
    }

    public String packageName()
    {
        return packageName;
    }

    @Override
    public Iterator<Entity> iterator()
    {
        return entities.iterator();
    }
}
