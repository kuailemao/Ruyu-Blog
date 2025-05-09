<template>
  <div 
    ref="parallaxContainer" 
    class="parallax-element"
    :style="{ 
      transform: `translateY(${offset}px)`,
      transition: 'transform 0.3s ease-out'
    }"
  >
    <slot></slot>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';

const props = defineProps({
  speed: {
    type: Number,
    default: 0.2,
    validator: (value: number) => value >= -1 && value <= 1
  },
  direction: {
    type: String,
    default: 'up',
    validator: (value: string) => ['up', 'down'].includes(value)
  }
});

const parallaxContainer = ref<HTMLElement | null>(null);
const offset = ref(0);
let scrollY = 0;
let windowHeight = 0;
let elementTop = 0;
let elementHeight = 0;

const calculateOffset = () => {
  if (!parallaxContainer.value) return;
  
  // 获取当前元素位置
  const rect = parallaxContainer.value.getBoundingClientRect();
  elementTop = rect.top + window.scrollY;
  elementHeight = rect.height;
  
  // 计算元素何时在视图中
  const elementInView = scrollY + windowHeight > elementTop && scrollY < elementTop + elementHeight;
  
  if (elementInView) {
    // 根据元素在视口中的距离计算偏移量
    const viewportOffset = (scrollY + windowHeight - elementTop) / (windowHeight + elementHeight);
    // 归一化到范围0-1
    const normalized = Math.max(0, Math.min(1, viewportOffset));
    
    // 应用速度和方向
    const direction = props.direction === 'up' ? -1 : 1;
    const maxOffset = elementHeight * props.speed * direction;
    offset.value = maxOffset * normalized;
  }
};

const handleScroll = () => {
  scrollY = window.scrollY;
  requestAnimationFrame(calculateOffset);
};

const handleResize = () => {
  windowHeight = window.innerHeight;
  calculateOffset();
};

onMounted(() => {
  windowHeight = window.innerHeight;
  
  if (parallaxContainer.value) {
    const rect = parallaxContainer.value.getBoundingClientRect();
    elementTop = rect.top + window.scrollY;
    elementHeight = rect.height;
  }
  
  window.addEventListener('scroll', handleScroll, { passive: true });
  window.addEventListener('resize', handleResize);
  
  // 初始计算
  handleScroll();
});

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll);
  window.removeEventListener('resize', handleResize);
});
</script>

<style scoped>
.parallax-element {
  will-change: transform;
}
</style> 