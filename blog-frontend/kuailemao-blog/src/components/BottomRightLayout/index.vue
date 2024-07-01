<script setup lang="ts">
defineProps({
  toTop: {
    type: Boolean,
    default: false,
  },
  scrollPercentage: {
    type: Boolean,
    default: false,
  }
})
</script>

<template>
  <div class="container_div">
    <div class="scroll_percentage" v-if="scrollPercentage">
      <slot name="scroll_percentage"/>
    </div>
    <ToTop v-if="toTop"/>
  </div>
</template>

<style scoped lang="scss">
.container_div {
  position: fixed;
  bottom: 5rem;
  right: 5rem;
  padding: 0.5rem;
  width: 60px;
  height: 200px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;


  // 屏幕宽度小于768时
  @media screen and (max-width: 768px) {
    height: 140px;
    bottom: 0;
    right: 1.2rem;
  }

  .scroll_percentage{
    background: var(--mao-scroll-percentage-bg);
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
    border-radius: 10px;
    width: 50px;
    height: 50px;
    margin-bottom: 2rem;
    display: flex;
    justify-content: center;
    align-items: center;
    font-weight: bold;

    @media screen and (max-width: 768px) {
      width: 40px;
      height: 40px;
    }
  }
}
</style>