import type { App } from 'vue'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

export function setupIcons(app: App) {
  // 注册 Element Plus 图标
  for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
  }
}