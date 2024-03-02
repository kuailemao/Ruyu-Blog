<script setup lang="ts">
import {getArticleList} from "@/apis/home";
import {ElMessage} from "element-plus";
import usePaginationStore from "@/store/modules/pagination";

const articleList = ref([{
  id: 0,
  articleTitle: '',
  articleContent: '',
  articleCover: '',
  categoryName: '',
  visitCount: '',
  commentCount: '',
  likeCount: '',
  favoriteCount: '',
  createTime: '',
  updateTime: '',
  tags: []
}]);

const paginationStore = usePaginationStore();

// 监听页数
watch(() => paginationStore.articlePagination.current, () => {
  getArticleListFunc()
  // 滚动到顶部
  window.scrollTo(0, 300);
})

// 页面加载时获取文章列表
onMounted(() => {
  getArticleListFunc()
})

function getArticleListFunc() {
  getArticleList(paginationStore.articlePagination.current, paginationStore.articlePagination.pageSize).then(res => {
    if (res.code === 200) {
      paginationStore.articlePagination.total = res.data.total;
      // 过滤内容
      res.data.page = res.data.page.map((item: any) => {
        item.articleContent = item.articleContent.replace(/[*#>`~\-\\[\]()\s]|(\n\n)/g, '')
        // 提取前 50 个字符
        item.articleContent = item.articleContent.substring(0, 60) + '...';
        return item;
      });
      articleList.value = res.data.page;
    } else {
      ElMessage.error(res.msg);
    }
  })
}

const card = ref()


</script>

<template>
  <!-- 封装文章列表卡片 -->
  <template v-for="(article,index) in articleList" :key="article.id" v-if="articleList.length > 1">
    <div v-slide-in ref="card" class="card">
      <div class="img img_left" style="margin-right: auto;" v-if="index % 2 == 0">
        <!-- 分类 -->
        <div class="classify" style="left: 0">
          {{ article.categoryName }}
        </div>
        <img :src="article.articleCover" alt="预览图" @click="$router.push('/article/'+article.id)"
             style="cursor: pointer"/>
      </div>
      <div class="character">
        <div class="title" @click="$router.push('/article/'+article.id)">{{ article.articleTitle }}</div>
        <div class="articleInformation">
          <el-tooltip effect="light" content="访问量">
            <div>
              <SvgIcon name="reading"/>
              <span>{{ article.visitCount }}</span>
            </div>
          </el-tooltip>
          <el-tooltip effect="light" content="评论数">
            <div>
              <SvgIcon name="comments"/>
              <span>{{ article.commentCount }}</span>
            </div>
          </el-tooltip>
          <el-tooltip effect="light" content="点赞数">
            <div>
              <SvgIcon name="like"/>
              <span>{{ article.likeCount }}</span>
            </div>
          </el-tooltip>
          <el-tooltip effect="light" content="收藏数">
            <div>
              <SvgIcon name="collection"/>
              <span>{{ article.favoriteCount }}</span>
            </div>
          </el-tooltip>
        </div>
        <div class="description" @click="$router.push('/article/'+article.id)">
          {{ article.articleContent }}
        </div>
        <div class="tag">
          <span>标签：</span>
          <el-tag size="small" v-for="tag in article.tags">{{ tag }}</el-tag>
        </div>
        <div style="display: flex;align-items: center;padding-top: 1rem">
          <div style="color: grey;font-size: 0.75em;margin-right: 1rem">发布于：{{ article.createTime }}</div>
          <div style="color: grey;font-size: 0.75em">更新于：{{ article.updateTime }}</div>
        </div>
      </div>
      <!-- 文章预览图 -->
      <div class="img img_right" style="margin-left: auto;" v-if="index % 2 == 1">
        <!-- 分类 -->
        <div class="classify" style="right: 0">
          {{ article.categoryName }}
        </div>
        <img :src="article.articleCover" alt="预览图" @click="$router.push('/article/'+article.id)"
             style="cursor: pointer"/>
      </div>
    </div>
  </template>
</template>

<style scoped lang="scss">

.card {
  width: 100%;
  height: 230px;
  border-radius: $border-radius;
  // 添加阴影
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  border: 1px solid var(--el-border-color);
  margin-bottom: 20px;
  display: flex;

  &:hover img {
    transform: scale(1.1);
  }

  .character {
    width: 50%;
    padding-left: 10px;

    .title {
      font-size: 22px;
      font-weight: bold;
      margin: 1rem 0;
      cursor: pointer;

      &:hover {
        color: #409EFF;
        // 过渡时间
        transition: color 0.3s;
      }
    }

    .articleInformation {
      display: flex;
      justify-content: flex-start;
      align-items: center;
      margin-top: 1rem;

      div {
        display: flex;

        span {
          margin-left: 5px;
          color: grey;
          font-size: 15px;
        }
      }

      // 第二个div开始
      div + div {
        margin-left: 10px;
      }
    }

    .description {
      font-size: 15px;
      padding: 1rem 0;
    }

    .tag {
      span{
        font-size: 1em;
      }
    }

    // 第二个tag开始
    .el-tag + .el-tag {
      margin-left: 5px;
    }
  }

  .img {
    position: relative;
    width: 45%;
    height: 100%;
    overflow: hidden;

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
      transition: transform 0.3s linear;
    }

    .classify {
      z-index: 1;
      position: absolute;
      top: 0;
      color: white;
      padding: 10px;
      backdrop-filter: blur(5px);
    }
  }

  .img_left {
    border-top-left-radius: $border-radius;
    border-bottom-left-radius: $border-radius;
  }

  .img_right {
    border-top-right-radius: $border-radius;
    border-bottom-right-radius: $border-radius;
  }
}

</style>