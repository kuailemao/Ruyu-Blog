package xyz.kuailemao.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import xyz.kuailemao.constants.SecurityConst;
import xyz.kuailemao.filter.JwtAuthorizeFilter;
import xyz.kuailemao.handler.SecurityHandler;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/10/10 16:26
 */
@Configuration
public class SecurityConfiguration {

    @Resource
    private SecurityHandler securityHandler;

    @Resource
    private JwtAuthorizeFilter jwtAuthorizeFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(conf -> conf
                        // 需要校验的接口
                        .requestMatchers(SecurityConst.AUTH_CHECK_ARRAY).authenticated()
                        // 注册接口
                        // 其他的都不用要校验
                        .anyRequest().permitAll()
                )
                .formLogin(conf -> conf
                        // 登录页面
                        .loginProcessingUrl(SecurityConst.LOGIN_PAGE)
                        // 成功登录处理
                        .successHandler(securityHandler::onAuthenticationSuccess)
                        // 失败登录处理
                        .failureHandler(securityHandler::onAuthenticationFailure)
                )
                .logout(conf -> conf
                        // 登出页面
                        .logoutUrl(SecurityConst.LOGOUT_PAGE)
                        // 退出登录处理
                        .logoutSuccessHandler(securityHandler::onLogoutSuccess)
                )
                .exceptionHandling(conf -> conf
                        // 没有登录处理
                        .authenticationEntryPoint(securityHandler::onUnAuthenticated)
                        // 没有权限处理
                        .accessDeniedHandler(securityHandler::onAccessDeny)
                )
                // 禁用 csrf
                .csrf(AbstractHttpConfigurer::disable)
                // 不处理 session ，使用token
                .sessionManagement(conf -> conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // token 校验添加过滤器
                .addFilterBefore(jwtAuthorizeFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
