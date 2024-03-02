package xyz.kuailemao.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/11/12 16:02
 */
@Data
@Builder
@Schema(name = "ChatSessionDetailVO", description = "会话详情VO")
public class ChatSessionDetailVO {
    @Schema(description = "会话id")
    private Long id;
    @Schema(description = "会话内容")
    private String conversation;
    @Schema(description = "会话创建时间")
    private Date createTime;
}
