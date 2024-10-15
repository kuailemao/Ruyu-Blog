<script setup lang="ts">
import { ref } from 'vue'
import { message } from 'ant-design-vue'
import dayjs from 'dayjs'
import Banners from './banners/index.vue'
import { updateWebInfo } from '~/api/blog/webInfo'

const props = defineProps({
  info: {
    type: Object,
  },
})

const emit = defineEmits(['reset:web:info'])

interface WebsiteInfoType {
  websiteName: string
  headerNotification: string
  sidebarAnnouncement: string
  recordInfo: string
  startTime: string
  lastUpdateTime: string
  articleCount: number
  wordCount: number
  visitCount: number
  runTime: string
}

const formData: Partial<WebsiteInfoType> = reactive(props.info as object)
const runTime = ref(formData.runTime)

// 每秒
setInterval(() => {
  const formattedString = dayjs(formData.startTime).format('YYYY-MM-DD HH:mm:ss') // 将Dayjs对象转换为指定格式的字符串
  runTime.value = getRunTime(formattedString as string)
}, 1000)

// 获取运行时长
function getRunTime(startTime: string) {
  // 将时间字符串转换为 Date 对象
  const date = new Date(startTime)
  // 获取当前时间
  const now = new Date()
  // 计算时间差（以毫秒为单位）
  const timeDiff = date.getTime() - now.getTime()
  // 将时间差转换为天数、小时数、分钟数和秒数
  const days = Math.floor(timeDiff / (1000 * 60 * 60 * 24))
  const hours = Math.floor((timeDiff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60))
  const minutes = Math.floor((timeDiff % (1000 * 60 * 60)) / (1000 * 60))
  const seconds = Math.floor((timeDiff % (1000 * 60)) / 1000)

  return `${days + 1} 天 ${hours + 1} 小时 ${minutes} 分钟 ${seconds} 秒`.replace(/-/g, '')
}

function resetWebsiteInfo() {
  emit('reset:web:info')
}

function updateWebsiteInfo() {
  console.log()
  const startTime = dayjs(formData.startTime).format('YYYY-MM-DD HH:mm:ss')
  const form = Object.assign({}, formData)
  form.startTime = startTime

  updateWebInfo(form).then((res) => {
    if (res.code === 200)
      message.success('保存成功')
  })
}
</script>

<template>
  <div class="info">
    <a-form>
      <a-form-item label="网站名称">
        <a-input v-model:value="formData.websiteName" />
      </a-form-item>
      <a-form-item label="头部通知">
        <a-input v-model:value="formData.headerNotification" />
      </a-form-item>
      <a-form-item label="侧面公告">
        <a-textarea v-model:value="formData.sidebarAnnouncement" show-count :maxlength="1000" />
      </a-form-item>
      <a-divider style="margin-top: -1rem">
        网站资讯
      </a-divider>
      <a-form-item label="运行时间">
        <a-date-picker v-model:value="formData.startTime" show-time placeholder="运行时间" />
      </a-form-item>
      <div style="display: flex;justify-content:space-between">
        <a-form-item label="运行时长">
          <a-input v-model:value="runTime" disabled style="width: 12.2rem" />
        </a-form-item>
        <a-form-item label="最后更新" style="margin-left: 1rem">
          <a-input v-model:value="formData.lastUpdateTime" disabled />
        </a-form-item>
      </div>
      <div style="display: flex">
        <a-form-item label="文章数目">
          <a-input v-model:value="formData.articleCount" disabled />
        </a-form-item>
        <a-form-item label="文章总字数" style="margin-left: 1rem">
          <a-input v-model:value="formData.wordCount" disabled />
        </a-form-item>
        <a-form-item label="访问次数" style="margin-left: 1rem">
          <a-input v-model:value="formData.visitCount" disabled />
        </a-form-item>
      </div>
      <a-form-item label="备案信息">
        <a-input v-model:value="formData.recordInfo" />
      </a-form-item>
      <div style="display: flex;justify-content: center">
        <a-button type="primary" style="margin-right: 1rem" @click="updateWebsiteInfo">
          保存
        </a-button>
        <a-button type="default" @click="resetWebsiteInfo">
          重置
        </a-button>
      </div>
    </a-form>
    <Banners/>
  </div>
</template>

<style scoped lang="scss">
.info{
  width: 50%;
}

:deep(.ant-upload-list-picture-card){
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  align-items: center;
}
</style>
