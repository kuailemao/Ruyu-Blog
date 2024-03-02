package xyz.kuailemao.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/11/3 15:15
 */
@Data
@Accessors(chain = true)
public class LeaveWordVO {
    //id
    private Long id;
    // 留言用户id
    private Long userId;
    //留言内容
    private String content;
    //留言时间
    private Date createTime;
    // 用户昵称
    private String nickname;
    // 用户头像
    private String avatar;
    // 留言评论数量
    private Long commentCount;
    // 留言点赞数量
    private Long likeCount;
    // 留言收藏数量
    private Long favoriteCount;
}
