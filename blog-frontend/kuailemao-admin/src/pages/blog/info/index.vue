<script setup lang="ts">
import { reactive, ref } from 'vue'
import dayjs from 'dayjs'
import StationmasterInfo from './stationmaster-info/index.vue'
import WebInfo from './web-info/index.vue'
import { websiteInfo } from '~/api/blog/webInfo'

const data = reactive(['站长信息', '网站信息'])
const value = ref(data[0])
const info = ref()

websiteInfo().then((res) => {
  if (res.code === 200) {
    info.value = res.data
    res.data.startTime = dayjs(res.data.startTime)
  }
  else {
    info.value = {}
  }
})

function resetStationmasterInfo() {
  info.value.webmasterName = undefined
  info.value.webmasterCopy = undefined
  info.value.githubLink = undefined
  info.value.giteeLink = undefined
}

function resetWebInfo() {
  info.value.websiteName = undefined
  info.value.headerNotification = undefined
  info.value.sidebarAnnouncement = undefined
  info.value.startTime = undefined
  info.value.recordInfo = undefined
}

// 是否黑夜模式
const isDark = ref(false)
isDark.value = useDark().value
</script>

<template>
  <div>
    <page-container>
      <template #content>
        <!-- 个人信息 -->
        <h2>信息管理</h2>
      </template>
      <template #default>
        <div :style="{ background: isDark ? 'none' : 'white' }">
          <div style="margin: 1rem">
            <a-segmented v-model:value="value" :options="data" />
          </div>
          <div>
            <div v-show="value === '站长信息'" class="info_container">
              <template v-if="info">
                <StationmasterInfo :info="info" @reset:stationmaster:info="resetStationmasterInfo" />
              </template>
            </div>
            <div v-show="value === '网站信息'" class="info_container">
              <template v-if="info">
                <WebInfo :info="info" @reset:web:info="resetWebInfo" />
              </template>
            </div>
          </div>
        </div>
      </template>
    </page-container>
  </div>
</template>

<style scoped lang="scss">
.info_container{
  margin-bottom: 4rem;
  display: flex;
  justify-content: center;
  }
</style>
