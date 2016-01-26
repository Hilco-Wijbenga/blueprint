package org.cavebeetle.blueprint;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import javax.lang.model.element.Modifier;
import org.cavebeetle.primes.Primes;
import org.cavebeetle.text.Show;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

public final class NonterminalWriter
{
    public static final void main(final String[] args) throws Exception
    {
        final File targetDir = new File(args[0]);
        final File srcMainJava = new File(targetDir, "generated-sources/java");
        final String packageName = args[1];
        final List<String> nonterminals = Lists.newArrayList(args).subList(2, args.length);
        final Primes primes = Primes.Maker.make();
        primes.skip(100);
        int maxLength = 0;
        for (final String nonterminal : nonterminals)
        {
            final int length = nonterminal.indexOf(' ');
            if (length > maxLength)
            {
                maxLength = length;
            }
        }
        final String mask = String.format("Generating nonterminal %%-%ds ... ", Integer.valueOf(maxLength + 2));
        final Pattern separator = Pattern.compile("([ \t]|\r\n|\n)+");
        for (final String nonterminal : nonterminals)
        {
            final Iterator<String> componentIt = Splitter.on(separator).omitEmptyStrings().split(nonterminal).iterator();
            final String nonterminalName = componentIt.next();
            System.out.print(String.format(mask, "'" + nonterminalName + "'"));
            final List<String> components = Lists.newArrayList();
            while (componentIt.hasNext())
            {
                final String component = componentIt.next();
                components.add(component);
            }
            final NonterminalWriter nonterminalWriter = new NonterminalWriter(primes, packageName, nonterminalName, components);
            final JavaFile javaFile = nonterminalWriter.create();
            javaFile.writeTo(srcMainJava);
            System.out.println("done");
        }
    }

    private final Primes primes;
    private final String packageName;
    private final String nonterminalName;
    private final String tag;
    private final TypeName nonterminalTypeName;
    private final List<String> components;

