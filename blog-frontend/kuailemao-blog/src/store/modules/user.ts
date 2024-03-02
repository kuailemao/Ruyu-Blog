import {defineStore} from 'pinia'
import {GET_TOKEN} from '@/utils/auth.ts';
import {getUserInfo, UserInfo} from "@/apis/user";

const useUserStore = defineStore('user', () => {
    const token = GET_TOKEN()
    const userInfo = shallowRef<UserInfo>()

    // 获取用户信息
    const getInfo = async () => {
        getUserInfo().then((res: any) => {
            if (res.code === 200) {
                userInfo.value = res.data
            }
        })
    }

    return {
        token,
        userInfo,
        getInfo
    }
})

export default useUserStore;
