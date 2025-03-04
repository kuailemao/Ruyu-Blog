<script setup lang="ts">
import PhotoGallery from './components/PhotoGallery.vue'
import { ref, computed, watch } from 'vue'
import { useDark } from '@vueuse/core'
import { getPhotoList } from '@/apis/photo'
import type { PhotoAndAlbumVO } from '@/apis/photo'
import type { GalleryItem } from './components/PhotoGallery.vue'

// 当前路径状态提升到 App 组件
const currentPath = ref<number[]>([])

// 加载状态
const loading = ref(false)

// 分页相关状态
const currentPage = ref(1)
const pageSize = 16
const hasMore = ref(true)
const total = ref(0)

// 数据结构定义
interface Photo {
  id: number
  url: string
  title: string
  description: string
}

interface AlbumData {
  id: number
  name: string
  description: string
  photos: Photo[]
  subAlbums?: AlbumData[]
  coverUrl?: string | null
}

// 相册数据
const galleries = ref<Record<string, GalleryItem[]>>({
  root: []
})

// 转换后端数据为前端需要的格式
const convertBackendData = (data: PhotoAndAlbumVO[]) => {
  return data.map(item => {
    if (item.type === 1) {
      // 相册
      return {
        type: 'album' as const,
        data: {
          id: item.id,
          name: item.name,
          description: item.description || '',
          photos: [],
          coverUrl: item.url
        }
      }
    } else {
      // 照片
      return {
        type: 'photo' as const,
        data: {
          id: item.id,
          url: item.url || '',
          title: item.name,
          description: item.description || ''
        }
      }
    }
  })
}

// 加载相册和照片数据
const loadGalleryData = async (parentId?: number, page: number = 1, append: boolean = false) => {
  try {
    loading.value = true
    const response = await getPhotoList({
      pageNum: page,
      pageSize: pageSize,
      parentId: parentId || null
    })
    
    const galleryKey = parentId ? parentId.toString() : 'root'
    const newItems = convertBackendData(response.data.page)
    
    if (append) {
      galleries.value[galleryKey] = [...(galleries.value[galleryKey] || []), ...newItems]
    } else {
      galleries.value[galleryKey] = newItems
    }

    total.value = response.data.total
    hasMore.value = page * pageSize < response.data.total
    currentPage.value = page
  } catch (error) {
    console.error('加载相册数据失败:', error)
  } finally {
    loading.value = false
  }
}

// 加载更多数据
const loadMore = async () => {
  if (loading.value || !hasMore.value) return
  const currentId = currentPath.value.length > 0 ? currentPath.value[currentPath.value.length - 1] : undefined
  await loadGalleryData(currentId, currentPage.value + 1, true)
}

// 监听路径变化，加载对应的数据
watch(() => currentPath.value, async (newPath) => {
  const currentId = newPath.length > 0 ? newPath[newPath.length - 1] : undefined
  currentPage.value = 1
  hasMore.value = true
  await loadGalleryData(currentId, 1, false)
}, { immediate: true })

const navigateToAlbum = (id: number) => {
  // 如果已经在这个相册中，不做任何操作
  if (currentPath.value[currentPath.value.length - 1] === id) {
    return
  }

  // 查找完整的路径
  const newPath = findAlbumPath(id)
  if (newPath) {
    currentPath.value = newPath
  } else {
    currentPath.value = [id]
  }
}

// 优化 findAlbumPath 方法
const findAlbumPath = (targetId: number, currentPath: number[] = []): number[] | null => {
  // 检查根级相册
  for (const item of galleries.value['root']) {
    if (item.type === 'album') {
      const album = item.data as AlbumData
      if (album.id === targetId) {
        return [targetId]
      }
      // 检查这个相册的子相册
      const childPath = findInAlbum(album.id, targetId)
      if (childPath) {
        return childPath
      }
    }
  }
  return null
}

