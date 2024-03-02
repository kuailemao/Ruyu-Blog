// 获取服务监控数据
import { message } from 'ant-design-vue'
import type SystemInfo from '~/pages/system/server-monitoring/type.ts'

export async function getServiceMonitorData() {
  return useGet<SystemInfo>('/monitor/server', null).catch(msg => message.warn(msg))
}
