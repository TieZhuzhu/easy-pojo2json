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
 * <p>
 * 解析器的职责主要分为三部分：
 * 1. 将 UAST/PSI 元素归一为字段或类模型。
 * 2. 根据 Jackson/Fastjson 注解、JavaDoc 标记与用户配置决定字段名和字段值。
 * 3. 递归展开嵌套类型、数组、集合、泛型与特殊对象，最终输出可序列化的对象树。
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
     * 解析单个字段的 JSON 键值对。
     *
     * @param pojoField 当前字段模型
     * @return 字段键值对；若字段应被忽略则返回 {@code null}
     */
    private Map.Entry<String, Object> parseField(POJOField pojoField) {
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
                pojoField.setIgnoreProperties(POJO2JSONParserUtils.arrayTextToList(annotation.findAttributeValue("value").getText()));
            }
        }

        String fieldKey = parseFieldKey(pojoField);
        if (fieldKey == null) {
            return null;
        }
        pojoField.setName(fieldKey);

        Object fieldValue = parseFieldValue(pojoField);
        if (fieldValue == null) {
            return null;
        }
        return Map.entry(fieldKey, fieldValue);
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
            String fieldName = POJO2JSONParserUtils.psiTextToString(annotation.findAttributeValue("value").getText());
            if (StringUtils.isNotBlank(fieldName)) {
                return fieldName;
            }
        }

        annotation = field.getAnnotation("com.alibaba.fastjson.annotation.JSONField");
        if (annotation != null) {
            String fieldName = POJO2JSONParserUtils.psiTextToString(annotation.findAttributeValue("name").getText());
            if (StringUtils.isNotBlank(fieldName)) {
                return fieldName;
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
}
