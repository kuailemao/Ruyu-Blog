package xyz.kuailemao.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/10/20 9:41
 */
@Getter
@AllArgsConstructor
public enum RespEnum {

    /**
     * 请求成功
     */
    SUCCESS(200, "success"),
    /**
     * 请求失败
     */
    FAILURE(500, "failure"),
    /**
     * 用户名或密码错误
     */
    USERNAME_OR_PASSWORD_ERROR(1001, "用户名或密码错误"),
    /**
     * 未登录提示
     */
    NOT_LOGIN(1002, "请先登录"),
    /**
     * 没有权限
     */
    NO_PERMISSION(1003, "没有权限"),
    /**
     * 请求频繁
     */
    REQUEST_FREQUENTLY(1004, "请求频繁"),
    /**
     * 验证码错误
     */
    VERIFY_CODE_ERROR(1005, "验证码错误"),
    /**
     * 用户名或邮箱已存在
     */
    USERNAME_OR_EMAIL_EXIST(1006, "用户名或邮箱已存在"),
    /**
     * 参数错误提示
     */
    PARAM_ERROR(1007, "参数错误"),
    /**
     * 其他故障
     */
    OTHER_ERROR(1008, "其他故障"),
    /**
     * 会话数量已达上限
     */
    SESSION_LIMIT(1009, "会话数量已达上限"),
    /**
     * 未删除子菜单
     */
    NO_DELETE_CHILD_MENU(1010, "请先删除子菜单"),
    /**
     * 文件上传错误
     */
    FILE_UPLOAD_ERROR(1011, "文件上传错误"),
    /**
     * 账号被封禁
     */
    BLACK_LIST_ERROR(1012, "账号被封禁");



    /**
     * code
     */
    private final Integer code;

    /**
     * Msg
     */
    private final String msg;

}
