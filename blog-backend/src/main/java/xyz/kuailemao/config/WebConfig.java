package xyz.kuailemao.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xyz.kuailemao.interceptor.AccessLimitInterceptor;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/10/16 19:56
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private AccessLimitInterceptor accessLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // redis限流拦截器
        registry.addInterceptor(accessLimitInterceptor).addPathPatterns("/**");
    }
}
