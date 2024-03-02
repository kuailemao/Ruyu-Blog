<template>
  <!-- svg：图标外层容器节点，内部需要与 use 标签结合使用 -->
  <svg :style="{ width, height }">
    <!-- xlink:href执行用哪一个图标，属性值务必#icon-图标名字 -->
    <!-- use 标签 fill 属性可以设置图标的颜色 -->
    <use :xlink:href="prefix + name" :fill="color"></use>
  </svg>
</template>

<script setup lang="ts">
// 接收父组件传递过来的参数
defineProps({
  // xlink:href 属性值的前缀
  prefix: {
    type: String,
    default: '#icon-',
  },
  // 提供使用的图标的名字
  name: String,
  // 接收父组件传递的颜色
  color: {
    type: String,
    default: '',
  },
  // 接收父组件传递过来的图标宽度
  width: {
    type: String,
    default: '16px',
  },
  // 接收父组件传递过来的图标高度
  height: {
    type: String,
    default: '16px',
  },
})
</script>

<style scoped lang="scss">

</style>
