package org.cavebeetle.blueprint;

import java.io.Reader;
import java.io.StringReader;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public final class Main
{
    public static void main(final String[] args) throws Exception
    {
        final String xmlText1 = //@formatter:off
                "<parent>\n" +
                "    <artifactId>ARTIFACT-ID</artifactId>\n" +
                "    <groupId>GROUP-ID</groupId>\n" +
                "</parent>\n" //@formatter:on
                ;
        final String xmlText2 = //@formatter:off
                "    <exclusion>\n" +
                "        <groupId>GROUP-ID</groupId>\n" +
                "        <artifactId>ARTIFACT-ID</artifactId>\n" +
                "    </exclusion>" //@formatter:on
                ;
        final String xmlText3 = //@formatter:off
                "<exclusions>\n" +
                "</exclusions>\n" //@formatter:on
                ;
        final String xmlText4 = //@formatter:off
                "<exclusions>\n" +
                "    <exclusion>\n" +
                "        <groupId>GROUP-ID</groupId>\n" +
                "        <artifactId>ARTIFACT-ID</artifactId>\n" +
                "    </exclusion>\n" +
                "</exclusions>\n" //@formatter:on
                ;
        final String xmlText5 = //@formatter:off
                "<exclusions>\n" +
                "    <exclusion>\n" +
                "    </exclusion>\n" +
                "    <exclusion>\n" +
                "        <groupId>GROUP-ID-TWO</groupId>\n" +
                "    </exclusion>\n" +
                "    <exclusion>\n" +
                "        <artifactId>ARTIFACT-ID-ONE</artifactId>\n" +
                "    </exclusion>\n" +
                "    <exclusion>\n" +
                "        <groupId>GROUP-ID-ONE</groupId>\n" +
                "        <artifactId>ARTIFACT-ID-ONE</artifactId>\n" +
                "    </exclusion>\n" +
                "</exclusions>\n" //@formatter:on
                ;
        final String xmlText6 = //@formatter:off
                "<dependencies>\n" +
                "</dependencies>\n" //@formatter:on
                ;
        final String xmlText7 = //@formatter:off
                "<dependencies>\n" +
                "    <dependency>\n" +
                "        <groupId>GROUP-ID-A</groupId>\n" +
                "        <artifactId>ARTIFACT-ID-A</artifactId>\n" +
                "        <exclusions>\n" +
                "            <exclusion>\n" +
                "                <groupId>GROUP-ID-ONE</groupId>\n" +
                "                <artifactId>ARTIFACT-ID-ONE</artifactId>\n" +
                "            </exclusion>\n" +
                "            <exclusion>\n" +
                "                <groupId>GROUP-ID-TWO</groupId>\n" +
                "                <artifactId>ARTIFACT-ID-TWO</artifactId>\n" +
                "            </exclusion>\n" +
                "        </exclusions>\n" +
                "    </dependency>\n" +
                "</dependencies>\n" //@formatter:on
                ;
        final String xmlText8 = //@formatter:off
                "<dependencies>\n" +
                "    <dependency>\n" +
                "        <groupId>GROUP-ID-A</groupId>\n" +
                "        <artifactId>ARTIFACT-ID-A</artifactId>\n" +
                "        <exclusions>\n" +
                "            <exclusion>\n" +
                "                <groupId>GROUP-ID-ONE</groupId>\n" +
                "                <artifactId>ARTIFACT-ID-ONE</artifactId>\n" +
                "            </exclusion>\n" +
                "            <exclusion>\n" +
                "                <groupId>GROUP-ID-TWO</groupId>\n" +
                "                <artifactId>ARTIFACT-ID-TWO</artifactId>\n" +
                "            </exclusion>\n" +
                "        </exclusions>\n" +
                "    </dependency>\n" +
                "    <dependency>\n" +
                "        <groupId>GROUP-ID-B</groupId>\n" +
                "        <artifactId>ARTIFACT-ID-B</artifactId>\n" +
                "        <exclusions>\n" +
                "            <exclusion>\n" +
                "                <groupId>GROUP-ID-THREE</groupId>\n" +
                "                <artifactId>ARTIFACT-ID-THREE</artifactId>\n" +
                "            </exclusion>\n" +
                "            <exclusion>\n" +
                "                <groupId>GROUP-ID-FOUR</groupId>\n" +
                "                <artifactId>ARTIFACT-ID-FOUR</artifactId>\n" +
                "            </exclusion>\n" +
                "        </exclusions>\n" +
                "    </dependency>\n" +
                "</dependencies>\n" //@formatter:on
                ;
        final String xmlText9 = //@formatter:off
                "<project>\n" +
                "    <parent>\n" +
                "        <artifactId>ARTIFACT-ID</artifactId>\n" +
                "        <groupId>GROUP-ID</groupId>\n" +
                "    </parent>\n" +
                "    <description>     \t  \t</description>\n" +
                "</project>\n" //@formatter:on
                ;
        final String xmlText9a = //@formatter:off
                "<project>\n" +
                "    <parent>\n" +
                "        <artifactId>ARTIFACT-ID</artifactId>\n" +
                "        <groupId>GROUP-ID</groupId>\n" +
                "    </parent>\n" +
                "    <description>\n" +
                "DESCRIPTION</description>\n" +
                "</project>\n" //@formatter:on
                ;
        final String xmlText9b = //@formatter:off
                "<project>\n" +
                "    <parent>\n" +
                "        <artifactId>ARTIFACT-ID</artifactId>\n" +
                "        <groupId>GROUP-ID</groupId>\n" +
                "    </parent>\n" +
                "    <description>DESCRIPTION\n" +
                "</description>\n" +
                "</project>\n" //@formatter:on
                ;
        final String xmlText9c = //@formatter:off
                "<project>\n" +
                "    <parent>\n" +
                "        <artifactId>ARTIFACT-ID</artifactId>\n" +
                "        <groupId>GROUP-ID</groupId>\n" +
                "    </parent>\n" +
                "<description>\n" +
                "DESCRIPTION\n" +
                "</description>\n" +
                "</project>\n" //@formatter:on
                ;
        final String xmlText10 = //@formatter:off
                "<project>\n" +
                "    <parent>\n" +
                "        <artifactId>ARTIFACT-ID</artifactId>\n" +
                "        <groupId>GROUP-ID</groupId>\n" +
                "    </parent>\n" +
                "    <description>\n" +
                "        DESCRIPTION\n" +
                "    </description>\n" +
                "</project>\n" //@formatter:on
                ;
        final String xmlText11 = //@formatter:off
                "<project>\n" +
                "    <parent>\n" +
                "        <artifactId>ARTIFACT-ID</artifactId>\n" +
                "        <groupId>GROUP-ID</groupId>\n" +
                "    </parent>\n" +
                "    <description>\n" +
                "        DESCRIPTION ONE     \n" +
                "            ... stuff ...     \n" +
                "            ... more stuff ...      \n" +
                "        DESCRIPTION TWO\n" +
                "    </description>\n" +
                "</project>\n" //@formatter:on
                ;
//        {
//            final Reader xmlReader = new StringReader(xmlText1);
//            final Serializer serializer = new Persister();
//            final Parent element = serializer.read(Parent.class, xmlReader);
//            System.out.println(element.toText());
//        }
//        {
//            final Reader xmlReader = new StringReader(xmlText2);
//            final Serializer serializer = new Persister();
//            final Exclusion element = serializer.read(Exclusion.class, xmlReader);
//            System.out.println(element.toText());
//        }
//        {
//            final Reader xmlReader = new StringReader(xmlText3);
//            final Serializer serializer = new Persister();
//            final Exclusions element = serializer.read(Exclusions.class, xmlReader);
//            System.out.println(element.toText());
//        }
//        {
//            final Reader xmlReader = new StringReader(xmlText4);
//            final Serializer serializer = new Persister();
//            final Exclusions element = serializer.read(Exclusions.class, xmlReader);
//            System.out.println(element.toText());
//        }
//        {
//            final Reader xmlReader = new StringReader(xmlText5);
//            final Serializer serializer = new Persister();
//            final Exclusions element = serializer.read(Exclusions.class, xmlReader);
//            System.out.println(element.toText());
//        }
//        {
//            final Reader xmlReader = new StringReader(xmlText6);
//            final Serializer serializer = new Persister();
//            final Dependencies element = serializer.read(Dependencies.class, xmlReader);
//            System.out.println(element.toText());
//        }
//        {
//            final Reader xmlReader = new StringReader(xmlText7);
//            final Serializer serializer = new Persister();
//            final Dependencies element = serializer.read(Dependencies.class, xmlReader);
//            System.out.println(element.toText());
//        }
//        {
//            final Reader xmlReader = new StringReader(xmlText8);
//            final Serializer serializer = new Persister();
//            final Dependencies element = serializer.read(Dependencies.class, xmlReader);
//            System.out.println(element.toText());
//        }
        {
            final Reader xmlReader = new StringReader(xmlText5);
            final Serializer serializer = new Persister();
            final Exclusions element = serializer.read(Exclusions.class, xmlReader);
            System.out.println(element.show());
        }
//        {
//            final Reader xmlReader = new StringReader(xmlText9a);
//            final Serializer serializer = new Persister();
//            final Project element = serializer.read(Project.class, xmlReader);
//            System.out.println(element.toText());
//        }
//        {
//            final Reader xmlReader = new StringReader(xmlText9b);
//            final Serializer serializer = new Persister();
//            final Project element = serializer.read(Project.class, xmlReader);
//            System.out.println(element.toText());
//        }
//        {
//            final Reader xmlReader = new StringReader(xmlText9c);
//            final Serializer serializer = new Persister();
//            final Project element = serializer.read(Project.class, xmlReader);
//            System.out.println(element.toText());
//        }
//        {
//            final Reader xmlReader = new StringReader(xmlText10);
//            final Serializer serializer = new Persister();
//            final Project element = serializer.read(Project.class, xmlReader);
//            System.out.println(element.toText());
//        }
//        {
//            final Reader xmlReader = new StringReader(xmlText11);
//            final Serializer serializer = new Persister();
//            final Project element = serializer.read(Project.class, xmlReader);
//            System.out.println(element.toText());
//        }
        System.out.println("Okay");
    }
}
