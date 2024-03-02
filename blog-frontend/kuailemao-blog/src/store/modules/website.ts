import {defineStore} from "pinia";
import {shallowRef} from "vue";
import WebsiteInfo from "@/apis/website/type.ts";
import {getWebsiteInfo} from "@/apis/website";

const useWebsiteStore = defineStore('website', () => {
    const webInfo = shallowRef<WebsiteInfo>()

    // 获取网站信息
    const getInfo = async () => {
        getWebsiteInfo().then(res => {
            // 获取当前时间的时间戳
            const now = new Date().getTime();
            // 将字符串转换为日期对象
            const lastUpdated = new Date(res.data.lastUpdateTime);
            // 获取最后更新时间的时间戳
            const lastUpdatedTimestamp = lastUpdated.getTime();
            // 计算日期差异
            const differenceInDays = Math.floor((now - lastUpdatedTimestamp) / (1000 * 60 * 60 * 24));
            res.data.lastUpdateTime = differenceInDays + '天前'
            webInfo.value = res.data
            console.log('网站信息', res.data)
        })
    }

    return {
        webInfo,
        getInfo
    }
})

export default useWebsiteStore