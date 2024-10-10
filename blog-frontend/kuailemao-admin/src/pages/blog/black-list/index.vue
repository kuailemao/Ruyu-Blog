<script setup lang="ts">
import type {Ref, UnwrapRef} from 'vue'
import {createVNode} from 'vue'
import {message, Modal} from 'ant-design-vue'
import {ExclamationCircleOutlined} from '@ant-design/icons-vue'
import {deleteCategoryByIds,} from '~/api/blog/category'
import {addCategory} from '~/api/blog/article'
import {addBlackList, blackList, deleteBlackList, updateBlackList} from "~/api/blog/black-list";
import dayjs, {Dayjs} from "dayjs";
import {debounce} from 'lodash-es';
import {userSearch} from "~/api/user";

interface FormState {
  userName: string,
  time: [string, string] | [Dayjs, Dayjs],
  reason: string,
  type: number | undefined,
}

const formState = ref<FormState>({
  userName: '',
  time: ['', ''],
  reason: '',
  type: undefined,
})

const columns: any[] = [
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
const tabData: Ref<UnwrapRef<any[]>> = ref([])

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
  await refreshFunc(submitData)
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

const formData = ref()

function deleteBlackListFunc(ids: string[], type?: number) {
  if (type === 0) {
    Modal.confirm({
      title: '注意',
      icon: createVNode(ExclamationCircleOutlined),
      content: `确定删除编号为 【${ids.join(',')}】 的分类吗？`,
      okText: '确认',
      cancelText: '取消',
      onOk: () => {
        deleteBlackList(ids).then((res) => {
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
  deleteBlackList(ids).then((res) => {
    if (res.code === 200) {
      message.success('删除成功')
      state.selectedRowKeys = []
      refreshFunc()
    }
  })
}

const updateOrInsertModal = ref<{
  id: string | undefined,
  reason: string | undefined,
  expiresTime: Dayjs | string | undefined
}>({
  id: undefined,
  reason: undefined,
  expiresTime: undefined
})

const userState = reactive({
  data: [],
  value: [],
  fetching: false,
  isInsert: false
});

async function updateOrInsertBlackList(id?: string) {
  if (id) {
    tabData.value.map((item: any) => {
      if (id === (item.id)) {
        updateOrInsertModal.value.id = item.id
        updateOrInsertModal.value.reason = item.reason
        updateOrInsertModal.value.expiresTime = dayjs(item.expiresTime)
      }
      return null
    })
    userState.isInsert = false
  } else {
    // 新增
    updateOrInsertModal.value = {
      id: undefined,
      reason: undefined,
      expiresTime: undefined
    }
    userState.isInsert = true
  }
  modalInfo.title = '修改黑名单'
  modalInfo.open = true
}

// 确定
async function modelOk() {
  modalInfo.loading = true
  if (updateOrInsertModal.value.id) {
    let updateData = {
      ...updateOrInsertModal.value,
    }
    updateData.expiresTime = dayjs(updateOrInsertModal.value.expiresTime).format('YYYY-MM-DD HH:mm:ss')
    await updateBlackList(updateData).then((res) => {
      if (res.code === 200) {
        modalInfo.loading = false
        message.success('修改成功')
      }
    }).catch(msg => {
      message.warn(msg)
      modalInfo.loading = false
    })
  } else {
    // TODO 新增黑名单
    let insertData = {
      userIds: [],
      ...updateOrInsertModal.value,
    }
    insertData.expiresTime = dayjs(updateOrInsertModal.value.expiresTime).format('YYYY-MM-DD HH:mm:ss')
    userState.value.map((data: any) => {
      insertData.userIds.push(data.value)
    })
    console.log('添加', insertData)
    await addBlackList(insertData).then((res) => {
      if (res.code === 200) {
        modalInfo.loading = false
        message.success('添加成功')
      }
    }).catch(msg => {
      message.warn(msg)
      modalInfo.loading = false
    })
  }
  await refreshFunc()
}

let lastFetchId = 0;

const fetchUser = debounce(value => {
  lastFetchId += 1;
  const fetchId = lastFetchId;
  userState.data = [];
  userState.fetching = true;
  userSearch({username: value}).then((res) => {
    if (fetchId !== lastFetchId) {
      // for fetch callback order
      return;
    }
    userState.data = res.data.map((user: any) => ({
      label: user.username,
      value: user.id,
    }));
    userState.fetching = false;
  })
}, 300);

watch(userState.value, () => {
  userState.data = [];
  userState.fetching = false;
});

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
          name="userName"
      >
        <a-input v-model:value="formState.userName" placeholder="请输入用户名称" style="width: 150px"/>
      </a-form-item>
      <a-form-item
          label="封禁时间"
          name="time"
      >
        <a-range-picker v-model:value="formState.time" :placeholder="['开始时间', '结束时间']"/>
      </a-form-item>
      <a-form-item
          label="封禁理由"
          name="reason"
      >
        <a-input v-model:value="formState.reason" placeholder="请输入封禁理由" style="width: 200px"/>
      </a-form-item>
      <a-form-item label="封禁类型" name="type" style="margin-right: 1rem">
        <a-select
            style="width: 7em"
            placeholder="封禁类型"
            v-model:value="formState.type"
        >
          <a-select-option :value="1">
            手动封禁
          </a-select-option>
          <a-select-option :value="2">
            自动封禁
          </a-select-option>
        </a-select>
      </a-form-item>
    </template>
    <template #operate-btn>
      <a-button type="primary" @click="updateOrInsertBlackList()">
        <template #icon>
          <PlusOutlined/>
        </template>
        新增
      </a-button>
      <a-button class="green" :disabled="!hasSelected"
                @click="updateOrInsertBlackList(state.selectedRowKeys[0] as string)">
        <template #icon>
          <FileSyncOutlined/>
        </template>
        修改
      </a-button>
      <a-button type="dashed" danger ghost :disabled="!(state.selectedRowKeys.length > 0)"
                @click="deleteBlackListFunc(state.selectedRowKeys as string[], 0)">
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
        <a-form-item label="用户名称" v-show="userState.isInsert">
          <a-select
              v-model:value="userState.value"
              mode="multiple"
              label-in-value
              placeholder="搜索用户"
              style="width: 100%"
              :filter-option="false"
              :not-found-content="userState.fetching ? undefined : null"
              :options="userState.data"
              @search="fetchUser"
          >
            <template v-if="userState.fetching" #notFoundContent>
              <a-spin size="small"/>
            </template>
          </a-select>
        </a-form-item>
        <a-form-item label="解封时间">
          <a-date-picker style="width: 100%" show-time v-model:value="updateOrInsertModal.expiresTime"
                         placeholder="解封时间"/>
        </a-form-item>
        <a-form-item label="封禁理由">
          <a-textarea :showCount="true" v-model:value="updateOrInsertModal.reason" placeholder="请输入封禁理由"
                      style="width: 100%;max-height: 100px"/>
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
            <a-popover title="IP信息">
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
            <a-button type="link" style="padding: 0;" @click="updateOrInsertBlackList(record.id)">
              <template #icon>
                <FileSyncOutlined/>
              </template>
              <span style="margin-inline-start:1px">修改</span>
            </a-button>
            <a-popconfirm
                title="是否确定删除"
                ok-text="Yes"
                cancel-text="No"
                @confirm="deleteBlackListFunc([record.id])"
            >
              <a-button type="link" style="padding: 0;margin-left: 5px">
                <template #icon>
                  <DeleteOutlined/>
                </template>
                <span style="margin-inline-start:1px">删除</span>
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
