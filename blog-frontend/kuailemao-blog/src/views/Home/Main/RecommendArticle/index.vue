<script setup lang="ts">
import {getRecommendArticleList} from "@/apis/home";

const recommendArticles = ref([])

getRecommendArticleList().then(res => {
  // 过滤内容
  res.data = res.data.map((item: any) => {
    item.articleContent = item.articleContent.replace(/[*#>`~\-\\[\]()\s]|(\n\n)/g, '')
    // 提取前 50 个字符
    item.articleContent = item.articleContent.substring(0, 25) + '...';
    return item;
  });
  recommendArticles.value = res.data
})
</script>

<template>
  <div class="essay_title">
    <el-divider border-style="dashed" content-position="left">
      <div>
        <SvgIcon name="recommend" color="#409EFF" class="icon"/>
        <span>推荐</span>
      </div>
    </el-divider>
  </div>
  <div>
    <el-carousel trigger="click" height="200px" class="recommend" v-slide-in>
      <el-carousel-item v-for="recommendArticle in recommendArticles" :key="recommendArticle.id"
                        @click="$router.push(`/article/${recommendArticle.id}`)" style="cursor: pointer">
        <div class="item_text">
          <div style="font-size: 30px">
            {{ recommendArticle.articleTitle }}
          </div>
          <div style="font-size: 15px">
            {{ recommendArticle.createTime }}
          </div>
          <div style="font-size: 18px">
            {{ recommendArticle.articleContent }}
          </div>
        </div>
        <el-image :src="recommendArticle.articleCover"/>
      </el-carousel-item>
    </el-carousel>
  </div>
</template>

<style scoped lang="scss">


.recommend {
  border-radius: $border-radius;

  .item_text {
    position: absolute;
    width: 100%;
    height: 100%;
    // 文字上下居中
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    color: #fff;
    font-size: 20px;
    font-weight: bold;
    background-color: rgba(0, 0, 0, 0.1);
    padding: 0 20px;
    z-index: 1;
    line-height: 40px;
  }

  // 图片
  .el-image {
    transform: translate(0, -20%);
  }
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