<script setup lang="ts">
import type { Ref, UnwrapRef } from 'vue'
import { h } from 'vue'
import type { TableColumnsType } from 'ant-design-vue'
import { message } from 'ant-design-vue'
import type { MenuData } from '~/layouts/basic-layout/typing.ts'
import type { MenuDataItem } from '~/pages/system/menu/type.ts'
import { commentList, deleteComment, isCheckComment, searchComment } from '~/api/blog/comment'

defineExpose({ h })

// 是否加载中
const isLoading = ref<boolean>(true)

const formState = reactive({
  commentUserName: '',
  commentContent: '',
  type: undefined,
  isCheck: undefined,
})

// 元数据
const menuData = ref()
// 构建好的树形数据
const commnetDataList: Ref<UnwrapRef<MenuData>> = ref([])

async function onFinish(values: any) {
  isLoading.value = true
  const { data } = await searchComment(values)
  isLoading.value = false
  commnetDataList.value = buildTree(data)
}

function onFinishFailed(errorInfo: any) {
  console.log('出现错误:', errorInfo)
}

const columns = ref<TableColumnsType>([
  {
    title: '编号',
    dataIndex: 'id',
    align: 'center',
  },
  {
    title: '评论类型',
    dataIndex: 'type',
    align: 'center',
  },
  {
    title: '类型编号',
    dataIndex: 'typeId',
    align: 'center',
  },
  {
    title: '评论内容',
    dataIndex: 'commentContent',
    align: 'center',
  },
  {
    title: '评论用户',
    dataIndex: 'commentUserName',
    align: 'center',
  },
  {
    title: '是否通过',
    dataIndex: 'isCheck',
    align: 'center',
  },
  {
    title: '评论时间',
    dataIndex: 'createTime',
    align: 'center',
  },
  {
    title: '操作',
    key: 'operation',
    align: 'center',
  },
])

onMounted(() => {
  getMenuList()
})

/**
 * 获取菜单列表
 */
async function getMenuList() {
  const { data } = await commentList()
  menuData.value = data
  commnetDataList.value = buildTree(data)
  isLoading.value = false
}

/**
 * 构建树形数据
 * @param data 原始数据
 */
function buildTree(data: any[]) {
  const tree = data.filter(item => item.parentId === null)
  tree.forEach((root) => {
    root.key = root.id
    root.isCheck = root.isCheck === 1
    const children = buildChildren(root, data)
    if (children.length > 0)
      root.children = children
  })
  return tree
}

/**
 * 构建子节点
 * @param parent 父节点
 * @param data 原始数据
 */
function buildChildren(parent: MenuDataItem, data: any[]) {
  const children = data.filter(item => item.parentId === parent.id)
  children.forEach((child) => {
    child.key = child.id
    child.isCheck = child.isCheck === 1
    const grandChildren = buildChildren(child, data)
    if (grandChildren.length > 0)
      child.children = grandChildren
  })
  return children
}

// 展开的行
const expand = ref({
  expandedRowKeys: ([]) as any,
  flag: false,
})

