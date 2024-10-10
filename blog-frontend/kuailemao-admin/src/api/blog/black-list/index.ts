import { message } from 'ant-design-vue'

// 黑名单列表
export async function blackList(data: any) {
  return usePost('/blackList/getBlackListing', data).catch(msg => message.warn(msg))
}

// 修改黑名单
export async function updateBlackList(data: any) {
  return usePut('/blackList/update', data)
}

// 新增黑名单
export async function addBlackList(data: any) {
  return usePost('/blackList/add', data)
}

// 删除黑名单
export async function deleteBlackList(ids: string[]) {
  return useDelete('/blackList/delete', JSON.stringify(ids))
}
