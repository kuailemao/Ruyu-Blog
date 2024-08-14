<template>
  <div class="imgs">
    <ul>
      <li class="item" v-for="(image, index) in imageList" :key="index" :style="{
        'background-image': 'url(' + image + ')'
      }">
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">

const imageList = [
  "https://image.kuailemao.xyz/blog/article/articleCover/8a9f081a-e6e0-4104-84e7-c3db5c16bca0.png",
  "https://image.kuailemao.xyz/blog/article/articleCover/dc20756c-57ed-48d2-98b1-f3e66100fd80.png",
  "https://image.kuailemao.xyz/blog/article/articleCover/75407c94-c41e-4adc-b952-b10603256844.png",
  "https://image.kuailemao.xyz/blog/article/articleCover/a7775a52-1377-4d49-9547-5573f5854bc5.png",
  "https://image.kuailemao.xyz/blog/article/articleCover/96b0e708-bcd8-4731-bebd-b8f07b88e020.png",
]
</script>

<style lang="scss" scoped>

.imgs {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100vh;
  z-index: -9;
  background-color: #363636;
  overflow: hidden;

  .item {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: no-repeat 50% 50% / cover;
    opacity: 0;
    // 一张图片 6s
    animation: imageAnimation 30s linear infinite 0s;
    backface-visibility: hidden;
    transform-style: preserve-3d;

    &:nth-child(2) {
      animation-delay: 6s;
    }

    &:nth-child(3) {
      animation-delay: 12s;
    }

    &:nth-child(4) {
      animation-delay: 18s;
    }

    &:nth-child(5) {
      animation-delay: 24s;
    }

    &:nth-child(6) {
      animation-delay: 30s;
    }
  }

  &::before {
    content: '';
    display: block;
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, .2);
    transition: all .2s ease-in-out 0s;
  }
}

@keyframes imageAnimation {
  0% {
    opacity: 0;
    animation-timing-function: ease-in;
  }

  2% {
    opacity: 1;
  }

  8% {
    opacity: 1;
    transform: scale(1.05);
    animation-timing-function: ease-out;
  }

  17% {
    opacity: 1;
    transform: scale(1.1);
  }

  25% {
    opacity: 0;
    transform: scale(1.1);
  }

  100% {
    opacity: 0;
  }
}
</style>