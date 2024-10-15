<!-- 侧边栏 -->
<template>
  <div>
    <div>
      <InfoCard/>
    </div>
    <div>
      <Card title="公告" prefixIcon="announcement" suffix-icon="jt_y" :isDithering="true" :isArrow="true"
            @invoke="announcement">
        <pre class="pre-text">
{{ useWebsite.webInfo?.sidebarAnnouncement }}
        </pre>
      </Card>
    </div>
    <div>
      <ElectronicClocks/>
    </div>
    <div>
      <RandomArticle/>
    </div>
    <ChargingList/>
    <div>
      <Card title="每日鸡汤" prefix-icon="edit" suffix-icon="rotate" :isRotate="true" :isScale="true" @invoke="soupSub">
        <p>{{ soup }}</p>
      </Card>
    </div>
    <div>
      <Card title="网站资讯" prefix-icon="statistics" :isScale="true">
        <div class="statistics">
          <div>文章数目：<span>{{ useWebsite.webInfo?.articleCount }}</span></div>
          <div>运行时长：<span>{{ differenceInDays }} 天</span></div>
          <div>全站字数：<span>{{ useWebsite.webInfo?.wordCount }}</span></div>
          <div>访问次数：<span>{{ useWebsite.webInfo?.visitCount }}</span></div>
          <div>最后更新：<span>{{ useWebsite.webInfo?.lastUpdateTime }}前</span></div>
        </div>
      </Card>
    </div>
  </div>
</template>

<script setup lang="ts">
import InfoCard from '@/components/CardInfo/index.vue'
import Card from '@/components/Card/index.vue'
import {ref, onMounted} from 'vue'
import {getSoup} from "@/apis/thirdParty";
import useWebsiteStore from "@/store/modules/website.ts";
import {ElMessageBox} from "element-plus";

const useWebsite = useWebsiteStore()

const differenceInDays = ref(0)
getDifferenceInDays()


// 监听数据是否过来
watch(() => useWebsite.webInfo?.startTime, () => {
  if (useWebsite.webInfo?.startTime) {
    getDifferenceInDays()
  }
})

// 计算天数方法
function getDifferenceInDays() {
  // 计算运行时长
  // 假设 startTime 是一个表示开始时间的 Date 对象
  let startTime = new Date(useWebsite.webInfo?.startTime); // 替换为你实际的开始时间
  // 获取当前时间
  let now = new Date();
  // 计算两个日期之间的差值（以毫秒为单位）
  let differenceInMs = now.getTime() - startTime.getTime();
  // 转换为天数（向下取整，不考虑小时、分钟和秒）
  differenceInDays.value = Math.floor(differenceInMs / (1000 * 60 * 60 * 24));
}


// 每日鸡汤
const soup = ref('')

function soupSub() {
  getSoup().then((res: any) => {
    soup.value = res.hitokoto
  })
}

function announcement() {
  ElMessageBox.alert(`<pre>${useWebsite.webInfo?.sidebarAnnouncement}</pre>`, '公告', {
    // if you want to disable its autofocus
    // autofocus: false,
    confirmButtonText: '关闭',
    closeOnPressEscape: true,
    dangerouslyUseHTMLString: true,
  })
}

onMounted(() => {
  getSoup().then((res: any) => {
    soup.value = res.hitokoto
  })
})

</script>

<style lang="scss" scoped>
.statistics {
  display: flex;
  flex-direction: column;
  color: $menuActiveText;

  div {
    margin: 5px 20px;
    font-size: 14px;
    display: flex;
    justify-content: space-between;
  }
}

.pre-text {
  text-align: left;
  overflow: auto; /* 如果内容超出了元素盒子的宽度，显示滚动条 */
}

</style>