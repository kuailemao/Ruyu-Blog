<script setup lang="ts">
import type { Ref, UnwrapRef } from 'vue'
import { createVNode } from 'vue'
import 'md-editor-v3/lib/style.css'
import { MdPreview } from 'md-editor-v3'
import { Modal, message } from 'ant-design-vue'
import { ExclamationCircleOutlined, MessageOutlined } from '@ant-design/icons-vue'
import dayjs from 'dayjs'
import { deleteFavorite, favoriteList, isCheckFavorite, searchFavorite } from '~/api/blog/favorite'

const formData = reactive({
  userName: undefined,
  isCheck: undefined,
  type: undefined,
  time: undefined,
})

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
  content: string
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
    title: '收藏类型',
    dataIndex: 'type',
    align: 'center',
  },
  {
    title: '收藏内容',
    dataIndex: 'content',
    align: 'center',
  },
  {
    title: '是否有效',
    dataIndex: 'isCheck',
    align: 'center',
  },
  {
    title: '收藏时间',
    dataIndex: 'createTime',
    align: 'center',
  },
  {
    title: '操作',
    dataIndex: 'operation',
    align: 'center',
  },
]

async function refreshFunc() {
  loading.value = true
  const { data } = await favoriteList()
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
  const { data } = await searchFavorite(submitData)
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
  isCheckFavorite({ id, isCheck }).then((res) => {
    if (res.code === 200) {
      message.success('操作成功')
      record.isCheckloading = false
    }
  })
}

// 删除文章
function onDelete(ids?: string[]) {
  if (ids) {
    deleteFavorite(ids).then((res) => {
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
        deleteFavorite(ids as string[]).then((res) => {
          if (res.code === 200) {
            message.success('删除成功')
            refreshFunc()
          }
        })
      },
    })
  }
}

const contentModel = reactive({
  show: false,
  content: '',
})

// 查看
function viewFunc(id?: string) {
  if (!id)
    id = state.selectedRowKeys[0] as string

  contentModel.show = true
  contentModel.content = tabData.value.find((item: any) => item.id === id)?.content as string
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
      <a-form-item label="收藏类型" name="type" style="margin-right: 1rem">
        <a-select
          v-model:value="formData.type"
          style="width: 13em"
          placeholder="收藏类型"
        >
          <a-select-option :value="1">
            文章
          </a-select-option>
          <a-select-option :value="2">
            留言
          </a-select-option>
        </a-select>
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
          <span style="color: #1677FF"><MessageOutlined /></span><span style="margin-left: 0.2rem">查看内容</span>
        </template>
        <template #footer>
          <a-button @click="contentModel.show = false">
            关闭
          </a-button>
        </template>
        <MdPreview v-model="contentModel.content" />
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
          <template v-if="column.dataIndex === 'type'">
            <a-tag color="blue">
              {{ record.type === 1 ? '文章' : '留言' }}
            </a-tag>
          </template>
          <template v-if="column.dataIndex === 'content'">
            {{ record.content.length > 7 ? `${record.content.slice(0, 7)}...` : record.content }}
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
