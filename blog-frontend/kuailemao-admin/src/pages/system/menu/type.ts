import type { VNodeChild } from 'vue'

/**
 * 菜单数据类型
 */
export interface MenuDataItem {
  // 唯一id
  id?: string | number
  // 菜单唯一key
  key?: string | number
  // 标题
  title: string | (() => VNodeChild)
  // 图标
  icon?: string | (() => VNodeChild)
  // 地址
  path: string
  // 绑定的哪个组件
  component?: string
  // 子集菜单
  children?: MenuDataItem[]
  // 重定向地址
  redirect?: string
  // 哪些是固定页签
  affix?: boolean
  // 父级菜单的id
  parentId?: string | number | null
  // 同路由中的name，主要是用于保活的左右
  name?: string
  // 是否隐藏当前菜单
  hideInMenu?: boolean
  // 如果使用了隐藏，那么点击当前菜单的时候，可以使用父级的key
  parentKeys?: string[]
  // 是否套用iframe
  isIframe?: boolean
  // 如果当前是iframe的模式，需要有一个跳转的url支撑，其不能和path重复，path还是为路由
  url?: string
  // 是否存在面包屑
  hideInBreadcrumb?: boolean
  // 是否需要显示所有的子菜单
  hideChildrenInMenu?: boolean
  // 是否保活
  keepAlive?: boolean
  // 这里包含所有的父级元素
  matched?: MenuDataItem[]
  // 全连接跳转模式
  target?: '_blank' | '_self' | '_parent'
  // 多语言配置
  locale?: string
  // 是否禁用
  isDisable?: boolean
  // 创建时间
  createTime?: string
  // 排序
  orderNum?: number
}

/**
 * 添加菜单数据类型
 */
export interface formType {
  id: string
  // 上级菜单
  parentId: string
  // 菜单标题
  title: string
  // 显示顺序
  orderNum: number
  // 访问角色id
  roleId: string | undefined
  // 菜单图标
  icon: string
  // 路由类型
  routerType: number
  // 组件路径
  component: string
  // 重定向地址
  redirect: string
  // 访问地址
  path: string
  // 内嵌网页地址
  url: string
  // 跳转模式
  target: string
  // 固定标签
  affix: number
  // 是否保活
  keepAlive: number
  // 显示状态
  hideInMenu: number
  // 菜单状态
  isDisable: number
}

/**
 * 添加菜单选择上级菜单表单的数据结构
 */
export interface newItem {
  value: number
  label: string
  children?: newItem[]
}
