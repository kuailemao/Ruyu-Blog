<script setup lang="ts">
import vueDanmaku from 'vue3-danmaku'
import {addTreeHole, getTreeHoleList} from "@/apis/treeHole";
import {ElMessage} from "element-plus";

const treeHoleList = ref([])
// 是否显示提交按钮
const isShowSubmit = ref(false)
const content = ref('')

onMounted(() => {
  getTreeHole()
})

function addTreeHoleBtn() {

  if (content.value === '') {
    ElMessage.warning('请输入内容')
    return
  }

  addTreeHole(content.value).then(res => {

    if (res.code === 200) {
      ElMessage.success('添加成功')
      getTreeHole()
      content.value = ''
    } else {
      ElMessage.error(res.msg)
    }
  })
}

function getTreeHole() {
  getTreeHoleList().then(res => {
    if (res.code === 200) {
      treeHoleList.value = res.data
    }
  })
}
</script>
<template>
  <div class="container">
    <div class="content_container">
      <div>树洞</div>
      <div>
        <input v-model="content" @focus="isShowSubmit = true" type="text" placeholder="在这里留下自己的足迹吧...">
        <button v-show="isShowSubmit" @click="addTreeHoleBtn"><span style="color: grey;font-style: italic;font-weight: bold">提交</span></button>
      </div>
    </div>
    <vue-danmaku :debounce="3000"
                 random-channel
                 :speeds="80"
                 :channels="5"
                 is-suspend
                 v-model:danmus="treeHoleList"
                 use-slot loop
                 style="height:100vh; width:100vw;">
      <template v-slot:dm="{ danmu }">
        <div class="barrage_container">
          <div>
            <el-avatar :src="danmu.avatar"/>
          </div>
          <div><span>{{ danmu.nickname }}：</span><span>{{ danmu.content }}</span></div>
        </div>
      </template>
    </vue-danmaku>
  </div>
</template>

<style scoped lang="scss">
.container {
  background-image: url('https://image.kuailemao.xyz/blog/TreeHole/TreeHole-back_compressed.webp');
  background-size: cover;
  background-position: center;
  min-width: 100vw;
  height: 100vh;

  // 内容
  .content_container {
    position: absolute;
    top: 40%;
    left: 50%;
    z-index: 2;
    transform: translate(-50%, -50%);


    & div:first-child {
      color: white;
      font-size: 2rem;
      font-weight: bold;
      text-shadow: 0 0 10px #000;
      text-align: center;
      text-transform: uppercase;
      letter-spacing: 0.2rem;
    }

    & div:last-child {
      display: flex;
      justify-content: center;
      align-items: center;
      margin-top: 1rem;

      & input {
        width: 16rem;
        height: 2rem;
        border: #409EFF solid 1px;
        border-radius: 1rem;
        outline: none;
        padding: 0 1rem;
        font-size: 1rem;
        background-color: rgba(255, 255, 255, 0.2)
      }

      // 改变placeholder样式
      & input::placeholder {
        color: white;
        font-style: italic;
      }

      & button {
        width: 5rem;
        height: 2rem;
        border-radius: 1rem;
        outline: none;
        margin-left: 0.5rem;
        background-color: rgba(255, 255, 255, 0.2);
        border: #409EFF solid 1px;
        color: white;
        font-size: 1rem;

        &:hover {
          background-color: rgba(255, 255, 255, 0.5);
          cursor: pointer;
        }
      }
    }
  }

  // 弹幕
  .barrage_container {
    display: flex;
    align-items: center;
    position: relative;

    // 下边框动画
    &::after {
      content: '';
      position: absolute;
      left: 0;
      bottom: 0;
      width: 0;
      height: 0.2em;
      border-radius: 0.1em;
      // 蓝紫色渐变色背景
      background: linear-gradient(to right, #00c6ff, #0072ff);
      transition: width 0.3s ease; /* 过渡动画效果 */
    }

    &:hover::after {
      width: 100%;
    }

    & div:last-child span:first-child {
      margin-left: 0.5rem;
      color: white;
      font-weight: bold;
    }

    & div:last-child span:last-child {
      font-size: 1.2rem;
    }

    & div:last-child {
      // 悬浮动态移动下边框
      border-bottom: 1px solid #ebebeb;
      padding: 0.5rem;
      margin-left: 0.5rem;
      border-radius: $border-radius;
      background-color: rgba(255, 255, 255, 0.2);

    }
  }

}
</style>