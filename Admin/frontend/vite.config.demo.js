import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'
import AutoImport from 'unplugin-auto-import/vite';

// 演示专用配置 - 绕过代理问题
export default defineConfig(() => {
  return { 
    base: "/",
    plugins: [
      vue(),
      AutoImport({
        imports: [
          'vue',
          'vue-router',
        ],
        dts: false
      }),
    ],
    server: {
      host: "0.0.0.0",
      port: 5173,
      // 禁用代理，避免连接后端失败
      proxy: {}
    },
    resolve: {
      alias: {
        '~': path.resolve(__dirname, './'),
        '@': path.resolve(__dirname, './src')
      }
    },
    // 构建配置
    build: {
      outDir: 'dist-demo',
      assetsDir: 'assets',
      sourcemap: false
    }
  }
})