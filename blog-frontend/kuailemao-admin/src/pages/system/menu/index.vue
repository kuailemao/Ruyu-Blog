<script setup lang="ts">
import type {Ref, UnwrapRef} from 'vue'
import {h} from 'vue'
import type {TableColumnsType} from 'ant-design-vue'
import {deleteRouteMenusApi, getRouteMenuByIdApi, getMenusApi, searchMenusApi} from '~/api/common/menu.ts'
import type {MenuData} from '~/layouts/basic-layout/typing.ts'
import {MenuDataItem} from "~/pages/system/menu/type.ts";
import modal from './modal.vue'
import {message} from "ant-design-vue";

defineExpose({h})

// 是否加载中
const isLoading = ref<boolean>(true)
// 打开对话框
const modalOpen = ref<boolean>(false)
// 对话框标题
const modalTitle = ref<string>()

const formState = reactive({
  username: '',
  status: undefined
})

async function onFinish(values: any) {
  if (values.username === '' && values.status === undefined) {
    refreshFunc()
    return
  }
  isLoading.value = true
  const {data} = await searchMenusApi(1, values.username, values.status)
  isLoading.value = false
  menuDataList.value = data as any
}

function onFinishFailed(errorInfo: any) {
  console.log('出现错误:', errorInfo)
}

const columns = ref<TableColumnsType>([
  {
    title: '菜单名称',
    dataIndex: 'title',
    width: '10%',
    align: 'center',
  },
  {
    title: '图标',
    dataIndex: 'icon',
    width: '2%',
    key: 'icon',
    align: 'center',
  },
  {
    title: '排序',
    dataIndex: 'orderNum',
    width: '2%',
    align: 'center',
  },
  {
    title: '组件路径',
    dataIndex: 'component',
    width: '10%',
    align: 'center',
  },
  {
    title: '状态',
    dataIndex: 'isDisable',
    key: 'isDisable',
    width: '5%',
    align: 'center',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: '14%',
    align: 'center',
  },
  {
    title: '操作',
    key: 'operation',
    width: '10%',
    align: 'center',
  },
])

onMounted(() => {
  getMenuList()
})
// 元数据
const menuData = ref()
// 构建好的树形数据
const menuDataList: Ref<UnwrapRef<MenuData>> = ref([])
// 上级菜单数据
const newMenuData = ref()

/**
 * 获取菜单列表
 */
async function getMenuList() {
  const {data} = await getMenusApi(1) as any
  menuData.value = data
  menuDataList.value = buildTree(data)
  newMenuData.value = buildTree(data)
  isLoading.value = false
}

/**
 * 构建树形数据
 * @param data 原始数据
 */
function buildTree(data: MenuDataItem[]) {
  let tree = data.filter(item => item.parentId === null);
  tree.forEach(root => {
    root.key = root.id
    let children = buildChildren(root, data);
    if (children.length > 0) {
      root.children = children;
    }
  });
  return tree;
}

/**
 * 构建子节点
 * @param parent 父节点
 * @param data 原始数据
 */
function buildChildren(parent: MenuDataItem, data: MenuDataItem[]) {
  let children = data.filter(item => item.parentId === parent.id);
  children.forEach(child => {
    child.key = child.id
    let grandChildren = buildChildren(child, data);
    if (grandChildren.length > 0) {
      child.children = grandChildren;
    }
  });
  return children;
}

// 展开的行
const expand = ref({
  expandedRowKeys: <any>([]),
  flag: false
})

function onExpandAll() {
  if (expand.value.flag) {
    expand.value.expandedRowKeys = []
    expand.value.flag = false
  } else {
    expand.value.expandedRowKeys = menuData.value.map(item => item.id)
    expand.value.flag = true
  }
}

/**
 * 展开行
 * @param expanded 是否展开
 * @param record 当前行数据
 */
function handleExpand(expanded: boolean, record: any) {
  if (expanded) {
    expand.value.expandedRowKeys.push(record.key);
  } else {
    // 是否存在
    const index = expand.value.expandedRowKeys.indexOf(record.key);
    if (index > -1) {
      // 删除
      expand.value.expandedRowKeys.splice(index, 1);
    }
  }
}

/**
 * 刷新
 */
function refreshFunc() {
  isLoading.value = true
  getMenuList()
}

const showModal = (flag: string) => {
  if (flag === 'add') {
    modalType.value = 0
    modalTitle.value = '添加菜单'
  } else if (flag === 'update') {
    modalType.value = 1
    modalTitle.value = '修改菜单'
  }

  modalOpen.value = true;
}

/**
 * 取消或确定回调
 * @param e
 */
function handleModalOpenUpdate(e: boolean) {
  modalOpen.value = e
}

/**
 * 添加成功回调
 */
