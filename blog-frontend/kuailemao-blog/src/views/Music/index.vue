<script setup lang="ts">

// 音乐模块前台
const env = import.meta.env

function iframeLoaded() {
  document.getElementById('loading').style.display = 'none';
  document.querySelector('iframe').style.visibility = 'visible';
}
</script>

<template>
  <div>
    <div id="loading">正在加载...</div>
    <div>
          <iframe :src="env.VITE_MUSIC_FRONTEND_URL" class="if" frameborder="0" :onload="iframeLoaded"></iframe>
    </div>
  </div>
</template>

<style scoped lang="scss">
#loading {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 20px;
  color: #000;
}

iframe {
  visibility: hidden;
}

.if {
  width: 100vw;
  height: 100vh;
  max-width: 100%
}
</style>