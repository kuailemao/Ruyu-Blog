<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { emojis } from '@/utils/O.o/emoji.ts'
import { heo } from "@/utils/O.o/heo.ts"

// 为预览创建状态变量
const showPreview = ref(false)
const previewContent = ref('')
const previewPosition = ref({ x: 0, y: 0 })
const isImage = ref(false)
const previewImageSrc = ref('')
const previewName = ref('')
// 添加防抖变量
const previewTimer = ref(null)

// 简化 props，只保留 popoverWidth
const props = defineProps({
  popoverWidth: {
    type: Number,
    default: 510
  }
})

const emit = defineEmits(['select-emoji', 'operation-complete'])

// 选中下标
const optionsIndex = ref(0)
// 表情包选项卡
const emojiOptions = ref(['Emoji', 'Heo'])
// 获取选项卡div
const options = ref()

// 默认选中第一个选项卡
onMounted(() => {
  if (options.value) {
    options.value.children[optionsIndex.value].classList.add('active');
  }
})

function optionEmoji(index: number) {
  optionsIndex.value = index
  // 清除前面样式
  for (let i = 0; i < options.value.children.length; i++) {
    options.value.children[i].classList.remove('active');
  }
  // 给选中的div添加样式
  options.value.children[index].classList.add('active');
  
  // 立即触发操作完成事件，而不是等到下一个事件循环
  emit('operation-complete');
}

// 添加创建点击效果的函数
function createClickEffect(event) {
  // 获取点击的元素
  const target = event.currentTarget;
  
  // 添加点击效果类
  target.classList.add('emoji-clicked');
  
  // 移除类名以重置动画
  setTimeout(() => {
    target.classList.remove('emoji-clicked');
  }, 300); // 动画持续时间
}

// 修改添加表情函数，加入点击效果
function addEmoji(emoji: string, event) {
  // 创建点击反馈效果
  createClickEffect(event);
  
  // 原有逻辑不变
  emit('select-emoji', emoji);
  emit('operation-complete');
}

// 显示文字表情预览
function showTextPreview(event, emoji, key) {
  // 清除之前的定时器
  if (previewTimer.value) clearTimeout(previewTimer.value)
  
  previewContent.value = emoji
  previewName.value = key
  isImage.value = false
  updatePreviewPosition(event)
  showPreview.value = true
}

// 显示图片表情预览
function showImagePreview(event, src, key) {
  // 清除之前的定时器
  if (previewTimer.value) clearTimeout(previewTimer.value)
  
  previewImageSrc.value = src
  previewName.value = key
  isImage.value = true
  updatePreviewPosition(event)
  showPreview.value = true
}

// 隐藏预览 - 添加延迟
function hidePreview() {
  // 使用定时器延迟隐藏，避免频繁切换
  previewTimer.value = setTimeout(() => {
    showPreview.value = false
  }, 100) // 100ms延迟
}

// 更新预览位置 - 简单但有效的解决方案
function updatePreviewPosition(event) {
  // 获取触发元素的位置
  const rect = event.target.getBoundingClientRect();
  
  // 大幅增加垂直偏移，确保不遮挡原表情
  // 使用固定的大值，确保在所有情况下都不会遮挡
  previewPosition.value = {
    x: rect.left + rect.width / 2,  // 水平居中
    y: rect.top - 120               // 大幅增加距离
  };
}
</script>

