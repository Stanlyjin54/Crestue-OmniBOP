/**
 * B2B主题系统组合式函数
 * 提供主题切换、颜色管理等功能
 */

import { ref, computed, watch, onMounted, onUnmounted } from 'vue'

// 主题配置
const themes = {
  light: {
    name: '浅色主题',
    primary: '#409EFF',
    success: '#67C23A',
    warning: '#E6A23C',
    danger: '#F56C6C',
    info: '#909399',
    background: '#ffffff',
    surface: '#f5f7fa',
    text: {
      primary: '#303133',
      secondary: '#606266',
      disabled: '#C0C4CC'
    },
    border: '#DCDFE6',
    shadow: '0 2px 12px 0 rgba(0, 0, 0, 0.1)'
  },
  dark: {
    name: '深色主题',
    primary: '#409EFF',
    success: '#67C23A',
    warning: '#E6A23C',
    danger: '#F56C6C',
    info: '#909399',
    background: '#141414',
    surface: '#1f1f1f',
    text: {
      primary: '#E4E7ED',
      secondary: '#CFD3DC',
      disabled: '#8D9095'
    },
    border: '#4C4D4F',
    shadow: '0 2px 12px 0 rgba(0, 0, 0, 0.3)'
  },
  b2b: {
    name: 'B2B商务主题',
    primary: '#2c5aa0',
    success: '#52c41a',
    warning: '#faad14',
    danger: '#ff4d4f',
    info: '#1890ff',
    background: '#f0f2f5',
    surface: '#ffffff',
    text: {
      primary: '#262626',
      secondary: '#595959',
      disabled: '#bfbfbf'
    },
    border: '#d9d9d9',
    shadow: '0 2px 8px rgba(0, 0, 0, 0.09)'
  }
}

// 主题状态
const currentTheme = ref('b2b') // 默认使用B2B主题
const isDark = computed(() => currentTheme.value === 'dark')
const themeColors = computed(() => themes[currentTheme.value] || themes.b2b)

/**
 * 主题系统组合式函数
 */
export function useTheme() {
  // 初始化主题
  const initTheme = () => {
    const savedTheme = localStorage.getItem('admin-theme')
    if (savedTheme && themes[savedTheme]) {
      currentTheme.value = savedTheme
    } else {
      // 检测系统主题偏好
      const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches
      currentTheme.value = prefersDark ? 'dark' : 'b2b'
    }
    applyTheme()
  }

  // 应用主题
  const applyTheme = () => {
    const theme = themeColors.value
    const root = document.documentElement
    
    // 设置CSS变量
    Object.entries(theme).forEach(([key, value]) => {
      if (typeof value === 'object') {
        Object.entries(value).forEach(([subKey, subValue]) => {
          root.style.setProperty(`--theme-${key}-${subKey}`, subValue)
        })
      } else {
        root.style.setProperty(`--theme-${key}`, value)
      }
    })

    // 设置Element Plus主题
    updateElementPlusTheme()
    
    // 保存主题设置
    localStorage.setItem('admin-theme', currentTheme.value)
    
    // 更新body类名
    document.body.classList.remove('theme-light', 'theme-dark', 'theme-b2b')
    document.body.classList.add(`theme-${currentTheme.value}`)
  }

  // 更新Element Plus主题
  const updateElementPlusTheme = () => {
    const theme = themeColors.value
    const style = document.getElementById('element-plus-theme') || document.createElement('style')
    style.id = 'element-plus-theme'
    
    style.textContent = `
      .el-button--primary {
        background-color: ${theme.primary};
        border-color: ${theme.primary};
      }
      .el-button--primary:hover {
        background-color: ${getColorShade(theme.primary, -10)};
        border-color: ${getColorShade(theme.primary, -10)};
      }
      .el-menu--horizontal .el-menu-item:hover {
        background-color: ${getColorShade(theme.primary, 90)};
      }
      .el-menu--horizontal .el-menu-item.is-active {
        color: ${theme.primary};
        border-bottom-color: ${theme.primary};
      }
      .el-table th {
        background-color: ${getColorShade(theme.surface, 5)};
      }
      .el-table tr:hover {
        background-color: ${getColorShade(theme.surface, 10)};
      }
    `
    
    if (!document.getElementById('element-plus-theme')) {
      document.head.appendChild(style)
    }
  }

  // 切换主题
  const toggleTheme = (themeName) => {
    if (themes[themeName]) {
      currentTheme.value = themeName
      applyTheme()
    }
  }

  // 获取颜色
  const getColor = (colorName, shade = 0) => {
    const color = themeColors.value[colorName]
    return shade ? getColorShade(color, shade) : color
  }

  // 生成颜色渐变
  const getGradient = (color1, color2, angle = 135) => {
    return `linear-gradient(${angle}deg, ${getColor(color1)}, ${getColor(color2)})`
  }

  // 生成阴影
  const getShadow = (opacity = 0.1) => {
    const shadowColor = isDark.value ? `rgba(0, 0, 0, ${opacity})` : `rgba(0, 0, 0, ${opacity})`
    return `0 2px 12px 0 ${shadowColor}`
  }

  // 监听系统主题变化
  const setupSystemThemeListener = () => {
    const mediaQuery = window.matchMedia('(prefers-color-scheme: dark)')
    const handleChange = (e) => {
      if (!localStorage.getItem('admin-theme')) {
        currentTheme.value = e.matches ? 'dark' : 'b2b'
        applyTheme()
      }
    }
    
    mediaQuery.addEventListener('change', handleChange)
    
    onUnmounted(() => {
      mediaQuery.removeEventListener('change', handleChange)
    })
  }

  // 颜色加深/变浅工具函数
  const getColorShade = (color, percent) => {
    const num = parseInt(color.replace("#", ""), 16)
    const amt = Math.round(2.55 * percent)
    const R = (num >> 16) + amt
    const G = (num >> 8 & 0x00FF) + amt
    const B = (num & 0x0000FF) + amt
    return "#" + (0x1000000 + (R < 255 ? R < 1 ? 0 : R : 255) * 0x10000 +
      (G < 255 ? G < 1 ? 0 : G : 255) * 0x100 +
      (B < 255 ? B < 1 ? 0 : B : 255)).toString(16).slice(1)
  }

  // 初始化
  onMounted(() => {
    initTheme()
    setupSystemThemeListener()
  })

  return {
    currentTheme,
    isDark,
    themeColors,
    themes: Object.keys(themes),
    toggleTheme,
    getColor,
    getGradient,
    getShadow
  }
}

/**
 * 主题工具函数
 */
export const themeUtils = {
  // 预设主题
  presets: themes,
  
  // 创建自定义主题
  createCustomTheme: (name, config) => {
    themes[name] = { ...themes.b2b, ...config, name }
    return themes[name]
  },
  
  // 获取主题列表
  getThemeList: () => {
    return Object.entries(themes).map(([key, theme]) => ({
      key,
      name: theme.name,
      primary: theme.primary
    }))
  }
}