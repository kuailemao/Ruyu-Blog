<script setup lang="ts">
import { ReloadOutlined, SearchOutlined } from '@ant-design/icons-vue'
import type { PropType } from 'vue'
import { computed, reactive, ref, watch } from 'vue'
import { message } from 'ant-design-vue'
import { useRoute } from 'vue-router'
import { addRoleUser, queryUserNotRole } from '~/api/user/role-user'

const props = defineProps({
  open: Boolean,
  title: String,
  formData: Object as PropType<any>,
  permissions: Array as PropType<any>,
})
const emit = defineEmits(['update:close'])
const route = useRoute()

const modalColumns: any = [
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
]
const loading = ref(false)
const tabData = ref([])

async function refreshFunc(searchData?: { roleName: string | undefined; roleKey: string | undefined }) {
  loading.value = true
  let newData: any = []
  if (searchData) {
    const { data } = await queryUserNotRole(route.query.id as string, searchData.roleName, searchData.roleKey)
    newData = data
  }
  else {
    const { data } = await queryUserNotRole(route.query.id as string)
    if (data)
      newData = data
  }
  tabData.value = newData
  loading.value = false
}
const formRef = ref()
/**
 * 重置表单
 */
function resetForm() {
  formRef.value.resetFields()
}

const modalOpen = computed(() => props.open)

watch(modalOpen, (value) => {
  if (value)
    refreshFunc()
})

function openBtnFunc() {
  emit('update:close', false)
}

type Key = string | number
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

/**
 * 确定
 */
async function handleOk() {
  // 单个授权
  if (state.selectedRowKeys.length > 0 && route.query.id) {
    const addData = {
      roleId: state.selectedRowKeys,
      userId: [route.query.id],
    }
    const data = await addRoleUser(addData)
    if (data.code === 200)
      message.success('授权成功，请重新登录后生效')
  }
  // 批量授权给多个/单个授权
  if (state.selectedRowKeys.length > 0 && props.permissions && props.permissions.length > 0) {
    const addData = {
      roleId: state.selectedRowKeys,
      userId: props.permissions,
    }
    const data = await addRoleUser(addData)
    if (data.code === 200)
      message.success('批量授权成功，请重新登录后生效')
  }

  emit('update:close', false, state.selectedRowKeys)
  state.selectedRowKeys = []
}

const searchData = reactive({
  roleKey: undefined,
  roleName: undefined,
})

/**
 * 提交
 */
function onFinish() {
  refreshFunc(searchData)
}

/**
 * 失败
 */
function onFinishFailed() {
  message.error('出现问题')
}
</script>

<template>
  <a-modal v-model:open="modalOpen" width="50%" :title="title" @ok="handleOk" @cancel="openBtnFunc">
    <a-form
      ref="formRef"
      style="margin-top: 2rem"
      :model="searchData"
      :colon="false"
      @submit="onFinish"
      @finish-failed="onFinishFailed"
    >
      <div style="display: flex">
        <a-form-item label="角色名称" name="roleName" style="margin-right: 1rem">
          <a-input v-model:value="searchData.roleName" placeholder="请输入角色名称" />
        </a-form-item>
        <a-form-item label="角色字符" name="roleKey" style="margin-right: 2rem">
          <a-input v-model:value="searchData.roleKey" placeholder="请输入角色字符" style="width: 220px" />
        </a-form-item>
        <a-form-item>
          <a-button type="primary" html-type="submit">
            <template #icon>
              <SearchOutlined />
            </template>
            搜索
          </a-button>
          <a-button style="margin-left: 10px" @click="resetForm">
            <template #icon>
              <ReloadOutlined />
            </template>
            重置
          </a-button>
        </a-form-item>
      </div>
    </a-form>
    <a-table
      :columns="modalColumns"
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
      </template>
      <template v-if="props.permissions && props.permissions.length > 0" #footer>
        <span>选中的权限编号：【{{ props.permissions.join(' , ') }}】</span><br>
      </template>
    </a-table>
  </a-modal>
</template>

<style scoped lang="scss">

</style>