<template>
  <el-popover
    placement="bottom-end"
    :width="popoverWidth"
    trigger="click"
    :popper-class="'emoji-popover-container'"
  >
    <template #reference>
      <slot name="trigger">
        <div class="emoji-trigger-btn">
          <svg-icon name="emojis" class="emoji-icon"/>
          <span class="emoji-ripple"></span>
        </div>
      </slot>
    </template>
    
    <div class="emojis_container" @mousedown.stop.prevent>
      <el-scrollbar>
        <div class="OvO_emojis" v-show="optionsIndex === 0">
          <!-- 文字表情 -->
          <div 
            v-for="(emoji,key) in emojis" 
            :key="key" 
            :title="key" 
            @click.stop="addEmoji(emoji, $event)"
            @mousedown.stop.prevent
            class="emoji-item"
            @mouseenter="showTextPreview($event, emoji, key)"
            @mouseleave="hidePreview"
          >
            {{ emoji }}
          </div>
        </div>
        <div class="OvO_heo" v-show="optionsIndex === 1">
          <div>
            <!-- 图片表情 -->
            <div 
              v-for="(src,key) in heo" 
              :key="key" 
              class="emoji-img-wrapper"
            >
              <img 
                :title="key" 
                :src="src" 
                @click="addEmoji(key, $event)"
                @mouseenter="showImagePreview($event, src, key)"
                @mouseleave="hidePreview"
              />
            </div>
          </div>
        </div>
      </el-scrollbar>
      <div class="OvO_options" ref="options" @mousedown.stop.prevent>
        <div v-for="(emojiOption,index) in emojiOptions" 
             class="item_emoji" 
             @click.stop="optionEmoji(index)"
             @mousedown.stop.prevent>
          {{ emojiOption }}
        </div>
      </div>
    </div>
  </el-popover>

  <!-- 全局预览元素 - 添加pointer-events: none -->
  <Teleport to="body">
    <div 
      v-show="showPreview" 
      class="global-emoji-preview"
      :style="{
        left: `${previewPosition.x}px`,
        top: `${previewPosition.y}px`
      }"
    >
      <div class="preview-content">
        <template v-if="!isImage">
          {{ previewContent }}
        </template>
        <template v-else>
          <img :src="previewImageSrc" />
        </template>
      </div>
      <div class="preview-name">{{ previewName }}</div>
    </div>
  </Teleport>
</template>

<style scoped lang="scss">
/* 整体容器优化 */
.emojis_container {
  background: white;
  border: 1px solid #ffdbec;
  border-radius: 12px;
  width: 100%;
  z-index: 999;
  box-shadow: 0 4px 16px rgba(253, 195, 216, 0.2);
  overflow: hidden;

  .el-scrollbar {
    height: 200px;
  }
}

/* 表情网格优化 */
.OvO_emojis {
  display: flex;
  flex-wrap: wrap;
  padding: 12px;
  background: #fffbfd;
}

.OvO_heo {
  padding: 12px;
  background: #fffbfd;

  div {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
  }
}

/* 文字表情项美化 */
.emoji-item {
  cursor: pointer;
  padding: 6px;
  font-size: 1.2em;
  position: relative;
  border-radius: 6px;
  transition: all 0.2s ease;
  
  &:hover {
    background-color: #fff0f7;
    transform: scale(1.1);
  }
}

/* 图片表情项美化 */
.emoji-img-wrapper {
  position: relative;
  border-radius: 6px;
  padding: 2px;
  transition: all 0.2s ease;
  
  img {
    width: 28px;
    height: 28px;
    cursor: pointer;
  }
  
  &:hover {
    background-color: #fff0f7;
    transform: scale(1.1);
  }
}

