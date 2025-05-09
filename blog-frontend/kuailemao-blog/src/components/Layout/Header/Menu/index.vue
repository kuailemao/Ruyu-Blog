<script setup lang="ts">

import {
  Clock,
  DocumentCopy,
  Files,
  Fries,
  Headset,
  HomeFilled,
  Postcard,
  PriceTag,
  Link, Setting, Promotion, ArrowDownBold, IceCreamRound, Close, PictureFilled
} from "@element-plus/icons-vue";
import {logout} from "@/apis/user"
import {REMOVE_TOKEN} from "@/utils/auth.ts";
import {useColorMode} from '@vueuse/core'
import useUserStore from "@/store/modules/user.ts"
import router from "@/router";
import useWebsiteStore from "@/store/modules/website.ts";
import SvgIcon from "@/components/SvgIcon/index.vue";
import {ref} from "vue";

const userStore = useUserStore()
const useWebsite = useWebsiteStore()
// 日夜切换
const mode = useColorMode()
const dialogVisible = ref(false)

const logoutSub = () => {
  logout().then((res: any) => {
    if (res.code === 200) {
      REMOVE_TOKEN()
      userStore.userInfo = undefined
      ElMessage.success('退出登录成功')
      router.push('/')
    }
  })
}

function changeToggle({detail}) {
  mode.value = detail
}

// 是否显示音乐模块
const env = import.meta.env

const isMenuVisible = ref(true);
const isTransparent = ref(true);
let lastScrollTop = 0;
let scrollTimeout: number | undefined;

const handleScroll = () => {
  const currentScrollTop = window.pageYOffset || document.documentElement.scrollTop;

  // 控制菜单显示和隐藏
  isMenuVisible.value = currentScrollTop <= lastScrollTop;

  // 立即更新背景透明状态
  isTransparent.value = currentScrollTop === 0;

  lastScrollTop = currentScrollTop <= 0 ? 0 : currentScrollTop; // For Mobile or negative scrolling
};

const debounceBackground = () => {
  if (scrollTimeout) {
    clearTimeout(scrollTimeout);
  }
  scrollTimeout = window.setTimeout(() => {
    const currentScrollTop = window.pageYOffset || document.documentElement.scrollTop;
    isTransparent.value = currentScrollTop === 0;
  }, 100); // 100ms 防抖时间
};

onMounted(() => {
  window.addEventListener('scroll', handleScroll);
  window.addEventListener('scroll', debounceBackground);
});

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll);
  window.removeEventListener('scroll', debounceBackground);
});
</script>

