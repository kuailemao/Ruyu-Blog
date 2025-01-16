<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'

interface Props {
  show: boolean
  photos: string[]
  currentIndex: number
}

const props = defineProps<Props>()

const emit = defineEmits<{
  (e: 'update:show', value: boolean): void
  (e: 'update:currentIndex', value: number): void
}>()

// 变换相关的状态
const scale = ref(1)
const minScale = 0.1
const maxScale = 10
const scaleStep = 0.1
const scaleMultiplier = 0.002
const rotation = ref(0)
const flipX = ref(false)
const flipY = ref(false)
const translateX = ref(0)
const translateY = ref(0)
const isDragging = ref(false)
const startX = ref(0)
const startY = ref(0)
const dragStartX = ref(0)
const dragStartY = ref(0)
const startDistance = ref(0)
const startScale = ref(1)
const isTransitioning = ref(false)
const transitionDirection = ref<'prev' | 'next' | ''>('')

// 判断是否为初始状态
const isInitialState = computed(() => {
  return scale.value === 1 && 
         rotation.value === 0 && 
         !flipX.value && 
         !flipY.value && 
         translateX.value === 0 && 
         translateY.value === 0
})

// 重置所有变换
const resetTransform = () => {
  scale.value = 1
  rotation.value = 0
  flipX.value = false
  flipY.value = false
  translateX.value = 0
  translateY.value = 0
}

// 关闭预览
const closePreview = () => {
  resetTransform()
  
  // 获取之前保存的滚动位置
  const scrollY = parseInt(document.body.style.top || '0') * -1
  
  // 恢复页面滚动状态
  document.body.style.position = ''
  document.body.style.width = ''
  document.body.style.top = ''
  document.body.style.left = ''
  document.body.style.marginTop = ''
  document.body.style.overflow = ''
  
  // 恢复到之前的滚动位置
  window.scrollTo(0, scrollY)
  
  emit('update:show', false)
}

// 切换图片
const prevPhoto = () => {
  if (isTransitioning.value) return
  isTransitioning.value = true
  transitionDirection.value = 'prev'
  
  resetTransform()
  
  const newIndex = props.currentIndex > 0 
    ? props.currentIndex - 1 
    : props.photos.length - 1
  emit('update:currentIndex', newIndex)

  setTimeout(() => {
    transitionDirection.value = ''
    isTransitioning.value = false
  }, 300)
}

const nextPhoto = () => {
  if (isTransitioning.value) return
  isTransitioning.value = true
  transitionDirection.value = 'next'
  
  resetTransform()
  
  const newIndex = props.currentIndex < props.photos.length - 1 
    ? props.currentIndex + 1 
    : 0
  emit('update:currentIndex', newIndex)

  setTimeout(() => {
    transitionDirection.value = ''
    isTransitioning.value = false
  }, 300)
}

// 图片变换方法
const zoomIn = () => {
  scale.value = Math.min(scale.value * 1.2, maxScale)
}

const zoomOut = () => {
  scale.value = Math.max(scale.value / 1.2, minScale)
}

const rotateLeft = () => {
  rotation.value -= 90
}

const rotateRight = () => {
  rotation.value += 90
}

const toggleFlipX = () => {
  flipX.value = !flipX.value
}

const toggleFlipY = () => {
  flipY.value = !flipY.value
}

// 处理键盘事件
const handleKeydown = (e: KeyboardEvent) => {
  if (!props.show) return
  
  if (e.key === 'Escape') {
    closePreview()
  } else if (e.key === 'ArrowLeft') {
    prevPhoto()
  } else if (e.key === 'ArrowRight') {
    nextPhoto()
  } else if (e.key === '+' || e.key === '=') {
    zoomIn()
  } else if (e.key === '-') {
    zoomOut()
  } else if (e.key === 'r') {
    rotateRight()
  } else if (e.key === 'R') {
    rotateLeft()
  } else if (e.key === '0') {
    resetTransform()
  }
}

// 处理鼠标事件
const handleMouseDown = (e: MouseEvent) => {
  isDragging.value = true
  startX.value = e.clientX
  startY.value = e.clientY
  dragStartX.value = translateX.value
  dragStartY.value = translateY.value
}

const handleMouseMove = (e: MouseEvent) => {
  if (!isDragging.value) return
  
  const deltaX = e.clientX - startX.value
  const deltaY = e.clientY - startY.value
  
  if (isInitialState.value && scale.value === 1) {
    if (Math.abs(deltaX) > 50) {
      if (deltaX > 0) {
        prevPhoto()
      } else {
        nextPhoto()
      }
      isDragging.value = false
    }
  } else {
    translateX.value = dragStartX.value + deltaX
    translateY.value = dragStartY.value + deltaY
  }
}

