import http from "@/utils/http.ts";

// 申请友链
export function applyLink(data: any) {
    return http({
        url: '/link/auth/apply',
        method: 'post',
        data: data
    })
}

// 查询友链列表
export function linkList() {
    return http({
        url: '/link/list',
        method: 'get'
    })
}
