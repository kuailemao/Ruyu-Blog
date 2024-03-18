<template>
  <div>
    <router-view></router-view>
  </div>
  <!-- 全局loading -->
  <loading></loading>
  <Music />
</template>

<script setup lang="ts">
import {useDark, useToggle} from "@vueuse/core";
import useWebsiteStore from "@/store/modules/website.ts";

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