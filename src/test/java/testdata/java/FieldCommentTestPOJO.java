package testdata.java;

import java.util.List;

/**
 * 字段注释复制测试样例。
 * 
 * @author August Lee
 * @see FieldCommentTestPOJO
 * @since 2026-05-15 18:29:08
 *
 */
public class FieldCommentTestPOJO {

    /**
     * 用户名称
     */
    private String username;

    /**
     * 角色列表
     */
    private List<Role> roles;

    /**
     * 嵌套角色对象测试样例。
     * 
     * @author August Lee
     * @see Role
     * @since 2026-05-15 18:29:08
     *
     */
    public static class Role {

        /**
         * 角色编码
         */
        private Integer roleId;
    }
}
