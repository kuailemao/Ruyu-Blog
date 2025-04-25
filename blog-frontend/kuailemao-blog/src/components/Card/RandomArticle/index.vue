<script setup lang="ts">

import {getRandomArticle, getRelatedArticle} from "@/apis/home";
import {onMounted} from "vue";

const props = defineProps({
  title: {
    type: String,
    default: '随机文章',
  },
  prefixIcon: {
    type: String,
    default: 'random_essay',
  },
  // 分类id
  categoryId: {
    type: String,
    default: '',
  },
  // 文章id
  articleId: {
    type: String,
    default: '',
  }
});

const randomArticles = ref([{
  id: '',
  articleTitle: '',
  articleCover: '',
  createTime: '',
}])

function randomArticleBtn() {
  imgRefresh.value = true
  getRandomArticleList()
}

function getRandomArticleList(){
  getRandomArticle().then(res => {
    res.data = formatDate(res.data)
    randomArticles.value = res.data
  })
}

// 监听参数变化
watch(() => props.articleId, () => {
  relatedRecommendBtn(props.categoryId, props.articleId)
})

// 相关推荐
function relatedRecommendBtn(categoryId: string, articleId: string) {
  getRelatedArticle(categoryId, articleId).then(res => {
    res.data = formatDate(res.data)
    randomArticles.value = res.data
  })
}

// 去掉时分秒
function formatDate(date: []) {
  date.forEach((item:any) => {
    item.createTime = item.createTime.split(' ')[0]
  })
  return date
}

const imgRefresh = ref(false)

function loadContent(){
  if (props.title == "随机文章") {
    getRandomArticleList()
  }
  if (props.title == "相关推荐") {
    relatedRecommendBtn(props.categoryId, props.articleId)
  }
}

</script>

<template>
  <!-- 随机文章 -->
  <Card :title="title" :prefix-icon="prefixIcon" :suffix-icon="title === '相关推荐' ? '' : 'rotate'" @isRotate="true"
        :isScale="true" @invoke="randomArticleBtn" v-view-request="{ callback: loadContent }">
    <div class="random_container" v-for="randomArticle in randomArticles">
      <div class="random_image" @click="$router.push('/article/'+randomArticle.id)">
        <img v-if="randomArticle.articleCover" :src="imgRefresh ? randomArticle.articleCover: ''" :data-src="randomArticle.articleCover" v-lazy="true"  alt="文章封面"/>
      </div>
      <div class="random_text" :key="randomArticle.id">
        <div>{{ randomArticle.articleTitle }}</div>
        <div>{{ randomArticle.createTime }}</div>
      </div>
    </div>
  </Card>
</template>

<style scoped lang="scss">
.random_container {
  display: flex;
  align-items: center;
  margin: 10px 0;

  .random_image {
    width: 45%;
    height: 70px;
    overflow: hidden;
    cursor: pointer;
    border-radius: 0.5rem;

    img {
      width: 100%;
      height: 100%;
      transition: transform 0.3s linear;
      object-fit: cover;

      &:hover {
        transform: scale(1.1);
      }
    }
  }

  .random_text {
    width: 55%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    line-height: 30px;

    :first-child {
      // 文章超出一行显示省略号
      white-space: nowrap;
      // 超出部分隐藏
      overflow: hidden;
      // 显示省略号
      text-overflow: ellipsis;

      margin-left: 1rem;
      font-size: 1em;
      color: $menuActiveText;
    }

    :last-child {
      margin-left: 1rem;
      font-size: 0.8em;
    }
  }
}
</style>