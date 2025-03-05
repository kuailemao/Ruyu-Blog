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

// 上传站长头像
export async function uploadAvatar(data: any) {
  return usePost('/websiteInfo/upload/avatar', data, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  })
}

// 上传站长资料背景
export async function uploadAckgroundImage(data: any) {
  return usePost('/websiteInfo/upload/background', data, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  })
}
