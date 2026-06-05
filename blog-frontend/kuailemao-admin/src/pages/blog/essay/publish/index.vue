<script setup lang="ts">
import 'md-editor-v3/lib/style.css'
import { MdEditor } from 'md-editor-v3'
import type { UploadProps } from 'ant-design-vue'
import { message } from 'ant-design-vue'
import type { Ref, UnwrapRef } from 'vue'
import type { BasicColorMode, UseColorModeReturn } from '@vueuse/core'
import {
  addCategory,
  addTag,
  articleCategory,
  articleTag,
  deleteCover,
  getArticle,
  publishArticle,
  uploadArticleImage,
  uploadCover,
} from '~/api/blog/article'
import type { CategoryType, TagType } from '~/pages/blog/essay/publish/type.ts'
import { useMultiTab } from '~/stores/multi-tab.ts'
import {compressImage} from "~/utils/CompressedImage.ts";

const route = useRoute()
const multiTab = useMultiTab()
// 日夜切换
const mode: UseColorModeReturn<BasicColorMode> = useColorMode()

const fileList = ref<UploadProps['fileList']>([])
// 预览Base64
const previewBase64 = ref<string>()
const formData = ref({
  categoryId: undefined,
  tagId: undefined,
  articleCover: undefined,
  articleTitle: undefined,
  articleContent: undefined,
  articleType: 1,
  isTop: 0,
  status: 1,
})

function getBase64(img: Blob, callback: (base64Url: string) => void) {
  const reader = new FileReader()
  reader.addEventListener('load', () => callback(reader.result as string))
  reader.readAsDataURL(img)
}

// 虚拟节点
const VNodes = defineComponent({
  props: {
    vnodes: {
      type: Object,
      required: true,
    },
  },
  render() {
    return this.vnodes
  },
})

const inputRef = ref()
const categoryName = ref()
const tagName = ref()

// 分类
const categoryList: Ref<UnwrapRef<CategoryType[]>> = ref([])
// 标签
const tagList: Ref<TagType[]> = ref([])

onMounted(async () => {
  getFormData()
  await getCategory()
  await getTag()
})

async function getCategory() {
  const { data } = await articleCategory()
  categoryList.value = data
}
async function getTag() {
  const { data } = await articleTag()
  tagList.value = data
}

const categoryLoading = ref(false)

async function addCategoryFunc(e: MouseEvent) {
  console.log(categoryName.value)
  if (!categoryName.value) {
    message.warn('请检查分类内容是否填写完整')
    return
  }
  categoryLoading.value = true
  e.preventDefault()
  const data = { categoryName: categoryName.value }
  try {
    const res = await addCategory(data)
    if (res.code === 200) {
      await getCategory()
      const created = categoryList.value.find(item => item.categoryName === data.categoryName)
      if (created)
        formData.value.categoryId = created.id
    }
  }
  catch (error) {
    console.error('addCategory failed:', error)
  }
  finally {
    categoryLoading.value = false
  }
  categoryName.value = ''
  setTimeout(() => {
    inputRef.value?.focus()
  }, 0)
}

const tagLoading = ref(false)

async function addTagFunc(e: MouseEvent) {
  if (!tagName.value) {
    message.warn('请检查标签内容是否填写完整')
    return
  }
  tagLoading.value = true
  e.preventDefault()
  const data = { tagName: tagName.value }
  try {
    const res = await addTag(data)
    if (res.code === 200) {
      await getTag()
      const created = tagList.value.find(item => item.tagName === data.tagName)
      if (created) {
        const current = (formData.value.tagId as any[]) || []
        if (!current.includes(created.id))
          formData.value.tagId = [...current, created.id] as any
      }
    }
  }
  catch (error) {
    console.error('addTag failed:', error)
  }
  finally {
    tagLoading.value = false
  }
  tagName.value = ''
  setTimeout(() => {
    inputRef.value?.focus()
  }, 0)
}

