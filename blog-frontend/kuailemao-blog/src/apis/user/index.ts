import http from "@/utils/http.ts";
import {AxiosResponse} from "axios";

export interface UserInfo {
    nickname: string;
    username: string;
    avatar: string;
    intro: string;
    registerType: number;
    email: string;
    roles: string[];
    gender: number;
    permissions: string[];
    loginTime: string;
    createTime: string;
}

// 用户登录
export function login(data: any) {
    return http({
        url: '/user/login',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        data: data,
        method: 'post'
    })
}

// 退出登录
export function logout() {
    return http({
        url: '/user/logout',
        method: 'post'
    })
}

// 第三方登录
export function oauthLogin(accessToken: string,type: string,username: string) {
    return http({
        url: '/user/login',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'Login-Type': type,
            'Access-Token': accessToken,
        },
        data: {
            username: username,
            password: accessToken,
        },
        method: 'post'
    })
}

// 获取用户信息
export function getUserInfo():Promise<AxiosResponse<UserInfo>> {
    return http({
        url: '/user/auth/info',
        method: 'get'
    })
}

// 用户注册
export function register(data: any) {
    return http({
        url: '/user/register',
        data: data,
        method: 'post'
    })
}

// 重置密码步骤一
export function resetPasswordStepOne(data: any) {
    return http({
        url: '/user/reset-confirm',
        data: data,
        method: 'post'
    })
}

// 重置密码步骤二
export function resetPasswordStepTwo(data: any) {
    return http({
        url: '/user/reset-password',
        data: data,
        method: 'post'
    })
}

// 修改用户信息
export function updateUserAccount(data: any) {
    return http({
        url: '/user/auth/update',
        data: data,
        method: 'post'
    })
}

// 修改电子邮箱
export function updateEmail(data: any) {
    return http({
        url: '/user/auth/update/email',
        data: data,
        method: 'post'
    })
}

// 修改第三方登录电子邮箱
export function updateThirdEmail(data: any) {
    return http({
        url: '/user/auth/third/update/email',
        data: data,
        method: 'post'
    })
}
