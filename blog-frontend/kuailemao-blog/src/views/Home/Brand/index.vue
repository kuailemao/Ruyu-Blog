<template>
  <div class="brand-container">
    <div class="brand">
      <!-- 标题 -->
      <p class="artboard">{{ useWebsite?.webInfo?.websiteName }}</p>
      <!-- 打字机 -->
      <div class="brand-text">
        <div class="title">
          {{obj.output}}
          <span class="easy-typed-cursor">|</span>
        </div>
      </div>
    </div>
    <!-- 波浪 -->
    <Wave></Wave>
    <!-- 向下按钮 -->
    <SvgIcon class="arrow-down" name="jt_x" width="50px" height="50px" @click="scrollDown"/>
  </div>
</template>

<script setup lang="ts">

import useWebsiteStore from "@/store/modules/website.ts";
import {getSoupTyping} from "@/apis/thirdParty";

const useWebsite = useWebsiteStore()

const obj = reactive({
  output: "",
  isEnd: false,
  speed: 300,
  singleBack: false,
  sleep: 3000,
  type: "rollback",
  backSpeed: 100,
  sentencePause: false,
});

const scrollDown = () => {
  window.scrollTo({
    behavior: "smooth",
    top: document.documentElement.clientHeight,
  });
};

onMounted(() => {
  getSoupTyping(obj)
});

</script>

<style lang="scss" scoped>

.brand-container {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  position: relative;
  width: 100%;
  height: 100vh;
  min-height: 10rem;
  color: var(--header-text-color);
}

.brand {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  position: fixed;
  z-index: -1;
  top: 15em;

  .artboard {
    font-family: "Fredericka the Great", Mulish, -apple-system, "PingFang SC", "Microsoft YaHei",
    sans-serif;
    font-size: 4.5em;

    @media (max-width: 500px) {
      font-size: 3em;
      // 字体大小过渡效果
      @keyframes titleScale {
        0% {
          transform: scale(1);
        }
        50% {
          transform: scale(1.1);
        }
        100% {
          transform: scale(1);
        }
      }
    }
    line-height: 1.5;
    animation: titleScale 1s;
    color: white;
    text-shadow: 0 1px 0 hsl(174, 5%, 80%), 0 2px 0 hsl(174, 5%, 75%),
    0 3px 0 hsl(174, 5%, 70%), 0 4px 0 hsl(174, 5%, 66%),
    0 5px 0 hsl(174, 5%, 64%), 0 6px 0 hsl(174, 5%, 62%),
    0 7px 0 hsl(174, 5%, 61%), 0 8px 0 hsl(174, 5%, 60%),
    0 0 5px rgba(0, 0, 0, 0.05), 0 1px 3px rgba(0, 0, 0, 0.2),
    0 3px 5px rgba(0, 0, 0, 0.2), 0 5px 10px rgba(0, 0, 0, 0.2),
    0 10px 10px rgba(0, 0, 0, 0.2), 0 20px 20px rgba(0, 0, 0, 0.3);
  }

  .brand-text{
    // 白色半透明背景
    background: rgba(255, 255, 255, 0.5);
    padding: 0.5em;
    border-radius: 0.5em;
  }

  .title {
    letter-spacing: 0.1em;
    background: linear-gradient(90deg, #f79533, #f37055, #ef4e7b, #a166ab, #5073b8, #1098ad, #07b39b, #6fba82);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    font-weight: 700;
    font-size: 1.5rem;
    @media (max-width: 500px) {
      font-size: 1em;
    }
  }
}

.easy-typed-cursor {
  opacity: 0;
  -webkit-animation: blink 0.7s infinite;
  -moz-animation: blink 0.7s infinite;
  animation: blink 0.7s infinite;
  background: linear-gradient(90deg, #f79533, #f37055, #ef4e7b, #a166ab, #5073b8, #1098ad, #07b39b, #6fba82);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  @media (max-width: 500px) {
    font-size: 1em;
  }
}

.arrow-down {
  position: absolute;
  bottom: 15vh;
  -webkit-animation: arrow-shake 1.5s ease-out infinite;
  animation: arrow-shake 1.5s ease-out infinite;
  cursor: pointer;
  z-index: 8;
}

@media (max-width: 767px) {
  .brand-container {
    padding: 3rem 0.5rem 0;
  }
}

@media (min-width: 760px) {
  .title {
    font-size: 1.5rem;
  }
}

@keyframes arrow-shake {
  0% {
    opacity: 1;
    transform: translateY(0);
  }

  30% {
    opacity: 0.5;
    transform: translateY(25px);
  }

  100% {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes blink {
  0% {
    opacity: 0;
  }

  100% {
    opacity: 1;
  }
}
</style>
