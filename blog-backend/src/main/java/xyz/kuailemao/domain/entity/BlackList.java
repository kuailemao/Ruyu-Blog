package xyz.kuailemao.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * (BlackList)表实体类
 *
 * @author kuailemao
 * @since 2024-09-05 16:13:19
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_black_list")
public class BlackList {
    //表id
    @TableId(type = IdType.AUTO)
    private Long id;
    //用户id
    private Long userId;
    //封禁理由
    private String reason;
    //是否解除封禁(0：未解除 1：解除) 默认：0
    private Integer isUnbanned;
    //封禁时间
    @TableField(fill = FieldFill.INSERT)
    private Date bannedTime;
    //到期时间
    private Date expiresTime;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //是否删除（0：未删除，1：已删除）默认：0
    private Integer isDeleted;
}

