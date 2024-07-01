<script setup>
import { defineComponent, onMounted, watch, ref, reactive, onBeforeUnmount, h } from "vue";

import { music } from "@/store/modules/music";
import { reqToplist, reqTopDetaliList } from "@/apis/music/index";
import { PLAYTYPE } from "../musicTool";
import { storeToRefs } from "pinia";
import { ElNotification } from "element-plus";

const { getCustomerMusicList } = storeToRefs(music());

const topList = ref([]);

const currentMusicList = ref([]); // 当前音乐播放列表

const currentTop = ref(null);

const scrollLoading = ref(false);

let observe, box;

defineComponent({
  name: "MusicList",
});

const params = reactive({
  limit: 30,
  offset: 0,
  id: "",
  loading: false,
  loadMore: true,
});

const musicListLoading = ref(false);

//  获取音乐排行榜
const reqMusicList = async () => {
  musicListLoading.value = true;
  const res = await reqToplist();
  if (res.code == 200) {
    topList.value = res.list;
    musicListLoading.value = false;
    currentTop.value = topList.value[3];
    await reqTopMusicList(topList.value[3].id);
  }
};

// 格局排行榜id 查询排行榜音乐
const reqTopMusicList = async (id) => {
  if (id) {
    params.id = id;
  }
  if (!params.loadMore) {
    return;
  }
  try {
    if (params.offset == 0) {
      params.loading = true;
    } else {
      scrollLoading.value = true;
    }
    const res = await reqTopDetaliList(params);
    if (res.code == 200) {
      getFlagToMusicList(res.songs || []);
      if (!res.songs.length) {
        params.loadMore = false;
      }
      currentMusicList.value =
        params.offset == 0 ? res.songs : currentMusicList.value.concat(res.songs);
      music().setMusicList(currentMusicList.value);
      observeBox();
    }
  } finally {
    params.loading = false;
    scrollLoading.value = false;
  }
};

// 无限加载
const observeBox = () => {
  // 获取要监听的元素
  box = document.querySelector(".observe");

  observe = new IntersectionObserver(
    (entries) => {
      entries.forEach(async (e) => {
        if (e.isIntersecting && e.intersectionRatio > 0) {
          if (!params.loading) {
            loadMore();
          }
        }
      });
    },
    { threshold: [1] }
  );
  box && observe.observe(box);
};

const playMusic = (item) => {
  // 设置当前音乐信息
  music().setMusicInfo(item.id);
  music().setPlayType(PLAYTYPE.TOP);
};

// 切换排行榜置空数据
const clickTopMusicList = (item) => {
  currentTop.value = item;
  params.id = item.id;
  params.offset = 0;
  params.loadMore = true;
  currentMusicList.value = [];
  music().setMusicList([]);
  reqTopMusicList();
};

// 添加歌曲
const customerAddMusic = (item) => {
  if (isActive(item.id)) return;
  music().setCustomerMusicList("add", item);
  music().setPlayType(PLAYTYPE.CUSTOM);
  ElNotification({
    offset: 60,
    title: "提示",
    duration: 1000,
    message: h("div", { style: "color: #7ec050; font-weight: 600;" }, "添加成功"),
  });
};
// 判断当前歌曲是否在用户定制列表中
const isActive = (id) => {
  if (!getCustomerMusicList.value.length) {
    return false;
  }
  let index = getCustomerMusicList.value.findIndex((item) => item.id == id);
  if (index == -1) {
    return false;
  } else {
    return true;
  }
};
// 判断歌曲是否被添加了
const getFlagToMusicList = (songs) => {
  Array.isArray(songs) &&
    songs.forEach((song) => {
      song.active = isActive(song.id);
    });
};

const loadMore = () => {
  params.offset = params.offset + params.limit;
  reqTopMusicList();
};

watch(
  () => getCustomerMusicList.value.length,
  () => {
    getFlagToMusicList(currentMusicList.value);
  }
);

onMounted(async () => {
  await reqMusicList();
});

onBeforeUnmount(() => {
  observe && observe.unobserve(box);
  observe = null;
});
</script>

