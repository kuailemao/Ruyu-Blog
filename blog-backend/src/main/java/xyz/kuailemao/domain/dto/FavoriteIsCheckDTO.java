package xyz.kuailemao.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2024/1/21 9:25
 */
@Data
public class FavoriteIsCheckDTO {
    // 收藏id
    @NotNull(message = "收藏id不能为空")
    private Long id;
    // 是否通过
    @NotNull(message = "是否有效不能为空")
    private Integer isCheck;
}
