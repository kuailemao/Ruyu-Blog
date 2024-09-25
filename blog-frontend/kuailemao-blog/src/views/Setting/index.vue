<script setup lang="ts">
import {ElMessage, FormRules, UploadInstance} from 'element-plus'
import {Plus, User, Select, Message, Refresh, Unlock} from '@element-plus/icons-vue'

import type {UploadProps} from 'element-plus'
import useUserStore from "@/store/modules/user.ts";
import {updateEmail, updateThirdEmail, updateUserAccount} from "@/apis/user";
import {sendEmail} from "@/apis/email";


const uploadRef = ref<UploadInstance>()

const accountForm = ref<any>({
  nickname: '',
  gender: undefined,
  intro: '',
  avatar: ''
})

const avatarImg = ref()

const userStore = useUserStore()

const emailForm = reactive({
  email: '',
  code: '',
  password: '',
})

function updateUser() {
  baseFormRef.value.validate((isValid: boolean) => {
    if (isValid) {
      updateUserAccount(accountForm.value).then((resp: any) => {
        if (resp.code == 200) {
          ElMessage.success('信息更新成功')
          userStore.getInfo()
        } else {
          ElMessage.error(resp.data.msg)
        }
      })
    } else ElMessage.warning('请完整填写信息')
  })
}

// 第一次的图片路径
const firstImg = ref('')

const submitUploadAntUpdate = () => {
  if (firstImg.value !== avatarImg.value) {
    uploadRef.value!.submit()
  } else updateUser()
}

const env = import.meta.env;

// 上传头像
const uploadAvatar = env.MODE === 'development' ? '/api/user/auth/upload/avatar' : env.VITE_SERVE + '/api/user/auth/upload/avatar'
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
      emailForm.email = userStore.userInfo.email
    }
  });
})

// 验证用户昵称
const validateUsername = (_: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请输入用户昵称'))
  } else if (!/[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(value)) {
    callback(new Error('用户名不能包含特殊字符，只能是中/英文'))
  } else {
    callback()
  }
}

const baseFormRef = ref()
const emailFormRef = ref()

const nicknameRules = {
  nickname: [
    {validator: validateUsername, trigger: ['blur', 'change']},
    {min: 2, max: 10, message: '用户昵称的长度必须在2-10个字符之间', trigger: ['blur', 'change']}
  ]
}

const emailRules: FormRules = {
  email: [
    {required: true, message: '请输入邮件地址', trigger: 'blur'},
    {type: 'email', message: '请输入合法的电子邮件地址', trigger: ['blur', 'change']}
  ],
  code: [
    {required: true, message: '请输入获取的验证码', trigger: 'blur'},
  ]
}

const centerDialogVisible = ref(false)

function updateEmailFunc(){
  if (emailForm.password === ''){
    ElMessage.warning('密码不能为空')
    return
  }
  updateEmail(emailForm).then((resp: any) => {
    if(resp.code == 200){
      ElMessage.success('邮件地址更新成功')
      emailForm.code = ''
      userStore.getInfo()
      centerDialogVisible.value = false
    }else ElMessage.error(resp.msg)
  })
}

// 更新邮件
function modifyEmail(){
  emailFormRef.value.validate((isValid: boolean) => {
    if (isValid) {
      centerDialogVisible.value = true
    } else ElMessage.warning('请完整填写信息')
  })
}

// 三方登录绑定邮箱
function thirdPartyLoginEmail(){
  emailFormRef.value.validate((isValid: boolean) => {
    if (isValid) {
      emailForm.password = '第三方登录'
      // 发送请求
      updateThirdEmail(emailForm).then((resp: any) => {
        if(resp.code == 200){
          ElMessage.success('邮件地址更新成功')
          emailForm.code = ''
          userStore.getInfo()
        }else ElMessage.error(resp.msg)
      })
    } else ElMessage.warning('请完整填写信息')
  })
}

// 判断邮箱是否正确
const isEmailValid = computed(() => /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(emailForm.email))

// 邮件发送验证码冷却时间
const coldTime = ref(0)

/**
 * 获取验证码
 */
