package testdata.java;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 字段、参数和局部变量解析测试样例。
 * 
 * @author August Lee
 * @see VariableTestPOJO
 * @since 2026-05-15 15:29:08
 *
 */
public class VariableTestPOJO {

    private List<SimpleTestPOJO<String>> listField = new ArrayList<>();

    /**
     * 构造器参数测试入口。
     *
     * @param cParameter 构造器参数样例
     */
    public VariableTestPOJO(SimpleTestPOJO<Integer> cParameter) {
    }

    /**
     * 方法参数与局部变量测试入口。
     *
     * @param mParameter 方法参数样例
     */
    private void pojoMethod(SimpleTestPOJO<String> mParameter) {
        SimpleTestPOJO<Data> localVariable = new SimpleTestPOJO<>();
    }
}

/**
 * 泛型简单对象测试样例。
 * 
 * @author August Lee
 * @see SimpleTestPOJO
 * @since 2026-05-15 15:29:08
 *
 */
class SimpleTestPOJO<T> {

    private int anInt = 0;

    private String string = "";

    private BigDecimal bigDecimal = BigDecimal.ZERO;

    private int[] ints = {0};

    private Set<Integer> linkedHashSet = new LinkedHashSet<>();

    private T data;
}

/**
 * 嵌套对象测试样例。
 * 
 * @author August Lee
 * @see Data
 * @since 2026-05-15 15:29:08
 *
 */
class Data {

    @JsonIgnore
    private String username;
    private String password;
}
