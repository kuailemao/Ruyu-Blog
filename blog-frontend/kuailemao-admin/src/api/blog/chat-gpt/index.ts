import { message } from 'ant-design-vue'

// 聊天列表
export async function chatGptList() {
  return useGet('/chatGpt/back/list').catch(msg => message.warn(msg))
}

// 搜索聊天
export async function searchChatGpt(data: any) {
  return usePost('/chatGpt/back/search', data).catch(msg => message.warn(msg))
}

// 是否通过聊天
export async function isCheckChatGpt(data: any) {
  return usePost('/chatGpt/back/isCheck', data).catch(msg => message.warn(msg))
}

// 删除聊天
export async function deleteChatGpt(data: any) {
  return useDelete('/chatGpt/back/delete', JSON.stringify(data)).catch(msg => message.warn(msg))
}