function getEmailCode(){
  if (emailForm.email === userStore.userInfo?.email){
    ElMessage.warning('邮件地址未更改')
    return
  }
  if(isEmailValid){
    coldTime.value = 60
    sendEmail(emailForm.email, 'resetEmail').then((resp: any) => {
      if (resp.code == 200) {
        ElMessage.success(`验证码已发送到邮箱：${emailForm.email}，请注意查收`)
        const intervalId = setInterval(() => {
          if (coldTime.value === 0) {
            clearInterval(intervalId);
          } else {
            coldTime.value--;
          }
        }, 1000)
      } else {
        ElMessage.error(resp.msg)
        coldTime.value = 0
      }
    })
  }
}
</script>

<template>
  <Header/>
  <div class="flex justify-center items-center">
    <el-dialog
        v-model="centerDialogVisible"
        title="帐号安全验证"
        width="500"
        align-center
    >
      <span class="font-bold">你正在进行敏感操作, 继续操作前请验证您的身份</span>
      <div class="mt-6">
        <span class="font-bold">密码验证</span>
        <el-input v-model="emailForm.password" type="password" placeholder="请输入密码"/>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="centerDialogVisible = false">关闭</el-button>
          <el-button type="primary" @click="updateEmailFunc" :icon="Refresh">
            更新邮件
          </el-button>
        </div>
      </template>
    </el-dialog>
    <div class="md:mt-16 mt-10 2xl:w-[100rem] w-full flex md:flex-row flex-col justify-center">
      <div class="md:w-1/2 w-full">
        <div class="bg_card box_show w-full p-5 rounded shadow-slate-300" >
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
                    :model="accountForm"
                    ref="baseFormRef"
                    :rules="nicknameRules"
                >
                  <el-form-item label="用户昵称" prop="nickname">
                    <el-input placeholder="请输入用户昵称" maxlength="10" v-model="accountForm.nickname"/>
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
        <div class="bg_card box_show w-full p-5 mt-5 rounded  shadow-slate-300 mb-28">
          <div>
            <el-icon>
              <Message/>
            </el-icon>
            <span class="font-bold text-xl ml-2">电子邮件设置</span>
          </div>
          <span style="color: gray;font-size: 0.8rem">在这里可以修改或绑定的电子邮箱信息，绑定后可以开启邮箱提醒！</span>
          <el-divider style="margin-top: 0.5rem"/>
          <div class="flex justify-center">
            <div class="w-full mb-5">
              <div class="flex justify-center mx-6">
                <el-form
                    label-position="top"
                    label-width="auto"
                    class="w-full mt-5"
                    :model="emailForm"
                    ref="emailFormRef"
                    :rules="emailRules"
                >
                  <el-form-item label="电子邮件" prop="email">
                    <el-input placeholder="请输入电子邮件" v-model="emailForm.email"/>
                  </el-form-item>
                  <el-form-item prop="code">
                    <div class="flex w-full">
                      <el-input placeholder="请获取验证码" v-model="emailForm.code"/>
                      <el-button type="success" @click="getEmailCode" plain class="ml-2" :disabled="!isEmailValid || coldTime != 0">
                        {{ coldTime > 0 ? `请稍后 ${coldTime} 秒` : '获取验证码' }}
                      </el-button>
                    </div>
                  </el-form-item>
                </el-form>
              </div>
              <template v-if="userStore.userInfo?.registerType === 0" >
                <el-button class="mx-6" :icon="Unlock" type="success" @click="modifyEmail">安全验证</el-button>
              </template>
              <template v-else>
                <el-button class="mx-6" :icon="Message" type="success" @click="thirdPartyLoginEmail">确定</el-button>
              </template>
            </div>
          </div>
        </div>
      </div>
      <div class="md:ml-10 md:w-[20rem] w-full p-5 " style="min-height: 20px">
        <transition name="el-fade-in-linear">
          <div v-if="userStore.userInfo" class="bg_card box_show rounded">
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
          <div v-if="userStore.userInfo" class="mt-5 p-3 bg_card box_show rounded">
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

// 修改 el-dialog 内容区的默认padding
:deep(.el-dialog__body) {
  padding-top: 0;
}

:deep(.el-dialog){
  border-radius: 10px;
}

.bg_card{
  background-color: var(--el-bg-color);
}

.box_show{
  box-shadow: var(--mao-shadow)
}
</style>
