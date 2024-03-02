import http from "@/utils/http.ts";
import WebsiteInfo from "@/apis/website/type.ts";
import {AxiosResponse} from "axios";

// 获取网站信息
export function getWebsiteInfo():Promise<AxiosResponse<WebsiteInfo>> {
    return http({
        url: '/websiteInfo/front',
        method: 'get'
    })
}