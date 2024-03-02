<script setup lang="ts">
import { Modal, message } from 'ant-design-vue'
import { createVNode } from 'vue'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'
import { deleteLoginLogByIds, loginLogList, searchLoginLog } from '~/api/log/login'

const formState = reactive({
  userName: undefined,
  address: undefined,
  state: undefined,
  time: undefined,
})

const columns: any = [
  {
    title: '日志编号',
    dataIndex: 'id',
    align: 'center',
  },
  {
    title: '用户名称',
    dataIndex: 'userName',
    align: 'center',
  },
  {
    title: '登录地址',
    dataIndex: 'ip',
    align: 'center',
  },
  {
    title: '登录地点',
    dataIndex: 'address',
    align: 'center',
  },
  {
    title: '浏览器',
    dataIndex: 'browser',
    align: 'center',
  },
  {
    title: '操作系统',
    dataIndex: 'os',
    align: 'center',
  },
  {
    title: '类型',
    dataIndex: 'type',
    align: 'center',
  },
  {
    title: '登录状态',
    dataIndex: 'state',
    align: 'center',
  },
  {
    title: '操作信息',
    dataIndex: 'message',
    align: 'center',
  },
  {
    title: '登录日期',
    dataIndex: 'loginTime',
    align: 'center',
  },
]

type Key = string | number

const loading = ref(false)
const tabData = ref([]) as any

onMounted(() => {
  refreshFunc()
})

async function refreshFunc(searchData?: object) {
  loading.value = true
  let newData: any
  if (searchData) {
    newData = searchData
  }
  else {
    const { data } = await loginLogList()
    newData = data
  }
  newData.map((item: any) => {
    return item.status = item.status === 0
  })
  tabData.value = newData
  loading.value = false
}

async function onFinish(values: any) {
  // 转换时间
  const submitData = {
    ...values,
  }
  if (values.time && values.time[0]) {
    Object.assign(submitData, {
      loginTimeStart: values.time[0].format('YYYY-MM-DD HH:mm:ss'),
    })
  }
  if (values.time && values.time[1]) {
    Object.assign(submitData, {
      loginTimeEnd: values.time[1].format('YYYY-MM-DD HH:mm:ss'),
    })
  }
  loading.value = true
  const { data } = await searchLoginLog(submitData)
  await refreshFunc(data)
}

const state = reactive<{
  selectedRowKeys: Key[]
  loading: boolean
}>({
  selectedRowKeys: [], // Check here to configure the default column
  loading: false,
})

/**
 * 选中表格
 */
function onSelectChange(selectedRowKeys: Key[]) {
  state.selectedRowKeys = selectedRowKeys
}

function deleteLoginLog(ids: string[]) {
  Modal.confirm({
    title: '注意',
    icon: createVNode(ExclamationCircleOutlined),
    content: `确定删除编号为 【${ids.join(',')}】 的登录日志吗？`,
    okText: '确认',
    cancelText: '取消',
    onOk: () => {
      deleteLoginLogByIds(ids).then((res) => {
        if (res.code === 200) {
          message.success('删除成功')
          state.selectedRowKeys = []
          refreshFunc()
        }
      })
    },
  })
}

function deleteAll() {
  if (!tabData || tabData.value.length === 0) {
    message.warn('没有能清空的登录日志')
    return
  }
  const ids = tabData.value.map((tab: any) => tab.id)
  Modal.confirm({
    title: '注意',
    icon: createVNode(ExclamationCircleOutlined),
    content: `是否要清空所有登录日志？`,
    okText: '确认',
    cancelText: '取消',
    onOk: () => {
      deleteLoginLogByIds(ids).then((res) => {
        if (res.code === 200) {
          message.success('清空成功')
          refreshFunc()
        }
      })
    },
  })
}
</script>

<template>
  <layout
    :form-state="formState"
    @update:refresh-func="refreshFunc"
    @update:on-finish="onFinish"
  >
    <template #form-items>
      <a-form-item
        label="登录地址"
        name="address"
      >
        <a-input v-model:value="formState.address" placeholder="请输入登录地址" style="width: 250px" />
      </a-form-item>
      <a-form-item
        label="用户名称"
        name="userName"
      >
        <a-input v-model:value="formState.userName" placeholder="请输入用户名称" style="width: 250px" />
      </a-form-item>
      <a-form-item label="状态" name="state" style="margin-left: 1.8rem">
        <a-select v-model:value="formState.state" placeholder="登录状态" style="width: 250px">
          <a-select-option :value="0">
            成功
          </a-select-option>
          <a-select-option :value="1">
            失败
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item
        label="登录时间"
        name="time"
      >
        <a-range-picker v-model:value="formState.time" placement="topLeft" :placeholder="['开始时间', '结束时间']" style="width: 250px" />
      </a-form-item>
    </template>
    <template #operate-btn>
      <a-button type="dashed" danger ghost :disabled="!(state.selectedRowKeys.length > 0)" @click="deleteLoginLog(state.selectedRowKeys as string[])">
        <template #icon>
          <DeleteOutlined />
        </template>
        删除
      </a-button>
      <a-button type="primary" danger ghost @click="deleteAll()">
        <template #icon>
          <DeleteOutlined />
        </template>
        清空
      </a-button>
      <a-button class="orange" @click="message.warn('别点了，有空再写')">
        <template #icon>
          <VerticalAlignBottomOutlined />
        </template>
        导出
      </a-button>
    </template>
    <template #table-content>
      <a-table
        :columns="columns"
        :data-source="tabData"
        :loading="loading"
        :row-selection="{ selectedRowKeys: state.selectedRowKeys, onChange: onSelectChange }"
        :row-key="record => record.id"
        size="small"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'userName' && record.userName.length > 10 ">
            <a-tooltip placement="top">
              <template #title>
                <span>{{ record.userName }}</span>
              </template>
              <span style="width: 100px; display: inline-block; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
                {{ record.userName }}
              </span>
            </a-tooltip>
          </template>
          <template v-if="column.dataIndex === 'message' && record.message.length > 10 ">
            <a-tooltip placement="top">
              <template #title>
                <span>{{ record.message }}</span>
              </template>
              <span style="width: 100px; display: inline-block; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
                {{ record.message }}
              </span>
            </a-tooltip>
          </template>
          <template v-if="column.dataIndex === 'state'">
            <template v-if="record.state === 0">
              <a-tag color="#87d068">
                成功
              </a-tag>
            </template>
            <template v-else>
              <a-tag color="#f50">
                失败
              </a-tag>
            </template>
          </template>
          <template v-if="column.dataIndex === 'type'">
            <template v-if="record.type === 0">
              <a-tag color="#87d068">
                前台
              </a-tag>
            </template>
            <template v-if="record.type === 1">
              <a-tag color="#2db7f5">
                后台
              </a-tag>
            </template>
            <template v-if="record.type === 2">
              <a-tag color="red">
                违规
              </a-tag>
            </template>
          </template>
        </template>
      </a-table>
    </template>
  </layout>
</template>

<style scoped lang="scss">

</style>
