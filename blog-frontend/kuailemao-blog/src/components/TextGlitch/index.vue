<template>
  <div class="glitch-wrapper">
    <div class="glitch" :data-text="text">{{ text }}</div>
  </div>
</template>

<script setup lang="ts">
defineProps({
  text: {
    type: String,
    required: true
  }
});
</script>

<style scoped>
.glitch-wrapper {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
}

.glitch {
  position: relative;
  font-family: "Fredericka the Great", Mulish, -apple-system, "PingFang SC", "Microsoft YaHei",
  sans-serif;
  font-size: 4.5em;
  line-height: 1.5;
  color: white;
  animation: glitch-skew 1s infinite linear alternate-reverse;
  text-shadow: 0 1px 0 hsl(174, 5%, 80%), 0 2px 0 hsl(174, 5%, 75%),
  0 3px 0 hsl(174, 5%, 70%), 0 4px 0 hsl(174, 5%, 66%),
  0 5px 0 hsl(174, 5%, 64%), 0 6px 0 hsl(174, 5%, 62%),
  0 7px 0 hsl(174, 5%, 61%), 0 8px 0 hsl(174, 5%, 60%),
  0 0 5px rgba(0, 0, 0, 0.05), 0 1px 3px rgba(0, 0, 0, 0.2),
  0 3px 5px rgba(0, 0, 0, 0.2), 0 5px 10px rgba(0, 0, 0, 0.2),
  0 10px 10px rgba(0, 0, 0, 0.2), 0 20px 20px rgba(0, 0, 0, 0.3);
}

@media (max-width: 500px) {
  .glitch {
    font-size: 3em;
  }
}

.glitch::before,
.glitch::after {
  content: attr(data-text);
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.glitch::before {
  left: 2px;
  text-shadow: -2px 0 #ff00c1;
  clip: rect(44px, 450px, 56px, 0);
  animation: glitch-anim 5s infinite linear alternate-reverse;
}

.glitch::after {
  left: -2px;
  text-shadow: -2px 0 #00fff9, 2px 2px #ff00c1;
  animation: glitch-anim2 1s infinite linear alternate-reverse;
}

@keyframes glitch-anim {
  0% {
    clip: rect(64px, 9999px, 98px, 0);
    transform: skew(0.85deg);
  }
  5% {
    clip: rect(92px, 9999px, 52px, 0);
    transform: skew(0.36deg);
  }
  10% {
    clip: rect(61px, 9999px, 5px, 0);
    transform: skew(0.95deg);
  }
  15% {
    clip: rect(8px, 9999px, 23px, 0);
    transform: skew(0.51deg);
  }
  20% {
    clip: rect(76px, 9999px, 41px, 0);
    transform: skew(0.96deg);
  }
  25% {
    clip: rect(8px, 9999px, 6px, 0);
    transform: skew(0.55deg);
  }
  30% {
    clip: rect(88px, 9999px, 91px, 0);
    transform: skew(0.66deg);
  }
  35% {
    clip: rect(63px, 9999px, 99px, 0);
    transform: skew(0.2deg);
  }
  40% {
    clip: rect(23px, 9999px, 25px, 0);
    transform: skew(0.46deg);
  }
  45% {
    clip: rect(79px, 9999px, 97px, 0);
    transform: skew(0.01deg);
  }
  50% {
    clip: rect(59px, 9999px, 54px, 0);
    transform: skew(0.38deg);
  }
  55% {
    clip: rect(62px, 9999px, 58px, 0);
    transform: skew(0.06deg);
  }
  60% {
    clip: rect(57px, 9999px, 20px, 0);
    transform: skew(0.65deg);
  }
  65% {
    clip: rect(44px, 9999px, 59px, 0);
    transform: skew(0.44deg);
  }
  70% {
    clip: rect(48px, 9999px, 43px, 0);
    transform: skew(0.12deg);
  }
  75% {
    clip: rect(48px, 9999px, 24px, 0);
    transform: skew(0.92deg);
  }
  80% {
    clip: rect(74px, 9999px, 45px, 0);
    transform: skew(0.62deg);
  }
  85% {
    clip: rect(98px, 9999px, 43px, 0);
    transform: skew(0.17deg);
  }
  90% {
    clip: rect(28px, 9999px, 75px, 0);
    transform: skew(0.68deg);
  }
  95% {
    clip: rect(96px, 9999px, 63px, 0);
    transform: skew(0.2deg);
  }
  100% {
    clip: rect(88px, 9999px, 13px, 0);
    transform: skew(0.1deg);
  }
}

@keyframes glitch-anim2 {
  0% {
    clip: rect(43px, 9999px, 58px, 0);
    transform: skew(0.59deg);
  }
  5% {
    clip: rect(14px, 9999px, 55px, 0);
    transform: skew(0.1deg);
  }
  10% {
    clip: rect(25px, 9999px, 30px, 0);
    transform: skew(0.94deg);
  }
  15% {
    clip: rect(91px, 9999px, 61px, 0);
    transform: skew(0.69deg);
  }
  20% {
    clip: rect(65px, 9999px, 29px, 0);
    transform: skew(0.32deg);
  }
  25% {
    clip: rect(19px, 9999px, 98px, 0);
    transform: skew(0.22deg);
  }
  30% {
    clip: rect(55px, 9999px, 57px, 0);
    transform: skew(0.08deg);
  }
  35% {
    clip: rect(19px, 9999px, 91px, 0);
    transform: skew(0.23deg);
  }
  40% {
    clip: rect(11px, 9999px, 56px, 0);
    transform: skew(0.39deg);
  }
  45% {
    clip: rect(7px, 9999px, 51px, 0);
    transform: skew(0.21deg);
  }
  50% {
    clip: rect(22px, 9999px, 88px, 0);
    transform: skew(0.36deg);
  }
  55% {
    clip: rect(42px, 9999px, 38px, 0);
    transform: skew(0.5deg);
  }
  60% {
    clip: rect(20px, 9999px, 34px, 0);
    transform: skew(0.68deg);
  }
  65% {
    clip: rect(46px, 9999px, 82px, 0);
    transform: skew(0.5deg);
  }
  70% {
    clip: rect(1px, 9999px, 89px, 0);
    transform: skew(0.92deg);
  }
  75% {
    clip: rect(44px, 9999px, 85px, 0);
    transform: skew(0.44deg);
  }
  80% {
    clip: rect(84px, 9999px, 91px, 0);
    transform: skew(0.48deg);
  }
  85% {
    clip: rect(28px, 9999px, 68px, 0);
    transform: skew(0.24deg);
  }
  90% {
    clip: rect(7px, 9999px, 29px, 0);
    transform: skew(0.72deg);
  }
  95% {
    clip: rect(66px, 9999px, 91px, 0);
    transform: skew(0.37deg);
  }
  100% {
    clip: rect(14px, 9999px, 36px, 0);
    transform: skew(0.93deg);
  }
}

@keyframes glitch-skew {
  0% {
    transform: skew(1deg);
  }
  10% {
    transform: skew(0deg);
  }
  20% {
    transform: skew(1deg);
  }
  30% {
    transform: skew(-1deg);
  }
  40% {
    transform: skew(0deg);
  }
  50% {
    transform: skew(1deg);
  }
  60% {
    transform: skew(-1deg);
  }
  70% {
    transform: skew(2deg);
  }
  80% {
    transform: skew(-1deg);
  }
  90% {
    transform: skew(0deg);
  }
  100% {
    transform: skew(-2deg);
  }
}
</style> 