    public NonterminalWriter(final Primes primes, final String packageName, final String nonterminalName, final List<String> components)
    {
        this.primes = primes;
        this.packageName = packageName;
        this.nonterminalName = nonterminalName;
        tag = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, nonterminalName);
        nonterminalTypeName = ClassName.get(packageName, nonterminalName);
        this.components = components;
    }

    public JavaFile create()
    {
        return JavaFile
                .builder(packageName, createNonterminalType())
                .indent("    ")
                .skipJavaLangImports(true)
                .build();
    }

    public TypeSpec createNonterminalType()
    {
        final TypeName comparable = ParameterizedTypeName
                .get(ClassName.get(Comparable.class), ClassName.get(packageName, nonterminalName));
        return TypeSpec
                .classBuilder(nonterminalName)
                .addJavadoc("The &lt;$L&gt; element.", tag)
                .addAnnotation(AnnotationSpec.builder(Root.class).build())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(comparable)
                .addSuperinterface(Show.class)
                .addField(createFieldMissing())
                .addFields(createFields())
                .addMethod(createDefaultConstructor())
                .addMethod(createFullConstructor())
                .addMethods(createGetters())
                .addMethod(createHashCode())
                .addMethod(createEquals())
                .addMethod(createCompareTo())
                .addMethod(createShow())
                .build();
    }

    public FieldSpec createFieldMissing()
    {
        return FieldSpec
                .builder(nonterminalTypeName, "MISSING", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .addJavadoc("Represents a missing {@code $L}.\n", nonterminalName)
                .initializer("new $T()", nonterminalTypeName)
                .build();
    }

    public Iterable<FieldSpec> createFields()
    {
        final List<FieldSpec> fields = Lists.newArrayList();
        for (final String component : components)
        {
            final TypeName fieldType = ClassName.get(packageName, component);
            final String fieldName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, component);
            final AnnotationSpec elementAnnotation = AnnotationSpec
                    .builder(Element.class)
                    .addMember("name", "\"" + fieldName + "\"")
                    .addMember("required", "false")
                    .build();
            final FieldSpec field = FieldSpec
                    .builder(fieldType, fieldName, Modifier.PRIVATE, Modifier.FINAL)
                    .addAnnotation(elementAnnotation)
                    .build();
            fields.add(field);
        }
        return fields;
    }

    public MethodSpec createDefaultConstructor()
    {
        final CodeBlock.Builder constructorBuilder = CodeBlock
                .builder()
                .add("this(\n")
                .indent()
                .indent();
        final Iterator<String> componentIt = components.iterator();
        while (componentIt.hasNext())
        {
            final String component = componentIt.next();
            final TypeName fieldType = ClassName.get(packageName, component);
            constructorBuilder.add("$T.MISSING", fieldType);
            if (componentIt.hasNext())
            {
                constructorBuilder.add(",\n");
            }
            else
            {
                constructorBuilder.add(");\n");
            }
        }
        final CodeBlock constructor = constructorBuilder
                .unindent()
                .unindent()
                .build();
        return MethodSpec
                .constructorBuilder()
                .addJavadoc("Creates an empty {@code $T} instance.\n", nonterminalTypeName)
                .addModifiers(Modifier.PUBLIC)
                .addCode(constructor)
                .build();
    }

    public MethodSpec createFullConstructor()
    {
        final MethodSpec.Builder constructorBuilder = MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Creates a new {@code CiManagement} instance.\n")
                .addJavadoc("\n");
        for (final String component : components)
        {
            final TypeName fieldType = ClassName.get(packageName, component);
            final String fieldName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, component);
            constructorBuilder.addJavadoc("@param $L the {@code $T} instance.\n", fieldName, fieldType);
        }
        for (final String component : components)
        {
            final TypeName fieldType = ClassName.get(packageName, component);
            final String fieldName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, component);
            constructorBuilder.addParameter(ParameterSpec
                    .builder(fieldType, fieldName, Modifier.FINAL)
                    .addAnnotation(AnnotationSpec
                            .builder(Element.class)
                            .addMember("name", "\"" + fieldName + "\"")
                            .addMember("required", "false")
                            .build())
                    .build());
        }
        for (final String component : components)
        {
            final TypeName fieldType = ClassName.get(packageName, component);
            final String fieldName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, component);
            constructorBuilder.addStatement("this.$L = $L == null ? $T.MISSING : $L", fieldName, fieldName, fieldType, fieldName);
        }
        return constructorBuilder.build();
    }

    public Iterable<MethodSpec> createGetters()
    {
        final List<MethodSpec> getters = Lists.newArrayList();
        for (final String component : components)
        {
            final TypeName fieldType = ClassName.get(packageName, component);
            final String fieldName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, component);
            final String simpleFieldName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, component).replace('_', ' ');
            final MethodSpec getter = MethodSpec
                    .methodBuilder(fieldName)
                    .addJavadoc(
                            "Returns this {@code $L}'s $L.\n\n@return the $L.\n",
                            nonterminalName,
                            simpleFieldName,
                            simpleFieldName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(fieldType)
                    .addCode(CodeBlock
                            .builder()
                            .addStatement("return $L", fieldName)
                            .build())
                    .build();
            getters.add(getter);
        }
        return getters;
    }

    public MethodSpec createCompareTo()
    {
        final String other = "other";
        final CodeBlock.Builder comparisonBuilder = CodeBlock
                .builder()
                .addStatement("$T.checkNotNull($L, \"Missing '$L'.\")", Preconditions.class, other, other)
                .add("return $T\n", ComparisonChain.class)
                .indent()
                .indent()
                .add(".start()\n");
        for (final String component : components)
        {
            final String fieldName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, component);
            comparisonBuilder.add(".compare($L, other.$L)\n", fieldName, fieldName);
        }
        final CodeBlock comparison = comparisonBuilder
                .add(".result();\n")
                .unindent()
                .unindent()
                .build();
        return MethodSpec
                .methodBuilder("compareTo")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .addParameter(ParameterSpec.builder(nonterminalTypeName, other, Modifier.FINAL).build())
                .addCode(comparison)
                .build();
    }

    public MethodSpec createHashCode()
    {
        final CodeBlock.Builder hashCodeBuilder = CodeBlock
                .builder()
                .addStatement("final int prime = $L", primes.next())
                .addStatement("int result = 1");
        for (final String component : components)
        {
            final String fieldName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, component);
            hashCodeBuilder.addStatement("result = prime * result + $L.hashCode()", fieldName);
        }
        final CodeBlock hashCode = hashCodeBuilder
                .addStatement("return result")
                .build();
        return MethodSpec
                .methodBuilder("hashCode")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .addCode(hashCode)
                .build();
    }

    public MethodSpec createEquals()
    {
        final ParameterSpec object = ParameterSpec
                .builder(Object.class, "object", Modifier.FINAL)
                .build();
        return MethodSpec
                .methodBuilder("equals")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(boolean.class)
                .addParameter(object)
                .addCode(CodeBlock
                        .builder()
                        .beginControlFlow("if (this == $L)", object.name)
                        .addStatement("return true")
                        .endControlFlow()
                        .beginControlFlow("if ($L == null || getClass() != $L.getClass())", object.name, object.name)
                        .addStatement("return false")
                        .endControlFlow()
                        .addStatement("final $T other = ($T) $L", nonterminalTypeName, nonterminalTypeName, object.name)
                        .addStatement("return compareTo(other) == 0")
                        .build())
                .build();
    }

    public MethodSpec createShow()
    {
        final CodeBlock.Builder toTextBuilder = CodeBlock
                .builder()
                .addStatement("final StringBuilder text = new StringBuilder()")
                .addStatement("text.append(\"<$L>\").append('\\n')", tag);
        for (final String component : components)
        {
            final String fieldName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, component);
            toTextBuilder.addStatement("text.append(\"    \").append($L.show().replace(\"\\n\", \"\\n    \")).append('\\n')", fieldName);
        }
        final CodeBlock toText = toTextBuilder
                .addStatement("text.append(\"</$L>\")", tag)
                .addStatement("return text.toString()")
                .build();
        return MethodSpec
                .methodBuilder("show")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .addCode(toText)
                .build();
    }
}
