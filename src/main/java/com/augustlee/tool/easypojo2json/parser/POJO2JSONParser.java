package com.augustlee.tool.easypojo2json.parser;

import com.google.gson.GsonBuilder;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.util.PsiUtil;
import com.augustlee.tool.easypojo2json.parser.el.EvaluationContextFactory;
import com.augustlee.tool.easypojo2json.parser.model.POJOClass;
import com.augustlee.tool.easypojo2json.parser.model.POJOField;
import com.augustlee.tool.easypojo2json.parser.model.POJOVariable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UVariable;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.*;
import java.util.stream.Collectors;

/**
 * POJO 到 JSON 示例值的核心解析器。
 * 
 * @author August Lee
 * @see POJO2JSONParser
 * @since 2026-05-15 15:29:08
 *
 */
public class POJO2JSONParser {

    /**
     * GSON 仅负责最终的 pretty-print 输出，不参与字段语义推断。
     */
    private final GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();

    /**
     * 被视作“集合语义”的父类型集合。
     * <p>
     * 当字段类型命中这些父类型时，解析器会提取元素泛型并按“单元素示例列表”的策略生成结果。
     */
    private final List<String> iterableTypes = List.of(
            "java.lang.Iterable",
            "java.util.Collection",
            "java.util.AbstractCollection",
            "java.util.List",
            "java.util.AbstractList",
            "java.util.Set",
            "java.util.AbstractSet");

    /**
     * 用于解析字段名和默认值表达式的 SpEL 解析器。
     */
    private final ExpressionParser expressionParser = new SpelExpressionParser();

    /**
     * 采用模板解析上下文，使配置可以使用 {@code #{...}} 的 SpEL 语法。
     */
    private final ParserContext templateParserContext = new TemplateParserContext();

    /**
     * 默认构造器。
     */
    public POJO2JSONParser() {
    }

    /**
     * 将 UAST 元素转换为格式化 JSON 文本。
     *
     * @param uElement 需要转换的 UAST 元素，通常是类、字段、参数或局部变量
     * @return 格式化后的 JSON 字符串
     */
    public String uElementToJSONString(@NotNull final UElement uElement) {
        Object result = null;

        if (uElement instanceof UVariable variable) {
            result = parseFieldValue(POJOVariable.init((PsiVariable) variable.getJavaPsi(), getPsiClassGenerics(variable.getType())));
        } else if (uElement instanceof UClass uClass) {
            // IDEA 建议直接通过 UClass#getJavaPsi 取得底层 PsiClass。
            result = parseClass(POJOClass.init(uClass.getJavaPsi()));
        }

        // 目前输出阶段仅序列化对象树，不保留字段注释到 JSON 文本中。
        return gsonBuilder.create().toJson(result);
    }

    /**
     * 将 UAST 元素转换为带字段 JavaDoc 注释的 JSON 风格文本。
     *
     * @param uElement 需要转换的 UAST 元素，通常是类、字段、参数或局部变量
     * @return 带字段注释的 JSON 风格字符串
     */
    public String uElementToJSONStringWithComment(@NotNull final UElement uElement) {
        CommentedValue result = null;

        if (uElement instanceof UVariable variable) {
            result = parseFieldValueWithComment(POJOVariable.init((PsiVariable) variable.getJavaPsi(), getPsiClassGenerics(variable.getType())));
        } else if (uElement instanceof UClass uClass) {
            result = parseClassWithComment(POJOClass.init(uClass.getJavaPsi()));
        }

        return renderCommentedValue(result, 0);
    }

