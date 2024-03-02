export const STORAGE_AUTHORIZE_KEY = 'Authorization'

// 存储token
export const useAuthorization = createGlobalState(() => useStorage<null | string>(STORAGE_AUTHORIZE_KEY, null))
