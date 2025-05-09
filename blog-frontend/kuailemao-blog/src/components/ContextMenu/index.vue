<template>
  <div
    v-if="isVisible"
    class="context-menu"
    :style="{
      top: `${y}px`,
      left: `${x}px`
    }"
  >
    <div class="menu-container" :style="isDarkMode ? 'background-color: #1e1b2d; color: #e2e0e7; border-color: #2d2644;' : 'background-color: #ffffff;'">
      <div class="menu-header" :style="isDarkMode ? 'background: linear-gradient(to right, #312a48, #2d2644); border-color: #372f52;' : 'background: linear-gradient(to right, #f9f5ff, #f5f3ff);'">
        <div class="menu-title-icon">
          <svg viewBox="0 0 24 24" width="24" height="24" :stroke="isDarkMode ? '#a78bfa' : '#8b5cf6'" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="12" cy="12" r="10"></circle>
            <line x1="12" y1="8" x2="12" y2="12"></line>
            <line x1="12" y1="16" x2="12.01" y2="16"></line>
          </svg>
        </div>
        <span :style="isDarkMode ? 'color: #a78bfa;' : 'color: #8b5cf6;'">{{ useWebsite.webInfo?.websiteName }}</span>
      </div>
      
      <ul class="menu-items">
        <!-- 首页 -->
        <li class="menu-item" @click="navigateTo('/')">
          <div class="menu-item-icon">
            <svg viewBox="0 0 24 24" width="24" height="24" :stroke="isDarkMode ? '#a78bfa' : '#8b5cf6'" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round">
              <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"></path>
              <polyline points="9 22 9 12 15 12 15 22"></polyline>
            </svg>
          </div>
          <span>首页</span>
        </li>
        
        <!-- 归档 - 与系统菜单对应 -->
        <li class="menu-item has-submenu">
          <div class="menu-item-content">
            <div class="menu-item-icon">
              <svg viewBox="0 0 24 24" width="24" height="24" :stroke="isDarkMode ? '#a78bfa' : '#8b5cf6'" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round">
                <path d="M22 19a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h5l2 3h9a2 2 0 0 1 2 2z"></path>
              </svg>
            </div>
            <span>归档</span>
            <div class="menu-item-arrow">
              <svg viewBox="0 0 24 24" width="24" height="24" :stroke="isDarkMode ? '#a78bfa' : '#8b5cf6'" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round">
                <polyline points="9 18 15 12 9 6"></polyline>
              </svg>
            </div>
          </div>
          <ul class="submenu" :style="isDarkMode ? 'background-color: #1e1b2d; border-color: #2d2644;' : 'background-color: #ffffff;'">
            <li class="submenu-item" @click.stop="navigateTo('/category')">
              <div class="submenu-item-icon">
                <svg viewBox="0 0 24 24" width="24" height="24" :stroke="isDarkMode ? '#a78bfa' : '#8b5cf6'" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round">
                  <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                  <polyline points="14 2 14 8 20 8"></polyline>
                  <line x1="16" y1="13" x2="8" y2="13"></line>
                  <line x1="16" y1="17" x2="8" y2="17"></line>
                  <polyline points="10 9 9 9 8 9"></polyline>
                </svg>
              </div>
              <span>分类</span>
            </li>
            <li class="submenu-item" @click.stop="navigateTo('/tags')">
              <div class="submenu-item-icon">
                <svg viewBox="0 0 24 24" width="24" height="24" :stroke="isDarkMode ? '#a78bfa' : '#8b5cf6'" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round">
                  <path d="M20.59 13.41l-7.17 7.17a2 2 0 0 1-2.83 0L2 12V2h10l8.59 8.59a2 2 0 0 1 0 2.82z"></path>
                  <line x1="7" y1="7" x2="7.01" y2="7"></line>
                </svg>
              </div>
              <span>标签</span>
            </li>
            <li class="submenu-item" @click.stop="navigateTo('/timeline')">
              <div class="submenu-item-icon">
                <svg viewBox="0 0 24 24" width="24" height="24" :stroke="isDarkMode ? '#a78bfa' : '#8b5cf6'" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round">
                  <circle cx="12" cy="12" r="10"></circle>
                  <polyline points="12 6 12 12 16 14"></polyline>
                </svg>
              </div>
              <span>时间轴</span>
            </li>
          </ul>
        </li>
        
        <!-- 其他 - 与系统菜单对应 -->
        <li class="menu-item has-submenu">
          <div class="menu-item-content">
            <div class="menu-item-icon">
              <svg viewBox="0 0 24 24" width="24" height="24" :stroke="isDarkMode ? '#a78bfa' : '#8b5cf6'" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round">
                <path d="M8 3v3a2 2 0 0 1-2 2H3m18 0h-3a2 2 0 0 1-2-2V3m0 18v-3a2 2 0 0 1 2-2h3M3 16h3a2 2 0 0 1 2 2v3"></path>
              </svg>
            </div>
            <span>其他</span>
            <div class="menu-item-arrow">
              <svg viewBox="0 0 24 24" width="24" height="24" :stroke="isDarkMode ? '#a78bfa' : '#8b5cf6'" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round">
                <polyline points="9 18 15 12 9 6"></polyline>
              </svg>
            </div>
          </div>
          <ul class="submenu" :style="isDarkMode ? 'background-color: #1e1b2d; border-color: #2d2644;' : 'background-color: #ffffff;'">
            <li class="submenu-item" @click.stop="navigateTo('/tree-hole')">
              <div class="submenu-item-icon">
                <svg viewBox="0 0 24 24" width="24" height="24" :stroke="isDarkMode ? '#a78bfa' : '#8b5cf6'" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round">
                  <path d="M18 8h1a4 4 0 0 1 0 8h-1"></path>
                  <path d="M2 8h16v9a4 4 0 0 1-4 4H6a4 4 0 0 1-4-4V8z"></path>
                  <line x1="6" y1="1" x2="6" y2="4"></line>
                  <line x1="10" y1="1" x2="10" y2="4"></line>
                  <line x1="14" y1="1" x2="14" y2="4"></line>
                </svg>
              </div>
              <span>树洞</span>
            </li>
            <li class="submenu-item" @click.stop="navigateTo('/message')">
              <div class="submenu-item-icon">
                <svg viewBox="0 0 24 24" width="24" height="24" :stroke="isDarkMode ? '#a78bfa' : '#8b5cf6'" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round">
                  <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"></path>
                </svg>
              </div>
              <span>留言板</span>
            </li>
            <li class="submenu-item" @click.stop="navigateTo('/about')">
              <div class="submenu-item-icon">
                <svg viewBox="0 0 24 24" width="24" height="24" :stroke="isDarkMode ? '#a78bfa' : '#8b5cf6'" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round">
                  <circle cx="12" cy="12" r="10"></circle>
                  <line x1="12" y1="16" x2="12" y2="12"></line>
                  <line x1="12" y1="8" x2="12.01" y2="8"></line>
                </svg>
              </div>
              <span>关于</span>
            </li>
          </ul>
        </li>
        
        <!-- 友链 -->
        <li class="menu-item" @click="navigateTo('/link')">
          <div class="menu-item-icon">
            <svg viewBox="0 0 24 24" width="24" height="24" :stroke="isDarkMode ? '#a78bfa' : '#8b5cf6'" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round">
              <path d="M10 13a5 5 0 0 0 7.54.54l3-3a5 5 0 0 0-7.07-7.07l-1.72 1.71"></path>
              <path d="M14 11a5 5 0 0 0-7.54-.54l-3 3a5 5 0 0 0 7.07 7.07l1.71-1.71"></path>
            </svg>
          </div>
          <span>友链</span>
        </li>
        
        <!-- 音乐 -->
        <li class="menu-item" @click="navigateTo('/music')">
          <div class="menu-item-icon">
            <svg viewBox="0 0 24 24" width="24" height="24" :stroke="isDarkMode ? '#a78bfa' : '#8b5cf6'" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round">
              <path d="M9 18V5l12-2v13"></path>
              <circle cx="6" cy="18" r="3"></circle>
              <circle cx="18" cy="16" r="3"></circle>
            </svg>
          </div>
          <span>音乐</span>
        </li>
        
        <!-- 相册 -->
        <li class="menu-item" @click="navigateTo('/photo')">
          <div class="menu-item-icon">
            <svg viewBox="0 0 24 24" width="24" height="24" :stroke="isDarkMode ? '#a78bfa' : '#8b5cf6'" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round">
              <rect x="3" y="3" width="18" height="18" rx="2" ry="2"></rect>
              <circle cx="8.5" cy="8.5" r="1.5"></circle>
              <polyline points="21 15 16 10 5 21"></polyline>
            </svg>
          </div>
          <span>相册</span>
        </li>
        
        <li class="menu-divider" :style="isDarkMode ? 'background: #372f52;' : 'background: #f0e6ff;'"></li>
        
        <!-- 刷新页面 - 特殊样式 -->
        <li class="menu-item refresh-item" @click="handleRefresh">
          <div class="menu-item-icon">
            <svg viewBox="0 0 24 24" width="24" height="24" stroke="white" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round">
              <path d="M23 4v6h-6"></path>
              <path d="M1 20v-6h6"></path>
              <path d="M3.51 9a9 9 0 0 1 14.85-3.36L23 10M1 14l4.64 4.36A9 9 0 0 0 20.49 15"></path>
            </svg>
          </div>
          <span>刷新页面</span>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, nextTick, computed } from 'vue';
