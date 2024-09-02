// 上传banner
import {message} from "ant-design-vue";

export async function uploadBanner(data: any, handleProgress: any) {
  return usePost('/banners/upload/banner', data, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
    onUploadProgress: handleProgress,
  })
}

// 查询banner列表
export async function backGetBanners() {
  return useGet('/banners/back/list')
}

// 删除banner
export async function deleteBanner(bannerId: any) {
  return useDelete(`/banners/${bannerId}`)
}

// 修改顺序
export async function updateOrder(data: any) {
  return usePut('/banners/update/sort/order', JSON.stringify(data)).catch(resp => message.error(resp))
}