// 添加辅助函数来在相册中查找路径
const findInAlbum = (albumId: number, targetId: number): number[] | null => {
  const items = galleries.value[albumId.toString()]
  if (!items) return null

  // 直接子相册中查找
  for (const item of items) {
    if (item.type === 'album') {
      const album = item.data as AlbumData
      if (album.id === targetId) {
        return [albumId, targetId]
      }
      // 递归查找更深层的子相册
      const childPath = findInAlbum(album.id, targetId)
      if (childPath) {
        return [albumId, ...childPath]
      }
    }
  }
  return null
}

// 在 script 部分的开头，其他 import 语句后面添加
const isMobileMenuOpen = ref(false)

// 修改移动端菜单打开/关闭的方法
const toggleMobileMenu = (isOpen: boolean) => {
  isMobileMenuOpen.value = isOpen
  // 控制 body 的滚动
  if (isOpen) {
    document.body.style.overflow = 'hidden'
  } else {
    document.body.style.overflow = ''
  }
}

// 修改关闭菜单方法
const closeMobileMenu = () => {
  toggleMobileMenu(false)
}

// 添加简化版的 albumTree computed 属性
const albumTree = computed(() => {
  const tree: {
    id: number;
    name: string;
    children: any[];
    level: number;
  }[] = [];

  const buildTree = (parentId: string | null = null, level: number = 0): typeof tree => {
    const result = [];
    for (const [key, items] of Object.entries(galleries.value)) {
      const albums = items.filter(item => item.type === 'album')
          .map(item => {
            const album = item.data as AlbumData;
            return {
              id: album.id,
              name: album.name
            };
          });

      for (const album of albums) {
        if (parentId === null && key === 'root') {
          result.push({
            id: album.id,
            name: album.name,
            children: buildTree(album.id.toString(), level + 1),
            level
          });
        } else if (parentId === key) {
          result.push({
            id: album.id,
            name: album.name,
            children: buildTree(album.id.toString(), level + 1),
            level
          });
        }
      }
    }
    return result;
  };

  return buildTree();
});

// 使用 VueUse 的 useDark
const isDark = useDark()
</script>

<template>
  <div class="app-container" :class="{ 'dark-mode': isDark }">
    <header class="header">
      <div class="header-content">
        <button class="menu-toggle" @click="toggleMobileMenu(!isMobileMenuOpen)">
          <span class="menu-icon"></span>
        </button>
      </div>
    </header>
    <main class="main-content">
      <aside class="album-menu" :class="{ 'mobile-open': isMobileMenuOpen }">
        <div class="menu-header">
          <h2>相册列表</h2>
          <button class="close-menu" @click="closeMobileMenu">×</button>
        </div>
        <ul class="album-tree">
          <li class="home-item"
              :class="{ active: currentPath.length === 0 }"
              @click="currentPath = []; closeMobileMenu()">
            主页
          </li>
          <li v-for="album in albumTree"
              :key="album.id"
              class="album-tree-item"
              :class="{
                active: currentPath[currentPath.length - 1] === album.id,
                'has-children': album.children.length > 0,
                'expanded': currentPath.includes(album.id)
              }"
              :style="{ paddingLeft: `${album.level * 16}px` }"
              @click="navigateToAlbum(album.id); closeMobileMenu()">
            <span class="album-name">
              {{ album.name }}
            </span>
            <ul v-if="album.children.length > 0" class="sub-albums">
              <li v-for="child in album.children"
                  :key="child.id"
                  class="album-tree-item"
                  :class="{
                    active: currentPath[currentPath.length - 1] === child.id,
                    'has-children': child.children.length > 0,
                    'expanded': currentPath.includes(child.id)
                  }"
                  :style="{ paddingLeft: `${child.level * 16}px` }"
                  @click.stop="navigateToAlbum(child.id); closeMobileMenu()">
                <span class="album-name">
                  {{ child.name }}
                </span>
                <ul v-if="child.children.length > 0" class="sub-albums">
                  <li v-for="grandChild in child.children"
                      :key="grandChild.id"
                      class="album-tree-item"
                      :class="{
                        active: currentPath[currentPath.length - 1] === grandChild.id
                      }"
                      :style="{ paddingLeft: `${grandChild.level * 16}px` }"
                      @click.stop="navigateToAlbum(grandChild.id); closeMobileMenu()">
                    <span class="album-name">
                      {{ grandChild.name }}
                    </span>
                  </li>
                </ul>
              </li>
            </ul>
          </li>
        </ul>
      </aside>
      <div class="overlay"
           v-if="isMobileMenuOpen"
           @click="closeMobileMenu"></div>
      <div class="gallery-container">
        <PhotoGallery
            v-model:currentPath="currentPath"
            :galleries="galleries"
            :is-dark-mode="isDark"
            :loading="loading"
            :has-more="hasMore"
            @load-more="loadMore"
        />
      </div>
    </main>
  </div>
