package org.cavebeetle.blueprint;

import java.lang.reflect.Method;
import java.util.List;
import javax.lang.model.element.Modifier;
import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

public final class TerminalTestWriter
{
    private final ProjectInfo projectInfo;
    private final String packageName;
    private final String terminalName;
    private final IndefiniteArticle indefiniteArticle;
    private final String tag;
    private final TypeName terminalTypeName;

    public TerminalTestWriter(final JavaFileInfo javaFileInfo)
    {
        Preconditions.checkNotNull(javaFileInfo, "Missing 'javaFileInfo'.");
        projectInfo = javaFileInfo.projectInfo();
        packageName = javaFileInfo.packageName();
        terminalName = javaFileInfo.entityName();
        indefiniteArticle = javaFileInfo.indefiniteArticle();
        tag = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, terminalName);
        terminalTypeName = ClassName.get(packageName, terminalName);
    }

    public void create()
    {
        final JavaFile javaFile = JavaFile
                .builder(packageName, createTerminalTestType())
                .indent("    ")
                .skipJavaLangImports(true)
                .build();
        Misc.writeJavaFile(javaFile, projectInfo.srcTestJavaDir());
    }

    public TypeSpec createTerminalTestType()
    {
        return TypeSpec
                .classBuilder(terminalName + "Test")
                .addJavadoc("The unit tests for {@code $L}.\n", terminalName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addFields(createFields())
                .addMethod(createSetUp())
                .addMethod(createTest_that_the_value_of_a_Terminal_instance_cannot_be_null())
                .addMethod(createTest_that_MISSING_is_essentially_an_empty_instance())
                .addMethod(createTest_that_a_Terminal_instances_order_is_based_on_its_value())
                .addMethod(createTest_that_a_Terminal_instances_hash_code_is_based_on_its_value())
                .addMethod(createTest_that_a_Terminal_instances_equality_to_another_is_based_on_its_value())
                .addMethod(createTest_that_the_textual_representation_of_a_Terminal_is_correct())
                .addMethod(createExecuteBridgeMethod())
                .build();
    }

    public List<FieldSpec> createFields()
    {
        return Lists.newArrayList(
                FieldSpec
                        .builder(terminalTypeName, tag, Modifier.PRIVATE)
                        .build(),
                FieldSpec
                        .builder(terminalTypeName, "different" + terminalName, Modifier.PRIVATE)
                        .build());
    }

    public MethodSpec createSetUp()
    {
        return MethodSpec
                .methodBuilder("setUp")
                .addJavadoc("Sets up each unit test.\n")
                .addAnnotation(ClassName.get("org.junit", "Before"))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addCode(CodeBlock
                        .builder()
                        .addStatement("$L = new $T($S)", tag, terminalTypeName, "value")
                        .addStatement("$L = new $T($S)", "different" + terminalName, terminalTypeName, "different")
                        .build())
                .build();
    }

    public MethodSpec createTest_that_the_value_of_a_Terminal_instance_cannot_be_null()
    {
        return MethodSpec
                .methodBuilder("test_that_the_value_of_" + indefiniteArticle.show() + "_" + terminalName + "_instance_cannot_be_null")
                .addJavadoc("Tests that the value of $L {@code $T} instance cannot be {@code null}.\n", indefiniteArticle.show(), terminalTypeName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addAnnotation(ClassName.get("org.junit", "Test"))
                .addCode(CodeBlock
                        .builder()
                        .beginControlFlow("try")
                        .indent()
                        .addStatement("new $T(null)", terminalTypeName)
                        .addStatement("$T.fail($S)", ClassName.get("org.junit", "Assert"), "Expected a NullPointerException.")
                        .unindent()
                        .nextControlFlow("catch (final NullPointerException e)")
                        .indent()
                        .addStatement("$T.assertEquals($S, e.getMessage())", ClassName.get("org.junit", "Assert"), "Missing 'value'.")
                        .unindent()
                        .endControlFlow()
                        .build())
                .build();
    }

    public MethodSpec createTest_that_MISSING_is_essentially_an_empty_instance()
    {
        return MethodSpec
                .methodBuilder("test_that_MISSING_is_essentially_an_empty_instance")
                .addJavadoc("Tests that {@code $T.MISSING} is essentially an \"empty\" instance.\n", terminalTypeName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addAnnotation(ClassName.get("org.junit", "Test"))
                .addCode(CodeBlock
                        .builder()
                        .addStatement("$T.assertEquals($S, $T.MISSING.value())", ClassName.get("org.junit", "Assert"), "", terminalTypeName)
                        .build())
                .build();
    }

    public MethodSpec createTest_that_a_Terminal_instances_order_is_based_on_its_value()
    {
        return MethodSpec
                .methodBuilder("test_that_" + indefiniteArticle.show() + "_" + terminalName + "_instance_order_is_based_on_its_value")
                .addJavadoc("Tests that $L {@code $T} instance's order is based on its value.\n", indefiniteArticle.show(), terminalTypeName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addAnnotation(ClassName.get("org.junit", "Test"))
                .addCode(CodeBlock
                        .builder()
                        .addStatement("final $T $LAbc = new $T($S)", terminalTypeName, tag, terminalTypeName, "abc")
                        .addStatement("final $T $LXyz = new $T($S)", terminalTypeName, tag, terminalTypeName, "xyz")
                        .addStatement("$T.assertTrue($LAbc.compareTo($LXyz) < 0)", ClassName.get("org.junit", "Assert"), tag, tag)
                        .addStatement("$T.assertEquals(0, $LAbc.compareTo($LAbc))", ClassName.get("org.junit", "Assert"), tag, tag)
                        .addStatement("$T.assertTrue($LXyz.compareTo($LAbc) > 0)", ClassName.get("org.junit", "Assert"), tag, tag)
                        .addStatement("$T.assertEquals(0, $LXyz.compareTo($LXyz))", ClassName.get("org.junit", "Assert"), tag, tag)
                        .build())
                .build();
    }

    public MethodSpec createTest_that_a_Terminal_instances_hash_code_is_based_on_its_value()
    {
        return MethodSpec
                .methodBuilder("test_that_" + indefiniteArticle.show() + "_" + terminalName + "_instances_hash_code_is_based_on_its_value")
                .addJavadoc("Tests that $L {@code $T} instance's hash code is based on its value.\n", indefiniteArticle.show(), terminalTypeName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addAnnotation(ClassName.get("org.junit", "Test"))
                .addCode(CodeBlock
                        .builder()
                        .addStatement("$T.assertEquals($L.hashCode(), $L.hashCode())", ClassName.get("org.junit", "Assert"), tag, tag)
                        .addStatement("$T.assertEquals($L.hashCode(), new $T($S).hashCode())", ClassName.get("org.junit", "Assert"), tag, terminalTypeName, "value")
                        .addStatement("$T.assertNotEquals($L.hashCode(), different$L.hashCode())", ClassName.get("org.junit", "Assert"), tag, terminalName)
                        .build())
                .build();
    }

    public MethodSpec createTest_that_a_Terminal_instances_equality_to_another_is_based_on_its_value()
    {
        return MethodSpec
                .methodBuilder("test_that_" + indefiniteArticle.show() + "_" + terminalName + "_instances_equality_to_another_is_based_on_its_value")
                .addJavadoc("Tests that $L {@code $T} instance's equality to another is based on its value.\n", indefiniteArticle.show(), terminalTypeName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addAnnotation(ClassName.get("org.junit", "Test"))
                .addCode(CodeBlock
                        .builder()
                        .addStatement("$T.assertTrue($L.equals($L))", ClassName.get("org.junit", "Assert"), tag, tag)
                        .addStatement("$T.assertTrue($L.equals(new $T($S)))", ClassName.get("org.junit", "Assert"), tag, terminalTypeName, "value")
                        .addStatement("$T.assertTrue(new $T($S).equals($L))", ClassName.get("org.junit", "Assert"), terminalTypeName, "value", tag)
                        .addStatement("$T.assertTrue(new $T($S).equals(new $T($S)))", ClassName.get("org.junit", "Assert"), terminalTypeName, "value", terminalTypeName, "  value  ")
                        .addStatement("$T.assertFalse($L.equals(null))", ClassName.get("org.junit", "Assert"), tag)
                        .addStatement("$T.assertFalse($L.equals(new Object()))", ClassName.get("org.junit", "Assert"), tag)
                        .addStatement("$T.assertFalse($L.equals(different$L))", ClassName.get("org.junit", "Assert"), tag, terminalName)
                        .build())
                .build();
    }

    public MethodSpec createTest_that_the_textual_representation_of_a_Terminal_is_correct()
    {
        return MethodSpec
                .methodBuilder("test_that_the_textual_representation_of_" + indefiniteArticle.show() + "_" + terminalName + "_is_correct")
                .addJavadoc("Tests that the textual representation of $L {@code $T} is correct.\n", indefiniteArticle.show(), terminalTypeName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addAnnotation(ClassName.get("org.junit", "Test"))
                .addCode(CodeBlock
                        .builder()
                        .addStatement("$T.assertEquals(\"<$L>value</$L>\", $L.show())", ClassName.get("org.junit", "Assert"), tag, tag, tag)
                        .build())
                .build();
    }

    public MethodSpec createExecuteBridgeMethod()
    {
        return MethodSpec
                .methodBuilder("executeBridgeMethod")
                .addJavadoc(
                        "Excecutes the bridge method that is generated for {@code Comparable}. This is not a test, it simply completes the\n" +
                            "coverage.\n" +
                            "\n" +
                            "@throws Exception if anything unexpected happens.\n")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addAnnotation(ClassName.get("org.junit", "Test"))
                .addException(Exception.class)
                .addCode(CodeBlock
                        .builder()
                        .addStatement("final $T compareTo = $T.class.getDeclaredMethod($S, Object.class)", Method.class, terminalTypeName, "compareTo")
                        .addStatement("compareTo.invoke($L, $L)", tag, tag)
                        .build())
                .build();
    }
}
