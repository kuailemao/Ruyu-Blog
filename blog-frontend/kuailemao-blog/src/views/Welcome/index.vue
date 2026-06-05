<script setup lang="ts">

</script>

<template>
  <Header/>
  <div style="width: 100vw;height: 100vh;overflow: hidden;display: flex">
    <!-- 左边区域 -->
    <div class="left-card">
      <!-- CONFIG:欢迎页面的背景图 -->
      <el-image style="width: 100%;height: 100%" fit="cover"
                src="http://image.zhuyuxi.xyz/blog/background/%E8%83%8C%E6%99%AF.png"/>
    </div>
    <div class="welcome-title">
      <div>欢迎来到我的个人博客</div>
    </div>
    <!-- 右边区域 -->
    <div class="right-card">
      <router-view v-slot="{Component}">
        <transition name="el-fade-in-linear" mode="out-in">
          <component :is="Component"/>
        </transition>
      </router-view>
    </div>
  </div>
</template>

<style scoped>
.left-card{
  flex: 1;
  background-color: black
}

.right-card {
  width: 400px;
  z-index: 1;
  background-color: var(--el-bg-color);
}

@media screen and (max-width: 600px) {
  .left-card {
    display: none;
  }
  .right-card{
    width: 100vw;
  }
}

.welcome-title {
  position: absolute;
  bottom: 30px;
  left: 30px;
  color: white;
  text-shadow: 0 0 10px white;
}

.welcome-title div:first-child {
  font-size: 30px;
  font-weight: bold;
}
</style>