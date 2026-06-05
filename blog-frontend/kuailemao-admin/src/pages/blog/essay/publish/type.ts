// 文章分类
export interface CategoryType {
  id: string | number
  categoryName: string
}

// 文章标签
export interface TagType {
  id: string | number
  tagName: string
}

// 提交数据
export interface ArticleType {
  categoryId: string | number
  tagId: Array<string | number>
  articleTitle: string
  articleCover: string
  articleContent: string
  articleType: number
  isTop: number
  status: number
}
