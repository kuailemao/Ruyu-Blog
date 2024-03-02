// 文章分类
export interface CategoryType {
  id: number
  categoryName: string
}

// 文章标签
export interface TagType {
  id: number
  tagName: string
}

// 提交数据
export interface ArticleType {
  categoryId: string
  tagId: string[]
  articleTitle: string
  articleCover: string
  articleContent: string
  articleType: number
  isTop: number
  status: number
}
