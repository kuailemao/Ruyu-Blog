package xyz.kuailemao.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/10/30 15:09
 */
@Data
public class TreeHoleVO {
    //用户昵称
    @Schema(description = "用户昵称")
    private String nickname;
    //用户头像
    @Schema(description = "用户头像")
    private String avatar;
    //内容
    @Schema(description = "内容")
    private String content;
}
