import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'
import AutoImport from 'unplugin-auto-import/vite';

// 简化版配置 - 绕过SCSS问题
export default defineConfig(() => {
  return { 
    base: "/",
    plugins: [
      vue({
        // 禁用SCSS预处理，使用纯CSS
        template: {
          compilerOptions: {
            // 禁用样式预处理
          }
        }
      }),
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
      proxy: {}
    },
    resolve: {
      alias: {
        '~': path.resolve(__dirname, './'),
        '@': path.resolve(__dirname, './src')
      }
    },
    css: {
      // 使用纯CSS，禁用预处理器
      preprocessorOptions: {
        scss: {
          // 空配置，避免SCSS处理错误
        },
        sass: {
          // 空配置，避免Sass处理错误
        }
      }
    },
    optimizeDeps: {
      // 排除有问题的依赖
      exclude: ['sass']
    }
  }
})