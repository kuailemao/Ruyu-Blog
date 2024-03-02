package xyz.kuailemao.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/10/27 15:17
 * 分类下的文章vo
 */
@Data
@Schema(name = "CategoryArticleVO", description = "分类下的文章vo")
public class CategoryArticleVO {
    //文章id
    @Schema(description = "文章id")
    private Long id;
    //分类名称
    @Schema(description = "分类id")
    private Long categoryId;
    // 文章标签
    @Schema(description = "文章标签")
    private List<TagVO> tags;
    //文章缩略图
    @Schema(description = "文章缩略图")
    private String articleCover;
    //文章标题
    @Schema(description = "文章标题")
    private String articleTitle;
    //访问量
    @Schema(description = "访问量")
    private Long visitCount;
    //文章创建时间
    @Schema(description = "文章创建时间")
    private Date createTime;
}
