package testdata.java;


import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 字段忽略注解测试样例。
 */
public class JsonIgnoreTestPOJO {

    @JsonIgnore
    private String username;
    private String password;
}
