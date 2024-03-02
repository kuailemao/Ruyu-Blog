package xyz.kuailemao.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 *  @author kuailemao
 * 
 * 创建时间：2024/1/22 12:47
 *
 */
@Data
public class CommentIsCheckDTO {
    // 收藏id
    @NotNull(message = "评论id不能为空")
    private Long id;
    // 是否通过
    @NotNull(message = "是否通过不能为空")
    private Integer isCheck;
}
