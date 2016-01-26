package org.cavebeetle.blueprint;

public final class JavaFileInfo
{
    private final ProjectInfo projectInfo;
    private final String packageName;
    private final JavaFileType javaFileType;
    private final String entityName;
    private final IndefiniteArticle indefiniteArticle;
    private final Integer prime;

    public JavaFileInfo(final ProjectInfo projectInfo,
            final String packageName,
            final JavaFileType javaFileType,
            final String entityName,
            final IndefiniteArticle indefiniteArticle,
            final Integer prime)
    {
        this.projectInfo = projectInfo;
        this.packageName = packageName;
        this.javaFileType = javaFileType;
        this.entityName = entityName;
        this.indefiniteArticle = indefiniteArticle;
        this.prime = prime;
    }

    public ProjectInfo projectInfo()
    {
        return projectInfo;
    }

    public String packageName()
    {
        return packageName;
    }

    public String entityName()
    {
        return entityName;
    }

    public IndefiniteArticle indefiniteArticle()
    {
        return indefiniteArticle;
    }

    public Integer prime()
    {
        return prime;
    }
}