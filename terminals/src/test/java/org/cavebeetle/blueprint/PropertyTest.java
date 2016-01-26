package org.cavebeetle.blueprint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.lang.reflect.Method;
import org.junit.Before;
import org.junit.Test;

/**
 * The unit tests for {@code Property}.
 */
public final class PropertyTest
{
    private Property property;
    private Property propertyWithDifferentKey;
    private Property propertyWithDifferentValue;

    /**
     * Sets up each unit test.
     */
    @Before
    public void setUp()
    {
        property = new Property("KEY", "VALUE");
        propertyWithDifferentKey = new Property("KEY2", "VALUE");
        propertyWithDifferentValue = new Property("KEY", "VALUE2");
    }

    /**
     * Tests that a {@code Property}'s key cannot be {@code null}.
     */
    @Test
    public final void test_that_a_Propertys_key_cannot_be_null()
    {
        try
        {
            new Property(null, "VALUE");
            fail("Expected a NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals("Missing 'key'.", e.getMessage());
        }
    }

    /**
     * Tests that {@code Property.MISSING} is essentially an "empty" instance.
     */
    @Test
    public final void test_that_MISSING_is_essentially_an_empty_instance()
    {
        assertEquals("", Property.MISSING.key());
        assertEquals("", Property.MISSING.value());
    }

    /**
     * Tests that a {@code Property}'s order is based on its fields.
     */
    @Test
    public final void test_that_a_Propertys_order_is_based_on_its_fields()
    {
        final Property propertyLessKey = new Property("JEY", "VALUE");
        assertEquals(-1, propertyLessKey.compareTo(property));
        assertEquals(+1, property.compareTo(propertyLessKey));
        final Property propertyLessValue = new Property("KEY", "UALUE");
        assertEquals(-1, propertyLessValue.compareTo(property));
        assertEquals(+1, property.compareTo(propertyLessValue));
        assertEquals(0, property.compareTo(property));
        assertEquals(0, property.compareTo(new Property("KEY", "VALUE")));
    }

    /**
     * Tests that a {@code Property}'s hash code is based on its fields.
     */
    @Test
    public final void test_that_a_Propertys_hash_code_is_based_on_its_fields()
    {
        assertEquals(property.hashCode(), property.hashCode());
        assertEquals(property.hashCode(), new Property("KEY", "VALUE").hashCode());
        assertNotEquals(property.hashCode(), propertyWithDifferentKey.hashCode());
        assertNotEquals(property.hashCode(), propertyWithDifferentValue.hashCode());
    }

    /**
     * Tests that a {@code Property}'s key is trimmed.
     */
    @Test
    public final void test_that_a_Propertys_key_is_trimmed()
    {
        assertEquals("", new Property(" \t \n \r\n ", "VALUE").key());
    }

    /**
     * Tests that a {@code Property}'s value cannot be {@code null}.
     */
    @Test
    public final void test_that_a_Propertys_value_cannot_be_null()
    {
        try
        {
            new Property("KEY", null);
            fail("Expected a NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals("Missing 'value'.", e.getMessage());
        }
    }

    /**
     * Tests that a {@code Property}'s value is trimmed.
     */
    @Test
    public final void test_that_a_Propertys_value_is_trimmed()
    {
        assertEquals("", new Property("KEY", " \t \n \r\n ").value());
    }

    /**
     * Tests that a {@code Property}'s equality to another is based on its fields.
     */
    @Test
    public final void test_that_a_Propertys_equality_to_another_is_based_on_its_fields()
    {
        assertTrue(property.equals(property));
        assertTrue(property.equals(new Property("KEY", "VALUE")));
        assertTrue(new Property("KEY", "VALUE").equals(property));
        assertTrue(new Property("KEY", "VALUE").equals(new Property(" KEY ", " VALUE ")));
        assertFalse(property.equals(null));
        assertFalse(property.equals(new Object()));
        assertFalse(property.equals(propertyWithDifferentKey));
        assertFalse(property.equals(propertyWithDifferentValue));
    }

    /**
     * Tests that the textual representation of a {@code Property} is correct.
     */
    @Test
    public final void test_that_the_textual_representation_of_a_Property_is_correct()
    {
        assertEquals("<KEY>VALUE</KEY>", property.show());
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
        final Method compareTo = Property.class.getDeclaredMethod("compareTo", Object.class);
        compareTo.invoke(property, property);
    }
}
