<script setup lang="ts">
import type { Ref, UnwrapRef } from 'vue'
import { Modal, message } from 'ant-design-vue'
import { createVNode } from 'vue'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'
import { getMenusApi } from '~/api/common/menu.ts'
import { buildTree } from '~/utils/tree.ts'
import { addTag, deleteTagByIds, searchTag, searchTagById, tagList, updateTag } from '~/api/blog/tag'

const formState = reactive({
  tagName: undefined,
  time: undefined,
})

interface DataType {
  id: string
  title: string
  key: string
  orderNum: number
  status: boolean
  createTime: string
}

const columns: any = [
  {
    title: '标签编号',
    dataIndex: 'id',
    align: 'center',
  },
  {
    title: '标签名称',
    dataIndex: 'tagName',
    align: 'center',
  },
  {
    title: '文章数量',
    dataIndex: 'articleCount',
    align: 'center',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    align: 'center',
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
    align: 'center',
  },
  {
    title: '操作',
    dataIndex: 'operation',
    key: 'operation',
    align: 'center',
  },
]

type Key = string | number

const loading = ref(false)
const tabData: Ref<UnwrapRef<DataType[]>> = ref([])

onMounted(() => {
  refreshFunc()
})

async function refreshFunc(searchData?: object) {
  loading.value = true
  let newData: any = []
  if (searchData) {
    newData = searchData
  }
  else {
    const { data } = await tagList()
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
  if (values.time && values.time[0] && values.time && values.time[1]) {
    Object.assign(submitData, {
      startTime: values.time[0].format('YYYY-MM-DD HH:mm:ss'),
      endTime: values.time[1].format('YYYY-MM-DD HH:mm:ss'),
    })
  }

  loading.value = true
  const { data } = await searchTag(submitData)
  await refreshFunc(data)
}

const state = reactive<{
  selectedRowKeys: Key[]
  loading: boolean
}>({
  selectedRowKeys: [], // Check here to configure the default column
  loading: false,
})

const hasSelected = computed(() => state.selectedRowKeys.length > 0 && state.selectedRowKeys.length === 1)

/**
 * 选中表格
 */
function onSelectChange(selectedRowKeys: Key[]) {
  state.selectedRowKeys = selectedRowKeys
}

const modalInfo = reactive({
  open: false,
  title: '',
  loading: false,
})

const treeData = ref()
const formData = ref()

function deleteTag(ids: string[], type?: number) {
  if (type === 0) {
    Modal.confirm({
      title: '注意',
      icon: createVNode(ExclamationCircleOutlined),
      content: `确定删除编号为 【${ids.join(',')}】 的标签吗？`,
      okText: '确认',
      cancelText: '取消',
      onOk: () => {
        deleteTagByIds(ids).then((res) => {
          if (res.code === 200) {
            message.success('删除成功')
            state.selectedRowKeys = []
            refreshFunc()
          }
        })
      },
    })
    return
  }
  deleteTagByIds(ids).then((res) => {
    if (res.code === 200) {
      message.success('删除成功')
      state.selectedRowKeys = []
      refreshFunc()
    }
  })
}

async function updateOrInsertTag(id?: string) {
  const { data } = await getMenusApi(1) as any
  treeData.value = buildTree(data)
  if (id) {
    const { data: tagInfo } = await searchTagById(id)
    formData.value = tagInfo
    modalInfo.open = true
    modalInfo.title = '修改标签'
  }
  else {
    formData.value = {}
    modalInfo.open = true
    modalInfo.title = '添加标签'
  }
}

// 确定
async function modelOk() {
  modalInfo.loading = true
  if (formData.value.id) {
    await updateTag(formData.value).then((res) => {
      modalInfo.loading = false
      if (res.code === 200)
        message.success('修改成功')
    }).catch()
  }
  else {
    await addTag(formData.value).then((res) => {
      modalInfo.loading = false
      if (res.code === 200)
        message.success('添加成功')
    }).catch(() => modalInfo.loading = false)
  }
  modalInfo.open = false
  await refreshFunc()
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
        label="标签名称"
        name="tagName"
      >
        <a-input v-model:value="formState.tagName" placeholder="请输人标签名称" style="width: 250px" />
      </a-form-item>
      <a-form-item
        label="创建时间"
        name="time"
      >
        <a-range-picker v-model:value="formState.time" :placeholder="['开始时间', '结束时间']" />
      </a-form-item>
    </template>
    <template #operate-btn>
      <a-button type="primary" @click="updateOrInsertTag()">
        <template #icon>
          <PlusOutlined />
        </template>
        新增
      </a-button>
      <a-button class="green" :disabled="!hasSelected" @click="updateOrInsertTag(state.selectedRowKeys[0] as string)">
        <template #icon>
          <FileSyncOutlined />
        </template>
        修改
      </a-button>
      <a-button type="dashed" danger ghost :disabled="!(state.selectedRowKeys.length > 0)" @click="deleteTag(state.selectedRowKeys as string[], 0)">
        <template #icon>
          <DeleteOutlined />
        </template>
        删除
      </a-button>
      <a-button class="orange" @click="message.warn('别点了，有空再写')">
        <template #icon>
          <VerticalAlignBottomOutlined />
        </template>
        导出
      </a-button>
    </template>
    <template #table-content>
      <a-modal v-model:open="modalInfo.open" :title="modalInfo.title" :confirm-loading="modalInfo.loading" width="400px" @ok="modelOk">
        <a-form-item
          label="标签名称"
          name="tagName"
        >
          <a-input v-model:value="formData.tagName" placeholder="请输人标签名称" show-count :maxlength="20" />
        </a-form-item>
      </a-modal>
      <a-table
        :columns="columns"
        :data-source="tabData"
        :loading="loading"
        :row-selection="{ selectedRowKeys: state.selectedRowKeys, onChange: onSelectChange }"
        :row-key="record => record.id"
        size="small"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'tagName'">
            <a-tag color="blue">
              <TagOutlined /><span>{{ record.tagName }}</span>
            </a-tag>
          </template>
          <template v-if="column.dataIndex === 'articleCount'">
            <a-tag>
              {{ record.articleCount }}
            </a-tag>
          </template>
          <template v-if="column.key === 'operation'">
            <a-button type="link" style="padding: 0;" @click="updateOrInsertTag(record.id)">
              <template #icon>
                <FileSyncOutlined />
              </template>
              <span style="margin-inline-start:1px">修改</span>
            </a-button>
            <a-popconfirm
              title="是否确定删除"
              ok-text="Yes"
              cancel-text="No"
              @confirm="deleteTag([record.id])"
            >
              <a-button type="link" style="padding: 0;margin-left: 5px">
                <template #icon>
                  <DeleteOutlined />
                </template>
                <span style="margin-inline-start:1px">删除</span>
              </a-button>
            </a-popconfirm>
          </template>
          <template v-else-if="column.key === 'icon'">
            <!-- 图标 -->
            <component :is="record.icon" />
          </template>
        </template>
      </a-table>
    </template>
  </layout>
</template>

<style scoped lang="scss">

</style>
