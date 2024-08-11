package xyz.kuailemao.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.kuailemao.domain.BaseData;


/**
 * (CommentEmail)表实体类
 *
 * @author kuailemao
 * @since 2023-10-19 15:44:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_comment")
public class Comment implements BaseData {
    //评论id
    @TableId(type = IdType.AUTO)
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
    //评论用户的id
    private Long commentUserId;
    //回复用户的id
    private Long replyUserId;
    //是否通过 (0否 1是)
    private Integer isCheck;
    //评论时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //是否删除（0：未删除，1：已删除）
    private Integer isDeleted;
}

