package xyz.kuailemao.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import xyz.kuailemao.domain.entity.LoginUser;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/10/12 0:01
 *
 * Security获取相关信息工具类
 */
public class SecurityUtils {

    /**
     * 获取当前登录用户id
     * @return 用户id
     */
    public static Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof LoginUser user) {
            return user.getUser().getId();
        }
        return 0L; // 如果无法获取用户ID，返回默认值
    }

    /**
     * 判断用户是否登录
     * @return 是否登录
     */
    public static boolean isLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getPrincipal() instanceof LoginUser;
    }

    /**
     * 获取当前登录用户的角色
     * @return 角色列表
     */
    public static List<String> getUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList(); // 如果无法获取用户角色，返回空列表
    }

    /**
     * 获取request
     * @return request
     */
    public static HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            return requestAttributes.getRequest();
        }
        return null;
    }

}
