<script setup lang="ts">
import type { Ref, UnwrapRef } from 'vue'
import { createVNode } from 'vue'
import 'md-editor-v3/lib/style.css'
import { MdPreview } from 'md-editor-v3'
import { Modal, message } from 'ant-design-vue'
import { ExclamationCircleOutlined, MessageOutlined } from '@ant-design/icons-vue'
import dayjs from 'dayjs'
import { chatGptList, deleteChatGpt, isCheckChatGpt, searchChatGpt } from '~/api/blog/chat-gpt'

const formData = reactive({
  userName: undefined,
  isCheck: undefined,
  type: undefined,
  time: undefined,
})

// 上传地址
const DomainName = import.meta.env.VITE_APP_DOMAIN_NAME

onMounted(async () => {
  await refreshFunc()
})

type Key = string | number

const state = reactive<{
  selectedRowKeys: Key[]
  loading: boolean
}>({
  selectedRowKeys: [], // Check here to configure the default column
  loading: false,
})

interface DataType {
  id: string
  conversation: any
  avatar: string
}

const loading = ref(false)
const tabData: Ref<UnwrapRef<DataType[]>> = ref([])

/**
 * 选中表格
 */
function onSelectChange(selectedRowKeys: Key[]) {
  state.selectedRowKeys = selectedRowKeys
}

const columns: any = [
  {
    title: '编号',
    dataIndex: 'id',
    align: 'center',
  },
  {
    title: '用户名称',
    dataIndex: 'userName',
    align: 'center',
  },
  {
    title: '会话记录',
    dataIndex: 'conversation',
    align: 'center',
  },
  {
    title: '是否有效',
    dataIndex: 'isCheck',
    align: 'center',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    align: 'center',
  },
  {
    title: '修改时间',
    dataIndex: 'createTime',
    align: 'center',
  },
  {
    title: '操作',
    dataIndex: 'operation',
    align: 'center',
  },
]

const contentModel = reactive({
  show: false,
  conversation: { content: [], model: '' },
  avatar: '',
})

async function refreshFunc() {
  loading.value = true
  const { data } = await chatGptList()
  if (data && data.length > 0) {
    tabData.value = data.map((item: any) => {
      item.isCheck = item.isCheck === 1
      return item
    })
  }
  loading.value = false
}

async function onFinish(values: any) {
  loading.value = true
  const submitData = {
    ...values,
    startTime: values.time && values.time.length > 0 ? dayjs(values.time[0]).format('YYYY-MM-DD HH:mm:ss') : undefined,
    endTime: values.time && values.time.length > 0 ? dayjs(values.time[1]).format('YYYY-MM-DD HH:mm:ss') : undefined,
  }
  const { data } = await searchChatGpt(submitData)
  if (data && data.length > 0) {
    tabData.value = data.map((item: any) => {
      item.isCheck = item.isCheck === 1
      return item
    })
  }
  else {
    message.warn('没有查询到相关收藏内容')
    tabData.value = []
  }

  loading.value = false
}

// 是否通过
function updateIsCheckFunc(id: string, isCheck: any, record: any) {
  record.isCheckloading = true
  isCheck = isCheck ? 1 : 0
  isCheckChatGpt({ id, isCheck }).then((res) => {
    if (res.code === 200) {
      message.success('操作成功')
      record.isCheckloading = false
    }
  })
}

// 删除文章
function onDelete(ids?: string[]) {
  if (ids) {
    deleteChatGpt(ids).then((res) => {
      if (res.code === 200) {
        message.success('删除成功')
        refreshFunc()
      }
    })
  }
  else {
    // 批量删除
    const ids = state.selectedRowKeys
    Modal.confirm({
      title: '注意',
      icon: createVNode(ExclamationCircleOutlined),
      content: `确定删除编号为 【${ids.join(',')}】 的收藏吗？`,
      okText: '确认',
      cancelText: '取消',
      onOk: () => {
        deleteChatGpt(ids as string[]).then((res) => {
          if (res.code === 200) {
            message.success('删除成功')
            refreshFunc()
          }
        })
      },
    })
  }
}

