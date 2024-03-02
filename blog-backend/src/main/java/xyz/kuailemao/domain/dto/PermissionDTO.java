package xyz.kuailemao.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import xyz.kuailemao.domain.BaseData;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/12/6 9:16
 */
@Accessors(chain = true)
@Data
public class PermissionDTO implements BaseData {
    //权限表id
    private Integer id;
    //描述
    @NotNull(message = "权限描述不能为空")
    private String permissionDesc;
    //权限字符
    @NotNull(message = "权限字符不能为空")
    private String permissionKey;
    // 所在菜单
    @NotNull(message = "所在菜单不能为空")
    private Long permissionMenuId;
}
