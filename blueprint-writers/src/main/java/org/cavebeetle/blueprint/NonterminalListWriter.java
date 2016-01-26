package org.cavebeetle.blueprint;

import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.lang.model.element.Modifier;
import org.cavebeetle.primes.Primes;
import org.cavebeetle.text.Show;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
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

public final class NonterminalListWriter
{
    public static final void main(final String[] args) throws Exception
    {
        final File targetDir = new File(args[0]);
        final File srcMainJava = new File(targetDir, "generated-sources/java");
        final String packageName = args[1];
        final List<String> nonterminalLists = Lists.newArrayList(args).subList(2, args.length);
        final Primes primes = Primes.Maker.make();
        primes.skip(500);
        int maxLength = 0;
        for (final String nonterminalList : nonterminalLists)
        {
            final String nonterminalListName = nonterminalList.substring(0, nonterminalList.indexOf(':')).trim();
            final int length = nonterminalListName.trim().length();
            if (length > maxLength)
            {
                maxLength = length;
            }
        }
        final String mask = String.format("Generating nonterminal list %%-%ds ... ", Integer.valueOf(maxLength + 2));
        for (final String nonterminalList : nonterminalLists)
        {
            final String nonterminalListName = nonterminalList.substring(0, nonterminalList.indexOf(':')).trim();
            final String nonterminalListEntryName = nonterminalList.substring(nonterminalList.indexOf(':') + 1).trim();
            System.out.print(String.format(mask, "'" + nonterminalListName + "'"));
            final NonterminalListWriter nonterminalListWriter = new NonterminalListWriter(primes, packageName, nonterminalListName, nonterminalListEntryName);
            final JavaFile javaFile = nonterminalListWriter.create();
            javaFile.writeTo(srcMainJava);
            System.out.println("done");
        }
    }

    private final Primes primes;
    private final String packageName;
    private final String nonterminalListName;
    private final String tag;
    private final String simpleName;
    private final TypeName nonterminalListTypeName;
    private final String nonterminalListEntryName;

