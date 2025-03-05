<script setup lang="ts">
import {ref, onMounted} from 'vue'
import {Image, Modal, Form, Input, Upload, message, Pagination} from 'ant-design-vue'
import type {UploadProps, FormInstance} from 'ant-design-vue'
import type {Rule} from 'ant-design-vue/es/form'
import {createAlbum, photoAndAlbumList, uploadPhoto, updateAlbum, deletePhotoOrAlbum} from "~/api/blog/photo";
import {compressImage} from "~/utils/CompressedImage.ts";

// 统一的数据接口
interface BaseItem {
  id: number
  name: string
  parentId: number | null
  createTime: string
  type: 1 | 2  // 1: 相册, 2: 照片
  children?: BaseItem[]  // 子数据字段
}

interface Album extends BaseItem {
  type: 1
  description: string
  children?: (Album | Photo)[]
}

interface Photo extends BaseItem {
  type: 2
  url: string
  size: string
}

onMounted(() => {
  refreshFunc()
})
// 数据管理
const allItems = ref<(Album | Photo)[]>([])
// 当前显示的项目
const currentItems = ref<(Album | Photo)[]>([])

async function refreshFunc() {
  // 查询所有数据
  photoAndAlbumList({
    pageNum: currentPage.value,
    pageSize: pageSize.value,
    parentId: currentAlbumId.value
  }).then(res => {
    console.log("平铺数据",res.data.page)
    const flatData = res.data.page
    // 构建树形结构
    const buildTree = (items: (Album | Photo)[], parentId: number | null = null): (Album | Photo)[] => {
      return items
          .filter(item => item.parentId === parentId)
          .map(item => {
            if (item.type === 1) {
              const children = buildTree(items, item.id)
              return {...item, children} as Album
            }
            return item
          })
    }

    // 从根节点开始构建
    allItems.value = buildTree(flatData)
    console.log('构建的树形结构:', allItems.value)
  })
}

const currentAlbumId = ref<number | null>(null)
const breadcrumbPath = ref<Album[]>([])
const showModal = ref(false)
const modalType = ref<1 | 2 | 3>(1)
const formRef = ref<FormInstance>()
const formRules: Record<string, Rule[]> = {
  name: [{required: true, type: 'string', message: '请输入名称', trigger: 'blur'}]
}
const formState = ref({
  id: 0,
  name: '',
  description: '',
  parentId: null as number | null,
  file: null as File | null
})

// 预览状态
const previewVisible = ref(false)

// 处理预览状态变化
const handlePreviewChange = (visible: boolean) => {
  previewVisible.value = visible
}

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)  // 每页8个项目
const total = ref(0)

// 添加loading状态
const loading = ref(false)

// 加载当前相册的内容
const loadCurrentItems = async () => {
  loading.value = true
  try {
    const res = await photoAndAlbumList({
      pageNum: currentPage.value,
      pageSize: pageSize.value,
      parentId: currentAlbumId.value
    })
    
    if (res.code === 200) {
      console.log("分页数据", res.data)
      // 更新数据和总数
      currentItems.value = res.data.page  // 当前页的数据
      total.value = res.data.total      // 数据总数
    }
  } catch (error) {
    console.error('Failed to load items:', error)
    message.error('加载失败')
  } finally {
    loading.value = false
  }
}

// 处理页码变化
const handlePageChange = async (page: number) => {
  currentPage.value = page
  await loadCurrentItems()
}

