package xyz.kuailemao.constants;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/10/11 20:27
 */
public class RespConst {

    /**
     * 登录成功提示
     */
    public static final String SUCCESS_LOGIN_MSG = "登录成功";

    /**
     * 请先获取验证码
     */
    public static final String VERIFY_CODE_NULL_MSG = "请先获取验证码";

    /**
     * 用户名或密码错误
     */
    public static final String USERNAME_OR_PASSWORD_ERROR_MSG = "用户名或密码错误";

    /**
     * 账号被停用
     */
    public static final String ACCOUNT_DISABLED_MSG = "该账号已被停用，无法登录";

    /**
     * 测试账号
     */
    public static final String TEST_ACCOUNT_MSG = "该账号为后台测试账号，无法登录前台";

    /**
     * 账号无权限（后台）
     */
    public static final String NO_PERMISSION_MSG = "该账号不具备任何权限，无法登录";

    /**
     * Token 前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * Token 请求头
     */
    public static final String TOKEN_HEADER = "Authorization";

    /**
     * Banner数量已达上限
     */
    public static final String BANNER_MAX_COUNT_MSG = "首页banner数量已达上限";

}
