<script setup lang="ts">
import { ReloadOutlined, SearchOutlined } from '@ant-design/icons-vue'
import { computed, reactive, ref, watch } from 'vue'
import { message } from 'ant-design-vue'
import { useRoute } from 'vue-router'
import { addUserRole, queryNotRoleUser } from '~/api/role/user-role'

const props = defineProps({
  open: Boolean,
  title: String,
  formData: Object as PropType<any>,
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
]
const loading = ref(false)
const tabData = ref([])

async function refreshFunc(searchData?: { username: string | undefined; email: string | undefined }) {
  loading.value = true
  let newData: any = []
  if (searchData) {
    const { data } = await queryNotRoleUser(route.query.id as string, searchData.username, searchData.email)
    newData = data
  }
  else {
    const { data } = await queryNotRoleUser(route.query.id as string)
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
  if (state.selectedRowKeys.length > 0 && route.query.id) {
    const addData = {
      userId: state.selectedRowKeys,
      roleId: route.query.id,
    }
    const data = await addUserRole(addData)
    if (data.code === 200)
      message.success('授权成功，请重新登录后生效')
  }
  emit('update:close', false, state.selectedRowKeys)
  state.selectedRowKeys = []
}

const searchData = reactive({
  username: undefined,
  email: undefined,
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
        <a-form-item label="用户名称" name="username" style="margin-right: 1rem">
          <a-input v-model:value="searchData.username" placeholder="请输入用户名称" />
        </a-form-item>
        <a-form-item label="电子邮箱" name="email" style="margin-right: 2rem">
          <a-input v-model:value="searchData.email" placeholder="请输入电子邮箱" style="width: 220px" />
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
      </template>
    </a-table>
  </a-modal>
</template>

<style scoped lang="scss">

</style>