// 更新面包屑导航
const updateBreadcrumb = async (album: Album) => {
  try {
    // 直接将当前相册添加到面包屑路径
    if (breadcrumbPath.value.length === 0) {
      // 如果是从根目录进入，直接添加
      breadcrumbPath.value = [album]
    } else {
      // 如果已经有路径，检查是否是子相册
      const lastAlbum = breadcrumbPath.value[breadcrumbPath.value.length - 1]
      if (album.parentId === lastAlbum.id) {
        // 是子相册，添加到路径末尾
        breadcrumbPath.value = [...breadcrumbPath.value, album]
      } else if (album.parentId === null) {
        // 如果是根相册，重置路径
        breadcrumbPath.value = [album]
      } else {
        // 如果是其他情况，可能需要重新获取完整路径
        const res = await photoAndAlbumList({
          pageNum: 1,
          pageSize: 1000,  // 获取足够多的数据以构建路径
          parentId: null   // 从根目录开始查找
        })
        
        if (res.code === 200) {
          const items = res.data.list
          const path: Album[] = []
          let currentId: number | null = album.id
          
          // 从当前相册往上查找父级
          while (currentId !== null) {
            const current = items.find(item => item.id === currentId) as Album
            if (current) {
              path.unshift(current)
              currentId = current.parentId
            } else {
              break
            }
          }
          
          breadcrumbPath.value = path
        }
      }
    }
  } catch (error) {
    console.error('Failed to update breadcrumb:', error)
    message.error('更新导航失败')
  }
}

// 进入相册
const enterAlbum = async (album: Album) => {
  if (currentAlbumId.value === album.id) {
    return
  }
  
  // 如果是从根目录进入相册，保存当前页码
  if (currentAlbumId.value === null) {
    rootPageNumber.value = currentPage.value
  }
  
  currentAlbumId.value = album.id
  await updateBreadcrumb(album)  // 等待面包屑更新完成
  // 进入新相册时重置分页到第一页
  currentPage.value = 1
  await loadCurrentItems()
}

// 返回指定位置
const goBack = async (index: number) => {
  if (index === -1) {
    // 返回根目录时恢复之前保存的页码
    currentAlbumId.value = null
    breadcrumbPath.value = []
    currentPage.value = rootPageNumber.value
  } else {
    // 返回指定层级
    const targetAlbum = breadcrumbPath.value[index]
    breadcrumbPath.value = breadcrumbPath.value.slice(0, index + 1)
    currentAlbumId.value = targetAlbum.id
    currentPage.value = 1
  }
  await loadCurrentItems()
}

// 打开模态框
const openModal = (type: 1 | 2 | 3) => {
  modalType.value = type
  // 重置表单状态，并设置当前所在相册的ID作为父ID
  formState.value = {
    id: 0,
    name: '',
    description: '',
    parentId: currentAlbumId.value,  // 使用当前相册ID作为父ID
    file: null
  }
  showModal.value = true
}

// 编辑相册
const handleEdit = (item: Album | Photo) => {
  // 只允许编辑相册
  if (item.type !== 1) {
    return
  }
  modalType.value = 3  // 3表示编辑模式
  formState.value = {
    id: item.id,  // 保存要编辑的相册ID
    name: item.name,
    description: (item as Album).description,
    parentId: item.parentId,
    file: null
  }
  showModal.value = true
}

// 删除相册或照片
const handleDelete = async (item: Album | Photo) => {
  Modal.confirm({
    title: '确认删除',
    content: `确定要删除${('url' in item) ? '照片' : '相册'} "${item.name}" 吗？`,
    okText: '确定',
    cancelText: '取消',
    okType: 'danger',
    async onOk() {
      try {
        const res = await deletePhotoOrAlbum({id: item.id, type: item.type,parentId: item.parentId})
        if (res.code === 200) {
          message.success('删除成功')
          // 如果当前页已经没有数据了，则返回上一页
          if (currentItems.value.length === 1 && currentPage.value > 1) {
            currentPage.value--
          }
          await loadCurrentItems()
        }
      } catch (error) {
        console.error('Delete failed:', error)
        message.error('删除失败')
      }
    }
  })
}