/* 二次元风格选项卡 */
.OvO_options {
  position: relative;
  display: flex;
  justify-content: center;
  width: 100%;
  background: #fff8fa;
  padding: 6px 0;
  
  .item_emoji {
    position: relative;
    margin: 0 8px;
    padding: 6px 16px;
    color: #adadad;
    font-size: 14px;
    border-radius: 20px;
    cursor: pointer;
    transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
    background: transparent;
    overflow: hidden;
    
    /* 选中状态 */
    &.active {
      color: white;
      background: linear-gradient(45deg, #ffb6c1, #ff85a2);
      box-shadow: 0 2px 8px rgba(255, 166, 183, 0.4);
      transform: translateY(-2px);
      font-weight: bold;
    }
    
    /* 悬浮效果 */
    &:not(.active):hover {
      color: #ff85a2;
      background: #fff0f5;
    }
  }
}

/* 预览框美化 */
.global-emoji-preview {
  position: fixed;
  transform: translateX(-50%);
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(4px);
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(250, 170, 190, 0.3);
  padding: 10px;
  z-index: 10000;
  display: flex;
  flex-direction: column;
  align-items: center;
  pointer-events: none;
  border: 1px solid #ffdbec;
  
  .preview-content {
    font-size: 36px;
    display: flex;
    justify-content: center;
    align-items: center;
    
    img {
      width: 48px;
      height: 48px;
    }
  }
  
  .preview-name {
    font-size: 12px;
    margin-top: 6px;
    color: #ff85a2;
    font-weight: 500;
  }
  
  /* 移除固定的三角形指示器 */
  &::after {
    content: none;
  }
  
  /* 顶部三角形（当预览框在下方时显示） */
  &.position-bottom::before {
    content: '';
    position: absolute;
    bottom: 100%;
    left: 50%;
    transform: translateX(-50%);
    border-width: 8px;
    border-style: solid;
    border-color: transparent transparent #ffdbec transparent;
  }
  
  /* 底部三角形（当预览框在上方时显示） */
  &.position-top::after {
    content: '';
    position: absolute;
    top: 100%;
    left: 50%;
    transform: translateX(-50%);
    border-width: 8px;
    border-style: solid;
    border-color: #ffdbec transparent transparent transparent;
  }
}

/* 表情触发按钮样式 */
.emoji-trigger-btn {
  display: inline-flex;
  justify-content: center;
  align-items: center;
  position: relative;
  width: 38px;
  height: 38px;
  border-radius: 50%;
  background: linear-gradient(135deg, #fff6f8, #fff);
  border: 2px solid #ffdbe8;
  cursor: pointer;
  overflow: hidden;
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
  box-shadow: 0 2px 6px rgba(255, 182, 193, 0.2);
  
  /* 图标样式 */
  .emoji-icon {
    width: 1.5em;
    height: 1.5em;
    color: #ff85a2;
    transition: transform 0.5s cubic-bezier(0.68, -0.55, 0.27, 1.55);
  }
  
  /* 悬浮效果 */
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 10px rgba(255, 182, 193, 0.3);
    
    .emoji-icon {
      transform: rotate(15deg) scale(1.1);
    }
  }
  
  /* 点击效果 */
  &:active {
    transform: translateY(0);
    box-shadow: 0 2px 4px rgba(255, 182, 193, 0.2);
  }
  
  /* 水波纹动画 */
  .emoji-ripple {
    position: absolute;
    width: 100%;
    height: 100%;
    background: radial-gradient(circle, rgba(255, 183, 197, 0.8) 0%, rgba(255, 183, 197, 0) 70%);
    border-radius: 50%;
    transform: scale(0);
    opacity: 1;
    pointer-events: none;
    animation: none;
  }
  
  &:active .emoji-ripple {
    animation: ripple 0.6s cubic-bezier(0.4, 0, 0.2, 1);
  }
}

/* 水波纹动画关键帧 */
@keyframes ripple {
  0% {
    transform: scale(0);
    opacity: 1;
  }
  100% {
    transform: scale(2);
    opacity: 0;
  }
}

/* 当弹出框打开时的样式 */
:deep(.emoji-popover-container) {
  animation: popup 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
  transform-origin: top center;
}

/* 弹出动画 */
@keyframes popup {
  0% {
    transform: scale(0.9);
    opacity: 0;
  }
  100% {
    transform: scale(1);
    opacity: 1;
  }
}

/* 添加到现有样式的底部 */
:deep(.emoji-popover-container *) {
  /* 防止内部元素获取焦点 */
  pointer-events: auto !important; /* 确保点击事件仍然有效 */
  user-select: none; /* 防止文本选择 */
  
  /* 这是关键 - 阻止任何元素获取焦点 */
  &:focus {
    outline: none !important;
  }
}

/* 使整个表情选择器不能获取键盘焦点 */
.emojis_container, .OvO_options, .OvO_emojis, .OvO_heo, .emoji-item, .emoji-img-wrapper {
  outline: none !important;
  -webkit-tap-highlight-color: transparent;
  
  /* 使所有内容无法获取焦点但仍可点击 */
  * {
    outline: none !important;
    -webkit-tap-highlight-color: transparent;
  }
}

/* 文字表情点击效果 */
.emoji-item.emoji-clicked {
  animation: emoji-pulse 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  background-color: #ffd1e6;
  color: #ff4081;
  transform: scale(1.2);
  z-index: 1;
}

/* 图片表情点击效果 */
.emoji-img-wrapper img.emoji-clicked {
  animation: emoji-pulse 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  filter: drop-shadow(0 0 4px rgba(255, 64, 129, 0.7));
  transform: scale(1.2);
  z-index: 1;
}

/* 点击动画关键帧 */
@keyframes emoji-pulse {
  0% {
    transform: scale(1);
    box-shadow: 0 0 0 0 rgba(255, 64, 129, 0.4);
  }
  50% {
    transform: scale(1.2);
    box-shadow: 0 0 0 10px rgba(255, 64, 129, 0);
  }
  100% {
    transform: scale(1.1);
    box-shadow: 0 0 0 0 rgba(255, 64, 129, 0);
  }
}
</style> 