package org.cavebeetle.blueprint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import com.google.common.collect.Lists;

/**
 * The unit tests for {@code Misc}.
 */
public final class MiscTest
{
    // Misc#toLines
    /**
     * Tests that {@code Misc#toLines} works.
     */
    @Test
    public final void test_that_toLines_works()
    {
        final String text = "  \n    Hello\n  World\n";
        final List<String> result = Misc.toLines(text);
        assertEquals(2, result.size());
        assertEquals("  Hello", result.get(0));
        assertEquals("World", result.get(1));
    }

    // Misc#removeFirstEmptyAndLastEmptyLine
    /**
     * Tests that Misc#removeFirstEmptyAndLastEmptyLine handles {@code null} correctly.
     */
    @Test
    public final void test_that_removeFirstEmptyAndLastEmptyLine_handles_NULL_correctly()
    {
        try
        {
            Misc.removeFirstEmptyAndLastEmptyLine(null);
            fail("Expected a NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals("Missing 'lines'.", e.getMessage());
        }
    }

    /**
     * Tests that removing the first line of an empty list does nothing.
     */
    @Test
    public final void test_that_removing_the_first_line_of_an_empty_list_does_nothing()
    {
        final List<String> list = Collections.<String> emptyList();
        final List<String> result = Misc.removeFirstEmptyAndLastEmptyLine(list);
        assertTrue(result.isEmpty());
    }

    /**
     * Tests that removing the first line of a singleton list leaves an empty list.
     */
    @Test
    public final void test_that_removing_the_first_line_of_a_singleton_list_leaves_an_empty_list()
    {
        final List<String> list = Lists.newArrayList("");
        final List<String> result = Misc.removeFirstEmptyAndLastEmptyLine(list);
        assertTrue(result.isEmpty());
    }

    /**
     * Tests that removing the first non empty line of a singleton list does nothing.
     */
    @Test
    public final void test_that_removing_the_non_empty_first_line_of_a_singleton_list_does_nothing()
    {
        final List<String> list = Lists.newArrayList("hello");
        final List<String> result = Misc.removeFirstEmptyAndLastEmptyLine(list);
        assertEquals(1, result.size());
        assertSame(list.get(0), result.get(0));
    }

    /**
     * Tests that removing the first line works if it is a {@code null}.
     */
    @Test
    public final void test_that_removing_the_first_line_works_if_it_is_a_NULL()
    {
        final List<String> list = Lists.newArrayList(null, "Hello", "World");
        final List<String> result = Misc.removeFirstEmptyAndLastEmptyLine(list);
        assertEquals(2, result.size());
        assertEquals("Hello", result.get(0));
        assertEquals("World", result.get(1));
    }

    /**
     * Tests that removing the first line works if it is a empty.
     */
    @Test
    public final void test_that_removing_the_first_line_works_if_it_is_empty()
    {
        final List<String> list = Lists.newArrayList("", "Hello", "World");
        final List<String> result = Misc.removeFirstEmptyAndLastEmptyLine(list);
        assertEquals(2, result.size());
        assertEquals("Hello", result.get(0));
        assertEquals("World", result.get(1));
    }

    /**
     * Tests that removing the first line works if it is whitespace only.
     */
    @Test
    public final void test_that_removing_the_first_line_works_if_it_is_whitespace_only()
    {
        final List<String> list = Lists.newArrayList(" \t \f \n \r\n", "Hello", "World");
        final List<String> result = Misc.removeFirstEmptyAndLastEmptyLine(list);
        assertEquals(2, result.size());
        assertEquals("Hello", result.get(0));
        assertEquals("World", result.get(1));
    }

    /**
     * Tests that removing the first line does nothing if it is not empty.
     */
    @Test
    public final void test_that_removing_the_first_line_does_nothing_if_it_is_not_empty()
    {
        final List<String> list = Lists.newArrayList("Hello", "World");
        final List<String> result = Misc.removeFirstEmptyAndLastEmptyLine(list);
        assertEquals(2, result.size());
        assertEquals("Hello", result.get(0));
        assertEquals("World", result.get(1));
    }

    /**
     * Tests that removing the last line works if it is a {@code null}.
     */
    @Test
    public final void test_that_removing_the_last_line_works_if_it_is_a_NULL()
    {
        final List<String> list = Lists.newArrayList("Hello", "World", null);
        final List<String> result = Misc.removeFirstEmptyAndLastEmptyLine(list);
        assertEquals(2, result.size());
        assertEquals("Hello", result.get(0));
        assertEquals("World", result.get(1));
    }

    /**
     * Tests that removing the last line works if it is a empty.
     */
    @Test
    public final void test_that_removing_the_last_line_works_if_it_is_empty()
    {
        final List<String> list = Lists.newArrayList("Hello", "World", "");
        final List<String> result = Misc.removeFirstEmptyAndLastEmptyLine(list);
        assertEquals(2, result.size());
        assertEquals("Hello", result.get(0));
        assertEquals("World", result.get(1));
    }

    /**
     * Tests that removing the last line works if it is whitespace only.
     */
    @Test
    public final void test_that_removing_the_last_line_works_if_it_is_whitespace_only()
    {
        final List<String> list = Lists.newArrayList("Hello", "World", " \t \f \n \f\n");
        final List<String> result = Misc.removeFirstEmptyAndLastEmptyLine(list);
        assertEquals(2, result.size());
        assertEquals("Hello", result.get(0));
        assertEquals("World", result.get(1));
    }

    // Misc#findSmallestIndentation
    /**
     * Tests that Misc#findSmallestIndentation handles {@code null} correctly.
     */
    @Test
    public final void test_that_findSmallestIndentation_handles_NULL_correctly()
    {
        try
        {
            Misc.findSmallestIndentation(null);
            fail("Expected a NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals("Missing 'lines'.", e.getMessage());
        }
    }

    /**
     * Tests that finding the smallest indentation in an empty list returns {@code 0}.
     */
    @Test
    public final void test_that_finding_the_smallest_indentation_in_an_empty_list_returns_0()
    {
        final List<String> emptyList = Collections.<String> emptyList();
        assertEquals(0, Misc.findSmallestIndentation(emptyList));
    }

    /**
     * Tests that finding the smallest indentation in a singleton list with an empty line works (zero).
     */
    @Test
    public final void test_that_finding_the_smallest_indentation_in_a_list_ignores_empty_lines()
    {
        final List<String> list = Lists.newArrayList(
                "",
                "\t",
                "    ");
        assertEquals(0, Misc.findSmallestIndentation(list));
    }

    /**
     * Tests that finding the smallest indentation in a singleton list works (zero).
     */
    @Test
    public final void test_that_finding_the_smallest_indentation_in_a_singleton_list_works_zero()
    {
        final List<String> list = Lists.newArrayList("Hello world!");
        assertEquals(0, Misc.findSmallestIndentation(list));
    }

    /**
     * Tests that finding the smallest indentation in a singleton list works (four).
     */
    @Test
    public final void test_that_finding_the_smallest_indentation_in_a_singleton_list_works_four()
    {
        final List<String> list = Lists.newArrayList("    Hello world!");
        assertEquals(4, Misc.findSmallestIndentation(list));
    }

    /**
     * Tests that finding the smallest indentation in a list works (zero).
     */
    @Test
    public final void test_that_finding_the_smallest_indentation_in_a_list_works_zero()
    {
        final List<String> list = Lists.newArrayList(
                "Hello",
                "world!");
        assertEquals(0, Misc.findSmallestIndentation(list));
    }

    /**
     * Tests that finding the smallest indentation in a list works (largest first).
     */
    @Test
    public final void test_that_finding_the_smallest_indentation_in_a_list_works_largest_first()
    {
        final List<String> list = Lists.newArrayList(
                "    Hello ",
                "  world!");
        assertEquals(2, Misc.findSmallestIndentation(list));
    }

    /**
     * Tests that finding the smallest indentation in a list works (smallest first).
     */
    @Test
    public final void test_that_finding_the_smallest_indentation_in_a_list_works_smallest_first()
    {
        final List<String> list = Lists.newArrayList(
                "  Hello ",
                "    world!");
        assertEquals(2, Misc.findSmallestIndentation(list));
    }

    // Misc#removeCommonIndentationAndTrailingWhitespace
    /**
     * Tests that {@code Misc#removeCommonIndentationAndTrailingWhitespace} handles {@code null} correctly.
     */
    @Test
    public final void test_that_removeCommonIndentationAndTrailingWhitespace_handles_NULL_correctly()
    {
        try
        {
            Misc.removeCommonIndentationAndTrailingWhitespace(null);
            fail("Expected a NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals("Missing 'lines'.", e.getMessage());
        }
    }

    /**
     * Tests that removing common indentation from an empty list does not change the list.
     */
    @Test
    public final void test_that_removing_common_indentation_from_an_empty_list_does_not_change_the_list()
    {
        final List<String> lines = Collections.<String> emptyList();
        Misc.removeCommonIndentationAndTrailingWhitespace(lines);
        assertTrue(lines.isEmpty());
    }

    /**
     * Tests that removing common indentation works.
     */
    @Test
    public final void test_that_removing_common_indentation_works()
    {
        final List<String> lines = Lists.newArrayList(
                "    Hello",
                "  World",
                "",
                "  !!!");
        final List<String> result = Misc.removeCommonIndentationAndTrailingWhitespace(lines);
        assertEquals(4, result.size());
        assertEquals("  Hello", result.get(0));
        assertEquals("World", result.get(1));
        assertEquals("", result.get(2));
        assertEquals("!!!", result.get(3));
    }

    // Misc#constructor
    /**
     * Calls the default constructor to satisfy code coverage.
     */
    @Test
    public final void includeDefaultConstructor()
    {
        new Misc();
    }
}
