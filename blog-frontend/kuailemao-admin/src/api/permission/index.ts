import { message } from 'ant-design-vue'

/**
 * 查询所有权限字符
 */
export async function permissionList() {
  return useGet('/permission/list', null).catch(msg => message.warn(msg))
}

/**
 * 搜索权限
 */
export async function searchPermissionList(permissionDesc?: string, permissionKey?: string, permissionMenuId?: string) {
  return useGet('/permission/search', null, {
    params: {
      permissionDesc,
      permissionKey,
      permissionMenuId,
    },
  }).catch(msg => message.warn(msg))
}

/**
 * 查询所有权限所在菜单
 */
export async function permissionMenuList() {
  return useGet('/permission/menu', null).catch(msg => message.warn(msg))
}

/**
 * 添加权限
 */
export function addPermission(data: object) {
  return usePost('/permission/add', data).catch(msg => message.warn(msg))
}

/**
 * 修改权限
 */
export function updatePermission(data: object) {
  return usePost('/permission/update', data).catch(msg => message.warn(msg))
}

/**
 * 获取对应权限信息
 */
export function getPermission(permissionId: string) {
  return useGet(`/permission/get/${permissionId}`).catch(msg => message.warn(msg))
}

/**
 * 删除对应权限
 */
export function deletePermission(permissionId: string) {
  return useDelete(`/permission/delete/${permissionId}`).catch(msg => message.warn(msg))
}
