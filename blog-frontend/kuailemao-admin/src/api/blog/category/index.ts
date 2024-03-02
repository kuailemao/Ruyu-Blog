import { message } from 'ant-design-vue'

// 分类列表
export async function categoryList() {
  return useGet('/category/back/list').catch(msg => message.warn(msg))
}

// 搜索分类
export async function searchCategory(data: any) {
  return usePost('/category/back/search', data).catch(msg => message.warn(msg))
}

// 根据id搜索分类
export async function searchCategoryById(id: string) {
  return useGet(`/category/back/get/${id}`).catch(msg => message.warn(msg))
}

// 新增分类
export async function addCategory(data: any) {
  return usePut('/category/back/add', data).catch(msg => message.warn(msg))
}

// 修改分类
export async function updateCategory(data: any) {
  return usePost('/category/back/update', data).catch(msg => message.warn(msg))
}

// 删除分类
export async function deleteCategoryByIds(ids: string[]) {
  return useDelete('/category/back/delete', JSON.stringify(ids)).catch(msg => message.warn(msg))
}
