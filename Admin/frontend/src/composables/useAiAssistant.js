import { ref, computed } from 'vue'
import {
  getAiAssistantLogs,
  getAiAssistantLogDetail,
  deleteAiAssistantLog,
  batchDeleteAiAssistantLogs,
  getAiAssistantLogStatistics,
  getAiAssistantUsageTrend,
  getAiAssistantTopUsers,
  getAiAssistantPopularFeatures,
  getAiAssistantConfig,
  updateAiAssistantConfig,
  getAiAssistantPerformance,
  getAiAssistantErrors,
  exportAiAssistantLogs,
  exportAiAssistantStatistics
} from '@/api/ai_assistant/ai_assistant'

// AI助手状态管理
const aiAssistantState = ref({
  logs: [],
  statistics: {
    totalRequests: 0,
    successRate: 0,
    avgResponseTime: 0,
    totalUsers: 0,
    popularFeatures: [],
    topUsers: []
  },
  performance: {
    responseTime: [],
    errorRate: [],
    throughput: []
  },
  config: {
    maxTokens: 4000,
    temperature: 0.7,
    enableHistory: true,
    historyLimit: 50,
    enableLogging: true
  },
  loading: false,
  total: 0,
  currentPage: 1,
  pageSize: 20
})

// AI助手日志列表
export function useAiAssistantLogs() {
  const state = aiAssistantState.value

  // 获取AI助手日志列表
  const fetchLogs = async (params = {}) => {
    try {
      state.loading = true
      const queryParams = {
        page: state.currentPage,
        pageSize: state.pageSize,
        ...params
      }
      
      const response = await getAiAssistantLogs(queryParams)
      state.logs = response.data.records || []
      state.total = response.data.total || 0
      state.currentPage = response.data.current || 1
      state.pageSize = response.data.size || 20
      
      return response
    } catch (error) {
      console.error('获取AI助手日志失败:', error)
      throw error
    } finally {
      state.loading = false
    }
  }

  // 获取AI助手日志详情
  const fetchLogDetail = async (id) => {
    try {
      state.loading = true
      const response = await getAiAssistantLogDetail(id)
      return response.data
    } catch (error) {
      console.error('获取AI助手日志详情失败:', error)
      throw error
    } finally {
      state.loading = false
    }
  }

  // 删除AI助手日志
  const deleteLog = async (id) => {
    try {
      state.loading = true
      await deleteAiAssistantLog(id)
      await fetchLogs()
      return true
    } catch (error) {
      console.error('删除AI助手日志失败:', error)
      throw error
    } finally {
      state.loading = false
    }
  }

  // 批量删除AI助手日志
  const batchDeleteLogs = async (ids) => {
    try {
      state.loading = true
      await batchDeleteAiAssistantLogs(ids)
      await fetchLogs()
      return true
    } catch (error) {
      console.error('批量删除AI助手日志失败:', error)
      throw error
    } finally {
      state.loading = false
    }
  }

  // 导出AI助手日志
  const exportLogs = async (params = {}) => {
    try {
      state.loading = true
      const response = await exportAiAssistantLogs(params)
      
      // 创建下载链接
      const blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.download = `ai_assistant_logs_${new Date().getTime()}.xlsx`
      link.click()
      window.URL.revokeObjectURL(url)
      
      return true
    } catch (error) {
      console.error('导出AI助手日志失败:', error)
      throw error
    } finally {
      state.loading = false
    }
  }

  return {
    logs: computed(() => state.logs),
    loading: computed(() => state.loading),
    total: computed(() => state.total),
    currentPage: computed(() => state.currentPage),
    pageSize: computed(() => state.pageSize),
    fetchLogs,
    fetchLogDetail,
    deleteLog,
    batchDeleteLogs,
    exportLogs,
    // 分页操作
    setCurrentPage: (page) => {
      state.currentPage = page
    },
    setPageSize: (size) => {
      state.pageSize = size
    }
  }
}

