<script setup lang="ts">
import type {
  DraggableEvent,
  UseDraggableReturn,
} from 'vue-draggable-plus'
import {
  VueDraggable,
} from 'vue-draggable-plus'
import {ref} from 'vue'
import {message, UploadProps} from 'ant-design-vue'
import {backGetBanners, deleteBanner, updateOrder, uploadBanner} from "~/api/blog/banners";
import {compressImage} from "~/utils/CompressedImage.ts";

// 图片类型
interface FileItem {
  id: string,
  path: string,
  size: string,
  type: string,
  userId: string,
  sortOrder: number,
  createTime: string
}

const fileList = ref<FileItem[]>()

const el = ref<UseDraggableReturn>()
const disabled = ref(false)


const previewVisible = ref(false)
const tempImage = ref()
const previewTitle = ref('')

function getFileList() {
  backGetBanners().then(res => {
    fileList.value = res.data.length > 0 ? res.data : []
  })
}

onMounted(() => {
  getFileList()
})

function getBase64(file: File) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}

function handleCancel() {
  previewVisible.value = false
  previewTitle.value = ''
}

async function handlePreview(file: UploadProps['fileList'][number]) {
  if (!file.path && !file.preview)
    file.preview = (await getBase64(file.originFileObj)) as string

  tempImage.value = file
  previewVisible.value = true
  previewTitle.value = file.name || file.path.substring(file.path.lastIndexOf('/') + 1)
}

const uploading = ref(false)
const progress = ref(0)

// 上传前
async function beforeUpload(file: UploadProps['fileList'][number]) {
  const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/webp'
  if (!isJpgOrPng) {
    message.error('文件格式必须是jpg或png或webp')
    return
  }
  const compressedFile = await compressImage(file)

  const isLt03MB = compressedFile.size / 1024 / 1024 < 0.3
  if (!isLt03MB) {
    message.error('图片压缩后大小大于 0.3MB')
    return
  }

  // 手动上传
  const formData = new FormData();
  formData.append('bannerImage', compressedFile, compressedFile.name);
  uploading.value = true;
  uploadBanner(formData, handleProgress).then(async (res) => {
    if (res.code === 200) {
      getFileList()
      message.success('上传成功')
    }
  }).catch(msg => {
    message.warn(msg)
  }).finally(() => {
    uploading.value = false
  })

  return false;
}

// axios请求，图片上传进度回调
function handleProgress(progressEvent: ProgressEvent) {
  progress.value = Math.round((progressEvent.loaded * 100) / progressEvent.total)
}


function deleteBannerFunc(bannerId: string) {
  deleteBanner(bannerId).then(() => {
    getFileList()
    message.success('删除成功')
  })
}

// 排序
function sortOrderFunc(_: DraggableEvent) {
  updateOrder(fileList.value).then((req) => {
    if (req.code === 200) {
      message.success('排序成功')
    }
  })
}

</script>

<template>
  <a-divider>首页轮播图</a-divider>
  <div class="carousel">
    <div class="flex">
      <VueDraggable :disabled="disabled" ref="el" v-if="fileList" v-model="fileList" :onEnd="sortOrderFunc"
                    style="display: flex" animation="150">
        <template v-for="(file) in fileList" :key="file.id">
          <div class="preview_list">
            <div class="m-2 w-[100px] h-[100px]">
              <img :src="file.path" alt="banners" class="w-full h-full rounded-2xl">
            </div>
            <!-- 灰色遮盖层 -->
            <div class="grey_cover m-2 w-[100px] h-[100px] bg-gray-300 rounded-2xl relative">
              <div class="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 text-black flex">
                <div style="font-size: 20px;" class="text-gray-400 hover:text-black duration-300"
                     @click="handlePreview(file)">
                  <ZoomInOutlined/>
                </div>
                <div class="mx-1"></div>
                <div style="font-size: 20px;" class="text-gray-400 hover:text-black duration-300"
                     @click="deleteBannerFunc(file.id)">
                  <DeleteOutlined/>
                </div>
              </div>
            </div>
          </div>
        </template>
        <div class="flex flex-col justify-center items-center">
          <div v-if="uploading"
               class="m-2 w-[100px] h-[100px] text-gray-400 flex justify-center items-center rounded-2xl bg-gray-300">
            上传中...
          </div>
          <a-progress :show-info="false" v-if="uploading" :percent="progress ?? 0" size="small" class="ml-1"/>
        </div>
      </VueDraggable>
      <!-- 上传按钮 -->
      <a-upload
          class="ant-upload ant-upload-picture-card m-2"
          v-model="fileList"
          list-type="picture-card"
          :showUploadList="false"
          :before-upload="beforeUpload"
          v-show="fileList && fileList.length < 5"
      >
        <div class="el-upload el-upload--picture-card">
          <div class="icon">
            <PlusOutlined/>
          </div>
        </div>
      </a-upload>
    </div>
    <a-modal :open="previewVisible" :footer="null" @cancel="handleCancel">
      <template #title>
        <span style="font-size: 1rem">名称：{{ previewTitle }}</span> <br>
        <div>大小： {{ (tempImage.size / 1024).toFixed(2) }} KB</div>
        <div>上传时间： {{ tempImage.createTime }}</div>
        <div>格式： {{ tempImage.type }}</div>
        <div>上传人id： {{ tempImage.userId }}</div>
      </template>
      <img alt="example" style="width: 100%" :src="tempImage.path">
    </a-modal>
  </div>
</template>

<style scoped lang="scss">

.preview_list {
  position: relative;

  .grey_cover {
    position: absolute;
    top: 0;
    left: 0;
    width: 100px;
    height: 100px;
    opacity: 0;
  }

  &:hover .grey_cover {
    opacity: 0.8;
    transition: all 0.3s ease;
  }

}

.carousel {
  display: flex;
  justify-content: center;
}

/* 基本样式 */
.el-upload {
  display: inline-block;
  text-align: center;
  cursor: pointer;
  outline: 0;

  .icon {
    font-size: 30px;
    color: #c0ccda;
  }
}

.el-upload--picture-card {
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #FAFAFA;
  border: 5px dashed #D9D9D9;
  border-radius: 6px;
  box-sizing: border-box;
  width: 100px;
  height: 100px;
  line-height: 146px;
  vertical-align: top;
  transition: all 0.3s ease;

  &:hover {
    border-color: #409EFF;
  }
}

.el-upload__input {
  display: none;
}

/* 图标样式 */
.el-icon-plus {
  width: 30px;
  height: 30px;
  font-size: 30px;
  color: #c0ccda;
}
</style>
