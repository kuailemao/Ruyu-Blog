<script setup lang="ts">
import {MdCatalog} from 'md-editor-v3';

const scrollElement = document.documentElement;
const id = 'preview-only';

</script>

<template>
  <div class="card">
    <div class="title">
      <div class="title_text">
        <SvgIcon class="scale" name="directory" width="30" height="30"/>
        <span style="margin-left: 10px">目录</span>
      </div>
    </div>
    <div style="height: 16em">
      <el-scrollbar>
        <MdCatalog :editorId="id" :scrollElement="scrollElement"/>
      </el-scrollbar>
    </div>
  </div>
</template>

<style scoped lang="scss">

.card {
  background-color: var(--el-bg-color);
  width: $card-width;
  margin: $card-margin;
  border: 1px solid var(--el-border-color);
  border-radius: $border-radius;
  // 添加阴影
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  .title {
    border-radius: $border-radius $border-radius 0 0;
    background-color: var(--el-bg-color);
    z-index: 2;
    display: flex;
    padding: 10px;
    justify-content: space-between;
    align-items: center;
    font-size: 20px;
    // 文字跟图标对齐
    .title_text {
      display: flex;
      align-items: center;

      // 放大缩小效果
      .scale {
        animation: scale 1s infinite;
        transform-origin: center;

        @keyframes scale {
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
    }
  }
}
</style>