import { useRouter } from 'vue-router';
import useWebsiteStore from "@/store/modules/website.ts";

const useWebsite = useWebsiteStore()

const router = useRouter();
const isVisible = ref(false);
const x = ref(0);
const y = ref(0);

// 检测暗色模式
const isDarkMode = computed(() => {
  return document.documentElement.classList.contains('dark');
});

// 显示菜单
const showMenu = (event: MouseEvent) => {
  event.preventDefault();
  
  // 计算位置，确保菜单不会超出窗口
  const menuWidth = 240; // 菜单宽度
  const submenuWidth = 180; // 子菜单宽度
  const menuHeight = 370; // 预估菜单高度（减小高度，确保更好定位）
  
  // 初始位置为鼠标位置
  let posX = event.clientX;
  let posY = event.clientY;
  
  // 如果菜单会超出右边界，向左显示
  // 同时考虑子菜单需要的空间
  if (posX + menuWidth + submenuWidth > window.innerWidth) {
    posX = Math.max(10, window.innerWidth - menuWidth - 10);
  }
  
  // 如果菜单会超出下边界，则向上显示
  if (posY + menuHeight > window.innerHeight) {
    posY = Math.max(10, window.innerHeight - menuHeight - 10);
  }
  
  x.value = posX;
  y.value = posY;
  isVisible.value = true;
  
  // 关闭所有可能打开的子菜单
  setTimeout(() => {
    document.querySelectorAll('.has-submenu').forEach(item => {
      item.classList.remove('active');
    });
  }, 50);
  
  // 确保子菜单位置正确显示
  nextTick(() => {
    adjustSubmenuPositions();
  });
};