<template>
  <div class="search_dialog_container">
    <!-- 搜索内容 -->
    <el-dialog
        v-model="dialogVisible"
        :show-close="false"
        :close-on-click-modal="false"
        :lock-scroll="true"
    >
      <template #header>
        <div style="display: flex;justify-content: space-between;align-items: center">
          <span style="font-size: 1.2rem">搜索</span>
          <el-button :icon="Close" style="background: none;font-size: 1.5rem;width: 30px;border: none"
                     @click="dialogVisible = false"/>
        </div>
      </template>
      <Search @isShowSearch="dialogVisible = false"/>
    </el-dialog>
  </div>
  <nav :class="{ 'hidden': !isMenuVisible, 'transparent': isTransparent }">
    <div id="menu-left">
      <div id="menus">
        <span id="blog-info">
          <a href="/">{{ useWebsite.webInfo?.websiteName }}</a>
        </span>
        <div class="menus_items">
          <div class="menus_item" @click="router.push('/')">
            <span>
              <el-icon>
                <HomeFilled/>
              </el-icon>
              <span>首页</span>
            </span>
          </div>
          <div class="menus_item">
            <span>
              <el-icon>
                <Files/>
              </el-icon>
              <span>归档</span>
              <el-icon class="arrow">
                <ArrowDownBold/>
              </el-icon>
            </span>
            <ul class="menus_item_child">
              <li @click="router.push('/category')">
                <span>
                  <el-icon>
                    <DocumentCopy/>
                  </el-icon>
                  <span>分类</span>
                </span>
              </li>
              <li @click="router.push('/tags')">
                <span>
                  <el-icon>
                    <PriceTag/>
                  </el-icon>
                  <span>标签</span>
                </span>
              </li>
              <li @click="router.push('/timeline')">
                <span>
                  <el-icon>
                    <Clock/>
                  </el-icon>
                  <span>时间轴</span>
                </span>
              </li>
            </ul>
          </div>
          <div class="menus_item">
            <span>
              <el-icon>
                <IceCreamRound/>
              </el-icon>
              <span>其他</span>
               <el-icon class="arrow">
                <ArrowDownBold/>
              </el-icon>
            </span>
            <ul class="menus_item_child">
              <li @click="router.push('/tree-hole')">
                <span>
                  <el-icon>
                    <Fries/>
                  </el-icon>
                  <span>树洞</span>
                </span>
              </li>
              <li @click="router.push('/message')">
                <span>
                  <el-icon>
                    <Postcard/>
                  </el-icon>
                  <span>留言板</span>
                </span>
              </li>
              <li @click="router.push('/about')">
                <span>
                  <el-icon>
                    <Link/>
                  </el-icon>
                  <span>关于</span>
                </span>
              </li>
            </ul>
          </div>
          <div class="menus_item" @click="router.push('/link')">
            <span>
              <el-icon>
                <Link/>
              </el-icon>
              <span>友链</span>
            </span>
          </div>
          <div class="menus_item" @click="router.push('/music')" v-if="env.VITE_MUSIC_FRONTEND_URL">
            <span>
              <el-icon>
                <Headset/>
              </el-icon>
             <span>音乐</span>
            </span>
          </div>
          <div class="menus_item" @click="router.push('/photo')">
            <span>
              <el-icon>
                <PictureFilled/>
              </el-icon>
              <span>相册</span>
            </span>
          </div>
        </div>
      </div>
    </div>
    <div id="menu-right">
      <!-- 日夜切换 -->
      <div style="margin-right: 4.5rem;margin-top: -0.2rem">
        <toggle-button @change="changeToggle" size="1"></toggle-button>
      </div>
      <div id="search-button">
        <!-- 搜索按钮 -->
        <div class="search" @click="dialogVisible = true">
          <SvgIcon name="search" width="30" height="30" color="#409EFF" class="icon"/>
        </div>
      </div>
      <div class="user-info">
        <div v-if="!userStore.userInfo">
          <el-tooltip
              class="box-item"
              effect="light"
              content="点击去登录"
              placement="right"
          >
            <el-avatar @click="$router.push('/welcome')" style="margin-right: 3rem">登录</el-avatar>
          </el-tooltip>
        </div>
        <div v-else style="display: flex">
          <div class="profile">
            <div style="font-size: 15px;font-weight: bold;color: black">{{ userStore.userInfo?.username }}</div>
            <div style="font-size: 14px;color: #363636;margin-top: 3px"
                 v-if="userStore.userInfo?.registerType === 0">{{ userStore.userInfo?.email }}
            </div>
            <div style="font-size: 14px;color: #363636;margin-top: 3px" v-else>
              {{ userStore.userInfo?.registerType === 1 ? 'Gitee登录' : 'Github登录' }}
            </div>
          </div>
          <el-dropdown>
            <el-avatar style="margin-right: 3rem"
                       :src="userStore.userInfo?.avatar"></el-avatar>
            <template #dropdown>
              <el-dropdown-item @click="router.push('/setting')">
                <template #default>
                  <el-icon>
                    <Setting/>
                  </el-icon>
                  个人设置
                </template>
              </el-dropdown-item>
              <!--                  <el-dropdown-item>-->
              <!--                    <template #default>-->
              <!--                      <el-icon>-->
              <!--                        <Collection/>-->
              <!--                      </el-icon>-->
              <!--                      我的收藏-->
              <!--                    </template>-->
              <!--                  </el-dropdown-item>-->
              <el-dropdown-item @click="logoutSub">
                <template #default>
                  <el-icon>
                    <Promotion/>
                  </el-icon>
                  退出登录
                </template>
              </el-dropdown-item>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>
  </nav>
