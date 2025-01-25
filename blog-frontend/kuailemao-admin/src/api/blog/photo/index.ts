import { message } from 'ant-design-vue'

// 相册与照片列表
export async function photoAndAlbumList() {
  return useGet('/photos/back/list').catch(msg => message.warn(msg))
}
