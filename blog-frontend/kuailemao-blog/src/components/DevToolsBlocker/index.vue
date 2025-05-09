<template>
  <transition name="slide-down">
    <div v-if="isWarningVisible && isEnabled" class="dev-tools-blocker">
      <div class="warning-container">
        <div class="warning-content">
          <div class="warning-icon">
            <svg viewBox="0 0 24 24" width="24" height="24" stroke="#8b5cf6" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round">
              <circle cx="12" cy="12" r="10"></circle>
              <line x1="12" y1="8" x2="12" y2="12"></line>
              <line x1="12" y1="16" x2="12.01" y2="16"></line>
            </svg>
          </div>
          <div class="warning-text">
            <h3>禁止查看源代码</h3>
            <p>泥想干嘛(┬┬﹏┬┬)</p>
          </div>
        </div>
        <button class="close-button" @click="hideWarning">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
            <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12 19 6.41z"/>
          </svg>
        </button>
      </div>
    </div>
  </transition>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed, watch } from 'vue';

const props = defineProps({
  // 是否启用开发者工具禁用功能
  enableDevToolsBlocker: {
    type: Boolean,
    default: true
  },
  // 警告消失时间（毫秒）
  autoDismissTime: {
    type: Number,
    default: 2000 // 默认2秒后自动消失
  }
});

const isWarningVisible = ref(false);
let devToolsCheckInterval: number | undefined;
let dismissTimeout: number | undefined;
let debuggerCounter = 0;
const MAX_DEBUGGER_CALLS = 3;
const DEBUGGER_RESET_TIME = 5000;
let debuggerResetTimeout: number | undefined;
let continuousDebuggerEnabled = false;
// 将环境变量字符串正确转换为布尔值
const PROD_MODE = import.meta.env.VITE_ENABLE_DEV_TOOLSBLOCKER === 'true';

// 连续执行debugger语句的递归函数
function debuggerLoop() {
  if (continuousDebuggerEnabled && isEnabled.value) {
    try {
      // 添加debugger语句
      debugger;
      
      // 在随机时间后再次执行，避免可预测性
      const randomDelay = Math.floor(Math.random() * 500) + 200;
      setTimeout(debuggerLoop, randomDelay);
    } catch (e) {
      // 如果出错，继续尝试
      setTimeout(debuggerLoop, 500);
    }
  }
}

const isEnabled = computed(() => {
  // 在开发模式下，仅在明确请求时启用
  // 在生产模式下，除非明确禁用，否则始终启用
  // 如果 VITE_ENABLE_DEV_TOOLSBLOCKER 明确设置为 false，则不启用
  return PROD_MODE ? props.enableDevToolsBlocker : false;
});

// 监听警告状态，设置自动消失
watch(isWarningVisible, (newVal) => {
  if (newVal) {
    if (dismissTimeout) clearTimeout(dismissTimeout);
    dismissTimeout = window.setTimeout(() => {
      hideWarning();
    }, props.autoDismissTime);
  }
});

// 添加多重debugger语句，通过连续调用形成拦截
const runDebuggerSequence = () => {
  if (!isEnabled.value) return;
  
  // 避免过于频繁地触发debugger
  debuggerCounter++;
  if (debuggerResetTimeout) clearTimeout(debuggerResetTimeout);
  
  // 在一段时间后重置计数器
  debuggerResetTimeout = window.setTimeout(() => {
    debuggerCounter = 0;
  }, DEBUGGER_RESET_TIME);
  
  // 只有在计数器较低时才执行，防止无限循环
  if (debuggerCounter < MAX_DEBUGGER_CALLS) {
    setTimeout(() => { debugger; }, 100);
    setTimeout(() => { debugger; }, 500);
    setTimeout(() => { debugger; }, 1000);
  }
  
  // 启用连续debugger循环
  if (!continuousDebuggerEnabled) {
    continuousDebuggerEnabled = true;
    debuggerLoop();
  }
};

// 使用Function构造函数创建一个难以通过开发工具跳过的函数
const createObfuscatedDebugger = () => {
  if (!isEnabled.value) return;
  
  try {
    // 使用Function构造函数创建动态函数，更难被跳过
    const dynamicFn = new Function(
      `
      debugger;
      for(let i=0; i<3; i++) {
        (function() {
          debugger;
        })();
      }
      return true;
      `
    );
    
    // 执行该函数
    dynamicFn();
  } catch (e) {
    // 忽略错误
  }
};

const showWarning = () => {
  if (isEnabled.value) {
    isWarningVisible.value = true;
    runDebuggerSequence();
    createObfuscatedDebugger();
  }
};