</template>

<style scoped lang="scss">
nav {
  position: fixed;
  top: 0;
  display: flex;
  height: 50px;
  width: 100%;
  z-index: 999;
  background-color: rgba(255, 255, 255, 0.4);
  backdrop-filter: blur(6px);
  transition: top 0.3s ease-in-out, background-color 0.3s ease-in-out;

  &.hidden {
    top: -50px; // 隐藏菜单
  }

  &.transparent {
    background-color: transparent; // 顶部时透明
  }

  #menu-left {
    flex: 2;

    #menus {
      display: flex;
      justify-content: left;
      align-items: center;
      height: 100%;
      width: 100%;
      font-weight: bold;

      #blog-info {
        width: 120px;
        margin: 0 10px;
      }

      .menus_items {
        width: 100%;
        height: 100%;
        display: flex;
        justify-content: left;

        .menus_item {
          position: relative;
          height: 100%;
          width: 100px;
          display: flex;
          justify-content: space-evenly;
          align-items: center;

          span .arrow {
            margin-left: 5px;
            transition: all 0.5s;
            transform: rotate(0deg);
            color: #409EFF;
          }

          &::before {
            content: '';
            position: absolute;
            bottom: 5px;
            left: 0;
            height: 2px;
            width: 0;
            background-color: #409EFF;
            transition: width 0.3s; // 添加过渡效果
          }

          &:hover {
            cursor: pointer;

            span .arrow {
              // 悬浮时旋转180度
              transition: all 0.5s;
              transform: rotate(180deg);
              color: #cc5de8;
            }

            .menus_item_child {
              display: block;
            }

            &::before {
              width: 100%; // 在悬浮时展开进度条
            }
          }

          span {
            display: flex;
            align-items: center;
            justify-content: center;
            span{
              margin-left: 5px;
            }
          }
        }

        .menus_item_child {
          display: none;
          position: absolute;
          top: 50px;
          background: var(--el-bg-color);
          // 阴影
          box-shadow: 0 2px 12px 0 var(--shadow-color);
          border-radius: 5px;

          li {
            display: flex;
            justify-content: left;
            padding: 10px;
            border-radius: 5px;

            &:hover {
              cursor: pointer;
              background: #91a7ff;
            }
          }

          // 子菜单出现动画
          @keyframes slide-down {
            0% {
              opacity: 0;
              transform: translateY(-10px);
            }
            100% {
              opacity: 1;
              transform: translateY(0);
            }
          }

          animation: slide-down 0.3s ease-out;
        }
      }
    }
  }

  #menu-right {
    flex: 1;
    width: 100%;
    display: flex;
    justify-content: right;
    align-items: center;

    .search {
      display: flex;
      justify-content: center;
      align-items: center;
      margin-right: 20px;
      transition: transform 0.3s linear;
      cursor: pointer;

      &:hover {
        transform: scale(1.1);
      }
    }
  }
}

.search_dialog_container {
  :deep(.el-dialog) {
    overflow: auto;
    border-radius: 10px;
    height: 70%;
  }

  @media screen and (max-width: 650px) {
    :deep(.el-dialog) {
      border-radius: 0;
      margin-top: 0;
      margin-bottom: 0;
      width: 100vw;
      height: 100%;
    }
  }
}

:deep(.el-dialog) {
  // 过渡效果
  transition: all .3s;
  @media (max-width: 1400px) {
    width: 45%;
  }
  @media (max-width: 1000px) {
    width: 60%;
  }
  @media (max-width: 760px) {
    width: 70%;
  }
  @media (max-width: 600px) {
    width: 90%;
  }
}

</style>