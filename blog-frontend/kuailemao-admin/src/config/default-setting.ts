import type { LayoutSetting } from '~@/stores/app'

export default {
  'title': 'Ruyu-blog',
  'theme': 'light',
  'logo': 'https://image.kuailemao.xyz/blog/logo/blog后台logo.svg',
  'collapsed': false,
  'drawerVisible': true,
  'colorPrimary': '#1677FF',
  'layout': 'mix',
  'contentWidth': 'Fluid',
  'fixedHeader': false,
  'fixedSider': true,
  'splitMenus': true,
  'header': true,
  'menu': true,
  'watermark': true,
  'menuHeader': true,
  'footer': false,
  'colorWeak': false,
  'multiTab': true,
  'multiTabFixed': false,
  'keepAlive': true,
  'accordionMode': false,
  'leftCollapsed': true,
  'headerHeight': 48,
  'copyright': 'ruyu-blog 2024',
  'animationName': 'slide-fadein-right',
} as LayoutSetting

export const animationNameList = [
  {
    label: 'None',
    value: 'none',
  },
  {
    label: 'Fadein Up',
    value: 'slide-fadein-up',
  },
  {
    label: 'Fadein Right',
    value: 'slide-fadein-right',
  },
  {
    label: 'Zoom Fadein',
    value: 'zoom-fadein',
  },
  {
    label: 'Fadein',
    value: 'fadein',
  },
]
export type AnimationNameValueType = 'none' | 'slide-fadein-up' | 'slide-fadein-right' | 'zoom-fadein' | 'fadein'
