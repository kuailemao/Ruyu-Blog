package xyz.kuailemao.enums;

import cn.hutool.core.date.DateField;
import lombok.Getter;

/**
 * 黑名单策略
 * @author kuailemao
 * @since 2024-10-10 18:47:33
 */
@Getter
public enum BlackListPolicy {
    DDOS_ATTACK_10_YEARS(200, DateField.YEAR, 10, "非法DDOS攻击，已被封禁十年，有问题联系网站管理员", "疑似非法DDOS攻击\nIP:{}\n地址:{}\n请求次数:{}"),
    DDOS_ATTACK_1_MONTH(100, DateField.MONTH, 1, "疑似非法DDOS攻击，已被封禁一个月，有问题联系网站管理员", "疑似非法DDOS攻击\nIP:{}\n地址:{}\n请求次数:{}"),
    TOO_MANY_REQUESTS_1_HOUR(60, DateField.HOUR, 1, "请求过于频繁,已被封禁一小时，有问题联系网站管理员", "疑似非法DDOS攻击\nIP:{}\n地址:{}\n请求次数:{}");

    private final int requestThreshold;
    private final DateField timeUnit;
    private final int duration;
    private final String message;
    private final String reason;

    BlackListPolicy(int requestThreshold, DateField timeUnit, int duration, String message,String reason) {
        this.requestThreshold = requestThreshold;
        this.timeUnit = timeUnit;
        this.duration = duration;
        this.message = message;
        this.reason = reason;
    }
}

// 使用枚举类的示例代码
//for (BlackListPolicy policy : BlackListPolicy.values()) {
//    if (count == policy.getRequestThreshold()) {
//        return handleBlackList(policy.getTimeUnit(), policy.getDuration(), expireTime, timestampByIP,
//                policy.getMessage() + "\nIP:" + ip + "\n地址:" + uri + "\n请求次数:" + count, response);
//    }
//}