package xyz.kuailemao.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2024/1/22 8:18
 */
@Data
public class CommentListVO {
    //评论id
    private Long id;
    //评论类型 (1文章 2留言板)
    private Integer type;
    //类型id
    private Integer typeId;
    //父评论id
    private Long parentId;
    //回复评论id
    private Long replyId;
    //评论的内容
    private String commentContent;
    //评论用户的名称
    private String commentUserName;
    //是否通过 (0否 1是)
    private Integer isCheck;
    //评论时间
    private Date createTime;
    //更新时间
    private Date updateTime;
}
