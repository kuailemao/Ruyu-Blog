package xyz.kuailemao.domain.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/12/4 10:18
 */
@Data
public class RoleSearchDTO {
    // 角色名称
    private String roleName;
    //角色字符
    private String roleKey;
    // 状态（0：正常，1：停用）
    private Integer status;
    //创建时间，开始时间
    private Date createTimeStart;
    //创建时间，结束时间
    private Date createTimeEnd;
}
