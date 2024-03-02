<script setup lang="ts">
import type { PropType, Ref, UnwrapRef } from 'vue'
import { ref } from 'vue'
import { message } from 'ant-design-vue'
import type { MenuDataItem, formType } from '~/pages/system/menu/type.ts'
import { addRouteMenusApi, queryRoleList, updateRouteMenusApi } from '~/api/common/menu.ts'
import type { roleListType } from '~/api/common/type.ts'
import { iconsArr1, iconsArr2, iconsArr3 } from '~/pages/system/menu/icons.ts'

const props = defineProps({
  modalOpen: Boolean,
  modalTitle: String,
  menuData: Array as PropType<MenuDataItem[]>,
  formData: Object as PropType<formType> | null,
  modalType: Number,
})

const emit = defineEmits(['update:modalOpen', 'add:success'])

// 类型是否为目录
const isDir = ref(false)

const roles: Ref<UnwrapRef<roleListType[] | undefined>> = ref([])

onMounted(async () => {
  const { data } = await queryRoleList()
  roles.value = convertDataStructure(data, 'role')
})

let viewForm = reactive<formType>({} as formType)

// 监听props.formData变化
watch(() => props.formData, (val) => {
  if (val)
    viewForm = val as any
})

const treeData = computed(() => {
  return convertDataStructure(props.menuData as MenuDataItem[])
})

// 更改数据结构
function convertDataStructure(data: any, type: string = 'menu') {
  return data.map((item: any) => {
    let newItem
    if (type === 'role') {
      newItem = {
        value: item.id as number,
        label: item.roleName as string,
        children: [] as any[],
      }
    }
    else {
      newItem = newItem = {
        value: item.id as number,
        label: item.title as string,
        children: [] as any[],
      }
    }

    if (item.children && item.children.length > 0)
      newItem.children = convertDataStructure(item.children)

    return newItem
  })
}

const open = computed(() => {
  return props.modalOpen
})

const activeKey = ref('3')

// 图标
const icons1 = computed(() => {
  return iconsArr1
})
const icons2 = computed(() => {
  return iconsArr2
})
const icons3 = computed(() => {
  return iconsArr3
})

function addIcon(text: string) {
  viewForm.icon = text
}

function handleClose() {
  emit('update:modalOpen', false)
}

async function handleOpen() {
  if (props.modalType === 0) {
    const data = await addRouteMenusApi(viewForm)
    if (data.code === 200)
      message.success('菜单添加成功')
    // eslint-disable-next-line vue/custom-event-name-casing
    emit('add:success')
  }
  else if (props.modalType === 1) {
    const data = await updateRouteMenusApi(viewForm)
    if (data.code === 200)
      message.success('菜单修改成功')
    isDir.value = false
  }
  emit('update:modalOpen', false)
}
</script>

