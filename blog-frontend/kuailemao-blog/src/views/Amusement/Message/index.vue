<script setup lang="ts">
</script>

<template>
  <div>
    <Main margin-top="4%" is-side-bar>
      <template #information>
        <SideBar/>
      </template>
      <template #content>
        <router-view v-slot="{Component}">
          <transition name="el-fade-in-linear" mode="out-in">
            <component :is="Component"/>
          </transition>
        </router-view>
      </template>
    </Main>
  </div>
</template>

<style scoped lang="scss">

</style>