import { message } from 'ant-design-vue'

// 收藏列表
export async function favoriteList() {
  return useGet('/favorite/back/list').catch(msg => message.warn(msg))
}

// 搜索收藏
export async function searchFavorite(data: any) {
  return usePost('/favorite/back/search', data).catch(msg => message.warn(msg))
}

// 是否通过收藏
export async function isCheckFavorite(data: any) {
  return usePost('/favorite/back/isCheck', data).catch(msg => message.warn(msg))
}

// 删除收藏
export async function deleteFavorite(data: any) {
  return useDelete('/favorite/back/delete', JSON.stringify(data)).catch(msg => message.warn(msg))
}