    public NonterminalListWriter(final Primes primes, final String packageName, final String nonterminalListName, final String nonterminalListEntryName)
    {
        this.primes = primes;
        this.packageName = packageName;
        this.nonterminalListName = nonterminalListName;
        tag = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, nonterminalListName);
        simpleName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, nonterminalListName).replace('_', ' ');
        nonterminalListTypeName = ClassName.get(packageName, nonterminalListName);
        this.nonterminalListEntryName = nonterminalListEntryName;
    }

    public JavaFile create()
    {
        return JavaFile
                .builder(packageName, createNonterminalListType())
                .indent("    ")
                .skipJavaLangImports(true)
                .build();
    }

    public TypeSpec createNonterminalListType()
    {
        final TypeName comparable = ParameterizedTypeName
                .get(ClassName.get(Comparable.class), ClassName.get(packageName, nonterminalListName));
        final TypeName iterable = ParameterizedTypeName
                .get(ClassName.get(Iterable.class), ClassName.get(packageName, nonterminalListEntryName));
        return TypeSpec
                .classBuilder(nonterminalListName)
                .addJavadoc("The &lt;$L&gt; element.", tag)
                .addAnnotation(AnnotationSpec.builder(Root.class).build())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(comparable)
                .addSuperinterface(iterable)
                .addSuperinterface(Show.class)
                .addField(createFieldMissing())
                .addField(createFieldList())
                .addMethod(createConstructor())
                .addMethod(createPrivateConstructor())
                .addMethod(createMethodIterator())
                .addMethod(createHashCode())
                .addMethod(createEquals())
                .addMethod(createCompareTo())
                .addMethod(createShow())
                .build();
    }

    public FieldSpec createFieldMissing()
    {
        return FieldSpec
                .builder(nonterminalListTypeName, "MISSING", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .addJavadoc("Represents a missing {@code $L}.\n", nonterminalListName)
                .initializer("new $T()", nonterminalListTypeName)
                .build();
    }

    public FieldSpec createFieldList()
    {
        final TypeName list = ParameterizedTypeName
                .get(ClassName.get(List.class), ClassName.get(packageName, nonterminalListEntryName));
        return FieldSpec
                .builder(list, "list", Modifier.PRIVATE, Modifier.FINAL)
                .addAnnotation(AnnotationSpec
                        .builder(ElementList.class)
                        .addMember("inline", "true")
                        .addMember("required", "false")
                        .addMember("empty", "false")
                        .build())
                .build();
    }

    public MethodSpec createConstructor()
    {
        final TypeName list = ParameterizedTypeName
                .get(ClassName.get(List.class), ClassName.get(packageName, nonterminalListEntryName));
        return MethodSpec
                .constructorBuilder()
                .addJavadoc("Creates a new {@code $L} instance.\n\n@param list the list of {@code $L} instances.\n", nonterminalListName, nonterminalListEntryName)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec
                        .builder(list, "list", Modifier.FINAL)
                        .addAnnotation(AnnotationSpec
                                .builder(ElementList.class)
                                .addMember("inline", "true")
                                .addMember("required", "false")
                                .addMember("empty", "false")
                                .build())
                        .build())
                .addCode(CodeBlock
                        .builder()
                        .addStatement("$T.checkNotNull(list, \"Missing 'list'.\")", Preconditions.class)
                        .addStatement("this.list = list")
                        .build())
                .build();
    }

    public MethodSpec createPrivateConstructor()
    {
        return MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addCode(CodeBlock
                        .builder()
                        .addStatement("list = $T.emptyList()", Collections.class)
                        .build())
                .build();
    }

    public MethodSpec createMethodIterator()
    {
        final TypeName iterator = ParameterizedTypeName
                .get(ClassName.get(Iterator.class), ClassName.get(packageName, nonterminalListEntryName));
        return MethodSpec
                .methodBuilder("iterator")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(iterator)
                .addCode(CodeBlock
                        .builder()
                        .addStatement("return list.iterator()")
                        .build())
                .build();
    }

    public MethodSpec createHashCode()
    {
        return MethodSpec
                .methodBuilder("hashCode")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .addCode(CodeBlock
                        .builder()
                        .addStatement("final int prime = $L", primes.next())
                        .addStatement("return prime + list.hashCode()")
                        .build())
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
                        .addStatement("final $T other = ($T) $L", nonterminalListTypeName, nonterminalListTypeName, object.name)
                        .addStatement("return compareTo(other) == 0")
                        .build())
                .build();
    }

    public MethodSpec createCompareTo()
    {
        final String other = "other";
        return MethodSpec
                .methodBuilder("compareTo")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .addParameter(ParameterSpec
                        .builder(nonterminalListTypeName, other, Modifier.FINAL)
                        .build())
                .addCode(CodeBlock
                        .builder()
                        .addStatement("$T.checkNotNull($L, \"Missing '$L'.\")", Preconditions.class, other, other)
                        .add("return $T\n", ComparisonChain.class)
                        .indent()
                        .indent()
                        .add(".start()\n")
                        .add(".compare(list, other.list, $T\n", Ordering.class)
                        .indent()
                        .indent()
                        .add(".<$L> natural()\n", nonterminalListEntryName)
                        .add(".lexicographical())\n")
                        .unindent()
                        .unindent()
                        .add(".result();\n")
                        .unindent()
                        .unindent()
                        .build())
                .build();
    }

    public MethodSpec createShow()
    {
        return MethodSpec
                .methodBuilder("show")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .addCode(CodeBlock
                        .builder()
                        .addStatement("final $T text = new $T()", StringBuilder.class, StringBuilder.class)
                        .addStatement("text.append(\"<$L>\").append('\\n')", tag)
                        .beginControlFlow("for (final $L item : this)", nonterminalListEntryName)
                        .addStatement("text.append(\"    \").append(item.show().replace(\"\\n\", \"\\n    \")).append('\\n')")
                        .endControlFlow()
                        .addStatement("text.append(\"</$L>\")", tag)
                        .addStatement("return text.toString()")
                        .build())
                .build();
    }
}
