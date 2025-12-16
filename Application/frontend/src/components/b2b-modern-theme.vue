/**
 * B2B现代化主题样式
 * 基于uni-app和uView Plus的现代化UI设计
 */

<template>
  <view class="b2b-modern-theme">
    <!-- 主题配置 -->
    <view class="theme-config" :style="themeVariables">
      <!-- 内容插槽 -->
      <slot />
    </view>
  </view>
</template>

<script setup>
import { computed, provide } from 'vue';

// 主题配置
const props = defineProps({
  theme: {
    type: String,
    default: 'light', // light, dark, auto
    validator: (value) => ['light', 'dark', 'auto'].includes(value)
  },
  primaryColor: {
    type: String,
    default: '#409eff'
  },
  secondaryColor: {
    type: String,
    default: '#67c23a'
  },
  warningColor: {
    type: String,
    default: '#e6a23c'
  },
  dangerColor: {
    type: String,
    default: '#f56c6c'
  },
  infoColor: {
    type: String,
    default: '#909399'
  },
  successColor: {
    type: String,
    default: '#67c23a'
  },
  borderRadius: {
    type: String,
    default: '12rpx'
  },
  shadow: {
    type: String,
    default: '0 4rpx 20rpx rgba(0, 0, 0, 0.08)'
  }
});

// 主题变量
const themeVariables = computed(() => {
  const isDark = props.theme === 'dark' || (props.theme === 'auto' && uni.getSystemInfoSync().theme === 'dark');
  
  return {
    // 主色调
    '--b2b-primary': props.primaryColor,
    '--b2b-secondary': props.secondaryColor,
    '--b2b-warning': props.warningColor,
    '--b2b-danger': props.dangerColor,
    '--b2b-info': props.infoColor,
    '--b2b-success': props.successColor,
    
    // 背景色
    '--b2b-bg-primary': isDark ? '#1a1a1a' : '#ffffff',
    '--b2b-bg-secondary': isDark ? '#2a2a2a' : '#f5f7fa',
    '--b2b-bg-tertiary': isDark ? '#3a3a3a' : '#f0f2f5',
    
    // 文字色
    '--b2b-text-primary': isDark ? '#ffffff' : '#303133',
    '--b2b-text-secondary': isDark ? '#c0c4cc' : '#606266',
    '--b2b-text-tertiary': isDark ? '#909399' : '#909399',
    '--b2b-text-disabled': isDark ? '#5a5c60' : '#c0c4cc',
    
    // 边框色
    '--b2b-border-primary': isDark ? '#4a4a4a' : '#dcdfe6',
    '--b2b-border-secondary': isDark ? '#5a5a5a' : '#e4e7ed',
    '--b2b-border-tertiary': isDark ? '#6a6a6a' : '#ebeef5',
    
    // 圆角和阴影
    '--b2b-border-radius': props.borderRadius,
    '--b2b-shadow': props.shadow,
    '--b2b-shadow-light': isDark ? '0 2rpx 8rpx rgba(255, 255, 255, 0.1)' : '0 2rpx 8rpx rgba(0, 0, 0, 0.04)',
    '--b2b-shadow-heavy': isDark ? '0 8rpx 32rpx rgba(255, 255, 255, 0.15)' : '0 8rpx 32rpx rgba(0, 0, 0, 0.12)',
    
    // 渐变背景
    '--b2b-gradient-primary': `linear-gradient(135deg, ${props.primaryColor}, ${props.secondaryColor})`,
    '--b2b-gradient-secondary': `linear-gradient(135deg, ${props.secondaryColor}, ${props.successColor})`,
    
    // 间距
    '--b2b-spacing-xs': '8rpx',
    '--b2b-spacing-sm': '16rpx',
    '--b2b-spacing-md': '24rpx',
    '--b2b-spacing-lg': '32rpx',
    '--b2b-spacing-xl': '40rpx',
    
    // 字体大小
    '--b2b-font-size-xs': '20rpx',
    '--b2b-font-size-sm': '24rpx',
    '--b2b-font-size-base': '28rpx',
    '--b2b-font-size-lg': '32rpx',
    '--b2b-font-size-xl': '36rpx',
    '--b2b-font-size-2xl': '40rpx',
    '--b2b-font-size-3xl': '48rpx'
  };
});