function handleAddSuccess() {
  refreshFunc()
}

const formData = ref()
// 弹窗标识：0：新增 1：修改 2：删除
const modalType: Ref<UnwrapRef<number | null>> = ref(null)

/**
 * 修改
 */
async function updateMenu(id: string) {
  const {data} = await getRouteMenuByIdApi(id);
  if (!data.roleId) data.roleId = undefined
  formData.value = data
  showModal('update')
}

/**
 * 添加
 */
function addMenu(flag: string, parentId?: string) {
  const form = {
    // id
    id: '',
    // 上级菜单
    parentId: '',
    // 菜单标题
    title: '',
    // 显示顺序
    orderNum: 0,
    // 角色id
    roleId: undefined,
    // 菜单图标
    icon: '',
    // 路由类型
    routerType: 0,
    // 组件路径
    component: '',
    // 重定向地址
    redirect: '',
    // 内嵌网页地址
    url: '',
    // 访问地址
    path: '',
    // 跳转模式
    target: '',
    // 固定标签
    affix: 0,
    // 是否保活
    keepAlive: 1,
    // 显示状态
    hideInMenu: 0,
    // 菜单状态
    isDisable: 0,
  }
  if (parentId !== undefined) {
    form.parentId = parentId
  }
  formData.value = form
  showModal(flag)
}

/**
 * 删除
 */
async function deleteMenu(id: string) {
  const data = await deleteRouteMenusApi(id).catch(res => {
    message.warn(res)
  })
  if (data.code === 200) {
    message.success('菜单删除成功')
    refreshFunc()
  }
}

</script>

<template>
  <layout
      @update:refreshFunc="refreshFunc"
      @update:onFinish="onFinish"
      @update:onFinishFailed="onFinishFailed"
      :formState="formState"
  >
    <template #form-items>
      <a-form-item
          label="菜单名称"
          name="username"
      >
        <a-input v-model:value="formState.username" placeholder="请输入菜单名称"/>
      </a-form-item>

      <a-form-item label="状态" name="status" style="width: 240px">
        <a-select v-model:value="formState.status" placeholder="菜单状态">
          <a-select-option :value="0">
            正常
          </a-select-option>
          <a-select-option :value="1">
            停用
          </a-select-option>
        </a-select>
      </a-form-item>
    </template>

    <template #operate-btn>
      <div>
        <a-button @click="addMenu('add')" type="primary">
          <template #icon>
            <PlusOutlined/>
          </template>
          新增
        </a-button>
        <a-button type="dashed" style="margin-bottom: 10px;color: grey" @click="onExpandAll">
          <template #icon>
            <ArrowsAltOutlined/>
          </template>
          展开/折叠
        </a-button>
      </div>
    </template>
    <template #table-content>
      <div>
        <a-table
            :columns="columns"
            :data-source="menuDataList"
            :expanded-row-keys="expand.expandedRowKeys"
            @expand="handleExpand"
            :loading="isLoading"
        >
          <template #bodyCell="{ column,record  }">
            <template v-if="column.key === 'operation'">
              <a-button type="link" style="padding: 0;" @click="updateMenu(record.id)">
                <template #icon>
                  <FileSyncOutlined/>
                </template>
                <span style="margin-inline-start:1px">修改</span>
              </a-button>
              <a-button type="link" style="padding: 0;margin-left: 5px" @click="addMenu('add',record.parentId)">
                <template #icon>
                  <PlusOutlined/>
                </template>
                <span style="margin-inline-start:1px">新增</span>
              </a-button>
              <a-popconfirm
                  title="是否确定删除"
                  ok-text="Yes"
                  cancel-text="No"
                  @confirm="deleteMenu(record.id)"
              >
                <a-button type="link" style="padding: 0;margin-left: 5px">
                  <template #icon>
                    <DeleteOutlined/>
                  </template>
                  <span style="margin-inline-start:1px">删除</span>
                </a-button>
              </a-popconfirm>
            </template>
            <template v-else-if="column.key === 'isDisable'">
              <template v-if="record.isDisable">
                <a-tag color="green">正常</a-tag>
              </template>
              <template v-else>
                <a-tag color="red">停用</a-tag>
              </template>
            </template>
            <template v-else-if="column.key === 'icon'">
              <!-- 图标 -->
              <component :is="record.icon"/>
            </template>
          </template>
        </a-table>
      </div>
      <template v-if="menuDataList.length > 0">
        <modal :modalOpen="modalOpen"
               :modalTitle="modalTitle"
               @update:modalOpen="handleModalOpenUpdate"
               @add:success="handleAddSuccess"
               :menuData="newMenuData"
               :formData="formData"
               :modalType="modalType"
        />
      </template>
    </template>
  </layout>
</template>

<style scoped lang="less">
.middle_btn {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
