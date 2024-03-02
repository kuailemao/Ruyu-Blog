package xyz.kuailemao.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.kuailemao.domain.BaseData;

import java.util.Date;


/**
 * (LeaveWord)表实体类
 *
 * @author kuailemao
 * @since 2023-11-03 15:01:10
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_leave_word")
public class LeaveWord implements BaseData {
    //id
    private Long id;
    // 留言用户id
    private Long userId;
    //留言内容
    private String content;
    //是否通过 (0否 1是)
    private Integer isCheck;
    //留言时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //是否删除（0：未删除，1：已删除）
    private Integer isDeleted;
}

