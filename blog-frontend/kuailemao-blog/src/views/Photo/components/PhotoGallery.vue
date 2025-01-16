<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, watch } from 'vue'
import PhotoPreview from './PhotoPreview.vue'
import AlbumBanner from './AlbumBanner.vue'

interface Album {
  id: number
  name: string
  description: string
}

interface Photo {
  id: number
  url: string
  title: string
  description: string
}

interface GalleryItem {
  type: 'album' | 'photo'
  data: Album | Photo
}

const props = defineProps<{
  currentPath: number[]
  galleries: Record<string, GalleryItem[]>
  isDarkMode: boolean
}>()

const emit = defineEmits<{
  (e: 'update:currentPath', value: number[]): void
}>()

const showPreview = ref(false)
const currentPhotoIndex = ref(0)
const previewPhotos = ref<string[]>([])

const handleItemClick = (item: GalleryItem) => {
  if (item.type === 'photo') {
    // 保存当前滚动位置
    const scrollPosition = window.scrollY

    // 打开预览前禁用滚动
    document.body.style.position = 'fixed'
    document.body.style.width = '100%'
    document.body.style.top = `-${scrollPosition}px`
    document.body.style.overflow = 'hidden'
    document.body.style.left = '0'
    document.body.style.marginTop = '0'

    const photos = getCurrentGallery()
        .filter(item => item.type === 'photo')
        .map(item => (item.data as Photo).url)
    previewPhotos.value = photos
    currentPhotoIndex.value = photos.indexOf((item.data as Photo).url)
    showPreview.value = true
  } else {
    emit('update:currentPath', [...props.currentPath, (item.data as Album).id])
  }
}

// 添加缺失的状态
const itemsVisible = ref<boolean[]>([])

// 添加缺失的函数
const getCurrentGallery = () => {
  const path = props.currentPath.length === 0 ? 'root' : props.currentPath[props.currentPath.length - 1].toString()
  return props.galleries[path] || []
}

const initializeVisibility = () => {
  const gallery = getCurrentGallery()
  itemsVisible.value = new Array(gallery.length).fill(false)
  requestAnimationFrame(() => {
    gallery.forEach((_, index) => {
      setTimeout(() => {
        itemsVisible.value[index] = true
      }, index * 100)
    })
  })
}

// 添加生命周期钩子
onMounted(() => {
  initializeVisibility()
})

onUnmounted(() => {
  // 清理工作
  document.body.style.position = ''
  document.body.style.width = ''
  document.body.style.top = ''
  document.body.style.overflow = ''
  document.body.style.left = ''
  document.body.style.marginTop = ''
})

// 添加 watch 以处理路径变化
watch(() => [props.currentPath, props.galleries], () => {
  initializeVisibility()
}, { deep: true })

// 添加获取相册封面的函数
const getAlbumCover = (albumId: number) => {
  const album = props.galleries.root.find(
      item => item.type === 'album' && (item.data as AlbumData).id === albumId
  )

  if (album && album.type === 'album') {
    const albumData = album.data as AlbumData
    if (albumData.photos && albumData.photos.length > 0) {
      return albumData.photos[0].url
    }
  }

  return 'https://pic2.zhimg.com/v2-3aecfb4a857585d6eb2796902a565956_r.jpg?source=172ae18b'
}

// 添加判断相册是否有照片的函数
const hasPhotos = (item: GalleryItem) => {
  if (item.type === 'album') {
    const albumData = item.data as AlbumData
    return albumData.photos && albumData.photos.length > 0
  }
  return false
}

// 添加缺失的 AlbumData 接口
interface AlbumData {
  id: number
  name: string
  photos: Photo[]
  subAlbums?: AlbumData[]
}

// 修改获取当前相册信息的计算属性
const currentAlbum = computed(() => {
  const path = props.currentPath.length === 0 ? 'root' : props.currentPath[props.currentPath.length - 1].toString()
  const gallery = props.galleries[path] || []

  // 获取当前相册信息
  let albumInfo: { data: Album } | undefined

  if (path === 'root') {
    // 如果是根目录，使用默认标题
    return {
      title: '我的相册',
      description: '相册功能正在测试阶段，图片来源于网络，如有侵权请联系我！！！',
      photosCount: gallery.filter(item => item.type === 'photo').length,
      albumsCount: gallery.filter(item => item.type === 'album').length,
      coverImage: gallery.find(item => item.type === 'photo')?.data.url
    }
  } else {
    // 在父级相册中查找当前相册的信息
    const parentPath = props.currentPath.length > 1
        ? props.currentPath[props.currentPath.length - 2].toString()
        : 'root'
    const parentGallery = props.galleries[parentPath] || []

    albumInfo = parentGallery.find(
        item => item.type === 'album' && (item.data as Album).id === Number(path)
    ) as { data: Album } | undefined
  }

  return {
    title: albumInfo?.data.name || '未命名相册',
    description: albumInfo?.data.description || '',
    photosCount: gallery.filter(item => item.type === 'photo').length,
    albumsCount: gallery.filter(item => item.type === 'album').length,
    coverImage: gallery.find(item => item.type === 'photo')?.data.url
  }
})

