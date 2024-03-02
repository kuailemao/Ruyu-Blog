package xyz.kuailemao.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/12/25 9:20
 */
@Getter
@AllArgsConstructor
public enum UrlEnum {

    /**
     * Gitee解析Token获取个人信息
     */
    GITEE_USER_INFO("https://gitee.com/api/v5/user", "GET", "Gitee解析Token获取个人信息"),

    /**
     * Github解析Token获取个人信息
     */
    GITHUB_USER_INFO("https://api.github.com/user", "GET", "Github解析Token获取个人信息");

    /**
     * url
     */
    private final String url;

    /**
     * 请求方法
     */
    private final String method;

    /**
     * 描述
     */
    private final String description;
}
