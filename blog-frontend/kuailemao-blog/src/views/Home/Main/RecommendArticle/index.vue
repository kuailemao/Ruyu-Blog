<script setup lang="ts">
import { getRecommendArticleList } from "@/apis/home";
import { Swiper, SwiperSlide } from 'swiper/vue';
import { Navigation, Pagination, Autoplay } from 'swiper/modules';
// Import Swiper styles
import 'swiper/css';
import 'swiper/css/navigation';
import 'swiper/css/pagination';

const recommendArticles = ref([])

const modules = ref([Navigation,Pagination,Autoplay]);

function loadContent(){
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
}

</script>

<template>
  <div>
    <el-divider border-style="dashed" content-position="left">
      <div class="flex items-center">
        <SvgIcon name="recommend" color="#409EFF" class="icon"/>
        <span class="ml-[5px]">推荐</span>
      </div>
    </el-divider>
  </div>
  <div  v-view-request="{ callback: loadContent }">
    <swiper class="h-[200px] recommend"
            loop
            navigation
            :pagination="{ clickable: true }"
            :autoplay="{ delay: 2500 }"
            :modules="modules"
            v-if="recommendArticles.length > 0"
    >
      <swiper-slide v-for="recommendArticle in recommendArticles" :key="recommendArticle.id"
                    @click="$router.push(`/article/${recommendArticle.id}`)">
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
      </swiper-slide>
      <div class="swiper-pagination"></div>
    </swiper>
  </div>
  <el-skeleton v-if="recommendArticles.length == 0" :rows="5" animated />
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
    // 屏幕大于768时
    @media screen and (min-width: 768px) {
      transform: translate(0, -20%);
    }
  }
}
</style>