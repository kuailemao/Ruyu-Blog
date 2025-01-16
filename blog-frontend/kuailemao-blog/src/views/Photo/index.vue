<script setup lang="ts">
import PhotoGallery from './components/PhotoGallery.vue'
import { ref, computed } from 'vue'
import { useDark } from '@vueuse/core'

// 当前路径状态提升到 App 组件
const currentPath = ref<number[]>([])

// 从 PhotoGallery 组件共享数据结构
interface AlbumData {
  id: number
  name: string
  photos: Photo[]
  subAlbums?: AlbumData[]
}

interface Photo {
  id: number
  url: string
  title: string
  description: string
}

interface GalleryItem {
  type: 'album' | 'photo'
  data: AlbumData | Photo
}

// 创建基础图片数组
const baseImages = [
  'https://tse4-mm.cn.bing.net/th/id/OIP-C.cS62Bm3m6aNb5IlIUG1VRgHaMb?w=193&h=323&c=7&r=0&o=5&dpr=1.3&pid=1.7',
  'https://tse3-mm.cn.bing.net/th/id/OIP-C.4m6sCxvmZ7N9YWBqEx_GWAHaLm?w=199&h=312&c=7&r=0&o=5&dpr=1.3&pid=1.7',
  'https://tse2-mm.cn.bing.net/th/id/OIP-C.Yh06BPbhaS2Pwds2cl-2-QHaFO?w=208&h=147&c=7&r=0&o=5&dpr=1.3&pid=1.7',
  'https://pic2.zhimg.com/v2-c661740055d91606880283692e40720c_r.jpg',
  'https://tse1-mm.cn.bing.net/th/id/OIP-C.3J7LSmLYBm-HEJ8nOnrzAAHaLH?rs=1&pid=ImgDetMain',
  'https://gao7pic.gao7.com/07481182f93f4ad6b17a9b29ec8c7e76.png',
  'https://5b0988e595225.cdn.sohucs.com/images/20181116/00cccb6d4cff45fe9eafe9b39e041210.png',
  'https://pic3.zhimg.com/v2-3c8a6dae7606b0cf498e3eda18747853_r.jpg',
  'https://pic3.zhimg.com/v2-193414bb442fac65b857d46f2a1a87ed_r.jpg',
  'https://pic2.zhimg.com/v2-70458016c89413161ff4fe3942099bed_r.jpg',
  'https://pica.zhimg.com/50/v2-83c5a7310c2d3a431784b1228d64ae35_720w.jpg?source=1940ef5c',
  'https://ts1.cn.mm.bing.net/th/id/R-C.f7644b4034d476dad42166426c31e2f3?rik=o3cOGWU9jX2%2fEg&riu=http%3a%2f%2fn.sinaimg.cn%2fsinacn10113%2f58%2fw750h908%2f20190627%2f6420-hyzpvir4006614.jpg&ehk=SVA%2bf6ELmAtank4c31vpGJbU4X89E0ZwvNfbV3UTHcU%3d&risl=&pid=ImgRaw&r=0',
  'https://img.keaitupian.cn/uploads/upimg/1592459047193770.jpg',
  'https://img-baofun.zhhainiao.com/pcwallpaper_ugc_mobile/preview_jpg/82e40c5fb47fa98aacb47c305e4ee27c.jpg',
  'https://pic2.zhimg.com/v2-32a731a74335036f010ff2a2110d4e28_r.jpg',
  'https://wallpaperm.cmcm.com/4700eaf249b71d56d95aff8ca94313fa.jpg',
  'https://tse2-mm.cn.bing.net/th/id/OIP-C.2f9gdL3o5GNTl3G3Myma1AAAAA?rs=1&pid=ImgDetMain',
  'https://pic2.zhimg.com/v2-3aecfb4a857585d6eb2796902a565956_r.jpg?source=172ae18b',
  'https://p.qpic.cn/dnfbbspic/0/dnfbbs_dnfbbs_dnf_gamebbs_qq_com_forum_201911_22_083548z4dn4295a5ej89ad.jpg/0',
  'https://5b0988e595225.cdn.sohucs.com/images/20181029/ef4539a993824e7c9794dff06f0bdfd1.png',
  'https://tse1-mm.cn.bing.net/th/id/OIP-C.A1U1EzgHqfMUJvAUQNOALwHaLN?rs=1&pid=ImgDetMain',
  'https://ts1.cn.mm.bing.net/th/id/R-C.1092b972873293f4a63fc3e1c08ea9b3?rik=9jEW%2fahq3DXsBA&riu=http%3a%2f%2fimg2.100bt.com%2fupload%2fzl%2f20140509%2f1399637147977.jpg&ehk=1O3Bln%2bLSBXqT7oRhCkK1n609m4lPwWQEK1Ti%2bI%2bn0Y%3d&risl=&pid=ImgRaw&r=0',
  'https://ts1.cn.mm.bing.net/th/id/R-C.e5717389af85e7cea3a862a0276bad1d?rik=ahq6mEY1GC0BkA&riu=http%3a%2f%2fwww.puhuajia.com%2fdata%2fattachment%2fforum%2f202001%2f07%2f120726e7eem7792661qozw.png&ehk=GZBwLOKEWYGLGkgxFZCvVy9Mo3PWfvoQ75Hnl836A2s%3d&risl=&pid=ImgRaw&r=0',
  'https://gao7pic.gao7.com/a597b5551e4345119678baa57b95aba5.png'
].map((url, index) => ({
  id: 100 + index,
  url,
  title: '动漫壁纸',
  description: '精美插画'
}))

