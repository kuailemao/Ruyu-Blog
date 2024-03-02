import {defineStore} from 'pinia'

let usePaginationStore = defineStore('pagination', {
    state: () => ({
        // 文章分页
        articlePagination: {
            // 当前页
            current: 1,
            // 每页条数
            pageSize: 10,
            // 总条数
            total: 0,
        }
    }),
})

export default usePaginationStore