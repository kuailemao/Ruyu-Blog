import {ConfigEnv, defineConfig, loadEnv} from 'vite'
import AutoImport from 'unplugin-auto-import/vite'
import viteCompression from 'vite-plugin-compression';
import Components from 'unplugin-vue-components/vite'
import {ElementPlusResolver} from 'unplugin-vue-components/resolvers'
import { visualizer } from 'rollup-plugin-visualizer'
import vue from '@vitejs/plugin-vue'
// 引入svg需要用到插件
import {createSvgIconsPlugin} from 'vite-plugin-svg-icons'
import path from 'path'
import tailwindcss from 'tailwindcss'
import autoprefixer from 'autoprefixer'

// https://vitejs.dev/config/
export default defineConfig(({ mode }: ConfigEnv) => {
    return {
        plugins: [
            viteCompression({
                verbose: true, // 是否在控制台中输出压缩结果
                disable: false,
                threshold: 1024, // 如果体积大于阈值，将被压缩，单位为b，体积过小时请不要压缩，以免适得其反
                algorithm: 'gzip', // 压缩算法，可选['gzip'，' brotliccompress '，'deflate '，'deflateRaw']
                ext: '.gz',
                // 源文件压缩后是否删除(亲测配置为true后浏览器会出现错误，除非nginx配置index  index.html index.htm;)
                // 具体出现问题参考：https://blog.csdn.net/zzk_01/article/details/125857217
                deleteOriginFile: false
            }),
            vue(),
            createSvgIconsPlugin({
                // 指定需要缓存的图标文件夹
                iconDirs: [path.resolve(process.cwd(), 'src/assets/icons')],
                // 指定symbolId格式
                symbolId: 'icon-[dir]-[name]',
            }),
            AutoImport({
                imports: ['vue', 'vue-router', 'pinia'],
                resolvers: [ElementPlusResolver()],
                dts: "src/types/auto-imports.d.ts",
            }),
            Components({
                resolvers: [ElementPlusResolver()],
                dts: "src/types/components.d.ts",
            }),
            // 打包体积分析
            visualizer({
                open: true,
                filename: 'visualizer.html' //分析图生成的文件名
            })
        ],
        resolve: {
            alias: {
                "@": path.resolve("./src") // 相对路径别名配置，使用 @ 代替 src
            }
        },
        css: {
            preprocessorOptions: {
                scss: {
                    javascriptEnabled: true,
                    additionalData: '@import "./src/styles/variable.scss";',
                },
            },
            postcss: {
                plugins: [
                    tailwindcss,
                    autoprefixer,
                ]
            }
        },
        build: {
            rollupOptions: {
                // 配置打包文件分类输出
                output: {
                    chunkFileNames: 'js/[name]-[hash].js', // 引入文件名的名称
                    entryFileNames: 'js/[name]-[hash].js', // 包的入口文件名称
                    assetFileNames: '[ext]/[name]-[hash].[ext]', // 资源文件像 字体，图片等
                },
                // 最小化拆分包， 将需要分离的包单独的打包出来
                manualChunks(id) {
                    if (id.includes('node_modules')) {
                        return id.toString().split('node_modules/')[1].split('/')[0].toString();
                    }
                }
            }
        },
        server: {
            port: 99,
            host: '0.0.0.0',
            proxy: {
                '/api': {
                    target: `${loadEnv(mode, process.cwd()).VITE_SERVE}`,
                    changeOrigin: true,
                    rewrite: (path) => path.replace(/^\/api/, '')
                },
                '/wapi': {
                    target: `${loadEnv(mode, process.cwd()).VITE_MUSIC_SERVE}`,
                    changeOrigin: true,
                    rewrite: (path) => path.replace(/^\/wapi/, '')
                }
            }
        }
    }
})
