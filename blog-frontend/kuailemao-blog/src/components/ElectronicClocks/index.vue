<script setup lang="ts">

import Card from "@/components/Card/index.vue";

// 日期
const time = ref()
const date = ref()
const week = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'];
const timerID = setInterval(updateTime, 1000);
updateTime();

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
</script>

<template>
  <Card title="电子时钟" prefix-icon="time" :isRotate="true" :isScale="true">
    <div id="clock">
      <p class="date">{{ date }}</p>
      <p class="time">{{ time }}</p>
    </div>
  </Card>
</template>

<style scoped lang="scss">
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