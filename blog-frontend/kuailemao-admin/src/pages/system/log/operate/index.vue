<script setup lang="ts">
import { Modal, message } from 'ant-design-vue'
import { createVNode } from 'vue'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'
import { deleteLogByIds, logList, searchLog } from '~/api/log/operate'

const formState = reactive({
  ip: undefined,
  module: undefined,
  userName: undefined,
  operation: undefined,
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
    title: '系统模块',
    dataIndex: 'module',
    align: 'center',
  },
  {
    title: '操作类型',
    dataIndex: 'operation',
    align: 'center',
  },
  {
    title: '操作人员',
    dataIndex: 'userName',
    align: 'center',
  },
  {
    title: '操作地址',
    dataIndex: 'ip',
    align: 'center',
  },
  {
    title: '操作地点',
    dataIndex: 'address',
    align: 'center',
  },
  {
    title: '操作状态',
    dataIndex: 'state',
    align: 'center',
  },
  {
    title: '操作日期',
    dataIndex: 'loginTime',
    align: 'center',
  },
  {
    title: '消耗时间',
    dataIndex: 'time',
    align: 'center',
  },
  {
    title: '操作',
    dataIndex: 'operate',
    align: 'center',
  },
]

type Key = string | number

const loading = ref(false)
const tabData = ref([]) as any
// 类型
const operations = ref()

onMounted(() => {
  refreshFunc()
})

/**
 * 选中表格
 */
function onSelectChange(selectedRowKeys: Key[]) {
  state.selectedRowKeys = selectedRowKeys
}

