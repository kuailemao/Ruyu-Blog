<template>
  <div>
    <router-view></router-view>
  </div>
  <!-- 全局loading -->
  <loading></loading>
  <Music />
  <DevToolsBlocker :enableDevToolsBlocker="true" />
  <ContextMenu />
</template>

<script setup lang="ts">
import {useDark, useToggle} from "@vueuse/core";
import useWebsiteStore from "@/store/modules/website.ts";
import DevToolsBlocker from "@/components/DevToolsBlocker/index.vue";
import ContextMenu from "@/components/ContextMenu/index.vue";

const useWebsite = useWebsiteStore()

onMounted(() => {
  useWebsite.getInfo()
})

//  深色切换
useDark({
  selector: 'html',
  attribute: 'class',
  valueLight: 'light',
  valueDark: 'dark'
})

useDark({
  onChanged(dark) {
    useToggle(dark)
  }
})
</script>

<style scoped lang="scss">

</style>