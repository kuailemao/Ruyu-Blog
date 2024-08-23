package xyz.kuailemao.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * (Like)表实体类
 *
 * @author kuailemao
 * @since 2023-10-18 19:41:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_like")
public class Like {
    //点赞表id
    @TableId(type = IdType.AUTO)
    private String id;
    //点赞的用户id
    private Long userId;
    //点赞类型(1,文章，2,评论)
    private Integer type;
    //点赞的文章id
    private Integer typeId;
    //点赞时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    // 修改时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}

