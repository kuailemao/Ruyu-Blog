import { message } from 'ant-design-vue'

// 查询文章分类
export async function articleCategory() {
  return useGet('/category/list').catch(msg => message.warn(msg))
}

// 查询文章标签
export async function articleTag() {
  return useGet('/tag/list').catch(msg => message.warn(msg))
}

// 新增标签
export async function addTag(data: any) {
  return usePut('/tag', data).catch(msg => message.warn(msg))
}

// 新增分类
export async function addCategory(data: any) {
  return usePut('/category', data).catch(msg => message.warn(msg))
}

// 上传文章封面
export async function uploadCover(data: any) {
  return usePost('/article/upload/articleCover', data, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  }).catch(msg => message.warn(msg))
}

// 发布文章
export async function publishArticle(data: any) {
  return usePost('/article/publish', data).catch(msg => message.warn(msg))
}

// 发布错误，删除封面
export async function deleteCover(articleCoverUrl: string) {
  return useGet('/article/delete/articleCover', { articleCoverUrl }).catch(msg => message.warn(msg))
}

// 上传文章图片
export async function uploadArticleImage(data: any) {
  return usePost('/article/upload/articleImage', data, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  }).catch(msg => message.warn(msg))
}

// 文章列表
export async function articleList() {
  return useGet('/article/back/list').catch(msg => message.warn(msg))
}

// 文章搜索
export async function articleSearch(data: any) {
  return usePost('/article/back/search', data).catch(msg => message.warn(msg))
}

// 修改文章状态
export async function updateArticleStatus(data: any) {
  return usePost('/article/back/update/status', null, {
    params: data,
  }).catch(msg => message.warn(msg))
}

// 修改文章是否顶置
export async function updateArticleTop(data: any) {
  return usePost('/article/back/update/isTop', null, {
    params: data,
  }).catch(msg => message.warn(msg))
}

// 回显文章数据
export async function getArticle(articleId: string) {
  return useGet(`/article/back/echo/${articleId}`).catch(msg => message.warn(msg))
}

// 删除文章
export async function deleteArticle(ids: string[]) {
  return useDelete('/article/back/delete', JSON.stringify(ids)).catch(msg => message.warn(msg))
}
