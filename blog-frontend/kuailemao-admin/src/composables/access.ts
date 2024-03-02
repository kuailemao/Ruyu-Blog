import { toArray } from '@v-c/utils'
import type { AccessEnum } from '~@/utils/constant'

export function useAccess() {
  const userStore = useUserStore()
  const roles = computed(() => userStore.roles)
  const permissions = computed(() => userStore.permissions)
  const hasRole = (roles: (string | number)[] | string | number | AccessEnum) => {
    const accessRoles = userStore.roles
    const roleArr = toArray(roles).flat(1)
    return roleArr.some(role => accessRoles?.includes(role))
  }
  const hasPermi = (roles: (string | number)[] | string | number | AccessEnum) => {
    const accessPermissions = userStore.permissions
    const roleArr = toArray(roles).flat(1)
    return roleArr.some(role => accessPermissions?.includes(role))
  }
  return {
    hasRole,
    hasPermi,
    roles,
    permissions,
  }
}