// 提供主题上下文
provide('b2bTheme', {
  theme: props.theme,
  colors: {
    primary: props.primaryColor,
    secondary: props.secondaryColor,
    warning: props.warningColor,
    danger: props.dangerColor,
    info: props.infoColor,
    success: props.successColor
  }
});
</script>

<style lang="scss" scoped>
.b2b-modern-theme {
  display: contents;
}

.theme-config {
  // 全局样式重置
  color: var(--b2b-text-primary);
  background-color: var(--b2b-bg-primary);
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  
  // 深度选择器，影响子组件
  :deep() {
    // 按钮样式
    .b2b-btn {
      border-radius: var(--b2b-border-radius);
      box-shadow: var(--b2b-shadow-light);
      transition: all 0.3s ease;
      
      &:active {
        transform: scale(0.98);
      }
      
      &.b2b-btn-primary {
        background: var(--b2b-gradient-primary);
        color: white;
        border: none;
        
        &:hover {
          box-shadow: var(--b2b-shadow-heavy);
        }
      }
      
      &.b2b-btn-secondary {
        background-color: var(--b2b-bg-secondary);
        color: var(--b2b-text-secondary);
        border: 2rpx solid var(--b2b-border-primary);
        
        &:hover {
          background-color: var(--b2b-bg-tertiary);
        }
      }
      
      &.b2b-btn-success {
        background: var(--b2b-gradient-secondary);
        color: white;
        border: none;
      }
      
      &.b2b-btn-warning {
        background-color: var(--b2b-warning);
        color: white;
        border: none;
      }
      
      &.b2b-btn-danger {
        background-color: var(--b2b-danger);
        color: white;
        border: none;
      }
    }
    
    // 卡片样式
    .b2b-card {
      background-color: var(--b2b-bg-primary);
      border-radius: var(--b2b-border-radius);
      box-shadow: var(--b2b-shadow);
      padding: var(--b2b-spacing-md);
      transition: all 0.3s ease;
      
      &:hover {
        box-shadow: var(--b2b-shadow-heavy);
        transform: translateY(-2rpx);
      }
    }
    
    // 输入框样式
    .b2b-input {
      background-color: var(--b2b-bg-secondary);
      border: 2rpx solid var(--b2b-border-primary);
      border-radius: var(--b2b-border-radius);
      color: var(--b2b-text-primary);
      padding: var(--b2b-spacing-sm);
      
      &:focus {
        border-color: var(--b2b-primary);
        box-shadow: 0 0 0 4rpx rgba(64, 158, 255, 0.2);
      }
      
      &::placeholder {
        color: var(--b2b-text-tertiary);
      }
    }
    
    // 标签样式
    .b2b-tag {
      border-radius: calc(var(--b2b-border-radius) / 2);
      padding: var(--b2b-spacing-xs) var(--b2b-spacing-sm);
      font-size: var(--b2b-font-size-xs);
      font-weight: 500;
      
      &.b2b-tag-primary {
        background-color: rgba(64, 158, 255, 0.1);
        color: var(--b2b-primary);
        border: 2rpx solid rgba(64, 158, 255, 0.2);
      }
      
      &.b2b-tag-success {
        background-color: rgba(103, 194, 58, 0.1);
        color: var(--b2b-success);
        border: 2rpx solid rgba(103, 194, 58, 0.2);
      }
      
      &.b2b-tag-warning {
        background-color: rgba(230, 162, 60, 0.1);
        color: var(--b2b-warning);
        border: 2rpx solid rgba(230, 162, 60, 0.2);
      }
      
      &.b2b-tag-danger {
        background-color: rgba(245, 108, 108, 0.1);
        color: var(--b2b-danger);
        border: 2rpx solid rgba(245, 108, 108, 0.2);
      }
    }
    
    // 表格样式
    .b2b-table {
      background-color: var(--b2b-bg-primary);
      border-radius: var(--b2b-border-radius);
      overflow: hidden;
      box-shadow: var(--b2b-shadow-light);
      
      .b2b-table-header {
        background: var(--b2b-gradient-primary);
        color: white;
        font-weight: 600;
      }
      
      .b2b-table-row {
        &:nth-child(even) {
          background-color: var(--b2b-bg-secondary);
        }
        
        &:hover {
          background-color: var(--b2b-bg-tertiary);
        }
      }
    }
    
    // 模态框样式
    .b2b-modal {
      background-color: var(--b2b-bg-primary);
      border-radius: var(--b2b-border-radius);
      box-shadow: var(--b2b-shadow-heavy);
      
      .b2b-modal-header {
        border-bottom: 2rpx solid var(--b2b-border-primary);
        padding: var(--b2b-spacing-md);
        
        .b2b-modal-title {
          color: var(--b2b-text-primary);
          font-size: var(--b2b-font-size-lg);
          font-weight: 600;
        }
      }
      
      .b2b-modal-body {
        padding: var(--b2b-spacing-md);
        color: var(--b2b-text-secondary);
      }
      
      .b2b-modal-footer {
        border-top: 2rpx solid var(--b2b-border-primary);
        padding: var(--b2b-spacing-md);
      }
    }
    
    // 导航栏样式
    .b2b-navbar {
      background: var(--b2b-gradient-primary);
      color: white;
      box-shadow: var(--b2b-shadow);
      
      .b2b-navbar-title {
        font-size: var(--b2b-font-size-lg);
        font-weight: 600;
      }
    }
    
    // 侧边栏样式
    .b2b-sidebar {
      background-color: var(--b2b-bg-primary);
      border-right: 2rpx solid var(--b2b-border-primary);
      
      .b2b-sidebar-item {
        color: var(--b2b-text-secondary);
        transition: all 0.3s ease;
        
        &:hover {
          background-color: var(--b2b-bg-secondary);
          color: var(--b2b-primary);
        }
        
        &.active {
          background-color: rgba(64, 158, 255, 0.1);
          color: var(--b2b-primary);
          border-right: 4rpx solid var(--b2b-primary);
        }
      }
    }
    
    // 加载动画
    .b2b-loading {
      display: inline-block;
      width: 40rpx;
      height: 40rpx;
      border: 4rpx solid var(--b2b-border-primary);
      border-radius: 50%;
      border-top-color: var(--b2b-primary);
      animation: b2b-spin 1s ease-in-out infinite;
    }
    
    @keyframes b2b-spin {
      to {
        transform: rotate(360deg);
      }
    }
    
    // 动画效果
    .b2b-fade-in {
      animation: b2b-fade-in 0.3s ease-out;
    }
    
    .b2b-slide-up {
      animation: b2b-slide-up 0.3s ease-out;
    }
    
    .b2b-bounce {
      animation: b2b-bounce 0.5s ease-in-out;
    }
    
    @keyframes b2b-fade-in {
      from {
        opacity: 0;
      }
      to {
        opacity: 1;
      }
    }
    
    @keyframes b2b-slide-up {
      from {
        transform: translateY(100%);
        opacity: 0;
      }
      to {
        transform: translateY(0);
        opacity: 1;
      }
    }
    
    @keyframes b2b-bounce {
      0%, 20%, 53%, 80%, 100% {
        transform: translateY(0);
      }
      40%, 43% {
        transform: translateY(-20rpx);
      }
      70% {
        transform: translateY(-10rpx);
      }
      90% {
        transform: translateY(-4rpx);
      }
    }
  }
}
</style>