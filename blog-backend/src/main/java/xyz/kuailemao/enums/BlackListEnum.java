package xyz.kuailemao.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author kuailemao
 * @since 2024/9/12 下午4:40
 * 黑名单枚举类
 */
@Getter
@AllArgsConstructor
public enum BlackListEnum {

    // 是否封禁
    IS_BANNED(0, "封禁"),
    IS_NOT_BANNED(1, "未封禁");

    private final Integer code;
    private final String desc;
}
