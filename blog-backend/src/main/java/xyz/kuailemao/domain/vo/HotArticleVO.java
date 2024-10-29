package xyz.kuailemao.domain.vo;

import lombok.Data;

/**
 * @author kuailemao
 * @since 2024/10/29 17:56
 * 热门推荐VO
 */
@Data
public class HotArticleVO {
    //文章id
    private Long id;
    //文章标题
    private String articleTitle;
    //访问量
    private Long visitCount;
}
