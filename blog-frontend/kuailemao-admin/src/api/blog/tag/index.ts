import { message } from 'ant-design-vue'

// 标签列表
export async function tagList() {
  return useGet('/tag/back/list').catch(msg => message.warn(msg))
}

// 搜索标签
export async function searchTag(data: any) {
  return usePost('/tag/back/search', data).catch(msg => message.warn(msg))
}

// 根据id搜索标签
export async function searchTagById(id: string) {
  return useGet(`/tag/back/get/${id}`).catch(msg => message.warn(msg))
}

// 新增标签
export async function addTag(data: any) {
  return usePut('/tag/back/add', data).catch(msg => message.warn(msg))
}

// 修改标签
export async function updateTag(data: any) {
  return usePost('/tag/back/update', data).catch(msg => message.warn(msg))
}

// 删除标签
export async function deleteTagByIds(ids: string[]) {
  return useDelete('/tag/back/delete', JSON.stringify(ids)).catch(msg => message.warn(msg))
}
