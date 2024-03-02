import { message } from 'ant-design-vue'

// 友链列表
export async function linkList() {
  return useGet('/link/back/list').catch(msg => message.warn(msg))
}

// 搜索友链
export async function searchLink(data: any) {
  return usePost('/link/back/search', data).catch(msg => message.warn(msg))
}

// 是否通过友链
export async function isCheckLink(data: any) {
  return usePost('/link/back/isCheck', data).catch(msg => message.warn(msg))
}

// 删除友链
export async function deleteLink(data: any) {
  return useDelete('/link/back/delete', JSON.stringify(data)).catch(msg => message.warn(msg))
}