async function beforeUpload(file: UploadProps['fileList'][number]) {
  const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/webp'
  if (!isJpgOrPng) {
    message.error('图片只能是 JPG/PNG/WebP 格式!')
    return false
  }

  // 压缩图片
  const compressedFile = await compressImage(file)
  const isLt03M = compressedFile.size / 1024 / 1024 < 0.3
  if (!isLt03M) {
    message.error('图片压缩后大于 0.3MB')
    return false
  }

  fileList.value = [compressedFile]
  getBase64(file, (base64Url: string) => {
    previewBase64.value = base64Url
  })

  return false
}

async function safeDeleteCover(coverUrl?: string) {
  if (!coverUrl)
    return
  try {
    await deleteCover(coverUrl)
  }
  catch (error) {
    console.error('deleteCover failed:', error)
  }
}

async function onFinish() {
  if (!formData.value.articleTitle || !formData.value.categoryId || !formData.value.tagId || !formData.value.articleContent) {
    message.warn('请检查是否填写完整')
    return
  }

  if (!formData.value.articleCover && !fileList.value[0]) {
    message.warn('请上传文章封面')
    return
  }

  if (!fileList.value[0] && formData.value.articleCover) {
    try {
      await publishArticle(formData.value)
      message.success('发布成功')
    }
    catch (error) {
      console.error('发布失败:', error)
    }
    return
  }

  const coverForm = new FormData()
  coverForm.append('articleCover', fileList.value[0], fileList.value[0].name)

  try {
    const uploadRes = await uploadCover(coverForm)
    const uploadedCover = uploadRes?.data as string | undefined
    if (!uploadedCover) {
      message.error('上传文章封面失败')
      return
    }

    formData.value.articleCover = uploadedCover

    try {
      await publishArticle(formData.value)
      message.success('发布成功')
      formData.value.categoryId = undefined
      formData.value.tagId = undefined
      formData.value.articleCover = undefined
      formData.value.articleTitle = undefined
      formData.value.articleContent = undefined
      formData.value.articleType = 1
      formData.value.isTop = 0
      formData.value.status = 1
      fileList.value = []
      previewBase64.value = ''
    }
    catch (error) {
      console.error('publishArticle failed:', error)
      await safeDeleteCover(uploadedCover)
    }
  }
  catch (error) {
    console.error('uploadCover failed:', error)
  }
}

async function onUploadArticleImg(files: any, callback: any) {
  const res = await Promise.all(
    files.map(async (file) => {
      const compressedFile = await compressImage(file)
      return new Promise((rev, rej) => {
        const form = new FormData()
        form.append('articleImage', compressedFile, compressedFile.name)
        uploadArticleImage(form).then((res) => {
          if (res.code === 200)
            rev(res.data)
        }).catch(error => rej(error))
      })
    }),
  )
  callback(res)
}

const toolbars = [
  'bold',
  'underline',
  'italic',
  '-',
  'title',
  'strikeThrough',
  'sub',
  'sup',
  'quote',
  'unorderedList',
  'orderedList',
  'task',
  '-',
  'codeRow',
  'code',
  'link',
  'image',
  'table',
  'mermaid',
  'katex',
  '-',
  'revoke',
  'next',
  'save',
  '=',
  'pageFullscreen',
  'fullscreen',
  'preview',
  'htmlPreview',
  'catalog',
]

// 数据回显
function getFormData() {
  if (route.query.id) {
    getArticle(route.query.id as string).then((res) => {
      if (res.data)
        formData.value = res.data
    })
  }
}

function close() {
  multiTab.close(route.fullPath)
}
</script>

