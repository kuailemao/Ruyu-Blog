<script setup lang="ts">
import { h, ref } from 'vue'
import { ReloadOutlined, SearchOutlined } from '@ant-design/icons-vue'

defineProps({
  formState: Object,
  isList: {
    type: Boolean,
    default: true,
  },
})

const emit = defineEmits(['update:refreshFunc', 'update:onFinish', 'update:onFinishFailed'])

// 是否显示搜索
const isShowForm = ref<boolean>(true)

const formRef = ref()

/**
 * 重置表单
 */
function resetForm() {
  formRef.value.resetFields()
}

/**
 * 刷新方法
 */
function refreshFunc() {
  emit('update:refreshFunc')
}

/**
 * 完成回调
 */
function onFinish(values: any) {
  emit('update:onFinish', values)
}

/**
 * 完成失败回调
 */
function onFinishFailed(errorInfo: any) {
  emit('update:onFinishFailed', errorInfo)
}
</script>

<template>
  <page-container>
    <template #content>
      <template v-if="isShowForm">
        <a-form
          ref="formRef"
          :model="formState"
          layout="inline"
          autocomplete="off"
          @finish="onFinish"
          @finish-failed="onFinishFailed"
        >
          <slot name="form-items" />

          <a-form-item v-if="isList">
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
        </a-form>
      </template>
      <div class="middle_btn">
        <div>
          <slot name="operate-btn" />
        </div>
        <div>
          <template v-if="isList">
            <span>
              <a-tooltip title="隐藏搜索" @click="isShowForm = !isShowForm">
                <a-button shape="circle" :icon="h(SearchOutlined)" />
              </a-tooltip>
            </span>
            <span style="margin-left: 10px">
              <a-tooltip title="刷新" @click="refreshFunc">
                <a-button shape="circle" :icon="h(ReloadOutlined)" />
              </a-tooltip>
            </span>
          </template>
        </div>
      </div>
      <div style="margin-top: 10px">
        <slot name="table-content" />
      </div>
    </template>
  </page-container>
</template>

<style scoped lang="css">
.middle_btn {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

::v-deep(.ant-form-item){
  margin-bottom: 10px;
}

::v-deep(.ant-btn){
  margin-right: 10px;
}
</style>
