<!--
 * @Author: Zhang Yuming
 * @Date: 2023-06-26 15:48:34
 * @Description: 音乐播放器 控制音乐播放、暂停等
 -->
<script setup>
import { defineComponent, onMounted } from "vue";

import { storeToRefs } from "pinia";
import { music } from "@/store/modules/music";

import { ElNotification } from "element-plus";
import { calcMusicTime } from "../../musicTool";

defineComponent({
  name: "AudioControls",
});

// 音乐播放器的一些参数 使用storetoRefs是为了保持数据的响应式
const { getIsPaused, getCurrentTime, getDuration } = storeToRefs(music());

const play = () => {
  music().togglePlay();
};

// 上一首
const prev = async () => {
  music().setNext(false);
};

// 下一首
const next = async () => {
  music().setNext(true);
};

onMounted(() => {
  ElNotification({
    offset: 60,
    title: "欢迎访问我的个人博客~",
  });
  music().init();
  ElNotification({
    offset: 120,
    title: "左下角听听歌吧～",
  });
});
</script>

<template>
  <div class="audio-controls">
    <div class="left">
      <i class="iconfont icon-shangyiqu change-color" @click="prev"></i>
      <i class="iconfont icon-zanting change-color" v-if="getIsPaused" @click="play"></i>
      <i class="iconfont icon-bofangzhong change-color" v-else @click="play"></i>
      <i class="iconfont icon-xiayiqu change-color" @click="next"></i>
    </div>
    <TimeVolume
      class="right"
      :currentTime="calcMusicTime(getCurrentTime)"
      :duration="calcMusicTime(getDuration)"
    />
  </div>
</template>

<style lang="scss" scoped>
.audio-controls {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-left: 15%;
  .left {
    width: 20%;
    display: flex;
    align-items: center;
    justify-content: space-around;
  }

  .right {
    position: relative;
    width: 30%;
    display: flex;
    justify-content: space-around;
    align-items: center;

    .time {
      font-size: 1rem;
    }
  }
}
.iconfont {
  font-size: 1.8rem;
}

.icon-zanting,
.icon-bofangzhong {
  font-size: 2.8rem;
}
.change-color:hover {
  cursor: pointer;
  color: var(--music-main-active);
}

@media screen and (max-width: 768px) {
  .audio-controls {
    padding-left: 5% !important;
  }
  .left {
    width: 40% !important;
  }
  .right {
    width: 60% !important;
  }
}
</style>
