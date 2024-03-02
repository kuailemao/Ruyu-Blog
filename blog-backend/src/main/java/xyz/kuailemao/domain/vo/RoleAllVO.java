package xyz.kuailemao.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/11/30 15:34
 */
@Data
public class RoleAllVO {
    //角色id
    private Long id;
    // 角色名称
    private String roleName;
    //角色字符
    private String roleKey;
    // 状态（0：正常，1：停用）
    private Integer status;
    // 顺序
    private Long orderNum;
    //创建时间
    private Date createTime;
}
