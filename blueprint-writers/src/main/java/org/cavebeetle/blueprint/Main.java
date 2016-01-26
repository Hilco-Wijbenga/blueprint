package org.cavebeetle.blueprint;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.cavebeetle.primes.Primes;
import com.google.common.collect.Lists;
import com.squareup.javapoet.JavaFile;

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
        final File targetDir = new File(args[0]);
        final File srcMainJavaDir = new File(targetDir, "generated-sources/java");
        final File srcTestJavaDir = new File(targetDir, "generated-test-sources/java");
        final String packageName = args[1];
        final List<String> terminals = Lists.newArrayList(args).subList(2, args.length);
        final Primes primes = Primes.Maker.make();
        primes.skip(100);
        int maxLength = 0;
        for (final String terminal_ : terminals)
        {
            final String terminal = terminal_.charAt(0) == '+' ? terminal_.substring(1) : terminal_;
            if (terminal.length() > maxLength)
            {
                maxLength = terminal.length();
            }
        }
        final String terminalMask = String.format("%%-%ds", Integer.valueOf(maxLength + 2));
        final String terminalTestMask = String.format("%%-%ds", Integer.valueOf(maxLength + 6));
        for (final String terminal_ : terminals)
        {
            final String terminal;
            final IndefiniteArticle indefiniteArticle;
            if (terminal_.charAt(0) != '+')
            {
                terminal = terminal_;
                indefiniteArticle = IndefiniteArticle.A;
            }
            else
            {
                terminal = terminal_.substring(1);
                indefiniteArticle = IndefiniteArticle.AN;
            }
            System.out.print("Generating terminal ");
            System.out.print(String.format(terminalMask, "'" + terminal + "'"));
            System.out.print(" ... ");
            {
                final TerminalWriter terminalWriter = new TerminalWriter(primes, packageName, terminal);
                final JavaFile javaFile = terminalWriter.create();
                writeJavaFile(javaFile, srcMainJavaDir);
            }
            System.out.print("terminal test ");
            System.out.print(String.format(terminalTestMask, "'" + terminal + "Test'"));
            System.out.print(" ... ");
            {
                final TerminalTestWriter terminalTestWriter = new TerminalTestWriter(primes, packageName, terminal, indefiniteArticle);
                final JavaFile javaFile = terminalTestWriter.create();
                writeJavaFile(javaFile, srcTestJavaDir);
            }
            System.out.println("done");
        }
    }

    private static final void writeJavaFile(final JavaFile javaFile, final File dir)
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