const handleMouseUp = () => {
  isDragging.value = false
}

// 处理触摸事件
const handleTouchStart = (e: TouchEvent) => {
  if (e.touches.length === 1) {
    isDragging.value = true
    startX.value = e.touches[0].clientX
    startY.value = e.touches[0].clientY
    dragStartX.value = translateX.value
    dragStartY.value = translateY.value
  } else if (e.touches.length === 2) {
    startDistance.value = Math.hypot(
      e.touches[0].clientX - e.touches[1].clientX,
      e.touches[0].clientY - e.touches[1].clientY
    )
    startScale.value = scale.value
  }
}

const handleTouchMove = (e: TouchEvent) => {
  if (e.touches.length === 1 && isDragging.value) {
    const deltaX = e.touches[0].clientX - startX.value
    const deltaY = e.touches[0].clientY - startY.value
    
    if (isInitialState.value && scale.value === 1) {
      if (Math.abs(deltaX) > 50) {
        if (deltaX > 0) {
          prevPhoto()
        } else {
          nextPhoto()
        }
        isDragging.value = false
      }
    } else {
      translateX.value = dragStartX.value + deltaX
      translateY.value = dragStartY.value + deltaY
    }
  } else if (e.touches.length === 2) {
    const currentDistance = Math.hypot(
      e.touches[0].clientX - e.touches[1].clientX,
      e.touches[0].clientY - e.touches[1].clientY
    )
    const scaleFactor = currentDistance / startDistance.value
    scale.value = Math.min(Math.max(startScale.value * scaleFactor, minScale), maxScale)
  }
}

const handleTouchEnd = () => {
  isDragging.value = false
}

// 处理滚轮缩放
const handleWheel = (e: WheelEvent) => {
  e.preventDefault()
  const delta = -e.deltaY * scaleMultiplier
  const newScale = scale.value * (1 + delta)
  scale.value = Math.min(Math.max(newScale, minScale), maxScale)
}

// 添加生命周期钩子
onMounted(() => {
  window.addEventListener('mousemove', handleMouseMove)
  window.addEventListener('mouseup', handleMouseUp)
  window.addEventListener('keydown', handleKeydown)
})

onUnmounted(() => {
  window.removeEventListener('mousemove', handleMouseMove)
  window.removeEventListener('mouseup', handleMouseUp)
  window.removeEventListener('keydown', handleKeydown)
})
</script>

<template>
  <div v-if="show" class="preview-modal" @click="closePreview">
    <button class="close-btn" @click="closePreview">×</button>
    <div class="preview-content" @click.stop>
      <div class="preview-image-container">
        <div class="drag-container"
             :style="{
               transform: `translate(${translateX}px, ${translateY}px)`
             }">
          <img
            :key="currentIndex"
            :src="photos[currentIndex]"
            :style="{
              transform: `scale(${scale}) rotate(${rotation}deg) 
                         scaleX(${flipX ? -1 : 1}) scaleY(${flipY ? -1 : 1})`,
              transformOrigin: 'center',
              transition: 'transform 0.3s ease'
            }"
            :class="[
              'preview-image',
              transitionDirection === 'prev' ? 'slide-prev' : '',
              transitionDirection === 'next' ? 'slide-next' : ''
            ]"
            @mousedown="handleMouseDown"
            @wheel.prevent="handleWheel"
            @touchstart.prevent="handleTouchStart"
            @touchmove.prevent="handleTouchMove"
            @touchend.prevent="handleTouchEnd"
            @touchcancel.prevent="handleTouchEnd"
            @dragstart.prevent
            draggable="false"
            alt="预览图片"
          >
        </div>
      </div>
      <div class="preview-toolbar">
        <div class="toolbar-group">
          <button class="preview-btn" @click="prevPhoto" title="上一张">◀</button>
          <button class="preview-btn" @click="nextPhoto" title="下一张">▶</button>
        </div>
        <div class="toolbar-group">
          <button class="preview-btn" @click="zoomIn" title="放大">+</button>
          <button class="preview-btn" @click="zoomOut" title="缩小">-</button>
          <button class="preview-btn" @click="rotateLeft" title="向左旋转">↺</button>
          <button class="preview-btn" @click="rotateRight" title="向右旋转">↻</button>
          <button class="preview-btn" @click="toggleFlipX" title="水平翻转">↔</button>
          <button class="preview-btn" @click="toggleFlipY" title="垂直翻转">↕</button>
          <button class="preview-btn" @click="resetTransform" title="重置">⟲</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.preview-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.9);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  overscroll-behavior: none;
  touch-action: none;
  -webkit-overflow-scrolling: touch;
}

