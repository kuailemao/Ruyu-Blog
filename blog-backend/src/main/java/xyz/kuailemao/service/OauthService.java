package xyz.kuailemao.service;

import jakarta.servlet.http.HttpServletRequest;
import me.zhyd.oauth.model.AuthResponse;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/12/21 17:09
 */
public interface OauthService {

    /**
     * 处理第三方登录
     *
     * @param authResponse 授权响应
     * @param request      请求
     * @param type         登录类型
     * @return 响应结果
     */
    String handleLogin(AuthResponse authResponse, HttpServletRequest request,Integer type);
}
