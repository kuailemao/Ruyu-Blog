<script setup lang="ts">
import {ref} from 'vue'
import {Delete, Loading} from '@element-plus/icons-vue'
import useWebsiteStore from "@/store/modules/website.ts";
import {ArticleHotRecommend, ArticleSearch} from "@/apis/article/type.ts";
import router from "@/router";
import {useLocalStorage} from "@vueuse/core";
import {getHotRecommend, searchArticleContent} from "@/apis/article";
import {getRandomArticle} from "@/apis/home";
import {ElMessage} from "element-plus";

const emit = defineEmits(['isShowSearch'])

const websiteStore = useWebsiteStore()

const searchValue = ref('')

function handleSearch(_: any, isAutoFocus: boolean = false) {
  if (searchValue.value && optionsValue.value === '内容') {
    console.log(isAutoFocus)
    if (!isAutoFocus) {
      historyList.value.push(searchValue.value)
    }
    searchArticleContent(searchValue.value).then((res: any) => {
      if (res.code === 1004) {
        ElMessage.error(res.msg);
        return;
      }
      articleSearchList.value = res.data
      articleSearchList.value = articleSearchList.value?.map(item => {
        const regex = new RegExp(`(${searchValue.value})`, 'gi');
        const articleContent = item.articleContent.replace(regex, '<span class="highlight">$1</span>');
        return {
          ...item,
          articleContent
        };
      });
    })
  }
}

const optionsValue = ref('标题')

const options = ['标题', '内容']

const showSearch = ref(true)

const articleSearchList = ref<Array<ArticleSearch>>()

const historyList = useLocalStorage<string[]>('searchHistoryList', []);

const inputRef: Ref<HTMLInputElement | null> = ref(null);

const hotList = ref<Array<ArticleHotRecommend>>()

onMounted(() => {
  getHot()
})

// 搜索框获得焦点
async function handleFocus() {
  if ((searchValue.value && optionsValue.value === '标题') && !websiteStore.searchTitle) {
    await websiteStore.getArticleTitleList();
  }
  showSearch.value = false
}

watch(optionsValue, () => {
  articleSearchList.value = []
  searchValue.value = ''
})

watchEffect(async () => {
  if (!searchValue.value) {
    articleSearchList.value = []
  }
  if (searchValue.value && optionsValue.value === '标题') {
    if (!websiteStore.searchTitle) {
      await websiteStore.getArticleTitleList();
    }
    const query = searchValue.value.toLowerCase();
    articleSearchList.value = websiteStore.searchTitle?.filter(item =>
        item.articleTitle.toLowerCase().includes(query)
    ).map(item => {
      const regex = new RegExp(`(${query})`, 'gi');
      const highlightedTitle = item.articleTitle.replace(regex, '<span class="highlight">$1</span>');
      return {
        ...item,
        highlightedTitle
      };
    });
  }
})

// 搜索框失去焦点
function handleBlur() {
  showSearch.value = true
}

function clickSearchResult(articleId: number) {
  if (searchValue.value && optionsValue.value === '标题') {
    historyList.value.push(searchValue.value)
  }
  searchValue.value = ''
  router.push('/article/' + articleId)
  emit('isShowSearch')
}

function delAllHistory() {
  historyList.value = []
}

function historySearch(value: string) {
  searchValue.value = value
  inputRef.value?.focus();
  handleFocus()
  if (searchValue.value && optionsValue.value === '内容') {
    handleSearch('', true)
  }
}

function getHot() {
  getHotRecommend().then((res: any) => {
    hotList.value = res.data
  })
}

function changeToggle() {
  getRandomArticle().then(res => {
    hotList.value = res.data
  })
}

</script>

