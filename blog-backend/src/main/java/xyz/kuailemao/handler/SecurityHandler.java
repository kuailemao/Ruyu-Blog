package xyz.kuailemao.handler;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import xyz.kuailemao.constants.Const;
import xyz.kuailemao.constants.RespConst;
import xyz.kuailemao.domain.entity.LoginUser;
import xyz.kuailemao.domain.response.ResponseResult;
import xyz.kuailemao.domain.vo.AuthorizeVO;
import xyz.kuailemao.enums.RespEnum;
import xyz.kuailemao.service.LoginLogService;
import xyz.kuailemao.service.UserService;
import xyz.kuailemao.utils.JwtUtils;
import xyz.kuailemao.utils.RedisCache;
import xyz.kuailemao.utils.StringUtils;
import xyz.kuailemao.utils.WebUtil;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/10/11 16:03
 */
@Component
public class SecurityHandler {

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private RedisCache redisCache;

    @Resource
    private LoginLogService loginLogService;

    @Resource
    private UserService userService;

    public static final String USER_NAME = "username";


    /**
     * 登录成功处理
     *
     * @param request        请求
     * @param response       响应
     * @param authentication 认证信息
     */
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        handlerOnAuthenticationSuccess(request,response,(LoginUser)authentication.getPrincipal());
    }

    public void handlerOnAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            LoginUser user
    ) {
        String typeHeader = request.getHeader(Const.TYPE_HEADER);
        if ((!StringUtils.matches(typeHeader, List.of(Const.BACKEND_REQUEST, Const.FRONTEND_REQUEST)) && user.getUser().getRegisterType() == 1)) {
            throw new BadCredentialsException("非法请求");
        }
        Long id = user.getUser().getId();
        String name = user.getUser().getUsername();
        // UUID做jwt的id
        String uuid = UUID.randomUUID().toString();
        // 生成jwt
        String token = jwtUtils.createJwt(uuid, user, id, name);

        // 转换VO
        AuthorizeVO authorizeVO = user.getUser().asViewObject(AuthorizeVO.class, v -> {
            v.setToken(token);
            v.setExpire(jwtUtils.expireTime());
        });
        userService.userLoginStatus(user.getUser().getId(), user.getUser().getRegisterType());
        loginLogService.loginLog(request, request.getParameter(USER_NAME), 0, RespConst.SUCCESS_LOGIN_MSG);
        WebUtil.renderString(response, ResponseResult.success(authorizeVO, RespConst.SUCCESS_LOGIN_MSG).asJsonString());
    }


    /**
     * 登录失败处理
     */
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {
        loginLogService.loginLog(request, request.getParameter(USER_NAME), 1, exception.getMessage());
        WebUtil.renderString(response, ResponseResult.failure(RespEnum.USERNAME_OR_PASSWORD_ERROR.getCode(), exception.getMessage()).asJsonString());
    }

    /**
     * 退出登录处理
     */
    public void onLogoutSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        boolean invalidateJwt = jwtUtils.invalidateJwt(request.getHeader("Authorization"));
        if (invalidateJwt) {
            WebUtil.renderString(response, ResponseResult.success().asJsonString());
            return;
        }
        WebUtil.renderString(response, ResponseResult.failure(RespEnum.NOT_LOGIN.getCode(), RespEnum.NOT_LOGIN.getMsg()).asJsonString());
    }

    /**
     * 没有登录处理
     */
    public void onUnAuthenticated(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {
        WebUtil.renderString(response, ResponseResult.failure(RespEnum.NOT_LOGIN.getCode(), RespEnum.NOT_LOGIN.getMsg()).asJsonString());
    }

    /**
     * 没有权限处理
     */
    public void onAccessDeny(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException exception
    ) {
        WebUtil.renderString(response, ResponseResult.failure(RespEnum.NO_PERMISSION.getCode(), RespEnum.NO_PERMISSION.getMsg()).asJsonString());
    }
}
