// 封装axios
import axios, {AxiosError, AxiosInstance, InternalAxiosRequestConfig} from 'axios'
import {ElMessage} from "element-plus"
import NProgress from 'nprogress';
import 'nprogress/nprogress.css';
import {Jwt_Prefix} from "@/const/Jwt";
import {GET_TOKEN} from "@/utils/auth.ts";
import useLoadingStore from "@/store/modules/loading.ts";
import {REQUEST_LOADING_PATH} from "@/utils/enum.ts";

// 创建axios实例
const http: AxiosInstance = axios.create({
    baseURL: import.meta.env.VITE_APP_BASE_API ?? '/', // api的base_url
    timeout: 60000, // 请求超时时间
    headers: {
        'Content-Type': 'application/json;charset=UTF-8'
    }
})

const env = import.meta.env
const pathRequestCount = new Map();
const firstRequestPaths = new Set(); // 使用 Set 来记录已经请求过的路径
let loadingShown = false;

// request拦截器
http.interceptors.request.use((config: InternalAxiosRequestConfig) => {
    let url = config?.url;
    if (url?.startsWith(import.meta.env.VITE_MUSIC_BASE_API)){
        config.baseURL = "";
    }
    let matchingPath = REQUEST_LOADING_PATH.find(path => url?.startsWith(path));

    if (!(url?.startsWith(env.VITE_YIYAN_API)) || matchingPath) {
        if (matchingPath && !firstRequestPaths.has(matchingPath)) { // 仅在第一次请求时
            firstRequestPaths.add(matchingPath);
            pathRequestCount.set(matchingPath, (pathRequestCount.get(matchingPath) || 0) + 1);
            if (!loadingShown){
                loadingShown = true;
                const loadingStore = useLoadingStore();
                loadingStore.show();
                NProgress.start();
            }
        } else NProgress.start();
    }


    config.headers['X-Client-Type'] = 'Frontend'
    // 请求头添加token
    if (GET_TOKEN() == null) return config
    config.headers['Authorization'] = Jwt_Prefix + GET_TOKEN()

    return config
}, error => {
    return Promise.reject(error)
})

// response拦截器
http.interceptors.response.use(
    (response) => {
        let url = response.config?.url;
        let matchingPath = REQUEST_LOADING_PATH.find(path => url?.startsWith(path));

        if (matchingPath) {
            pathRequestCount.set(matchingPath, pathRequestCount.get(matchingPath) - 1);

            if (pathRequestCount.get(matchingPath) === 0) { // 所有特定路径的请求都已完成
                loadingShown = false;
                const loadingStore = useLoadingStore();
                loadingStore.hide();
                pathRequestCount.clear(); // 清空整个 Map
                NProgress.done();
            }
        } else NProgress.done();

        if(response.data.code === 1012){
            ElNotification({
                title: '账号已被封禁',
                message: response.data.msg,
                type: 'warning',
            })
        }

        return response.data
    },
    (error: AxiosError) => {
        let message = error.message;
        if (message == "Network Error") {
            message = "后端接口连接异常";
        } else if (message.includes("timeout")) {
            message = "系统接口请求超时";
        } else if (message.includes("Request failed with status code")) {
            message = "系统接口" + message.substring(message.length - 3) + "异常";
        }
        if (!error?.config?.url?.startsWith("https://v1.hitokoto.cn")) {
            ElMessage.error(message)
        }
        return Promise.reject(error.response)
    }
)


export default http