// 添加获取最近相册的计算属性
const recentAlbums = computed(() => {
  const path = props.currentPath.length === 0 ? 'root' : props.currentPath[props.currentPath.length - 1].toString()
  const gallery = props.galleries[path] || []

  return gallery
      .filter(item => item.type === 'album')
      .slice(0, 4) // 只显示前4个相册
      .map(item => ({
        id: (item.data as Album).id,
        name: (item.data as Album).name,
        coverUrl: getAlbumCover((item.data as Album).id)
      }))
})

// 添加相册点击处理
const handleAlbumClick = (albumId: number) => {
  emit('update:currentPath', [...props.currentPath, albumId])
}

// 修改面包屑数据计算属性
const breadcrumbs = computed(() => {
  return props.currentPath.map((id, index) => {
    // 获取父级路径
    const parentPath = index === 0 ? 'root' : props.currentPath[index - 1].toString()
    const parentGallery = props.galleries[parentPath] || []

    // 在父级相册中查找当前相册信息
    const albumInfo = parentGallery.find(
        item => item.type === 'album' && (item.data as Album).id === id
    ) as { data: Album } | undefined

    return {
      id,
      name: albumInfo?.data.name || '未命名相册'
    }
  })
})

// 处理面包屑点击
const handleBreadcrumbClick = (index: number) => {
  if (index === -1) {
    emit('update:currentPath', [])
  } else {
    emit('update:currentPath', props.currentPath.slice(0, index + 1))
  }
}
</script>

<template>
  <div class="photo-gallery" :class="{ 'dark-mode': isDarkMode }">
    <AlbumBanner
        :title="currentAlbum.title"
        :description="currentAlbum.description"
        :photos-count="currentAlbum.photosCount"
        :albums-count="currentAlbum.albumsCount"
        :cover-image="currentAlbum.coverImage"
        :breadcrumbs="breadcrumbs"
        :is-dark-mode="isDarkMode"
        @breadcrumb-click="handleBreadcrumbClick"
    />

    <div class="gallery-wrapper">
      <div v-if="getCurrentGallery().length > 0" class="gallery-grid">
        <div v-for="(item, index) in getCurrentGallery()"
             :key="item.type + (item.data as any).id"
             class="gallery-item"
             :class="{ visible: itemsVisible[index] }"
             @click="handleItemClick(item)">
          <template v-if="item.type === 'album'">
            <div v-if="!hasPhotos(item)" class="default-album-cover">
              <div class="default-album-inner">
                <div class="album-icon"></div>
              </div>
            </div>
            <img v-else
                 :src="getAlbumCover((item.data as Album).id)"
                 :alt="(item.data as Album).name"
                 loading="lazy">
          </template>
          <img v-else
               :src="(item.data as Photo).url"
               alt=""
               loading="lazy">
          <div class="item-info" v-if="item.type === 'album'">
            <h3>{{ (item.data as Album).name }}</h3>
          </div>
        </div>
      </div>

      <!-- 简化的空状态显示 -->
      <div v-else class="empty-state">
        <p>这个相册还没有内容哦~</p>
      </div>
    </div>

    <!-- 使用新的 PhotoPreview 组件 -->
    <PhotoPreview
        v-model:show="showPreview"
        v-model:currentIndex="currentPhotoIndex"
        :photos="previewPhotos"
    />
  </div>
</template>

<style scoped>
.photo-gallery {
  width: 100%;
  min-height: calc(100vh - 200px);
  background: transparent;
  overflow: visible;
  padding: 0 20px;
  display: flex;
  flex-direction: column;
}

.gallery-wrapper {
  padding: 30px;
  position: relative;
  z-index: 1;
  background: rgba(255, 255, 255, 0.98);
  border-radius: 32px;
  backdrop-filter: blur(10px);
  margin-top: 0;
  flex: 1;
  min-height: 400px;
}

.gallery-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr); /* 固定为4列 */
  gap: 20px;
  padding: 10px 0 30px 0;
  max-width: 1400px;
  margin: 0 auto;
}

.gallery-item {
  position: relative;
  border-radius: 12px;
  overflow: hidden;
  aspect-ratio: 1;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  background: rgba(255, 255, 255, 0.9);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
}

.gallery-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: all 0.3s ease;
}

.gallery-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(92, 106, 196, 0.15);
}

.gallery-item:hover img {
  transform: scale(1.05);
}

