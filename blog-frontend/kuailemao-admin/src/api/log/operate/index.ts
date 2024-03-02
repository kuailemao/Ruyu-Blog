import { message } from 'ant-design-vue'

/**
 * 查询所有操作日志
 */
export async function logList(current: number, pageSize: number) {
  return useGet(`/log/list/${current}/${pageSize}`).catch(msg => message.warn(msg))
}

/**
 * 搜索登录日志
 */
export async function searchLog(data: any) {
  return usePost(`/log/search`, data).catch(msg => message.warn(msg))
}

/**
 * 删除操作日志
 */
export async function deleteLogByIds(ids: string[]) {
  return useDelete('/log/delete', { ids }).catch(msg => message.warn(msg))
}
