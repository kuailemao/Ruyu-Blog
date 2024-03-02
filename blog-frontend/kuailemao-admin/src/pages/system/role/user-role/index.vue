<script setup lang="ts">
import { createVNode, onMounted, reactive, ref } from 'vue'
import { useRoute } from 'vue-router'
import { Modal, message } from 'ant-design-vue'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'
import MyModal from './my-modal.vue'
import { useMultiTab } from '~/stores/multi-tab.ts'
import { deleteUserRole, queryRoleUser } from '~/api/role/user-role'

const route = useRoute()
const multiTab = useMultiTab()

function close() {
  multiTab.close(route.fullPath)
}

const columns: any = [
  {
    title: '用户编号',
    dataIndex: 'id',
    align: 'center',
  },
  {
    title: '用户名称',
    dataIndex: 'username',
    align: 'center',
  },
  {
    title: '用户昵称',
    dataIndex: 'nickname',
    align: 'center',
  },
  {
    title: '邮箱',
    dataIndex: 'email',
    align: 'center',
  },
  {
    title: '状态',
    dataIndex: 'isDisable',
    align: 'center',
    key: 'isDisable',
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
const tabData = ref([])

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
    const { data } = await queryRoleUser(route.query.id as string)
    if (data)
      newData = data
  }
  tabData.value = newData
  loading.value = false
}

const formData = reactive({
  username: undefined,
  email: undefined,
})

async function onFinish(values: { username: string; email: string }) {
  loading.value = true
  const { data } = await queryRoleUser(route.query.id as string, values.username, values.email)
  await refreshFunc(data)
}

const modalOpen = ref(false)

function openBtnFunc(value: boolean, keys?: number[]) {
  if (!value && keys && keys.length > 0)
    refreshFunc()

  modalOpen.value = value
}

async function deleteFunc(userId: string[]) {
  if (!userId || userId.length === 0) {
    message.error('请勾选要取消授权的用户')
    return
  }

  const deleteData = {
    roleId: route.query.id as string,
    userId,
  }
  Modal.confirm({
    title: '注意',
    icon: createVNode(ExclamationCircleOutlined),
    content: `确定取消授权该编号为 【${userId.join(',')}】 的用户吗？`,
    okText: '确认',
    cancelText: '取消',
    onOk: async () => {
      const data = await deleteUserRole(deleteData)
      if (data.code === 200) {
        message.success('成功取消授权，请重新登录后生效')
        await refreshFunc()
      }
    },
  })
}

const title = computed(() => {
  return `授权 “${route.query.name}” 角色`
})
</script>

<template>
  <layout
    :form-state="formData"
    @update:refresh-func="refreshFunc"
    @update:on-finish="onFinish"
  >
    <template #form-items>
      <a-form-item label="用户名称" name="username">
        <a-input v-model:value="formData.username" style="width: 250px" placeholder="请输入用户名称" />
      </a-form-item>
      <a-form-item label="用户邮箱" name="email">
        <a-input v-model:value="formData.email" style="width: 250px" placeholder="请输入用户邮箱" />
      </a-form-item>
    </template>
    <template #operate-btn>
      <a-button type="primary" style="margin-right: 10px" @click="openBtnFunc(true)">
        <template #icon>
          <PlusOutlined />
        </template>
        添加用户
      </a-button>
      <a-button type="dashed" danger ghost style="margin-right: 10px" @click="deleteFunc(state.selectedRowKeys as string[])">
        <template #icon>
          <CloseCircleOutlined />
        </template>
        批量取消授权
      </a-button>
      <a-button class="orange" style="margin-right: 10px" @click="close">
        <template #icon>
          <CloseOutlined />
        </template>
        关闭
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
          <template v-if="column.key === 'isDisable'">
            <template v-if="record.isDisable === 0">
              <a-tag color="green">
                正常
              </a-tag>
            </template>
            <template v-else>
              <a-tag color="red">
                停用
              </a-tag>
            </template>
          </template>
          <template v-if="column.key === 'operation'">
            <a-button type="link" @click="deleteFunc([record.id])">
              <template #icon>
                <CloseCircleOutlined />
              </template>
              取消授权
            </a-button>
          </template>
        </template>
      </a-table>
      <MyModal :open="modalOpen" :title="title" @update:close="openBtnFunc" />
    </template>
  </layout>
</template>

<style scoped lang="scss">

</style>
