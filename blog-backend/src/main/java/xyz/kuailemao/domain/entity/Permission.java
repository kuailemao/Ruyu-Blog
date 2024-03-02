package xyz.kuailemao.domain.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.kuailemao.domain.BaseData;

import java.util.Date;


/**
 * (Permission)表实体类
 *
 * @author kuailemao
 * @since 2023-12-05 19:55:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_permission")
public class Permission implements BaseData {
    //权限表id
    private Integer id;
    //描述
    private String permissionDesc;
    //权限字符
    private String permissionKey;
    // 菜单id
    private Long menuId;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //是否删除（0：未删除，1：已删除）
    private Integer isDeleted;
}

