package org.cavebeetle.blueprint;

import java.io.File;
import java.util.List;
import org.cavebeetle.primes.Primes;
import com.google.common.collect.Lists;

/**
 * The main class to run the terminal and terminal test writers.
 */
public final class Main
{
    /**
     * Creates both the requested terminals and their unit tests. The terminals go to
     * {@code $targetDir/generated-sources/java}, the terminal unit tests go to
     * {@code $targetDir/generated-test-sources/java}.
     *
     * @param args
     *     [0] is the "target" directory;
     *     [1] the package name for the generated code;
     *     [2..] the names of the terminals to generate.
     */
    public static final void main(final String[] args)
    {
        final List<JavaFileInfo> javaFileInfos = Lists.newArrayList();
        final ProjectInfo projectInfo = new ProjectInfo(new File(args[0]));
        final String packageName = args[1];
        final List<String> lines = Lists.newArrayList(args).subList(2, args.length);
        final Primes primes = Primes.Maker.make();
        primes.skip(100);
        for (final String line : lines)
        {
            final JavaFileType javaFileType;
            final String code = line.substring(0, 3);
            if (code.equals("[T]"))
            {
                javaFileType = JavaFileType.TERMINAL;
            }
            else
            {
                throw new IllegalStateException("Unexpected JavaFileType code: '" + code + "'.");
            }
            final String entityName;
            final IndefiniteArticle indefiniteArticle;
            if (line.charAt(4) != '+')
            {
                entityName = line.substring(4);
                indefiniteArticle = IndefiniteArticle.A;
            }
            else
            {
                entityName = line.substring(5);
                indefiniteArticle = IndefiniteArticle.AN;
            }
            javaFileInfos.add(new JavaFileInfo(projectInfo, packageName, javaFileType, entityName, indefiniteArticle, primes.next()));
        }
        int maxLength = 0;
        for (final JavaFileInfo javaFileInfo : javaFileInfos)
        {
            if (javaFileInfo.entityName().length() > maxLength)
            {
                maxLength = javaFileInfo.entityName().length();
            }
        }
        final String terminalMask = String.format("%%-%ds", Integer.valueOf(maxLength + 2));
        for (final JavaFileInfo javaFileInfo : javaFileInfos)
        {
            System.out.print("Generating terminal ");
            System.out.print(String.format(terminalMask, "'" + javaFileInfo.entityName() + "'"));
            System.out.print(" ... ");
            {
                final TerminalWriter terminalWriter = new TerminalWriter(javaFileInfo);
                terminalWriter.create();
            }
            System.out.println("done");
        }
    }
}
