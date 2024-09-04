<script setup lang="ts">
import {MdCatalog} from 'md-editor-v3';

const scrollElement = document.documentElement;
const id = 'preview-only';

</script>

<template>
  <div class="card px-[10px]" v-slide-in>
    <div class="title">
      <div class="title_text">
        <SvgIcon class="scale" name="directory" width="30" height="30"/>
        <span style="margin-left: 10px">目录</span>
      </div>
    </div>
    <div class="min-h-[16em] max-h-[40em] overflow-y-auto">
      <MdCatalog :editorId="id" :scrollElement="scrollElement"/>
    </div>
  </div>
</template>

<style scoped lang="scss">

// 目录
:deep(.md-editor-catalog-active){
  & > span{
    background: var(--el-color-primary-light-9);
    color: var(--el-color-primary-light-4);
    border-radius: $border-radius;
    font-weight: bold;
    //transition: all .6s;
    padding: 0.5rem 0 0.5rem 0.5rem;
  }
}

:deep(.md-editor-catalog){
  span:hover{
    color: var(--el-color-primary-light-5);
  }
}

:deep(.md-editor-catalog-link){
  margin: 0;
  padding-top: 0;
  padding-bottom: 0;
  span{
    margin: 0.25rem 0.25rem;
  }
}

:deep(.md-editor-catalog-wrapper > .md-editor-catalog-link:first-of-type) {
  padding-top: 0;
}

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