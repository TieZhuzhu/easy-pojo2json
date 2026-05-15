package testdata.java;


import java.util.List;

/**
 * JavaDoc 版忽略子属性测试样例。
 */
public class JsonIgnorePropertiesDocTestPOJO {

    private String username;
    /**
     * @JsonIgnoreProperties users, aaa, bbb
     */
    private List<Role> roles;

    /**
     * 嵌套角色对象测试样例。
     */
    public class Role {

        private String roleName;
        private List<JsonIgnorePropertiesDocTestPOJO> users;
    }
}
