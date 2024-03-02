<script setup lang="ts">
import ArticleList from "../ArticleList/index.vue"
import {tagList} from "@/apis/tag";
import {whereArticleList} from "@/apis/article";
import {dayjs} from "element-plus";

const route = useRoute()

const isQueryArticle = ref(false)
const tags = ref([])
const articleList = ref([])
const title = ref('')

onMounted(async () => {
  await tagList().then(res => {
    if (res.code === 200) {
      tags.value = res.data
    }
  })
  if (route.params.id) {
    isQueryArticle.value = true
    tags.value.forEach(item => {
      if (item.id === Number(route.params.id)) {
        title.value = item.tagName
      }
    })
    getArticle(route.params.id)
  }
})

watch(() => route.params.id, (id) => {
  if (id) {
    isQueryArticle.value = true
    tags.value.forEach(item => {
      if (item.id === Number(route.params.id)) {
        title.value = item.tagName
      }
    })
    getArticle(id)
  } else {
    isQueryArticle.value = false
  }
})

// 文章
function getArticle(id: string) {
  whereArticleList(2, id).then(res => {
    if (res.code === 200 && res.data !== undefined) {
      res.data.forEach(item => {
        item.createTime = dayjs(item.createTime).format('YYYY-MM-DD')
      })
      console.log(res.data)
      articleList.value = res.data
    } else {
      articleList.value = []
    }
  })
}

</script>

<template>
  <div>
    <Main only-father-container>
      <template #banner>
        <Banner title="标签页" subtitle="tags"/>
      </template>
      <template #content>
        <div class="tags_container">
          <div class="title" v-if="!isQueryArticle">
            标签 {{ title }}
          </div>
          <div class="title" v-if="isQueryArticle">
            标签 - {{ title }}
          </div>
          <template v-if="!isQueryArticle">
            <div class="item_container">
              <template v-for="tag in tags" :key="tag.id">
                <div v-slide-in class="item" @click="$router.push(`/tags/${tag.id}`)">
                  <span @click="$router.push(`/tags/${tag.id}`)"># {{ tag.tagName }}</span>
                  <span>{{ tag.articleCount }}</span></div>
              </template>
            </div>
          </template>
          <template v-if="isQueryArticle">
            <el-divider/>
            <ArticleList :article-list="articleList"/>
          </template>
        </div>
      </template>
    </Main>
  </div>
</template>

<style scoped lang="scss">
.tags_container {
  display: flex;
  flex-direction: column;
  width: 100%;

  .title {
    font-size: 2rem;
    padding: 1rem 0 0 1rem;
  }

  .item_container {
    display: flex;
    flex-wrap: wrap;
    padding: 1rem;

    .item {
      display: flex;
      font-size: 1.2rem;
      margin: 0.5rem;
      padding: 0.5rem;
      border: 1px solid var(--el-border-color);
      border-radius: 5px;
      cursor: pointer;
      transition: all 0.3s;
      color: #565352;

      & span:first-child {
        color: grey;
        margin-right: 0.4rem;
      }

      & span:last-child {
        display: flex;
        align-items: center;
        color: white;
        margin-left: 0.4rem;
        font-size: 0.6rem;
        background-color: #555555;
        padding: 0.1rem 0.5rem;
        border-radius: 0.7em;
      }

      &:hover {
        border: 1px solid #409EFF;
        transform: translateY(-0.2rem);
      }
    }
  }
}

</style>