</template>

<style scoped>
.app-container {
  max-width: 1400px;
  margin: 2rem auto;
  padding: 0;
  min-height: 100vh;
}

.header {
  padding: 0 20px;
  position: relative;
  z-index: 2;
}

.header-content {
  padding: 10px 0;
  display: flex;
  align-items: center;
}

.main-content {
  display: grid;
  grid-template-columns: 260px 1fr;
  gap: 20px;
  padding: 0 20px;
  position: relative;
}

.gallery-container {
  min-width: 0;
  border-radius: 12px;
  overflow: visible;
}

/* 移动端适配 */
@media (max-width: 768px) {
  .header {
    padding: 0 10px;
  }

  .main-content {
    padding: 0 10px;
    grid-template-columns: 1fr;
  }

  .header-content {
    padding: 8px 0;
  }
}

.menu-toggle {
  display: none;
  background: none;
  border: none;
  padding: 10px;
  cursor: pointer;
}

.menu-icon {
  display: block;
  width: 24px;
  height: 2px;
  background: #2c3e50;
  position: relative;
}

.menu-icon::before,
.menu-icon::after {
  content: '';
  position: absolute;
  width: 24px;
  height: 2px;
  background: #2c3e50;
  left: 0;
}

.menu-icon::before {
  top: -6px;
}

.menu-icon::after {
  bottom: -6px;
}

