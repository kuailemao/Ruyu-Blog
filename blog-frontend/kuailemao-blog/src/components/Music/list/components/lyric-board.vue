<script setup>
import { watch, onMounted, onBeforeUnmount, nextTick, ref } from "vue";
import { music } from "@/store/modules/music";
import { storeToRefs } from "pinia";
import { gsapTransLyric } from "@/utils/transform";

import SpecialTitle from "./special-title.vue";
import { debounce } from "@/utils/tool";

let lyricBox, timer, timer1, timer2, timer3, timer4;

const {
  getMusicInfo,
  getIsToggleImg,
  getMusicDescription,
  getIsPaused,
  getCurrentLyticIndex,
  getShowLyricBoard,
  getLyricType,
} = storeToRefs(music());

const replaceUrl = ref("");
const fileList = ref([]);
const brightness = ref(0.3);
const showControl = ref(false);
const isMobile = ref(false);

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

const scrollToMiddle = () => {
  nextTick(() => {
    let current = document.getElementById("currentLyticIndex");

    if (!current) return;

    let h = current
      ? current.offsetTop -
        Math.round(lyricBox.clientHeight / 2) +
        Math.round(current.offsetHeight / 2) +
        -30
      : 0;

    lyricBox &&
      lyricBox.scrollTo({
        top: h,
        behavior: "smooth",
      });
  });
};

const fullScreen = () => {
  const app = document.querySelector(".lyric-mask");
  if (document.fullscreenElement) {
    document.exitFullscreen();
  } else {
    app.requestFullscreen();
  }
};

const goToIndex = (index) => {
  music().setCurrentTimeByClickLyric(index);
};

const toggleLyricType = (type) => {
  music().setLyricType(type);

  if (type == "COMMON") {
    nextTick(() => {
      scrollToMiddle();
    });
  } else {
    gsapTransLyric(".special-title", 0.8);
    animateTitle();
  }
};

const animateTitle = () => {
  nextTick(() => {
    if (timer3) {
      clearTimeout(timer3);
      timer3 = null;
    }
    timer3 = setTimeout(() => {
      gsapTransLyric(".special-title", 0.8, true);
    }, 3600);
  });
};

const calcLyricDuration = () => {
  const nextTime = getMusicInfo.value.lyricTimeList[getCurrentLyticIndex.value + 1];
  const currentTime = getMusicInfo.value.lyricTimeList[getCurrentLyticIndex.value];

  const duration = nextTime - currentTime;

  const fromDuration = duration >= 1500 ? 1.2 : 0;
  const leaveDuration = duration >= 1500 ? 0.3 : 0;

  return {
    duration,
    fromDuration,
    leaveDuration,
  };
};

const animateSpecial = () => {
  const { duration, fromDuration, leaveDuration } = calcLyricDuration();

  if (!duration || duration < 1500) {
    document.querySelector(".special-lyric").style.opacity = 1;
    if (timer) {
      clearTimeout(timer);
    }
    timer = setTimeout(() => {
      document.querySelector(".special-lyric").style.opacity = 0;
    }, duration - 200);
  } else {
    if (timer1) {
      clearTimeout(timer1);
      timer1 = null;
    }
    timer1 = setTimeout(() => {
      gsapTransLyric(".special-lyric", fromDuration);
    });
    if (timer2) {
      clearTimeout(timer2);
      timer2 = null;
    }
    timer2 = setTimeout(
      () => {
        gsapTransLyric(".special-lyric", leaveDuration, true);
      },
      duration - leaveDuration * 1000
    );
  }
};

const replaceImage = (file) => {
  replaceUrl.value = URL.createObjectURL(file.raw);
};
const closeBoard = () => {
  music().setShowLyricBoard(false);
};

const toggleDisc = () => {
  music().setIsShow();
};
const changeBrightness = (flag) => {
  if (flag) {
    if (brightness.value < 1) {
      brightness.value += 0.1;
    }
  } else {
    if (brightness.value > 0.1) {
      brightness.value -= 0.1;
    }
  }
};

const resize = debounce(() => {
  // 当前视口宽度
  let w = document.documentElement.clientWidth || document.body.clientWidth;
  if (w > "798") {
    isMobile.value = false;
  } else {
    isMobile.value = true;
  }
}, 50);

// 鼠标进来显示 控制
const mouseEnter = () => {
  if (timer4) {
    clearTimeout(timer4);
    timer4 = null;
  }
  showControl.value = true;
};
// 鼠标移出隐藏 控制
const mouseLeave = () => {
  if (timer4) {
    clearTimeout(timer4);
    timer4 = null;
  }

  timer4 = setTimeout(() => {
    showControl.value = false;
  }, 2000);
};

watch(
  () => getCurrentLyticIndex.value,
  () => {
    if (getLyricType.value == "COMMON") {
      scrollToMiddle();
    } else {
      nextTick(() => {
        animateSpecial();
      });
    }
  }
);

