<script setup lang="ts">
import type { AccessEnum } from '~/utils/constant'
// 废弃这种权限控制方式
defineProps<{
  access: string | number | (string | number)[] | AccessEnum
}>()
const { hasRole } = useAccess()
</script>

<template>
  <slot v-if="hasRole(access)" />
</template>