// 调整子菜单位置，防止溢出屏幕
const adjustSubmenuPositions = () => {
  document.querySelectorAll('.submenu').forEach(submenu => {
    const submenuEl = submenu as HTMLElement;
    const parentItem = submenuEl.closest('.has-submenu') as HTMLElement;
    
    // 重置之前可能的样式调整
    submenuEl.style.left = '100%';
    submenuEl.style.right = 'auto';
    submenuEl.style.top = '0';
    
    // 添加延迟以确保样式重置生效
    setTimeout(() => {
      const rect = submenuEl.getBoundingClientRect();
      const parentRect = parentItem.getBoundingClientRect();
      
      // 如果子菜单超出右边界，向左显示
      if (rect.right > window.innerWidth) {
        submenuEl.style.left = 'auto';
        submenuEl.style.right = '100%';
      }
      
      // 如果子菜单超出下边界，调整位置
      if (rect.bottom > window.innerHeight) {
        // 先尝试上对齐
        let newTop = 0;
        
        // 计算顶部可用空间
        const availableTopSpace = parentRect.top;
        const availableBottomSpace = window.innerHeight - parentRect.bottom;
        
        if (rect.height > availableBottomSpace && availableTopSpace > availableBottomSpace) {
          // 如果底部空间不足且顶部空间更大，则让子菜单显示在父菜单顶部对齐
          newTop = -rect.height + parentRect.height;
        } else {
          // 否则根据溢出量计算向上位移
          const overflowHeight = Math.min(rect.bottom - window.innerHeight, rect.height - parentRect.height);
          newTop = -overflowHeight;
        }
        
        submenuEl.style.top = `${newTop}px`;
      }
    }, 0);
  });
};

