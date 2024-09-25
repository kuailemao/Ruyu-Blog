<script setup lang="ts">
import {EditPen, Lock, Message} from "@element-plus/icons-vue";
import {ElMessage} from "element-plus";
import {sendEmail} from "@/apis/email";
import {resetPasswordStepOne, resetPasswordStepTwo} from "@/apis/user";

const router = useRouter();

const active = ref(0)
const coldTime = ref(0)
const formRef = ref()

const form = reactive({
  email: '',
  code: '',
  password: '',
  password_repeat: ''
})

// 验证重复密码
const validatePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== form.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'},
    {min: 6, max: 20, message: '密码的长度必须在 6-20 个字符之间', trigger: ['blur', 'change']}
  ],
  password_repeat: [
    {validator: validatePassword, trigger: ['blur', 'change']}
  ],
  email: [
    {required: true, message: '请输入邮件地址', trigger: 'blur'},
    {type: 'email', message: '请输入合法的电子邮件地址', trigger: ['blur', 'change']}
  ],
  code: [
    {required: true, message: '请输入获取的验证码', trigger: 'blur'},
  ]
}

function askCode() {
  if (isEmailValid) {
    coldTime.value = 60
    sendEmail(form.email, "reset").then(res => {
      if (res.code === 200) {
        ElMessage.success(`验证码已发送到邮箱：${form.email}，请注意查收`)
        setInterval(() => coldTime.value--, 1000)
      } else {
        ElMessage.warning(res.msg)
        coldTime.value = 0
      }
    })
  } else {
    ElMessage.warning('请输入正确的电子邮件')
  }
}

// 判断邮箱是否正确
const isEmailValid = computed(() => /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(form.email))

function confirmReset() {
  formRef.value.validate((valid) => {
    if (valid) {
      resetPasswordStepOne(form).then(res => {
        if (res.code === 200) {
          console.log(res)
          active.value++
        } else {
          ElMessage.warning(res.msg)
        }
      })
    }
  })
}

function doReset() {
  formRef.value.validate((valid) => {
    if (valid) {
      resetPasswordStepTwo(form).then(res => {
        if (res.code === 200) {
          ElMessage.success('密码重置成功，请重新登录')
          router.push('/login')
        } else {
          ElMessage.warning(res.msg)
        }
      })
    }
  })
}

</script>

<template>
  <div style="text-align: center">
    <div style="margin-top: 30px">
      <el-steps align-center :active="active" finish-status="success">
        <el-step title="验证电子邮件"/>
        <el-step title="重写设定密码"/>
      </el-steps>
    </div>
    <!-- 第一步 -->
    <div style="margin: 0 20px" v-if="active === 0">
      <div style="margin-top: 80px">
        <div style="font-size: 25px;font-weight: bold">重置密码</div>
        <div style="font-size: 14px;color: grey;margin-top: 1rem">请输入需要重置密码的电子邮件地址</div>
      </div>
      <div style="margin-top: 50px">
        <el-form :model="form" :rules="rules" ref="formRef">
          <el-form-item prop="email">
            <el-input v-model="form.email" type="email" placeholder="电子邮件地址">
              <template #prefix>
                <el-icon>
                  <Message/>
                </el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="code">
            <el-row :gutter="10" style="width: 100%">
              <el-col :span="17">
                <el-input v-model="form.code" maxlength="6" placeholder="请输入验证码">
                  <template #prefix>
                    <el-icon>
                      <EditPen></EditPen>
                    </el-icon>
                  </template>
                </el-input>
              </el-col>
              <el-col :span="5">
                <el-button type="success" @click="askCode" :disabled="!isEmailValid || coldTime > 0">
                  {{ coldTime > 0 ? `请稍后 ${coldTime} 秒` : '获取验证码' }}
                </el-button>
              </el-col>
            </el-row>
          </el-form-item>
        </el-form>
      </div>
      <div style="margin-top: 80px">
        <el-button @click="confirmReset" type="warning" style="width: 270px" plain>开始重置密码</el-button>
      </div>
    </div>
    <!-- 第二步 -->
    <div style="margin: 0 20px" v-if="active === 1">
      <div>
        <div style="margin-top: 80px">
          <div style="font-size: 25px;font-weight: bold">重置密码</div>
          <div style="font-size: 14px;color: grey;margin-top: 1rem">请填写你的新密码，务必牢记，防止丢失</div>
        </div>
      </div>
      <div style="margin-top: 50px">
        <el-form :model="form" :rules="rules" ref="formRef">
          <el-form-item prop="password">
            <el-input v-model="form.password" maxlength="20" type="password" placeholder="密码">
              <template #prefix>
                <el-icon>
                  <Lock/>
                </el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="password_repeat">
            <el-input v-model="form.password_repeat" maxlength="20" type="password" placeholder="重复密码">
              <template #prefix>
                <el-icon>
                  <Lock/>
                </el-icon>
              </template>
            </el-input>
          </el-form-item>
        </el-form>
      </div>
      <div style="margin-top: 80px">
        <el-button type="danger" style="width: 270px" plain @click="doReset">立即重置密码</el-button>
      </div>
    </div>
    <div style="margin-top: 20px">
      <span style="font-size: 14px;line-height: 15px;color: grey">改变注意?</span>
      <el-link style="translate: 0 -1px" @click="$router.push('/login')">返回登录</el-link>
    </div>
  </div>
</template>

<style scoped>

</style>