<script setup lang="ts">

// 返回底部
function backToBottom() {
  window.scrollTo({
    top: document.documentElement.scrollHeight,
    behavior: 'smooth'
  });
}
</script>

<template>
  <div @click="backToBottom">
    <el-tooltip
        effect="light"
        content="查看评论"
        placement="right"
    >
      <div>
        <svg-icon name="comment" class="back-to-top"/>
      </div>
    </el-tooltip>
  </div>
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