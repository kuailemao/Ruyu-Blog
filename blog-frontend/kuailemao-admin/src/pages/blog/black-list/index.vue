<script setup lang="ts">
import type {Ref, UnwrapRef} from 'vue'
import {Modal, message} from 'ant-design-vue'
import {createVNode} from 'vue'
import {ExclamationCircleOutlined} from '@ant-design/icons-vue'
import {getMenusApi} from '~/api/common/menu.ts'
import {buildTree} from '~/utils/tree.ts'
import {
  categoryList,
  deleteCategoryByIds,
  searchCategory,
  searchCategoryById,
  updateCategory,
} from '~/api/blog/category'
import {addCategory} from '~/api/blog/article'
import {blackList} from "~/api/blog/black-list";

const formState = reactive({
  categoryName: undefined,
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
    title: '名单编号',
    dataIndex: 'id',
    align: 'center',
  },
  {
    title: '用户名称',
    dataIndex: 'userName',
    align: 'center',
  },
  {
    title: '封禁时间',
    dataIndex: 'bannedTime',
    align: 'center',
  },
  {
    title: '解封时间',
    dataIndex: 'expiresTime',
    align: 'center',
  },
  {
    title: '封禁类型',
    dataIndex: 'type',
    align: 'center',
  },
  {
    title: '封禁理由',
    dataIndex: 'reason',
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
  const {data} = await blackList(searchData)
  tabData.value = data
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
  await refreshFunc(values)
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

function deleteCategory(ids: string[], type?: number) {
  if (type === 0) {
    Modal.confirm({
      title: '注意',
      icon: createVNode(ExclamationCircleOutlined),
      content: `确定删除编号为 【${ids.join(',')}】 的分类吗？`,
      okText: '确认',
      cancelText: '取消',
      onOk: () => {
        deleteCategoryByIds(ids).then((res) => {
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
  deleteCategoryByIds(ids).then((res) => {
    if (res.code === 200) {
      message.success('删除成功')
      state.selectedRowKeys = []
      refreshFunc()
    }
  })
}

async function updateOrInsertCategory(id?: string) {
  const {data} = await getMenusApi(1) as any
  treeData.value = buildTree(data)
  if (id) {
    const {data: categoryInfo} = await searchCategoryById(id)
    formData.value = categoryInfo
    modalInfo.open = true
    modalInfo.title = '修改分类'
  } else {
    formData.value = {}
    modalInfo.open = true
    modalInfo.title = '添加分类'
  }
}

// 确定
async function modelOk() {
  modalInfo.loading = true
  if (formData.value.id) {
    await updateCategory(formData.value).then((res) => {
      if (res.code === 200) {
        modalInfo.loading = false
        message.success('修改成功')
      }
    })
  } else {
    await addCategory(formData.value).then((res) => {
      if (res.code === 200) {
        modalInfo.loading = false
        message.success('添加成功')
      }
    })
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
          label="用户名称"
          name="categoryName"
      >
        <a-input v-model:value="formState.categoryName" placeholder="请输入用户名称" style="width: 150px"/>
      </a-form-item>
      <a-form-item
          label="封禁时间"
          name="time"
      >
        <a-range-picker v-model:value="formState.time" :placeholder="['开始时间', '结束时间']"/>
      </a-form-item>
      <a-form-item
          label="封禁理由"
          name="categoryName"
      >
        <a-input v-model:value="formState.categoryName" placeholder="请输入封禁理由" style="width: 200px"/>
      </a-form-item>
      <a-form-item label="封禁类型" name="isCheck" style="margin-right: 1rem">
        <a-select
            style="width: 7em"
            placeholder="封禁类型"
        >
          <a-select-option :value="1">
            手动封禁
          </a-select-option>
          <a-select-option :value="0">
            自动封禁
          </a-select-option>
        </a-select>
      </a-form-item>
    </template>
    <template #operate-btn>
      <a-button type="primary" @click="updateOrInsertCategory()">
        <template #icon>
          <PlusOutlined/>
        </template>
        新增
      </a-button>
      <a-button class="green" :disabled="!hasSelected"
                @click="updateOrInsertCategory(state.selectedRowKeys[0] as string)">
        <template #icon>
          <FileSyncOutlined/>
        </template>
        修改
      </a-button>
      <a-button type="dashed" danger ghost :disabled="!(state.selectedRowKeys.length > 0)"
                @click="deleteCategory(state.selectedRowKeys as string[], 0)">
        <template #icon>
          <DeleteOutlined/>
        </template>
        删除
      </a-button>
      <a-button class="orange" @click="message.warn('别点了，有空再写')">
        <template #icon>
          <VerticalAlignBottomOutlined/>
        </template>
        导出
      </a-button>
    </template>
    <template #table-content>
      <a-modal v-model:open="modalInfo.open" :title="modalInfo.title" :confirm-loading="modalInfo.loading" width="400px"
               @ok="modelOk">
        <a-form-item
            label="分类名称"
            name="categoryName"
        >
          <a-input v-model:value="formData.categoryName" placeholder="请输入分类名称" show-count :maxlength="20"/>
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
          <template v-if="column.dataIndex === 'type'">
            <a-tag :color="record.type == 1 ? '#87d068' : '#f50'">
              {{ record.type == 1 ? '手动封禁' : '自动封禁' }}
            </a-tag>
          </template>
          <!-- 系统自动封禁，没有用户名称，显示ip信息 -->
          <template v-if="column.dataIndex === 'userName' && record.type == 2">
            <a-popover title="IP信息" >
              <template #content>
                  <div :style="{ maxWidth: '300px', wordBreak: 'break-all' }">
                  {{ record.ipInfo }}
                </div>
              </template>
              <a-button>查看IP信息</a-button>
            </a-popover>
          </template>
          <template v-else-if="column.dataIndex === 'reason' && record.reason.length > 10">
            <a-popover title="封禁理由">
              <template #content>
                {{ record.reason }}
              </template>
              {{ record.reason.slice(0, 10) }}...
            </a-popover>
          </template>
          <template v-if="column.key === 'operation'">
            <a-button type="link" style="padding: 0;" @click="updateOrInsertCategory(record.id)">
              <template #icon>
                <FileSyncOutlined/>
              </template>
              <span style="margin-inline-start:1px">修改</span>
            </a-button>
            <a-popconfirm
                title="是否确定删除"
                ok-text="Yes"
                cancel-text="No"
                @confirm="deleteCategory([record.id])"
            >
              <a-button type="link" style="padding: 0;margin-left: 5px">
                <template #icon>
                  <DeleteOutlined/>
                </template>
                <span style="margin-inline-start:1px">解除</span>
              </a-button>
            </a-popconfirm>
          </template>
          <template v-else-if="column.key === 'icon'">
            <!-- 图标 -->
            <component :is="record.icon"/>
          </template>
        </template>
      </a-table>
    </template>
  </layout>
</template>

<style scoped lang="scss">

</style>