.preview-content {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  touch-action: none;
}

.preview-image-container {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.preview-image {
  max-width: 100vw;
  max-height: 100vh;
  object-fit: contain;
  user-select: none;
  -webkit-user-drag: none;
  transform-origin: center;
  will-change: transform;
  backface-visibility: hidden;
  -webkit-backface-visibility: hidden;
  touch-action: none;
}

.preview-toolbar {
  position: fixed;
  bottom: 30px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 16px;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(12px);
  padding: 12px 24px;
  border-radius: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.2);
  z-index: 1001;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.preview-toolbar:hover {
  background: rgba(255, 255, 255, 0.15);
  transform: translateX(-50%) translateY(-2px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.3);
}

.toolbar-group {
  display: flex;
  gap: 12px;
  position: relative;
}

.toolbar-group:first-child::after {
  content: '';
  position: absolute;
  right: -8px;
  top: 50%;
  transform: translateY(-50%);
  width: 1px;
  height: 24px;
  background: rgba(255, 255, 255, 0.3);
}

.preview-btn {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: white;
  font-size: 18px;
  cursor: pointer;
  padding: 8px;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 12px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
  -webkit-tap-highlight-color: transparent;
  outline: none;
  user-select: none;
  touch-action: manipulation;
}

.preview-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(45deg, #ff6b6b, #ff8e8e);
  opacity: 0;
  transition: opacity 0.3s ease;
  z-index: -1;
}

.preview-btn:hover {
  transform: translateY(-2px);
  border-color: rgba(255, 255, 255, 0.4);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.preview-btn:hover::before {
  opacity: 0.8;
}

.preview-btn[title]::after {
  content: attr(title);
  position: absolute;
  bottom: 100%;
  left: 50%;
  transform: translateX(-50%) translateY(-8px);
  padding: 6px 10px;
  background: rgba(0, 0, 0, 0.8);
  color: white;
  font-size: 12px;
  border-radius: 8px;
  white-space: nowrap;
  opacity: 0;
  visibility: hidden;
  transition: all 0.3s ease;
  backdrop-filter: blur(4px);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.preview-btn:hover[title]::after {
  opacity: 1;
  visibility: visible;
  transform: translateX(-50%) translateY(-4px);
}

.close-btn {
  position: fixed;
  top: 20px;
  right: 20px;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: white;
  font-size: 28px;
  cursor: pointer;
  width: 44px;
  height: 44px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  backdrop-filter: blur(8px);
  z-index: 1002;
  -webkit-tap-highlight-color: transparent;
  outline: none;
  user-select: none;
  touch-action: manipulation;
}

.close-btn:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: scale(1.1);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.close-btn:active {
  transform: scale(0.95);
  opacity: 0.8;
}

/* 切换动画 */
.preview-image.slide-prev {
  animation: slideFromLeft 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.preview-image.slide-next {
  animation: slideFromRight 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

@keyframes slideFromLeft {
  from {
    opacity: 0;
    transform: translateX(-10%) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateX(0) scale(1);
  }
}

@keyframes slideFromRight {
  from {
    opacity: 0;
    transform: translateX(10%) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateX(0) scale(1);
  }
}

/* 移动端适配 */
@media (max-width: 768px) {
  .preview-toolbar {
    bottom: 20px;
    padding: 12px;
    gap: 12px;
    flex-direction: column;
    width: calc(100% - 32px);
    max-width: 320px;
  }

  .toolbar-group {
    gap: 8px;
    flex-wrap: wrap;
    justify-content: center;
  }

  .toolbar-group:first-child::after {
    display: none;
  }

  .toolbar-group:first-child {
    padding-bottom: 8px;
    border-bottom: 1px solid rgba(255, 255, 255, 0.2);
  }

  .preview-btn {
    width: 36px;
    height: 36px;
    font-size: 16px;
  }

  .preview-btn[title]::after {
    bottom: 120%;
  }

  .close-btn {
    top: 10px;
    right: 10px;
    width: 40px;
    height: 40px;
    font-size: 24px;
  }
}

/* 深色模式适配 */
@media (prefers-color-scheme: dark) {
  .preview-toolbar {
    background: rgba(30, 30, 30, 0.8);
  }

  .preview-btn {
    background: rgba(40, 40, 40, 0.8);
  }
}

.drag-container {
  position: relative;
  display: inline-block;
  will-change: transform;
}
</style> 