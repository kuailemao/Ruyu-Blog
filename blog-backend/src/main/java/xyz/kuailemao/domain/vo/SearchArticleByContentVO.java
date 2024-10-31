package xyz.kuailemao.domain.vo;

import lombok.Data;

/**
 * @author kuailemao
 * @since 2024/10/30 11:50
 * 按照文章内容搜索文章
 */
@Data
public class SearchArticleByContentVO {
    //文章id
    private Long id;
    //文章标题
    private String articleTitle;
    //访问量
    private Long visitCount;
    //分类名称
    private String categoryName;
    // 内容
    private String articleContent;
}
