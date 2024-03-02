package xyz.kuailemao.domain.vo;

import lombok.Data;
import xyz.kuailemao.domain.BaseData;

import java.util.Date;
import java.util.List;


/**
 * 后台文章列表VO
 */
@Data
public class ArticleListVO implements BaseData {
    //文章id
    private Long id;
    // 分类id
    private Long categoryId;
    // 作者id
    private Long userId;
    // 作者名称
    private String userName;
    //类型 (1原创 2转载 3翻译)
    private Integer articleType;
    //分类名称
    private String categoryName;
    // 文章标签名称
    private List<String> tagsName;
    //文章缩略图
    private String articleCover;
    //文章标题
    private String articleTitle;
    //是否置顶 (0否 1是）
    private Integer isTop;
    //文章状态 (1公开 2私密 3草稿)
    private Integer status;
    //访问量
    private Long visitCount;
    //文章创建时间
    private Date createTime;
}

