import http from "@/utils/http.ts";

// 获取首页文章列表
export function getArticleList(pageNum: Number, pageSize: Number) {
    return http({
        url: '/article/list',
        method: 'get',
        params: {
            pageNum,
            pageSize
        }
    })
}

// 获取推荐文章列表
export function getRecommendArticleList() {
    return http({
        url: '/article/recommend',
        method: 'get'
    })
}

// 获取随机文章
export function getRandomArticle() {
    return http({
        url: '/article/random',
        method: 'get'
    })
}

// 相关推荐(按照分类)
export function getRelatedArticle(categoryId: string, articleId: string) {
    return http({
        url: `/article/related/${categoryId}/${articleId}`,
        method: 'get'
    })
}