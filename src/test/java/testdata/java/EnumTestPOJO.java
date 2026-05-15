package testdata.java;

/**
 * 枚举类型测试样例。
 * 
 * @author August Lee
 * @see EnumTestPOJO
 * @since 2026-05-15 15:29:08
 *
 */
public class EnumTestPOJO {

    private Type type = Type.TYPE_A;

    /**
     * 枚举值测试样例。
     * 
     * @author August Lee
     * @see Type
     * @since 2026-05-15 15:29:08
     *
     */
    public enum Type {
        TYPE_A,
        TYPE_B,
        TYPE_C;
    }
}
