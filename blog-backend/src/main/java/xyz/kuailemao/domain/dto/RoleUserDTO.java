package xyz.kuailemao.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/12/7 20:18
 */
@Data
public class RoleUserDTO {
    @NotNull(message = "用户不能为空")
    private List<Long> userId;
    @NotNull(message = "选择的角色不能为空")
    private List<Long> roleId;
}
