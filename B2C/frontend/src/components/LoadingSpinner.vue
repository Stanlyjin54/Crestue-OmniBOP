<template>
  <div class="loading-container" :class="{ 'full-screen': fullScreen }">
    <div class="loading-spinner">
      <div class="spinner-ring"></div>
      <div class="spinner-ring"></div>
      <div class="spinner-ring"></div>
    </div>
    <div v-if="text" class="loading-text">{{ text }}</div>
  </div>
</template>

<script setup lang="ts">
interface Props {
  text?: string
  fullScreen?: boolean
}

withDefaults(defineProps<Props>(), {
  text: '加载中...',
  fullScreen: false
})
</script>

<style scoped lang="scss">
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px;
  
  &.full-screen {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(255, 255, 255, 0.9);
    z-index: 9999;
  }
}

.loading-spinner {
  position: relative;
  width: 60px;
  height: 60px;
  margin-bottom: 16px;
}

.spinner-ring {
  position: absolute;
  width: 100%;
  height: 100%;
  border: 3px solid transparent;
  border-top-color: #409eff;
  border-radius: 50%;
  animation: spin 1.2s linear infinite;
  
  &:nth-child(1) {
    animation-delay: -0.45s;
  }
  
  &:nth-child(2) {
    animation-delay: -0.3s;
    border-top-color: #67c23a;
  }
  
  &:nth-child(3) {
    animation-delay: -0.15s;
    border-top-color: #e6a23c;
  }
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.loading-text {
  color: #909399;
  font-size: 14px;
  text-align: center;
}

// 小型加载动画
.mini-spinner {
  .loading-spinner {
    width: 20px;
    height: 20px;
    margin-bottom: 0;
  }
  
  .spinner-ring {
    border-width: 2px;
  }
  
  .loading-text {
    display: none;
  }
}
</style>