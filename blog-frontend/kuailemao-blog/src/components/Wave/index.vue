<script setup lang="ts">
</script>

<template>
  <div class="header">
    <div>
      <!--
          xmlns:SVG命名看见URI
          viewBox:定义当前视口（viewbox）中绘制区域的位置大小
          preserveeAspectRatio：定义了绘制区域在视口中的对齐方式
          shape-rendering：定义了形状的渲染方式（如何呈现头像的锯齿效果）
       -->
      <svg class="waves" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink"
           viewBox="0 24 150 28" preserveAspectRatio="none" shape-rendering="auto">
        <!-- 定义一个defs元素，用于存储各种元素的定义，可以被其他元素引用。
            在defs元素中定义了一个名为“gentle-wave”的路径元素，用于描述波浪形状
            改路径秒苏联一系列二次贝塞尔曲线的控制点坐标，从而实现了波浪形状 -->
        <defs>
          <path id="gentle-wave"
                d="M-160 44c30 0 58-18 88-18s 58 18 88 18 58-18 88-18 58 18 88 18 v44h-352z"/>
        </defs>
        <!-- 定义一个g元素，用于讲多个图形组合在一期，并应用一些样式
            在g元素中使用use元素多次引用了赚钱定义的名为gentle-wave的路径元素
            通过设置不同的x，y坐标和填充颜色，实现了波浪形状和渐变效果 -->
        <g class="parallax">
          <use xlink:href="#gentle-wave" x="48" y="0" fill="var(--mao-bg-wave01)"/>
          <use xlink:href="#gentle-wave" x="48" y="3" fill="var(--mao-bg-wave02)"/>
          <use xlink:href="#gentle-wave" x="48" y="5" fill="var(--mao-bg-wave03)"/>
          <use xlink:href="#gentle-wave" x="48" y="7" fill="var(--mao-bg-wave04)"/>
        </g>

      </svg>
    </div>
  </div>
</template>

<style scoped lang="scss">
* {
  padding: 0;
  margin: 0;
}

* {
  padding: 0;
  margin: 0;
}

h1 {
  font-weight: 300;
  letter-spacing: 2px;
  font-size: 48px;
}

p {
  font-family: 'Lato', sans-serif;
  letter-spacing: 1px;
  font-size: 30px;
  color: #333333;
}

.header {
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100%;
  height: 0;
  min-height: 100px;
  max-height: 9.375rem;
  z-index: -1;
  // 波浪背景
  //background: linear-gradient(60deg, rgba(84, 58, 183, 1) 0%, rgba(0, 172, 193, 1) 100%);
}

.inner-header {
  height: 65vh;
  width: 100%;
  margin: 0;
  padding: 0;
}

.flex {
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: center;
}

.waves {
  position: relative;
  width: 100%;
  height: 15vh;
  margin-bottom: -7px;
  min-height: 100px;
  max-height: 150px;
}

.content {
  position: relative;
  height: 20vh;
  text-align: center;
  background-color: white;
}

.parallax > use {
  /* 使use元素执行move-forever动画 */
  animation: move-forever 25s cubic-bezier(.55, .5, .45, .5) infinite;
}

.parallax > use:nth-child(1) {
  /* 延迟2秒启动动画  */
  animation-delay: -2s;
  /* 设置动画持续时间为7秒 */
  animation-duration: 7s;
}

.parallax > use:nth-child(2) {
  animation-delay: -3s;
  animation-duration: 10s;
}

.parallax > use:nth-child(3) {
  animation-delay: -4s;
  animation-duration: 13s;
}

.parallax > use:nth-child(4) {
  animation-delay: -5s;
  animation-duration: 20s;
}

@keyframes move-forever {
  0% {
    transform: translate3d(-90px, 0, 0);
  }

  100% {
    transform: translate3d(85px, 0, 0);
  }
}

@media (max-width: 910px) {
  .waves {
    height: 70px;
    min-height: 70px;
    max-height: 150px;
  }

  .header {
    bottom: -40px;
  }

}
</style>