package xyz.kuailemao.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/10/15 2:37
 * 文章卡片VO
 */
@Data
@Schema(name = "ArticleVO", description = "文章卡片VO")
public class ArticleVO {
    //文章id
    @Schema(description = "文章id")
    private Long id;
    //分类名称
    @Schema(description = "分类名称")
    private String categoryName;
    // 文章标签
    @Schema(description = "文章标签")
    private List<String> tags;
    //文章缩略图
    @Schema(description = "文章缩略图")
    private String articleCover;
    //文章标题
    @Schema(description = "文章标题")
    private String articleTitle;
    //文章内容
    @Schema(description = "文章内容")
    private String articleContent;
    //类型 (1原创 2转载 3翻译)
    @Schema(description = "类型 (1原创 2转载 3翻译)")
    private Integer articleType;
    //访问量
    @Schema(description = "访问量")
    private Long visitCount;
    //评论数
    @Schema(description = "评论数")
    private Long commentCount;
    //点赞数
    @Schema(description = "点赞数")
    private Long likeCount;
    //收藏数
    @Schema(description = "收藏数")
    private Long favoriteCount;
    //文章创建时间
    @Schema(description = "文章创建时间")
    private Date createTime;
    //文章更新时间
    @Schema(description = "文章更新时间")
    private Date updateTime;
}
