<script setup lang="ts">
import {ElMessage, UploadInstance} from 'element-plus'
import {Plus, User, Select, Message, Refresh} from '@element-plus/icons-vue'

import type {UploadProps} from 'element-plus'
import useUserStore from "@/store/modules/user.ts";
import {updateUserAccount} from "@/apis/user";


const uploadRef = ref<UploadInstance>()

const accountForm = ref<any>({
  nickname: '',
  gender: undefined,
  intro: '',
  avatar: ''
})

const avatarImg = ref()

const userStore = useUserStore()

function updateUser() {
  updateUserAccount(accountForm.value).then((resp: any) => {
    if (resp.code == 200) {
      ElMessage.success('信息更新成功')
      userStore.getInfo()
    } else {
      ElMessage.error(resp.data.msg)
    }
  })
}

// 第一次的图片路径
const firstImg = ref('')

const submitUploadAntUpdate = () => {
  if (firstImg.value !== avatarImg.value) {
    uploadRef.value!.submit()
  } else updateUser()
}

// 上传头像
const uploadAvatar = import.meta.env.VITE_SERVE + '/user/auth/upload/avatar'
// token
const token = localStorage.getItem('Token') || sessionStorage.getItem('Token') || ''

const handleAvatarSuccess: UploadProps['onSuccess'] = (
    response
) => {
  if (response.code !== 200) {
    ElMessage.error('头像上传失败！' + response.msg)
    return
  }
  accountForm.value.avatar = response.data
  updateUser()
  firstImg.value = avatarImg.value
}

const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile) => {
  firstImg.value = avatarImg.value
  if (rawFile.type !== 'image/jpeg' && rawFile.type !== 'image/png') {
    ElMessage.error('头像图片需要jpg或者png类型的图片！！')
    return false
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error('头像图片大小不能超过2MB！')
    return false
  }
  return true
}

const handleChange = (uploadFile: any) => {
  avatarImg.value = URL.createObjectURL(uploadFile.raw)
}

onMounted(() => {
  watchEffect(() => {
    if (userStore.userInfo) {
      accountForm.value = userStore.userInfo
      avatarImg.value = userStore.userInfo.avatar
      firstImg.value = userStore.userInfo.avatar
    }
  });
})
</script>

<template>
  <Header/>
  <div class="flex justify-center items-center">
    <div class="md:mt-16 mt-10 2xl:w-[100rem] w-full flex md:flex-row flex-col justify-center">
      <div class="md:w-1/2 w-full">
        <div class="bg-white w-full p-5 rounded shadow shadow-slate-300">
          <div>
            <el-icon>
              <User/>
            </el-icon>
            <span class="font-bold text-xl ml-2">账号信息设置</span>
          </div>
          <span style="color: gray;font-size: 0.8rem">在这里可以编辑你的个人信息，个人简介等等……</span>
          <el-divider style="margin-top: 0.5rem"/>
          <div class="flex justify-center mx-6">
            <div class="w-full mb-5">
              <div class="flex justify-center">
                <el-upload
                    class="avatar-uploader"
                    :action="uploadAvatar"
                    :show-file-list="false"
                    :on-success="handleAvatarSuccess"
                    :before-upload="beforeAvatarUpload"
                    :on-change="handleChange"
                    :headers="{'Authorization': 'Bearer ' + JSON.parse(token).token}"
                    :auto-upload="false"
                    ref="uploadRef"
                    name="avatarFile"
                >
                  <img v-if="avatarImg" :src="avatarImg" class="avatar" alt="头像" style="border-radius: 50%"/>
                  <el-icon v-else class="avatar-uploader-icon">
                    <Plus/>
                  </el-icon>
                </el-upload>
              </div>
              <div class="flex justify-center">
                <el-form
                    label-position="top"
                    label-width="auto"
                    class="w-full mt-5"
                >
                  <el-form-item label="用户昵称">
                    <el-input placeholder="请输入用户昵称" v-model="accountForm.nickname"/>
                  </el-form-item>
                  <el-form-item label="性别">
                    <el-radio-group v-model="accountForm.gender">
                      <el-radio :label="1">男</el-radio>
                      <el-radio :label="2">女</el-radio>
                      <el-radio :label="0">保密</el-radio>
                    </el-radio-group>
                  </el-form-item>
                  <el-form-item label="个人简介">
                    <el-input type="textarea" placeholder="请输入个人简介" v-model="accountForm.intro"/>
                  </el-form-item>
                </el-form>
              </div>
              <el-button type="success" :icon="Select" @click="submitUploadAntUpdate">更新信息</el-button>
            </div>
          </div>
        </div>
        <div class="bg-white w-full p-5 mt-5 rounded shadow shadow-slate-300 mb-28">
          <div>
            <el-icon>
              <Message/>
            </el-icon>
            <span class="font-bold text-xl ml-2">电子邮件设置</span>
          </div>
          <span style="color: gray;font-size: 0.8rem">在这里可以修改绑定的电子邮箱信息</span>
          <el-divider style="margin-top: 0.5rem"/>
          <div class="flex justify-center">
            <div class="w-full mb-5" v-if="userStore.userInfo?.registerType === 0">
              <div class="flex justify-center mx-6">
                <el-form
                    label-position="top"
                    label-width="auto"
                    class="w-full mt-5"
                >
                  <el-form-item label="电子邮件">
                    <el-input placeholder="请输入电子邮件"/>
                  </el-form-item>
                  <el-form-item>
                    <div class="flex w-full">
                      <el-input placeholder="请获取验证码"/>
                      <el-button type="success" plain class="ml-2">获取验证码</el-button>
                    </div>
                  </el-form-item>
                </el-form>
              </div>
              <el-button class="ml-6" type="success" :icon="Refresh">更新信息</el-button>
            </div>
            <div v-else>
              <span>您是第三方的注册方式，无法修改哦！！</span>
            </div>
          </div>
        </div>
      </div>
      <div class="md:ml-10 md:w-[20rem] w-full p-5 " style="min-height: 20px;position: sticky;top: 20px">
        <transition name="el-fade-in-linear">
        <div v-if="userStore.userInfo" class="bg-white rounded" style="border: 1px solid #dcdfe6">
          <div style="text-align: center;padding: 15px 15px 10px 15px">
            <el-avatar :size="70" :src="userStore.userInfo?.avatar"/>
            <div style="font-weight: bold">
              你好，{{ userStore.userInfo?.nickname }}
            </div>
            <el-divider style="margin: 10px 0"/>
            <div style="font-size: 14px;color: grey;padding: 10px">
              {{ userStore.userInfo?.intro || '这个用户很懒，没有填写个人简介~' }}
            </div>
          </div>
        </div>
        </transition>
        <transition name="el-fade-in-linear">
          <div v-if="userStore.userInfo" class="mt-5 p-3 bg-white rounded" style="border: 1px solid #dcdfe6">
            <div class="text-gray-400 font-bold">
              欢迎加入Ruyu个人博客！
            </div>
            <div>注册时间：{{ userStore.userInfo?.createTime }}</div>
            <div>登录时间：{{ userStore.userInfo?.loginTime }}</div>
          </div>
        </transition>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 140px;
  border-radius: 50%;
  height: 140px;
  text-align: center;
}

.avatar-uploader :hover {
  border-color: var(--el-color-primary);
}

.avatar-uploader-icon {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

</style>
