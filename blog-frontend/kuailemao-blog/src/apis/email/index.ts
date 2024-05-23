import http from "@/utils/http.ts";

/**
 * 发送邮件
 * @param email 邮件地址
 * @param type 类型
 */
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