function deleteLog(ids: string[]) {
  Modal.confirm({
    title: '注意',
    icon: createVNode(ExclamationCircleOutlined),
    content: `确定删除编号为 【${ids.join(',')}】 的登录日志吗？`,
    okText: '确认',
    cancelText: '取消',
    onOk: () => {
      deleteLogByIds(ids).then((res) => {
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
    content: `是否要清空所有操作日志？`,
    okText: '确认',
    cancelText: '取消',
    onOk: () => {
      deleteLogByIds(ids).then((res) => {
        if (res.code === 200) {
          message.success('清空成功')
          refreshFunc()
        }
      })
    },
  })
}

const modalOpen = ref(false)

function handleClose() {
  modalOpen.value = false
}

// 日志详细
const logDetail = ref({
  module: undefined,
  operation: undefined,
  method: undefined,
  userName: undefined,
  exception: undefined,
  ip: undefined,
  reqParameter: undefined,
  address: undefined,
  reqAddress: undefined,
  reqMapping: undefined,
  state: 0,
  loginTime: undefined,
  time: undefined,
  returnParameter: undefined,
  description: undefined,
})

function getLog(id: string) {
  tabData.value.map((item: any) => {
    if (item.id === id)
      logDetail.value = item
    return null
  })
  modalOpen.value = true
}

const page = reactive({
  // 总日志数量
  total: 0,
  // 当前页
  current: 1,
  pageSize: 10,
})

// 分页配置
function pageChange(pagination: any) {
  // {current: 2, pageSize: 10}
  page.current = pagination.current
  page.pageSize = pagination.pageSize
  // 字段全部为空就走刷新，不然就搜索
  if (Object.values(formState).every(value => value === undefined))
    refreshFunc()

  onFinish(formState)
}

async function refreshFunc(searchData?: object) {
  loading.value = true
  let newData: any
  if (searchData) {
    newData = searchData
  }
  else {
    const { data } = await logList(page.current, page.pageSize)
    if (data && data.page.length > 0)
      operations.value = Array.from(new Set(data.page.map((item: any) => item.operation)))

    newData = data.page
    page.total = data.total
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
    current: page.current,
    pageSize: page.pageSize,
  }
  if (values.time && values.time[0]) {
    Object.assign(submitData, {
      logTimeStart: values.time[0].format('YYYY-MM-DD HH:mm:ss'),
    })
  }
  if (values.time && values.time[1]) {
    Object.assign(submitData, {
      logTimeEnd: values.time[1].format('YYYY-MM-DD HH:mm:ss'),
    })
  }
  loading.value = true
  const { data } = await searchLog(submitData)
  page.total = data.total
  await refreshFunc(data.page)
}

const state = reactive<{
  selectedRowKeys: Key[]
  loading: boolean
}>({
  selectedRowKeys: [], // Check here to configure the default column
  loading: false,
})
</script>

<template>
  <layout
    :form-state="formState"
    @update:refresh-func="refreshFunc"
    @update:on-finish="onFinish"
  >
    <template #form-items>
      <a-form-item
        label="操作地址"
        name="ip"
      >
        <a-input v-model:value="formState.ip" placeholder="请输入操作地址" style="width: 250px" />
      </a-form-item>
      <a-form-item
        label="系统模块"
        name="module"
      >
        <a-input v-model:value="formState.module" placeholder="请输入系统模块" style="width: 250px" />
      </a-form-item>
      <a-form-item
        label="操作人员"
        name="userName"
      >
        <a-input v-model:value="formState.userName" placeholder="请输入操作人员" style="width: 250px" />
      </a-form-item>
      <a-form-item label="类型" name="operation" style="margin-left: 1.8rem">
        <a-select v-model:value="formState.operation" placeholder="操作类型" style="width: 250px">
          <template v-for="item in operations" :key="item">
            <a-select-option :value="item">
              {{ item }}
            </a-select-option>
          </template>
        </a-select>
      </a-form-item>
      <a-form-item label="状态" name="state" style="margin-left: 1.8rem">
        <a-select v-model:value="formState.state" placeholder="操作状态" style="width: 250px">
          <a-select-option :value="0">
            成功
          </a-select-option>
          <a-select-option :value="1">
            失败
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item
        label="操作时间"
        name="time"
      >
        <a-range-picker v-model:value="formState.time" placement="topLeft" :placeholder="['开始时间', '结束时间']" style="width: 250px" />
      </a-form-item>
    </template>
    <template #operate-btn>
      <a-button type="dashed" danger ghost :disabled="!(state.selectedRowKeys.length > 0)" @click="deleteLog(state.selectedRowKeys as string[])">
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
        :pagination="{ total: page.total }"
        @change="pageChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'userName'">
            <a-tag v-if="record.userName === 'unknown-1702606997'" color="pink">
              未知
            </a-tag>
          </template>
          <template v-if="column.dataIndex === 'operation'">
            <a-tag :color="record.operation !== '删除' ? 'orange' : 'red'">
              {{ record.operation }}
            </a-tag>
          </template>
          <template v-if="column.dataIndex === 'time'">
            <a-tag color="blue">
              {{ record.time }}毫秒
            </a-tag>
          </template>
          <template v-if="column.dataIndex === 'state'">
            <template v-if="record.state === 0">
              <a-tag color="#87d068">
                成功
              </a-tag>
            </template>
            <template v-if="record.state === 1">
              <a-tag color="#f50">
                失败
              </a-tag>
            </template>
            <template v-if="record.state === 2">
              <a-tag color="#a46244">
                异常
              </a-tag>
            </template>
          </template>
          <template v-if="column.dataIndex === 'operate'">
            <a-button type="link" @click="getLog(record.id)">
              <template #icon>
                <EyeOutlined />
              </template>
              <span style="margin-left: 0.2rem">详细</span>
            </a-button>
          </template>
        </template>
      </a-table>
      <a-modal v-model:open="modalOpen" width="900px" @cancel="handleClose">
        <template #footer>
          <a-button @click="handleClose">
            关闭
          </a-button>
        </template>
        <template #title>
          <span style="font-size: 1.2rem">操作日志详细</span>
        </template>
        <div class="log-detail">
          <div>
            <div>
              <label>操作模块：</label><div>{{ logDetail.module }}</div>
            </div>
            <div><label>请求地址：</label><div>{{ logDetail.reqAddress }}</div></div>
          </div>
          <div>
            <div><label>登录信息：</label><div>{{ logDetail.userName }} / {{ logDetail.ip }} / {{ logDetail.address }}</div></div>
            <div><label>请求方式：</label><div>{{ logDetail.reqMapping }}</div></div>
          </div>
          <div>
            <div><label>操作方法：</label><div>{{ logDetail.method }}</div></div>
          </div>
          <div>
            <div>
              <label>
                请求参数：
              </label><div>{{ logDetail.reqParameter }}</div>
            </div>
          </div>
          <div>
            <div><label>返回参数：</label><div>{{ logDetail.returnParameter }}</div></div>
          </div>
          <div>
            <div>
              <label>操作类型：</label>
              <div>
                <a-tag color="orange">
                  {{ logDetail.operation }}
                </a-tag>
              </div>
            </div>
            <div>
              <label>操作描述：</label>
              <div>
                {{ logDetail.description }}
              </div>
            </div>
          </div>
          <div>
            <div v-if="logDetail.state === 2">
              <label>异常信息：</label>
              <div>
                {{ logDetail.exception }}
              </div>
            </div>
          </div>
          <div>
            <div>
              <label>操作状态：</label>
              <div v-if="logDetail.state === 0">
                <a-tag color="#87d068">
                  成功
                </a-tag>
              </div>
              <div v-if="logDetail.state === 1">
                <a-tag color="#f50">
                  失败
                </a-tag>
              </div>
              <div v-if="logDetail.state === 2">
                <a-tag color="#a46244">
                  异常
                </a-tag>
              </div>
            </div>
            <div>
              <label>消耗时间：</label><div>
                <a-tag color="blue">
                  {{ logDetail.time }}毫秒
                </a-tag>
              </div>
            </div>
            <div><label>操作时间：</label><div>{{ logDetail.loginTime }}</div></div>
          </div>
        </div>
      </a-modal>
    </template>
  </layout>
</template>

<style scoped lang="scss">
.log-detail{
  display: flex;
  flex-direction: column;
  // 文本自动换行
  word-break: break-all;
  div{
    display: flex;
    margin-top: 1rem;
    @media(max-width: 768px) {
      display: block;
    }
    div{
      display: flex;
      width: 100%;
      label{
        font-weight: bold;
        width: 7em;
        font-size: 1rem;
      }
      div{
        margin-top: 0;
      }
    }
  }
}
</style>
