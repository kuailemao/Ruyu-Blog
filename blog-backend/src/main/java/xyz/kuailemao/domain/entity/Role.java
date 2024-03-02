package xyz.kuailemao.domain.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import xyz.kuailemao.domain.BaseData;

import java.util.Date;


/**
 * (Role)表实体类
 *
 * @author kuailemao
 * @since 2023-10-13 15:02:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("sys_role")
public class Role implements BaseData {
    //角色id
    private Long id;
    // 角色名称
    private String roleName;
    //角色字符
    private String roleKey;
    //是否删除（0：未删除，1：已删除）
    private Integer isDeleted;
    // 状态（0：正常，1：停用）
    private Integer status;
    // 顺序
    private Long orderNum;
    // 备注
    private String remark;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}