// 隐藏菜单
const hideMenu = () => {
  isVisible.value = false;
};

// 导航到指定路由
const navigateTo = (path: string) => {
  router.push(path);
  hideMenu();
};

// 刷新页面
const handleRefresh = () => {
  window.location.reload();
  hideMenu();
};

// 点击其他地方时隐藏菜单
const handleClick = (event: MouseEvent) => {
  const contextMenu = document.querySelector('.context-menu');
  if (isVisible.value && contextMenu && !contextMenu.contains(event.target as Node)) {
    hideMenu();
  }
};

// 手动激活子菜单（解决触摸设备的问题）
const handleSubmenuActivation = (event: Event) => {
  const target = event.currentTarget as HTMLElement;
  const parentLi = target.closest('.has-submenu') as HTMLElement;
  const hasSubmenuElements = document.querySelectorAll('.has-submenu');
  
  // 先清除所有激活状态
  hasSubmenuElements.forEach(el => {
    if (el !== parentLi) {
      el.classList.remove('active');
    }
  });
  
  // 切换当前元素的激活状态
  parentLi.classList.toggle('active');
  
  // 阻止事件冒泡，避免触发hideMenu
  event.stopPropagation();
};

onMounted(() => {
  document.addEventListener('contextmenu', showMenu);
  document.addEventListener('click', handleClick);
  window.addEventListener('resize', adjustSubmenuPositions);
});

onUnmounted(() => {
  document.removeEventListener('contextmenu', showMenu);
  document.removeEventListener('click', handleClick);
  window.removeEventListener('resize', adjustSubmenuPositions);
});
</script>

<style scoped>
/* 完全重写样式，确保没有透明效果 */
.context-menu {
  position: fixed;
  z-index: 100000;
  user-select: none;
  animation: fadeIn 0.2s ease-out;
  filter: drop-shadow(0 4px 20px rgba(0, 0, 0, 0.15));
}

@keyframes fadeIn {
  from { opacity: 0; transform: scale(0.98); }
  to { opacity: 1; transform: scale(1); }
}

.menu-container {
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(139, 92, 246, 0.15);
  border: 1px solid #f3f0ff;
  min-width: 240px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Arial, sans-serif;
  color: #4a4158;
  position: relative;
  transition: all 0.2s ease;
  animation: slideIn 0.2s ease;
  overflow: visible !important;
}

html[class='dark'] .menu-container {
  background-color: #1e1b2d;
  border: 1px solid #2d2644;
  color: #e2e0e7;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
}

html[class='dark'] .submenu {
  background-color: #1e1b2d;
  border: 1px solid #2d2644;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
}

@keyframes slideIn {
  from { opacity: 0; transform: translateY(5px); }
  to { opacity: 1; transform: translateY(0); }
}

