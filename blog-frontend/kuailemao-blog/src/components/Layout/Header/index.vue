<script lang="ts" setup>
import {ref} from 'vue'
import {
  Files,
  HomeFilled,
  IceCreamRound,
  Postcard,
  Bowl,
  Link,
  UserFilled,
  Headset,
  Collection,
  Setting, ChatLineSquare, Promotion, Clock, DocumentCopy, PriceTag, Fries, Close
} from '@element-plus/icons-vue'
import SvgIcon from '@/components/SvgIcon/index.vue'
import { useColorMode } from '@vueuse/core'
import DayNightToggleButton from "@/components/DayNightToggle"
import useUserStore from "@/store/modules/user.ts"
import {logout, oauthLogin} from "@/apis/user"
import {REMOVE_TOKEN, SET_TOKEN} from "@/utils/auth.ts"
import {ElMessage} from "element-plus"
import router from "@/router"
import useWebsiteStore from "@/store/modules/website.ts";

const userStore = useUserStore()
const useWebsite = useWebsiteStore()
const route = useRoute()
// 日夜切换
const mode = useColorMode()

onMounted(async () => {
  customElements.define("toggle-button", DayNightToggleButton);
  await userStore.getInfo()
})

thirdLogin()

// 第三方登录
function thirdLogin() {
  // 判断url上面是否有gitee的token
  let access_token = route.query.access_token
  let login_type = route.query.login_type
  let user_name = route.query.user_name
  if (access_token && login_type) {
    oauthLogin(<string>access_token, <string>login_type, <string>user_name).then(async (res: any) => {
      if (res.code === 200) {
        // 去掉参数query
        await router.replace({query: {}})
        SET_TOKEN(res.data.token, res.data.expire, true)
        await userStore.getInfo()
        ElMessage.success('登录成功')
        await router.push('/')
      } else {
        ElMessage.error(res.msg)
      }
    })
  }
}

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

// 菜单默认值
const activeIndex = ref('1')
const handleSelect = (key: string, keyPath: string[]) => {
  // console.log(key, keyPath)
}

const isHeader = ref(true)

// 监听滚轮事件
window.addEventListener('wheel', function (event) {
  if (event.deltaY > 0) {
    // 向下滚动
    // console.log('向下滚动');
    isHeader.value = false
  } else if (event.deltaY < 0) {
    // 向上滚动
    // console.log('向上滚动');
    isHeader.value = true
  }
});

const dialogVisible = ref(false)
const drawer = ref(false)

function changeToggle({detail}) {
  mode.value = detail
}

