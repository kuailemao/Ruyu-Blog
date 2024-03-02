import http from "@/utils/http.ts";
// 点赞
export const userLike = (type: number, typeId: string) => {
    return http.request({
        url: '/like/auth/like',
        method: "post",
        params: {
            type,
            typeId
        }
    });
}

// 取消点赞文章
export const cancelLike = (type: number, typeId: string) => {
    return http.request({
        url: '/like/auth/like',
        method: "delete",
        params: {
            type,
            typeId
        }
    });
}

// 是否点赞
export const isLike = (type: number, typeId?: string) => {
    return http.request({
        url: '/like/whether/like',
        method: "get",
        params: {
            type,
            typeId
        }
    });
}