<script setup lang="ts">
import {ref, onMounted, computed} from 'vue'
import {Image, Modal, Form, Input, Upload, message, Pagination} from 'ant-design-vue'
import type {UploadProps, FormInstance} from 'ant-design-vue'
import type {Rule} from 'ant-design-vue/es/form'
import {createAlbum, photoAndAlbumList, uploadPhoto} from "~/api/blog/photo";

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

async function refreshFunc() {
  // 查询所有数据
  photoAndAlbumList().then(res => {
    const flatData = res.data
    // 构建树形结构
    const buildTree = (items: (Album | Photo)[], parentId: number | null = null): (Album | Photo)[] => {
      return items
        .filter(item => item.parentId === parentId)
        .map(item => {
          if (item.type === 1) {
            const children = buildTree(items, item.id)
            return { ...item, children } as Album
          }
          return item
        })
    }
    
    // 从根节点开始构建
    allItems.value = buildTree(flatData)
    console.log('构建的树形结构:', allItems.value)
  })
}

// 当前显示的项目
const currentItems = computed(() => {
  if (currentAlbumId.value === null) {
    // 根目录
    return allItems.value
  } else {
    // 查找当前相册
    const findAlbum = (items: (Album | Photo)[]): Album | undefined => {
      for (const item of items) {
        if (item.type === 1) {
          if (item.id === currentAlbumId.value) {
            return item as Album
          }
          if (item.children) {
            const found = findAlbum(item.children)
            if (found) return found
          }
        }
      }
      return undefined
    }
    
    const currentAlbum = findAlbum(allItems.value)
    return currentAlbum?.children || []
  }
})

const currentAlbumId = ref<number | null>(null)
const breadcrumbPath = ref<Album[]>([])
const showModal = ref(false)
const modalType = ref<1 | 2>(1)
const formRef = ref<FormInstance>()
const formRules: Record<string, Rule[]> = {
  name: [{required: true, type: 'string', message: '请输入名称', trigger: 'blur'}]
}
const formState = ref({
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

// 加载当前相册的内容
const loadCurrentItems = async () => {
  // 使用现有的数据结构，不需要重新加载
  // 数据已经在 currentItems 计算属性中处理
}

// 更新面包屑导航
const updateBreadcrumb = (album: Album) => {
  const findPath = (items: (Album | Photo)[], targetId: number, path: Album[] = []): Album[] | null => {
    for (const item of items) {
      if (item.type === 1) {
        if (item.id === targetId) {
          return [...path, item as Album]
        }
        if (item.children) {
          const found = findPath(item.children, targetId, [...path, item as Album])
          if (found) return found
        }
      }
    }
    return null
  }

  const path = findPath(allItems.value, album.id)
  if (path) {
    breadcrumbPath.value = path
  }
}

// 添加一个变量来保存根目录的页码
const rootPageNumber = ref(1)

// 进入相册
const enterAlbum = (album: Album) => {
  if (currentAlbumId.value === album.id) {
    return
  }
  
  // 如果是从根目录进入相册，保存当前页码
  if (currentAlbumId.value === null) {
    rootPageNumber.value = currentPage.value
  }
  
  currentAlbumId.value = album.id
  updateBreadcrumb(album)
  // 进入新相册时重置分页到第一页
  currentPage.value = 1
}

// 返回指定位置
const goBack = (index: number) => {
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
  }
}

// 打开模态框
const openModal = (type: 1 | 2) => {
  modalType.value = type
  // 重置表单状态，并设置当前所在相册的ID作为父ID
  formState.value = {
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
  if ('url' in item) {
    return
  }
  modalType.value = 1
  formState.value = {
    name: item.name,
    description: item.description,
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
        // const res = await deletePhotoOrAlbum(item.id, item.type)
        const res = null;
        if (res.code === 200) {
          message.success('删除成功')
          refreshFunc()
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
    formState.value.file = file
    // 直接使用完整文件名，包含后缀
    formState.value.name = file.name
    handleFileChange(file)
    return false
  },
  accept: 'image/*',
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
        await refreshFunc()
      }
    } else {
      // 上传照片
      if (!formState.value.file) {
        message.error('请选择要上传的照片')
        return
      }
      const formData = new FormData()
      formData.append('file', formState.value.file)
      formData.append('name', formState.value.name)
      formData.append('parentId', String(formState.value.parentId))
      
      const res = await uploadPhoto(formData)
      if (res.code === 200) {
        message.success('上传照片成功')
        await refreshFunc()
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

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)  // 每页显示10个项目（5行，每行2个）
const total = ref(0)

// 计算当前页的数据
const paginatedItems = computed(() => {
  // 先计算总数
  total.value = currentItems.value.length
  
  // 计算当前页的数据
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  
  // 确保不会超出范围
  if (start >= total.value) {
    currentPage.value = 1
    return currentItems.value.slice(0, pageSize.value)
  }
  
  return currentItems.value.slice(start, end)
})

// 处理页码变化
const handlePageChange = (page: number, size: number) => {
  currentPage.value = page
  pageSize.value = size
}

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
          <button class="btn primary" @click="openModal(1)">
            <i class="icon">📁</i>
            <span>新建相册</span>
          </button>
          <button class="btn primary upload" @click="openModal(2)">
            <i class="icon">📷</i>
            <span>上传照片</span>
          </button>
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
        <div class="list">
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
            <!-- 先显示相册 -->
            <template v-for="item in paginatedItems" :key="item.id">
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
                  <button class="btn small edit" @click="handleEdit(item)">编辑</button>
                  <button class="btn small danger" @click="handleDelete(item)">删除</button>
                </div>
              </div>
            </template>

            <!-- 再显示照片 -->
            <template v-for="item in paginatedItems" :key="item.id">
              <div v-if="item.type === 2" class="list-item">
                <div class="item-image">
                  <Image
                      :src="item.url"
                      :alt="item.name"
                      preview
                  />
                </div>
                <div class="item-content">
                  <h3>{{ item.name }}</h3>
                  <p class="size">{{ item.size }}</p>
                  <p class="time">{{ item.createTime }}</p>
                </div>
                <div class="item-actions">
                  <button class="btn small danger" @click="handleDelete(item)">删除</button>
                </div>
              </div>
            </template>
          </template>
        </div>

        <!-- 分页器 -->
        <div class="pagination-container">
          <Pagination
              v-if="total > 0"
              v-model:current="currentPage"
              v-model:pageSize="pageSize"
              :total="total"
              :show-size-changer="false"
              @change="handlePageChange"
          />
        </div>

        <!-- 使用 Ant Design Vue 的模态框 -->
        <Modal
            :visible="showModal"
            :title="modalType === 1 ? '相册' : '照片'"
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

            <Form.Item v-if="modalType === 1" label="描述" name="description">
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
                    <span>点击上传</span>
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
