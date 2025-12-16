<template>
  <div class="error-container" :class="{ 'full-screen': fullScreen }">
    <div class="error-content">
      <div class="error-icon">
        <el-icon :size="iconSize">
          <component :is="errorIcon" />
        </el-icon>
      </div>
      
      <h3 class="error-title">{{ title }}</h3>
      <p class="error-message">{{ message }}</p>
      
      <div class="error-actions">
        <el-button 
          v-if="showRetry" 
          type="primary" 
          @click="handleRetry"
        >
          {{ retryText }}
        </el-button>
        <el-button 
          v-if="showBack" 
          @click="handleBack"
        >
          {{ backText }}
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
// 图标已在main.ts中全局注册，无需单独导入

export interface Props {
  type?: 'network' | '404' | 'empty' | 'error' | 'search'
  title?: string
  message?: string
  showRetry?: boolean
  showBack?: boolean
  retryText?: string
  backText?: string
  fullScreen?: boolean
  iconSize?: number
}

const props = withDefaults(defineProps<Props>(), {
  type: 'error',
  showRetry: true,
  showBack: true,
  retryText: '重新加载',
  backText: '返回首页',
  fullScreen: false,
  iconSize: 64
})

const emit = defineEmits<{
  retry: []
}>()

const router = useRouter()

const errorIcon = computed(() => {
  const iconMap = {
    network: 'Connection',
    404: 'DocumentDelete',
    empty: 'PictureRounded',
    error: 'Warning',
    search: 'Search'
  }
  return iconMap[props.type]
})

const defaultTitle = computed(() => {
  const titleMap = {
    network: '网络连接失败',
    404: '页面不存在',
    empty: '暂无数据',
    error: '出错了',
    search: '搜索无结果'
  }
  return titleMap[props.type]
})

const defaultMessage = computed(() => {
  const messageMap = {
    network: '请检查网络连接后重试',
    404: '您访问的页面不存在或已被删除',
    empty: '暂无相关数据',
    error: '系统遇到了一些问题，请稍后重试',
    search: '没有找到相关内容，试试其他关键词'
  }
  return messageMap[props.type]
})

const title = computed(() => props.title || defaultTitle.value)
const message = computed(() => props.message || defaultMessage.value)

const handleRetry = () => {
  emit('retry')
}

const handleBack = () => {
  router.push('/')
}
</script>

<style scoped lang="scss">
.error-container {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  text-align: center;
  
  &.full-screen {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: #fff;
    z-index: 9998;
  }
}

.error-content {
  max-width: 400px;
}

.error-icon {
  margin-bottom: 20px;
  color: #c0c4cc;
  
  .el-icon {
    display: block;
    margin: 0 auto;
  }
}

.error-title {
  margin: 0 0 12px;
  font-size: 18px;
  font-weight: 500;
  color: #303133;
}

.error-message {
  margin: 0 0 24px;
  font-size: 14px;
  color: #909399;
  line-height: 1.5;
}

.error-actions {
  display: flex;
  gap: 12px;
  justify-content: center;
  
  .el-button {
    min-width: 100px;
  }
}

// 不同错误类型的样式
.error-container {
  &[data-type="network"] .error-icon {
    color: #f56c6c;
  }
  
  &[data-type="404"] .error-icon {
    color: #e6a23c;
  }
  
  &[data-type="empty"] .error-icon {
    color: #909399;
  }
  
  &[data-type="error"] .error-icon {
    color: #f56c6c;
  }
  
  &[data-type="search"] .error-icon {
    color: #409eff;
  }
}
</style>