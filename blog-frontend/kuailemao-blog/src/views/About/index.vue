<script setup lang="ts">
import JSConfetti from 'js-confetti'
import useWebsiteStore from "@/store/modules/website.ts";

const jsConfetti = new JSConfetti()

jsConfetti.addConfetti()

const websiteStore = useWebsiteStore()
</script>

<template>
  <div class="flex justify-center items-center h-[100%] w-[99%]" >
    <div class="flex justify-center items-center max-lg:flex-col h-full w-full xl:w-[90%]">
      <div class="w-[40%] max-lg:w-full h-full flex flex-col justify-center items-center">
        <div class="h-[20rem] w-[20rem] rounded-full overflow-hidden mt-[5rem] drop-shadow-lg">
          <div class="bg-cover bg-no-repeat bg-center w-full h-full" :style="{ 'background-image': 'url(' + websiteStore.webInfo?.webmasterAvatar + ')' }"></div>
        </div>
        <div>
          <h1 :data-shadow='websiteStore.webInfo?.webmasterName'>{{ websiteStore.webInfo?.webmasterName }}</h1>
        </div>
        <div class="text-gray-600 w-full font-bold dark:text-gray-300 text-center">
          人生如棋落子无悔道心稳固如箭离弦永不回头
        </div>
      </div>
      <div class="flex flex-col justify-center items-center xl:w-[40%] lg:w-[60%] h-full">
        <div class="w-full h-[60vh] flex flex-col justify-center items-center">
          <div class="w-full text-[3rem] text-center text-gray-600 dark:text-gray-300 max-lg:text-[2rem] max-lg:pb-6">
            一名中二Web全栈小白
          </div>
          <div class="mt-4 text-center text-gray-500 dark:text-gray-300 max-lg:px-3">
            千年以前，看见元婴强者自己的小世界，非常羡慕，于是心中立誓，我也要变强，后抛弃世间情爱，终踏上修仙一途，虽一介散修，但亦往，经历千磨万难，
            炼气百年，四百年筑基，一千年结丹，两千年突破元婴，又一千年后遭遇瓶颈，决心闭死关，四千年后的今日，终于突破<span class="text-red-500 font-bold"> 化神</span>，感叹回首沧桑，
            道不尽仙凡殊途，尽人间。
          </div>
        </div>
        <div class="w-full h-[40vh] flex flex-col justify-center items-center">
          <div class="text-gray-600 dark:text-gray-300 ">------------------------我的个人导航------------------------</div>
          <div class="flex justify-center items-center h-full">
            <a href="https://github.com/kuailemao" target="_blank">
              <div class="bg-white dark:bg-slate-800 w-[120px] h-[120px] flex justify-center items-center rounded-2xl drop-shadow-lg">
                <SvgIcon name="github_icon" width="100px" height="100px"/>
              </div>
            </a>
            <a href="https://gitee.com/kuailemao" target="_blank"  class="mx-10 max-lg:mx-4">
              <div class="bg-white dark:bg-slate-800 w-[120px] h-[120px] flex justify-center items-center rounded-2xl drop-shadow-lg">
                <SvgIcon name="gitee_icon" width="100px" height="100px"/>
              </div>
            </a>
            <a href="https://space.bilibili.com/299105957" target="_blank">
              <div class="bg-white dark:bg-slate-800 w-[120px] h-[120px] flex justify-center items-center rounded-2xl drop-shadow-lg">
                <SvgIcon name="bilibili_icon" width="100px" height="100px"/>
              </div>
            </a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
@import url(https://fonts.googleapis.com/css?family=Righteous);

*, *:before, *:after {
  box-sizing: border-box;
  position: relative;
}

h1 {
  display: inline-block;
  color: white;
  font-family: 'Righteous', serif;
  font-size: 10em;
  text-shadow: .03em .03em 0 hsla(230,40%,50%,1);
}
h1:after {
  content: attr(data-shadow);
  position: absolute;
  top: .06em; left: 0.06em;
  z-index: -1;
  text-shadow: none;
  background-image:
      linear-gradient(
              45deg,
              transparent 45%,
              hsla(48,20%,90%,1) 45%,
              hsla(48,20%,90%,1) 55%,
              transparent 0
      );
  background-size: .05em .05em;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;

  animation: shad-anim 15s linear infinite;
}

@keyframes shad-anim {
  0% {background-position: 0 0}
  0% {background-position: 100% -100%}
}
</style>