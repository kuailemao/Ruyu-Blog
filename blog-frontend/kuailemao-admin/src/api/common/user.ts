export interface UserInfo {
  id: number | string
  username: string
  nickname: string
  avatar: string
  // 角色字符
  roles?: (string | number)[]
  // 权限字符
  permissions?: (string | number)[]
}

export function getUserInfoApi() {
  return useGet<UserInfo>('/user/auth/info', null)
}
