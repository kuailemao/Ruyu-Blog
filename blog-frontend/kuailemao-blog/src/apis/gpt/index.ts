import http from "@/utils/http.ts";

// 保存会话
export function saveGptSession(data: any) {
    return http({
        url: '/chatGpt/auth/saveConversation',
        method: 'post',
        data: JSON.stringify(data)
    })
}

// 查询会话列表
export function queryGptSessionList() {
    return http({
        url: '/chatGpt/auth/conversation/list',
        method: 'get'
    })
}

// 查询会话详情
export function queryGptSessionDetail(id: string) {
    return http({
        url: '/chatGpt/auth/conversation/detail',
        method: 'get',
        params: {
            id
        }
    })
}

// 删除某个会话
export function deleteGptSession(id: string) {
    return http({
        url: '/chatGpt/auth/conversation/delete',
        method: 'delete',
        params: {
            id
        }
    })
}