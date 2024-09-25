<script setup lang="ts">
import { Modal, message } from 'ant-design-vue'
import { type Ref, type UnwrapRef, createVNode } from 'vue'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'
import DetailModal from './detail-modal.vue'
import { userDelete, userDetail, userList, userSearch, userUpdateStatus } from '~/api/user'

const formState = reactive({
  username: undefined,
  email: undefined,
  isDisable: undefined,
  time: undefined,
})

const columns: any = [
  {
    title: '用户编号',
    dataIndex: 'id',
    align: 'center',
  },
  {
    title: '用户头像',
    dataIndex: 'avatar',
    align: 'center',
  },
  {
    title: '用户名称',
    dataIndex: 'username',
    align: 'center',
  },
  {
    title: '用户邮箱',
    dataIndex: 'email',
    align: 'center',
  },
  {
    title: '状态',
    dataIndex: 'isDisable',
    align: 'center',
    key: 'status',
  },
  {
    title: '注册方式',
    dataIndex: 'registerType',
    align: 'center',
  },
  {
    title: '登录地址',
    dataIndex: 'loginAddress',
    align: 'center',
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
const tabData: Ref<UnwrapRef<any[]>> = ref([])

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
    const { data } = await userList()
    newData = data
  }
  if (newData && newData.length > 0) {
    newData = newData.map((item: any) => {
      item.isDisable = item.isDisable === 0
      switch (item.registerType) {
        case 0:
          item.registerType = '邮箱/用户名'
          break
        case 1:
          item.registerType = 'Gitee'
          break
        case 2:
          item.registerType = 'Github'
          break
        default:
          item.registerType = '未知'
          break
      }
      return item
    })
  }
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
  const { data } = await userSearch(submitData)
  await refreshFunc(data)
}

/**
 * 修改状态
 */
async function statusBtn(id: string, isDisable: number) {
  const data: any = await userUpdateStatus(id, isDisable ? 0 : 1).catch((msg) => {
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
    if (isDisable)
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

function deleteUser(ids: string[]) {
  Modal.confirm({
    title: '注意',
    icon: createVNode(ExclamationCircleOutlined),
    content: `确定删除编号为 【${ids.join(',')}】 的角色吗？`,
    okText: '确认',
    cancelText: '取消',
    onOk: () => {
      userDelete(ids).then((res) => {
        if (res.code === 200) {
          message.success('删除成功')
          state.selectedRowKeys = []
          refreshFunc()
        }
      })
    },
  })
}
// 详情窗口
const detailModal = ref(false)
const userDetailData = ref()
// 新增窗口
const insertModal = ref(false)

async function userDetailFunc(id?: string) {
  if (!id)
    id = state.selectedRowKeys[0] as string
  detailModal.value = true
  const { data } = await userDetail(id)
  userDetailData.value = data
}

function closeModalFunc() {
  detailModal.value = false
  userDetailData.value = undefined
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
        name="username"
      >
        <a-input v-model:value="formState.username" placeholder="请输入用户名称" style="width: 250px" />
      </a-form-item>
      <a-form-item
        label="用户邮箱"
        name="email"
      >
        <a-input v-model:value="formState.email" placeholder="请输入用户邮箱" style="width: 250px" />
      </a-form-item>
      <a-form-item label="状态" name="isDisable" style="width: 240px">
        <a-select v-model:value="formState.isDisable" placeholder="用户状态">
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
      <a-button class="light-blue" :disabled="!hasSelected" @click="userDetailFunc()">
        <template #icon>
          <SolutionOutlined />
        </template>
        <span>详细</span>
      </a-button>
      <a-button type="dashed" danger ghost :disabled="!(state.selectedRowKeys.length > 0)" @click="deleteUser(state.selectedRowKeys as string[])">
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
      <DetailModal :modal-open="detailModal" :data="userDetailData" @update:close:modal="closeModalFunc" />
      <InsertModal :modal-open="insertModal" @update:close:modal="insertModal = false" />
      <a-table
        :columns="columns"
        :data-source="tabData"
        :loading="loading"
        :row-selection="{ selectedRowKeys: state.selectedRowKeys, onChange: onSelectChange }"
        :row-key="record => record.id"
        size="small"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'avatar'">
            <!-- 头像 -->
            <a-avatar shape="square" size="large" :src="record.avatar" alt="无法显示头像" />
          </template>
          <template v-if="column.dataIndex === 'isDisable'">
            <a-switch v-model:checked="record.isDisable" @change="statusBtn(record.id, record.isDisable)" />
          </template>
          <template v-if="column.dataIndex === 'registerType'">
            <a-tag color="blue">
              {{ record.registerType }}
            </a-tag>
          </template>
          <template v-if="column.key === 'operation'">
            <a-button type="link" style="padding: 0;" @click="userDetailFunc(record.id)">
              <template #icon>
                <SolutionOutlined />
              </template>
              <span style="margin-inline-start:1px">详情</span>
            </a-button>
            <a-button type="link" style="padding: 0;margin-left: 5px" @click="deleteUser([record.id])">
              <template #icon>
                <DeleteOutlined />
              </template>
              <span style="margin-inline-start:1px">删除</span>
            </a-button>
            <a-button type="link" style="padding: 0;margin-left: 5px" @click="$router.push({ path: '/user/role', query: { id: record.id, name: record.roleName } })">
              <template #icon>
                <KeyOutlined />
              </template>
              <span style="margin-inline-start:1px">分配角色</span>
            </a-button>
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