// 是否显示音乐模块
const env = import.meta.env
</script>
<template>
  <div>
    <!-- 搜索内容 -->
    <el-dialog
        v-model="dialogVisible"
        width="35%"
        :show-close="false"
        style="border-radius: 15px"
        :close-on-click-modal="false"
        :lock-scroll="false"
    >
      <template #header>
        <div style="display: flex;justify-content: space-between;align-items: center">
          <span style="font-size: 1.2rem">搜索</span>
          <el-button :icon="Close" style="background: none;font-size: 1.5rem;width: 30px;border: none"
                     @click="dialogVisible = false"/>
        </div>
      </template>
      <Search/>
    </el-dialog>
  </div>
  <div>
    <transition name="content-header">
      <div class="content-header" v-show="isHeader">
        <el-menu
            router
            :default-active="activeIndex"
            mode="horizontal"
            :ellipsis="false"
            @select="handleSelect"
            style="width: 100%;border: none"
            class="menu"
        >
          <el-tooltip
              class="box-item"
              effect="light"
              content="点击回到首页"
              placement="bottom"
          >
            <el-link href="/">
              <!--              <el-image class="logo" src="https://element-plus.org/images/element-plus-logo.svg"/>-->
              <div style="color: white;font-size: 1rem;font-weight: bold;margin-left: 0.2rem">
                {{ useWebsite.webInfo?.websiteName }}
              </div>
            </el-link>
          </el-tooltip>

          <el-menu-item index="/" class="index">
            <el-icon>
              <HomeFilled/>
            </el-icon>
            首页
          </el-menu-item>
          <el-sub-menu index="2">
            <template #title>
              <el-icon>
                <Files/>
              </el-icon>
              归档
            </template>
            <el-menu-item index="/timeline">
              <el-icon>
                <Clock/>
              </el-icon>
              时间轴
            </el-menu-item>
            <el-menu-item index="/category">
              <el-icon>
                <DocumentCopy/>
              </el-icon>
              分类
            </el-menu-item>
            <el-menu-item index="/tags">
              <el-icon>
                <PriceTag/>
              </el-icon>
              标签
            </el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="3">
            <template #title>
              <el-icon>
                <IceCreamRound/>
              </el-icon>
              其他
            </template>
            <el-menu-item index="/tree-hole">
              <el-icon>
                <Fries/>
              </el-icon>
              树洞
            </el-menu-item>
            <el-menu-item index="/message">
              <el-icon>
                <Postcard/>
              </el-icon>
              留言版
            </el-menu-item>
            <el-menu-item index="/chat-gpt">
              <el-icon>
                <Bowl/>
              </el-icon>
              ChatGPT
            </el-menu-item>
          </el-sub-menu>
          <el-menu-item index="/link">
            <el-icon>
              <Link/>
            </el-icon>
            友链
          </el-menu-item>
          <template v-if="env.VITE_MUSIC_FRONTEND_URL">
          <el-menu-item index="/music">
            <el-icon>
              <Headset/>
            </el-icon>
            音乐
          </el-menu-item>
          </template>
          <el-menu-item index="/about">
            <el-icon>
              <UserFilled/>
            </el-icon>
            关于
          </el-menu-item>
          <div class="flex-grow"/>
          <!-- 日夜切换 -->
          <div style="margin-right: 4.5rem;margin-top: -0.2rem">
            <toggle-button @change="changeToggle" size="1" ></toggle-button>
          </div>
          <!-- 搜索按钮 -->
          <div class="search" @click="dialogVisible = true">
            <SvgIcon name="search" width="30" height="30" color="#409EFF" class="icon"/>
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
        </el-menu>
        <!-- 移动端 -->
        <div class="move_nav" style="margin-left: 1.5rem">
          <div>
            <div @click="drawer = true">
              <SvgIcon name="directory_icon" width="30" height="30" color="#409EFF" class="icon"/>
            </div>
            <!-- 移动端日夜切换 -->
            <div style="margin-left: 5rem">
              <toggle-button @change="changeToggle" size="1" ></toggle-button>
            </div>
          </div>

          <!-- 搜索按钮 -->
          <div class="right_nav">
            <div class="search" @click="dialogVisible = true" style="margin-right: 2rem">
              <SvgIcon name="search" width="30" height="30" color="#409EFF" class="icon"/>
            </div>
            <div class="user-info">
              <div v-if="userStore.userInfo == undefined">
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
                <el-tooltip
                    class="box-item"
                    effect="light"
                    placement="right"
                >
                  <template #content>
                    <div class="profile">
                      <div style="font-size: 15px;font-weight: bold;color: black">{{
                          userStore.userInfo?.username
                        }}
                      </div>
                      <div style="font-size: 14px;color: #363636;margin-top: 3px"
                           v-if="userStore.userInfo?.registerType === 0">{{ userStore.userInfo?.email }}
                      </div>
                      <div style="font-size: 14px;color: #363636;margin-top: 3px" v-else>
                        {{ userStore.userInfo?.registerType === 1 ? 'Gitee登录' : 'Github登录' }}
                      </div>
                    </div>
                  </template>
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
                </el-tooltip>
              </div>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </div>
  <div>
    <el-drawer v-model="drawer" :with-header="true" size="40%" direction="ltr" :show-close="false">
      <template #header>
        <span style="font-size: 1.2rem">导航</span>
        <el-button :icon="Close" style="background: none;font-size: 1.5rem;width: 30px;border: none"
                   @click="drawer = false"/>
      </template>
      <template #default>
        <MoveMenu @update:closeDrawer="drawer = false"/>
      </template>
    </el-drawer>
  </div>
</template>

<style lang="scss" scoped>

.move_nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 45px;
  box-sizing: border-box;
  position: fixed;
  top: 0;
  z-index: 999;
  width: 100vw;
  @media screen and (min-width: 910px) {
    display: none;
  }

  .right_nav {
    display: flex;
  }
}

.content-header {
  background-color: rgba(255, 255, 255, 0.4);
  backdrop-filter: blur(6px);
  display: flex;
  align-items: center;
  height: 45px;
  box-sizing: border-box;
  position: fixed;
  top: 0;
  z-index: 999;
  width: 100vw;

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

  .menu {
    font-weight: bold;
    background-color: rgba(248, 249, 250, 0);
    height: 45px;
    @media screen and (max-width: 910px) {
      display: none;
    }
  }

  .el-menu-item.is-active {
    background: rgba(248, 249, 250, 0.2);
  }

  // 更改element-ui默认样式
  .menu :deep(.el-menu-item:hover)  {
    //background-color: rgba(248, 249, 250, 0.2);
    color: #409EFF;
  }

  .logo {
    height: 32px;
    margin-left: 10px;
  }

  .user-info {
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .el-avatar:hover {
    cursor: pointer;
  }

  .profile {
    text-align: right;
    margin-right: 2rem;

    :first-child {
      font-size: 18px;
      font-weight: bold;
      line-height: 20px;
    }

    :last-child {
      font-size: 10px;
      color: grey;
    }
  }

  .flex-grow {
    flex-grow: 1;
  }
}

// 组件穿透更改elementui-plue菜单的鼠标悬浮样式
:deep(.el-menu-item:hover) {
  background-color: rgba(222, 222, 64, 0.2);
}

// 头部缩回去的动画
.content-header-enter-active, .content-header-leave-active {
  transition: all .8s;
}

.content-header-enter, .content-header-leave-to {
  transform: translateY(-100%);
}

// 修改 el-dialog 内容区的默认padding
:deep(.el-dialog__body) {
  padding-top: 0;
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

:deep(.el-drawer__header) {
  border-bottom: solid 1px var(--el-border-color);
  padding: 0.8rem;
  margin-bottom: 0;
}

</style>
