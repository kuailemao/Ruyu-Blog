import { message } from 'ant-design-vue'
import type { roleListType } from '~/api/role/type.ts'

/**
 * 查询所有角色信息
 */
export async function roleList() {
  return useGet<roleListType[]>('/role/list', null).catch(msg => message.warn(msg))
}

/**
 * 更新角色状态
 */
export async function roleUpdateStatus(id: string, status: number) {
  return usePost('/role/update/status', { id, status })
}

/**
 * 获取角色信息
 */
export async function roleInfoById(id: string) {
  return useGet(`/role/get/${id}`, null).catch(msg => message.warn(msg))
}

/**
 * 修改角色信息
 */
export async function roleUpdate(data: any) {
  return usePut('/role/update', data).catch(msg => message.warn(msg))
}

/**
 * 添加角色信息
 */
export async function roleInsert(data: any) {
  return usePut('/role/add', data).catch(msg => message.warn(msg))
}

/**
 * 删除角色
 */
export async function roleDelete(ids: string[]) {
  return useDelete('/role/delete', { ids }).catch(msg => message.warn(msg))
}

/**
 * 搜索角色
 */
export async function roleSearch(data: any) {
  return usePost('/role/search', data).catch(msg => message.warn(msg))
}
