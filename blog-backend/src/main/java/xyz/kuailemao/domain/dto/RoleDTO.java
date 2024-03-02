package xyz.kuailemao.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import xyz.kuailemao.domain.BaseData;

import java.util.List;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/12/3 17:17
 */
@Accessors(chain = true)
@Data
public class RoleDTO implements BaseData {
    //角色id
    private Long id;
    // 角色名称
    @NotNull
    private String roleName;
    //角色字符
    @NotNull
    private String roleKey;
    // 状态（0：正常，1：停用）
    @NotNull
    private Integer status;
    // 顺序
    @NotNull
    private Long orderNum;
    // 备注
    private String remark;
    // 权限菜单id
    private List<Long> menuIds;
}
