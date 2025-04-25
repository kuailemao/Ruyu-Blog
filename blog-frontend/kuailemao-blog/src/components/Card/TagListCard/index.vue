<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { tagList } from '@/apis/tag';
import { useRouter } from 'vue-router';

// 标签类型接口
interface Tag {
  id: number;
  tagName: string;
  articleCount: number;
}

const router = useRouter();
const tags = ref<Tag[]>([]);
const loading = ref(true);

// 加载标签数据
function loadContent() {
  loading.value = true;
  tagList().then((res: any) => {
    if (res.code === 200 && res.data) {
      // 按文章数量排序并取前10个
      tags.value = res.data
        .sort((a: Tag, b: Tag) => b.articleCount - a.articleCount)
        .slice(0, 10);
    }
    loading.value = false;
  }).catch(() => {
    loading.value = false;
  });
}

// 跳转到标签详情页
function goToTag(tagId: number) {
  router.push(`/tags/${tagId}`);
}

onMounted(() => {
  loadContent();
});
</script>

<template>
  <!-- 标签列表 -->
  <Card title="热门标签" prefix-icon="tag" @isRotate="true"
        :isScale="true" v-view-request="{ callback: loadContent }">
    <div class="tag-list-container">
      <!-- 小胶带装饰 -->
      <div class="tape tape-top-right"></div>
      <div class="tape tape-bottom-left"></div>
      
      <!-- 装饰贴纸 -->
      <div class="sticker sticker-1"></div>
      <div class="sticker sticker-2"></div>
      
      <!-- 加载中 -->
      <div v-if="loading" class="loading-container">
        <div class="kawaii-cloud">
          <div class="cloud-body"></div>
          <div class="cloud-face">
            <div class="eye left"></div>
            <div class="eye right"></div>
            <div class="mouth"></div>
          </div>
        </div>
        <div class="loading-text">加载中...</div>
      </div>
      
      <!-- 标签内容 -->
      <div v-else class="tags-wrapper">
        <!-- 装饰元素：彩色小点 -->
        <div class="decoration-dot dot1"></div>
        <div class="decoration-dot dot2"></div>
        <div class="decoration-dot dot3"></div>
        <div class="decoration-dot dot4"></div>
        
        <div v-for="tag in tags" :key="tag.id" 
             class="tag-item" 
             @click="goToTag(tag.id)">
          <div class="tag-content">
            <span class="tag-symbol">#</span>
            <span class="tag-name">{{ tag.tagName }}</span>
            <div class="tag-count">{{ tag.articleCount }}</div>
          </div>
          <div class="tag-deco"></div>
        </div>
        
        <!-- 空状态 -->
        <div v-if="tags.length === 0" class="empty-state">
          <div class="kawaii-cloud sad">
            <div class="cloud-body"></div>
            <div class="cloud-face">
              <div class="eye left"></div>
              <div class="eye right"></div>
              <div class="mouth sad"></div>
            </div>
          </div>
          <div>暂无标签~</div>
        </div>
      </div>
    </div>
  </Card>
</template>

