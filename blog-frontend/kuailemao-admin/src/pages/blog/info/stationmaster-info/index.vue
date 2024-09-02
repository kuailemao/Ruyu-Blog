<script setup lang="ts">
import {ref} from 'vue'
import {LoadingOutlined, PlusOutlined, UploadOutlined} from '@ant-design/icons-vue'
import type {UploadChangeParam, UploadProps} from 'ant-design-vue'
import {message} from 'ant-design-vue'
import {useAuthorization} from '~/composables/authorization.ts'
import {updateStationmaster, updateWebsiteInfo} from "~/api/blog/webInfo";

const emit = defineEmits(["reset:stationmaster:info"])

const props = defineProps({
  info: {
    type: Object,
  },
})

interface FormDataType {
  webmasterAvatar: string;
  webmasterName: string;
  webmasterCopy: string;
  webmasterProfileBackground: string;
  giteeLink: string;
  githubLink: string;
}

const formData:Partial<FormDataType> = reactive(props.info as object)

// 头像上传
function getBase64(img: Blob, callback: (base64Url: string) => void) {
  const reader = new FileReader()
  reader.addEventListener('load', () => callback(reader.result as string))
  reader.readAsDataURL(img)
}

const avatarFileList = ref([])
const loading = ref<boolean>(false)
const imageUrl = ref<string>()
// 背景上传
const backFileList = ref<UploadProps['fileList']>([])

if(formData.webmasterAvatar && formData.webmasterProfileBackground){
  imageUrl.value = formData.webmasterAvatar as string
  const myUrl = new URL(formData.webmasterProfileBackground as string);
  const fileName = myUrl.pathname.split('/').pop();
  backFileList.value = [{
    thumbUrl: formData.webmasterProfileBackground,
    name: fileName
  }]
}

// 上传图片请求头
const headers = {
  'Authorization': `Bearer ${JSON.parse(<string>useAuthorization().value).token}`,
}

function handleChangeAvatar(info: UploadChangeParam) {
  if (info.file.status === 'uploading') {
    loading.value = true
    return
  }
  if (info.file.status === 'done') {
    // Get this url from response in real world.
    getBase64(info.file.originFileObj, (base64Url: string) => {
      imageUrl.value = base64Url
      loading.value = false
    })
  }
  if (info.file.response && info.file.response.code === 200)
    message.success('头像上传成功')
  else
    message.error(`头像上传失败：${info.file.response.msg}`)

  loading.value = false
}

function handleChangeBack(info: UploadChangeParam) {
  if (info.file.status === 'uploading') {
    return
  }
  if (info.file.response && info.file.response.code === 200)
    message.success('背景上传成功')
  else
    message.error(`背景上传失败：${info.file.response.msg}`)
}

function beforeUpload(file: UploadProps['fileList'][number]) {
  const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/webp'
  if (!isJpgOrPng)
    message.error('文件格式必须是jpg或png或webp')

  const isLt2M = file.size / 1024 / 1024 < 5
  if (!isLt2M)
    message.error('图片必须小于 0.3MB')

  return isJpgOrPng && isLt2M
}

// 修改
function updateStationmasterInfo(){
  updateStationmaster(formData).then(res => {
    if(res.code === 200){
      message.success('保存成功')
    }
  })
}

// 重置
function resetStationmasterInfo(){
  emit('reset:stationmaster:info')
}

const env = import.meta.env
</script>

<template>
  <div class="info">
    <a-form>
      <div class="avatar">
        <a-upload
            v-model:file-list="avatarFileList"
            name="avatar"
            list-type="picture-card"
            class="avatar-uploader"
            :headers="headers"
            :show-upload-list="false"
            :action="env.MODE === 'production' ? env.VITE_APP_BASE_URL + env.VITE_APP_BASE_API + '/websiteInfo/upload/avatar' : env.VITE_APP_BASE_URL + '/websiteInfo/upload/avatar'"
            :before-upload="beforeUpload"
            @change="handleChangeAvatar"
        >
          <img v-if="imageUrl" :src="imageUrl" alt="avatar">
          <div v-else>
            <LoadingOutlined v-if="loading"/>
            <PlusOutlined v-else/>
            <div class="ant-upload-text">
              头像上传
            </div>
          </div>
        </a-upload>
      </div>
      <a-form-item label="名称">
        <a-input v-model:value="formData.webmasterName"/>
      </a-form-item>
      <a-form-item label="文案">
        <a-input v-model:value="formData.webmasterCopy"/>
      </a-form-item>
      <a-form-item label="背景">
        <div>
          <a-upload
              v-model:file-list="backFileList"
              list-type="picture"
              name="background"
              :show-upload-list="{showRemoveIcon: false}"
              :headers="headers"
              :before-upload="beforeUpload"
              :max-count="1"
              :action="env.MODE === 'production' ? env.VITE_APP_BASE_URL + env.VITE_APP_BASE_API + '/websiteInfo/upload/background' : env.VITE_APP_BASE_URL + '/websiteInfo/upload/background'"
              @change="handleChangeBack"
          >
            <div style="display: flex;">
              <a-button>
                <UploadOutlined/>
                背景上传
              </a-button>
            </div>
          </a-upload>
          <div style="font-size: 12px;color: grey">
            图片资源上传与保存按钮无关
          </div>
        </div>
      </a-form-item>
      <a-divider>相关链接</a-divider>
      <a-form-item label="Github">
        <a-input v-model:value="formData.githubLink"/>
      </a-form-item>
      <a-form-item label="Gitee&nbsp;&nbsp;">
        <a-input v-model:value="formData.giteeLink"/>
      </a-form-item>
      <div style="display: flex;justify-content: center">
        <a-button type="primary" @click="updateStationmasterInfo" style="margin-right: 1rem">
          保存
        </a-button>
        <a-button type="default" @click="resetStationmasterInfo">
          重置
        </a-button>
      </div>
    </a-form>
  </div>
</template>

<style scoped lang="scss">
.info {
  width: 50%;

  .avatar {
    width: 100px;
    height: 100px;
    margin: 1rem auto;

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
  }
}
</style>
