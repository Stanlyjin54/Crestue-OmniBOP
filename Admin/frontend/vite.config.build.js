import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'
import AutoImport from 'unplugin-auto-import/vite';

// 构建版配置 - 绕过SCSS问题
/**
 * 构建配置
 * @description 用于生产构建的简化配置，禁用SCSS预处理
 */
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
    resolve: {
      alias: {
        '~': path.resolve(__dirname, './'),
        '@': path.resolve(__dirname, './src')
      }
    },
    css: {
      // 使用纯CSS，禁用预处理器
      preprocessorOptions: {
        scss: false,
        sass: false
      }
    },
    optimizeDeps: {
      // 排除有问题的依赖
      exclude: ['sass']
    },
    build: {
      // 构建配置
      outDir: 'dist',
      assetsDir: 'assets',
      sourcemap: false,
      minify: 'terser'
    }
  }
})