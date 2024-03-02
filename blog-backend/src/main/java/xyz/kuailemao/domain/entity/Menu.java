package xyz.kuailemao.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.kuailemao.domain.BaseData;


/**
 * (Menu)表实体类
 *
 * @author kuailemao
 * @since 2023-11-17 22:15:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_menu")
public class Menu implements BaseData {
    //唯一id
    private Long id;
    //标题
    private String title;
    //图标
    private String icon;
    //地址
    private String path;
    //绑定的哪个组件，默认自带的组件类型分别是：Iframe、RouteView和ComponentError
    private String component;
    //父菜单重定向地址(默认第一个子菜单)
    private String redirect;
    //是否是固定页签(0否 1是)
    private Integer affix;
    //父级菜单的id
    private Long parentId;
    //同路由中的name，主要是用于保活的左右
    private String name;
    //是否隐藏当前菜单(0否 1是)
    private Integer hideInMenu;
    //如果当前是iframe的模式，需要有一个跳转的url支撑，其不能和path重复，path还是为路由
    private String url;
    //是否存在于面包屑(0否 1是)
    private Integer hideInBreadcrumb;
    //是否需要显示所有的子菜单(0否 1是)
    private Integer hideChildrenInMenu;
    //是否保活(0否 1是)
    private Integer keepAlive;
    //全连接跳转模式('_blank' | '_self' | '_parent')
    private String target;
    //是否禁用 (0否 1是)
    private Integer isDisable;
    //排序
    private Integer orderNum;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //是否删除（0：未删除，1：已删除）
    private Integer isDeleted;
}

