import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'
import AutoImport from 'unplugin-auto-import/vite';

// https://vitejs.dev/config/
export default defineConfig(({mode}) => {
  
  const env = loadEnv(mode, process.cwd())
  
  return { 
    base: env.VITE_BASE,
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
      proxy: {}
    },
    resolve: {
      alias: {
        // 设置路径
        '~': path.resolve(__dirname, './'),
        // 设置别名
        '@': path.resolve(__dirname, './src')
      }
    },
    css: {
      preprocessorOptions: {
        scss: {
          // 确保 SCSS 预处理正确工作
        }
      }
    }
  }
})