package xyz.kuailemao.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2024/1/16 9:55
 * 留言是否通过
 */
@Data
public class LeaveWordIsCheckDTO {
    // 留言id
    @NotNull(message = "留言id不能为空")
    private Long id;
    // 是否通过
    @NotNull(message = "是否通过不能为空")
    private Integer isCheck;
}
