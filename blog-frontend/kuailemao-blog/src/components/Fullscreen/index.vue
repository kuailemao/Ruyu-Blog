<script setup lang="ts">
import screenfull from "screenfull";
import {ElMessage, ElMessageBox} from "element-plus";

function fullscreenFunc() {
  // 网页全屏，如果浏览器不支持提示不支持
  if (screenfull.isEnabled) {
    if (screenfull.isFullscreen) {
      ElMessage.success("退出网页全屏")
      screenfull.exit();
    } else {
      ElMessage.success("进入网页全屏")
      screenfull.toggle();
    }
  } else {
    ElMessageBox.alert("浏览器不支持全屏", "提示", {
      confirmButtonText: "确定",
      type: "warning",
    });
  }
}

</script>

<template>
  <div @click="fullscreenFunc()">
    <el-tooltip
        effect="light"
        content="网页全屏"
        placement="right"
    >
      <div>
        <svg-icon name="fullscreen" class="back-to-top"/>
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