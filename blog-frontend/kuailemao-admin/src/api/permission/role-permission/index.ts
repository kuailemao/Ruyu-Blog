import { message } from 'ant-design-vue'

// 对应权限的角色
export async function queryPermissionRole(permissionId: string, roleName?: string, roleKey?: string) {
  return useGet('/role/permission/role/list', null, {
    params: {
      permissionId,
      roleName,
      roleKey,
    },
  }).catch(msg => message.warn(msg))
}

// 未分配该权限的角色列表
export async function queryPermissionNotRole(permissionId: string, roleName?: string, roleKey?: string) {
  return useGet('/role/permission/not/role/list', null, {
    params: {
      permissionId,
      roleName,
      roleKey,
    },
  }).catch(msg => message.warn(msg))
}

// 添加角色权限关系
export async function addRolePermission(data: any) {
  return usePost('/role/permission/add', data).catch(msg => message.warn(msg))
}

// 删除角色权限关系（取消授权）
export async function deleteRolePermission(data: any) {
  return useDelete('/role/permission/delete', data).catch(msg => message.warn(msg))
}
