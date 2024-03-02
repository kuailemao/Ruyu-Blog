package xyz.kuailemao.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/11/27 10:42
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuByIdVO {
    //唯一id
    private Long id;
    //标题
    private String title;
    //图标
    private String icon;
    // 角色id
    private List<Long> roleId;
    // 路由类型
    private Long routerType;
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
    //是否隐藏当前菜单(0否 1是)
    private Integer hideInMenu;
    //如果当前是iframe的模式，需要有一个跳转的url支撑，其不能和path重复，path还是为路由
    private String url;
    //是否保活(0否 1是)
    private Integer keepAlive;
    //全连接跳转模式('_blank' | '_self' | '_parent')
    private String target;
    //排序
    private Integer orderNum;
    //是否禁用 (0否 1是)
    private Integer isDisable;
}
