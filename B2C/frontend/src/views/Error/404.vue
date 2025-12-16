<template>
  <div class="error-container">
    <div class="error-content">
      <div class="error-icon">
        <el-icon size="120" color="#f56c6c">
          <error />
        </el-icon>
      </div>
      <h1 class="error-title">404</h1>
      <p class="error-message">抱歉，您访问的页面不存在</p>
      <p class="error-description">请检查网址是否正确，或返回首页</p>
      <div class="error-actions">
        <el-button type="primary" @click="router.push('/')">
          返回首页
        </el-button>
        <el-button @click="router.back()">
          返回上一页
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()
</script>

<style scoped lang="scss">
.error-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.error-content {
  text-align: center;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  padding: 60px 40px;
  max-width: 500px;
  width: 100%;
}

.error-icon {
  margin-bottom: 20px;
}

.error-title {
  font-size: 72px;
  font-weight: bold;
  color: #f56c6c;
  margin-bottom: 10px;
}

.error-message {
  font-size: 24px;
  color: #333;
  margin-bottom: 10px;
}

.error-description {
  color: #666;
  font-size: 16px;
  margin-bottom: 30px;
}

.error-actions {
  display: flex;
  justify-content: center;
  gap: 15px;
}

// 响应式设计
@media screen and (max-width: 480px) {
  .error-content {
    padding: 40px 20px;
  }
  
  .error-title {
    font-size: 60px;
  }
  
  .error-message {
    font-size: 20px;
  }
  
  .error-actions {
    flex-direction: column;
    align-items: center;
  }
}
</style>