<script setup lang="ts">
import { createVNode, onMounted, reactive, ref } from 'vue'
import { useRoute } from 'vue-router'
import { Modal, message } from 'ant-design-vue'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'
import MyModal from './my-modal.vue'
import { useMultiTab } from '~/stores/multi-tab.ts'
import { deleteRoleUser, queryUserRole } from '~/api/user/role-user'

const route = useRoute()
const multiTab = useMultiTab()

function close() {
  multiTab.close(route.fullPath)
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
    title: '角色顺序',
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
    const { data } = await queryUserRole(route.query.id as string)
    if (data)
      newData = data
  }
  tabData.value = newData
  loading.value = false
}

const formData = reactive({
  roleName: undefined,
  roleKey: undefined,
})

async function onFinish(values: { roleName: string; roleKey: string }) {
  loading.value = true
  const { data } = await queryUserRole(route.query.id as string, values.roleName, values.roleKey)
  await refreshFunc(data)
}

const modalOpen = ref(false)

function openBtnFunc(value: boolean, keys?: number[]) {
  if (!value && keys && keys.length > 0)
    refreshFunc()

  modalOpen.value = value
}

async function deleteFunc(roleId: string[]) {
  if (!roleId || roleId.length === 0) {
    message.error('请勾选要取消授权的角色')
    return
  }

  const deleteData = {
    userId: [route.query.id] as string[],
    roleId,
  }
  Modal.confirm({
    title: '注意',
    icon: createVNode(ExclamationCircleOutlined),
    content: `确定取消授权该编号为 【${roleId.join(',')}】 的角色吗？`,
    okText: '确认',
    cancelText: '取消',
    onOk: async () => {
      const data = await deleteRoleUser(deleteData)
      if (data.code === 200) {
        message.success('成功取消授权，请重新登录后生效')
        await refreshFunc()
      }
    },
  })
}
</script>

<template>
  <layout
    :form-state="formData"
    @update:refresh-func="refreshFunc"
    @update:on-finish="onFinish"
  >
    <template #form-items>
      <a-form-item label="角色名称" name="roleName">
        <a-input v-model:value="formData.roleName" style="width: 250px" placeholder="请输入角色名称" />
      </a-form-item>
      <a-form-item label="角色字符" name="roleKey">
        <a-input v-model:value="formData.roleKey" style="width: 250px" placeholder="请输入角色字符" />
      </a-form-item>
    </template>
    <template #operate-btn>
      <a-button type="primary" style="margin-right: 10px" @click="openBtnFunc(true)">
        <template #icon>
          <PlusOutlined />
        </template>
        添加角色
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
          <template v-if="column.key === 'status'">
            <template v-if="record.status === 0">
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
      <MyModal :open="modalOpen" title="授权用户" @update:close="openBtnFunc" />
    </template>
  </layout>
</template>

<style scoped lang="scss">

</style>
