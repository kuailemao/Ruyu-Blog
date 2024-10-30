package xyz.kuailemao.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/10/16 9:50
 * 随机文章VO
 */
@Data
@Schema(name = "RandomArticleVO", description = "随机文章VO")
public class RandomArticleVO {
    //文章id
    @Schema(description = "文章id")
    private Long id;
    //文章缩略图
    @Schema(description = "文章缩略图")
    private String articleCover;
    //文章标题
    @Schema(description = "文章标题")
    private String articleTitle;
    // 文章热度
    @Schema(description = "文章访问量")
    private Long visitCount;
    //文章创建时间
    @Schema(description = "文章创建时间")
    private Date createTime;
}
