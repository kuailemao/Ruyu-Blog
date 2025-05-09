<template>
  <div class="mouse-trail-container">
    <canvas ref="trailCanvas" class="trail-canvas"></canvas>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';

const trailCanvas = ref<HTMLCanvasElement | null>(null);
let animationFrameId: number | null = null;
let ctx: CanvasRenderingContext2D | null = null;
let points: { x: number; y: number; size: number; color: string; age: number }[] = [];
let mouseX = 0;
let mouseY = 0;
let mouseActive = false;

const colors = ['#f79533', '#f37055', '#ef4e7b', '#a166ab', '#5073b8', '#1098ad', '#07b39b', '#6fba82'];

const initCanvas = () => {
  if (!trailCanvas.value) return;
  
  // 将画布设置为窗口大小
  trailCanvas.value.width = window.innerWidth;
  trailCanvas.value.height = window.innerHeight;
  
  ctx = trailCanvas.value.getContext('2d');
  if (!ctx) return;
  
  // 事件监听器
  window.addEventListener('mousemove', handleMouseMove);
  window.addEventListener('mouseenter', () => { mouseActive = true; });
  window.addEventListener('mouseleave', () => { mouseActive = false; });
  window.addEventListener('resize', handleResize);
  
  // 开始动画
  animate();
};

const handleMouseMove = (e: MouseEvent) => {
  mouseX = e.clientX;
  mouseY = e.clientY;
  
  // 添加新点
  if (mouseActive) {
    addPoint(mouseX, mouseY);
  }
};

const addPoint = (x: number, y: number) => {
  const color = colors[Math.floor(Math.random() * colors.length)];
  points.push({
    x,
    y,
    size: Math.random() * 15 + 5,
    color,
    age: 0
  });
};

const handleResize = () => {
  if (!trailCanvas.value) return;
  
  trailCanvas.value.width = window.innerWidth;
  trailCanvas.value.height = window.innerHeight;
};

const animate = () => {
  if (!ctx || !trailCanvas.value) return;
  
  // 使用完全透明的背景清除画布
  ctx.clearRect(0, 0, trailCanvas.value.width, trailCanvas.value.height);
  
  // 更新并绘制点
  for (let i = 0; i < points.length; i++) {
    const point = points[i];
    
    // 增加年龄
    point.age += 1;
    
    // 移除旧点
    if (point.age > 50) {
      points.splice(i, 1);
      i--;
      continue;
    }
    
    // 根据年龄计算大小
    const size = point.size * (1 - point.age / 50);
    
    // 绘制点
    ctx.beginPath();
    ctx.arc(point.x, point.y, size, 0, Math.PI * 2);
    ctx.fillStyle = point.color;
    ctx.globalAlpha = 1 - point.age / 50;
    ctx.fill();
    ctx.globalAlpha = 1;
  }
  
  animationFrameId = requestAnimationFrame(animate);
};

onMounted(() => {
  initCanvas();
});

onUnmounted(() => {
  window.removeEventListener('mousemove', handleMouseMove);
  window.removeEventListener('mouseenter', () => { mouseActive = true; });
  window.removeEventListener('mouseleave', () => { mouseActive = false; });
  window.removeEventListener('resize', handleResize);
  
  if (animationFrameId !== null) {
    cancelAnimationFrame(animationFrameId);
  }
});
</script>

<style scoped>
.mouse-trail-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 9999;
  pointer-events: none;
}

.trail-canvas {
  position: absolute;
  top: 0;
  left: 0;
}
</style> 