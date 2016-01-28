package org.cavebeetle.maven.vcw;

import java.io.File;
import java.util.List;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.cavebeetle.maven.vcw.impl.Entities;
import org.cavebeetle.maven.vcw.impl.EntityWriter;
import com.google.common.collect.Lists;

@Mojo(name = "run", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class EntityWriterMojo
        extends
            AbstractMojo
{
    @Parameter(
        defaultValue = "${project.build.directory}/generated-sources/java",
        property = "vcw.gen-src.main.directory")
    public File generatedSourcesMainJavaDir;
    @Parameter(
        defaultValue = "${project.build.directory}/generated-test-sources/java",
        property = "vcw.gen-src.test.directory")
    public File generatedSourcesTestJavaDir;
    @Parameter
    public String packageName = "org.example";
    @Parameter
    public List<EntityDto> entities = Lists.newArrayList();

    @Override
    public void execute()
            throws MojoExecutionException
    {
        new EntityWriter(new Entities(generatedSourcesMainJavaDir, generatedSourcesTestJavaDir, packageName, entities)).execute();
    }
}
