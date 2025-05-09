<template>
  <div class="brand-container">
    <div class="brand">
      <!-- 标题 -->
      <TextGlitch :text="useWebsite?.webInfo?.websiteName || ''" />
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
    <div class="button-container" @click="scrollDown">
      <SvgIcon class="arrow-down" name="jt_x" width="50px" height="50px" />
      <div class="button-ripple"></div>
    </div>
  </div>
</template>

<script setup lang="ts">

import useWebsiteStore from "@/store/modules/website.ts";
import {getSoupTyping} from "@/apis/thirdParty";
import TextGlitch from "@/components/TextGlitch/index.vue";

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

.button-container {
  position: absolute;
  bottom: 15vh;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  z-index: 8;
}

.arrow-down {
  -webkit-animation: arrow-shake 1.5s ease-out infinite;
  animation: arrow-shake 1.5s ease-out infinite;
  z-index: 9;
  position: relative;
}

.button-ripple {
  position: absolute;
  width: 70px;
  height: 70px;
  border-radius: 50%;
  background: linear-gradient(90deg, #f79533, #f37055, #ef4e7b, #a166ab, #5073b8, #1098ad, #07b39b, #6fba82);
  background-size: 300% 300%;
  animation: gradientAnimation 4s ease infinite, ripple 2s ease-out infinite;
  opacity: 0.7;
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

@keyframes gradientAnimation {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}

@keyframes ripple {
  0% {
    transform: scale(0.8);
    opacity: 0.7;
  }
  50% {
    transform: scale(1.2);
    opacity: 0.5;
  }
  100% {
    transform: scale(0.8);
    opacity: 0.7;
  }
}
</style>
