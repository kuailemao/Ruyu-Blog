import { message } from 'ant-design-vue'

// 对应角色的用户
export async function queryRoleUser(roleId: string, username?: string, email?: string) {
  return useGet('/user/role/user/list', null, {
    params: {
      roleId,
      username,
      email,
    },
  }).catch(msg => message.warn(msg))
}

// 没有对应角色的用户
export async function queryNotRoleUser(roleId: string, username?: string, email?: string) {
  return useGet('/user/role/not/user/list', null, {
    params: {
      roleId,
      username,
      email,
    },
  }).catch(msg => message.warn(msg))
}

// 给用户添加角色权限
export async function addUserRole(data: any) {
  return usePost('/user/role/add', data).catch(msg => message.warn(msg))
}

// 取消用户授权
export async function deleteUserRole(data: any) {
  return useDelete('/user/role/delete', data).catch(msg => message.warn(msg))
}
