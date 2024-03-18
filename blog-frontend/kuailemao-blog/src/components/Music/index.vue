<!--
* @Author: Zhang Yuming
* @Date: 2023-07-03 11:35:38
* @Description: 播放器首页
-->
<script setup>
import MusicList from "./list/index.vue";
import MusicControl from "./controls/index.vue";
import { defineComponent, ref, watch } from "vue";
import blogAvatar from "@/assets/images/blogAvatar.svg";

import { music } from "@/store/modules/music";
import { storeToRefs } from "pinia";

const { getIsShow, getShowLyricBoard, getIsPaused, getIsToggleImg, getMusicDescription } =
  storeToRefs(music());

defineComponent({
  name: "MusicPlayer",
});

// 页面初始化播放器隐藏的时候不要做动画
const clickFlag = ref(false);

const toggleDisc = () => {
  music().setIsShow();
  if (!clickFlag.value) {
    clickFlag.value = true;
  }
};

const playMusic = () => {
  music().togglePlay();
};

// 在弹出遮罩层的时候 让body不能滚动
watch(
  () => getIsShow.value,
  (newV) => {
    if (newV) {
      document.documentElement.style.overflowY = "hidden";
    } else {
      document.documentElement.style.overflowY = "visible";
    }
  },
  {
    immediate: true,
  }
);
</script>

<template>
  <div
    :class="['music', getIsShow ? 'show-music' : '', !getIsShow && clickFlag ? 'hide-music' : '']"
  >
    <div class="flex justify-center items-center !w-[100%] !h-[100%]">
      <div class="music-box flex flex-col justify-center items-center">
        <i
          v-if="!getShowLyricBoard"
          class="iconfont icon-off-search dark-close change-color"
          @click="toggleDisc"
        ></i>
        <!-- 播放器列表 -->
        <MusicList class="list" />
        <!-- 播放器控制器 -->
        <MusicControl />
      </div>
    </div>
  </div>
  <div v-if="!getIsShow" :class="['music-disc', getIsPaused ? 'music-left' : '']">
    <img
      @click="toggleDisc"
      :class="['music-img', getIsToggleImg ? '' : 'disc-rotate', getIsPaused ? 'paused' : '']"
      :src="getMusicDescription?.al?.picUrl || blogAvatar"
      alt="music cover"
    />
    <div class="info-box">
      <div class="info">
        <div class="text-sm whitespace-nowrap">
          {{ getMusicDescription?.name }}
        </div>
        <div class="text-sm whitespace-nowrap">
          {{ getMusicDescription?.ar[0]?.name || "歌手走丢了" }}
        </div>
      </div>
      <i
        :class="['change-color', 'iconfont', getIsPaused ? 'icon-zanting' : 'icon-bofangzhong ']"
        @click="playMusic"
      ></i>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.music {
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  background-color: var(--shadow-mask-bg);
  z-index: 2002;
  opacity: 0;
  display: none;

  &-box {
    position: relative;
    width: 100%;
    max-width: 1024px;
    background-color: var(--global-white);
    overflow: hidden;
  }

  .list {
    max-width: 1024px;
    height: 100%;
    padding-top: 30px;
    overflow: hidden;
  }

  .control {
    max-width: 1024px;
  }
}
.icon-off-search {
  position: absolute;
  top: 8px;
  right: 8px;
  font-size: 1.4rem;
  z-index: 2001;
  background-color: var(--shadow-button-bg);
  padding: 10px;
  border-radius: 20px;
}

.close-board {
  position: absolute;
  top: 8px;
  left: 8px;
  cursor: pointer;
  font-size: 1.4rem;
  z-index: 2001;
  padding: 10px;
  border-radius: 20px;
  background-color: var(--shadow-button-bg);
}

.show-music {
  display: block;
  animation: showPlayer 0.3s ease-in-out forwards;
}

.hide-music {
  animation: hidePlayer 0.3s ease-in-out forwards;
}

.music-disc {
  position: fixed;
  left: 0;
  box-sizing: border-box;
  background-color: var(--global-shadow-white);
  display: flex;
  align-items: center;
  z-index: 2002;
  cursor: pointer;
  transition: left 0.2s;
  .music-img {
    width: 50px;
    height: 50px;
    border-radius: 25px;
    margin: 5px;
  }

  .info-box {
    display: flex;
    align-items: center;
    transition: width 0.2s;
    width: 0;
    overflow: hidden;
    font-weight: bold;
    letter-spacing: 1px;
    box-sizing: border-box;
  }
}

.change-color:hover {
  cursor: pointer;
  color: var(--music-main-active);
}

.disc-rotate {
  animation: rotate360 18s infinite linear;
}

.paused {
  animation-play-state: paused;
}
// pc
@media screen and (min-width: 768px) {
  .music-disc {
    bottom: 50px;
    height: 60px;
    border-radius: 30px;
    min-width: 60px;

    .music-img {
      width: 50px;
      height: 50px;
      border-radius: 25px;
    }
  }

  .music-disc:hover {
    left: 0;
    .info-box {
      width: 100%;
      padding-right: 10px;
    }
    .info {
      padding-right: 5px;
    }
  }

  .music-box {
    height: 100%;
    max-height: 580px;
    position: relative;
    padding: 0;
    border-radius: 12px;
  }

  .music-left {
    left: -30px;
  }
}

// mobile
@media screen and (max-width: 768px) {
  .music-disc {
    bottom: 17px;
    width: 50px;
    border-radius: 25px;
    min-width: 50px;

    .music-img {
      width: 40px;
      height: 40px;
      border-radius: 20px;
    }
  }
  .music-left {
    left: -25px;
  }

  .music-box {
    width: 100%;
    height: 100%;
  }

  .icon-off-search {
    top: 5px;
  }
  .close-board {
    top: 35px;
  }
}

@keyframes showPlayer {
  0% {
    opacity: 0;
  }

  100% {
    opacity: 1;
  }
}

@keyframes hidePlayer {
  0% {
    opacity: 1;
    display: block;
  }

  100% {
    display: none;
    opacity: 0;
  }
}

@keyframes rotate360 {
  0% {
    transform: rotate(0deg);
  }

  100% {
    transform: rotate(360deg);
  }
}
</style>
