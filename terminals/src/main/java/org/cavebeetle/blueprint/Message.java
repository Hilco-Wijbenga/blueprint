package org.cavebeetle.blueprint;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;
import com.google.common.base.Preconditions;

/**
 * The &lt;message&gt; element.
 */
@Root
public final class Message
        implements
            Iterable<String>,
            Terminal<Message>
{
    /**
     * Represents a missing {@code Message}.
     */
    public static final Message MISSING = new Message();
    @Text(required = false)
    private final String value;
    private final List<String> lines;

    /**
     * Creates a new {@code Message} instance.
     *
     * @param value the value representing the actual message.
     */
    public Message(@Text(required = false) final String value)
    {
        Preconditions.checkNotNull(value, "Missing 'value'.");
        lines = Misc.removeCommonIndentationAndTrailingWhitespace(Misc.toLines(value));
        if (lines.isEmpty())
        {
            this.value = "";
        }
        else
        {
            final StringBuilder sb = new StringBuilder();
            final Iterator<String> lineIt = lines.iterator();
            while (true)
            {
                final String line = lineIt.next();
                sb.append(line);
                if (!lineIt.hasNext())
                {
                    break;
                }
                sb.append('\n');
            }
            this.value = sb.toString();
        }
    }

    private Message()
    {
        value = "";
        lines = Collections.emptyList();
    }

    @Override
    public String value()
    {
        return value;
    }

    @Override
    public Iterator<String> iterator()
    {
        return lines.iterator();
    }

    @Override
    public int compareTo(final Message other)
    {
        return value.compareTo(other.value);
    }

    @Override
    public int hashCode()
    {
        final int prime = 2;
        return prime + value.hashCode();
    }

    @Override
    public boolean equals(final Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (object == null || getClass() != object.getClass())
        {
            return false;
        }
        final Message other = (Message) object;
        return value.equals(other.value);
    }

    @Override
    public String show()
    {
        final StringBuilder text = new StringBuilder();
        text.append("<message>");
        if (!value.isEmpty())
        {
            for (final String line : lines)
            {
                text.append("\n    ").append(line);
            }
            text.append('\n');
        }
        text.append("</message>");
        return text.toString();
    }
}
