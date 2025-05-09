<template>
  <div ref="particlesContainer" class="particles-container"></div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';

const particlesContainer = ref<HTMLElement | null>(null);
let animationFrameId: number | null = null;
let particles: Particle[] = [];

interface Particle {
  x: number;
  y: number;
  size: number;
  speedX: number;
  speedY: number;
  color: string;
}

const colors = ['#f79533', '#f37055', '#ef4e7b', '#a166ab', '#5073b8', '#1098ad', '#07b39b', '#6fba82'];
let mouseX = 0;
let mouseY = 0;

const createParticles = (count: number) => {
  if (!particlesContainer.value) return;
  
  const canvas = document.createElement('canvas');
  const ctx = canvas.getContext('2d');
  if (!ctx) return;
  
  const rect = particlesContainer.value.getBoundingClientRect();
  canvas.width = rect.width;
  canvas.height = rect.height;
  particlesContainer.value.appendChild(canvas);
  
  // 创建粒子
  for (let i = 0; i < count; i++) {
    particles.push({
      x: Math.random() * canvas.width,
      y: Math.random() * canvas.height,
      size: Math.random() * 3 + 1,
      speedX: Math.random() * 0.5 - 0.25,
      speedY: Math.random() * 0.5 - 0.25,
      color: colors[Math.floor(Math.random() * colors.length)]
    });
  }
  
  // 处理鼠标移动
  window.addEventListener('mousemove', (e) => {
    mouseX = e.clientX;
    mouseY = e.clientY;
  });
  
  // 动画函数
  const animate = () => {
    if (!ctx) return;
    
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    
    particles.forEach((particle, index) => {
      // 移动粒子
      particle.x += particle.speedX;
      particle.y += particle.speedY;
      
      // 边缘反弹
      if (particle.x < 0 || particle.x > canvas.width) {
        particle.speedX *= -1;
      }
      
      if (particle.y < 0 || particle.y > canvas.height) {
        particle.speedY *= -1;
      }
      
      // 鼠标交互 - 粒子远离鼠标光标
      const dx = particle.x - (mouseX - rect.left);
      const dy = particle.y - (mouseY - rect.top);
      const distance = Math.sqrt(dx * dx + dy * dy);
      
      if (distance < 100) {
        const angle = Math.atan2(dy, dx);
        const force = (100 - distance) / 1500;
        particle.speedX += Math.cos(angle) * force;
        particle.speedY += Math.sin(angle) * force;
      }
      
      // 应用一些阻力
      particle.speedX *= 0.99;
      particle.speedY *= 0.99;
      
      // 绘制粒子
      ctx.beginPath();
      ctx.arc(particle.x, particle.y, particle.size, 0, Math.PI * 2);
      ctx.fillStyle = particle.color;
      ctx.fill();
      
      // 连接彼此接近的粒子
      for (let j = index + 1; j < particles.length; j++) {
        const otherParticle = particles[j];
        const dx = particle.x - otherParticle.x;
        const dy = particle.y - otherParticle.y;
        const distance = Math.sqrt(dx * dx + dy * dy);
        
        if (distance < 100) {
          ctx.beginPath();
          ctx.strokeStyle = particle.color;
          ctx.globalAlpha = 1 - distance / 100;
          ctx.lineWidth = 0.5;
          ctx.moveTo(particle.x, particle.y);
          ctx.lineTo(otherParticle.x, otherParticle.y);
          ctx.stroke();
          ctx.globalAlpha = 1;
        }
      }
    });
    
    animationFrameId = requestAnimationFrame(animate);
  };
  
  animate();
};

onMounted(() => {
  createParticles(50);
});

onUnmounted(() => {
  if (animationFrameId !== null) {
    cancelAnimationFrame(animationFrameId);
  }
});
</script>

<style scoped>
.particles-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100vh;
  z-index: -8;
  pointer-events: none;
}
</style> 