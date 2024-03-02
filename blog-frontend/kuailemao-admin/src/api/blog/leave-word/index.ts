import { message } from 'ant-design-vue'

// 留言列表
export async function leaveMessageList() {
  return useGet('/leaveWord/back/list').catch(msg => message.warn(msg))
}

// 搜索留言
export async function searchLeaveMessage(data: any) {
  return usePost('/leaveWord/back/search', data).catch(msg => message.warn(msg))
}

// 是否通过留言
export async function isCheckLeaveMessage(data: any) {
  return usePost('/leaveWord/back/isCheck', data).catch(msg => message.warn(msg))
}

// 删除留言
export async function deleteLeaveMessage(data: any) {
  return useDelete('/leaveWord/back/delete', JSON.stringify(data)).catch(msg => message.warn(msg))
}
