// 文章搜索
export interface ArticleSearch {
    id: number;
    categoryName: string;
    visitCount: number;
    articleTitle: string;
    highlightedTitle: string;
    articleContent: string;
}
// 文章热门推荐
export interface ArticleHotRecommend {
    id: number;
    articleTitle: string;
    visitCount: number;
}