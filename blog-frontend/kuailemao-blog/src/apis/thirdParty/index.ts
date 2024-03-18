import http from "@/utils/http.ts";
// 第三方的api接口
// 每日鸡汤
export const getSoup = () => {
    return http({
        url: 'https://v1.hitokoto.cn/?c=a&encode=json',
        method: 'get'
    })
}