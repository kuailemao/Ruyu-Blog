<script setup lang="ts">
import {User, Lock} from '@element-plus/icons-vue'
import {reactive, ref} from "vue";
import {login} from "@/apis/user";
import router from "@/router";
import {SET_TOKEN} from "@/utils/auth";
import {ElMessage} from "element-plus";
import useUserStore from "@/store/modules/user.ts";


const formRef = ref();
const env = import.meta.env;

const form = reactive({
  username: '',
  password: '',
  remember: false
})

const rule = {
  username: [
    {required: true, message: '请输入用户名'}
  ],
  password: [
    {required: true, message: '请输入密码'}
  ]
}
const userStore = useUserStore()
function userLogin() {
  formRef.value.validate((valid) => {
    if (valid) {
      login(form).then(res => {
        if (res.code === 200) {
          SET_TOKEN(res.data.token, res.data.expire, form.remember)
          ElMessage.success('登录成功')
          router.push('/')
          userStore.getInfo()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
</script>

<template>
  <div style="text-align: center;margin: 0 20px">
    <div style="margin-top: 150px">
      <div style="font-size: 25px;font-weight: bold">登录</div>
      <div style="font-size: 14px;color: grey;margin-top: 1rem">用户密码使用键式哈希算法加密，请放心注册</div>
    </div>
    <div style="margin-top: 50px">
      <el-form :model="form" :rules="rule" ref="formRef">
        <el-form-item prop="username">
          <el-input v-model="form.username" maxlength="20" type="text" placeholder="用户名/邮箱">
            <template #prefix>
              <el-icon>
                <User/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" maxlength="20" placeholder="密码" @keyup.enter="userLogin()">
            <template #prefix>
              <el-icon>
                <Lock/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-row>
          <el-col :span="12" style="text-align: left">
            <el-form-item prop="remember">
              <el-checkbox v-model="form.remember" label="记住我"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="12" style="text-align: right">
            <el-link @click="$router.push('/reset')">忘记密码</el-link>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div style="margin-top: 30px">
      <el-button style="width: 270px" type="success" plain @click="userLogin()">立即登录</el-button>
    </div>
    <el-divider>
      <span style="font-size: 13px;color: grey">没有账号</span>
    </el-divider>
    <div>
      <el-button @click="$router.push('/register')" style="width: 270px" type="danger" plain>立即注册</el-button>
    </div>
    <el-divider>
      <span style="font-size: 13px;color: grey">其他方式</span>
    </el-divider>
    <div>
      <div class="other_login">
        <div>
          <el-link :href="env.MODE === 'development' ? env.VITE_SERVE + '/oauth/gitee/render' : env.VITE_SERVE + '/api/oauth/gitee/render'">
            <svg-icon name="gitee" width="20px" height="20px" color="#4E86F1"/>
          </el-link>
        </div>
        <div style="margin-left: 1rem">
          <el-link :href="env.MODE === 'development' ? env.VITE_SERVE + '/oauth/github/render' : env.VITE_SERVE + '/api/oauth/github/render'">
            <svg-icon name="github" width="20px" height="20px" color="#4E86F1"/>
          </el-link>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.other_login{
  display: flex;
  justify-content: center;
  div:hover{
    cursor: pointer;
  }
}
</style>