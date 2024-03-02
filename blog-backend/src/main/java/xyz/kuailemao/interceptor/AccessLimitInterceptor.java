package xyz.kuailemao.interceptor;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import xyz.kuailemao.annotation.AccessLimit;
import xyz.kuailemao.constants.RespConst;
import xyz.kuailemao.domain.response.ResponseResult;
import xyz.kuailemao.enums.RespEnum;
import xyz.kuailemao.utils.RedisCache;
import xyz.kuailemao.utils.WebUtil;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Redis限流拦截器
 */
@Slf4j
@Component
public class AccessLimitInterceptor implements HandlerInterceptor {

    @Resource
    private RedisCache redisCache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean result = true;
        // 是否是HandlerMethod实例
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);

            // 方法上面如果没有限流注解就直接通过
            if (accessLimit == null)
                return result;
            // 如果方法上有限流注解
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            String ip = request.getRemoteAddr();
            String method = request.getMethod();
            String uri = request.getRequestURI();
            String key = "limit:" + method + ":" + uri + ":" + ip;

            try {
                // redis 进行自增
                Long count = redisCache.increment(key, 1L);
                if (Objects.nonNull(count) && count == 1) {
                    // 第一次访问，设置过期时间
                    redisCache.expire(key, seconds, TimeUnit.SECONDS);
                } else if (count > maxCount) {
                    WebUtil.renderString(response, ResponseResult.failure(RespEnum.REQUEST_FREQUENTLY.getCode(), accessLimit.msg()).asJsonString());
                    // 限制
                    log.warn("用户IP[" + ip + "]访问地址[" + uri + "]超过了限定的次数[" + maxCount + "]");
                    result = false;
                }
            } catch (RedisConnectionFailureException e) {
                log.error("redis连接异常", e);
                result = false;
            }
        }

        return result;
    }
}
