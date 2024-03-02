import { message } from 'ant-design-vue'

// 查询网站信息
export async function websiteInfo() {
  return useGet('/websiteInfo').catch(msg => message.warn(msg))
}

// 修改站长信息
export async function updateStationmaster(data: any) {
  return usePost('websiteInfo/stationmaster', data).catch(msg => message.warn(msg))
}

// 修改网站信息
export async function updateWebInfo(data: any) {
  return usePost('websiteInfo/webInfo', data).catch(msg => message.warn(msg))
}
