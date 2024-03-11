// 封装axios
import axios, {AxiosError, AxiosInstance, InternalAxiosRequestConfig} from 'axios'
import {ElMessage} from "element-plus"
import NProgress from 'nprogress';
import 'nprogress/nprogress.css';
import {Jwt_Prefix} from "@/const/Jwt";
import {GET_TOKEN} from "@/utils/auth.ts";
import useLoadingStore from "@/store/modules/loading.ts";


// 创建axios实例
const http: AxiosInstance = axios.create({
    baseURL: import.meta.env.VITE_APP_BASE_API ?? '/', // api的base_url
    timeout: 60000, // 请求超时时间
    headers: {
        'Content-Type': 'application/json;charset=UTF-8'
    }
})

let pendingRequestCount = 0; // 初始化请求计数器

// request拦截器
http.interceptors.request.use((config: InternalAxiosRequestConfig) => {
    let url = config?.url;
    if (!(url?.startsWith("https://v1.hitokoto.cn"))){
        // 判断请求前缀
        if (pendingRequestCount === 0) {
            // TODO 绝对应该反过来判断
            // const loadingStore = useLoadingStore();
            // loadingStore.show();
            NProgress.start();
        }
        pendingRequestCount++;
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
        pendingRequestCount--;
        if (pendingRequestCount === 0) {
            // TODO 皮卡丘 Loading
            // const loadingStore = useLoadingStore();
            // loadingStore.hide();
            NProgress.done();
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



