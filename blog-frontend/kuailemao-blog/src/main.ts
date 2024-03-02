import { createApp } from 'vue'
import App from '@/App.vue'
// svg插件需要配置代码
import 'virtual:svg-icons-register'
// 引入自定义插件对象：注册整个项目全局组件
import gloablComponent from '@/components'
import 'element-plus/dist/index.css'
import router from '@/router'
// 引入模板的全局的样式
import '@/styles/index.scss'
// 黑暗模式
import 'element-plus/theme-chalk/dark/css-vars.css'
// pinia
import pinia from '@/store'
// 全局指令
import vSlideIn from '@/directives/vSlideIn.ts'

// 获取应用实例对象
const app = createApp(App)
app.directive('slide-in',vSlideIn)
// 安装自定义插件
app.use(gloablComponent)
// 安装路由
app.use(router)
// 设置pinia
app.use(pinia)

app.mount('#app')
