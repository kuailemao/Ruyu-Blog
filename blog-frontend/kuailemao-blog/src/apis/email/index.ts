import http from "@/utils/http.ts";

// 发送邮件
export function sendEmail(email: any, type: any) {
    return http({
        url: '/public/ask-code',
        params: {
            email: email,
            type: type
        },
        method: 'get'
    })
}