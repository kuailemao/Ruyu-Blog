package xyz.kuailemao.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2024/1/22 20:45
 */
@Data
public class LinkIsCheckDTO {
    // 友链id
    @NotNull(message = "友链id不能为空")
    private Long id;
    // 是否通过
    @NotNull(message = "是否通过不能为空")
    private Integer isCheck;
}