.menu-header {
  display: none;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.close-menu {
  display: none;
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
}

.overlay {
  display: none;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 100;
}

.album-menu {
  width: 260px;
  position: sticky;
  top: 20px;
  height: calc(100vh - 100px);
  overflow-y: auto;
  padding: 20px;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  border-radius: 12px;
  border: 1px solid rgba(92, 106, 196, 0.1);
  box-sizing: border-box;
  transition: all 0.3s ease;
  margin-top: 20px;
}

/* 深色模式样式 */
.dark-mode .album-menu {
  background: rgba(30, 30, 30, 0.8);
  border-color: rgba(255, 255, 255, 0.1);
}

.dark-mode .menu-icon,
.dark-mode .menu-icon::before,
.dark-mode .menu-icon::after {
  background: #fff;
}

.dark-mode .album-name {
  background: rgba(40, 40, 40, 0.8);
  color: rgba(255, 255, 255, 0.9);
  border-color: rgba(255, 255, 255, 0.1);
}

.dark-mode .home-item {
  background: rgba(40, 40, 40, 0.8);
  color: rgba(255, 255, 255, 0.9);
  border-color: rgba(255, 255, 255, 0.1);
}

.dark-mode .album-name:hover,
.dark-mode .home-item:hover {
  background: #5c6ac4;
  color: white;
  box-shadow: 0 4px 12px rgba(92, 106, 196, 0.3);
}

.dark-mode .album-tree-item.active > .album-name {
  background: #5c6ac4;
  color: white;
  box-shadow: 0 4px 12px rgba(92, 106, 196, 0.3);
}

.dark-mode .home-item.active {
  background: #5c6ac4;
  color: white;
  box-shadow: 0 4px 12px rgba(92, 106, 196, 0.3);
}

.dark-mode .has-children > .album-name::before {
  color: rgba(255, 255, 255, 0.7);
}

.dark-mode .home-item::before {
  color: rgba(255, 255, 255, 0.7);
}

/* 移动端深色模式适配 */
@media (max-width: 768px) {
  .dark-mode .overlay {
    background: rgba(0, 0, 0, 0.7);
  }
}

.album-tree {
  list-style: none;
  padding: 0;
  margin: 0;
}

.album-tree-item {
  position: relative;
  cursor: pointer;
  transition: all 0.3s ease;
  margin: 6px 0;
}

.album-name {
  display: block;
  padding: 10px 16px;
  padding-left: 40px;
  border-radius: 10px;
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
  background: rgba(255, 255, 255, 0.8);
  color: #5c6ac4;
  font-size: 0.95em;
  position: relative;
  border: 1px solid rgba(92, 106, 196, 0.1);
}

.album-name::before {
  content: '📁';
  position: absolute;
  left: 12px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 1.2em;
  line-height: 1;
}

/* 有子菜单的项目使用打开的文件夹图标 */
.has-children > .album-name::before {
  content: '📂';
}

/* 主页图标 */
.home-item::before {
  content: '🏠';
  position: absolute;
  left: 12px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 1.2em;
  line-height: 1;
}

/* 移除之前的 Font Awesome 相关样式 */
.album-name i,
.home-item i {
  display: none;
}

/* 调整文字位置 */
.album-name {
  padding-left: 40px;
}

.home-item {
  padding-left: 40px;
}

.album-tree-item.active > .album-name {
  background: #5c6ac4;
  color: white;
  box-shadow: 0 4px 12px rgba(92, 106, 196, 0.2);
}

.album-tree-item.active > .album-name::before,
.album-tree-item.active > .album-name i {
  color: white;
}

.sub-albums {
  list-style: none;
  padding: 0;
  margin: 0;
  display: none;
  opacity: 0;
  transform: translateX(-10px);
  transition: all 0.3s ease;
}

.has-children:hover > .sub-albums {
  display: block;
  opacity: 1;
  transform: translateX(0);
  margin-top: 6px;
}

.home-item:hover {
  background: #5c6ac4;
  color: white;
  transform: translateX(4px);
  box-shadow: 0 4px 12px rgba(92, 106, 196, 0.2);
}

.home-item:hover i {
  color: white;
}

.home-item {
  display: block;
  padding: 10px 16px;
  padding-left: 40px;
  border-radius: 10px;
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
  background: rgba(255, 255, 255, 0.8);
  color: #5c6ac4;
  margin-bottom: 12px;
  cursor: pointer;
  position: relative;
  border: 1px solid rgba(92, 106, 196, 0.1);
}

.home-item i {
  position: absolute;
  left: 12px;
  top: 50%;
  transform: translateY(-50%);
  color: #5c6ac4;
  transition: all 0.3s ease;
  font-size: 1em;
}

.home-item:hover {
  background: #5c6ac4;
  color: white;
  transform: translateX(4px);
  box-shadow: 0 4px 12px rgba(92, 106, 196, 0.2);
}

.home-item:hover i,
.home-item.active i {
  color: white;
}

.home-item.active {
  background: #5c6ac4;
  color: white;
  box-shadow: 0 4px 12px rgba(92, 106, 196, 0.2);
}

@media (max-width: 768px) {
  .album-menu {
    padding: 16px;
  }

  .album-name,
  .home-item {
    padding: 12px 16px;
    padding-left: 36px;
  }

  .header-content {
    padding: 8px 0;
  }
}

/* 修改箭头基础样式 */
.has-children > .album-name::before {
  content: '›';
  position: absolute;
  left: 12px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 1.2em;
  color: #5c6ac4;
  transition: all 0.3s ease;
}

/* 箭头悬浮效果，只旋转不变色 */
.has-children > .album-name:hover::before {
  transform: translateY(-50%) rotate(90deg);
}

/* 选中状态下的箭头变为白色 */
.has-children.active > .album-name::before {
  color: white;
}

/* 基础样式 */
.album-name {
  display: block;
  padding: 10px 16px;
  padding-left: 40px;
  border-radius: 10px;
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
  background: rgba(255, 255, 255, 0.8);
  color: #5c6ac4;
  font-size: 0.95em;
  position: relative;
  border: 1px solid rgba(92, 106, 196, 0.1);
}

/* 选中状态 - 背景变蓝，文字变白 */
.album-tree-item.active > .album-name {
  background: #5c6ac4;
  color: white;
  box-shadow: 0 4px 12px rgba(92, 106, 196, 0.2);
}

/* 悬停状态 - 只有轻微位移和阴影效果 */
.album-name:hover {
  transform: translateX(4px);
  box-shadow: 0 2px 8px rgba(92, 106, 196, 0.15);
}

/* 主页按钮基础样式 */
.home-item {
  display: block;
  padding: 10px 16px;
  padding-left: 40px;
  border-radius: 10px;
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
  background: rgba(255, 255, 255, 0.8);
  color: #5c6ac4;
  margin-bottom: 12px;
  cursor: pointer;
  position: relative;
  border: 1px solid rgba(92, 106, 196, 0.1);
}

/* 主页按钮选中状态 */
.home-item.active {
  background: #5c6ac4;
  color: white;
  box-shadow: 0 4px 12px rgba(92, 106, 196, 0.2);
}

/* 主页按钮悬浮状态 */
.home-item:hover {
  transform: translateX(4px);
  box-shadow: 0 2px 8px rgba(92, 106, 196, 0.15);
}

/* 主页图标基础颜色 */
.home-item::before {
  content: '🏠';
  position: absolute;
  left: 12px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 1.2em;
  line-height: 1;
  color: #5c6ac4;
}

/* 主页图标选中状态 */
.home-item.active::before {
  color: white;
}

.breadcrumb-root {
  color: #666;
  user-select: none; /* 防止文本被选中 */
}

/* 保持其他面包屑样式不变 */
.breadcrumb-item {
  cursor: pointer;
  transition: color 0.3s ease;
}

.breadcrumb-item:hover {
  color: #2196F3;
}

/* 添加/修改响应式样式 */
@media (max-width: 1024px) {
  .app-container {
    padding: 10px;
  }

  .main-content {
    grid-template-columns: 200px 1fr;
    gap: 15px;
  }

  .album-menu {
    padding: 16px;
  }
}

@media (max-width: 768px) {
  .app-container {
    padding: 8px;
  }

  .header-content h1 {
    font-size: 1.5rem;
  }

  .menu-toggle {
    display: block;
    -webkit-tap-highlight-color: transparent;
    outline: none;
    user-select: none;
    touch-action: manipulation;
  }

  .menu-header {
    display: flex;
    height: 44px;
    padding: 0 0 16px 0;
    margin: 0;
    align-items: center;
  }

  .main-content {
    grid-template-columns: 1fr;
  }

  .album-menu {
    position: fixed;
    top: 0;
    left: 0;
    height: 100vh;
    width: 280px;
    transform: translateX(-100%);
    transition: transform 0.3s ease;
    z-index: 1000;
  }

  .album-menu.mobile-open {
    transform: translateX(0);
  }

  .overlay {
    display: none;
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.5);
    z-index: 999;
    backdrop-filter: blur(2px);
    touch-action: none;
  }

  .mobile-open + .overlay {
    display: block;
  }

  .close-menu {
    display: block;
    padding: 0;
    margin: 0;
    width: 44px;
    height: 44px;
    color: #5c6ac4;
    transition: color 0.3s ease;
    -webkit-tap-highlight-color: transparent;
    outline: none;
    user-select: none;
    touch-action: manipulation;
  }

  .breadcrumb {
    font-size: 0.9rem;
    white-space: nowrap;
    overflow-x: auto;
    padding-bottom: 5px;
    -webkit-overflow-scrolling: touch;
    scrollbar-width: none; /* Firefox */
  }

  .breadcrumb::-webkit-scrollbar {
    display: none; /* Chrome, Safari */
  }

  .album-name,
  .home-item {
    padding: 12px 16px;
    padding-left: 36px;
    height: 44px;
    box-sizing: border-box;
    display: flex;
    align-items: center;
  }

  /* 优化触摸区域 */
  .album-tree-item,
  .home-item {
    min-height: 44px;
  }

  /* 改善移动端的悬浮效果 */
  .album-name:hover,
  .home-item:hover {
    transform: none;
  }

  /* 优化子菜单展开效果 */
  .has-children:hover > .sub-albums {
    display: none;
  }

  .has-children.active > .sub-albums {
    display: block;
    opacity: 1;
    transform: none;
  }

  /* 移动端菜单打开时的样式 */
  .album-menu {
    /* ... 其他样式保持不变 ... */
    -webkit-overflow-scrolling: touch;
  }

  /* 遮罩层样式 */
  .overlay {
    /* ... 其他样式保持不变 ... */
    position: fixed;
    touch-action: none;
  }

  /* 确保内容区域在菜单打开时不可滚动 */
  .mobile-open ~ .gallery-container {
    pointer-events: none;
  }
}

