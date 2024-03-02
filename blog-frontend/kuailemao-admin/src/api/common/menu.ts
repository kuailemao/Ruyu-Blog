import { message } from 'ant-design-vue'
import type { MenuData } from '~@/layouts/basic-layout/typing'
import type { formType } from '~/pages/system/menu/type.ts'
import type { roleListType } from '~/api/common/type.ts'

/**
 * 获取路由菜单
 */
export function getRouteMenusApi(typeId: number, username?: string, status?: number) {
  return useGet<MenuData>(`/menu/router/list/${typeId}`, null, {
    params: {
      username,
      status,
    },
  })
  // return useGet<MenuData>('/menu')
}

/**
 * 获取管理菜单
 */
export async function getMenusApi(typeId: number) {
  return useGet<MenuData>(`/menu/list/${typeId}`, null, {
  }).catch(msg => message.warn(msg))
}

/**
 * 搜索管理菜单
 */
export async function searchMenusApi(typeId: number, username?: string, status?: number) {
  return useGet<MenuData>(`/menu/search/list/${typeId}`, null, {
    params: {
      username,
      status,
    },
  }).catch(msg => message.warn(msg))
}

/**
 * 添加菜单
 * @param data 菜单数据
 */
export async function addRouteMenusApi(data: formType) {
  return usePost<formType>('/menu', data).catch(msg => message.warn(msg))
}

/**
 * 根据id查询菜单信息
 */
export async function getRouteMenuByIdApi(id: string) {
  return useGet<formType>(`/menu/${id}`, null, {
    customDev: true,
  }).catch(msg => message.warn(msg))
}

/**
 * 修改菜单
 * @param data 菜单数据
 */
export async function updateRouteMenusApi(data: formType) {
  return usePut<formType>('/menu', data, {
    customDev: true,
  }).catch(msg => message.warn(msg))
}

/**
 * 删除菜单
 * @param id 菜单id
 */
export async function deleteRouteMenusApi(id: string) {
  return useDelete<formType>(`/menu/${id}`, null, {
    customDev: true,
  }).catch(msg => message.warn(msg))
}

/**
 * 查询菜单所需角色列表
 */
export async function queryRoleList() {
  return useGet<roleListType[]>('/menu/role/list', null, {
    customDev: true,
  }).catch(msg => message.warn(msg))
}
