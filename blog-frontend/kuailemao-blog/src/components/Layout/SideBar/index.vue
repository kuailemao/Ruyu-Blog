<!-- 侧边栏 -->
<template>
  <div>
    <div>
      <InfoCard/>
    </div>
    <div v-slide-in class="announcement-container">
      <!-- 坐姿动漫少女 -->
      <div class="anime-girl">
        <img src="@/assets/images/动漫少女坐姿-公告_压缩.png" alt="动漫少女" />
      </div>
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
    <div>
      <TagListCard/>
    </div>
    <ChargingList/>
    <div>
      <Card title="每日鸡汤" prefix-icon="edit" suffix-icon="rotate" :isRotate="true" :isScale="true" @invoke="soupSub">
        <div class="soup-container">
          <i class="soup-quote-left">"</i>
          <Transition name="fade" mode="out-in">
            <p class="soup-text" :key="soup">{{ soup }}</p>
          </Transition>
          <i class="soup-quote-right">"</i>
        </div>
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
.announcement-container {
  position: relative;
  margin-top: 70px; // 为动漫少女预留空间
}

.anime-girl {
  position: absolute;
  top: -85px; // 向上偏移以实现坐在容器上的效果
  left: 50%;
  transform: translateX(-50%);
  z-index: 1;
  
  img {
    max-height: 120px;
    width: auto;
    // 添加一些阴影效果增强立体感
    filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.1));
    // 图片禁止拖拽
    -webkit-user-drag: none;
    user-drag: none;
  }
}

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

.soup-container {
  position: relative;
  padding: 10px 25px;
  margin: 10px 0;

  .soup-text {
    font-size: 15px;
    line-height: 1.6;
    color: var(--el-text-color-primary);
    text-align: center;
    font-style: italic;
    margin: 0;
    padding: 0 10px;
    transition: all 0.3s ease;
  }

  .soup-quote-left,
  .soup-quote-right {
    position: absolute;
    font-size: 32px;
    color: var(--el-color-primary-light-7);
    font-family: "Times New Roman", serif;
    opacity: 0.6;
  }

  .soup-quote-left {
    top: -5px;
    left: 5px;
  }

  .soup-quote-right {
    bottom: -15px;
    right: 5px;
  }
}

// 添加过渡动画样式
.fade-enter-active,
.fade-leave-active {
  transition: all 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: translateY(10px);
}

.fade-enter-to,
.fade-leave-from {
  opacity: 1;
  transform: translateY(0);
}
</style>