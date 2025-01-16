<script setup lang="ts">

import {
  Bowl, Clock,
  DocumentCopy,
  Files,
  Fries, Headset,
  HomeFilled,
  IceCreamRound,
  Link, PictureFilled,
  Postcard, PriceTag, UserFilled
} from "@element-plus/icons-vue";
import router from "@/router";

const emit = defineEmits("update:closeDrawer")

function isClose(){
  emit("update:closeDrawer")
}

// 是否显示音乐模块
const env = import.meta.env
</script>
<template>
<div>
  <el-menu
      router
      style="width: 100%;border: none"
      class="menu"
  >
    <el-menu-item index="/" class="index" @click="isClose">
      <el-icon>
        <HomeFilled/>
      </el-icon>
      首页
    </el-menu-item>
    <el-sub-menu index="2">
      <template #title>
        <el-icon>
          <Files/>
        </el-icon>
        归档
      </template>
      <el-menu-item index="/timeline" @click="isClose">
        <el-icon>
          <Clock/>
        </el-icon>
        时间轴
      </el-menu-item>
      <el-menu-item index="/category" @click="isClose">
        <el-icon>
          <DocumentCopy/>
        </el-icon>
        分类
      </el-menu-item>
      <el-menu-item index="/tags" @click="isClose">
        <el-icon>
          <PriceTag/>
        </el-icon>
        标签
      </el-menu-item>
    </el-sub-menu>
    <el-sub-menu index="3">
      <template #title>
        <el-icon>
          <IceCreamRound/>
        </el-icon>
        其他
      </template>
      <el-menu-item index="/tree-hole" @click="isClose">
        <el-icon>
          <Fries/>
        </el-icon>
        树洞
      </el-menu-item>
      <el-menu-item index="/message" @click="isClose">
        <el-icon>
          <Postcard/>
        </el-icon>
        留言板
      </el-menu-item>
    </el-sub-menu>
    <el-menu-item index="/link" @click="isClose">
      <el-icon>
        <Link/>
      </el-icon>
      友链
    </el-menu-item>
    <template v-if="env.VITE_MUSIC_FRONTEND_URL">
      <el-menu-item index="/music">
        <el-icon>
          <Headset/>
        </el-icon>
        音乐
      </el-menu-item>
    </template>
    <el-menu-item index="/about" @click="isClose">
      <el-icon>
        <UserFilled/>
      </el-icon>
      关于
    </el-menu-item>
    <el-menu-item index="/photo" @click="isClose">
      <el-icon>
        <PictureFilled/>
      </el-icon>
      相册
    </el-menu-item>
  </el-menu>
</div>
</template>

<style scoped lang="scss">

</style>