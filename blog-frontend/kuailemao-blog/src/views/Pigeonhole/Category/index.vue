<script setup lang="ts">
import {categoryList} from "@/apis/category";
import {whereArticleList} from "@/apis/article";
import ArticleList from "../ArticleList/index.vue"
import {dayjs} from "element-plus";

const route = useRoute()

const categorys = ref([])
const articleList = ref([])
const isQueryArticle = ref(false)
// 分类标题
const title = ref('')

onMounted(async () => {
  await categoryList().then(res => {
    if (res.code === 200) {
      categorys.value = res.data
    }
  })

  // 地址栏是否有分类id
  if (route.params.id) {
    isQueryArticle.value = true
    // 判断选中的分类
    categorys.value.forEach(item => {
      if (item.id === Number(route.params.id)) {
        item.isActive = true
        title.value = item.categoryName
      } else {
        item.isActive = false
      }
    })
    getArticle(route.params.id)
  }
})

// 地址栏是否有分类id
watch(() => route.params.id, (id) => {
  if (id) {
    isQueryArticle.value = true
    categorys.value.forEach(item => {
      if (item.id === Number(route.params.id)) {
        item.isActive = true
        title.value = item.categoryName
      } else {
        item.isActive = false
      }
    })
    getArticle(id)
  } else {
    isQueryArticle.value = false
  }
})

// 文章
function getArticle(id: string) {
  whereArticleList(1,id).then(res => {
    if (res.code === 200 && res.data !== undefined) {
      res.data.forEach(item => {
        item.createTime = dayjs(item.createTime).format('YYYY-MM-DD')
      })
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
        <Banner title="分类" subtitle="category"/>
      </template>
      <template #content>
        <template v-if="!isQueryArticle">
          <div class="category_container">
            <div class="title">
              文章分类
            </div>
            <div class="item_container">
              <template v-for="category in categorys" :key="category.id">
                <div v-slide-in class="item" @click="$router.push(`/category/${category.id}`)">
                  <div>{{ category.categoryName }}</div>
                  <div><span>{{ category.articleCount }}</span></div>
                </div>
              </template>
            </div>
          </div>
        </template>
        <template v-if="isQueryArticle">
          <div class="category_container">
            <div class="title">
              {{ title }}
            </div>
            <div>
              <el-scrollbar>
                <div class="scrollbar-flex-content">
                  <template v-for="category in categorys" :key="category.id">
                    <p @click="$router.push(`/category/${category.id}`)"
                       class="scrollbar-item" :class="category.isActive?'active':''">
                      {{ category.categoryName }}
                    </p>
                  </template>
                </div>
              </el-scrollbar>
            </div>
            <el-divider/>
            <ArticleList :articleList="articleList"/>
          </div>
        </template>
      </template>
    </Main>
  </div>
</template>

<style scoped lang="scss">

.category_container {
  display: flex;
  flex-direction: column;
  width: 100%;

  .scrollbar-flex-content {
    display: flex;
    white-space: nowrap;

    .active {
      color: grey !important;
      background: var(--el-color-danger-light-7) !important;
    }

    .scrollbar-item {
      flex-shrink: 0;
      display: flex;
      align-items: center;
      justify-content: center;
      height: 3em;
      margin: 0 1em;
      padding: 0.5rem 1rem;
      text-align: center;
      border-radius: 0.6em;
      border: 1px solid var(--el-color-danger-light-7);
      background: var(--el-color-danger-light-9);
      color: var(--el-color-danger);

      &:hover {
        cursor: pointer;
        color: grey;
        background: var(--el-color-danger-light-7);
      }
    }
  }

  .title {
    font-size: 1.72rem;
    padding: 1rem;
  }

  .item_container {
    display: flex;
    flex-wrap: wrap;

    .item {
      display: flex;
      flex-direction: column;
      width: calc(100% / 3 - 2em);
      height: 7em;
      background: var(--mao-bg-category);
      opacity: 0.8;
      margin: 1em;
      border-radius: $border-radius;
      padding: 1.3rem;
      transition: all 0.3s;

      @media screen and (max-width: 1200px) {
        width: calc(100% / 2 - 2em);
      }

      @media screen and (max-width: 1000px) {
        width: calc(100% - 2em);
      }

      &:hover {
        opacity: 1;
        box-shadow: 0 0 0.5rem 0.1rem #ccc;
        cursor: pointer;
      }

      & div:first-child {
        font-size: 1.33rem;
        font-weight: bold;
        // 左边框
        border-left: 0.15rem solid black;
        // 左边框的距离
        padding-left: 1rem;
        position: relative;

        &::after {
          content: '';
          position: absolute;
          left: 0;
          bottom: -1rem;
          width: 0;
          height: 0.2em;
          border-radius: 0.1em;
          // 蓝紫色渐变色背景
          background: white;
          transition: width 0.8s ease; /* 过渡动画效果 */
        }

        &:hover::after {
          width: 100%;
        }
      }

      & div:last-child {
        margin-top: 1.5rem;
        font-size: 1.3rem;
        color: grey;
      }
    }
  }
}


</style>