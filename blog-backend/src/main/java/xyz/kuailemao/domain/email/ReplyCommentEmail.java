package xyz.kuailemao.domain.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.kuailemao.domain.BaseData;

import java.io.Serializable;

/**
 * @author kuailemao
 * @since 2024/8/8 上午12:31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyCommentEmail implements Serializable {
    // id
    private Long id;
    // 类型 1:文章评论 2:友链评论
    private Integer type;
    // 类型ID
    private Long typeId;
    // 评论标题
    private String title;
    // 文章地址
    private String url;
    // 评论人头像
    private String avatar;
    // 评论人昵称
    private String nickname;
    // 评论内容
    private String content;
    // 评论时间
    private String time;
    // 回复人头像
    private String replyAvatar;
    // 回复人昵称
    private String replyNickname;
    // 回复内容
    private String replyContent;
    // 回复时间
    private String replyTime;
}
