<script setup lang="ts">

</script>

<template>
  <div>
    <el-tooltip
        effect="light"
        content="更多功能"
        placement="right"
    >
      <div class="more_rotating">
        <svg-icon name="bottom_right_more" class="back-to-top"/>
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

.more_rotating{
  animation: rotating 3s linear infinite;
}
</style>