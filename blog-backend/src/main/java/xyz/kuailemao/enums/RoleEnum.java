package xyz.kuailemao.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/11/6 11:11
 */
@Getter
@AllArgsConstructor
public enum RoleEnum {

    Role_STATUS_ARTICLE(0, "状态：正常"),
    ROLE_STATUS_ARTICLE(1, "状态：停用");

    // 类型
    private final Integer status;
    // 描述
    private final String desc;
}
