import type { App } from 'vue'
import router from './index'

export async function setupRouter(app: App) {
  app.use(router)
  
  // 等待路由准备就绪
  await router.isReady()
  
  return router
}