import { message } from 'ant-design-vue'

/**
 * 相册与照片列表
 */
export async function photoAndAlbumList(params: {
  pageNum: number
  pageSize: number
  parentId: number | null
}) {
  return useGet('/photo/back/list', params).catch(msg => message.warn(msg))
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

/**
 * 上传照片
 * @param data 照片信息
 */
export async function uploadPhoto(data: any) {
  return usePost('/photo/upload', data, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  }).catch(msg => message.warn(msg))
}

/**
 * 修改相册
 */
export async function updateAlbum(data: {
  id: number
  name: string
  description: string
}) {
  return usePost('/photo/album/update', data).catch(msg => message.warn(msg))
}

/**
 * 删除相册或照片
 */
export async function deletePhotoOrAlbum(data: { id: number; type: 1 | 2 ;parentId: number | null }) {
  return useDelete('/photo/delete', data).catch(msg => message.warn(msg))
}
