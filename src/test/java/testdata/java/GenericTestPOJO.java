package testdata.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 泛型与嵌套泛型测试样例。
 */
public class GenericTestPOJO {

    private List listNonGeneric = new ArrayList();

    private Map mapNonGeneric = new HashMap();


    private List<Integer> list = new ArrayList<>();

    private List<Integer[]> listArr = new ArrayList<>();

    private List<List<Integer>[]> listListArr = new ArrayList<>();

    private List<Type> listEnum = new ArrayList<>();

    private List<List<Integer>> listList = new ArrayList<>();

    private List<List<List<Integer>>> listListList = new ArrayList<>();

    private List<Object> listObject = new ArrayList<>();

    private List<?> listGenericObject = new ArrayList<>();

    // --

    private Generic<Integer> generic = new Generic<>();

    private Generic<Integer[]> genericArr = new Generic<>();

    private Generic<List<Integer>[]> genericListArr = new Generic<>();

    private Generic<Type> genericEnum = new Generic<>();

    private Generic<Generic<Integer>> genericGeneric = new Generic<>();

    private Generic<Generic<Generic<Integer>>> genericGenericGeneric = new Generic<>();

    private Generic<Object> genericObject = new Generic<>();

    private Generic<?> genericGenericObject = new Generic<>();

    /**
     * 单泛型内部类测试样例。
     *
     * @param <T> 泛型数据类型
     */
    public class Generic<T> {

        private String username;

        private T data;
    }

    /**
     * 枚举泛型测试样例。
     */
    public enum Type {
        TYPE_A,
        TYPE_B,
        TYPE_C;
    }

    private Generics nonGenerics = new Generics();

    private Generics<String, Integer, List> generics = new Generics<>();

    /**
     * 多泛型内部类测试样例。
     *
     * @param <A> 泛型 A
     * @param <B> 泛型 B
     * @param <C> 泛型 C
     */
    public class Generics<A, B, C> {
        private A a;
        private B b;
        private C c;
    }
}
