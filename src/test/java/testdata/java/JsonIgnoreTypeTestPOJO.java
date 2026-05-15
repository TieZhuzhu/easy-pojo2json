package testdata.java;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

import java.util.List;

/**
 * 忽略整个类型注解测试样例。
 * 
 * @author August Lee
 * @see JsonIgnoreTypeTestPOJO
 * @since 2026-05-15 15:29:08
 *
 */
public class JsonIgnoreTypeTestPOJO {

    private String username;
    private List<Role> roles;

    /**
     * 被标记为忽略类型的角色对象样例。
     * 
     * @author August Lee
     * @see Role
     * @since 2026-05-15 15:29:08
     *
     */
    @JsonIgnoreType
    public class Role {
        private String roleName;
        private List<JsonIgnoreTypeTestPOJO> users;
    }
}
