package testdata.java;

/**
 * 成员内部类解析测试样例。
 * 
 * @author August Lee
 * @see MemberClassTestPOJO
 * @since 2026-05-15 15:29:08
 *
 */
public class MemberClassTestPOJO {

    private String test;

    /**
     * 内部类 A 测试样例。
     * 
     * @author August Lee
     * @see A
     * @since 2026-05-15 15:29:08
     *
     */
    public class A {

        private String a;
    }

    /**
     * 内部类 B 测试样例。
     * 
     * @author August Lee
     * @see B
     * @since 2026-05-15 15:29:08
     *
     */
    public class B {

        private String b;
    }
}
