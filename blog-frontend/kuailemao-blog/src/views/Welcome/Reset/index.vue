<script setup lang="ts">
import { computed, reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { EditPen, Lock, Message } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import { sendEmail } from "@/apis/email";
import { resetPasswordStepOne, resetPasswordStepTwo } from "@/apis/user";

const router = useRouter();
const active = ref(0);
const coldTime = ref(0);
const formRef = ref();

const form = reactive({
  email: "",
  code: "",
  password: "",
  password_repeat: "",
});

const PASSWORD_REGEX = /^(?=.*[A-Za-z])(?=.*\d)\S{8,20}$/;
const CODE_REGEX = /^\d{6}$/;

const validatePasswordRepeat = (rule, value, callback) => {
  if (!value) {
    callback(new Error("请再次输入密码"));
  } else if (value !== form.password) {
    callback(new Error("两次输入的密码不一致"));
  } else {
    callback();
  }
};

const rules = {
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    {
      pattern: PASSWORD_REGEX,
      message: "Password must be 8-20 chars with letters and digits, no spaces",
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
  sendEmail(email, "reset")
    .then((res) => {
      if (res.code === 200) {
        ElMessage.success(`验证码已发送到: ${email}`);
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

function confirmReset() {
  formRef.value.validate((valid) => {
    if (!valid) {
      return;
    }
    resetPasswordStepOne({
      email: form.email.trim(),
      code: form.code.trim(),
    }).then((res) => {
      if (res.code === 200) {
        active.value += 1;
      } else {
        ElMessage.warning(res.msg);
      }
    });
  });
}

function doReset() {
  formRef.value.validate((valid) => {
    if (!valid) {
      return;
    }
    resetPasswordStepTwo({
      email: form.email.trim(),
      code: form.code.trim(),
      password: form.password.trim(),
    }).then((res) => {
      if (res.code === 200) {
        ElMessage.success("密码重置成功，请重新登录");
        router.push("/login");
      } else {
        ElMessage.warning(res.msg);
      }
    });
  });
}
</script>

<template>
  <div style="text-align: center">
    <div style="margin-top: 30px">
      <el-steps align-center :active="active" finish-status="success">
        <el-step title="Verify email" />
        <el-step title="Set new password" />
      </el-steps>
    </div>

    <div v-if="active === 0" style="margin: 0 20px">
      <div style="margin-top: 80px">
        <div style="font-size: 25px; font-weight: bold">Reset password</div>
        <div style="font-size: 14px; color: grey; margin-top: 1rem">Please input the email for reset</div>
      </div>
      <div style="margin-top: 50px">
        <el-form ref="formRef" :model="form" :rules="rules">
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
                <el-button type="success" :disabled="!isEmailValid || coldTime > 0" @click="askCode">
                  {{ coldTime > 0 ? `Wait ${coldTime}s` : "Get code" }}
                </el-button>
              </el-col>
            </el-row>
          </el-form-item>
        </el-form>
      </div>
      <div style="margin-top: 80px">
        <el-button type="warning" style="width: 270px" plain @click="confirmReset">Start reset</el-button>
      </div>
    </div>

    <div v-if="active === 1" style="margin: 0 20px">
      <div style="margin-top: 80px">
        <div style="font-size: 25px; font-weight: bold">Reset password</div>
        <div style="font-size: 14px; color: grey; margin-top: 1rem">Please input and confirm your new password</div>
      </div>
      <div style="margin-top: 50px">
        <el-form ref="formRef" :model="form" :rules="rules">
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
        </el-form>
      </div>
      <div style="margin-top: 80px">
        <el-button type="danger" style="width: 270px" plain @click="doReset">Reset now</el-button>
      </div>
    </div>

    <div style="margin-top: 20px">
      <span style="font-size: 14px; line-height: 15px; color: grey">Changed your mind?</span>
      <el-link style="translate: 0 -1px" @click="$router.push('/login')">Back to login</el-link>
    </div>
  </div>
</template>

<style scoped>

</style>