// 处理文件上传
const uploadProps: UploadProps = {
  beforeUpload: (file) => {
    // 检查文件格式
    const allowedTypes = [
      'image/jpeg',
      'image/png',
      'image/webp',
      'image/gif'
    ]
    const isValidFormat = allowedTypes.includes(file.type)
    if (!isValidFormat) {
      message.error('只支持 JPG/PNG/WebP/GIF 格式的图片')
      return Upload.LIST_IGNORE
    }

    // 检查文件大小（8MB = 8 * 1024 * 1024 bytes）
    const isLessThan8M = file.size / 1024 / 1024 < 8
    if (!isLessThan8M) {
      message.error('图片大小不能超过 8MB')
      return Upload.LIST_IGNORE
    }

    formState.value.file = file
    formState.value.name = file.name.split('.')[0]
    handleFileChange(file)
    return false
  },
  accept: '.jpg,.jpeg,.png,.webp,.gif',
  listType: 'picture-card',
  maxCount: 1,
  onPreview: () => {
    handlePreviewChange(true)
  },
  onRemove: () => {
    formState.value.file = null
    formState.value.name = ''
    previewUrl.value = ''
  }
}

// 预览图片的URL
const previewUrl = ref<string>('')

// 监听文件变化并生成预览URL
const handleFileChange = (file: File) => {
  if (file) {
    const reader = new FileReader()
    reader.onload = (e) => {
      previewUrl.value = e.target?.result as string
    }
    reader.readAsDataURL(file)
  } else {
    previewUrl.value = ''
  }
}

// 提交表单
const handleSubmit = async () => {
  try {
    await formRef.value?.validateFields()

    if (modalType.value === 1) {
      // 创建相册
      const res = await createAlbum({
        name: formState.value.name,
        description: formState.value.description,
        parentId: formState.value.parentId
      })
      if (res.code === 200) {
        message.success('创建相册成功')
        await loadCurrentItems()
      }
    } else if (modalType.value === 3) {
      // 修改相册
      const res = await updateAlbum({
        id: formState.value.id,
        name: formState.value.name,
        description: formState.value.description,
      })
      if (res.code === 200) {
        message.success('修改相册成功')
        await loadCurrentItems()
      }
    } else {
      // 上传照片
      if (!formState.value.file) {
        message.error('请选择要上传的照片')
        return
      }

      try {
        // 压缩图片
        const compressedFile = await compressImage(formState.value.file)

        // 构建 FormData
        const formData = new FormData()

        formData.append('file', compressedFile, compressedFile.name)
        formData.append('name', formState.value.name)
        if (formState.value.parentId) {
          formData.append('parentId', String(formState.value.parentId))
        }

        // 上传压缩后的图片
        const res = await uploadPhoto(formData)
        if (res.code === 200) {
          message.success('上传照片成功')
          await loadCurrentItems()
        }
      } catch (error) {
        message.error('上传照片成功')
      }

    }

    showModal.value = false
  } catch (error) {
    console.error('Validation failed:', error)
  }
}

// 关闭模态框
const handleCancel = () => {
  showModal.value = false
  formRef.value?.resetFields()
}

// 添加一个变量来保存根目录的页码
const rootPageNumber = ref(1)

onMounted(() => {
  loadCurrentItems()
})
</script>

