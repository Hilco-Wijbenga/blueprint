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
 * The unit tests for {@code Description}.
 */
public final class DescriptionTest
{
    private String value;
    private Description description;
    private Description descriptionWithDifferentValue;

    /**
     * Sets up each unit test.
     */
    @Before
    public void setUp()
    {
        value = "    Hello   \n  World\n    ";
        description = new Description(value);
        descriptionWithDifferentValue = new Description("Hello\nWorld");
    }

    /**
     * Tests that a {@code Description}'s value cannot be {@code null}.
     */
    @Test
    public final void test_that_a_Description_value_cannot_be_NULL()
    {
        try
        {
            new Description(null);
            fail("Expected a NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals("Missing 'value'.", e.getMessage());
        }
    }

    /**
     * Tests that a {@code Description} with an empty value is empty.
     */
    @Test
    public final void test_that_a_Description_with_an_empty_value_is_empty()
    {
        final Description emptyDescription = new Description("   \t   \n\r");
        assertEquals("", emptyDescription.value());
    }

    /**
     * Tests that a {@code Description}'s lines retain their indentation.
     */
    @Test
    public final void test_that_a_Description_lines_retain_their_indentation()
    {
        final Iterator<String> lineIt = description.iterator();
        assertEquals("  Hello", lineIt.next());
        assertEquals("World", lineIt.next());
        assertFalse(lineIt.hasNext());
    }

    /**
     * Tests that a {@code Description}'s hash code is based on its value.
     */
    @Test
    public final void test_that_a_Description_hash_code_is_based_on_its_fields()
    {
        assertEquals(description.hashCode(), description.hashCode());
        assertEquals(description.hashCode(), new Description(value).hashCode());
        assertNotEquals(description.hashCode(), new Description("Hello\nworld").hashCode());
    }

    /**
     * Tests that {@code Description.MISSING} is essentially an "empty" instance.
     */
    @Test
    public final void test_that_MISSING_is_essentially_an_empty_instance()
    {
        assertEquals("", Description.MISSING.value());
        assertFalse(Description.MISSING.iterator().hasNext());
    }

    /**
     * Tests that a {@code Description}'s order is based on its field.
     */
    @Test
    public final void test_that_a_Description_order_is_based_on_its_value()
    {
        final Description descriptionAbc = new Description("abc");
        final Description descriptionXyz = new Description("xyz");
        assertTrue(descriptionAbc.compareTo(descriptionXyz) < 0);
        assertEquals(0, descriptionAbc.compareTo(descriptionAbc));
        assertTrue(descriptionXyz.compareTo(descriptionAbc) > 0);
        assertEquals(0, descriptionXyz.compareTo(descriptionXyz));
    }

    /**
     * Tests that a {@code Description}'s equality to another is based on its value.
     */
    @Test
    public final void test_that_a_Description_equality_to_another_is_based_on_its_value()
    {
        assertTrue(description.equals(description));
        assertTrue(description.equals(new Description(value)));
        assertTrue(new Description(value).equals(description));
        assertFalse(description.equals(null));
        assertFalse(description.equals(new Object()));
        assertFalse(description.equals(descriptionWithDifferentValue));
    }

    /**
     * Tests that the textual representation of an empty {@code Description} is correct.
     */
    @Test
    public final void test_that_the_textual_representation_of_an_empty_Description_is_correct()
    {
        assertEquals("<description></description>", Description.MISSING.show());
    }

    /**
     * Tests that the textual representation of a {@code Description} is correct.
     */
    @Test
    public final void test_that_the_textual_representation_of_a_Description_is_correct()
    {
        assertEquals("<description>\n      Hello\n    World\n</description>", description.show());
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
        final Method compareTo = Description.class.getDeclaredMethod("compareTo", Object.class);
        compareTo.invoke(description, description);
    }
}
