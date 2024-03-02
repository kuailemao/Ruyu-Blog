<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { message } from 'ant-design-vue'
import MyModal from './my-modal.vue'
import batchAuthModal from './role-permission/my-modal.vue'
import {
  deletePermission,
  getPermission,
  permissionList,
  permissionMenuList,
  searchPermissionList,
} from '~/api/permission'

const columns: any = [
  {
    title: '权限编号',
    dataIndex: 'id',
    align: 'center',
  },
  {
    title: '权限字符',
    dataIndex: 'permissionKey',
    align: 'center',
  },
  {
    title: '权限菜单',
    dataIndex: 'menuName',
    align: 'center',
  },
  {
    title: '权限说明',
    dataIndex: 'permissionDesc',
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
  getMenuList()
})

const menuList = ref()

async function getMenuList() {
  const { data } = await permissionMenuList()
  if (data && data.length > 0) {
    menuList.value = data.map((item: any) => {
      return {
        value: item.menuId as number,
        label: item.menuName as string,
      }
    })
  }
}

async function refreshFunc(searchData?: object) {
  loading.value = true
  let newData: any = []
  if (searchData) {
    newData = searchData
  }
  else {
    const { data } = await permissionList()
    if (data)
      newData = data
  }
  tabData.value = newData
  loading.value = false
}

interface formDataType {
  permissionDesc: string | undefined
  permissionKey: string | undefined
  permissionMenuId: string | undefined
}
const formData = reactive({
  permissionDesc: undefined,
  permissionKey: undefined,
  permissionMenuId: undefined,
})

const modalData = ref({
  id: undefined,
  permissionDesc: undefined,
  permissionKey: undefined,
  permissionMenuId: undefined,
})

async function onFinish(values: formDataType) {
  loading.value = true
  const { data } = await searchPermissionList(values.permissionDesc, values.permissionKey, values.permissionMenuId)
  await refreshFunc(data)
}
// 是否修改
const updateDisabled = computed(() => {
  return state.selectedRowKeys.length > 0 && state.selectedRowKeys.length < 2
})
//  是否批量授权
const batchDisabled = computed(() => {
  return state.selectedRowKeys.length > 0
})

const modalInfo = reactive({
  open: false,
  title: '',
})

async function openBtnFunc(value: boolean, flag?: number, id?: number) {
  if (flag === 0) {
    modalData.value = {} as any
    modalInfo.title = '新增权限'
  }
  if (id || (state.selectedRowKeys && flag === 1)) {
    if (id)
      modalData.value.id = id as any
    else
      modalData.value.id = state.selectedRowKeys[0] as any

    const { data } = await getPermission(modalData.value.id as any)
    modalData.value = data
    modalInfo.title = '修改权限'
  }

  modalInfo.open = value
}

function closeModal() {
  modalInfo.open = false
}

const isBatchAuth = ref(false)

function openBatchAuth(flag: boolean) {
  isBatchAuth.value = flag
}

async function deletePermissionBtn(pId: string) {
  const data = await deletePermission(pId)
  if (data.code === 200) {
    message.success('删除成功')
    await refreshFunc()
  }
}
</script>

<template>
  <layout
    :form-state="formData"
    @update:refresh-func="refreshFunc"
    @update:on-finish="onFinish"
  >
    <template #form-items>
      <a-form-item label="权限字符" name="permissionKey">
        <a-input v-model:value="formData.permissionKey" style="width: 250px" placeholder="请输入权限字符" />
      </a-form-item>
      <a-form-item label="权限说明" name="permissionDesc">
        <a-input v-model:value="formData.permissionDesc" style="width: 250px" placeholder="请输入权限说明" />
      </a-form-item>
      <a-form-item label="权限菜单" name="permissionMenuId">
        <a-select
          v-model:value="formData.permissionMenuId"
          placeholder="请选择权限菜单"
          style="width: 200px"
          :options="menuList"
        />
      </a-form-item>
    </template>
    <template #operate-btn>
      <a-button type="primary" style="margin-right: 10px" @click="openBtnFunc(true, 0)">
        <template #icon>
          <PlusOutlined />
        </template>
        新增
      </a-button>
      <a-button class="green" style="margin-right: 10px" :disabled="!updateDisabled" @click="openBtnFunc(true, 1)">
        <template #icon>
          <FileSyncOutlined />
        </template>
        修改
      </a-button>
      <a-button type="primary" ghost style="margin-right: 10px" :disabled="!batchDisabled" @click="isBatchAuth = true">
        <template #icon>
          <SecurityScanOutlined />
        </template>
        批量授权
      </a-button>
    </template>
    <template #table-content>
      <batchAuthModal :open="isBatchAuth" title="批量授权" :permissions="state.selectedRowKeys" @update:close="openBatchAuth" />
      <a-table
        :columns="columns"
        :data-source="tabData"
        :loading="loading"
        :row-selection="{ selectedRowKeys: state.selectedRowKeys, onChange: onSelectChange }"
        :row-key="record => record.id"
        size="small"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'menuName'">
            <a-tag color="blue">
              <template #icon>
                <AlignLeftOutlined />
              </template>
              <span style="margin-left: -2px">{{ record.menuName }}</span>
            </a-tag>
          </template>
          <template v-if="column.key === 'operation'">
            <a-button type="link" style="padding: 0;" @click="openBtnFunc(true, 1, record.id)">
              <template #icon>
                <FileSyncOutlined />
              </template>
              <span style="margin-inline-start:1px">修改</span>
            </a-button>
            <a-popconfirm
              title="是否确定删除"
              ok-text="Yes"
              cancel-text="No"
              @confirm="deletePermissionBtn(record.id)"
            >
              <a-button type="link" style="padding: 0;margin-left: 5px">
                <template #icon>
                  <DeleteOutlined />
                </template>
                <span style="margin-inline-start:1px">删除</span>
              </a-button>
            </a-popconfirm>
            <a-button type="link" style="padding: 0;margin-left: 5px" @click="$router.push({ path: '/permission/authorization', query: { id: record.id } })">
              <template #icon>
                <SmileOutlined />
              </template>
              <span style="margin-inline-start:1px">授权角色</span>
            </a-button>
          </template>
        </template>
      </a-table>
      <MyModal
        :open="modalInfo.open"
        :title="modalInfo.title"
        :form-data="modalData"
        @update:close="closeModal"
        @update:refresh-func="refreshFunc"
      />
    </template>
  </layout>
</template>

<style scoped lang="scss">

</style>
