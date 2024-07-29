<script setup lang="ts">
defineProps({
  title: String,
  subtitle: String
})


</script>

<template>
  <div>
    <div class="header">
      <h2 class="title">{{ title }}</h2>
      <h3 class="subtitle"><span>{{ subtitle }}</span></h3>
    </div>
  </div>
</template>

<style scoped lang="scss">
.header {
  height: 50vh;
  width: 100%;
  text-align: center;
  margin-bottom: 5rem;
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: center;
  background-image: url("https://image.kuailemao.xyz/blog/background/%E5%AD%A6%E4%B9%A0%E7%9A%84%E5%8F%B2%E8%92%82%E5%A4%AB.jpg");
  background-size: cover;
  background-position: center;

  .title {
    color: white;
    font-size: 4.6rem;
    font-weight: normal;
    text-shadow: 0 1px 0 hsl(174, 5%, 80%), 0 2px 0 hsl(174, 5%, 75%),
    0 3px 0 hsl(174, 5%, 70%), 0 4px 0 hsl(174, 5%, 66%),
    0 5px 0 hsl(174, 5%, 64%), 0 6px 0 hsl(174, 5%, 62%),
    0 7px 0 hsl(174, 5%, 61%), 0 8px 0 hsl(174, 5%, 60%),
    0 0 5px rgba(0, 0, 0, 0.05), 0 1px 3px rgba(0, 0, 0, 0.2),
    0 3px 5px rgba(0, 0, 0, 0.2), 0 5px 10px rgba(0, 0, 0, 0.2),
    0 10px 10px rgba(0, 0, 0, 0.2), 0 20px 20px rgba(0, 0, 0, 0.3);
  }

  $shadow: #533d4a;
  $red: #e55643;
  $green: #6ba988;
  $yellow: #f1c83c;
  $shadow: #533d4a;

  .subtitle {
      color: rgba(255, 255, 255, 0.5);
      font-size: 1.6rem;
      letter-spacing: 5px;
      margin: 1rem 0 0 0;
      font-weight: normal;

    span {
      transform: skew(-10deg);
      display: block;
      float: left;
      text-shadow: $shadow 1px 1px, $shadow 2px 2px, $shadow 3px 3px,
      $shadow 4px 4px, $shadow 5px 5px, $shadow 6px 6px;
      min-width: 10px;
      min-height: 10px;
      position: relative;
    }
  }

  .subtitle {
    &:nth-child(1) {
      color: $red;
    }
    &:nth-child(2) {
      color: $green;
    }
    &:nth-child(3) {
      color: $yellow;
    }
  }
}
</style>