<template>
  <page-container>
    <template #content>
      <div class="photo-manager">
        <!-- 操作按钮 -->
        <div class="actions">
          <div class="left-actions">
            <button class="btn primary" @click="openModal(1)">
              <i class="icon">📁</i>
              <span>新建相册</span>
            </button>
            <button class="btn primary upload" @click="openModal(2)">
              <i class="icon">📷</i>
              <span>上传照片</span>
            </button>
          </div>
          <div class="right-actions">
            <button class="btn primary refresh" :class="{ 'loading': loading }" @click="loadCurrentItems" :disabled="loading">
              <i class="icon">🔄</i>
              <span>{{ loading ? '加载中...' : '刷新' }}</span>
            </button>
          </div>
        </div>

        <!-- 面包屑导航 -->
        <div class="breadcrumb">
          <span class="breadcrumb-item" @click="goBack(-1)">相册管理</span>
          <template v-for="(album, index) in breadcrumbPath" :key="album.id">
            <span class="breadcrumb-separator">/</span>
            <span class="breadcrumb-item" @click="goBack(index)">{{ album.name }}</span>
          </template>
        </div>

        <!-- 列表显示 -->
        <div class="list" :class="{ 'loading': loading }">
          <div v-if="loading" class="loading-overlay">
            <div class="loading-spinner">
              <div class="icon"></div>
              <span>加载中...</span>
            </div>
          </div>
          <div v-if="currentItems.length === 0" class="empty-state">
            <div class="empty-content">
              <i class="empty-icon">📁</i>
              <p>该相册还是空的哦~</p>
              <div class="empty-actions">
                <button class="btn primary" @click="openModal(1)">新建相册</button>
                <button class="btn primary upload" @click="openModal(2)">上传照片</button>
              </div>
            </div>
          </div>

          <template v-else>
            <!-- 统一的网格容器 -->
            <div class="grid-container">
              <template v-for="item in currentItems" :key="item.id">
                <!-- 相册项 -->
                <div v-if="item.type === 1"
                     class="list-item album-item"
                     @click="enterAlbum(item as Album)">
                  <div class="item-image album-icon">
                    <i class="icon">📁</i>
                  </div>
                  <div class="item-content">
                    <h3>{{ item.name }}</h3>
                    <p>{{ (item as Album).description }}</p>
                    <p class="time">{{ item.createTime }}</p>
                    <p v-if="item.children" class="count">{{ item.children.length }} 个项目</p>
                  </div>
                  <div class="item-actions" @click.stop>
                    <button v-if="item.type === 1" class="btn small edit" @click="handleEdit(item)">编辑</button>
                    <button class="btn small danger" @click="handleDelete(item)">删除</button>
                  </div>
                </div>

                <!-- 照片项 -->
                <div v-else class="list-item">
                  <div class="item-image">
                    <Image
                        :src="item.url"
                        :alt="item.name"
                        preview
                    />
                  </div>
                  <div class="item-content">
                    <h3>{{ item.name }}</h3>
                    <p class="size">大小：{{ item.size }}MB</p>
                    <p class="time">{{ item.createTime }}</p>
                  </div>
                  <div class="item-actions">
                    <button class="btn small danger" @click="handleDelete(item)">删除</button>
                  </div>
                </div>
              </template>
            </div>
          </template>
        </div>

        <!-- 分页器 -->
        <div class="pagination-container">
          <Pagination
              v-if="total > 0"
              v-model:current="currentPage"
              :pageSize="pageSize"
              :total="total"
              :show-size-changer="false"
              @change="handlePageChange"
          />
        </div>

        <!-- 使用 Ant Design Vue 的模态框 -->
        <Modal
            :visible="showModal"
            :title="modalType === 1 ? '新建相册' : modalType === 2 ? '上传照片' : '编辑相册'"
            @cancel="handleCancel"
            @ok="handleSubmit"
            :maskClosable="false"
            :destroyOnClose="true"
        >
          <Form
              :model="formState"
              ref="formRef"
              :rules="formRules"
          >
            <Form.Item label="名称" name="name">
              <Input v-model:value="formState.name" placeholder="请输入名称"/>
            </Form.Item>

            <Form.Item v-if="modalType === 1 || modalType === 3" label="描述" name="description">
              <Input.TextArea
                  v-model:value="formState.description"
                  :rows="3"
                  placeholder="请输入描述"
              />
            </Form.Item>

            <Form.Item v-if="modalType === 2" label="照片" name="file">
              <div class="upload-container">
                <Upload v-bind="uploadProps">
                  <div class="ant-upload-text">
                    <i class="icon">📷</i>
                    <span>点击上传照片</span>
                    <p style="margin-top: 8px; color: #999; font-size: 12px;">
                      支持 JPG/PNG/WebP/GIF 格式，大小不超过 4MB
                    </p>
                  </div>
                </Upload>
                <Image
                    v-if="previewUrl"
                    :src="previewUrl"
                    :preview="{
                visible: previewVisible,
                onVisibleChange: handlePreviewChange
              }"
                    style="display: none;"
                />
              </div>
            </Form.Item>
          </Form>
        </Modal>
      </div>
    </template>
  </page-container>
</template>

<style scoped lang="scss">
@import "./style/index";

</style>
