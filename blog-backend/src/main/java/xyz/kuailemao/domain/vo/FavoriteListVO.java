package xyz.kuailemao.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2024/1/21 9:20
 */
@Data
public class FavoriteListVO {
    //收藏id
    private Long id;
    //收藏的用户姓名
    private String userName;
    //收藏类型(1,文章 2,留言板)
    private Integer type;
    // 收藏内容
    private String content;
    // 是否有效 (0否 1是)
    private Integer isCheck;
    //收藏时间
    private Date createTime;
}
