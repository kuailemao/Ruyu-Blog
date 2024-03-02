<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps({
  modalOpen: Boolean,
  modalTitle: String,
  width: String,
})

const emit = defineEmits(['update:modalOpen'])

const open = computed(() => {
  return props.modalOpen
})

function handleClose() {
  emit('update:modalOpen', false)
}

async function handleOpen() {
  emit('update:modalOpen', false)
}
</script>

<template>
  <div>
    <a-modal v-model:open="open" :width="props.width" :title="modalTitle" @ok="handleOpen" @cancel="handleClose">
      <slot />
    </a-modal>
  </div>
</template>

<style scoped lang="scss">

</style>
