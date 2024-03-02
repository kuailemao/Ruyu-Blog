package xyz.kuailemao.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2024/1/19 21:24
 */
@Data
public class TreeHoleIsCheckDTO {
    // 树洞id
    @NotNull(message = "树洞id不能为空")
    private Long id;
    // 是否通过
    @NotNull(message = "是否通过不能为空")
    private Integer isCheck;
}
