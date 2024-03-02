import SvgIcon from './SvgIcon/index.vue';
import type { App, Component } from 'vue';


const components: { [name: string]: Component } = {
    SvgIcon
};

// 注册components文件夹内部全部全局组件
export default {
    install(app: App) {
        Object.keys(components).forEach((key: string) => {
            app.component(key, components[key]);
        })
    }
}