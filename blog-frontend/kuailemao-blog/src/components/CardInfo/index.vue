<script setup lang="ts">
import useWebsiteStore from "@/store/modules/website.ts";

const useWebsite = useWebsiteStore()
</script>

<template>
  <div class="info_container" :style="`background-image: url(${useWebsite.webInfo?.webmasterProfileBackground})`" v-slide-in>
    <div>
      <el-avatar size="large" :src="useWebsite.webInfo?.webmasterAvatar"/>
    </div>

    <div style="font-size: 18px;margin: 10px 0;color: white">
      {{ useWebsite.webInfo?.webmasterName }}
    </div>
    <div style="color: #bbbbbb;font-size: 15px;line-height: 25px" class="truncate ">
      {{ useWebsite.webInfo?.webmasterCopy }}
    </div>
    <div class="quantity_container">
      <div>
        <span>{{ useWebsite.webInfo?.articleCount }}</span>
        <span>文章数</span>
      </div>
      <el-divider direction="vertical"/>
      <div>
        <span>{{ useWebsite.webInfo?.categoryCount }}</span>
        <span>分类数</span>
      </div>
      <el-divider direction="vertical"/>
      <div>
        <span>{{ useWebsite.webInfo?.commentCount }}</span>
        <span>评论数</span>
      </div>
    </div>
    <div class="icon">
      <a :href="useWebsite.webInfo?.githubLink">
        <div>
          <SvgIcon name="github_icon" width="20px" height="20px"/>
        </div>
      </a>
      <a :href="useWebsite.webInfo?.giteeLink">
      <div>
        <SvgIcon name="gitee_icon" width="20px" height="20px"/>
      </div>
      </a>
    </div>
  </div>
</template>

<style scoped lang="scss">
.info_container {
  box-sizing: border-box;
  width: $card-width;
  height: 300px;
  margin: $card-margin;
  border-radius: $border-radius;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  text-align: center;
  border: 1px solid var(--el-border-color);
  background: var(--el-bg-color);
  background-size: 100% 67%;
  background-repeat: no-repeat;
  // 设置图片下面渐变过渡

  div .el-avatar {
    margin-top: 30px;
    width: 80px;
    height: 80px;
  }

  // 鼠标悬浮头像旋转
  div:hover .el-avatar {
    transition: all 0.9s;
    transform: rotate(360deg);
  }

  .quantity_container {
    display: flex;
    justify-content: space-around;
    width: 100%;
    margin-top: 0.7rem;
    div {
      display: flex;
      flex-direction: column;

      span {
        font-size: 15px;
        margin-top: 5px;
      }

      span:last-child {
        color: grey;
      }
    }

    .el-divider {
      margin-top: 10px;
      height: 30px;
    }
  }

  .icon {
    display: flex;
    // 左对齐
    justify-content: flex-start;
    margin: 10px 0 0 10px;

    div {
      margin-right: 10px;
    }
  }
}


</style>