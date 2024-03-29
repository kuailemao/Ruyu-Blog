export interface LoginParams {
  username: string
  password: string
  type?: 'account'
}

export interface LoginMobileParams {
  mobile: string
  code: string
  type: 'mobile'
}

export interface LoginResultModel {
  token: string
  expire: string
  code: number
}

export function loginApi(params: LoginParams | LoginMobileParams) {
  return usePost<LoginResultModel, LoginParams | LoginMobileParams>('/user/login', params, {
    // 设置为false的时候不会携带token
    token: false,
    // 开发模式下使用自定义的接口
    // customDev: true,
    // 是否开启全局请求loading
    loading: true,
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
      'X-Client-Type': 'Backend',
    },
  })
}

export function logoutApi() {
  return useGet('/user/logout')
}
