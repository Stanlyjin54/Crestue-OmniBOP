import { createApp } from 'vue'
import './styles/index.scss'
import App from './App.vue'
import router from './router'
import { setupStore } from './stores'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)

// 注册Element Plus图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 配置Element Plus
app.use(ElementPlus, {
  size: 'default',
  zIndex: 3000,
})

// 配置路由
app.use(router)

// 配置状态管理
setupStore(app)

// 初始化用户信息
import { useUserStore } from '@/stores/user'
const userStore = useUserStore()
userStore.initUser()

app.mount('#app')