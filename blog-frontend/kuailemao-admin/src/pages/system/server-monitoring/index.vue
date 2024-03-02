<script setup lang="ts">
import type { Ref } from 'vue'
import { onMounted, ref } from 'vue'
import type SystemInfo from './type.ts'
import { getServiceMonitorData } from '~/api/server'

const serverInfo: Ref<SystemInfo> = ref()
const loading = ref(true)

onMounted(async () => {
  const { data } = await getServiceMonitorData()
  serverInfo.value = data
  loading.value = false
})
</script>

<template>
  <page-container>
    <template #default>
      <div class="cpuSty">
        <a-card :bordered="false" style="width: 48%" :loading="loading">
          <template #title>
            <DashboardOutlined />
            CPU
          </template>

          <table class="tab">
            <thead>
              <tr>
                <th style="width: 50%">
                  属性
                </th>
                <th style="width: 50%">
                  值
                </th>
              </tr>
            </thead>
            <template v-if="serverInfo">
              <tbody>
                <tr>
                  <td><div>核心数</div></td>
                  <td><div>{{ serverInfo.cpu.cpuNum }}</div></td>
                </tr>
                <tr>
                  <td><div>用户使用率</div></td>
                  <td><div>{{ serverInfo.cpu.used }}%</div></td>
                </tr>
                <tr>
                  <td><div>系统使用率</div></td>
                  <td><div>{{ serverInfo.cpu.sys }}%</div></td>
                </tr>
                <tr>
                  <td><div>当前空闲率</div></td>
                  <td><div>{{ serverInfo.cpu.free }}</div></td>
                </tr>
              </tbody>
            </template>
          </table>
        </a-card>
        <a-card :bordered="false" style="width: 48%" :loading="loading">
          <template #title>
            <ProfileOutlined />
            内存
          </template>
          <table class="tab">
            <thead>
              <tr>
                <th style="width: 33%">
                  属性
                </th>
                <th style="width: 33%">
                  内存
                </th>
                <th style="width: 33%">
                  JVM
                </th>
              </tr>
            </thead>
            <template v-if="serverInfo">
              <tbody>
                <tr>
                  <td><div>总内存</div></td>
                  <td><div>{{ serverInfo.mem.total }}G</div></td>
                  <td><div>{{ serverInfo.jvm.total }}M</div></td>
                </tr>
                <tr>
                  <td><div>已用内存</div></td>
                  <td><div>{{ serverInfo.mem.used }}G</div></td>
                  <td><div>{{ serverInfo.jvm.total - serverInfo.jvm.free }}M</div></td>
                </tr>
                <tr>
                  <td><div>剩余内存</div></td>
                  <td><div>{{ serverInfo.mem.free }}G</div></td>
                  <td><div>{{ serverInfo.jvm.free }}M</div></td>
                </tr>
                <tr>
                  <td><div>使用率</div></td>
                  <td><div>{{ ((serverInfo.mem.used / serverInfo.mem.total) * 100).toFixed(2) }}%</div></td>
                  <td><div>{{ (((serverInfo.jvm.total - serverInfo.jvm.free) / serverInfo.jvm.total) * 100).toFixed(2) }}%</div></td>
                </tr>
              </tbody>
            </template>
          </table>
        </a-card>
      </div>
      <div style="margin-top: 1rem" class="cpuSty">
        <a-card :bordered="false" style="width: 98%" :loading="loading">
          <template #title>
            <DesktopOutlined />
            服务器信息
          </template>
          <table class="tab">
            <template v-if="serverInfo">
              <tbody>
                <tr style="border: none">
                  <td><div>服务器名称</div></td>
                  <td><div>{{ serverInfo.sys.computerName }}</div></td>
                  <td><div>操作系统</div></td>
                  <td><div>{{ serverInfo.sys.osName }}</div></td>
                </tr>
                <tr>
                  <td><div>服务器IP</div></td>
                  <td><div>{{ serverInfo.sys.computerIp }}</div></td>
                  <td><div>系统架构</div></td>
                  <td><div>{{ serverInfo.sys.osArch }}</div></td>
                </tr>
              </tbody>
            </template>
          </table>
        </a-card>
      </div>
      <div style="margin-top: 1rem" class="cpuSty">
        <a-card :bordered="false" style="width: 98%" :loading="loading">
          <template #title>
            <CoffeeOutlined />
            Java虚拟机信息
          </template>
          <table class="tab">
            <template v-if="serverInfo">
              <tbody>
                <tr style="border: none">
                  <td><div>Java名称</div></td>
                  <td style="width: 40%">
                    <div>{{ serverInfo.jvm.name }}</div>
                  </td>
                  <td><div>Java版本</div></td>
                  <td><div>{{ serverInfo.jvm.version }}</div></td>
                </tr>
                <tr>
                  <td><div>启动时间</div></td>
                  <td><div>{{ serverInfo.jvm.startTime }}</div></td>
                  <td><div>运行时长</div></td>
                  <td><div>{{ serverInfo.jvm.runTime }}</div></td>
                </tr>
                <tr>
                  <td><div>安装路径</div></td>
                  <td colspan="3">
                    <div>{{ serverInfo.jvm.home }}</div>
                  </td>
                </tr>
                <tr>
                  <td><div>项目路径</div></td>
                  <td colspan="3">
                    <div>{{ serverInfo.sys.userDir }}</div>
                  </td>
                </tr>
                <tr>
                  <td style="width: 20%">
                    <div>运行参数</div>
                  </td>
                  <td colspan="3">
                    <div>{{ serverInfo.jvm.inputArgs }}</div>
                  </td>
                </tr>
              </tbody>
            </template>
          </table>
        </a-card>
      </div>
      <div style="margin-top: 1rem" class="cpuSty">
        <a-card :bordered="false" style="width: 98%" :loading="loading">
          <template #title>
            <InboxOutlined />
            磁盘状态
          </template>
          <table class="tab">
            <thead>
              <tr>
                <th>
                  盘符路径
                </th>
                <th>
                  文件系统
                </th>
                <th>
                  盘符类型
                </th>
                <th>
                  总大小
                </th>
                <th>
                  可用大小
                </th>
                <th>
                  已用大小
                </th>
                <th>
                  已用百分比
                </th>
              </tr>
            </thead>
            <template v-if="serverInfo">
              <tbody>
                <template v-for="sysFile in serverInfo.sysFiles" :key="sysFile.dirName">
                  <tr>
                    <td><div>{{ sysFile.dirName }}</div></td>
                    <td><div>{{ sysFile.sysTypeName }}</div></td>
                    <td><div>{{ sysFile.typeName }}</div></td>
                    <td><div>{{ sysFile.total }}</div></td>
                    <td><div>{{ sysFile.free }}</div></td>
                    <td><div>{{ sysFile.used }}</div></td>
                    <td><div>{{ sysFile.usage }}%</div></td>
                  </tr>
                </template>
              </tbody>
            </template>
          </table>
        </a-card>
      </div>
    </template>
  </page-container>
</template>

<style scoped lang="scss">
.cpuSty{
  display: flex;
  justify-content: space-around;

  .tab{
    width: 100%;
    text-align: left;
    font-size: 1rem;

    thead{
      tr{
        color: #909399;
        height: 3rem;
      }
    }
    tbody{
      tr{
        color: #606266;
        height:3em;
        border-top: 1px solid #e8e8e8;
      }
    }
  }
}

:deep(.ant-card-body){
  padding: 1rem 24px;
}
</style>
