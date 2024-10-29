// 文章标题搜索
export interface ArticleSearchByTitle {
    id: number;
    categoryName: string;
    visitCount: number;
    articleTitle: string;
}
// 文章热门推荐
export interface ArticleHotRecommend {
    id: number;
    articleTitle: string;
    visitCount: number;
}