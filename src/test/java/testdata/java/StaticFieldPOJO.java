package testdata.java;


import lombok.Data;

import java.io.Serializable;

/**
 * 静态字段过滤测试样例。
 * 
 * @author August Lee
 * @see StaticFieldPOJO
 * @since 2026-05-15 15:29:08
 *
 */
@Data
public class StaticFieldPOJO implements Serializable {

    private static final long serialVersionUID = 0L;


    public static int thisStatic = 0;

    private final int thisFinal = 0;

    private transient int thisTransient = 0;


    private static final int thisStaticFinal = 0;

    private static transient int thisStaticTransient = 0;


    private final transient int thisFinalTransient = 0;

    private static final transient int thisStaticFinalTransient = 0;
}