<template>
  <div class="music-list">
    <div class="!max-w-[1024px] !w-[100%] flex md:justify-between justify-center items-start">
      <div class="music-list__left">
        <div class="header">分类歌单</div>
        <el-row v-loading="musicListLoading" class="body">
          <el-col
            class="flex justify-center items-center overflow-auto"
            :span="6"
            v-for="item in topList"
            :key="item.id"
          >
            <div class="top" @click="clickTopMusicList(item)">
              <img class="top-bg" :src="item.coverImgUrl" />
              <i class="iconfont icon-zanting play"></i>
            </div>
          </el-col>
        </el-row>
      </div>
      <div class="music-list__right">
        <div class="!w-[100%] flex items-center">
          <span v-if="currentTop" class="top-name text-overflow" :title="currentTop.name">{{
            currentTop.name
          }}</span>
          <el-popover
            ref="elPopoverRef"
            placement="bottom"
            :width="330"
            :show-arrow="false"
            :teleported="false"
            trigger="click"
            @touchmove.stop.prevent
          >
            <template #reference>
              <span class="iconfont icon-nav-search scale"></span>
            </template>
            <SearchList />
          </el-popover>
        </div>

        <el-row style="width: 100%">
          <el-col :span="24" class="header">
            <div class="title title1">歌曲</div>
            <div class="title title2">作者</div>
            <div class="title title3">其他</div>
          </el-col>
        </el-row>
        <el-row v-loading="params.loading" class="body">
          <el-col
            class=" justify-start items-center overflow-auto"
            :span="24"
            v-for="item in currentMusicList"
            :key="item.id"
          >
            <div class="name" @click="playMusic(item)">
              <span class="text-overflow" :title="item.name">{{ item.name }}</span>
            </div>
            <div class="author">
              <span class="text-overflow" :title="item.ar[0].name">{{ item.ar[0].name }}</span>
            </div>
            <div class="other">
              <span class="text-overflow" :title="item.alia[0]">{{ item.alia[0] }}</span>
            </div>
            <div class="add-music">
              <i
                :class="[
                  'iconfont',
                  'icon-tianjiadao',
                  'change-color',
                  item.active ? 'active' : '',
                ]"
                @click="customerAddMusic(item)"
              ></i>
            </div>
          </el-col>
          <div class="observe" @click="loadMore">
            <template v-if="!params.loading">
              <Loading :size="24" v-if="scrollLoading" />
              <template v-else>
                {{ params.loadMore ? "下拉/点击加载更多～" : "已经到底了" }}
              </template>
            </template>
          </div>
        </el-row>
      </div>
    </div>
    <!-- 歌词面板 -->
    <LyricBoard />
  </div>
</template>

<style lang="scss" scoped>
.music-list {
  box-sizing: border-box;
  display: flex;
  justify-content: center;
  width: 100%;
  .top-name {
    display: inline-block;
    font-size: 1.2rem;
    font-weight: 600;
    padding-left: 5px;
    max-width: 40%;
  }

  &__left {
    width: 50%;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
    overflow: hidden;
    padding-bottom: 10px;
    .header {
      padding-left: 20px;
      font-weight: 600;
      font-size: 1.2rem;
    }
    .body {
      height: 100%;
      overflow: auto;
    }
  }

  &__right {
    position: relative;
    width: 50%;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
    overflow: hidden;
    padding-bottom: 10px;
    .header {
      width: 100%;
      display: flex;
      .title {
        font-weight: 600;
        font-size: 1.1rem;
        &1 {
          width: 30%;
          overflow: hidden;
        }
        &2 {
          width: 25%;
          overflow: hidden;
        }
        &3 {
          overflow: hidden;
          width: 25%;
        }
      }
    }
    .body {
      height: 100%;
      overflow: auto;
    }
  }
  .top {
    position: relative;
    width: 80px;
    height: 80px;
    &-bg {
      width: 80px;
      height: 80px;
    }
    &:hover {
      .play {
        visibility: visible;
      }
    }

    .play {
      z-index: 1;
      position: absolute;
      top: 0;
      left: 0;
      bottom: 0;
      right: 0;
      background: rgba(0, 0, 0, 0.3);
      transition: all 0.1s;
      display: flex;
      justify-content: center;
      align-items: center;
      visibility: hidden;
    }

    .icon-zanting {
      font-size: 2rem;
      color: var(--global-white);
    }
  }

  .name {
    width: 30%;
    cursor: pointer;

    &:hover {
      color: var(--music-main-active);
    }
  }

  .author {
    width: 25%;
  }

  .other {
    width: 25%;
  }

  .add-music {
    text-align: center;
    width: 20%;
    &:hover {
      color: var(--music-main-active);
    }
  }

  .active {
    color: var(--music-main-active);
  }

  .text-overflow {
    display: inline-block;
    width: 90%;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .observe {
    display: flex;
    justify-content: center;
    align-items: flex-start;
    width: 100%;
    color: var(--primary);
    cursor: pointer;
  }

  .icon-nav-search {
    font-size: 1.6rem;
  }
}
// mobile
@media screen and (max-width: 768px) {
  .music-list__left {
    display: none;
  }

  .music-list__right {
    position: relative;
    width: 400px;
  }

  .search-down {
    top: 35px !important;
    left: 75% !important;
  }
}

:deep(.el-col-24, .el-col-24.is-guttered){
  display: flex;
}
</style>
