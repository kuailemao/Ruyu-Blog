<template>
  <div class="welcome-character">
    <img 
      src="@/assets/images/趴着可爱女性角色_压缩.png"
      alt="Welcome Character"
      class="character-image"
    >
    <div class="welcome-message">欢迎访问，喜欢的话给个Star吧！！！</div>
  </div>
</template>

<script setup lang="ts">
// No script needed for this component
</script>

<style scoped lang="scss">
.welcome-character {
  position: fixed;
  right: 45%;
  bottom: -25px;
  z-index: 1000;
  
  .character-image {
    width: 200px;
    height: auto;
    transition: transform 0.3s ease;
    -webkit-user-drag: none;
    user-drag: none;
    
    &:hover {
      transform: scale(1.05);
      
      & + .welcome-message {
        opacity: 1;
        visibility: visible;
      }
    }
  }
  
  .welcome-message {
    position: absolute;
    top: -20px;
    left: 50%;
    transform: translateX(-50%);
    background-color: rgba(0, 0, 0, 0.7);
    color: white;
    padding: 8px 16px;
    border-radius: 20px;
    font-size: 14px;
    opacity: 0;
    visibility: hidden;
    transition: opacity 0.3s ease, visibility 0.3s ease;
    white-space: nowrap;
  }
}
</style> 