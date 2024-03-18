<!--
* @Author: Zhang Yuming
* @Date: 2023-07-03 11:48:31
* @Description: 音乐进度组件
-->
<script setup>
import { ref, watch, defineComponent } from "vue";

import { music } from "@/store/modules/music";

defineComponent({
  name: "ProgressLine",
});

const props = defineProps({
  schedule: {
    // 进度
    type: Number,
    default: 0,
  },
});

const currentSchedule = ref(0);

const handleChange = () => {
  // 先打断自动设置播放进度
  music().setIsUseProgress(true);
  // 修改pina里的当前播放进度
  music().setCurrentTime(currentSchedule.value);
};

watch(
  () => props.schedule,
  (newV) => {
    currentSchedule.value = newV;
  },
  {
    immediate: true,
  }
);
</script>

<template>
  <div ref="musicRef" class="music-line">
    <el-slider v-model="currentSchedule" :step="0.1" :show-tooltip="false" @change="handleChange" />
  </div>
</template>

<style lang="scss" scoped>
.music-line {
  position: relative;
  width: 100%;
}
.iconfont {
  font-size: 1.5rem;
  font-weight: bold;
}

:deep(.el-slider) {
  height: 6px;
}

:deep(.el-slider__bar) {
  background-color: var(--music-main-active);
  border-radius: 0px !important;
}

:deep(.el-slider__button) {
  width: 6px;
  height: 6px;
  border: solid 3px var(--music-main-active);
}

:deep(.el-slider.is-vertical .el-slider__runway) {
  width: 4px;
  margin: 0;
}

:deep(.el-slider.is-vertical .el-slider__button-wrapper) {
  left: -16px;
}

:deep(.el-slider.is-vertical .el-slider__bar) {
  width: 4px;
}
</style>
