package xyz.kuailemao.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/12/5 11:37
 */
@Data
public class UserRoleDTO {
    @NotNull(message = "角色不能为空")
    private Long roleId;
    @NotNull(message = "选择的用户不能为空")
    private List<Long> userId;
}
