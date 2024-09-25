import { message } from 'ant-design-vue'

// 黑名单列表
export async function blackList(data: any) {
  return useGet('/blackList/getBlackListing').catch(msg => message.warn(msg))
}