function onExpandAll() {
  if (expand.value.flag) {
    expand.value.expandedRowKeys = []
    expand.value.flag = false
  }
  else {
    expand.value.expandedRowKeys = menuData.value.map((item: any) => item.id)
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
    expand.value.expandedRowKeys.push(record.key)
  }
  else {
    // 是否存在
    const index = expand.value.expandedRowKeys.indexOf(record.key)
    if (index > -1) {
      // 删除
      expand.value.expandedRowKeys.splice(index, 1)
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

// 是否通过
function updateIsCheckFunc(id: string, isCheck: any, record: any) {
  record.isCheckloading = true
  isCheck = isCheck ? 1 : 0
  isCheckComment({ id, isCheck }).then((res) => {
    if (res.code === 200) {
      message.success('操作成功，相关子评论将变成同一状态')
      record.isCheckloading = false
      refreshFunc()
    }
  })
}

/**
 * 删除
 */
async function onDelete(id: string) {
  deleteComment(id).then((res) => {
    if (res.code === 200) {
      message.success('删除成功')
      refreshFunc()
    }
  })
}
// 前台域名
const domain = import.meta.env.VITE_APP_DOMAIN_NAME_FRONT
</script>

<template>
  <layout
    :form-state="formState"
    @update:refreshFunc="refreshFunc"
    @update:onFinish="onFinish"
    @update:onFinishFailed="onFinishFailed"
  >
    <template #form-items>
      <a-form-item
        label="评论用户"
        name="commentUserName"
      >
        <a-input v-model:value="formState.commentUserName" placeholder="请输入评论用户" />
      </a-form-item>
      <a-form-item
        label="评论内容"
        name="commentContent"
      >
        <a-input v-model:value="formState.commentContent" placeholder="请输入评论内容" />
      </a-form-item>
      <a-form-item label="评论类型" name="type" style="width: 240px">
        <a-select v-model:value="formState.type" placeholder="评论类型">
          <a-select-option :value="1">
            文章
          </a-select-option>
          <a-select-option :value="2">
            留言
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="是否通过" name="isCheck" style="width: 240px">
        <a-select v-model:value="formState.isCheck" placeholder="是否通过">
          <a-select-option :value="1">
            是
          </a-select-option>
          <a-select-option :value="0">
            否
          </a-select-option>
        </a-select>
      </a-form-item>
    </template>

    <template #operate-btn>
      <div>
        <a-button class="orange" @click="message.warn('别点了，有空再写')">
          <template #icon>
            <VerticalAlignBottomOutlined />
          </template>
          导出
        </a-button>
        <a-button type="dashed" style="margin-bottom: 10px;color: grey" @click="onExpandAll">
          <template #icon>
            <ArrowsAltOutlined />
          </template>
          展开/折叠
        </a-button>
      </div>
    </template>
    <template #table-content>
      <div>
        <a-table
          :columns="columns"
          :data-source="commnetDataList"
          :expanded-row-keys="expand.expandedRowKeys"
          :loading="isLoading"
          @expand="handleExpand"
        >
          <template #bodyCell="{ column, record }">
            <template v-if="column.key === 'operation'">
              <a :href="record.type === 1 ? `${domain}article/${record.typeId}` : `${domain}message/detail/${record.typeId}`" target="_blank">
                <a-button type="link" style="padding: 0;">
                  <template #icon>
                    <LinkOutlined />
                  </template>
                  <span style="margin-inline-start:1px">跳转</span>
                </a-button>
              </a>
              <a-popconfirm
                title="是否确定删除"
                ok-text="Yes"
                cancel-text="No"
                @confirm="onDelete(record.id)"
              >
                <a-button type="link" style="padding: 0">
                  <template #icon>
                    <DeleteOutlined />
                  </template>
                  <span style="margin-left: 3px">删除</span>
                </a-button>
              </a-popconfirm>
            </template>
            <template v-if="column.dataIndex === 'isCheck'">
              <a-switch
                v-model:checked="record.isCheck"
                checked-children="是"
                un-checked-children="否"
                :loading="record.isCheckloading"
                @change="updateIsCheckFunc(record.id, record.isCheck, record)"
              />
            </template>
            <template v-if="column.dataIndex === 'type'">
              <a-tag color="blue">
                {{ record.type === 1 ? '文章' : '留言' }}
              </a-tag>
            </template>
            <template v-if="column.dataIndex === 'typeId'">
              <a-tag>
                {{ record.typeId }}
              </a-tag>
            </template>
            <template v-if="column.dataIndex === 'commentContent'">
              <a-popover title="评论">
                <template #content>
                  {{ record.commentContent }}
                </template>
                {{ record.commentContent.length > 10 ? `${record.commentContent.substring(0, 10)}...` : record.commentContent }}
              </a-popover>
            </template>
            <template v-if="column.dataIndex === 'createTime'">
              <a-tooltip placement="top">
                <template #title>
                  <span>{{ record.createTime }}</span>
                </template>
                {{ record.createTime.split(' ')[0] }}...
              </a-tooltip>
            </template>
          </template>
        </a-table>
      </div>
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
