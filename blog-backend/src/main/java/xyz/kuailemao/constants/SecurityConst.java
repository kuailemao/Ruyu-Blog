package xyz.kuailemao.constants;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/10/11 15:10
 */
public class SecurityConst {

    /**
     * 需要校验的用户接口
     */
    public static final String AUTH_CHECK = "user/auth/**";

    /**
     * 需要校验的文章接口
     */
    public static final String ARTICLE_CHECK = "article/auth/**";

    /**
     * 需要校验的树洞接口
     */
    public static final String TREE_HOLE_CHECK = "/treeHole/auth/**";

    /**
     * 需要校验的评论接口
     */
    public static final String COMMENT_CHECK = "/comment/auth/**";
    /**
     * 需要校验的收藏接口
     */
    public static final String FAVORITE_CHECK = "/favorite/auth/**";
    /**
     * 需要校验的点赞接口
     */
    public static final String LIKE_CHECK = "/like/auth/**";
    /**
     * 需要校验的留言板接口
     */
    public static final String LEAVE_WORD_CHECK = "/leaveWord/auth/**";

    /**
     * 需要校验的gpt聊天接口
     */
    public static final String CHAT_GPT_CHECK = "/chatGpt/auth/**";

    /**
     * 需要校验的友链接口
     */
    public static final String LINK_CHECK = "/link/auth/**";

    /**
     * 需要校验的菜单接口
     */
    public static final String MENU_CHECK = "/menu/**";

    /**
     * 需要校验的角色接口
     */
    public static final String ROLE_CHECK = "/role/**";

    /**
     * 需要校验的权限接口
     */
    public static final String PERMISSION_CHECK = "/permission/**";

    /*
      注意：Controller方法上面添加了@PreAuthorize("hasAnyAuthority('blog:banner:delete')")后会自动进行登录权限校验
     */

    /**
     * 数组常量
     */
    public static final String[] AUTH_CHECK_ARRAY = {
            AUTH_CHECK,
            ARTICLE_CHECK,
            TREE_HOLE_CHECK,
            COMMENT_CHECK,
            FAVORITE_CHECK,
            LIKE_CHECK,
            LEAVE_WORD_CHECK,
            CHAT_GPT_CHECK,
            LINK_CHECK,
            MENU_CHECK,
            ROLE_CHECK,
            PERMISSION_CHECK
    };

    /**
     * 登录页面
     */
    public static final String LOGIN_PAGE = "/user/login";

    /**
     * 退出登录页面
     */
    public static final String LOGOUT_PAGE = "/user/logout";
    /**
     * 角色前缀
     */
    public static final String ROLE_PREFIX = "ROLE_";

    /**
     * 测试账号
     */
    public static final String ROLE_TESTER = "Test";
}
