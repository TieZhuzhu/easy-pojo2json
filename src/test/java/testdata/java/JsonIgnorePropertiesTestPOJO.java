package testdata.java;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * 忽略子属性注解测试样例。
 */
public class JsonIgnorePropertiesTestPOJO {

    private String username;
    @JsonIgnoreProperties({"users", "aaa", "bbb"})
    private List<Role> roles;

    /**
     * 嵌套角色对象测试样例。
     */
    public class Role {

        private String roleName;
        private List<JsonIgnorePropertiesTestPOJO> users;
    }
}