// AI助手统计管理
export function useAiAssistantStatistics() {
  const state = aiAssistantState.value

  // 获取AI助手统计
  const fetchStatistics = async (params = {}) => {
    try {
      state.loading = true
      const response = await getAiAssistantLogStatistics(params)
      state.statistics = {
        totalRequests: response.data.totalRequests || 0,
        successRate: response.data.successRate || 0,
        avgResponseTime: response.data.avgResponseTime || 0,
        totalUsers: response.data.totalUsers || 0,
        popularFeatures: response.data.popularFeatures || [],
        topUsers: response.data.topUsers || []
      }
      return response
    } catch (error) {
      console.error('获取AI助手统计失败:', error)
      throw error
    } finally {
      state.loading = false
    }
  }

  // 获取使用趋势
  const fetchUsageTrend = async (params = {}) => {
    try {
      const response = await getAiAssistantUsageTrend(params)
      return response.data
    } catch (error) {
      console.error('获取AI助手使用趋势失败:', error)
      throw error
    }
  }

  // 获取热门用户
  const fetchTopUsers = async (params = {}) => {
    try {
      const response = await getAiAssistantTopUsers(params)
      return response.data
    } catch (error) {
      console.error('获取AI助手热门用户失败:', error)
      throw error
    }
  }

  // 获取热门功能
  const fetchPopularFeatures = async (params = {}) => {
    try {
      const response = await getAiAssistantPopularFeatures(params)
      return response.data
    } catch (error) {
      console.error('获取AI助手热门功能失败:', error)
      throw error
    }
  }

  // 导出统计数据
  const exportStatistics = async (params = {}) => {
    try {
      const response = await exportAiAssistantStatistics(params)
      
      // 创建下载链接
      const blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.download = `ai_assistant_statistics_${new Date().getTime()}.xlsx`
      link.click()
      window.URL.revokeObjectURL(url)
      
      return true
    } catch (error) {
      console.error('导出AI助手统计失败:', error)
      throw error
    }
  }

  return {
    statistics: computed(() => state.statistics),
    loading: computed(() => state.loading),
    fetchStatistics,
    fetchUsageTrend,
    fetchTopUsers,
    fetchPopularFeatures,
    exportStatistics
  }
}

// AI助手性能分析
export function useAiAssistantPerformance() {
  const state = aiAssistantState.value

  // 获取性能数据
  const fetchPerformance = async (params = {}) => {
    try {
      state.loading = true
      const response = await getAiAssistantPerformance(params)
      state.performance = {
        responseTime: response.data.responseTime || [],
        errorRate: response.data.errorRate || [],
        throughput: response.data.throughput || []
      }
      return response
    } catch (error) {
      console.error('获取AI助手性能数据失败:', error)
      throw error
    } finally {
      state.loading = false
    }
  }

  // 获取错误分析
  const fetchErrors = async (params = {}) => {
    try {
      state.loading = true
      const response = await getAiAssistantErrors(params)
      return response.data
    } catch (error) {
      console.error('获取AI助手错误分析失败:', error)
      throw error
    } finally {
      state.loading = false
    }
  }

  return {
    performance: computed(() => state.performance),
    loading: computed(() => state.loading),
    fetchPerformance,
    fetchErrors
  }
}

// AI助手配置管理
export function useAiAssistantConfig() {
  const state = aiAssistantState.value

  // 获取配置
  const fetchConfig = async () => {
    try {
      state.loading = true
      const response = await getAiAssistantConfig()
      state.config = response.data || state.config
      return response
    } catch (error) {
      console.error('获取AI助手配置失败:', error)
      throw error
    } finally {
      state.loading = false
    }
  }

  // 更新配置
  const updateConfig = async (config) => {
    try {
      state.loading = true
      const response = await updateAiAssistantConfig(config)
      state.config = { ...state.config, ...config }
      return response
    } catch (error) {
      console.error('更新AI助手配置失败:', error)
      throw error
    } finally {
      state.loading = false
    }
  }

  return {
    config: computed(() => state.config),
    loading: computed(() => state.loading),
    fetchConfig,
    updateConfig
  }
}

// AI助手功能配置
export const aiAssistantFeatures = {
  // 功能类型
  types: {
    CHAT: '聊天对话',
    TEXT_GENERATION: '文本生成',
    TRANSLATION: '翻译',
    SUMMARIZATION: '总结摘要',
    ANALYSIS: '数据分析',
    RECOMMENDATION: '智能推荐',
    OCR: '文字识别',
    SENTIMENT: '情感分析'
  },

  // 优先级配置
  priorities: {
    LOW: '低',
    NORMAL: '普通',
    HIGH: '高',
    CRITICAL: '紧急'
  },

  // 状态配置
  statuses: {
    ACTIVE: '活跃',
    INACTIVE: '非活跃',
    MAINTENANCE: '维护中',
    ERROR: '错误'
  }
}

// AI助手统计配置
export const aiAssistantStatisticsConfig = {
  totalRequests: {
    label: '总请求数',
    value: 0,
    unit: '次',
    color: '#409EFF',
    icon: 'ChatDotRound'
  },
  successRate: {
    label: '成功率',
    value: 0,
    unit: '%',
    color: '#67C23A',
    icon: 'SuccessFilled'
  },
  avgResponseTime: {
    label: '平均响应时间',
    value: 0,
    unit: 'ms',
    color: '#E6A23C',
    icon: 'Timer'
  },
  totalUsers: {
    label: '总用户数',
    value: 0,
    unit: '人',
    color: '#F56C6C',
    icon: 'User'
  },
  dailyActiveUsers: {
    label: '日活跃用户',
    value: 0,
    unit: '人',
    color: '#909399',
    icon: 'UserFilled'
  },
  peakConcurrency: {
    label: '峰值并发',
    value: 0,
    unit: '个',
    color: '#909399',
    icon: 'Connection'
  }
}

export default {
  useAiAssistantLogs,
  useAiAssistantStatistics,
  useAiAssistantPerformance,
  useAiAssistantConfig,
  aiAssistantFeatures,
  aiAssistantStatisticsConfig
}