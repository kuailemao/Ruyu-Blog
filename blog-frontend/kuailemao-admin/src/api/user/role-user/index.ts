import { message } from 'ant-design-vue'

/**
 * 查询用户的角色
 * @param userId 用户id
 * @param roleName 角色名称
 * @param roleKey 角色key
 */
export async function queryUserRole(userId: string, roleName?: string, roleKey?: string) {
  return useGet('/user/role/role/list', null, {
    params: {
      userId,
      roleName,
      roleKey,
    },
  }).catch(msg => message.warn(msg))
}

/**
 * 未分配该用户的角色列表
 * @param userId 用户id
 * @param roleName  角色名称
 * @param roleKey 角色key
 */
export async function queryUserNotRole(userId: string, roleName?: string, roleKey?: string) {
  return useGet('/user/role/not/role/list', null, {
    params: {
      userId,
      roleName,
      roleKey,
    },
  }).catch(msg => message.warn(msg))
}

/**
 * 添加角色用户关系
 * @param data 角色用户关系对象
 */
export async function addRoleUser(data: any) {
  return usePost('/user/role/user/add', data).catch(msg => message.warn(msg))
}

/**
 * 删除角色用户关系（取消授权）
 * @param data 角色用户关系对象
 */
export async function deleteRoleUser(data: any) {
  return useDelete('/user/role/user/delete', data).catch(msg => message.warn(msg))
}
