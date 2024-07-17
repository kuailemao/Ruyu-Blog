<script setup lang="ts">
import {throttle} from "@/utils/optimize.ts";

// 距离顶部高度
const top = ref(0)
window.addEventListener("scroll", throttle(() => {
  top.value = window.scrollY || document.documentElement.scrollTop || document.body.scrollTop || 0
}, 200));

// 返回顶部
function backToTop() {
  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  });
}
</script>

<template>
  <transition name="el-zoom-in-bottom">
    <div v-if="top >= 1000" @click="backToTop">
      <el-tooltip
          effect="light"
          content="回到顶部"
          placement="right"
      >
        <div>
          <svg-icon name="back_to_top" class="back-to-top"/>
        </div>
      </el-tooltip>
    </div>
  </transition>
</template>

<style scoped lang="scss">

.back-to-top {
  height: 30px !important;
  width: 30px !important;

  @media screen and (max-width: 768px) {
    height: 30px !important;
    width: 30px !important;
  }
}


</style>