<template>
  <!-- 搜索 -->
  <div class="content_container">
    <div class="search">
      <el-form @submit.native.prevent>
        <el-input
            ref="inputRef"
            :placeholder="optionsValue === '标题' ? '请输入搜索内容' : '回车进行内容搜索'"
            v-model="searchValue"
            prefix-icon="el-icon-search"
            @keyup.enter.native="handleSearch"
            @focus="handleFocus"
            @blur="handleBlur"
        >
          <template #prefix>
            <div style="width: 0;">
              <SvgIcon name="search" width="20" height="20"/>
            </div>
          </template>
          <template #suffix>
            <div class="custom-style">
              <el-segmented v-model="optionsValue" :options="options" size="small"/>
            </div>
          </template>
        </el-input>
      </el-form>
    </div>
    <template v-if="showSearch">
      <div class="search_history">
        <!-- 搜索历史 -->
        <div class="header_history">
          <div>
            搜索历史
          </div>
          <div class="event_history" @click="delAllHistory">
            <el-icon>
              <Delete/>
            </el-icon>
            <span>清除记录</span>
          </div>
        </div>
        <!-- 历史记录 -->
        <div>
          <el-check-tag
              type="primary"
              style="margin: 5px"
              v-for="(item) in historyList"
              :key="item"
              checked
              @click="historySearch(item)"
          >
            {{ item }}
          </el-check-tag>
        </div>
        <!-- 热门推荐 -->
        <div class="header_history">
          <div>
            热门推荐
          </div>
          <div class="event_history" @click="changeToggle">
            <el-icon>
              <Loading/>
            </el-icon>
            <span>换一换</span>
          </div>
        </div>
        <div class="recommend_container">
          <div class="item" v-for="hot in hotList" :key="hot.id" @click="() => {
            emit('isShowSearch')
            $router.push('/article/' + hot.id)
          }">
            {{ hot.articleTitle }}
            <div>
              <SvgIcon name="heat"/>
              <span>{{ hot.visitCount }}</span></div>
          </div>
        </div>
      </div>
    </template>
    <template v-else>
      <div v-if="articleSearchList?.length === 0" style="text-align: center;padding-top: 2rem">
        <span style="font-size: 12px;color: grey;">{{
            optionsValue === '标题' ? '请输入要搜索的内容' : '内容搜索每分钟只能搜索5次'
          }}</span>
      </div>
      <div class="search_result">
        <template v-if="searchValue && optionsValue === '标题'">
          <div v-for="item in articleSearchList" :key="item.id" @mousedown="clickSearchResult(item.id)">
            <div class="search_result_item">
              <div>
                <div v-html="item.highlightedTitle"></div>
                <div class="text-xs mt-1 dark:text-[#A3A3A3] p-1">
                  <el-tag size="small" class="mr-2">
                    {{ item.categoryName }}
                  </el-tag>
                </div>
              </div>
              <div class="flex space-x-2 text-xs text-[#475569] items-center justify-center">
                <SvgIcon name="heat"/>
                <span>{{ item.visitCount }}</span>
              </div>
            </div>
          </div>
        </template>
        <template v-if="searchValue && optionsValue === '内容'">
          <div v-for="item in articleSearchList" :key="item.id" @mousedown="clickSearchResult(item.id)">
            <div class="search_result_item">
              <div>
                <div v-html="item.articleTitle"></div>
                <div class="text-xs mt-1 dark:text-[#A3A3A3] p-1 flex">
                  <div>
                    <el-tag size="small" class="mr-2">
                      {{ item.categoryName }}
                    </el-tag>
                  </div>
                  <div v-html="item.articleContent">
                  </div>
                </div>
              </div>
              <div class="flex space-x-2 text-xs text-[#475569] items-center justify-center">
                <SvgIcon name="heat"/>
                <span>{{ item.visitCount }}</span>
              </div>
            </div>
          </div>
        </template>
      </div>
    </template>
  </div>
</template>

<style scoped lang="scss">
@import "@/styles/mixin.scss";

// 搜索关键字高亮
:deep(.highlight) {
  background-color: yellow;
  border-radius: 5px;
}

.content_container {
  height: 100%;

  .search_result {
    width: 100%;
    height: 100%;
    overflow-y: scroll;
    overflow-x: hidden;
    margin-top: 1rem;

    .search_result_item {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    & > div {
      padding: 5px;
      cursor: pointer;
    }

    & > div:hover {
      background-color: #e0e2e5;
      transition: background-color 0.3s linear;
    }
  }

  // 搜索框
  .search_input {
    display: flex;
    justify-content: center;
    align-items: center;
    transition: transform 0.3s linear;
    cursor: pointer;

    &:hover {
      transform: scale(1.1);
    }
  }

  // 热门推荐
  .recommend_container {

    .item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin: 5px 10px;
      font-size: 16px;
      padding: 5px;
      border-radius: 5px;

      div {
        display: flex;
        justify-content: center;
        align-items: center;
        font-size: 10px;

        span {
          margin-left: 5px;
        }
      }
    }

    div:hover {
      cursor: pointer;
      color: #FE2C55FF;
      background-color: #e0e2e5;
      transition: background-color 0.3s linear;
    }
  }

  // 搜索历史
  .search_history {
    width: 100%;

    .header_history {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 10px;
      font-size: 16px;
      font-weight: bold;
    }

    .event_history {
      @include flex;

      &:hover {
        cursor: pointer;
        color: #FE2C55FF;
        transition: color 0.3s linear;
      }

      span {
        margin-left: 5px;
      }
    }
  }
}

.custom-style .el-segmented {
  --el-segmented-item-selected-color: var(--el-bg-color);
  --el-segmented-item-selected-bg-color: #ff8787;
  --el-border-radius-base: 16px;
  font-size: 0.9em;
  color: grey;
}

:deep(.search .el-input__wrapper) {
  padding: 0.5px 5px 0.5px 5px;
}

// 鼠标悬浮按钮上面
:deep(.el-input-group__append:hover) {
  // 背景颜色变化过渡
  transition: background-color 0.3s linear;
  background-color: #e0e2e5;
}

:deep(.el-input-group__append) {
  padding: 0 10px;
}
</style>