<script setup>
import { defineComponent, h, ref, reactive } from "vue";

import { music } from "@/store/modules/music";
import { PLAYTYPE } from "../../musicTool";
import { ElNotification } from "element-plus";
import { reqSearch, reqSearchSingerHot } from "@/apis/music/index";
import { storeToRefs } from "pinia";

defineComponent({
  name: "CustomMusicList",
});

const { getCustomerMusicList } = storeToRefs(music());
const keyWords = ref(""); // 搜索关键词
const searchList = ref([]); // 搜索列表
const singer = ref("");
const keyWordsSongs = ref([]); //
const params = reactive({
  limit: 20,
  offset: 0,
  id: "",
  loadMore: true,
  loading: false,
});

const playMusic = (item) => {
  // 设置当前播放音乐
  music().setMusicInfo(item.id, false);
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
// 添加歌曲
const customerAddMusic = (item) => {
  if (isActive(item.id)) return;
  music().setCustomerMusicList("add", item);
  music().setPlayType(PLAYTYPE.CUSTOM);

  searchList.value.forEach((song) => {
    song.active = isActive(song.id);
  });
  keyWordsSongs.value.forEach((song) => {
    song.active = isActive(song.id);
  });
  ElNotification({
    offset: 60,
    title: "提示",
    duration: 1000,
    message: h("div", { style: "color: #7ec050; font-weight: 600;" }, "添加成功"),
  });
};

const returnAuthors = (arr, attr) => {
  let resArr = arr.map((v) => {
    return v[attr];
  });

  return resArr.join(",");
};

// 搜索歌曲
const search = async () => {
  if (!keyWords.value) return;
  const res = await reqSearch(keyWords.value);
  if (res.code == 200) {
    let obj = {
      id: "",
      name: "",
    };
    keyWordsSongs.value = Array.isArray(res.result.songs) ? res.result.songs : [];
    if (!keyWordsSongs.value.length) {
      ElNotification({
        offset: 60,
        title: "提示",
        duration: 1000,
        message: h("div", { style: "color: #7ec050; font-weight: 600;" }, "没有相关的歌曲"),
      });
      return;
    }
    keyWordsSongs.value.forEach((song) => {
      song.active = isActive(song.id);
    });
    singer.value = Object.assign(obj, { id: res.result.songs[0].artists[0].id });
    document.querySelector(".search-music-list__detail").scrollTo({
      top: 0,
      behavior: "smooth",
    });
    searchSingerSongs("init");
  }
};

const searchSingerSongs = async (type) => {
  if (!params.loadMore || !singer.value) return;
  params.loading = true;
  params.id = singer.value.id;
  if (type == "init") {
    params.offset = 0;
  } else {
    params.offset = params.limit + params.offset;
  }
  const res = await reqSearchSingerHot(params);
  if (res.code == 200) {
    let list = Array.isArray(res.songs) ? res.songs : [];
    if (!list.length) {
      ElNotification({
        offset: 60,
        title: "提示",
        duration: 1000,
        message: h("div", { style: "color: #7ec050; font-weight: 600;" }, "没有相关的歌曲"),
      });
      return;
    }
    if (!list.length) {
      params.loadMore = false;
    }
    list.forEach((song) => {
      song.active = isActive(song.id);
    });
    searchList.value = params.offset == 0 ? list : searchList.value.concat(list);
    params.loading = false;
  }
};
</script>

<template>
  <div class="search-music-list">
    <div class="search">
      <el-input
        style="width: 180px"
        v-model="keyWords"
        placeholder="请输入歌名或歌手"
        @keyup.enter="search"
        clearable
      ></el-input>
      <el-button
        style="background-color: #62c28a; margin-left: 10px; color: #676767"
        @click="search"
        >搜索</el-button
      >
    </div>
    <div class="flex justify-center items-start pt-[50px]">
      <div class="search-music-list__detail">
        <el-row v-if="keyWordsSongs.length" class="body">
          <el-col :span="24" class="big-title">相似歌曲</el-col>
          <el-row>
            <el-col :span="24" class="header">
              <div class="title title1">歌曲</div>
              <div class="title title2">作者</div>
            </el-col>
          </el-row>
          <el-col
            class="flex justify-start items-center overflow-auto"
            :span="24"
            v-for="item in keyWordsSongs"
            :key="item.id"
          >
            <div class="name" @click="playMusic(item)">
              <span class="text-overflow" :title="item.name">{{ item.name }}</span>
            </div>
            <div class="author">
              <span class="text-overflow" :title="returnAuthors(item.artists, 'name')">{{
                returnAuthors(item.artists, "name")
              }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row v-if="searchList.length" class="body">
          <el-col :span="24" class="big-title">根据热门歌手推荐歌曲</el-col>
          <el-row>
            <el-col :span="24" class="header">
              <div class="title title1">歌曲</div>
              <div class="title title2">作者</div>
            </el-col>
          </el-row>
          <el-col
            class="flex justify-start items-center overflow-auto"
            :span="24"
            v-for="item in searchList"
            :key="item.id"
          >
            <div class="name" @click="playMusic(item)">
              <span class="text-overflow" :title="item.name">{{ item.name }}</span>
            </div>
            <div class="author">
              <span class="text-overflow" :title="returnAuthors(item.ar, 'name')">{{
                returnAuthors(item.ar, "name")
              }}</span>
            </div>

            <div class="add">
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
          <div class="observe" @click="searchSingerSongs('loadMore')">
            <Loading :size="24" v-if="params.loading" />
            <template v-else>
              {{ params.loadMore ? "点击加载更多～" : "已经到底了" }}
            </template>
          </div>
        </el-row>
        <div v-else class="empty">空空如也</div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.search-music-list {
  position: relative;
  box-sizing: border-box;
  border-radius: 8px;
  width: 320px;
  overflow: hidden;

  .search {
    position: absolute;
    top: 10px;
    left: 13px;
    width: 90%;
    height: 30px;
  }

  &__detail {
    position: relative;
    width: 330px;
    height: 350px;
    overflow-x: hidden;
    overflow-y: scroll;

    .big-title {
      font-weight: 600;
      font-size: 1.2rem;
      color: var(--music-main-active);
    }

    .header {
      width: 330px;
      display: flex;
      .title {
        font-weight: 600;
        font-size: 1rem;
        &1 {
          width: 40%;
        }
        &2 {
          width: 60%;
        }
      }
    }

    .body {
      max-height: 320px;
    }
  }

  .name {
    width: 40%;
    cursor: pointer;

    &:hover {
      color: var(--music-main-active);
    }
  }

  .author {
    width: 50%;
  }

  .add-music {
    text-align: center;
    width: 10%;
    &:hover {
      color: var(--music-main-active);
    }
  }

  .active {
    color: var(--music-main-active);
  }

  .text-overflow {
    font-size: 1rem;
    display: inline-block;
    width: 90%;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .empty {
    width: 80%;
    height: 100%;
    font-size: 0.8rem;
    display: flex;
    justify-content: center;
    align-items: center;
  }
}
.observe {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 30px;
  line-height: 30px;
  color: var(--primary);
  cursor: pointer;
}
</style>
