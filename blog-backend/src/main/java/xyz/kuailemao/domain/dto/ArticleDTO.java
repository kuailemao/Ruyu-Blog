package xyz.kuailemao.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import xyz.kuailemao.domain.BaseData;

import java.util.List;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2024/1/4 14:11
 */
@Data
public class ArticleDTO implements BaseData {
    // 文章id
    private Long id;
    //分类id
    @NotNull(message = "分类id不能为空")
    private Long categoryId;
    // 标签id
    @NotNull(message = "标签id不能为空")
    private List<Long> tagId;
    //文章缩略图
    @NotNull(message = "文章缩略图不能为空")
    private String articleCover;
    //文章标题
    @NotNull(message = "文章标题不能为空")
    private String articleTitle;
    //文章内容
    @NotNull(message = "文章内容不能为空")
    private String articleContent;
    //类型 (1原创 2转载 3翻译)
    @NotNull(message = "文章类型不能为空")
    private Integer articleType;
    //是否置顶 (0否 1是）
    @NotNull(message = "是否置顶不能为空")
    private Integer isTop;
    //文章状态 (1公开 2私密 3草稿)
    @NotNull(message = "文章状态不能为空")
    private Integer status;
}
