import mist from '@mistjs/eslint-config'

export default mist({
  vue: false,
  vueJsx: false,
  typescript: true,
  gitignore: true,
  markdown: true,
  ignores: [
    'types/auto-imports.d.ts',
    'types/components.d.ts',
    'public',
    'tsconfig.*.json',
    'tsconfig.json',
  ],
}, {
  rules: {
    'no-console': 0,
  },
})
