package xyz.kuailemao.annotation;

import java.lang.annotation.*;

/**
 * @author kuailemao
 * @since 2024/9/5 下午10:10
 * 封禁验证注解
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckBlacklist {
}
