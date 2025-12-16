/**
 * B2B主题组合式函数
 * 提供主题切换、颜色管理等功能
 */

import { ref, computed, watch, onMounted } from 'vue';

// 主题配置
const themeConfig = {
  light: {
    primary: '#409eff',
    secondary: '#67c23a',
    warning: '#e6a23c',
    danger: '#f56c6c',
    info: '#909399',
    success: '#67c23a',
    bgPrimary: '#ffffff',
    bgSecondary: '#f5f7fa',
    bgTertiary: '#f0f2f5',
    textPrimary: '#303133',
    textSecondary: '#606266',
    textTertiary: '#909399',
    textDisabled: '#c0c4cc',
    borderPrimary: '#dcdfe6',
    borderSecondary: '#e4e7ed',
    borderTertiary: '#ebeef5'
  },
  dark: {
    primary: '#409eff',
    secondary: '#67c23a',
    warning: '#e6a23c',
    danger: '#f56c6c',
    info: '#909399',
    success: '#67c23a',
    bgPrimary: '#1a1a1a',
    bgSecondary: '#2a2a2a',
    bgTertiary: '#3a3a3a',
    textPrimary: '#ffffff',
    textSecondary: '#c0c4cc',
    textTertiary: '#909399',
    textDisabled: '#5a5c60',
    borderPrimary: '#4a4a4a',
    borderSecondary: '#5a5a5a',
    borderTertiary: '#6a6a6a'
  }
};

// 主题状态
const currentTheme = ref('light');
const systemTheme = ref('light');

// 计算属性：当前主题配置
const theme = computed(() => themeConfig[currentTheme.value]);

// 计算属性：是否为暗色主题
const isDark = computed(() => currentTheme.value === 'dark');

// 监听系统主题变化
const observeSystemTheme = () => {
  if (typeof uni !== 'undefined' && uni.onThemeChange) {
    uni.onThemeChange((res) => {
      systemTheme.value = res.theme;
      if (currentTheme.value === 'auto') {
        updateThemeVariables();
      }
    });
  }
};

// 获取系统主题
const getSystemTheme = () => {
  if (typeof uni !== 'undefined' && uni.getSystemInfoSync) {
    const systemInfo = uni.getSystemInfoSync();
    return systemInfo.theme || 'light';
  }
  return 'light';
};

// 更新主题变量
const updateThemeVariables = () => {
  const themeValue = currentTheme.value === 'auto' ? systemTheme.value : currentTheme.value;
  const colors = themeConfig[themeValue];
  
  // 设置CSS变量
  if (typeof document !== 'undefined') {
    const root = document.documentElement;
    Object.entries(colors).forEach(([key, value]) => {
      const cssVar = `--b2b-${key.replace(/([A-Z])/g, '-$1').toLowerCase()}`;
      root.style.setProperty(cssVar, value);
    });
  }
};

// 切换主题
const toggleTheme = (newTheme) => {
  if (newTheme && ['light', 'dark', 'auto'].includes(newTheme)) {
    currentTheme.value = newTheme;
  } else {
    // 循环切换：light -> dark -> auto -> light
    const themes = ['light', 'dark', 'auto'];
    const currentIndex = themes.indexOf(currentTheme.value);
    const nextIndex = (currentIndex + 1) % themes.length;
    currentTheme.value = themes[nextIndex];
  }
  
  // 保存主题设置
  saveThemeSetting();
  updateThemeVariables();
};

// 保存主题设置
const saveThemeSetting = () => {
  if (typeof uni !== 'undefined' && uni.setStorageSync) {
    try {
      uni.setStorageSync('b2b-theme', currentTheme.value);
    } catch (error) {
      console.warn('Failed to save theme setting:', error);
    }
  }
};

// 加载主题设置
const loadThemeSetting = () => {
  if (typeof uni !== 'undefined' && uni.getStorageSync) {
    try {
      const savedTheme = uni.getStorageSync('b2b-theme');
      if (savedTheme && ['light', 'dark', 'auto'].includes(savedTheme)) {
        currentTheme.value = savedTheme;
      }
    } catch (error) {
      console.warn('Failed to load theme setting:', error);
    }
  }
};

// 获取颜色值
const getColor = (colorName, alpha = 1) => {
  const color = theme.value[colorName];
  if (alpha < 1) {
    // 将十六进制颜色转换为RGBA
    const hex = color.replace('#', '');
    const r = parseInt(hex.substr(0, 2), 16);
    const g = parseInt(hex.substr(2, 2), 16);
    const b = parseInt(hex.substr(4, 2), 16);
    return `rgba(${r}, ${g}, ${b}, ${alpha})`;
  }
  return color;
};

// 生成渐变
const getGradient = (startColor, endColor, angle = 135) => {
  const start = theme.value[startColor] || startColor;
  const end = theme.value[endColor] || endColor;
  return `linear-gradient(${angle}deg, ${start}, ${end})`;
};

