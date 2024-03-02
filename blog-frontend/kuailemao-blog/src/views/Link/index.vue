<script setup lang="ts">
import {Watermelon} from '@element-plus/icons-vue'
import {ElMessage, ElNotification, FormInstance, FormRules} from "element-plus";
import {applyLink, linkList} from "@/apis/link";
import {MdPreview} from "md-editor-v3";

const dialogVisible = ref(false)

onMounted(() => {
  linkListFunc()
})

const links = ref()

function linkListFunc() {
  linkList().then(res => {
    if (res.code === 200) {
      links.value = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const form = reactive({
  name: '',
  url: '',
  description: '',
  background: '',
  email: '',
  type: '1'
})

const ruleFormRef = ref<FormInstance>()

const rules = reactive<FormRules<any>>({
  name: [
    {required: true, message: '请填写网站名称', trigger: 'blur'},
    {min: 3, max: 15, message: '长度小3，最大15', trigger: 'blur'},
  ],
  url: [
    {required: true, message: '请填写网站地址', trigger: 'blur'},
    {min: 3, max: 50, message: '长度小3，最大50', trigger: 'blur'},
  ],
  description: [
    {required: true, message: '请填写网站描述', trigger: 'blur'},
    {min: 3, max: 30, message: '长度小3，最大15', trigger: 'blur'},
  ],
  background: [
    {required: true, message: '请填写友链背景图链接', trigger: 'blur'},
    {min: 3, max: 100, message: '长度小3，最大100', trigger: 'blur'},
  ],
  email: [
    {required: true, message: '请填写电子邮件地址', trigger: 'blur'},
    {min: 3, max: 20, message: '长度小3，最大15', trigger: 'blur'},
  ]
})

// 申请链接
function applyLinkFunc() {
  ruleFormRef.value?.validate(async (valid) => {
    if (valid) {
      await applyLink(form).then(res => {
        if (res.code === 200) {
          ElNotification({
            title: '成功',
            showClose: false,
            duration: 6000,
            message: '友链申请提交成功，待通过审核后会通过邮件通知您，请注意查收',
            type: 'success',
          })
          dialogVisible.value = false
        } else {
          ElMessage.error(res.msg)
        }
      })
    } else {
      return false
    }
  })
}

</script>

<template>
  <div>
    <el-dialog
        v-model="dialogVisible"
        title="申请友链"
        width="35%"
        style="border-radius: 15px"
        :close-on-click-modal="false"
        :lock-scroll="false"
    >
      <div class="form">
        <div style="display: flex;align-items: center;margin-bottom: 1rem">
        </div>
        <el-form :model="form" ref="ruleFormRef" :rules="rules">
          <el-form-item prop="name">
            <el-input v-model="form.name" placeholder="请输入网站名称" maxlength="15" show-word-limit>
              <template #prepend>
                网站名称
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="url">
            <el-input v-model="form.url" placeholder="请输入网站地址" maxlength="50" show-word-limit>
              <template #prepend>
                网站地址
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="description">
            <el-input v-model="form.description" placeholder="请输入网站描述" maxlength="30" show-word-limit>
              <template #prepend>
                网站描述
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="background">
            <el-input v-model="form.background" placeholder="请提供http地址" maxlength="100" show-word-limit>
              <template #prepend>
                背景图片
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="email">
            <el-input v-model="form.email" placeholder="填写邮箱地址" maxlength="20" show-word-limit>
              <template #prepend>
                邮箱地址
              </template>
            </el-input>
          </el-form-item>
          <div style="width: 100%;display: flex;flex-direction: row-reverse">
            <el-button type="primary" plain @click="applyLinkFunc">提交申请</el-button>
          </div>
        </el-form>
      </div>
    </el-dialog>
    <Main only-father-container>
      <template #banner>
        <Banner title="友链" subtitle="link"/>
      </template>
      <template #content>
        <div class="content">
          <div class="header">
            <div class="title">友链</div>
            <el-button type="primary" :icon="Watermelon" plain @click="dialogVisible = true" style="margin-right: 1rem">申请友链</el-button>
          </div>
          <el-divider/>
          <div class="title_content">
            <span style="font-size: 1rem;color: grey">欢迎访问友链板块！</span>
            <span>友链板块是一个旨在促进不同系统间相互协作和交流的平台。通过友链板块，您可以：</span>
            <span>1、分享自己系统的介绍和链接。</span>
            <span>2、发现更多的优秀博客网站。</span>
            <span style="font-size: 1rem;color: grey">注意：</span>
            <span>1、友链申请前必须先登录本网站，申请通过后会通过邮件的形式通知你。</span>
            <span>2、点击网站的名称进行友链跳转。</span>
          </div>
          <div class="link">
            <template v-for="link in links" :key="link.id">
              <div v-slide-in class="item">
                <div class="bg" :style="{background: `url(${link.background})`}"></div>
                <div class="content_link">
                  <div>
                    <el-avatar :src="link.avatar"/>
                    <div class="name"><a :href="link.url">{{ link.name }}</a></div>
                  </div>
                  <div class="description">{{ link.description }}</div>
                </div>
              </div>
            </template>
          </div>
        </div>
      </template>
    </Main>
  </div>
</template>

<style scoped lang="scss">
@import "@/styles/mixin.scss";

:deep(.el-dialog__body){
  padding-top: 0;
}

:deep(.el-dialog){
  // 过渡时间
  transition: all 0.3s ease-in-out;
  @media (max-width: 1000px)  {
    width: 60%;
  }

  @media (max-width: 550px)  {
    width: 90%;
  }
}

.content {
  margin-top: 1.5rem;

  .link {
    display: flex;
    flex-wrap: wrap;

    .item {
      margin: 0.5rem;
      width: calc(100% / 3 - 1rem);
      height: 13rem;
      border: #0072ff 1px solid;
      border-radius: $border-radius;
      box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
      @include flex;
      flex-direction: column;
      overflow: hidden;
      transition: all 0.3s ease-in-out;

      @media screen and (max-width: 830px) {
        width: calc(100% / 2 - 1rem);
      }

      @media screen and (max-width: 580px) {
        width: calc(100% - 1rem);

      }

      &:hover {
        .content_link {
          height: 50%;
          background: #0072ff;

          .name {
            color: #fdeeee;
          }

          .description {
            color: #fdeeee;
          }
        }

        .bg {
          filter: blur(2px);
        }
      }

      .bg {
        background-size: cover !important;
        background-position: center !important;
        width: 100%;
        height: 65%;
      }

      .content_link {
        transition: all 0.3s ease-in-out;
        display: flex;
        flex-direction: column;
        align-items: center;
        //background: white;
        height: 35%;
        width: 100%;
        padding: 0.5rem 0;

        div {
          display: flex;
          align-items: center;
          justify-content: center;

          .el-avatar {
            width: 2.5rem;
            height: 2.5rem;
            margin-left: -3rem;
          }

          .name {
            font-size: 1rem;
            font-weight: bold;
            margin-left: 0.5rem;

            a {
              // 去掉a标签样式
              color: inherit;
              text-decoration: none;
              cursor: pointer;
            }
          }
        }

        .description {
          line-height: 1rem;
          width: 15rem;
          font-size: 0.85rem;
          margin-left: 0.5rem;
          margin-top: 0.5rem;
          color: #7C7C7C;
        }
      }
    }
  }

  .title_content {
    font-weight: bold;
    font-size: 0.8rem;
    color: #999;
    display: flex;
    flex-direction: column;
    background: var(--mao-bg-message);
    padding: 0.5rem;
    border-radius: $border-radius;
    margin-bottom: 1rem;

    span {
      margin-bottom: 1rem;
      line-height: 1rem;
    }
  }

  .header {
    display: flex;
    justify-content: space-between;

    .el-button {
      height: 2.5rem;
    }

    .title {
      font-size: 2rem;
    }
  }
}
</style>