<template>
  <layout :is-list="false">
    <template #form-items>
      <a-form-item label="标题" style="margin-right: 1rem">
        <a-input v-model:value="formData.articleTitle" placeholder="输入文章标题" style="width: 15em" />
      </a-form-item>
      <a-form-item label="分类" style="margin-right: 1rem">
        <a-space>
          <a-select
            v-if="categoryList"
            v-model:value="formData.categoryId"
            :loading="categoryLoading"
            placeholder="选择分类"
            style="width: 15em"
            :options="categoryList.map(item => ({ value: item.id, label: item.categoryName }))"
          >
            <template #dropdownRender="{ menuNode: menu }">
              <VNodes :vnodes="menu" />
              <a-divider style="margin: 4px 0" />
              <a-space style="padding: 4px 8px">
                <a-input ref="inputRef" v-model:value="categoryName" placeholder="添加分类" />
                <a-button type="text" @click="addCategoryFunc">
                  <template #icon>
                    <plus-outlined />
                  </template>
                  添加
                </a-button>
              </a-space>
            </template>
          </a-select>
        </a-space>
      </a-form-item>
      <a-form-item label="标签" style="margin-right: 1rem">
        <a-select
          v-if="tagList"
          v-model:value="formData.tagId"
          mode="multiple"
          :loading="tagLoading"
          placeholder="选择标签"
          style="width: 15em"
          :options="tagList.map(item => ({ value: item.id, label: item.tagName }))"
        >
          <template #dropdownRender="{ menuNode: menu }">
            <VNodes :vnodes="menu" />
            <a-divider style="margin: 4px 0" />
            <a-space style="padding: 4px 8px">
              <a-input ref="inputRef" v-model:value="tagName" placeholder="添加标签" />
              <a-button type="text" @click="addTagFunc">
                <template #icon>
                  <plus-outlined />
                </template>
                添加
              </a-button>
            </a-space>
          </template>
        </a-select>
      </a-form-item>
      <a-form-item label="类型" style="margin-right: 1rem">
        <a-select
          v-model:value="formData.articleType"
          style="width: 15em"
        >
          <a-select-option :value="1">
            原创
          </a-select-option>
          <a-select-option :value="2">
            转载
          </a-select-option>
          <a-select-option :value="3">
            翻译
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="状态" style="margin-right: 1rem">
        <a-space>
          <a-select
            v-model:value="formData.status"
            style="width: 120px"
          >
            <a-select-option :value="1">
              公开
            </a-select-option>
            <a-select-option :value="2">
              私密
            </a-select-option>
            <a-select-option :value="3">
              草稿
            </a-select-option>
          </a-select>
        </a-space>
      </a-form-item>
      <a-form-item label="是否顶置" style="margin-right: 1rem">
        <a-select
          v-model:value="formData.isTop"
          style="width: 13em"
        >
          <a-select-option :value="1">
            是
          </a-select-option>
          <a-select-option :value="0">
            否
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item>
        <a-space>
          <template v-if="previewBase64 || formData.articleCover">
            <a-popover title="预览">
              <template #content>
                <a-image
                  :width="200"
                  :src="previewBase64 || formData.articleCover"
                />
              </template>
              <a-upload
                :file-list="fileList"
                :before-upload="beforeUpload"
                :max-count="1"
                :show-upload-list="false"
              >
                <a-button>
                  <PictureOutlined />
                  上传封面
                </a-button>
              </a-upload>
            </a-popover>
          </template>
          <template v-else>
            <a-upload
              :file-list="fileList"
              :before-upload="beforeUpload"
              :max-count="1"
              :show-upload-list="{ showRemoveIcon: false }"
            >
              <a-button>
                <PictureOutlined />
                上传封面
              </a-button>
            </a-upload>
          </template>
          <a-button type="primary" @click="onFinish">
            发布
          </a-button>
          <a-button class="orange" style="margin-right: 10px" @click="close">
            <template #icon>
              <CloseOutlined />
            </template>
            关闭
          </a-button>
        </a-space>
      </a-form-item>
    </template>
    <template #table-content>
      <div style="height: 80vh;width: 100%">
        <MdEditor v-model="formData.articleContent" :theme="mode" style="height: 80vh" :toolbars="toolbars as []" @onUploadImg="onUploadArticleImg" />
      </div>
    </template>
  </layout>
</template>

<style scoped lang="scss">

</style>
