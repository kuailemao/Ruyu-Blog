<script setup lang="ts">
import {ref} from 'vue'
import {LoadingOutlined, PlusOutlined, UploadOutlined} from '@ant-design/icons-vue'
import type {UploadProps} from 'ant-design-vue'
import {message} from 'ant-design-vue'
import {updateStationmaster, uploadAckgroundImage, uploadAvatar} from "~/api/blog/webInfo";
import {compressImage} from "~/utils/CompressedImage.ts";

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

/**
 * 将文件转换为 Base64 字符串
 * @param {File} file - 用户上传的文件
 * @returns {Promise<string>} 返回一个 Promise，成功时返回 Base64 字符串
 */
function fileToBase64(file: any) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    // 将文件读取为 Data URL 格式（即 Base64）
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result);
    reader.onerror = error => reject(error);
  });
}

const avatarFileList = ref([])
const loading = ref<boolean>(false)
const imageAvatarUrl = ref<string>()
// 背景上传
const backFileList = ref<UploadProps['fileList']>([])

if(formData.webmasterAvatar && formData.webmasterProfileBackground){
  imageAvatarUrl.value = formData.webmasterAvatar as string
  const myUrl = new URL(formData.webmasterProfileBackground as string);
  const fileName = myUrl.pathname.split('/').pop();
  backFileList.value = [{
    thumbUrl: formData.webmasterProfileBackground,
    name: fileName
  }]
}

async function beforeUploadAvatar(file: UploadProps['fileList'][number]) {
  loading.value = true
  const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/webp'
  if (!isJpgOrPng){
    message.error('文件格式必须是jpg或png或webp')
    return
  }

  const compressedFile = await compressImage(file)
  const isLt03M = compressedFile.size / 1024 / 1024 < 0.3
  if (!isLt03M) {
    message.error('图片压缩后大小必须小于 0.3MB')
    return
  }

  await fileToBase64(file).then(base64Url => {
    imageAvatarUrl.value = base64Url
    loading.value = false
  })

  const webmasterAvatar = new FormData()
  webmasterAvatar.append('avatar', compressedFile,compressedFile.name)

  uploadAvatar(webmasterAvatar).then((res) => {
    if (res.code === 200) {
      message.success('头像上传成功')
    } else {
      message.error(`头像上传失败：${res.msg}`)
    }
  })

  return false
}

async function beforeUploadAckgroundImag(file: UploadProps['fileList'][number]) {
  const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/webp'
  if (!isJpgOrPng){
    message.error('文件格式必须是jpg或png或webp')
    return
  }

  const compressedFile = await compressImage(file)
  const isLt03M = compressedFile.size / 1024 / 1024 < 0.3
  if (!isLt03M){
    message.error('图片压缩后大小必须小于 0.3MB')
    return
  }

  const webmasterAvatar = new FormData()
  webmasterAvatar.append('background', compressedFile,compressedFile.name)
  uploadAckgroundImage(webmasterAvatar).then((res) => {
    if (res.code === 200) {
      backFileList.value = [{
        thumbUrl: res.data,
        name: new URL(res.data).pathname.split('/').pop(),
      }]
      message.success('资料卡背景图上传成功')
    }else{
      message.error(`资料卡背景图上传失败：${res.msg}`)
    }
  })

  return false
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

</script>

<template>
  <div class="info">
    <a-form>
      <div class="avatar">
        <a-upload
            :file-list="avatarFileList"
            name="avatar"
            list-type="picture-card"
            class="avatar-uploader"
            :show-upload-list="false"
            :before-upload="beforeUploadAvatar"
        >
          <img v-if="imageAvatarUrl" :src="imageAvatarUrl" alt="avatar">
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
              :file-list="backFileList"
              list-type="picture"
              name="background"
              :show-upload-list="{showRemoveIcon: false}"
              :before-upload="beforeUploadAckgroundImag"
              :max-count="1"
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
