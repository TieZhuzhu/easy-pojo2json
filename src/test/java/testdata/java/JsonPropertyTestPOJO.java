package testdata.java;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 字段重命名注解测试样例。
 * 
 * @author August Lee
 * @see JsonPropertyTestPOJO
 * @since 2026-05-15 15:29:08
 *
 */
public class JsonPropertyTestPOJO {

    @JsonProperty
    private String userId;

    @JsonProperty("name")
    private String username;

    @JSONField(name = "pass")
    private String password;
}