watch(
  () => getShowLyricBoard.value,
  (newV) => {
    if (newV) {
      nextTick(() => {
        scrollToMiddle();
      });
    } else {
      showControl.value = false;
    }
  }
);
watch(
  () => getMusicInfo.value.id,
  (newV) => {
    if (getLyricType.value == "SPECIAL") {
      if (newV) {
        gsapTransLyric(".special-title", 0.8);
        animateTitle();
      }
    }
  }
);

watch(
  () => getIsPaused.value,
  (newV) => {
    if (getLyricType.value == "SPECIAL") {
      if (newV) {
        gsapTransLyric(".special-title", 0.5);
        clearTimeout(timer2);
        timer2 = null;
      } else {
        animateTitle();
      }
    }
  },
  {
    immediate: true,
  }
);

onMounted(() => {
  lyricBox = document.getElementById("lyricBox");

  window.addEventListener("resize", resize);
});

onBeforeUnmount(() => {
  clearTimeout(timer);
  clearTimeout(timer1);
  clearTimeout(timer2);
  clearTimeout(timer3);
  timer = null;
  timer1 = null;
  timer2 = null;
  timer3 = null;
  window.removeEventListener("resize", resize);
});
</script>

<template>
  <div
    :class="['lyric-mask', getShowLyricBoard ? 'lyric-mask-show' : 'lyric-mask-hide']"
    :style="{
      backgroundImage:
        getLyricType == 'SPECIAL' ? `url(${replaceUrl || getMusicDescription.al.picUrl})` : '',
    }"
  >
    <div
      v-if="getLyricType == 'SPECIAL'"
      class="special-mask"
      :style="{ backgroundColor: `rgba(0,0,0, ${brightness})` }"
    ></div>
    <div
      v-show="getLyricType == 'COMMON'"
      class="!w-[100%] !h-[100%] flex justify-between items-center"
    >
      <div class="left">
        <div class="text-4xl font-semibold">
          {{ getMusicDescription?.name }}
        </div>
        <div class="disc-box">
          <img
            :class="[
              'music-img',
              'animate__animated',
              'animate__fadeIn',
              getIsToggleImg ? '' : 'disc-rotate',
              getIsPaused ? 'paused' : '',
            ]"
            :src="getMusicDescription?.al?.picUrl"
            @click="fullScreen"
          />
        </div>
      </div>
      <div id="lyricBox" class="right">
        <div class="!p-[10px]">
          <div>
            <div class="text-2xl leading-loose text-center">
              {{ getMusicDescription?.name }}
            </div>
          </div>
          <div
            :id="getCurrentLyticIndex == index ? 'currentLyticIndex' : ''"
            :class="['lyric-word', getCurrentLyticIndex == index ? 'current' : '']"
            v-for="(item, index) in getMusicInfo.lyricList"
            :key="index"
            @click="goToIndex(index)"
          >
            {{ item }}
          </div>
        </div>
      </div>
    </div>
    <div
      v-show="getLyricType == 'SPECIAL'"
      class="special !w-[100%] !h-[100%] flex flex-col justify-center items-center"
    >
      <div class="special-title">
        <SpecialTitle :title="`《 ${getMusicDescription?.name} 》`" @click="fullScreen" />

        <div class="author text-2xl">
          <span class="brightness" @click="changeBrightness(false)"></span> --
          {{ getMusicDescription?.ar[0]?.name }}
          <span class="brightness" @click="changeBrightness(true)"></span>
        </div>
      </div>
      <span class="special-lyric text-3xl">
        {{ getMusicInfo.lyricList[getCurrentLyticIndex] }}
      </span>
    </div>

    <div class="bottom-control" @mouseenter="mouseEnter" @mouseleave="mouseLeave">
      <div
        :class="[
          'control',
          isMobile || getIsPaused || showControl ? 'show-control' : 'hide-control',
        ]"
      >
        <i class="close-board iconfont icon-arrowdown change-color" @click="closeBoard"></i>
        <div class="control-group">
          <i class="iconfont icon-shangyiqu change-color" @click="prev"></i>
          <i class="iconfont icon-zanting change-color" v-if="getIsPaused" @click="play"></i>
          <i class="iconfont icon-bofangzhong change-color" v-else @click="play"></i>
          <i class="iconfont icon-xiayiqu change-color" @click="next"></i>
        </div>
        <div class="toggle-type">
          <span class="type-btn mr-[10px]" v-show="getLyricType == 'SPECIAL'">
            <el-upload
              v-model:file-list="fileList"
              action="#"
              :multiple="false"
              :auto-upload="false"
              :show-file-list="false"
              :on-change="replaceImage"
            >
              <i class="iconfont icon-icon-test2"></i>
            </el-upload>
          </span>
          <span
            class="type-btn mr-[10px]"
            v-show="getLyricType == 'SPECIAL'"
            @click="toggleLyricType('COMMON')"
            ><i class="iconfont icon-icon-test1"
          /></span>
          <span
            class="type-btn mr-[10px]"
            v-show="getLyricType == 'COMMON'"
            @click="toggleLyricType('SPECIAL')"
            ><i class="iconfont icon-icon-test"
          /></span>
          <i class="iconfont icon-off-search change-color" @click="toggleDisc"></i>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.lyric-mask {
  position: absolute;
  top: 0px;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 2000;
  padding: 35px 0;
  background-color: var(--global-white);
  overflow: hidden;
  display: none;
  background-position: center center;
  background-size: cover;
  background-repeat: no-repeat;

  .special-mask {
    position: absolute;
    top: 0;
    left: 0;
    bottom: 0;
    right: 0;
    z-index: 1;
  }

  .left {
    width: 50%;
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }

  .right {
    padding: 20% 5%;
    width: 50%;
    height: 100%;
    display: flex;
    align-items: flex-start;
    justify-content: center;
    overflow: auto;
    scrollbar-width: none;
    &::-webkit-scrollbar {
      display: none;
    }
  }
}
.special {
  &-title {
    color: var(--global-white);
    cursor: pointer;
    z-index: 2;
    height: 180px;
  }

  .author {
    line-height: 2.8;
    color: var(--global-white);
    text-shadow: 0px 0px 5px var(--global-white);
    cursor: pointer;
    text-align: center;
  }

  .brightness {
    cursor: pointer;
    display: inline-block;
    width: 20px;
    height: 20px;
  }

  .special-lyric {
    color: var(--global-white);
    text-align: center;
    z-index: 2;
    font-smooth: never;
    height: 60px;
    line-height: 60px;
    text-shadow: 0px 0px 5px var(--global-white);
  }
}

