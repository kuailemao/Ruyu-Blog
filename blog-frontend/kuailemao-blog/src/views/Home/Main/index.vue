<template>
  <Main is-side-bar>
    <!-- 内容区 -->
    <template #content>
      <div class="announcement">
        <SvgIcon style="min-width: 30px" name="notice" color="#409EFF"/>
        <span>{{ useWebsite?.webInfo?.headerNotification }}</span>
      </div>
      <RecommendArticle/>
      <div class="essay_title">
        <el-divider border-style="dashed" content-position="left">
          <div>
            <SvgIcon name="essay_icon" color="#409EFF" class="icon"/>
            <span>文章</span>
          </div>
        </el-divider>
      </div>
      <div>
        <CardEssay/>
      </div>
      <div>
        <Pagination/>
      </div>
      <div>
        <el-divider border-style="dashed" content-position="center">
          <div style="font-weight: bold">~~到达底部啦~~</div>
        </el-divider>
      </div>
    </template>
    <!-- 侧边栏 -->
    <template #information>
      <SideBar/>
    </template>
  </Main>
</template>

<script setup lang="ts">
import RecommendArticle from './RecommendArticle/index.vue'
import useWebsiteStore from "@/store/modules/website.ts";

const useWebsite = useWebsiteStore()

</script>

<style scoped lang="scss">

.announcement {
  min-height: 50px;
  margin-top: 10px;
  padding: 5px;
  text-align: center;
  border: 1px dotted #ccc;
  border-radius: 15px;
  // 与图标对齐
  display: flex;
  justify-content: center;
  align-items: center;
}

.information_container {
  width: 30%;
  height: 100%;
  //border: red 1px solid;
}

.essay_title {

  div {
    display: flex;

    span {
      margin-left: 5px;
    }
  }

  .icon {
    color: #409EFF;
  }
}
</style>