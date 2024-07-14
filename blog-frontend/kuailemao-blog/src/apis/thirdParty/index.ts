import http from "@/utils/http.ts";
// 第三方的api接口
// 一言接口
let myYiYan = import.meta.env.VITE_YIYAN_API
if (!myYiYan){
    myYiYan = 'https://v1.hitokoto.cn/?c=a&encode=json'
}
// 每日鸡汤
export const getSoup = () => {
    return http({
        url: myYiYan,
        method: 'get'
    })
}