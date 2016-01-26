package org.cavebeetle.blueprint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.lang.reflect.Method;
import java.util.Iterator;
import org.junit.Before;
import org.junit.Test;

/**
 * The unit tests for {@code Comments}.
 */
public final class CommentsTest
{
    private String value;
    private Comments comments;
    private Comments commentsWithDifferentValue;

    /**
     * Sets up each unit test.
     */
    @Before
    public void setUp()
    {
        value = "    Hello   \n  World\n    ";
        comments = new Comments(value);
        commentsWithDifferentValue = new Comments("Hello\nWorld");
    }

    /**
     * Tests that a {@code Comments}'s value cannot be {@code null}.
     */
    @Test
    public final void test_that_a_Comments_value_cannot_be_NULL()
    {
        try
        {
            new Comments(null);
            fail("Expected a NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals("Missing 'value'.", e.getMessage());
        }
    }

    /**
     * Tests that a {@code Comments} with an empty value is empty.
     */
    @Test
    public final void test_that_a_Comments_with_an_empty_value_is_empty()
    {
        final Comments emptyComments = new Comments("   \t   \n\r");
        assertEquals("", emptyComments.value());
    }

    /**
     * Tests that a {@code Comments}'s lines retain their indentation.
     */
    @Test
    public final void test_that_a_Comments_lines_retain_their_indentation()
    {
        final Iterator<String> lineIt = comments.iterator();
        assertEquals("  Hello", lineIt.next());
        assertEquals("World", lineIt.next());
        assertFalse(lineIt.hasNext());
    }

    /**
     * Tests that a {@code Comments}'s hash code is based on its value.
     */
    @Test
    public final void test_that_a_Comments_hash_code_is_based_on_its_fields()
    {
        assertEquals(comments.hashCode(), comments.hashCode());
        assertEquals(comments.hashCode(), new Comments(value).hashCode());
        assertNotEquals(comments.hashCode(), new Comments("Hello\nworld").hashCode());
    }

    /**
     * Tests that {@code Comments.MISSING} is essentially an "empty" instance.
     */
    @Test
    public final void test_that_MISSING_is_essentially_an_empty_instance()
    {
        assertEquals("", Comments.MISSING.value());
        assertFalse(Comments.MISSING.iterator().hasNext());
    }

    /**
     * Tests that a {@code Comments}'s order is based on its field.
     */
    @Test
    public final void test_that_a_Comments_order_is_based_on_its_value()
    {
        final Comments commentsAbc = new Comments("abc");
        final Comments commentsXyz = new Comments("xyz");
        assertTrue(commentsAbc.compareTo(commentsXyz) < 0);
        assertEquals(0, commentsAbc.compareTo(commentsAbc));
        assertTrue(commentsXyz.compareTo(commentsAbc) > 0);
        assertEquals(0, commentsXyz.compareTo(commentsXyz));
    }

    /**
     * Tests that a {@code Comments}'s equality to another is based on its value.
     */
    @Test
    public final void test_that_a_Comments_equality_to_another_is_based_on_its_value()
    {
        assertTrue(comments.equals(comments));
        assertTrue(comments.equals(new Comments(value)));
        assertTrue(new Comments(value).equals(comments));
        assertFalse(comments.equals(null));
        assertFalse(comments.equals(new Object()));
        assertFalse(comments.equals(commentsWithDifferentValue));
    }

    /**
     * Tests that the textual representation of an empty {@code Comments} is correct.
     */
    @Test
    public final void test_that_the_textual_representation_of_an_empty_Comments_is_correct()
    {
        assertEquals("<comments></comments>", Comments.MISSING.show());
    }

    /**
     * Tests that the textual representation of a {@code Comments} is correct.
     */
    @Test
    public final void test_that_the_textual_representation_of_a_Comments_is_correct()
    {
        assertEquals("<comments>\n      Hello\n    World\n</comments>", comments.show());
    }

    /**
     * Excecutes the bridge method that is generated for {@code Comparable}. This is not a test, it simply completes the
     * coverage.
     *
     * @throws Exception if anything unexpected happens.
     */
    @Test
    public final void executeBridgeMethod() throws Exception
    {
        final Method compareTo = Comments.class.getDeclaredMethod("compareTo", Object.class);
        compareTo.invoke(comments, comments);
    }
}
