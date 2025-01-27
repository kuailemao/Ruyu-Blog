import http from "@/utils/http.ts";

// 相册和照片的类型定义
export interface PhotoAndAlbumVO {
    id: number;
    userId: number;
    name: string;
    description: string | null;
    type: 1 | 2; // 1: 相册, 2: 照片
    parentId: number | null;
    url: string | null;
    isCheck: number;
    size: number | null;
    createTime: string;
}

export interface PageVO<T> {
    records: T;
    total: number;
    size: number;
    current: number;
    pages: number;
}

// 获取相册和照片列表
export function getPhotoList(params: {
    pageNum?: number;
    pageSize?: number;
    parentId?: number | null;
}) {
    return http<PageVO<PhotoAndAlbumVO[]>>({
        url: '/photo/list',
        method: 'get',
        params
    });
}