/* 添加平板尺寸的优化 */
@media (min-width: 769px) and (max-width: 1024px) {
  .album-menu {
    width: 220px;
  }

  .album-name,
  .home-item {
    font-size: 0.9em;
  }
}

/* 添加更大屏幕的优化 */
@media (min-width: 1400px) {
  .app-container {
    max-width: 1600px;
  }

  .main-content {
    grid-template-columns: 300px 1fr;
    gap: 30px;
  }
}

/* 添加深色模式支持 */
@media (prefers-color-scheme: dark) {
  .album-menu {
    background: rgba(30, 30, 30, 0.9);
    border-color: rgba(255, 255, 255, 0.1);
  }

  .album-name,
  .home-item {
    background: rgba(40, 40, 40, 0.8);
    border-color: rgba(255, 255, 255, 0.1);
    color: #7b8cd4;
  }

  .album-tree-item.active > .album-name,
  .home-item.active {
    background: #5c6ac4;
    color: white;
  }

  .breadcrumb-root {
    color: #999;
  }

  .menu-icon,
  .menu-icon::before,
  .menu-icon::after {
    background: #fff;
  }
}

/* 针对深色模式的移动端样式 */
@media (prefers-color-scheme: dark) and (max-width: 768px) {
  .album-menu {
    background: rgb(30, 30, 30);
    box-shadow: 2px 0 10px rgba(0, 0, 0, 0.3);
  }
}

