<script setup lang="ts">
import type { PropType } from 'vue'
import { ref, watch } from 'vue'
import type { TreeProps } from 'ant-design-vue'
import { message } from 'ant-design-vue'
import { roleInsert, roleUpdate } from '~/api/role'

const props = defineProps({
  open: Boolean,
  title: String,
  treeData: Array as PropType<TreeProps['treeData']>,
  formData: Object as PropType<any>,
})

const emit = defineEmits(['update:close'])

const modalOpen = computed(() => props.open)

const treeDate = computed(() => convertDataStructure(props.treeData))

// 更改数据结构
function convertDataStructure(data: any) {
  return data.map((item: any) => {
    let newItem

    newItem = newItem = {
      key: item.id as number,
      title: item.title as string,
      children: [] as any[],
    }

    if (item.children && item.children.length > 0)
      newItem.children = convertDataStructure(item.children)

    return newItem
  })
}
const formData = computed(() => props.formData)
// 展开指定节点
const expandedKeys = ref<number[]>([])
// 选中的节点
const checkedKeys = ref([]) as any
// 是否子父联动
const checkStrictly = ref(true)
const checked = ref({
  a1: undefined,
  a2: undefined,
  a3: undefined,
})

function handleClose() {
  checked.value = { a1: undefined, a2: undefined, a3: undefined }
  emit('update:close')
}

async function handleOpen() {
  let merged: any = {}
  if (checkedKeys.value.checked?.length > 0)
    merged = Object.assign({}, formData.value, { menuIds: checkedKeys.value.checked })

  else merged = Object.assign({}, formData.value, { menuIds: checkedKeys.value })
  if (merged && merged.id) {
    const data = await roleUpdate(merged)
    if (data.code === 200)
      message.success('修改成功')
  }
  else {
    const data = await roleInsert(merged)
    if (data.code === 200)
      message.success('添加成功')
  }

  emit('update:close')
}

function checkedFunc(flag: string) {
  if (flag === 'a1' && checked.value.a1)
    expandedKeys.value = treeDate.value.map((item: any) => item.key)

  else if (flag === 'a1' && !checked.value.a1)
    expandedKeys.value = []

  if (flag === 'a2' && checked.value.a2) {
    checkedKeys.value = []
    selectAllNodes(treeDate.value)
  }
  else if (flag === 'a2' && !checked.value.a2) {
    checkedKeys.value = []
  }

  if (flag === 'a3' && checked.value.a3)
    checkStrictly.value = false
  else if (flag === 'a3' && !checked.value.a3)
    checkStrictly.value = true
}

// 选中所有节点
function selectAllNodes(nodes: any) {
  nodes.forEach((node: any) => {
    checkedKeys.value.push(node.key)
    if (node.children)
      selectAllNodes(node.children)
  })
}

function onExpand(Keys: any) {
  expandedKeys.value = Keys
}

watch(formData, () => {
  if (formData.value.menuIds)
    checkedKeys.value = formData.value.menuIds
  else
    checkedKeys.value = []
})
</script>

<template>
  <div>
    <a-modal v-model:open="modalOpen" :title="props.title" @ok="handleOpen" @cancel="handleClose">
      <a-form>
        <a-form-item label="角色名称" name="name" style="margin-top: 2rem">
          <a-input v-model:value="formData.roleName" placeholder="请输入角色名称" />
        </a-form-item>
        <a-form-item label="权限字符" name="name">
          <a-input v-model:value="formData.roleKey" placeholder="请输入权限字符" />
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
          <a-input-number id="inputNumber" v-model:value="formData.orderNum" :min="0" :max="10" />
        </a-form-item>
        <a-form-item label="状态" name="status">
          <a-radio-group v-model:value="formData.status">
            <a-radio :value="0">
              启用
            </a-radio>
            <a-radio :value="1">
              禁用
            </a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item>
          <template #label>
            菜单权限
          </template>
          <div>
            <a-checkbox v-model:checked="checked.a1" style="margin-right: 20px" @change="checkedFunc('a1')">
              展开/折叠
            </a-checkbox>
            <a-checkbox v-model:checked="checked.a2" style="margin-right: 20px" @change="checkedFunc('a2')">
              全选/全不选
            </a-checkbox>
            <a-checkbox v-model:checked="checked.a3" @change="checkedFunc('a3')">
              父子联动
            </a-checkbox>
          </div>
        </a-form-item>
        <a-tree
          v-model:expandedKeys="expandedKeys"
          v-model:checkedKeys="checkedKeys"
          :check-strictly="checkStrictly"
          checkable
          block-node
          :tree-data="treeData"
          style="border: 1px solid #E5E6E7;margin-left: 70px"
          @expand="onExpand"
        />
        <a-form-item label="备注" style="margin-top: 2rem">
          <a-textarea v-model:value="formData.remark" show-count :maxlength="100" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<style scoped lang="scss">
:deep(.ant-form-item-label){
  width: 70px;
}
</style>
