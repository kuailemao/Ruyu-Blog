import http from "@/utils/http.ts";

// 收藏
export const userFavorite = (type: number,typeId: string) => {
    return http.request({
        url: '/favorite/auth/favorite',
        method: "post",
        params: {
            type,
            typeId
        }
    });
}

// 取消收藏
export const cancelFavorite = (type: number,typeId: string) => {
    return http.request({
        url: '/favorite/auth/favorite',
        method: "delete",
        params: {
            type,
            typeId
        }
    });
}

// 是否收藏
export const isFavorite = (type: number,typeId: string) => {
    return http.request({
        url: '/favorite/whether/favorite',
        method: "get",
        params: {
            type,
            typeId
        }
    });
}