/* 添加打印样式优化 */
@media print {
  .menu-toggle,
  .album-menu,
  .overlay {
    display: none !important;
  }

  .main-content {
    grid-template-columns: 1fr;
  }

  .app-container {
    padding: 0;
  }
}

/* 添加触摸设备的优化 */
@media (hover: none) {
  .album-name:hover,
  .home-item:hover {
    transform: none;
  }

  .has-children:hover > .sub-albums {
    display: none;
  }

  .has-children.active > .sub-albums {
    display: block;
  }
}

/* 修改子菜单展开逻辑 */
.sub-albums {
  display: none;
  opacity: 0;
  transform: translateX(-10px);
  transition: all 0.3s ease;
}

/* 展开状态显示子菜单 */
.album-tree-item.expanded > .sub-albums {
  display: block;
  opacity: 1;
  transform: translateX(0);
}

/* 桌面端保持悬浮展开效果 */
@media (min-width: 769px) {
  .has-children:hover > .sub-albums {
    display: block;
    opacity: 1;
    transform: translateX(0);
  }
}

/* 移动端只使用展开类控制 */
@media (max-width: 768px) {
  .has-children:hover > .sub-albums {
    display: none;
  }

  .album-tree-item.expanded > .sub-albums {
    display: block;
    opacity: 1;
    transform: none;
  }
}

/* 添加右侧容器样式 */
.gallery-container {
  /* border: 1px solid red; */
  min-width: 60vw; /* 防止内容溢出 */
  width: 100%; /* 确保占满剩余空间 */
  position: relative; /* 为内部定位提供参考 */
}

</style>
