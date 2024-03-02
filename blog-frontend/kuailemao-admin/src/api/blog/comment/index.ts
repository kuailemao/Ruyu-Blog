import { message } from 'ant-design-vue'

// 评论列表
export async function commentList() {
  return useGet('/comment/back/list').catch(msg => message.warn(msg))
}

// 搜索评论
export async function searchComment(data: any) {
  return usePost('/comment/back/search', data).catch(msg => message.warn(msg))
}

// 是否通过评论
export async function isCheckComment(data: any) {
  return usePost('/comment/back/isCheck', data).catch(msg => message.warn(msg))
}

// 删除评论
export async function deleteComment(id: string) {
  return useDelete(`/comment/back/delete/${id}`).catch(msg => message.warn(msg))
}
