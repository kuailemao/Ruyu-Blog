<script setup lang="ts">

import {Close} from "@element-plus/icons-vue";
import {MdCatalog} from 'md-editor-v3';

const props = defineProps<{
  id: string,
  scrollElement: HTMLElement,
  isShowMoveCatalog: boolean,
}>()

// 自定义事件
const emit = defineEmits<{
  (e: 'update:isShowMoveCatalog',value: boolean): void
}>()

const isShowMoveCatalog = computed(() => props.isShowMoveCatalog)

// 监听关闭事件
const handleScroll = () => {
  emit('update:isShowMoveCatalog',false)
}
</script>

<template>
  <div v-if="scrollElement">
    <el-drawer v-model="isShowMoveCatalog" :with-header="true" size="60%" direction="rtl" :show-close="false" :before-close="handleScroll">
      <template #header>
        <span style="font-size: 1.2rem">目录</span>
        <el-button :icon="Close" style="background: none;font-size: 1.5rem;width: 30px;border: none"
                   @click="handleScroll()"/>
      </template>
      <template #default>
        <div class="move_catalog">
          <MdCatalog :editorId="props.id" :scrollElement="props.scrollElement"/>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<style scoped lang="scss">
// 移动端目录
.move_catalog {
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
      margin: 0.3rem 0.3rem;
    }
  }

  :deep(.md-editor-catalog-wrapper > .md-editor-catalog-link:first-of-type) {
    padding-top: 0;
  }
}
</style>