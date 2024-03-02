<script setup lang="ts">
import Main from "@/views/Home/Main/index.vue";
import Images from "@/views/Home/Images/index.vue";
import Brand from "@/views/Home/Brand/index.vue";

</script>
<template>
  <div>
    <Images/>
    <Brand/>
    <div class="bg">
      <Main/>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.bg {
  // 过渡时间
  transition: all 1s ease !important;
  background-color: var(--mao-background-color);
}
</style>