/* 相册信息样式 */
.item-info {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 15px;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));
  color: white;
  display: flex;
  align-items: center;
  gap: 10px;
  transition: all 0.3s ease;
}

.item-info::before {
  content: '📁';
  font-size: 1.4em;
  line-height: 1;
}

.item-info h3 {
  margin: 0;
  font-size: 1.1em;
  font-weight: 500;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 响应式调整 */
@media (max-width: 1400px) {
  .gallery-grid {
    grid-template-columns: repeat(4, 1fr);
    gap: 18px;
  }
}

@media (max-width: 1200px) {
  .gallery-grid {
    grid-template-columns: repeat(3, 1fr);
    gap: 15px;
  }
}

@media (max-width: 768px) {
  .photo-gallery {
    padding: 0 10px;
  }

  .gallery-wrapper {
    padding: 20px;
    margin-top: 0;
    border-radius: 24px;
  }

  .gallery-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 12px;
    padding: 5px 0 20px 0;
  }
}

/* 添加进入动画 */
.gallery-item {
  opacity: 0;
  transform: translateY(20px);
}

.gallery-item.visible {
  opacity: 1;
  transform: translateY(0);
  transition: all 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}

/* 简化的空状态样式 */
.empty-state {
  padding: 40px;
  color: #999;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 300px;
  background: rgba(0, 0, 0, 0.02);
  border-radius: 16px;
  margin: 20px 0;
}

.empty-state p {
  font-size: 1.1em;
  margin: 0;
}

@media (max-width: 768px) {
  .empty-state {
    min-height: 200px;
    padding: 20px;
  }

  .empty-state p {
    font-size: 1em;
  }
}

@media (max-width: 480px) {
  .empty-state {
    padding: 12px;
  }

  .empty-state p {
    font-size: 0.9em;
  }
}

/* 默认相册封面样式 */
.default-album-cover {
  width: 100%;
  height: 100%;
  background: linear-gradient(120deg, #e0e7ff 0%, #f5f7ff 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.default-album-cover::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(
      45deg,
      transparent 0%,
      rgba(255, 255, 255, 0.4) 50%,
      transparent 100%
  );
  animation: shine 3s infinite linear;
  pointer-events: none;
}

.album-icon {
  width: 50px;
  height: 50px;
  position: relative;
  margin: 20px;
}

.album-icon::before {
  content: '';
  position: absolute;
  width: 100%;
  height: 100%;
  background: #5c6ac4;
  border-radius: 12px;
  transform: rotate(-10deg);
  box-shadow: 0 3px 10px rgba(92, 106, 196, 0.2);
}

.album-icon::after {
  content: '';
  position: absolute;
  top: -5px;
  left: 5px;
  width: 100%;
  height: 100%;
  background: #7b8cd4;
  border-radius: 12px;
  transform: rotate(5deg);
  box-shadow: 0 3px 10px rgba(92, 106, 196, 0.2);
}

/* 悬浮效果 */
.gallery-item:hover .default-album-cover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(92, 106, 196, 0.2);
}

.gallery-item:hover .album-icon::before {
  transform: rotate(-15deg);
}

.gallery-item:hover .album-icon::after {
  transform: rotate(10deg);
}

/* 点击效果 */
.gallery-item:active .default-album-cover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(92, 106, 196, 0.15);
}

@keyframes shine {
  0% {
    transform: translateX(-100%) translateY(-100%) rotate(45deg);
  }
  100% {
    transform: translateX(100%) translateY(100%) rotate(45deg);
  }
}

/* 修改深色模式样式，使用类选择器而不是媒体查询 */
.dark-mode .gallery-wrapper {
  background: rgba(30, 30, 30, 0.98);
  box-shadow: none;
}

.dark-mode .gallery-item {
  background: rgba(40, 40, 40, 0.9);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

.dark-mode .gallery-item:hover {
  box-shadow: 0 8px 20px rgba(92, 106, 196, 0.2);
}

.dark-mode .empty-state {
  color: rgba(255, 255, 255, 0.6);
  background: rgba(255, 255, 255, 0.03);
}

.dark-mode .default-album-cover {
  background: linear-gradient(120deg, #2a2f4c 0%, #1a1f35 100%);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.3);
}

.dark-mode .album-icon::before {
  background: #3d4674;
}

.dark-mode .album-icon::after {
  background: #4d5894;
}

.dark-mode .gallery-item:hover .default-album-cover {
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.4);
}

.dark-mode .item-info {
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.8));
}

.dark-mode .gallery-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.3);
}

/* 移动端适配 */
@media (max-width: 768px) {
  .gallery-wrapper {
    padding: 20px;
    margin-top: 0;
    border-radius: 24px;
  }

  .gallery-grid {
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
    gap: 15px;
    padding: 5px 0 20px 0;
  }
}
</style> 