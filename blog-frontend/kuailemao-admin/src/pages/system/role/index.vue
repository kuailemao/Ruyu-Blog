<script setup lang="ts">
import type { Ref, UnwrapRef } from 'vue'
import { Modal, message } from 'ant-design-vue'
import { createVNode } from 'vue'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'
import MyModal from './my-modal.vue'
import { roleDelete, roleInfoById, roleList, roleSearch, roleUpdateStatus } from '~/api/role'
import { getMenusApi } from '~/api/common/menu.ts'
import { buildTree } from '~/utils/tree.ts'

const formState = reactive({
  roleName: undefined,
  roleKey: undefined,
  status: undefined,
  time: undefined,
  orderNum: undefined,
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
    title: '角色编号',
    dataIndex: 'id',
    align: 'center',
  },
  {
    title: '角色名称',
    dataIndex: 'roleName',
    align: 'center',
  },
  {
    title: '角色字符',
    dataIndex: 'roleKey',
    align: 'center',
  },
  {
    title: '显示顺序',
    dataIndex: 'orderNum',
    align: 'center',
  },
  {
    title: '状态',
    dataIndex: 'status',
    align: 'center',
    key: 'status',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
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
    const { data } = await roleList()
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
      createTimeStart: values.time[0].format('YYYY-MM-DD HH:mm:ss'),
    })
  }
  if (values.time && values.time[1]) {
    Object.assign(submitData, {
      createTimeEnd: values.time[1].format('YYYY-MM-DD HH:mm:ss'),
    })
  }
  loading.value = true
  const { data } = await roleSearch(submitData)
  await refreshFunc(data)
}

/**
 * 修改状态
 */
async function statusBtn(id: string, status: number) {
  const data = await roleUpdateStatus(id, status ? 0 : 1).catch((msg) => {
    message.warn(msg)
    tabData.value = tabData.value.map((item: any) => {
      if (id === (item.id)) {
        // 等待 0.5s
        setTimeout(() => {
          item.isDisable = !item.isDisable
        }, 500)
      }
      return item
    })
  })
  if (data.code === 200) {
    if (status)
      message.success('已启用')
    else message.info('已停用')
  }
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
})

/**
 * 关闭窗口
 */
function closeModal() {
  modalInfo.open = false
}

const treeData = ref()
const formData = ref()

function deleteRole(ids: string[], type?: number) {
  if (type === 0) {
    Modal.confirm({
      title: '注意',
      icon: createVNode(ExclamationCircleOutlined),
      content: `确定删除编号为 【${ids.join(',')}】 的角色吗？`,
      okText: '确认',
      cancelText: '取消',
      onOk: () => {
        roleDelete(ids).then((res) => {
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
  roleDelete(ids).then((res) => {
    if (res.code === 200) {
      message.success('删除成功')
      state.selectedRowKeys = []
      refreshFunc()
    }
  })
}

async function updateOrInsertRole(id?: string) {
  const { data } = await getMenusApi(1) as any
  treeData.value = buildTree(data)
  if (id) {
    const { data: roleInfo } = await roleInfoById(id)
    formData.value = roleInfo
    modalInfo.open = true
    modalInfo.title = '修改角色'
  }
  else {
    formData.value = {}
    modalInfo.open = true
    modalInfo.title = '添加角色'
  }
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
        label="角色名称"
        name="roleName"
      >
        <a-input v-model:value="formState.roleName" placeholder="请输入角色名称" style="width: 250px" />
      </a-form-item>
      <a-form-item
        label="角色字符"
        name="roleKey"
      >
        <a-input v-model:value="formState.roleKey" placeholder="请输入角色字符" style="width: 250px" />
      </a-form-item>
      <a-form-item label="状态" name="status" style="width: 240px">
        <a-select v-model:value="formState.status" placeholder="用户状态">
          <a-select-option :value="0">
            正常
          </a-select-option>
          <a-select-option :value="1">
            停用
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item
        label="创建时间"
        name="time"
      >
        <a-range-picker v-model:value="formState.time" :placeholder="['开始时间', '结束时间']" />
      </a-form-item>
    </template>
    <template #operate-btn>
      <a-button type="primary" @click="updateOrInsertRole()">
        <template #icon>
          <PlusOutlined />
        </template>
        新增
      </a-button>
      <a-button class="green" :disabled="!hasSelected" @click="updateOrInsertRole(state.selectedRowKeys[0] as string)">
        <template #icon>
          <FileSyncOutlined />
        </template>
        修改
      </a-button>
      <a-button type="dashed" danger ghost :disabled="!(state.selectedRowKeys.length > 0)" @click="deleteRole(state.selectedRowKeys as string[], 0)">
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
      <a-table
        :columns="columns"
        :data-source="tabData"
        :loading="loading"
        :row-selection="{ selectedRowKeys: state.selectedRowKeys, onChange: onSelectChange }"
        :row-key="record => record.id"
        size="small"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'status'">
            <a-switch v-model:checked="record.status" @change="statusBtn(record.id, record.status)" />
          </template>
          <template v-if="column.key === 'operation'">
            <a-button type="link" style="padding: 0;" @click="updateOrInsertRole(record.id)">
              <template #icon>
                <FileSyncOutlined />
              </template>
              <span style="margin-inline-start:1px">修改</span>
            </a-button>
            <a-popconfirm
              title="是否确定删除"
              ok-text="Yes"
              cancel-text="No"
              @confirm="deleteRole([record.id])"
            >
              <a-button type="link" style="padding: 0;margin-left: 5px">
                <template #icon>
                  <DeleteOutlined />
                </template>
                <span style="margin-inline-start:1px">删除</span>
              </a-button>
            </a-popconfirm>
            <a-button type="link" style="padding: 0;margin-left: 5px" @click="$router.push({ path: '/role/authorization', query: { id: record.id, name: record.roleName } })">
              <template #icon>
                <KeyOutlined />
              </template>
              <span style="margin-inline-start:1px">授权用户</span>
            </a-button>
          </template>
          <template v-else-if="column.key === 'icon'">
            <!-- 图标 -->
            <component :is="record.icon" />
          </template>
        </template>
      </a-table>
      <MyModal
        :open="modalInfo.open"
        :title="modalInfo.title"
        :tree-data="treeData"
        :form-data="formData"
        @update:close="closeModal"
      />
    </template>
  </layout>
</template>

<style scoped lang="scss">

</style>
