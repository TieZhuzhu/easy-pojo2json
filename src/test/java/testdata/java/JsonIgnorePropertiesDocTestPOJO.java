package testdata.java;


import java.util.List;

/**
 * JavaDoc 版忽略子属性测试样例。
 * 
 * @author August Lee
 * @see JsonIgnorePropertiesDocTestPOJO
 * @since 2026-05-15 15:29:08
 *
 */
public class JsonIgnorePropertiesDocTestPOJO {

    private String username;
    /**
     * @JsonIgnoreProperties users, aaa, bbb
     */
    private List<Role> roles;

    /**
     * 嵌套角色对象测试样例。
     * 
     * @author August Lee
     * @see Role
     * @since 2026-05-15 15:29:08
     *
     */
    public class Role {

        private String roleName;
        private List<JsonIgnorePropertiesDocTestPOJO> users;
    }
}
