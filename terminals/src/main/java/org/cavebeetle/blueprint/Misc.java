package org.cavebeetle.blueprint;

import java.util.Collections;
import java.util.List;
import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

/**
 * A collection of miscellaneous functions.
 */
public final class Misc
{
    /**
     * Splits the given text into separate lines and removes the first and/or last line if they are empty.
     *
     * @param text
     *     the original text.
     * @return the list of lines.
     */
    public static final List<String> toLines(final String text)
    {
        Preconditions.checkNotNull(text, "Missing 'text'.");
        final List<String> lines = Lists.newArrayList(Splitter.on('\n').split(text));
        final List<String> lines_ = removeFirstEmptyAndLastEmptyLine(lines);
        return removeCommonIndentationAndTrailingWhitespace(lines_);
    }

    /**
     * Returns a new list without the first and last empty line from the original list of lines. A line is considered
     * empty if it is {@code null} or its trimmed counterpart has length 0.
     *
     * @param lines
     *     the original lines.
     * @return a new list without the first and last empty line.
     */
    public static final List<String> removeFirstEmptyAndLastEmptyLine(final List<String> lines)
    {
        Preconditions.checkNotNull(lines, "Missing 'lines'.");
        if (lines.isEmpty())
        {
            return Collections.emptyList();
        }
        final boolean skipFirst = Strings.nullToEmpty(lines.get(0)).trim().isEmpty();
        if (lines.size() == 1)
        {
            return skipFirst ? Collections.<String> emptyList() : Lists.newArrayList(lines);
        }
        final int lineCount = lines.size();
        final int lastLineIndex = lineCount - 1;
        final boolean skipLast = Strings.nullToEmpty(lines.get(lastLineIndex)).trim().isEmpty();
        final int fromIndexInclusive = skipFirst ? 1 : 0;
        final int toIndexExclusive = skipLast ? lineCount - 1 : lineCount;
        return Lists.newArrayList(lines.subList(fromIndexInclusive, toIndexExclusive));
    }

    /**
     * Finds the smallest indentation shared by all given lines. Empty lines are ignored.
     *
     * @param lines
     *     the given lines.
     * @return the smallest indentation shared by all given lines.
     */
    public static final int findSmallestIndentation(final List<String> lines)
    {
        Preconditions.checkNotNull(lines, "Missing 'lines'.");
        if (lines.isEmpty())
        {
            return 0;
        }
        int minimumIndentation = Integer.MAX_VALUE;
        boolean emptyLinesOnly = true;
        for (final String line : lines)
        {
            final String nonNullLine = Strings.nullToEmpty(line);
            final String minimalLine = CharMatcher.WHITESPACE.trimLeadingFrom(nonNullLine);
            if (minimalLine.isEmpty())
            {
                continue;
            }
            emptyLinesOnly = false;
            final int indentation = nonNullLine.length() - minimalLine.length();
            if (indentation < minimumIndentation)
            {
                minimumIndentation = indentation;
            }
        }
        return emptyLinesOnly ? 0 : minimumIndentation;
    }

    /**
     * Returns a new list of lines with the shared, common indentation removed.
     *
     * @param lines
     *     the original lines.
     * @return a new list of lines with the shared, common indentation removed.
     */
    public static final List<String> removeCommonIndentationAndTrailingWhitespace(final List<String> lines)
    {
        Preconditions.checkNotNull(lines, "Missing 'lines'.");
        final int firstNonSpaceIndex = findSmallestIndentation(lines);
        final List<String> result = Lists.newArrayListWithCapacity(lines.size());
        for (final String line : lines)
        {
            final String nonNullLine = Strings.nullToEmpty(line);
            final String trimmedLine = nonNullLine.trim();
            if (trimmedLine.isEmpty())
            {
                result.add(trimmedLine);
            }
            else
            {
                result.add(CharMatcher.WHITESPACE.trimTrailingFrom(nonNullLine.substring(firstNonSpaceIndex)));
            }
        }
        return result;
    }
}
