<script setup lang="ts">

// 接收父组件传递过来的参数
defineProps({
  // 前图标的名字
  prefixIcon: String,
  // 后图标的名字
  suffixIcon: {
    type: String,
    default: '',
  },
  // 是否抖动
  isDithering: {
    type: Boolean,
    default: false,
  },
  // 是否箭头效果
  isArrow: {
    type: Boolean,
    default: false,
  },
  // 是否旋转效果
  isRotate: {
    type: Boolean,
    default: false,
  },
  // 是否放大缩小
  isScale: {
    type: Boolean,
    default: false,
  },
  // 标题
  title: String,
  // 是否是目录
  isCatalog: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['invoke'])

function invoke(){
  emit('invoke')
}
</script>

<template>
  <!-- 封装侧边栏卡片 -->
  <div v-slide-in class="card" :style="isCatalog ? 'position: relative;z-index: 9' : ''">
    <div class="title" :style="isCatalog ? 'position: sticky;top: 0' : ''">
      <div class="title_text">
        <SvgIcon :class="isDithering ? 'dithering' : '' || isScale ? 'scale' : ''" :name="prefixIcon" width="30"
                 height="30"/>
        <span style="margin-left: 10px">{{ title }}</span>
      </div>
      <el-tooltip
          class="box-item"
          effect="light"
          content="刷新"
          placement="top"
          v-if="suffixIcon == 'rotate'"
      >
        <div :class="{ 'arrow' : isArrow , 'rotate' : isRotate}"
             :style="suffixIcon == 'rotate' ? 'cursor: pointer' : ''"
             @click="invoke">
          <SvgIcon :name="suffixIcon" width="30" height="30"/>
        </div>
      </el-tooltip>
      <div v-else :class="{ 'arrow' : isArrow , 'rotate' : isRotate}"
           :style="suffixIcon == 'rotate' ? 'cursor: pointer' : ''"
           @click="invoke">
        <SvgIcon :name="suffixIcon" width="30" height="30"/>
      </div>

    </div>
    <div class="content">
      <slot/>
    </div>
  </div>
</template>

<style scoped lang="scss">
.card {
  background-color: var(--el-bg-color);
  width: $card-width;
  margin: $card-margin;
  border: 1px solid var(--el-border-color);
  border-radius: $border-radius;
  margin-top: 1.5em;
  // 添加阴影
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  overflow: hidden;

  .title {
    border-radius: $border-radius $border-radius 0 0;
    background-color: var(--el-bg-color);
    z-index: 2;
    display: flex;
    padding: 10px;
    justify-content: space-between;
    align-items: center;
    font-size: 20px;
    // 文字跟图标对齐
    .title_text {
      display: flex;
      align-items: center;

      .dithering {
        animation: shake 0.3s infinite;
        transform-origin: center;

        // 喇叭根据中心点抖动效果
        @keyframes shake {
          0% {
            transform: rotate(0deg);
          }
          25% {
            transform: rotate(-10deg);
          }
          50% {
            transform: rotate(0deg);
          }
          75% {
            transform: rotate(10deg);
          }
          100% {
            transform: rotate(0deg);
          }
        }
      }

      // 放大缩小效果
      .scale {
        animation: scale 1s infinite;
        transform-origin: center;

        @keyframes scale {
          0% {
            transform: scale(1);
          }
          50% {
            transform: scale(1.1);
          }
          100% {
            transform: scale(1);
          }
        }
      }

    }

    // 箭头动画
    .arrow:hover {
      animation: move 1s infinite;

      @keyframes move {
        0% {
          transform: translateX(0);
        }
        50% {
          transform: translateX(5px);
        }
        100% {
          transform: translateX(0);
        }
      }

    }

    // 随机旋转动画
    .rotate:hover {
      animation: rotate 1s infinite linear;
    }

    @keyframes rotate {
      // 306度是为了让箭头指向右边
      0% {
        transform: rotate(0deg);
      }
      100% {
        transform: rotate(360deg);
      }
    }

  }

  .content {
    min-height: 5em;
    text-align: center;
    line-height: 22px;
    padding: 10px;
    color: grey;
  }
}
</style>