const hideWarning = () => {
  isWarningVisible.value = false;
  if (dismissTimeout) {
    clearTimeout(dismissTimeout);
    dismissTimeout = undefined;
  }
};

const handleKeyDown = (e: KeyboardEvent) => {
  if (!isEnabled.value) return;

  // F12
  if (e.key === 'F12') {
    e.preventDefault();
    showWarning();
    return;
  }
  // Ctrl+Shift+I, Ctrl+Shift+J, Ctrl+Shift+C, Ctrl+U
  if (e.ctrlKey && e.shiftKey && ('I' === e.key.toUpperCase() || 'J' === e.key.toUpperCase() || 'C' === e.key.toUpperCase())) {
    e.preventDefault();
    showWarning();
    return;
  }
  if (e.ctrlKey && e.key.toUpperCase() === 'U') {
    e.preventDefault();
    showWarning();
    return;
  }
};

// 检查是否已经打开了开发者工具
const checkDevToolsPreOpened = () => {
  if (!isEnabled.value) return false;
  
  // 方法1: 检查窗口尺寸差异
  const widthThreshold = 160;
  const heightThreshold = 160;
  
  if (window.outerWidth - window.innerWidth > widthThreshold || 
      window.outerHeight - window.innerHeight > heightThreshold) {
    showWarning();
    return true;
  }
  
  // 移除主动显示警告的测试方法，改为仅返回检测结果
  try {
    const consoleTest = /./;
    consoleTest.toString = function() {
      return "Console open detected";
    };
    
    // 仅进行检测，不触发警告
    const hasFirebug = /firebug/i.test(navigator.userAgent);
    const hasChrome = typeof (window as any).chrome !== 'undefined' && 
                      typeof (navigator as any).vendor !== 'undefined' && 
                      /google/i.test((navigator as any).vendor);
    const hasDevTools = typeof (console as any).firebug !== 'undefined' && 
                       (/DevTools/i.test((console as any).firebug) || /Dev Tools/i.test((console as any).firebug));
    
    const isOpen = hasFirebug || (hasChrome && hasDevTools);
    
    if (isOpen) {
      showWarning();
      return true;
    }
  } catch (e) {
    // 忽略错误
  }
  
  // 方法3: 测量调试函数的执行时间，但提高阈值减少误报
  try {
    let executionTime = 0;
    const start = performance.now();
    debugger;
    executionTime = performance.now() - start;
    
    // 增加阈值以减少误报
    if (executionTime > 200) {
      showWarning();
      return true;
    }
  } catch (e) {
    // 忽略错误
  }
  
  return false;
};

const performDevToolsCheck = () => {
  if (!isEnabled.value) return;

  const threshold = 160; // 开发者工具打开时内外高度/宽度的典型差异
  if (window.outerWidth - window.innerWidth > threshold || 
      window.outerHeight - window.innerHeight > threshold) {
    if (!isWarningVisible.value) {
      showWarning();
    } else {
      // 如果开发者工具一直开着，定期重新触发debugger
      runDebuggerSequence();
      createObfuscatedDebugger();
    }
  }
};

// 在页面内容加载时执行的一次性检查
const injectAntiDevTools = () => {
  if (!isEnabled.value) return;
  
  // 注入内联脚本处理预先打开的开发工具 
  try {
    // 注入样式使得DevTools预览时内容模糊
    const style = document.createElement('style');
    style.textContent = `
      @media screen and (-webkit-min-device-pixel-ratio:0) {
        @supports (-webkit-appearance:none) and (display:flex) {
          html:has(> div[id^="inspector"]) * {
            filter: blur(5px);
            pointer-events: none !important;
          }
        }
      }
    `;
    document.head.appendChild(style);
    
    // 启动连续性的debugger检测 - 仅在确认检测到DevTools时启用
    continuousDebuggerEnabled = false;
    
    // 立即执行检测，但不显示警告除非真的检测到开发者工具
    const devToolsDetected = checkDevToolsPreOpened();
    
    // 只有在实际检测到开发者工具时才启用更积极的防御
    if (devToolsDetected) {
      continuousDebuggerEnabled = true;
      debuggerLoop();
    }
  } catch (e) {
    // 忽略错误
    console.error("Failed to inject anti-dev tools:", e);
  }
};