    /**
     * 解析类中的全部字段并组装为 JSON 对象结构。
     *
     * @param pojoClass 当前待解析类
     * @return 字段名到字段值的有序映射；若类被标记为忽略类型则返回 {@code null}
     */
    private Map<String, Object> parseClass(POJOClass pojoClass) {
        PsiClass psiClass = pojoClass.getPsiClass();

        PsiAnnotation annotation = psiClass.getAnnotation(com.fasterxml.jackson.annotation.JsonIgnoreType.class.getName());
        if (annotation != null) {
            return null;
        }

        return Arrays.stream(psiClass.getAllFields())
                .map(field -> parseField(pojoClass.toField(field)))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (ov, nv) -> ov, LinkedHashMap::new));
    }

    /**
     * 解析类中的全部字段并组装为带注释的对象结构。
     *
     * @param pojoClass 当前待解析类
     * @return 带注释的对象结构；若类被标记为忽略类型则返回 {@code null}
     */
    private CommentedObjectValue parseClassWithComment(POJOClass pojoClass) {
        PsiClass psiClass = pojoClass.getPsiClass();

        PsiAnnotation annotation = psiClass.getAnnotation(com.fasterxml.jackson.annotation.JsonIgnoreType.class.getName());
        if (annotation != null) {
            return null;
        }

        LinkedHashMap<String, CommentedField> fields = Arrays.stream(psiClass.getAllFields())
                .map(field -> parseFieldWithComment(pojoClass.toField(field)))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(CommentedField::key, field -> field, (ov, nv) -> ov, LinkedHashMap::new));

        return new CommentedObjectValue(new ArrayList<>(fields.values()));
    }

    /**
     * 解析单个字段的 JSON 键值对。
     *
     * @param pojoField 当前字段模型
     * @return 字段键值对；若字段应被忽略则返回 {@code null}
     */
    private Map.Entry<String, Object> parseField(POJOField pojoField) {
        String fieldKey = prepareFieldKey(pojoField);
        if (fieldKey == null) {
            return null;
        }

        Object fieldValue = parseFieldValue(pojoField);
        if (fieldValue == null) {
            return null;
        }
        return Map.entry(fieldKey, fieldValue);
    }

    /**
     * 解析单个字段的带注释键值对。
     *
     * @param pojoField 当前字段模型
     * @return 带注释字段；若字段应被忽略则返回 {@code null}
     */
    private CommentedField parseFieldWithComment(POJOField pojoField) {
        String fieldKey = prepareFieldKey(pojoField);
        if (fieldKey == null) {
            return null;
        }

        CommentedValue fieldValue = parseFieldValueWithComment(pojoField);
        if (fieldValue == null) {
            return null;
        }

        String comment = POJO2JSONParserUtils.extractDocDescription(pojoField.getPsiField().getDocComment());
        return new CommentedField(fieldKey, comment, fieldValue);
    }

    /**
     * 统一执行字段过滤、忽略属性继承与字段名解析。
     *
     * @param pojoField 当前字段模型
     * @return 最终输出字段名；若字段应被忽略则返回 {@code null}
     */
    private String prepareFieldKey(POJOField pojoField) {
        PsiField field = pojoField.getPsiField();

        // 静态字段不会出现在实例 JSON 中，这里也顺带过滤 Kotlin companion object / INSTANCE 等场景。
        if (field.hasModifierProperty(PsiModifier.STATIC)) {
            return null;
        }

        if (pojoField.getIgnoreProperties().contains(field.getName())) {
            return null;
        }

        PsiAnnotation annotation = field.getAnnotation(com.fasterxml.jackson.annotation.JsonIgnore.class.getName());
        if (annotation != null) {
            return null;
        }

        PsiDocComment docComment = field.getDocComment();
        if (docComment != null) {
            PsiDocTag psiDocTag = docComment.findTagByName("JsonIgnore");
            if (psiDocTag != null && "JsonIgnore".equals(psiDocTag.getName())) {
                return null;
            }

            // 文档注释中的忽略配置优先用于当前字段递归展开出的下级属性过滤。
            pojoField.setIgnoreProperties(POJO2JSONParserUtils.docTextToList("@JsonIgnoreProperties", docComment.getText()));
        } else {
            annotation = field.getAnnotation(com.fasterxml.jackson.annotation.JsonIgnoreProperties.class.getName());
            if (annotation != null) {
                PsiAnnotationMemberValue attributeValue = annotation.findAttributeValue("value");
                if (attributeValue != null) {
                    pojoField.setIgnoreProperties(POJO2JSONParserUtils.arrayTextToList(attributeValue.getText()));
                }
            }
        }

        String fieldKey = parseFieldKey(pojoField);
        if (fieldKey == null) {
            return null;
        }
        pojoField.setName(fieldKey);
        return fieldKey;
    }

    /**
     * 解析字段最终输出到 JSON 中的键名。
     * <p>
     * 优先级如下：
     * 1. Jackson {@code @JsonProperty}
     * 2. Fastjson {@code @JSONField(name = ...)}
     * 3. 类级 {@code @JsonNaming}
     * 4. 用户配置的字段名 SpEL 表达式
     *
     * @param pojoField 当前字段模型
     * @return 字段键名
     */
    private String parseFieldKey(POJOField pojoField) {
        PsiField field = pojoField.getPsiField();

        PsiAnnotation annotation = field.getAnnotation(com.fasterxml.jackson.annotation.JsonProperty.class.getName());
        if (annotation != null) {
            PsiAnnotationMemberValue attributeValue = annotation.findAttributeValue("value");
            if (attributeValue != null) {
                String fieldName = POJO2JSONParserUtils.psiTextToString(attributeValue.getText());
                if (StringUtils.isNotBlank(fieldName)) {
                    return fieldName;
                }
            }
        }

        annotation = field.getAnnotation("com.alibaba.fastjson.annotation.JSONField");
        if (annotation != null) {
            PsiAnnotationMemberValue attributeValue = annotation.findAttributeValue("name");
            if (attributeValue != null) {
                String fieldName = POJO2JSONParserUtils.psiTextToString(attributeValue.getText());
                if (StringUtils.isNotBlank(fieldName)) {
                    return fieldName;
                }
            }
        }

        annotation = pojoField.getPsiFieldClass().getAnnotation(com.fasterxml.jackson.databind.annotation.JsonNaming.class.getName());
        if (annotation != null) {
            String text = annotation.findAttributeValue("value").getText();
            String[] segments = text.split("\\.");
            String strategySimpleName = segments[segments.length - 2];
            if (strategySimpleName.equals(com.fasterxml.jackson.databind.PropertyNamingStrategies.LowerCamelCaseStrategy.class.getSimpleName())) {
                return pojoField.getCamelCaseName();
            } else if (strategySimpleName.equals(com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy.class.getSimpleName())) {
                return pojoField.getPascalCaseName();
            } else if (strategySimpleName.equals(com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy.class.getSimpleName())) {
                return pojoField.getSnakeCaseName();
            } else if (strategySimpleName.equals(com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperSnakeCaseStrategy.class.getSimpleName())) {
                return pojoField.getSnakeCaseUpperName();
            } else if (strategySimpleName.equals(com.fasterxml.jackson.databind.PropertyNamingStrategies.KebabCaseStrategy.class.getSimpleName())) {
                return pojoField.getKebabCaseName();
            } else if (strategySimpleName.equals(com.fasterxml.jackson.databind.PropertyNamingStrategies.LowerCaseStrategy.class.getSimpleName())) {
                return pojoField.getLowerCaseName();
            } else if (strategySimpleName.equals(com.fasterxml.jackson.databind.PropertyNamingStrategies.LowerDotCaseStrategy.class.getSimpleName())) {
                return pojoField.getLowerDotCaseName();
            }
        }

        Expression expression = expressionParser.parseExpression(SettingsState.getInstance().fieldNameSpEL, templateParserContext);
        return expression.getValue(EvaluationContextFactory.newEvaluationContext(pojoField), String.class);
    }

    /**
     * 解析字段或变量的默认值。
     * <p>
     * 该方法是插件的核心递归入口，负责处理：
     * 原始类型、数组、枚举、集合、普通对象、泛型替换以及特殊内置对象。
     *
     * @param pojoVariable 当前变量模型
     * @return 适合序列化为 JSON 的默认值对象
     */
    private Object parseFieldValue(POJOVariable pojoVariable) {
        PsiType type = pojoVariable.getPsiType();
        Map<String, String> psiTypeExpression = SettingsState.getInstance().classNameSpELMap;

        if (type instanceof PsiPrimitiveType) {
            return getPrimitiveTypeValue(pojoVariable, type, psiTypeExpression);
        } else if (type instanceof PsiArrayType) {
            PsiType typeToDeepType = type.getDeepComponentType();
            Object obj = parseFieldValue(pojoVariable.deepVariable(typeToDeepType, getPsiClassGenerics(typeToDeepType)));
            return obj != null ? List.of(obj) : List.of();
        } else {
            PsiClass psiClass = PsiUtil.resolveClassInClassTypeOnly(type);

            if (psiClass == null) {
                return new LinkedHashMap<>();
            }

            if (psiClass.isEnum()) {
                return Arrays.stream(psiClass.getAllFields())
                        .filter(psiField -> psiField instanceof PsiEnumConstant)
                        .findFirst()
                        .map(PsiField::getName)
                        .orElse("");
            }

            List<String> fieldTypeNames = new ArrayList<>();
            fieldTypeNames.add(psiClass.getQualifiedName());
            fieldTypeNames.addAll(
                    Arrays.stream(psiClass.getSupers())
                            .map(PsiClass::getQualifiedName)
                            .toList()
            );
            fieldTypeNames = fieldTypeNames.stream().filter(Objects::nonNull).toList();

            List<String> retain = new ArrayList<>(fieldTypeNames);
            retain.retainAll(psiTypeExpression.keySet());
            if (!retain.isEmpty()) {
                // 命中用户或默认配置的特殊类型时，不再继续递归展开，而是直接走表达式生成示例值。
                try {
                    Expression expression = expressionParser.parseExpression(psiTypeExpression.get(retain.get(0)), templateParserContext);
                    return expression.getValue(EvaluationContextFactory.newEvaluationContext(pojoVariable));
                } catch (Exception e) {
                    throw new KnownException(e);
                }
            }

            boolean iterable = fieldTypeNames.stream().anyMatch(iterableTypes::contains);
            if (iterable) {
                // 集合一律生成“只包含一个示例元素”的结构，既保留形态又避免输出过长。
                PsiType typeToDeepType = PsiUtil.extractIterableTypeParameter(type, false);
                if (typeToDeepType == null) {
                    return List.of();
                }
                Object obj = parseFieldValue(pojoVariable.deepVariable(typeToDeepType, getPsiClassGenerics(typeToDeepType)));
                return obj != null ? List.of(obj) : List.of();
            }

            if (pojoVariable.getRecursionLevel() > 200) {
                throw new KnownException("This class reference level exceeds maximum limit or has nested references!");
            }

            // 当字段类型本身是泛型占位符时，优先使用上层类解析出来的实际类型继续递归。
            PsiType typeToDeepType = pojoVariable.getPsiClassGenerics().get(psiClass.getName());
            if (typeToDeepType != null) {
                return parseFieldValue(pojoVariable.deepVariable(typeToDeepType, getPsiClassGenerics(typeToDeepType)));
            }

            return parseClass(pojoVariable.deepClass(psiClass, getPsiClassGenerics(type)));
        }
    }

    /**
     * 解析字段或变量的默认值，并保留对象层级中的字段注释信息。
     *
     * @param pojoVariable 当前变量模型
     * @return 带注释的值结构
     */
    private CommentedValue parseFieldValueWithComment(POJOVariable pojoVariable) {
        PsiType type = pojoVariable.getPsiType();
        Map<String, String> psiTypeExpression = SettingsState.getInstance().classNameSpELMap;

        if (type instanceof PsiPrimitiveType) {
            return new CommentedPrimitiveValue(getPrimitiveTypeValue(pojoVariable, type, psiTypeExpression));
        } else if (type instanceof PsiArrayType) {
            PsiType typeToDeepType = type.getDeepComponentType();
            CommentedValue obj = parseFieldValueWithComment(pojoVariable.deepVariable(typeToDeepType, getPsiClassGenerics(typeToDeepType)));
            return obj != null ? new CommentedArrayValue(List.of(obj)) : new CommentedArrayValue(List.of());
        } else {
            PsiClass psiClass = PsiUtil.resolveClassInClassTypeOnly(type);

            if (psiClass == null) {
                return new CommentedObjectValue(List.of());
            }

            if (psiClass.isEnum()) {
                return new CommentedPrimitiveValue(Arrays.stream(psiClass.getAllFields())
                        .filter(psiField -> psiField instanceof PsiEnumConstant)
                        .findFirst()
                        .map(PsiField::getName)
                        .orElse(""));
            }

            List<String> fieldTypeNames = new ArrayList<>();
            fieldTypeNames.add(psiClass.getQualifiedName());
            fieldTypeNames.addAll(
                    Arrays.stream(psiClass.getSupers())
                            .map(PsiClass::getQualifiedName)
                            .toList()
            );
            fieldTypeNames = fieldTypeNames.stream().filter(Objects::nonNull).toList();

            List<String> retain = new ArrayList<>(fieldTypeNames);
            retain.retainAll(psiTypeExpression.keySet());
            if (!retain.isEmpty()) {
                try {
                    Expression expression = expressionParser.parseExpression(psiTypeExpression.get(retain.get(0)), templateParserContext);
                    return new CommentedPrimitiveValue(expression.getValue(EvaluationContextFactory.newEvaluationContext(pojoVariable)));
                } catch (Exception e) {
                    throw new KnownException(e);
                }
            }

            boolean iterable = fieldTypeNames.stream().anyMatch(iterableTypes::contains);
            if (iterable) {
                PsiType typeToDeepType = PsiUtil.extractIterableTypeParameter(type, false);
                if (typeToDeepType == null) {
                    return new CommentedArrayValue(List.of());
                }
                CommentedValue obj = parseFieldValueWithComment(pojoVariable.deepVariable(typeToDeepType, getPsiClassGenerics(typeToDeepType)));
                return obj != null ? new CommentedArrayValue(List.of(obj)) : new CommentedArrayValue(List.of());
            }

            if (pojoVariable.getRecursionLevel() > 200) {
                throw new KnownException("This class reference level exceeds maximum limit or has nested references!");
            }

            PsiType typeToDeepType = pojoVariable.getPsiClassGenerics().get(psiClass.getName());
            if (typeToDeepType != null) {
                return parseFieldValueWithComment(pojoVariable.deepVariable(typeToDeepType, getPsiClassGenerics(typeToDeepType)));
            }

            return parseClassWithComment(pojoVariable.deepClass(psiClass, getPsiClassGenerics(type)));
        }
    }

    /**
     * 渲染带注释的对象树。
     *
     * @param value  带注释值
     * @param indent 当前缩进层级
     * @return 最终文本
     */
    private String renderCommentedValue(CommentedValue value, int indent) {
        if (value == null) {
            return "null";
        }

        if (value instanceof CommentedPrimitiveValue primitiveValue) {
            return gsonBuilder.create().toJson(primitiveValue.value());
        }
        if (value instanceof CommentedArrayValue arrayValue) {
            return renderCommentedArray(arrayValue, indent);
        }
        if (value instanceof CommentedObjectValue objectValue) {
            return renderCommentedObject(objectValue, indent);
        }

        return "null";
    }

    /**
     * 渲染带注释的对象结构。
     *
     * @param objectValue 对象结构
     * @param indent      当前缩进层级
     * @return 对象文本
     */
    private String renderCommentedObject(CommentedObjectValue objectValue, int indent) {
        if (objectValue.fields().isEmpty()) {
            return "{}";
        }

        StringBuilder builder = new StringBuilder();
        builder.append("{\n");

        for (int i = 0; i < objectValue.fields().size(); i++) {
            CommentedField field = objectValue.fields().get(i);
            builder.append(renderCommentedField(field, indent + 1));
            if (i < objectValue.fields().size() - 1) {
                builder.append(",");
            }
            builder.append("\n");
        }

        builder.append(indent(indent)).append("}");
        return builder.toString();
    }

    /**
     * 渲染带注释的数组结构。
     *
     * @param arrayValue 数组结构
     * @param indent     当前缩进层级
     * @return 数组文本
     */
    private String renderCommentedArray(CommentedArrayValue arrayValue, int indent) {
        if (arrayValue.elements().isEmpty()) {
            return "[]";
        }

        StringBuilder builder = new StringBuilder();
        builder.append("[\n");
        for (int i = 0; i < arrayValue.elements().size(); i++) {
            builder.append(indent(indent + 1))
                    .append(renderCommentedValue(arrayValue.elements().get(i), indent + 1));
            if (i < arrayValue.elements().size() - 1) {
                builder.append(",");
            }
            builder.append("\n");
        }
        builder.append(indent(indent)).append("]");
        return builder.toString();
    }

    /**
     * 渲染单个带注释字段。
     *
     * @param field  带注释字段
     * @param indent 当前缩进层级
     * @return 字段文本
     */
    private String renderCommentedField(CommentedField field, int indent) {
        StringBuilder builder = new StringBuilder();
        appendLineComment(builder, field.comment(), indent);
        builder.append(indent(indent))
                .append(gsonBuilder.create().toJson(field.key()))
                .append(": ")
                .append(renderCommentedValue(field.value(), indent));
        return builder.toString();
    }

    /**
     * 追加单行注释。
     *
     * @param builder 构建器
     * @param comment 注释正文
     * @param indent  当前缩进层级
     */
    private void appendLineComment(StringBuilder builder, String comment, int indent) {
        if (StringUtils.isBlank(comment)) {
            return;
        }

        String indentText = indent(indent);
        Arrays.stream(comment.split("\n"))
                .forEach(line -> builder.append(indentText).append("// ").append(line).append("\n"));
    }

    /**
     * 生成指定层级的两个空格缩进。
     *
     * @param indent 缩进层级
     * @return 缩进字符串
     */
    private String indent(int indent) {
        return "  ".repeat(Math.max(0, indent));
    }

    /**
     * 提取当前类型上下文中的泛型实参映射。
     *
     * @param type 当前 PSI 类型
     * @return 泛型名称到实际类型的映射
     */
    private Map<String, PsiType> getPsiClassGenerics(PsiType type) {
        PsiClass psiClass = PsiUtil.resolveClassInClassTypeOnly(type);
        if (psiClass != null) {
            return Arrays.stream(psiClass.getTypeParameters())
                    .map(p -> Pair.of(p, PsiUtil.substituteTypeParameter(type, psiClass, p.getIndex(), false)))
                    .filter(p -> p.getValue() != null)
                    .collect(Collectors.toMap(p -> p.getKey().getName(), Pair::getValue));
        }
        return Map.of();
    }

    /**
     * 为 Java 原始类型生成默认值。
     *
     * @param pojoVariable     当前变量模型
     * @param type             当前原始类型
     * @param psiTypeExpression 类型到 SpEL 表达式的映射
     * @return 原始类型默认值；未识别类型时返回 {@code null}
     */
    private Object getPrimitiveTypeValue(POJOVariable pojoVariable, PsiType type, Map<String, String> psiTypeExpression) {
        Expression expression;
        switch (type.getCanonicalText()) {
            case "boolean":
                expression = expressionParser.parseExpression(psiTypeExpression.get("java.lang.Boolean"), templateParserContext);
                return expression.getValue(EvaluationContextFactory.newEvaluationContext(pojoVariable));
            case "byte":
            case "short":
            case "int":
            case "long":
                expression = expressionParser.parseExpression(psiTypeExpression.get("java.lang.Number"), templateParserContext);
                return expression.getValue(EvaluationContextFactory.newEvaluationContext(pojoVariable));
            case "float":
                expression = expressionParser.parseExpression(psiTypeExpression.get("java.lang.Float"), templateParserContext);
                return expression.getValue(EvaluationContextFactory.newEvaluationContext(pojoVariable));
            case "double":
                expression = expressionParser.parseExpression(psiTypeExpression.get("java.lang.Double"), templateParserContext);
                return expression.getValue(EvaluationContextFactory.newEvaluationContext(pojoVariable));
            case "char":
                expression = expressionParser.parseExpression(psiTypeExpression.get("java.lang.Character"), templateParserContext);
                return expression.getValue(EvaluationContextFactory.newEvaluationContext(pojoVariable));
            default:
                return null;
        }
    }

    /**
     * 带注释值标记接口。
     *
     * @author August Lee
     * @see CommentedValue
     * @since 2026-05-15 15:29:08
     *
     */
    private interface CommentedValue {
    }

    /**
     * 带注释字段渲染模型。
     *
     * @param key     字段名
     * @param comment 字段注释
     * @param value   字段值
     */
    private record CommentedField(String key, String comment, CommentedValue value) {
    }

    /**
     * 原始值渲染模型。
     *
     * @param value 原始值
     */
    private record CommentedPrimitiveValue(Object value) implements CommentedValue {
    }

    /**
     * 数组值渲染模型。
     *
     * @param elements 数组元素
     */
    private record CommentedArrayValue(List<CommentedValue> elements) implements CommentedValue {
    }

    /**
     * 对象值渲染模型。
     *
     * @param fields 对象字段列表
     */
    private record CommentedObjectValue(List<CommentedField> fields) implements CommentedValue {
    }
}
