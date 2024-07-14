<!-- 侧边栏 -->
<template>
  <div>
    <div>
      <InfoCard/>
    </div>
    <div>
      <Card title="公告" prefixIcon="announcement" suffix-icon="jt_y" :isDithering="true" :isArrow="true">
        <p>{{ useWebsite.webInfo?.sidebarAnnouncement }}</p>
      </Card>
    </div>
    <div>
      <Card title="电子时钟" prefix-icon="time" :isRotate="true" :isScale="true">
        <div id="clock">
          <p class="date">{{ date }}</p>
          <p class="time">{{ time }}</p>
        </div>
      </Card>
    </div>
    <div>
      <RandomArticle/>
    </div>
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
          <div>最后更新：<span>{{ useWebsite.webInfo?.lastUpdateTime }}</span></div>
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

const useWebsite = useWebsiteStore()

// 日期
const time = ref()
const date = ref()
const week = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'];
const timerID = setInterval(updateTime, 1000);
updateTime();

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


function updateTime() {
  const cd = new Date();
  time.value = zeroPadding(cd.getHours(), 2) + ':' + zeroPadding(cd.getMinutes(), 2) + ':' + zeroPadding(cd.getSeconds(), 2);
  date.value = zeroPadding(cd.getFullYear(), 4) + '-' + zeroPadding(cd.getMonth() + 1, 2) + '-' + zeroPadding(cd.getDate(), 2) + ' ' + week[cd.getDay()];
}

function zeroPadding(num: number, digit: number) {
  var zero = '';
  for (var i = 0; i < digit; i++) {
    zero += '0';
  }
  return (zero + num).slice(-digit);
}

// 销毁
onUnmounted(() => {
  clearInterval(timerID);
})

// 每日鸡汤
const soup = ref('')

function soupSub() {
  getSoup().then((res: any) => {
    soup.value = res.hitokoto
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

#clock {

  font-family: 'Share Tech Mono', monospace;
  color: #ffffff;
  text-align: center;
  // 夜间模式
  //color: #daf6ff;
  //text-shadow: 0 0 20px rgba(10, 175, 230, 1),  0 0 20px rgba(10, 175, 230, 0);
  color: grey;
  text-shadow: 0 0 20px rgba(0, 0, 0, 0.2), 0 0 20px rgba(0, 0, 0, 0);

  .time {
    letter-spacing: 0.05em;
    font-size: 40px;
    padding: 5px 0;
  }

  .date {
    letter-spacing: 0.1em;
    margin-bottom: 5px;
    font-size: 15px;
  }
}

</style>