onMounted(() => {
  if (isEnabled.value) {
    // 基本事件监听
    document.addEventListener('keydown', handleKeyDown);
    window.addEventListener('resize', performDevToolsCheck);
    
    // 延迟执行第一次检查，避免页面加载时的误报
    setTimeout(() => {
      performDevToolsCheck();
    }, 2000);
    
    // 设置定期检查，但降低频率
    if (devToolsCheckInterval) clearInterval(devToolsCheckInterval);
    devToolsCheckInterval = setInterval(() => {
      performDevToolsCheck();
      // 降低随机触发的概率，减少误报
      if (Math.random() > 0.9) {
        createObfuscatedDebugger();
      }
    }, 2000); // 增加检查间隔
    
    // 注入反调试代码
    injectAntiDevTools();
    
    // 在DOMContentLoaded后进行一次全面检查
    if (document.readyState === 'loading') {
      document.addEventListener('DOMContentLoaded', injectAntiDevTools);
    } else {
      // 延迟执行，避免页面加载时的误报
      setTimeout(injectAntiDevTools, 1000);
    }
  }
});

onUnmounted(() => {
  if (isEnabled.value) {
    document.removeEventListener('keydown', handleKeyDown);
    window.removeEventListener('resize', performDevToolsCheck);
    document.removeEventListener('DOMContentLoaded', injectAntiDevTools);
    
    if (devToolsCheckInterval) {
      clearInterval(devToolsCheckInterval);
    }
    if (dismissTimeout) {
      clearTimeout(dismissTimeout);
    }
    if (debuggerResetTimeout) {
      clearTimeout(debuggerResetTimeout);
    }
    
    // 停止连续debugger循环
    continuousDebuggerEnabled = false;
  }
});
</script>

<style scoped>
/* 从顶部滑入动画 */
.slide-down-enter-active,
.slide-down-leave-active {
  transition: all 0.4s cubic-bezier(0.5, 0, 0.3, 1.5);
}

.slide-down-enter-from,
.slide-down-leave-to {
  transform: translateY(-100%);
  opacity: 0;
}

/* 警告容器样式 - 与右键菜单风格一致 */
.dev-tools-blocker {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 99999;
  pointer-events: none;
}

.warning-container {
  background: #ffffff;
  color: #4a4158;
  padding: 16px 24px;
  margin: 0 auto;
  width: 100%;
  max-width: 500px;
  border-radius: 0 0 12px 12px;
  box-shadow: 0 4px 20px rgba(139, 92, 246, 0.15);
  border: 1px solid #f3f0ff;
  border-top: none;
  display: flex;
  justify-content: space-between;
  align-items: center;
  pointer-events: auto;
  position: relative;
  overflow: hidden;
  animation: glow 3s infinite alternate;
}

@keyframes glow {
  0% {
    box-shadow: 0 4px 20px rgba(139, 92, 246, 0.15);
  }
  100% {
    box-shadow: 0 4px 25px rgba(139, 92, 246, 0.3);
  }
}

/* 警告内容 */
.warning-content {
  display: flex;
  align-items: center;
  gap: 16px;
  position: relative;
  z-index: 1;
}

.warning-icon {
  flex-shrink: 0;
  width: 32px;
  height: 32px;
  position: relative;
}

.warning-icon svg {
  width: 100%;
  height: 100%;
  animation: pulse 2s infinite ease-in-out;
  filter: drop-shadow(0 0 4px rgba(139, 92, 246, 0.4));
}

@keyframes pulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.1); }
}

.warning-text {
  flex-grow: 1;
}

.warning-text h3 {
  margin: 0 0 4px 0;
  font-size: 1.1rem;
  font-weight: 600;
  letter-spacing: 0.5px;
  color: #8b5cf6;
}

.warning-text p {
  margin: 0;
  font-size: 0.9rem;
  opacity: 0.9;
  font-weight: 400;
}

/* 关闭按钮 */
.close-button {
  background: transparent;
  border: none;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  position: relative;
  z-index: 1;
  transition: all 0.3s ease;
  padding: 0;
}

.close-button:hover {
  background: rgba(139, 92, 246, 0.1);
  transform: scale(1.1);
}

.close-button svg {
  width: 20px;
  height: 20px;
  fill: #8b5cf6;
}

/* 暗色主题适配 */
html[class='dark'] .warning-container {
  background: #1e1b2d;
  color: #e2e0e7;
  border-color: #2d2644;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
}

html[class='dark'] .warning-text h3 {
  color: #a78bfa;
}

html[class='dark'] .warning-icon svg {
  stroke: #a78bfa;
}

html[class='dark'] .close-button svg {
  fill: #a78bfa;
}

html[class='dark'] .close-button:hover {
  background: rgba(167, 139, 250, 0.2);
}

/* 响应式设计 */
@media (max-width: 600px) {
  .warning-container {
    max-width: 100%;
    border-radius: 0;
    padding: 12px 16px;
  }
  
  .warning-icon {
    width: 28px;
    height: 28px;
  }
  
  .warning-text h3 {
    font-size: 1rem;
  }
  
  .warning-text p {
    font-size: 0.8rem;
  }
}
</style> 