.menu-header {
  display: flex;
  align-items: center;
  padding: 14px 16px;
  border-bottom: 1px solid #f0e6ff;
  background: linear-gradient(to right, #f9f5ff, #f5f3ff);
  border-top-left-radius: 12px;
  border-top-right-radius: 12px;
  position: relative;
  margin-bottom: 1px;
}

html[class='dark'] .menu-header {
  background: linear-gradient(to right, #312a48, #2d2644);
  border-bottom: 1px solid #372f52;
}

.menu-title-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 10px;
  width: 20px;
  height: 20px;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% { transform: scale(1); }
  50% { transform: scale(1.05); }
  100% { transform: scale(1); }
}

.menu-items {
  list-style: none;
  margin: 0;
  padding: 10px;
}

.menu-divider {
  height: 1px;
  margin: 10px 0;
  background: #f0e6ff;
}

html[class='dark'] .menu-divider {
  background: #372f52;
}

.menu-item {
  position: relative;
  font-size: 0.9rem;
  margin: 4px 0;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.2s ease;
  overflow: visible !important;
  display: block;
}

.menu-item:not(.has-submenu) {
  display: flex;
  align-items: center;
  padding: 10px 14px;
}

.menu-item-content {
  display: flex;
  align-items: center;
  padding: 10px 14px;
  width: 100%;
  border-radius: 10px;
}

.has-submenu {
  position: relative;
  overflow: visible !important;
}

.menu-item:hover, .menu-item-content:hover {
  background-color: #f5f3ff;
  transform: translateX(2px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

html[class='dark'] .menu-item:hover, 
html[class='dark'] .menu-item-content:hover {
  background-color: #352e54;
}

.menu-item-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  width: 18px;
  height: 18px;
  transition: transform 0.3s;
}

.menu-item:hover .menu-item-icon {
  transform: scale(1.15) rotate(8deg);
}

.menu-item-icon svg {
  width: 18px;
  height: 18px;
  stroke: #8b5cf6;
  fill: none;
  transition: all 0.2s;
  stroke-width: 2;
}

html[class='dark'] .menu-item-icon svg {
  stroke: #a78bfa;
}

.menu-item:hover .menu-item-icon svg {
  stroke: #d946ef;
}

html[class='dark'] .menu-item:hover .menu-item-icon svg {
  stroke: #e879f9;
}

.menu-item span {
  transition: all 0.2s ease;
  font-weight: 500;
  letter-spacing: 0.2px;
}

.menu-item-arrow {
  margin-left: auto;
  width: 16px;
  height: 16px;
  opacity: 0.7;
  transition: transform 0.2s ease;
}

.has-submenu:hover .menu-item-arrow {
  transform: rotate(90deg);
}

.submenu {
  position: absolute;
  top: 0;
  left: 100%;
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(139, 92, 246, 0.12);
  border: 1px solid #f3f0ff;
  min-width: 180px;
  visibility: hidden;
  opacity: 0;
  transform: translateX(-5px);
  transition: all 0.2s ease;
  z-index: 1000;
  padding: 8px;
}

.has-submenu:hover > .submenu {
  visibility: visible;
  opacity: 1;
  transform: translateX(0);
}

.submenu-item {
  display: flex;
  align-items: center;
  padding: 10px 14px;
  cursor: pointer;
  font-size: 0.85rem;
  border-radius: 8px;
  margin: 3px 0;
  transition: all 0.2s ease;
}

.submenu-item:hover {
  background-color: #f5f3ff;
  transform: translateX(2px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

html[class='dark'] .submenu-item:hover {
  background-color: #352e54;
}

.submenu-item-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 10px;
  width: 16px;
  height: 16px;
}

.submenu-item-icon svg {
  width: 16px;
  height: 16px;
  stroke: #8b5cf6;
  fill: none;
  stroke-width: 2;
}

html[class='dark'] .submenu-item-icon svg {
  stroke: #a78bfa;
}

.submenu-item:hover .submenu-item-icon svg {
  stroke: #d946ef;
}

html[class='dark'] .submenu-item:hover .submenu-item-icon svg {
  stroke: #e879f9;
}

/* 刷新按钮特殊样式 */
.refresh-item {
  margin: 6px 8px;
  background: linear-gradient(135deg, #8b5cf6, #ec4899);
  box-shadow: 0 3px 12px rgba(139, 92, 246, 0.2);
  color: white;
  transition: all 0.2s ease;
  border: none;
  border-radius: 8px;
}

.refresh-item:hover {
  background: linear-gradient(135deg, #a78bfa, #f472b6);
  box-shadow: 0 5px 15px rgba(139, 92, 246, 0.4), 0 2px 5px rgba(236, 72, 153, 0.3);
  transform: translateY(-2px) translateX(2px);
}

.refresh-item .menu-item-icon svg {
  stroke: white;
}

.refresh-item span {
  color: white;
  font-weight: 600;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
}
</style> 