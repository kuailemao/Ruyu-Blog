<script setup lang="ts">
import {ref} from 'vue'
import {Delete, Loading} from '@element-plus/icons-vue'
import useWebsiteStore from "@/store/modules/website.ts";

const websiteStore = useWebsiteStore()

const searchValue = ref('')

function handleSearch() {
  ElNotification({
    title: '搜索功能敬请期待',
    showClose: false,
    duration: 6000,
    message: '该模块未开放，作者没有空',
    type: 'success',
  })
}

const value = ref('标题')

const options = ['标题', '内容']

const showSearch = ref(true)

// 搜索框获得焦点
function handleFocus() {
  // TODO 搜索策略
  console.log('搜索次数：{}',websiteStore.searchCountByTitle)
  showSearch.value = false
}

// 搜索框失去焦点
function handleBlur() {
  showSearch.value = true
}

</script>

<template>
  <!-- 搜索 -->
  <div class="content_container">
    <div class="search">
      <el-input
          placeholder="回车或点击搜索按钮"
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
            <el-segmented v-model="value" :options="options" size="small" />
          </div>
        </template>
      </el-input>
    </div>
    <template v-if="showSearch">
      <div class="search_history">
        <!-- 搜索历史 -->
        <div class="header_history">
          <div>
            搜索历史
          </div>
          <div class="event_history">
            <el-icon>
              <Delete/>
            </el-icon>
            <span>清除记录</span>
          </div>
        </div>
        <!-- 历史记录 -->
        <div>
          <el-tag
              closable
              size="small"
              effect="plain"
              style="margin: 5px"
          >
            vue
          </el-tag>
          <el-tag
              closable
              size="small"
              effect="plain"
              style="margin: 5px"
          >
            react
          </el-tag>
          <el-tag
              closable
              size="small"
              effect="plain"
              style="margin: 5px"
          >
            Java语言基础
          </el-tag>
          <el-tag
              closable
              size="small"
              effect="plain"
              style="margin: 5px"
          >
            站🐖长得多帅
          </el-tag>
          <el-tag
              closable
              size="small"
              effect="plain"
              style="margin: 5px"
          >
            宇宙无敌帅
          </el-tag>
          <el-tag
              closable
              size="small"
              effect="plain"
              style="margin: 5px"
          >
            哈哈哈
          </el-tag>
        </div>
        <!-- 热门推荐 -->
        <div class="header_history">
          <div>
            热门推荐
          </div>
          <div class="event_history">
            <el-icon>
              <Loading/>
            </el-icon>
            <span>换一换</span>
          </div>
        </div>
        <div class="recommend_container">
          <div class="item">
            Lambda+Stream函数式编程
            <div>
              <SvgIcon name="heat"/>
              <span>1312</span></div>
          </div>
          <div class="item">
            Java8-17新特性
            <div>
              <SvgIcon name="heat"/>
              <span>1242</span></div>
          </div>
          <div class="item">
            MybatisPlus
            <div>
              <SvgIcon name="heat"/>
              <span>1242</span></div>
          </div>
          <div class="item">
            分布式理论
            <div>
              <SvgIcon name="heat"/>
              <span>606</span></div>
          </div>
          <div class="item">
            MQ消息队列
            <div>
              <SvgIcon name="heat"/>
              <span>417</span></div>
          </div>
        </div>
      </div>
    </template>
    <template v-else>
      <div style="text-align: center;padding-top: 2rem">
        <span style="font-size: 12px;color: grey;">请输入要搜索的内容</span>
      </div>
    </template>
  </div>
</template>

<style scoped lang="scss">
@import "@/styles/mixin.scss";

.content_container {
  height: 100%;

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

:deep(.search .el-input__wrapper){
  padding: 0.5px 5px 0.5px 5px;
}

// 鼠标悬浮按钮上面
:deep(.el-input-group__append:hover) {
  // 背景颜色变化过渡
  transition: background-color 0.3s linear;
  background-color: #e0e2e5;
}

:deep(.el-input-group__append){
  padding: 0 10px;
}
</style>