.iconfont {
  font-size: 18px;
}

.bottom-control {
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 100%;
  height: 80px;
  z-index: 9999;
  display: flex;
  justify-content: center;
  align-items: center;

  .control {
    width: 100%;
    margin: 0 10px;
    padding: 10px 20px;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: var(--shadow-mask-bg);
    border-radius: 60px;

    &-group {
      width: 15%;
      display: flex;
      justify-content: space-around;
      align-items: center;
    }
  }

  .close-board {
    position: absolute;
    left: 50px;
    top: 50%;
    transform: translateY(-50%);
  }

  .toggle-type {
    position: absolute;
    right: 50px;
    top: 50%;
    transform: translateY(-50%);
    display: flex;
    align-items: center;
    .type-btn {
      cursor: pointer;
      color: var(--global-white);

      &:hover {
        color: var(--music-main-active);
      }
    }
  }

  .iconfont {
    font-size: 1.8rem;
    color: var(--global-white);
  }

  .icon-zanting,
  .icon-bofangzhong {
    font-size: 2.8rem;
  }
  .change-color:hover {
    cursor: pointer;
    color: var(--music-main-active);
  }
}

.lyric-mask-show {
  display: flex;
  animation: showBoard 0.3s ease-in-out forwards;
}
.lyric-mask-hide {
  animation: hideBoard 0.3s ease-in-out forwards;
}
@keyframes showBoard {
  0% {
    top: 100%;
  }
  100% {
    top: 0;
  }
}

@keyframes hideBoard {
  0% {
    top: 0;
    display: block;
  }
  100% {
    top: 100%;
    display: none;
  }
}

.show-control {
  display: flex;
  animation: showControl 0.6s ease-in-out forwards;
}

.hide-control {
  animation: hideControl 0.6s ease-in-out forwards;
}

@keyframes showControl {
  0% {
    opacity: 0;
    transform: translateY(30px);
  }

  100% {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes hideControl {
  0% {
    opacity: 1;
    transform: translateY(0);
  }

  100% {
    opacity: 0;
    transform: translateY(30px);
    display: none;
  }
}

.disc-rotate {
  animation: rotate360 36s infinite linear;
}

.paused {
  animation-play-state: paused;
}

.disc-box {
  position: relative;
  width: 30rem;
  height: 30rem;
  display: grid;
  place-items: center;

  .disc {
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
  }
  .music-img {
    width: 20rem;
    height: 20rem;
    border-radius: 10rem;
    object-fit: cover;
    cursor: pointer;
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
.lyric-word {
  text-align: center;
  line-height: 2;
  cursor: pointer;
  font-size: 1.2rem;
  transition: all 0.3s;
  opacity: 0.5;
}
.current {
  font-size: 1.5rem;
  font-weight: bold;
  opacity: 1;
}

// mobile
@media screen and (max-width: 768px) {
  .lyric-mask {
    .right {
      width: 100%;
    }
    .left {
      display: none;
    }
  }

  .control-group {
    width: 30% !important;
  }

  .close-board {
    left: 30px !important;
  }

  .toggle-type {
    right: 30px !important;
  }
}
</style>
