package xyz.kuailemao.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.kuailemao.domain.BaseData;

import java.util.Date;


/**
 * (TreeHole)表实体类
 *
 * @author kuailemao
 * @since 2023-10-30 11:14:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_tree_hole")
public class TreeHole implements BaseData {
    //树洞表id
    @TableId(type = IdType.AUTO)
    private Long id;
    //用户id
    private Long userId;
    //内容
    private String content;
    // 是否通过
    private Integer isCheck;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //修改时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //是否删除（0：未删除，1：已删除）
    private Integer isDeleted;
}

