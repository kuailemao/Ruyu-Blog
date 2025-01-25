import { message } from 'ant-design-vue'

/**
 * 相册与照片列表
 */
export async function photoAndAlbumList() {
  return useGet('/photo/back/list').catch(msg => message.warn(msg))
}

/**
 * 创建相册
 * @param data 相册信息
 */
export async function createAlbum(data: {
  name: string
  description: string
  parentId: number | null
}) {
  return usePost('/photo/album/create', data).catch(msg => message.warn(msg))
}

// 上传照片
export async function uploadPhoto(data: any) {
  return usePost('/photo/photo/upload', data, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  }).catch(msg => message.warn(msg))
}
