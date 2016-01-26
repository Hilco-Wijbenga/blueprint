package org.cavebeetle.blueprint;

import java.io.File;

public final class ProjectInfo
{
    private final File targetDir;
    private final File srcMainJavaDir;
    private final File srcTestJavaDir;

    public ProjectInfo(final File targetDir)
    {
        this.targetDir = targetDir;
        srcMainJavaDir = new File(targetDir, "generated-sources/java");
        srcTestJavaDir = new File(targetDir, "generated-test-sources/java");
    }

    public File targetDir()
    {
        return targetDir;
    }

    public File srcMainJavaDir()
    {
        return srcMainJavaDir;
    }

    public File srcTestJavaDir()
    {
        return srcTestJavaDir;
    }
}