<style scoped lang="scss">
.tag-list-container {
  min-height: 160px;
  position: relative;
  padding: 5px;
  background-image: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23f8bbd0' fill-opacity='0.1'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
  border-radius: 8px;
  overflow: hidden;
  
  // 贴纸装饰
  .sticker {
    position: absolute;
    z-index: 2;
    width: 40px;
    height: 40px;
    background-size: contain;
    background-repeat: no-repeat;
    opacity: 0.7;
    pointer-events: none; // 确保贴纸不会干扰鼠标事件
    
    &.sticker-1 {
      top: 8px;
      left: 8px;
      width: 30px;
      height: 30px;
      background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'%3E%3Cpath fill='%23FFB7C5' d='M50,5c24.85,0,45,20.15,45,45S74.85,95,50,95S5,74.85,5,50S25.15,5,50,5z'/%3E%3Cpath fill='%23FFF' d='M33,35c-5.52,0-10,4.48-10,10c0,5.52,4.48,10,10,10c5.52,0,10-4.48,10-10C43,39.48,38.52,35,33,35z M67,35 c-5.52,0-10,4.48-10,10c0,5.52,4.48,10,10,10c5.52,0,10-4.48,10-10C77,39.48,72.52,35,67,35z'/%3E%3Cpath fill='%23FF8FAB' d='M50,70c-8.84,0-16,1.79-16,4s7.16,4,16,4s16-1.79,16-4S58.84,70,50,70z'/%3E%3C/svg%3E");
      transform: rotate(15deg);
      animation: floatSticker 5s ease-in-out infinite;
    }
    
    &.sticker-2 {
      bottom: 12px;
      right: 10px;
      width: 35px;
      height: 35px;
      background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'%3E%3Cpath fill='%23AEC6CF' d='M50,5c24.85,0,45,20.15,45,45S74.85,95,50,95S5,74.85,5,50S25.15,5,50,5z'/%3E%3Cpath fill='%23FFF' d='M30,40c0,2.76,2.24,5,5,5s5-2.24,5-5s-2.24-5-5-5S30,37.24,30,40z M60,35c-2.76,0-5,2.24-5,5s2.24,5,5,5 s5-2.24,5-5S62.76,35,60,35z'/%3E%3Cpath fill='%23FFD1DC' d='M35,70c8.28,0,15-6.72,15-15H20C20,63.28,26.72,70,35,70z'/%3E%3C/svg%3E");
      transform: rotate(-8deg);
      animation: floatSticker 5.5s ease-in-out 0.5s infinite;
    }
  }
  
  // 胶带装饰
  .tape {
    position: absolute;
    height: 30px;
    width: 80px;
    background-color: rgba(255, 182, 193, 0.3);
    opacity: 0.7;
    z-index: 2;
    
    &.tape-top-right {
      top: -10px;
      right: 40px;
      transform: rotate(45deg);
    }
    
    &.tape-bottom-left {
      bottom: -10px;
      left: 40px;
      width: 60px;
      background-color: rgba(173, 216, 230, 0.3);
      transform: rotate(-35deg);
    }
  }
  
  // 加载动画
  .loading-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 200px;
    
    .loading-text {
      margin-top: 10px;
      font-size: 14px;
      color: var(--el-text-color-secondary);
      letter-spacing: 2px;
    }
  }
  
  // 云朵样式
  .kawaii-cloud {
    position: relative;
    width: 80px;
    height: 50px;
    
    .cloud-body {
      position: absolute;
      width: 100%;
      height: 100%;
      background-color: #f2f6fc;
      border-radius: 25px;
      box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
      animation: float 3s ease-in-out infinite;
      z-index: 1;
      
      &:before, &:after {
        content: "";
        position: absolute;
        background-color: #f2f6fc;
        border-radius: 50%;
      }
      
      &:before {
        width: 35px;
        height: 35px;
        top: -15px;
        left: 15px;
      }
      
      &:after {
        width: 30px;
        height: 30px;
        top: -10px;
        right: 15px;
      }
    }
    
    .cloud-face {
      position: absolute;
      z-index: 2;
      top: 12px;
      width: 100%;
      display: flex;
      flex-direction: column;
      align-items: center;
      
      .eye {
        width: 8px;
        height: 8px;
        background-color: #5d6d7e;
        border-radius: 50%;
        position: absolute;
        top: 5px;
        animation: blink 3s ease-in-out infinite;
        
        &.left {
          left: 25px;
        }
        
        &.right {
          right: 25px;
        }
        
        &:after {
          content: "";
          position: absolute;
          width: 3px;
          height: 3px;
          background-color: white;
          border-radius: 50%;
          top: 1px;
          left: 1px;
        }
      }
      
      .mouth {
        position: absolute;
        top: 20px;
        width: 20px;
        height: 8px;
        border-radius: 0 0 10px 10px;
        background-color: #5d6d7e;
        
        &.sad {
          border-radius: 10px 10px 0 0;
          top: 22px;
        }
      }
    }
    
    &.sad .cloud-body {
      background-color: #e0e6ed;
      
      &:before, &:after {
        background-color: #e0e6ed;
      }
    }
  }
  
  // 装饰性彩色小点
  .decoration-dot {
    position: absolute;
    width: 14px;
    height: 14px;
    border-radius: 50%;
    z-index: 1;
    
    &.dot1 {
      top: 15px;
      left: 15px;
      background-color: rgba(255, 192, 203, 0.3);
      animation: float 4s ease-in-out infinite;
    }
    
    &.dot2 {
      top: 30px;
      right: 25px;
      width: 10px;
      height: 10px;
      background-color: rgba(173, 216, 230, 0.3);
      animation: float 3.5s ease-in-out 0.2s infinite;
    }
    
    &.dot3 {
      bottom: 20px;
      left: 25%;
      width: 12px;
      height: 12px;
      background-color: rgba(216, 191, 216, 0.3);
      animation: float 4.5s ease-in-out 0.4s infinite;
    }
    
    &.dot4 {
      bottom: 40px;
      right: 15%;
      width: 8px;
      height: 8px;
      background-color: rgba(255, 222, 173, 0.3);
      animation: float 3s ease-in-out 0.6s infinite;
    }
  }
  
  // 标签列表容器
  .tags-wrapper {
    display: flex;
    flex-wrap: wrap;
    padding: 18px 12px;
    position: relative;
    min-height: 160px;
    justify-content: center;
    align-content: center;
    gap: 12px 10px; // 增加间距避免悬浮影响相邻标签
    
    // 随机化标签的整体排列
    &::before {
      content: "";
      position: absolute;
      width: 100%;
      height: 100%;
      background-image: 
        radial-gradient(circle at 25% 25%, rgba(255, 182, 193, 0.1) 0%, transparent 50%),
        radial-gradient(circle at 75% 75%, rgba(173, 216, 230, 0.1) 0%, transparent 50%);
      z-index: 0;
    }
    
    // 装饰性星星
    &::before, &::after {
      content: "";
      position: absolute;
      width: 12px;
      height: 12px;
      background: transparent;
      clip-path: polygon(50% 0%, 61% 35%, 98% 35%, 68% 57%, 79% 91%, 50% 70%, 21% 91%, 32% 57%, 2% 35%, 39% 35%);
      animation: twinkle 4s linear infinite;
      z-index: 0;
    }
    
    &::before {
      top: 15px;
      left: 15px;
      background-color: rgba(255, 204, 153, 0.7);
      animation-delay: 0s;
    }
    
    &::after {
      bottom: 10px;
      right: 20px;
      background-color: rgba(188, 143, 143, 0.7);
      animation-delay: 2s;
    }
  }
  
  // 标签项
  .tag-item {
    position: relative;
    border-radius: 16px;
    padding: 5px 10px;
    margin: 4px;
    background: linear-gradient(135deg, rgba(255,255,255,0.6) 0%, rgba(255,255,255,0.2) 100%);
    backdrop-filter: blur(5px);
    border: 1px solid rgba(255, 255, 255, 0.25);
    box-shadow: 0 3px 6px rgba(0, 0, 0, 0.04);
    transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
    overflow: hidden;
    z-index: 1;
    
    // 交错排列的偏移和旋转 - 完全取消重叠
    &:nth-child(4n+1) {
      transform: translateY(3px) rotate(-0.7deg);
      z-index: 2;
    }
    
    &:nth-child(4n+2) {
      transform: translateY(-2px) rotate(0.5deg);
      z-index: 1;
    }
    
    &:nth-child(4n+3) {
      transform: translateY(2px) rotate(0.8deg);
      z-index: 2;
    }
    
    &:nth-child(4n) {
      transform: translateY(-3px) rotate(-0.5deg);
      z-index: 1;
    }
    
    // 不同位置的标签设置不同样式 - 调整配色
    &:nth-child(3n) {
      background: linear-gradient(135deg, rgba(255,255,255,0.6) 0%, rgba(255,255,255,0.2) 100%);
      border-color: rgba(255, 192, 203, 0.35);
    }
    
    &:nth-child(3n+1) {
      background: linear-gradient(135deg, rgba(255,255,255,0.6) 0%, rgba(230,230,250,0.25) 100%);
      border-color: rgba(176, 196, 222, 0.35); 
    }
    
    &:nth-child(5n) {
      background: linear-gradient(135deg, rgba(255,255,255,0.6) 0%, rgba(255,250,240,0.25) 100%);
      border-color: rgba(255, 218, 185, 0.35);
    }
    
    // 为不同位置的标签设置不同的大小和颜色
    &:nth-child(4n+1) {
      font-size: 0.85rem;
      padding: 5px 10px;
    }
    
    &:nth-child(4n+2) {
      font-size: 0.8rem;
      padding: 5px 9px;
    }
    
    &:nth-child(4n+3) {
      font-size: 0.82rem;
      padding: 5px 10px;
    }
    
    &:nth-child(4n) {
      font-size: 0.83rem;
      padding: 5px 9px;
    }
    
    // 为每个标签添加不同的阴影方向
    &:nth-child(odd) {
      box-shadow: 1px 2px 5px rgba(0, 0, 0, 0.03);
    }
    
    &:nth-child(even) {
      box-shadow: -1px 2px 5px rgba(0, 0, 0, 0.03);
    }
    
    // 为每个标签添加不同的动画延迟和动画
    @for $i from 1 through 10 {
      &:nth-child(#{$i}) {
        animation: fadeInRandom 0.5s ease-out;
        animation-delay: #{$i * 0.03}s;
        animation-fill-mode: both;
      }
    }
    
    &:hover {
      transform: translateY(-2px) scale(1.02) rotate(0deg) !important;
      box-shadow: 0 4px 10px rgba(0, 0, 0, 0.07), 0 0 6px rgba(255, 182, 193, 0.2);
      background: linear-gradient(135deg, rgba(255,182,193,0.25) 0%, rgba(255,192,203,0.12) 100%);
      border-color: rgba(255, 182, 193, 0.5);
      z-index: 10 !important;
      
      .tag-deco {
        transform: scale(1.05) rotate(3deg);
        opacity: 0.7;
      }
      
      .tag-content {
        color: var(--el-color-primary);
        
        .tag-symbol {
          animation: bounce 0.6s ease infinite;
        }
        
        .tag-count {
          background-color: rgba(var(--el-color-primary-rgb), 0.15);
          transform: scale(1.05);
          box-shadow: 0 0 6px rgba(var(--el-color-primary-rgb), 0.12);
        }
      }
      
      // 悬停时添加小樱花效果
      &::after {
        opacity: 0.8;
        transform: translate(-50%, -50%) scale(0.8);
      }
      
      // 添加霓虹光效
      &::before {
        opacity: 0.4;
      }
    }
    
    // 霓虹光效
    &::before {
      content: "";
      position: absolute;
      inset: -1px;
      background: linear-gradient(90deg, #ff9a9e, #fad0c4, #ffecd2, #fcb69f, #ff9a9e);
      background-size: 300% 300%;
      z-index: -1;
      border-radius: 17px;
      animation: neonGlow 4s ease infinite;
      opacity: 0;
      transition: opacity 0.4s ease;
    }
    
    // 樱花装饰
    &::after {
      content: "";
      position: absolute;
      top: 50%;
      left: 50%;
      width: 22px;
      height: 22px;
      background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'%3E%3Cpath fill='rgba(255,182,193,0.6)' d='M30,50 C30,40 40,30 50,30 C60,30 70,40 70,50 C70,60 60,70 50,70 C40,70 30,60 30,50 Z M50,30 C50,20 55,10 50,0 C45,10 40,20 50,30 Z M70,50 C80,50 90,45 100,50 C90,55 80,60 70,50 Z M50,70 C50,80 45,90 50,100 C55,90 60,80 50,70 Z M30,50 C20,50 10,55 0,50 C10,45 20,40 30,50 Z'%3E%3C/path%3E%3C/svg%3E");
      background-size: contain;
      background-repeat: no-repeat;
      transform: translate(-50%, -50%) scale(0.5);
      opacity: 0;
      transition: all 0.3s ease;
      z-index: -1;
    }
    
    .tag-content {
      position: relative;
      z-index: 2;
      display: flex;
      align-items: center;
      font-size: 0.83rem;
      color: var(--el-text-color-regular);
      transition: color 0.3s ease;
      
      .tag-symbol {
        margin-right: 2px;
        color: var(--el-color-primary-light-3);
        font-weight: bold;
        transition: transform 0.2s ease;
      }
      
      .tag-name {
        margin-right: 4px;
        white-space: nowrap;
        text-shadow: 0 1px 1px rgba(255, 255, 255, 0.5);
        letter-spacing: 0.2px;
      }
      
      .tag-count {
        display: flex;
        align-items: center;
        justify-content: center;
        background-color: rgba(var(--el-color-primary-rgb), 0.08);
        border-radius: 9px;
        padding: 0px 5px;
        font-size: 0.65rem;
        color: var(--el-color-primary);
        font-weight: 500;
        min-width: 16px;
        box-shadow: 0 1px 2px rgba(0, 0, 0, 0.03) inset;
        transition: all 0.3s ease;
      }
    }
    
    .tag-deco {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      width: 100%;
      height: 100%;
      background: radial-gradient(circle at center, rgba(var(--el-color-primary-rgb), 0.08) 0%, transparent 70%);
      opacity: 0.25;
      transition: all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
      z-index: 1;
      border-radius: 50%;
    }
  }
  
  // 空状态
  .empty-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    color: var(--el-text-color-secondary);
    font-size: 14px;
    justify-content: center;
    width: 100%;
    min-height: 150px;
    
    .kawaii-cloud.sad {
      margin-bottom: 15px;
    }
  }
}

// 动画
@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

@keyframes floatSticker {
  0%, 100% {
    transform: translateY(0) rotate(var(--rotate, 0deg));
  }
  50% {
    transform: translateY(-5px) rotate(var(--rotate, 0deg));
  }
}

@keyframes blink {
  0%, 100% {
    transform: scaleY(1);
  }
  10%, 90% {
    transform: scaleY(1);
  }
  20% {
    transform: scaleY(0.1);
  }
}

@keyframes fadeInRandom {
  from {
    opacity: 0;
    transform: scale(0.8) translateY(15px);
  }
  to {
    opacity: 1;
    transform: scale(1) translateY(0);
  }
}

@keyframes bounce {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-3px);
  }
}

@keyframes twinkle {
  0%, 100% {
    opacity: 0.7;
    transform: scale(1);
  }
  50% {
    opacity: 0.3;
    transform: scale(0.8);
  }
}

@keyframes neonGlow {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}
</style>