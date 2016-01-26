package org.cavebeetle.blueprint;

import javax.lang.model.element.Modifier;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;
import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
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

public final class TerminalWriter
{
    private final JavaFileInfo javaFileInfo;
    private final Integer prime;
    private final String packageName;
    private final String terminalName;
    private final String tag;
    private final String simpleName;
    private final TypeName terminalTypeName;

    public TerminalWriter(final JavaFileInfo javaFileInfo)
    {
        Preconditions.checkNotNull(javaFileInfo, "Missing 'javaFileInfo'.");
        this.javaFileInfo = javaFileInfo;
        packageName = javaFileInfo.packageName();
        terminalName = javaFileInfo.entityName();
        prime = javaFileInfo.prime();
        tag = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, terminalName);
        simpleName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, terminalName).replace('_', ' ');
        terminalTypeName = ClassName.get(packageName, terminalName);
    }

    public void create()
    {
        final JavaFile javaFile = JavaFile
                .builder(packageName, createTerminalType())
                .indent("    ")
                .skipJavaLangImports(true)
                .build();
        Misc.writeJavaFile(javaFile, javaFileInfo.projectInfo().srcMainJavaDir());
        final TerminalTestWriter terminalTestWriter = new TerminalTestWriter(javaFileInfo);
        terminalTestWriter.create();
    }

    public TypeSpec createTerminalType()
    {
        final TypeName terminal = ParameterizedTypeName
                .get(ClassName.get(Terminal.class), ClassName.get(packageName, terminalName));
        return TypeSpec
                .classBuilder(terminalName)
                .addJavadoc("The &lt;$L&gt; element.\n", tag)
                .addAnnotation(AnnotationSpec
                        .builder(Root.class)
                        .build())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(terminal)
                .addField(createFieldMissing())
                .addField(createFieldValue())
                .addMethod(createPublicConstructor())
                .addMethod(createPrivateConstructor())
                .addMethod(createValueGetter())
                .addMethod(createCompareTo())
                .addMethod(createHashCode())
                .addMethod(createEquals())
                .addMethod(createShow())
                .build();
    }

    public FieldSpec createFieldMissing()
    {
        return FieldSpec
                .builder(terminalTypeName, "MISSING", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .addJavadoc("Represents a missing {@code $L}.\n", terminalName)
                .initializer("new $T()", terminalTypeName)
                .build();
    }

    public FieldSpec createFieldValue()
    {
        return FieldSpec
                .builder(String.class, "value", Modifier.PRIVATE, Modifier.FINAL)
                .addAnnotation(AnnotationSpec
                        .builder(Text.class)
                        .addMember("required", "false")
                        .build())
                .build();
    }

    public MethodSpec createPublicConstructor()
    {
        final ParameterSpec value = ParameterSpec
                .builder(String.class, "value", Modifier.FINAL)
                .addAnnotation(AnnotationSpec
                        .builder(Text.class)
                        .addMember("required", "false")
                        .build())
                .build();
        return MethodSpec
                .constructorBuilder()
                .addJavadoc(
                        "Creates a new {@code $L} instance.\n\n@param value the value representing the actual $L.\n",
                        terminalName,
                        simpleName)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(value)
                .addCode(CodeBlock
                        .builder()
                        .addStatement("$T.checkNotNull(value, \"Missing 'value'.\")", Preconditions.class)
                        .addStatement("this.value = value.trim()")
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
                        .addStatement("this.value = \"\"")
                        .build())
                .build();
    }

    public MethodSpec createValueGetter()
    {
        return MethodSpec
                .methodBuilder("value")
                .addAnnotation(Override.class)
                .addJavadoc(
                        "Returns this {@code $L}'s value.\n\n@return this {@code $L}'s value.\n",
                        terminalName,
                        terminalName)
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .addCode(CodeBlock
                        .builder()
                        .addStatement("return value")
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
                .addParameter(ParameterSpec.builder(terminalTypeName, other, Modifier.FINAL).build())
                .addCode(CodeBlock
                        .builder()
                        .addStatement("return value.compareTo($L.value)", other)
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
                        .addStatement("final int prime = $L", prime)
                        .addStatement("return prime + value.hashCode()")
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
                        .addStatement("final $T other = ($T) $L", terminalTypeName, terminalTypeName, object.name)
                        .addStatement("return value.equals(other.value)")
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
                        .addStatement("return String.format(\"<$L>%s</$L>\", value)", tag, tag)
                        .build())
                .build();
    }
}