// 生成阴影
const getShadow = (color = 'primary', intensity = 'normal') => {
  const baseColor = theme.value[color] || color;
  const shadows = {
    light: `0 2rpx 8rpx ${getColor(color, 0.1)}`,
    normal: `0 4rpx 16rpx ${getColor(color, 0.15)}`,
    heavy: `0 8rpx 32rpx ${getColor(color, 0.2)}`
  };
  return shadows[intensity] || shadows.normal;
};

// 初始化主题
const initTheme = () => {
  // 获取系统主题
  systemTheme.value = getSystemTheme();
  
  // 加载保存的主题设置
  loadThemeSetting();
  
  // 监听系统主题变化
  observeSystemTheme();
  
  // 更新主题变量
  updateThemeVariables();
};

// 监听主题变化
watch(currentTheme, () => {
  updateThemeVariables();
});

watch(systemTheme, () => {
  if (currentTheme.value === 'auto') {
    updateThemeVariables();
  }
});

// 组合式函数
export function useTheme() {
  onMounted(() => {
    initTheme();
  });
  
  return {
    theme,
    currentTheme,
    isDark,
    toggleTheme,
    getColor,
    getGradient,
    getShadow,
    updateThemeVariables
  };
}

// 主题工具函数
export const themeUtils = {
  // 判断颜色是否为亮色
  isLightColor(color) {
    const hex = color.replace('#', '');
    const r = parseInt(hex.substr(0, 2), 16);
    const g = parseInt(hex.substr(2, 2), 16);
    const b = parseInt(hex.substr(4, 2), 16);
    const brightness = (r * 299 + g * 587 + b * 114) / 1000;
    return brightness > 128;
  },
  
  // 获取对比色（黑或白）
  getContrastColor(color) {
    return this.isLightColor(color) ? '#000000' : '#ffffff';
  },
  
  // 生成随机颜色
  randomColor() {
    const colors = ['primary', 'secondary', 'warning', 'danger', 'info', 'success'];
    return colors[Math.floor(Math.random() * colors.length)];
  },
  
  // 生成颜色变体
  getColorVariant(color, variant = 'default') {
    const baseColor = theme.value[color] || color;
    const variants = {
      light: getColor(color, 0.1),
      lighter: getColor(color, 0.05),
      dark: getColor(color, 0.8),
      darker: getColor(color, 0.6)
    };
    return variants[variant] || baseColor;
  }
};

// 主题预设
export const themePresets = {
  default: {
    primary: '#409eff',
    secondary: '#67c23a',
    warning: '#e6a23c',
    danger: '#f56c6c',
    info: '#909399',
    success: '#67c23a'
  },
  
  ocean: {
    primary: '#1890ff',
    secondary: '#52c41a',
    warning: '#faad14',
    danger: '#ff4d4f',
    info: '#8c8c8c',
    success: '#52c41a'
  },
  
  sunset: {
    primary: '#ff6b6b',
    secondary: '#4ecdc4',
    warning: '#ffe66d',
    danger: '#ff6b6b',
    info: '#95a5a6',
    success: '#4ecdc4'
  },
  
  forest: {
    primary: '#27ae60',
    secondary: '#3498db',
    warning: '#f39c12',
    danger: '#e74c3c',
    info: '#7f8c8d',
    success: '#27ae60'
  },
  
  purple: {
    primary: '#9b59b6',
    secondary: '#3498db',
    warning: '#f39c12',
    danger: '#e74c3c',
    info: '#95a5a6',
    success: '#27ae60'
  }
};

// 应用主题预设
export const applyThemePreset = (presetName) => {
  const preset = themePresets[presetName];
  if (preset) {
    Object.entries(preset).forEach(([key, value]) => {
      if (themeConfig.light[key]) {
        themeConfig.light[key] = value;
        themeConfig.dark[key] = value;
      }
    });
    updateThemeVariables();
  }
};

// 自定义主题
export const createCustomTheme = (customColors) => {
  return {
    ...theme.value,
    ...customColors
  };
};

// 主题验证
export const validateTheme = (themeConfig) => {
  const requiredColors = ['primary', 'secondary', 'warning', 'danger', 'info', 'success'];
  const missingColors = requiredColors.filter(color => !themeConfig[color]);
  
  if (missingColors.length > 0) {
    console.warn(`Theme is missing required colors: ${missingColors.join(', ')}`);
    return false;
  }
  
  // 验证颜色格式
  const colorRegex = /^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$/;
  const invalidColors = Object.entries(themeConfig)
    .filter(([key, value]) => requiredColors.includes(key) && !colorRegex.test(value))
    .map(([key]) => key);
  
  if (invalidColors.length > 0) {
    console.warn(`Theme has invalid color formats: ${invalidColors.join(', ')}`);
    return false;
  }
  
  return true;
};