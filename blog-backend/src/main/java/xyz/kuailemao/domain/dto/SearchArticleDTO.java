package xyz.kuailemao.domain.dto;

import lombok.Data;
import xyz.kuailemao.domain.BaseData;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2024/1/7 19:22
 */
@Data
public class SearchArticleDTO implements BaseData {
    // 分类id
    private Long categoryId;
    //文章标题
    private String articleTitle;
    //文章状态 (1公开 2私密 3草稿)
    private Integer status;
    //是否置顶 (0否 1是）
    private Integer isTop;
}