// 查看
function viewFunc(id?: string) {
  if (!id)
    id = state.selectedRowKeys[0] as string

  contentModel.show = true
  contentModel.conversation = JSON.parse(tabData.value.find((item: any) => item.id === id)?.conversation)
  contentModel.avatar = tabData.value.find((item: any) => item.id === id)?.avatar as string
  console.log(contentModel.conversation)
}
</script>

<template>
  <layout
    :form-state="formData"
    @update:refresh-func="refreshFunc"
    @update:on-finish="onFinish"
  >
    <template #form-items>
      <a-form-item label="用户名称" name="userName" style="margin-right: 1rem">
        <a-input v-model:value="formData.userName" placeholder="输入用户名称" style="width: 15em" />
      </a-form-item>
      <a-form-item label="是否通过" name="isCheck" style="margin-right: 1rem">
        <a-select
          v-model:value="formData.isCheck"
          style="width: 13em"
          placeholder="是否通过"
        >
          <a-select-option :value="1">
            是
          </a-select-option>
          <a-select-option :value="0">
            否
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item
        label="创建时间"
        name="time"
      >
        <a-range-picker v-model:value="formData.time" :placeholder="['开始时间', '结束时间']" />
      </a-form-item>
    </template>
    <template #operate-btn>
      <a-button type="default" style="margin-right: 10px" :disabled="state.selectedRowKeys.length !== 1" @click="viewFunc()">
        <template #icon>
          <MessageOutlined />
        </template>
        查看
      </a-button>
      <a-button type="dashed" danger ghost :disabled="state.selectedRowKeys.length === 0" @click="onDelete()">
        <template #icon>
          <DeleteOutlined />
        </template>
        删除
      </a-button>
      <!-- 查看框 -->
      <a-modal v-model:open="contentModel.show" width="800px">
        <template #title>
          <span style="color: #1677FF"><MessageOutlined /></span><span style="margin-left: 0.2rem">查看会话-{{ contentModel.conversation.model }}</span>
        </template>
        <template #footer>
          <a-button @click="contentModel.show = false">
            关闭
          </a-button>
        </template>
        <a-list item-layout="horizontal" :data-source="contentModel.conversation.content">
          <template #renderItem="{ item }">
            <a-list-item>
              <a-list-item-meta
                :description="item.type === 'question' ? '提问' : '应答'"
              >
                <template #title>
                  <MdPreview v-model="item.content" />
                </template>
                <template #avatar>
                  <a-avatar :src="item.type === 'question' ? contentModel.avatar : `${DomainName}blog/gpt/chatgpt-de79290a.png`" />
                </template>
              </a-list-item-meta>
            </a-list-item>
          </template>
        </a-list>
      </a-modal>
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
          <template v-if="column.dataIndex === 'isCheck'">
            <a-switch
              v-model:checked="record.isCheck"
              checked-children="是"
              un-checked-children="否"
              :loading="record.isCheckloading"
              @change="updateIsCheckFunc(record.id, record.isCheck, record)"
            />
          </template>
          <template v-if="column.dataIndex === 'conversation'">
            {{ record.conversation.length > 40 ? `${record.conversation.slice(0, 40)}...` : record.conversation }}
          </template>
          <template v-if="column.dataIndex === 'operation'">
            <a-button type="link" style="padding: 0" @click="viewFunc(record.id)">
              <template #icon>
                <MessageOutlined />
              </template>
              <span style="margin-left: 3px">查看</span>
            </a-button>
            <a-popconfirm
              title="是否确定删除"
              ok-text="Yes"
              cancel-text="No"
              @confirm="onDelete([record.id])"
            >
              <a-button type="link" style="padding: 0">
                <template #icon>
                  <DeleteOutlined />
                </template>
                <span style="margin-left: 3px">删除</span>
              </a-button>
            </a-popconfirm>
          </template>
          <template v-if="column.dataIndex === 'createTime'">
            <a-tooltip placement="top">
              <template #title>
                <span>{{ record.createTime }}</span>
              </template>
              {{ record.createTime.split(' ')[0] }}...
            </a-tooltip>
          </template>
          <template v-if="column.dataIndex === 'updateTime'">
            <a-tooltip placement="top">
              <template #title>
                <span>{{ record.updateTime }}</span>
              </template>
              {{ record.updateTime.split(' ')[0] }}...
            </a-tooltip>
          </template>
        </template>
      </a-table>
    </template>
  </layout>
</template>

<style scoped lang="scss">

</style>
