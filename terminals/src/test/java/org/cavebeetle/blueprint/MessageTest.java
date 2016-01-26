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
 * The unit tests for {@code Message}.
 */
public final class MessageTest
{
    private String value;
    private Message message;
    private Message messageWithDifferentValue;

    /**
     * Sets up each unit test.
     */
    @Before
    public void setUp()
    {
        value = "    Hello   \n  World\n    ";
        message = new Message(value);
        messageWithDifferentValue = new Message("Hello\nWorld");
    }

    /**
     * Tests that a {@code Message}'s value cannot be {@code null}.
     */
    @Test
    public final void test_that_a_Message_value_cannot_be_NULL()
    {
        try
        {
            new Message(null);
            fail("Expected a NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals("Missing 'value'.", e.getMessage());
        }
    }

    /**
     * Tests that a {@code Message} with an empty value is empty.
     */
    @Test
    public final void test_that_a_Message_with_an_empty_value_is_empty()
    {
        final Message emptyMessage = new Message("   \t   \n\r");
        assertEquals("", emptyMessage.value());
    }

    /**
     * Tests that a {@code Message}'s lines retain their indentation.
     */
    @Test
    public final void test_that_a_Message_lines_retain_their_indentation()
    {
        final Iterator<String> lineIt = message.iterator();
        assertEquals("  Hello", lineIt.next());
        assertEquals("World", lineIt.next());
        assertFalse(lineIt.hasNext());
    }

    /**
     * Tests that a {@code Message}'s hash code is based on its value.
     */
    @Test
    public final void test_that_a_Message_hash_code_is_based_on_its_fields()
    {
        assertEquals(message.hashCode(), message.hashCode());
        assertEquals(message.hashCode(), new Message(value).hashCode());
        assertNotEquals(message.hashCode(), new Message("Hello\nworld").hashCode());
    }

    /**
     * Tests that {@code Message.MISSING} is essentially an "empty" instance.
     */
    @Test
    public final void test_that_MISSING_is_essentially_an_empty_instance()
    {
        assertEquals("", Message.MISSING.value());
        assertFalse(Message.MISSING.iterator().hasNext());
    }

    /**
     * Tests that a {@code Message}'s order is based on its field.
     */
    @Test
    public final void test_that_a_Message_order_is_based_on_its_value()
    {
        final Message messageAbc = new Message("abc");
        final Message messageXyz = new Message("xyz");
        assertTrue(messageAbc.compareTo(messageXyz) < 0);
        assertEquals(0, messageAbc.compareTo(messageAbc));
        assertTrue(messageXyz.compareTo(messageAbc) > 0);
        assertEquals(0, messageXyz.compareTo(messageXyz));
    }

    /**
     * Tests that a {@code Message}'s equality to another is based on its value.
     */
    @Test
    public final void test_that_a_Message_equality_to_another_is_based_on_its_value()
    {
        assertTrue(message.equals(message));
        assertTrue(message.equals(new Message(value)));
        assertTrue(new Message(value).equals(message));
        assertFalse(message.equals(null));
        assertFalse(message.equals(new Object()));
        assertFalse(message.equals(messageWithDifferentValue));
    }

    /**
     * Tests that the textual representation of an empty {@code Message} is correct.
     */
    @Test
    public final void test_that_the_textual_representation_of_an_empty_Message_is_correct()
    {
        assertEquals("<message></message>", Message.MISSING.show());
    }

    /**
     * Tests that the textual representation of a {@code Message} is correct.
     */
    @Test
    public final void test_that_the_textual_representation_of_a_Message_is_correct()
    {
        assertEquals("<message>\n      Hello\n    World\n</message>", message.show());
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
        final Method compareTo = Message.class.getDeclaredMethod("compareTo", Object.class);
        compareTo.invoke(message, message);
    }
}
