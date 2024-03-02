# 权限判断
> 对源码权限判断进行修改，使其更加符合实际开发需求

**支持：**
- 1.支持多角色
- 2.支持多权限

**使用：**
> 根据角色

- **方法1：** 在标签内添加 v-if="hasRole(['admin'])"，判断是否有admin角色
- **方法2：** 在标签内添加 v-hasRole="[AccessEnum.USER, AccessEnum.ADMIN]"，判断是否有USER或ADMIN角色

> 根据权限字符
 
- **方法1：** 在标签内添加 v-if="hasPermi(['user:add'])"，判断是否有user:add权限
- **方法2：** 在标签内添加 v-hasPermi="['user:add', 'user:delete']"，判断是否有user:add或user:delete权限

废弃了 `:access` 方式

## 菜单
``` text
/**
 * 父菜单
 * component: RouteView
 * redirect: 重定向的子路径（默认第一个子菜单）,
 *
 * component 为 RouteView 说明是一个父目录，会重定向到子目录去
 * redirect 为重定向的子路径，component 为 RouteView 时需设置
 *
 * component 为具体路径时不需要设置 redirect
 */

/**
 * 普通组件：
 * component：组件路径
 * path： 访问路径
 *
 * 内嵌组件：
 * component：Iframe
 * url:  内嵌网页地址
 * path： 访问路径
 *
 * 外链组件：
 * component：null
 * path：网页 url
 * target：跳转方式
 *
 */
```

**前端网页信息：**
* 网站名称
* 首页轮播图信息
* 头部通知
  * 侧面公告
* 网站资讯
  * 文章数目   
  * 运行时长
  * 全站字数
  * 访问次数
  * 最后更新
* 备案信息
