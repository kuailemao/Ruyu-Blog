<script setup>
defineProps({
  title: {
    type: String,
    default: "",
  },
});
</script>

<template>
  <div class="glitch" :data-text="title">{{ title }}</div>
</template>

<style lang="scss" scoped>
.glitch {
  color: white;
  font-size: 52px;
  position: relative;
  margin: 0 auto;
  z-index: 2000;
  text-shadow: 0px 0px 5px var(--global-white);
}
// @keyframes noise-anim {
//   $steps: 20;
//   @for $i from 0 through $steps {
//     #{percentage($i*calc((1/$steps)))} {
//       clip: rect(random(100) + px, random(999) + px, random(100) + px, 0);
//     }
//   }
// }
// .glitch:after {
//   content: attr(data-text);
//   position: absolute;
//   left: 2px;
//   text-shadow: -1px 0 red;
//   top: 0;
//   bottom: 0;
//   right: 0;
//   color: white;
//   overflow: hidden;
//   clip: rect(0, 10px, 0, 0);
//   animation: noise-anim 9s infinite linear alternate-reverse;
// }

// @keyframes noise-anim-2 {
//   $steps: 20;
//   @for $i from 0 through $steps {
//     #{percentage($i* calc((1/$steps)))} {
//       clip: rect(random(100) + px, random(999) + px, random(100) + px, 0);
//     }
//   }
// }
// .glitch:before {
//   content: attr(data-text);
//   position: absolute;
//   left: -2px;
//   text-shadow: 1px 0 #0000ff;
//   top: 0;
//   bottom: 0;
//   right: 0;
//   color: white;
//   overflow: hidden;
//   clip: rect(0, 10px, 0, 0);
//   animation: noise-anim-2 12s infinite linear alternate-reverse;
// }

@media screen and (max-width: 768px) {
  .glitch {
    font-size: 42px;
  }
}
</style>
