import {defineStore} from "pinia";
import {shallowRef} from "vue";
import WebsiteInfo from "@/apis/website/type.ts";
import {getWebsiteInfo} from "@/apis/website";
import {returnTime} from "@/utils/tool.ts";

const useWebsiteStore = defineStore('website', () => {
    const webInfo = shallowRef<WebsiteInfo>()

    // 获取网站信息
    const getInfo = async () => {
        getWebsiteInfo().then(res => {
            res.data.lastUpdateTime = returnTime(res.data.lastUpdateTime) as string
            webInfo.value = res.data
        })
    }

    return {
        webInfo,
        getInfo
    }
})

export default useWebsiteStore