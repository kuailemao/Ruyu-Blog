import { createPinia } from 'pinia'
import { createApp } from 'vue'
import type { App } from 'vue'
import * as Icons from '@ant-design/icons-vue'
import Root from './App.vue'
import { setupI18n } from './locales'
import {
  setupAccessDirectiveHasPermi,
  setupAccessDirectiveHasRole,
  setupLoadingDirective,
} from './directive'
import router from '~/router'
import '~/router/router-guard'
import 'ant-design-vue/dist/reset.css'
import '~/assets/styles/reset.css'
import 'uno.css'

// 自定义 a-button 颜色
import '~/assets/styles/my-a-button.css'

const pinia = createPinia()

// 注册全局图标
function registerIcons(app: App, icons: { [key: string]: any }) {
  for (const i in icons)
    app.component(i, icons[i])
}

async function start() {
  const app: App = createApp(Root)
  app.use(pinia)
  await setupI18n(app)
  setupDirective(app)
  app.use(router)
  app.mount('#app')
  app.config.performance = true

  registerIcons(app, Icons as { [key: string]: any })
}

function setupDirective(app: App) {
  // 注册loading自定义指令
  setupLoadingDirective(app)
  setupAccessDirectiveHasRole(app)
  setupAccessDirectiveHasPermi(app)
}
start().then(r => r)