// 定义根相册数据
const albumData: AlbumData[] = [
  {
    id: 1,
    name: '动漫美图',
    photos: baseImages.slice(6, 14),
    subAlbums: [
      {
        id: 4,
        name: '人物集',
        photos: baseImages.slice(0, 6),
        subAlbums: [
          {
            id: 7,
            name: '最爱',
            photos: baseImages.slice(0, 6)
          }
        ]
      },
      {
        id: 5,
        name: '场景集',
        photos: baseImages.slice(0, 8)
      }
    ]
  },
  {
    id: 2,
    name: '风景图集',
    photos: baseImages.slice(14, 24),
    subAlbums: [
      {
        id: 6,
        name: '壁纸集',
        photos: baseImages.slice(0, 10),
        subAlbums: [
          {
            id: 8,
            name: '收藏',
            photos: baseImages.slice(0, 8)
          }
        ]
      }
    ]
  },
  {
    id: 3,
    name: '精选集',
    photos: baseImages.slice(18, 24)
  },
  {
    id: 44,
    name: '测试集',
    photos: []
  }
]

// 转换为原来的 galleries 格式的函数
const convertToGalleries = (albums: AlbumData[]): Record<string, GalleryItem[]> => {
  const galleries: Record<string, GalleryItem[]> = {
    'root': []
  }

  const processAlbum = (album: AlbumData) => {
    const parentKey = galleries[album.id.toString()] || []

    if (album.subAlbums) {
      album.subAlbums.forEach(subAlbum => {
        parentKey.push({
          type: 'album',
          data: {
            id: subAlbum.id,
            name: subAlbum.name,
            photos: subAlbum.photos
          }
        })

        processAlbum(subAlbum)
      })
    }

    album.photos.forEach(photo => {
      parentKey.push({
        type: 'photo',
        data: photo
      })
    })

    galleries[album.id.toString()] = parentKey
  }

  albums.forEach(album => {
    galleries.root.push({
      type: 'album',
      data: {
        id: album.id,
        name: album.name,
        photos: album.photos
      }
    })
    processAlbum(album)
  })

  galleries.root.push(...baseImages.slice(0, 6).map(img => ({
    type: 'photo' as const,
    data: img
  })))

  return galleries
}

// 使用新的数据结构
const galleries = ref(convertToGalleries(albumData))

const getCurrentGallery = () => {
  const path = currentPath.value.length === 0 ? 'root' : currentPath.value[currentPath.value.length - 1].toString()
  return galleries.value[path] || []
}

const getBreadcrumbPath = computed(() => {
  if (currentPath.value.length === 0) return '当前位置：/'

  // 获取完整的路径名称
  const names = currentPath.value.map(id => getBreadcrumbName(id))
  return `当前位置：/${names.join('/')}`
})

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

// 添加面包屑导航点击处理方法
const handleBreadcrumbClick = (index: number) => {
  if (index === -1) {
    currentPath.value = []
  } else {
    currentPath.value = currentPath.value.slice(0, index + 1)
  }
  // 关闭动端菜单
  closeMobileMenu()
}

// 添加 getBreadcrumbName 函数
const getBreadcrumbName = (id: number) => {
  // 在所有相册数据中查找对应的相册
  for (const [key, items] of Object.entries(galleries.value)) {
    const album = items.find(item =>
        item.type === 'album' && (item.data as AlbumData).id === id
    )
    if (album) {
      return (album.data as AlbumData).name
    }
  }
  return ''
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

// 添加 Album 类型定义
interface Album {
  id: number
  name: string
  photos: Photo[]
}

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
  overflow: hidden;
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
  top: 0;
  height: calc(100vh - 100px);
  overflow-y: auto;
  padding: 20px;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  border-radius: 12px;
  border: 1px solid rgba(92, 106, 196, 0.1);
  box-sizing: border-box;
  transition: all 0.3s ease;
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
