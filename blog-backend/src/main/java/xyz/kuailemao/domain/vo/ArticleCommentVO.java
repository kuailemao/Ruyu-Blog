package xyz.kuailemao.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/10/19 20:09
 * 文章评论VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class ArticleCommentVO {
    //评论id
    private Long id;
    //评论类型 (1文章 2友链 3说说)
    private Integer commentType;
    //类型id
    private Integer typeId;
    //父评论id
    private Long parentId;
    //回复评论id
    private Long replyId;
    //评论的内容
    private String commentContent;
    //评论用户的id
    private Long commentUserId;
    //回复用户的id
    private Long replyUserId;
    //评论时间
    private Date createTime;
    // 评论昵称
    private String commentUserNickname;
    // 评论头像
    private String commentUserAvatar;
    // 回复的昵称
    private String replyUserNickname;
    // 点赞数
    private Long likeCount;
    // 子评论数
    private Long childCommentCount;
    // 父评论数
    private Long parentCommentCount;
    // 子评论
    private List<ArticleCommentVO> childComment;
}
