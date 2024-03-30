<template>
  <div class="brand-container">
    <div class="brand">
      <!-- 标题 -->
      <p class="artboard">{{ useWebsite?.webInfo?.websiteName }}</p>
      <!-- 打字机 -->
      <div class="title">
        {{obj.output}}
        <span class="easy-typed-cursor">|</span>
      </div>
    </div>
    <!-- 波浪 -->
    <Wave></Wave>
    <!-- 向下按钮 -->
    <SvgIcon class="arrow-down" name="jt_x" width="50px" height="50px" @click="scrollDown"/>
  </div>
</template>

<script setup lang="ts">

import EasyTyper from "easy-typer-js";
import useWebsiteStore from "@/store/modules/website.ts";

const useWebsite = useWebsiteStore()

const obj = reactive({
  output: "",
  isEnd: false,
  speed: 300,
  singleBack: false,
  sleep: 0,
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
  fetchData();
});

const fetchData = () => {
  fetch("https://v1.hitokoto.cn?timestamp=" + Date.now())
      .then((res) => {
        return res.json();
      })
      .then(({ hitokoto }) => {
        new EasyTyper(
            obj,
            hitokoto,
            () => {
              fetchData()
            },
            () => { }
        );
      });
};

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
    font-size: 3.5em;

    @media (max-width: 500px) {
      font-size: 2em;
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

  .title {
    letter-spacing: 0.1em;
  }
}

.easy-typed-cursor {
  opacity: 0;
  -webkit-animation: blink 0.7s infinite;
  -moz-animation: blink 0.7s infinite;
  animation: blink 0.7s infinite;
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
