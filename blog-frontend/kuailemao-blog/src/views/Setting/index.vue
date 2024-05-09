<script setup lang="ts">
import {ElMessage} from 'element-plus'
import {Plus, User, Select, Message, Refresh} from '@element-plus/icons-vue'

import type {UploadProps} from 'element-plus'

const radio = ref('保密')

const imageUrl = ref('')

const handleAvatarSuccess: UploadProps['onSuccess'] = (
    response,
    uploadFile
) => {
  imageUrl.value = URL.createObjectURL(uploadFile.raw!)
}

const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile) => {
  if (rawFile.type !== 'image/jpeg') {
    ElMessage.error('Avatar picture must be JPG format!')
    return false
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error('Avatar picture size can not exceed 2MB!')
    return false
  }
  return true
}
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
                    action="https://run.mocky.io/v3/9d059bf9-4660-45f2-925d-ce80ad6c4d15"
                    :show-file-list="false"
                    :on-success="handleAvatarSuccess"
                    :before-upload="beforeAvatarUpload"
                >
                  <img v-if="imageUrl" :src="imageUrl" class="avatar" alt="头像"/>
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
                    <el-input placeholder="请输入用户昵称"/>
                  </el-form-item>
                  <el-form-item label="性别">
                    <el-radio-group v-model="radio">
                      <el-radio label="男">男</el-radio>
                      <el-radio label="女">女</el-radio>
                      <el-radio label="保密">保密</el-radio>
                    </el-radio-group>
                  </el-form-item>
                  <el-form-item label="个人简介">
                    <el-input type="textarea" placeholder="请输入个人简介"/>
                  </el-form-item>
                </el-form>
              </div>
              <el-button type="success" :icon="Select">更新信息</el-button>
            </div>
          </div>
        </div>
        <div class="bg-white w-full p-5 mt-5 rounded shadow shadow-slate-300">
          <div>
            <el-icon>
              <Message/>
            </el-icon>
            <span class="font-bold text-xl ml-2">电子邮件设置</span>
          </div>
          <span style="color: gray;font-size: 0.8rem">在这里可以编辑你的个人信息，个人简介等等……</span>
          <el-divider style="margin-top: 0.5rem"/>
          <div class="flex justify-center">
            <div class="w-full mb-5">
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
          </div>
        </div>
      </div>
      <div class="md:ml-10 md:w-[20rem] w-full p-5 " style="min-height: 20px;position: sticky;top: 20px">
        <div class="bg-white rounded" style="border: 1px solid #dcdfe6">
          <div style="text-align: center;padding: 15px 15px 10px 15px">
            <el-avatar :size="70" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"/>
            <div style="font-weight: bold">
              你好，mao
            </div>
            <el-divider style="margin: 10px 0"/>
            <div style="font-size: 14px;color: grey;padding: 10px">
              {{ '这个用户很懒，没有填写个人简介~' }}
            </div>
          </div>
        </div>
        <div class="mt-5 p-3 bg-white rounded" style="border: 1px solid #dcdfe6">
          <div>账号注册时间：1970/1/1 08:00:00</div>
          <div style="color: grey">
            欢迎加入个人博客！
          </div>
        </div>
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
