package xyz.kuailemao.domain.vo;

import lombok.Data;

/**
 * @author kuailemao
 * @since 2024/10/24 17:31
 * &#064;description  初始化搜索标题数据VO
 */
@Data
public class InitSearchTitleVO {
    //文章id
    private Long id;
    // 分类Name
    private String categoryName;
    //访问量
    private Long visitCount;
    //文章标题
    private String articleTitle;
}
