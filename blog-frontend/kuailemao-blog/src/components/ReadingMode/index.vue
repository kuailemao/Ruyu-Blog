<script setup lang="ts">

</script>

<template>
  <div class="my-4">
    <el-tooltip
        effect="light"
        content="阅读模式"
        placement="right"
    >
      <div>
        <svg-icon name="reading_mode" class="back-to-top"/>
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