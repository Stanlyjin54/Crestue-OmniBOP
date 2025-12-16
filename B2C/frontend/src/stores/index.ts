import { createPinia } from 'pinia'
import type { App } from 'vue'

// 创建 pinia 实例
export const pinia = createPinia()

// 配置 pinia
export function setupStore(app: App) {
  app.use(pinia)
}

// 导出所有 store
export * from './user'
export * from './cart'