<!--
 * @Author: Zhang Yuming
 * @Date: 2023-07-03 11:48:12
 * @Description: 展示歌信息
 -->

<script setup>
import { defineComponent } from "vue";
import { music } from "@/store/index";
import { storeToRefs } from "pinia";

defineComponent({
  name: "Information",
});

const { getShowLyricBoard } = storeToRefs(music());

defineProps({
  // 当前播放的音乐
  musicInfo: {
    type: Object,
    default: () => {},
  },
  // 是否正在切换图片
  isToggleImg: {
    type: Boolean,
    default: false,
  },
  // 是否正在暂停
  isPaused: {
    type: Boolean,
    default: false,
  },
});
</script>

<template>
  <!-- 唱片展示 -->
  <div class="music-info">
    <img
      :class="['music-img', isToggleImg ? '' : 'disc-rotate', isPaused ? 'paused' : 'running']"
      @click="music().setShowLyricBoard(!getShowLyricBoard)"
      :src="musicInfo.al.picUrl"
    />
    <div class="music-desc">
      <div class="music-name">
        {{ musicInfo.name }}
      </div>
      <div class="author-name">
        {{ musicInfo.ar[0].name }}
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.music-info {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  cursor: pointer;
  .music-img {
    width: 60px;
    height: 60px;
    border-radius: 30px;
    object-fit: cover;
  }
  .music-desc {
    margin-left: 0.5rem;

    .author-name {
      font-size: 0.8rem;
      margin-top: 5px;
      overflow: hidden;
      text-wrap: nowrap;
      word-break: keep-all;
      text-overflow: ellipsis;
    }

    .music-name {
      font-size: 0.7rem;
      overflow: hidden;
      text-wrap: nowrap;
      word-break: keep-all;
      text-overflow: ellipsis;
    }
  }
}

.disc-rotate {
  animation: rotate360 18s infinite linear;
}

.running {
  animation-play-state: running;
}
.paused {
  animation-play-state: paused;
}

@keyframes rotate360 {
  0% {
    transform: rotate(0deg);
  }

  100% {
    transform: rotate(360deg);
  }
}

// mobile
@media screen and (max-width: 768px) {
  .music-img {
    // display: none;
    width: 50px !important;
    height: 50px !important;
    border-radius: 25px !important;
  }

  .music-desc {
    max-width: 4.5rem;
  }
}
</style>
