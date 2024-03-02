import type { App, Directive } from 'vue'

export const accessDirectiveHasRole: Directive = (el, binding) => {
  const { hasRole } = useAccess()
  if (!hasRole(binding.value))
    el.parentNode?.removeChild(el)
}

export const accessDirectiveHasPermi: Directive = (el, binding) => {
  const { hasPermi } = useAccess()
  if (!hasPermi(binding.value))
    el.parentNode?.removeChild(el)
}
export function setupAccessDirectiveHasRole(app: App) {
  app.directive('hasRole', accessDirectiveHasRole)
}

export function setupAccessDirectiveHasPermi(app: App) {
  app.directive('hasPermi', accessDirectiveHasPermi)
}
