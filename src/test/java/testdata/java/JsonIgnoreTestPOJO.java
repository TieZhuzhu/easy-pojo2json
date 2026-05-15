package testdata.java;


import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 字段忽略注解测试样例。
 * 
 * @author August Lee
 * @see JsonIgnoreTestPOJO
 * @since 2026-05-15 15:29:08
 *
 */
public class JsonIgnoreTestPOJO {

    @JsonIgnore
    private String username;
    private String password;
}
