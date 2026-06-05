<script setup lang="ts">
import { computed, reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { EditPen, Lock, Message, User } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import { sendEmail } from "@/apis/email";
import { register } from "@/apis/user";

// 邮件发送冷却时间（单位秒）
const coldTime = ref(0);
const formRef = ref();
const router = useRouter();


// 注册表单数据
const form = reactive({
  username: "",
  password: "",
  password_repeat: "",
  email: "",
  code: "",
});

// 验证规则
const PASSWORD_REGEX = /^(?=.*[A-Za-z])(?=.*\d)\S{8,20}$/;
const CODE_REGEX = /^\d{6}$/;
const USERNAME_REGEX = /^[a-zA-Z0-9\u4e00-\u9fa5]+$/;

// 用户名验证函数
const validateUsername = (rule, value, callback) => {
  // 去除首尾空格进行验证
  const normalized = value?.trim?.() ?? "";
  if (!normalized) {
    callback(new Error("请输入用户名"));
  } else if (!USERNAME_REGEX.test(normalized)) {
    callback(new Error("用户名只允许中文、英文和数字"));
  } else {
    callback();
  }
};

// 密码重复验证函数
const validatePasswordRepeat = (rule, value, callback) => {
  if (!value) {
    callback(new Error("请重复输入密码"));
  } else if (value !== form.password) {
    callback(new Error("两次输入的密码不一致"));
  } else {
    callback();
  }
};

// NOTE：注册表单校验规则对象
//required: true: 标识该项为必填项，message: 校验失败时向用户展示的提示文字
// triger: 'blur' 表示在输入框失去焦点时进行校验，'change' 表示在输入框内容发生变化时进行校验
// validator（自定义校验器）
const rules = {
  username: [
    { validator: validateUsername, trigger: ["blur", "change"] }
  ],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    {
      pattern: PASSWORD_REGEX,
      message: "密码必须包含字母和数字，长度为8-20个字符，且不能包含空格",
      trigger: ["blur", "change"],
    },
  ],
  password_repeat: [{ validator: validatePasswordRepeat, trigger: ["blur", "change"] }],
  email: [
    { required: true, message: "请输入邮箱", trigger: "blur" },
    { type: "email", message: "邮箱格式不正确", trigger: ["blur", "change"] },
  ],
  code: [
    { required: true, message: "请输入验证码", trigger: "blur" },
    { pattern: CODE_REGEX, message: "验证码必须是6个数字", trigger: ["blur", "change"] },
  ],
};

const isEmailValid = computed(() =>
  /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(form.email.trim())
);

function askCode() {
  if (!isEmailValid.value) {
    ElMessage.warning("请输入有效的邮箱地址");
    return;
  }
  if (coldTime.value > 0) {
    return;
  }

  coldTime.value = 60;
  const email = form.email.trim();
  sendEmail(email, "register")
    .then((res) => {
      if (res.code === 200) {
        ElMessage.success(`Verification code has been sent to: ${email}`);
        const intervalId = setInterval(() => {
          if (coldTime.value <= 1) {
            coldTime.value = 0;
            clearInterval(intervalId);
            return;
          }
          coldTime.value -= 1;
        }, 1000);
      } else {
        ElMessage.warning(res.msg);
        coldTime.value = 0;
      }
    })
    .catch(() => {
      coldTime.value = 0;
    });
}

function registerBtn() {
  formRef.value.validate((valid) => {
    if (!valid) {
      ElMessage.warning("请检查输入是否正确");
      return;
    }
    const payload = {
      username: form.username.trim(),
      password: form.password.trim(),
      email: form.email.trim(),
      code: form.code.trim(),
    };
    register(payload)
      .then((res) => {
        if (res.code === 200) {
          ElMessage.success("注册成功，请登录");
          router.push("/login");
        } else {
          ElMessage.warning(res.msg);
        }
      })
      .catch((e) => {
        ElMessage.warning(e?.data?.msg || "注册失败，请稍后再试");
      });
  });
}
</script>

<template>
  <div style="text-align: center; margin: 0 20px">
    <div style="margin-top: 100px">
      <div style="font-size: 25px; font-weight: bold">Create account</div>
      <div style="font-size: 14px; color: grey; margin-top: 1rem">Please fill in the registration form below</div>
    </div>
    <div style="margin-top: 50px">
      <el-form ref="formRef" :model="form" :rules="rules">
        <el-form-item prop="username">
          <el-input v-model="form.username" maxlength="10" type="text" placeholder="Username">
            <template #prefix>
              <el-icon>
                <User />
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" maxlength="20" type="password" placeholder="Password">
            <template #prefix>
              <el-icon>
                <Lock />
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password_repeat">
          <el-input v-model="form.password_repeat" maxlength="20" type="password" placeholder="Repeat password">
            <template #prefix>
              <el-icon>
                <Lock />
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="email">
          <el-input v-model="form.email" type="email" placeholder="Email">
            <template #prefix>
              <el-icon>
                <Message />
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="code">
          <el-row :gutter="10" style="width: 100%">
            <el-col :span="17">
              <el-input v-model="form.code" maxlength="6" placeholder="Verification code">
                <template #prefix>
                  <el-icon>
                    <EditPen />
                  </el-icon>
                </template>
              </el-input>
            </el-col>
            <el-col :span="5">
              <el-button type="success" :disabled="!isEmailValid || coldTime !== 0" @click="askCode">
                {{ coldTime > 0 ? `请等待 ${coldTime}s` : "获取验证码" }}
              </el-button>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
    </div>
    <div style="margin-top: 80px">
      <el-button type="warning" style="width: 270px" plain @click="registerBtn">立即注册</el-button>
    </div>
    <div style="margin-top: 20px">
      <span style="font-size: 14px; line-height: 15px; color: grey">账号已存在</span>
      <el-link style="translate: 0 -1px" @click="$router.push('/login')">立即登录</el-link>
    </div>
  </div>
</template>

<style scoped>

</style>
