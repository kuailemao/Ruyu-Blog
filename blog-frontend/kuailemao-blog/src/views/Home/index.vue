<script setup lang="ts">
import Main from "@/views/Home/Main/index.vue";
import Images from "@/views/Home/Images/index.vue";
import Brand from "@/views/Home/Brand/index.vue";
import Particles from "@/components/Particles/index.vue";
import MouseTrail from "@/components/MouseTrail/index.vue";
import ScrollParallax from "@/components/ScrollParallax/index.vue";

</script>
<template>
  <div class="home_container">
    <MouseTrail/>
    <Images/>
    <Particles/>
    <Brand/>
    <div class="bg">
      <ScrollParallax :speed="0.1" direction="up">
        <Main/>
      </ScrollParallax>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.bg {
  // 过渡时间
  transition: all 1s ease !important;
  background-color: var(--mao-background-color);
}

.home_container{
  // 浏览器宽度 - 滚动条宽度，设置为100vw首页底部会出现滚动条
  width: 100%;
  // 屏幕小910
  @media screen and (max-width: 910px) {
    width: 100vw;
  }
}
</style>