<template>
  <div>
    <a-modal v-model:open="open" :title="modalTitle" @ok="handleOpen" @cancel="handleClose">
      <a-form>
        <a-form-item style="margin-top: 2rem">
          <template #label>
            <a-tooltip placement="top">
              <template #title>
                <span style="font-size: 0.9em">
                  不选择即表示父级菜单
                </span>
              </template>
              <span>上级菜单</span>
            </a-tooltip>
          </template>
          <a-tree-select
            v-model:value="viewForm.parentId"
            show-search
            style="width: 100%"
            :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
            placeholder="请选择上级菜单"
            allow-clear
            :tree-default-expand-all="false"
            :tree-data="treeData"
            tree-node-filter-prop="label"
          />
        </a-form-item>
        <div style="display: flex">
          <a-form-item label="菜单标题" style="margin-right: 1em">
            <a-input v-model:value="viewForm.title" />
          </a-form-item>
          <a-form-item style="width: 120px">
            <template #label>
              <a-tooltip placement="top">
                <template #title>
                  <span style="font-size: 0.9em">
                    菜单的顺序，由小到大
                  </span>
                </template>
                <span>显示排序</span>
              </a-tooltip>
            </template>
            <a-input-number id="inputNumber" v-model:value="viewForm.orderNum" :min="0" :max="10" />
          </a-form-item>
        </div>
        <div style="display: flex">
          <a-form-item label="菜单图标" style="margin-right: 1em">
            <a-popover title="选择图标" trigger="click">
              <template #content>
                <a-tabs v-model:activeKey="activeKey">
                  <a-tab-pane key="1" tab="线性风格">
                    <div class="icon">
                      <div v-for="(icon, index) of icons1" :key="index">
                        <span @click="addIcon(icon)"> <component :is="icon" style="font-size: 1.3rem" /> </span>
                      </div>
                    </div>
                  </a-tab-pane>
                  <a-tab-pane key="2" tab="实低风格" force-render>
                    <div class="icon">
                      <div v-for="(icon, index) of icons2" :key="index">
                        <span @click="addIcon(icon)"> <component :is="icon" style="font-size: 1.3rem" /> </span>
                      </div>
                    </div>
                  </a-tab-pane>
                  <a-tab-pane key="3" tab="双色风格">
                    <div class="icon">
                      <div v-for="(icon, index) of icons3" :key="index">
                        <span @click="addIcon(icon)"> <component :is="icon" style="font-size: 1.3rem" /> </span>
                      </div>
                    </div>
                  </a-tab-pane>
                </a-tabs>
              </template>
              <a-input v-model:value="viewForm.icon">
                <template #prefix>
                  <template v-if="viewForm.icon !== ''">
                    <component :is="viewForm.icon" style="font-size: 1.3rem" />
                  </template>
                </template>
                {{ viewForm.icon }}
              </a-input>
            </a-popover>
          </a-form-item>
          <a-form-item>
            <template #label>
              <a-tooltip placement="top">
                <template #title>
                  <span style="font-size: 0.9em">角色控制（不填写即表示所有用户可访问）</span>
                </template>
                <span>权限控制</span>
              </a-tooltip>
            </template>
            <a-select
              v-model:value="viewForm.roleId"
              style="width: 120px"
              mode="multiple"
              :options="roles"
              placeholder="访问角色"
            />
          </a-form-item>
        </div>
        <div style="display: flex">
          <a-form-item>
            <template #label>
              <a-tooltip placement="top">
                <template #title>
                  <span style="font-size: 0.9em">内嵌：路由页面中嵌入网页，外链：点击路由跳转（假如百度）</span>
                </template>
                <span>路由类型</span>
              </a-tooltip>
            </template>
            <a-radio-group v-model:value="viewForm.routerType" name="radioGroup">
              <a-radio :value="0" @click="isDir = false">
                普通
              </a-radio>
              <a-radio :value="3" @click="isDir = true">
                目录
              </a-radio>
              <a-radio :value="1">
                内嵌
              </a-radio>
              <a-radio :value="2">
                外链
              </a-radio>
            </a-radio-group>
          </a-form-item>
        </div>
        <template v-if="viewForm.routerType === 0 || viewForm.routerType === 3 || !viewForm.routerType">
          <div class="left_right">
            <a-form-item style="margin-right: 1em">
              <template #label>
                <a-tooltip placement="top">
                  <template #title>
                    <span style="font-size: 0.9em">导航栏的地址(不包括ip与port)</span>
                  </template>
                  <span>访问地址</span>
                </a-tooltip>
              </template>
              <a-input v-model:value="viewForm.path" />
            </a-form-item>
            <template v-if="!isDir">
              <a-form-item style="margin-right: 1em">
                <template #label>
                  <a-tooltip placement="top">
                    <template #title>
                      <span style="font-size: 0.9em">绑定的哪个组件，默认自带的组件类型分别是：Iframe(内嵌)、RouteView(父菜单)</span>
                    </template>
                    <span>组件地址</span>
                  </a-tooltip>
                </template>
                <a-input v-model:value="viewForm.component" />
              </a-form-item>
            </template>
            <template v-if="isDir">
              <a-form-item style="margin-right: 1em">
                <template #label>
                  <a-tooltip placement="top">
                    <template #title>
                      <span style="font-size: 0.9em">访问父级菜单后重定向的地址</span>
                    </template>
                    <span>重定向地址</span>
                  </a-tooltip>
                </template>
                <a-input v-model:value="viewForm.redirect" />
              </a-form-item>
            </template>
          </div>
        </template>
        <template v-if="viewForm.routerType === 1">
          <div class="left_right">
            <a-form-item style="margin-right: 1em">
              <template #label>
                <a-tooltip placement="top">
                  <template #title>
                    <span style="font-size: 0.9em">导航栏的地址(不包括ip与port)</span>
                  </template>
                  <span>访问地址</span>
                </a-tooltip>
              </template>
              <a-input v-model:value="viewForm.path" />
            </a-form-item>
            <a-form-item style="margin-right: 1em">
              <template #label>
                <a-tooltip placement="top">
                  <template #title>
                    <span style="font-size: 0.9em">填写完整访问地址</span>
                  </template>
                  <span>内嵌网页地址</span>
                </a-tooltip>
              </template>
              <a-input v-model:value="viewForm.url" />
            </a-form-item>
          </div>
        </template>
        <template v-if="viewForm.routerType === 2">
          <div class="left_right">
            <a-form-item style="margin-right: 1em">
              <template #label>
                <a-tooltip placement="top">
                  <template #title>
                    <span style="font-size: 0.9em">填写完整跳转地址</span>
                  </template>
                  <span>跳转的url</span>
                </a-tooltip>
              </template>
              <a-input v-model:value="viewForm.path" />
            </a-form-item>
            <a-form-item style="width: 14.5em">
              <template #label>
                <a-tooltip placement="top">
                  <template #title>
                    <span style="font-size: 0.9em">
                      _blank:新窗口打开,
                      _self:当前窗口打开,
                      _parent:父级窗口打开
                    </span>
                  </template>
                  <span>跳转模式</span>
                </a-tooltip>
              </template>
              <a-select v-model:value="viewForm.target" placeholder="请选择跳转模式">
                <a-select-option value="_blank">
                  _blank
                </a-select-option>
                <a-select-option value="_self">
                  _self
                </a-select-option>
                <a-select-option value="_parent">
                  _parent
                </a-select-option>
              </a-select>
            </a-form-item>
          </div>
        </template>
        <div class="left_right">
          <a-form-item style="margin-right: 3.6em">
            <template #label>
              <a-tooltip placement="top">
                <template #title>
                  <span style="font-size: 0.9em">
                    是否固定标签页
                  </span>
                </template>
                <span>固定标签</span>
              </a-tooltip>
            </template>
            <a-radio-group v-model:value="viewForm.affix" name="radioGroup">
              <a-radio :value="0">
                否
              </a-radio>
              <a-radio :value="1">
                是
              </a-radio>
            </a-radio-group>
          </a-form-item>
          <a-form-item>
            <template #label>
              <a-tooltip placement="top">
                <template #title>
                  <span style="font-size: 0.9em">
                    是否显示菜单(隐藏依然可以访问只是不出现在菜单栏中)
                  </span>
                </template>
                <span>显示状态</span>
              </a-tooltip>
            </template>
            <a-radio-group v-model:value="viewForm.hideInMenu" name="radioGroup">
              <a-radio :value="0">
                显示
              </a-radio>
              <a-radio :value="1">
                隐藏
              </a-radio>
            </a-radio-group>
          </a-form-item>
        </div>
        <div class="left_right">
          <a-form-item style="margin-right: 3.6em">
            <template #label>
              <a-tooltip placement="top">
                <template #title>
                  <span style="font-size: 0.9em">
                    给路由添加缓存
                  </span>
                </template>
                <span>是否保活&nbsp;&nbsp;&nbsp;&nbsp;</span>
              </a-tooltip>
            </template>
            <a-radio-group v-model:value="viewForm.keepAlive" name="radioGroup" style="display: flex">
              <a-radio :value="0">
                否
              </a-radio>
              <a-radio :value="1">
                是
              </a-radio>
            </a-radio-group>
          </a-form-item>
          <a-form-item>
            <template #label>
              <a-tooltip placement="top">
                <template #title>
                  <span style="font-size: 0.9em">
                    停用表示不可使用,也不会出现在菜单栏中
                  </span>
                </template>
                <span>菜单状态</span>
              </a-tooltip>
            </template>
            <a-radio-group v-model:value="viewForm.isDisable" name="radioGroup">
              <a-radio :value="0">
                正常
              </a-radio>
              <a-radio :value="1">
                停用
              </a-radio>
            </a-radio-group>
          </a-form-item>
        </div>
      </a-form>
    </a-modal>
  </div>
</template>

<style scoped lang="less">
.left_right {
  display: flex;
  justify-content: space-between;
}

.icon {
  width: 25em;
  height: 10em;
  overflow-y: scroll;
  display: flex;
  flex-wrap: wrap;

  div {
    margin: 0.1rem;
    padding: 0.1rem;
    cursor: pointer;

    &:hover {
      background: #bdbdbd;
    }
  }
}
</style>
