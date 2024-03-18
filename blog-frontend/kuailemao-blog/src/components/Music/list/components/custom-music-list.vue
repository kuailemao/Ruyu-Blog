<script setup>
import { defineComponent, h, watch } from "vue";

import { music } from "@/store/modules/music";
import { PLAYTYPE } from "../../musicTool";
import { storeToRefs } from "pinia";
import { ElNotification } from "element-plus";

const { getCustomerMusicList } = storeToRefs(music());

defineComponent({
  name: "CustomMusicList",
});

const playMusic = (item) => {
  // 设置当前播放音乐
  music().setMusicInfo(item.id);
  // 设置播放音乐的详细描述
  music().setPlayType(PLAYTYPE.CUSTOM);
};

const customerDeleteMusic = (item) => {
  music().setCustomerMusicList("delete", item);
  music().setPlayType(PLAYTYPE.CUSTOM);

  ElNotification({
    offset: 60,
    title: "提示",
    duration: 1000,
    message: h("div", { style: "color: #7ec050; font-weight: 600;" }, "删除成功"),
  });
};
watch(
  () => getCustomerMusicList.value.length,
  () => {
    if (!getCustomerMusicList.value.length) {
      music().setPlayType("TOP");
    }
  }
);
</script>

<template>
  <div class="music-list">
    <div class="flex justify-between items-start">
      <div class="!py-[10px] music-list__detail">
        <el-row>
          <el-col :span="24" class="header">
            <div class="title title1">歌曲</div>
            <div class="title title2">作者</div>
            <div class="title title3">其他</div>
          </el-col>
        </el-row>
        <el-row class="body">
          <div style="width: 100%" v-if="getCustomerMusicList.length">
            <el-col
              class="flex justify-start items-center overflow-auto"
              :span="24"
              v-for="item in getCustomerMusicList"
              :key="item.id"
            >
              <div class="name" @click="playMusic(item)">
                <span class="text-overflow" :title="item.name">{{ item.name }}</span>
              </div>
              <div class="author">
                <span class="text-overflow" :title="item.ar[0].name">{{
                  item.hasOwnProperty("ar") ? item.ar[0].name : ""
                }}</span>
              </div>
              <div class="other">
                <span class="text-overflow" :title="item.alia[0]">{{
                  item.hasOwnProperty("alia") ? item.alia[0] : ""
                }}</span>
              </div>
              <div class="delete-music">
                <svg-icon name="delete" width="1rem" @click="customerDeleteMusic(item)"></svg-icon>
              </div>
            </el-col>
          </div>

          <div v-else class="empty">空空如也</div>
        </el-row>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.music-list {
  box-sizing: border-box;
  border-radius: 8px;
  padding: 5px;

  &__detail {
    position: relative;
    overflow: auto;
    width: 100%;
    .header {
      width: 100%;
      display: flex;
      .title {
        font-weight: 600;
        font-size: 1.2rem;
        &1 {
          width: 30%;
        }
        &2 {
          width: 25%;
        }
        &3 {
          width: 25%;
        }
      }
    }

    .body {
      max-height: 300px;
      overflow: auto;
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

  .delete-music {
    &:hover {
      transform: scale(1.1);
    }
  }

  .text-overflow {
    font-size: 1rem;
    display: inline-block;
    width: 100%;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .empty {
    width: 80%;
    height: 25px;
    font-size: 0.8rem;
    display: flex;
    justify-content: center;
    align-items: flex-end;
  }
}
</style>
