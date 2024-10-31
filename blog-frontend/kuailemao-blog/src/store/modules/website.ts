import {defineStore} from "pinia";
import {shallowRef} from "vue";
import WebsiteInfo from "@/apis/website/type.ts";
import {getWebsiteInfo} from "@/apis/website";
import {returnTime} from "@/utils/tool.ts";
import {getSearchTitleList} from "@/apis/article";
import {ArticleSearch} from "@/apis/article/type.ts";

const useWebsiteStore = defineStore('website', () => {
    const webInfo = shallowRef<WebsiteInfo>()
    // 标题搜索数据
    const searchTitle = shallowRef<Array<ArticleSearch>>()

    // 获取网站信息
    const getInfo = async () => {
        getWebsiteInfo().then(res => {
            res.data.lastUpdateTime = returnTime(res.data.lastUpdateTime) as string
            webInfo.value = res.data
        })
    }

    // 获取网站文章标题搜索数据
    const getArticleTitleList = async () => {
        const res = await getSearchTitleList()
        searchTitle.value = res.data
    }

    return {
        webInfo,
        getInfo,
        searchTitle,
        getArticleTitleList,
    }
})

export default useWebsiteStore