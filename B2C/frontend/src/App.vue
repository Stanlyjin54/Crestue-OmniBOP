<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { ElMessage } from 'element-plus'

// 全局错误处理
onMounted(() => {
  window.addEventListener('unhandledrejection', (event) => {
    console.error('Unhandled promise rejection:', event.reason)
    ElMessage.error('系统错误，请稍后重试')
  })
})
</script>

<style>
#app {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  min-height: 100vh;
  background-color: #f5f5f5;
}

/* 全局样式重置 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: inherit;
  line-height: 1.5;
  color: #333;
  background-color: #f5f5f5;
}

/* 链接样式 */
a {
  text-decoration: none;
  color: inherit;
}

a:hover {
  color: #409eff;
}

/* 按钮样式 */
.el-button {
  border-radius: 4px;
}

/* 输入框样式 */
.el-input__inner {
  border-radius: 4px;
}

/* 卡片样式 */
.el-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

/* 消息提示样式 */
.el-message {
  border-radius: 4px;
}

/* 响应式布局 */
@media screen and (max-width: 768px) {
  .el-container {
    flex-direction: column;
  }
  
  .el-aside {
    width: 100% !important;
  }
}
</style>