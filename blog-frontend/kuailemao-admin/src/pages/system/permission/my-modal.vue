<script setup lang="ts">
import type { PropType } from 'vue'
import { watch } from 'vue'
import type { SelectProps } from 'ant-design-vue'
import { message } from 'ant-design-vue'
import { getMenusApi } from '~/api/common/menu.ts'
import { addPermission, updatePermission } from '~/api/permission'

const props = defineProps({
  open: Boolean,
  title: String,
  formData: Object as PropType<any>,
})

const emit = defineEmits(['update:close', 'update:refresh-func'])

const modalOpen = computed(() => props.open)

const options = ref<SelectProps['options']>([])

watch(modalOpen, async () => {
  const { data } = await getMenusApi(1)
  if (data && data.length > 0) {
    options.value = data.map((item: any) => {
      return {
        value: item.id as number,
        label: item.title as string,
      }
    })
  }
})

const formData = computed(() => props.formData)

function handleClose() {
  emit('update:close')
}

async function handleOpen() {
  if (formData.value.id && formData.value.id > 0) {
    const data = await updatePermission(formData.value)
    if (data.code === 200) {
      message.success('修改成功')
      emit('update:refresh-func')
    }
  }
  else {
    const data = await addPermission(formData.value)
    if (data.code === 200) {
      message.success('新增成功')
      emit('update:refresh-func')
    }
  }
  emit('update:close')
}

function filterOption(input: string, option: any) {
  return Array.from(input).some(char => Array.from(option.label).includes(char))
}
</script>

<template>
  <div>
    <a-modal v-model:open="modalOpen" :title="props.title" @ok="handleOpen" @cancel="handleClose">
      <a-form>
        <a-form-item label="权限字符" name="name" style="margin-top: 2rem">
          <a-input v-model:value="formData.permissionKey" placeholder="请输入权限字符" />
        </a-form-item>
        <a-form-item label="权限菜单">
          <a-select
            v-model:value="formData.permissionMenuId"
            show-search
            placeholder="选择所在的菜单"
            style="width: 200px"
            :options="options"
            :filter-option="filterOption"
          />
        </a-form-item>
        <a-form-item label="权限描述" style="margin-top: 2rem">
          <a-textarea
            v-model:value="formData.permissionDesc" placeholder="权限字符该权限的描述" show-count
            :maxlength="100"
          />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<style scoped lang="scss">
:deep(.ant-form-item-label) {
  width: 70px;
}
</style>
