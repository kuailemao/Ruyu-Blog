import type { AxiosError, AxiosInstance, AxiosRequestConfig, AxiosResponse, InternalAxiosRequestConfig } from 'axios'
import axios from 'axios'
import { AxiosLoading } from './loading'
import { STORAGE_AUTHORIZE_KEY, useAuthorization } from '~/composables/authorization'
import { ContentTypeEnum, RequestEnum } from '~#/http-enum'
import router from '~/router'

export interface ResponseBody<T = any> {
  code: number
  data?: T
  msg: string
}

export interface RequestConfigExtra {
  token?: boolean
  customDev?: boolean
  loading?: boolean
}

const instance: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_APP_BASE_API ?? '/',
  timeout: 60000,
  headers: { 'Content-Type': ContentTypeEnum.JSON },
})
const axiosLoading = new AxiosLoading()

// Token 前缀
const TOKEN_PREFIX = 'Bearer '

async function requestHandler(config: InternalAxiosRequestConfig & RequestConfigExtra): Promise<InternalAxiosRequestConfig> {
  const useToken = useAuthorization()
  const notification = useNotification()

  if (useToken.value && config.token !== false) {
    const { token, expires } = JSON.parse(useToken.value)
    if (token && expires && new Date(expires) < new Date()) {
      useToken.value = null
      notification?.warning({
        message: '账号已过期',
        description: '请重新登录，正在为你跳转登陆页...',
        duration: 3,
      })
      router
        .push({
          path: '/login',
          query: {
            redirect: router.currentRoute.value.fullPath,
          },
        })
        .then(() => {
        })
      await Promise.reject(new Error('账号已过期'))
    }

    config.headers.set(STORAGE_AUTHORIZE_KEY, TOKEN_PREFIX + token)
  }

  // 增加多语言的配置
  const { locale } = useI18nLocale()
  config.headers.set('Accept-Language', locale.value ?? 'zh-CN')
  if (config.loading)
    axiosLoading.addLoading()

  return config
}

function responseHandler(response: any): ResponseBody<any> | AxiosResponse<any> | Promise<any> | any {
  const notification = useNotification()
  if (response.data.code === 1002) {
    // 未登录
    useAuthorization().value = null
    notification?.warning({
      message: 'Token不可用',
      description: '请重新登录，正在为你跳转登录页...',
      duration: 3,
    })
    router
      .push({
        path: '/login',
        query: {
          redirect: router.currentRoute.value.fullPath,
        },
      })
      .then(() => {
      })
  }
  return response.data
}

function errorHandler(error: AxiosError): Promise<any> {
  const token = useAuthorization()
  const notification = useNotification()

  if (error.response) {
    const { data, status, statusText } = error.response as AxiosResponse<ResponseBody>
    if (status === 401) {
      notification?.error({
        message: '401',
        description: data?.msg || statusText,
        duration: 3,
      })
      /**
       * 这里处理清空用户信息和token的逻辑，后续扩展
       */
      token.value = null
      router
        .push({
          path: '/login',
          query: {
            redirect: router.currentRoute.value.fullPath,
          },
        })
        .then(() => {
        })
    }
    else if (status === 403) {
      notification?.error({
        message: '403',
        description: data?.msg || statusText,
        duration: 3,
      })
    }
    else if (status === 500) {
      notification?.error({
        message: '500',
        description: data?.msg || statusText,
        duration: 3,
      })
    }
    else {
      notification?.error({
        message: '服务错误',
        description: data?.msg || statusText,
        duration: 3,
      })
    }
  }
  return Promise.reject(error)
}

interface AxiosOptions<T> {
  url: string
  params?: T
  data?: T
}

instance.interceptors.request.use(requestHandler)

instance.interceptors.response.use(responseHandler, errorHandler)

export default instance

function instancePromise<R = any, T = any>(options: AxiosOptions<T> & RequestConfigExtra): Promise<ResponseBody<R>> {
  const { loading } = options
  return new Promise((resolve, reject) => {
    instance.request(options).then((res: any) => {
      if (res.code === 200)
        resolve(res as any)
      else
        reject(res.msg)
    }).catch((e: Error | AxiosError) => {
      reject(e)
    })
      .finally(() => {
        if (loading)
          axiosLoading.closeLoading()
      })
  })
}

export function useGet<R = any, T = any>(url: string, params?: T, config?: AxiosRequestConfig & RequestConfigExtra): Promise<ResponseBody<R>> {
  const options = {
    url,
    params,
    method: RequestEnum.GET,
    ...config,
  }

  return instancePromise<R, T>(options)
}

export function usePost<R = any, T = any>(url: string, data?: T, config?: AxiosRequestConfig & RequestConfigExtra): Promise<ResponseBody<R>> {
  const options = {
    url,
    data,
    method: RequestEnum.POST,
    ...config,
  }
  return instancePromise<R, T>(options)
}

export function usePut<R = any, T = any>(url: string, data?: T, config?: AxiosRequestConfig & RequestConfigExtra): Promise<ResponseBody<R>> {
  const options = {
    url,
    data,
    method: RequestEnum.PUT,
    ...config,
  }
  return instancePromise<R, T>(options)
}

export function useDelete<R = any, T = any>(url: string, data?: T, config?: AxiosRequestConfig & RequestConfigExtra): Promise<ResponseBody<R>> {
  const options = {
    url,
    data,
    method: RequestEnum.DELETE,
    ...config,
  }
  return instancePromise<R, T>(options)
}
