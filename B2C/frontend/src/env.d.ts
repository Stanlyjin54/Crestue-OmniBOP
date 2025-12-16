/// <reference types="vite/client" />
/// <reference types="vue" />

interface ImportMetaEnv {
  readonly VITE_API_BASE_URL: string
  // 可以添加其他环境变量
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}

// Vue文件模块声明
declare module '*.vue' {
  import type { DefineComponent } from 'vue'
  const component: DefineComponent<{}, {}, any>
  export default component
}

// 解析.vue文件的路径
declare module '@/*.vue' {
  import type { DefineComponent } from 'vue'
  const component: DefineComponent<{}, {}, any>
  export default component
}