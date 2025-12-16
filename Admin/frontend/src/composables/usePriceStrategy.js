import { ref, reactive, computed } from 'vue'
import { ElMessage } from 'element-plus'

// 价格策略相关的API端点
const API_ENDPOINTS = {
  // 策略管理
  STRATEGY_LIST: '/api/price-strategy/list',
  STRATEGY_CREATE: '/api/price-strategy/create',
  STRATEGY_UPDATE: '/api/price-strategy/update',
  STRATEGY_DELETE: '/api/price-strategy/delete',
  STRATEGY_TOGGLE: '/api/price-strategy/toggle-status',
  STRATEGY_CLONE: '/api/price-strategy/clone',
  
  // 价格计算
  CALCULATE_PRICE: '/api/price-strategy/calculate',
  BATCH_CALCULATE: '/api/price-strategy/batch-calculate',
  
  // 统计分析
  STRATEGY_ANALYTICS: '/api/price-strategy/analytics',
  STRATEGY_PERFORMANCE: '/api/price-strategy/performance',
  
  // 导入导出
  STRATEGY_EXPORT: '/api/price-strategy/export',
  STRATEGY_IMPORT: '/api/price-strategy/import'
}

// 策略类型配置
export const STRATEGY_TYPES = {
  DISCOUNT: {
    value: 'DISCOUNT',
    label: '折扣策略',
    description: '按折扣率计算优惠',
    icon: 'Discount'
  },
  FIXED_AMOUNT: {
    value: 'FIXED_AMOUNT',
    label: '固定金额',
    description: '固定金额优惠',
    icon: 'Money'
  },
  TIERED: {
    value: 'TIERED',
    label: '阶梯价格',
    description: '根据购买数量阶梯定价',
    icon: 'TrendCharts'
  },
  BUY_X_GET_Y: {
    value: 'BUY_X_GET_Y',
    label: '买X送Y',
    description: '买X件商品送Y件',
    icon: 'ShoppingCart'
  },
  BUNDLE: {
    value: 'BUNDLE',
    label: '捆绑销售',
    description: '产品捆绑销售优惠',
    icon: 'Box'
  }
}

// 渠道类型配置
export const CHANNEL_TYPES = {
  B2C: {
    value: 'B2C',
    label: 'B2C零售',
    description: '面向个人消费者'
  },
  B2B: {
    value: 'B2B',
    label: 'B2B批发',
    description: '面向企业客户'
  },
  ALL: {
    value: 'ALL',
    label: '全渠道',
    description: '适用于所有渠道'
  }
}

// 用户类型配置
export const USER_TYPES = {
  INDIVIDUAL: {
    value: 'INDIVIDUAL',
    label: '个人用户',
    description: '普通个人消费者'
  },
  ENTERPRISE: {
    value: 'ENTERPRISE',
    label: '企业用户',
    description: '企业客户'
  },
  NEW_USER: {
    value: 'NEW_USER',
    label: '新用户',
    description: '首次购买用户'
  },
  VIP: {
    value: 'VIP',
    label: 'VIP用户',
    description: 'VIP会员用户'
  }
}

// 策略状态配置
export const STRATEGY_STATUS = {
  ACTIVE: {
    value: 'ACTIVE',
    label: '激活',
    tagType: 'success',
    description: '策略正在生效'
  },
  INACTIVE: {
    value: 'INACTIVE',
    label: '停用',
    tagType: 'info',
    description: '策略已停用'
  },
  EXPIRED: {
    value: 'EXPIRED',
    label: '已过期',
    tagType: 'danger',
    description: '策略已过期'
  }
}

/**
 * 价格策略管理组合式函数
 */
export function usePriceStrategy() {
  // 响应式数据
  const strategyList = ref([])
  const currentStrategy = ref(null)
  const loading = ref(false)
  
  // 统计数据
  const stats = reactive({
    activeStrategies: 0,
    todayCalculations: 0,
    averageDiscount: 0,
    strategyTypes: Object.keys(STRATEGY_TYPES).length,
    totalDiscounts: 0,
    conversionRate: 0,
    roi: 0
  })
  
  // 搜索和筛选
  const searchForm = reactive({
    strategyName: '',
    strategyType: '',
    channelType: '',
    userType: '',
    status: '',
    dateRange: []
  })
  
  // 分页
  const pagination = reactive({
    currentPage: 1,
    pageSize: 20,
    total: 0
  })
  
  // 价格计算结果缓存
  const calculationCache = ref(new Map())
  
  // 计算属性
  const filteredStrategies = computed(() => {
    let filtered = [...strategyList.value]
    
    // 按策略名称筛选
    if (searchForm.strategyName) {
      filtered = filtered.filter(s => 
        s.strategyName.includes(searchForm.strategyName)
      )
    }
    
    // 按策略类型筛选
    if (searchForm.strategyType) {
      filtered = filtered.filter(s => s.strategyType === searchForm.strategyType)
    }
    
    // 按渠道类型筛选
    if (searchForm.channelType) {
      filtered = filtered.filter(s => 
        s.channelType === searchForm.channelType || s.channelType === 'ALL'
      )
    }
    
    // 按用户类型筛选
    if (searchForm.userType) {
      filtered = filtered.filter(s => s.userType === searchForm.userType)
    }
    
    // 按状态筛选
    if (searchForm.status) {
      filtered = filtered.filter(s => s.status === searchForm.status)
    }
    
    return filtered
  })
  
  const activeStrategies = computed(() => 
    strategyList.value.filter(s => s.status === 'ACTIVE')
  )
  
  // 方法
  
  /**
   * 获取策略列表
   */
  const fetchStrategyList = async (params = {}) => {
    loading.value = true
    try {
      const requestParams = {
        page: pagination.currentPage,
        pageSize: pagination.pageSize,
        ...searchForm,
        ...params
      }
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 500))
      
      // 这里应该是实际的API调用
      // const response = await request.get(API_ENDPOINTS.STRATEGY_LIST, { params: requestParams })
      
      // 模拟数据
      const mockData = generateMockStrategies()
      strategyList.value = mockData
      pagination.total = mockData.length
      
      updateStats()
      
      return {
        success: true,
        data: mockData,
        total: pagination.total
      }
    } catch (error) {
      console.error('获取策略列表失败:', error)
      ElMessage.error('获取策略列表失败')
      return {
        success: false,
        error: error.message
      }
    } finally {
      loading.value = false
    }
  }
  
  /**
   * 创建策略
   */
  const createStrategy = async (strategyData) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      // 这里应该是实际的API调用
      // const response = await request.post(API_ENDPOINTS.STRATEGY_CREATE, strategyData)
      
      // 模拟创建成功
      const newStrategy = {
        id: Date.now(),
        ...strategyData,
        createTime: new Date().toISOString(),
        updateTime: new Date().toISOString()
      }
      
      strategyList.value.unshift(newStrategy)
      pagination.total++
      updateStats()
      
      ElMessage.success('策略创建成功')
      return {
        success: true,
        data: newStrategy
      }
    } catch (error) {
      console.error('创建策略失败:', error)
      ElMessage.error('创建策略失败')
      return {
        success: false,
        error: error.message
      }
    } finally {
      loading.value = false
    }
  }
  
  /**
   * 更新策略
   */
  const updateStrategy = async (id, strategyData) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 800))
      
      // 这里应该是实际的API调用
      // const response = await request.put(`${API_ENDPOINTS.STRATEGY_UPDATE}/${id}`, strategyData)
      
      // 模拟更新成功
      const index = strategyList.value.findIndex(s => s.id === id)
      if (index !== -1) {
        strategyList.value[index] = {
          ...strategyList.value[index],
          ...strategyData,
          updateTime: new Date().toISOString()
        }
        updateStats()
      }
      
      ElMessage.success('策略更新成功')
      return {
        success: true,
        data: strategyList.value[index]
      }
    } catch (error) {
      console.error('更新策略失败:', error)
      ElMessage.error('更新策略失败')
      return {
        success: false,
        error: error.message
      }
    } finally {
      loading.value = false
    }
  }
  
  /**
   * 删除策略
   */
  const deleteStrategy = async (id) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 500))
      
      // 这里应该是实际的API调用
      // const response = await request.delete(`${API_ENDPOINTS.STRATEGY_DELETE}/${id}`)
      
      // 模拟删除成功
      const index = strategyList.value.findIndex(s => s.id === id)
      if (index !== -1) {
        strategyList.value.splice(index, 1)
        pagination.total--
        updateStats()
      }
      
      ElMessage.success('策略删除成功')
      return {
        success: true
      }
    } catch (error) {
      console.error('删除策略失败:', error)
      ElMessage.error('删除策略失败')
      return {
        success: false,
        error: error.message
      }
    } finally {
      loading.value = false
    }
  }
  
  /**
   * 切换策略状态
   */
  const toggleStrategyStatus = async (id) => {
    try {
      const strategy = strategyList.value.find(s => s.id === id)
      if (!strategy) {
        throw new Error('策略不存在')
      }
      
      const newStatus = strategy.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE'
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 300))
      
      // 这里应该是实际的API调用
      // const response = await request.patch(`${API_ENDPOINTS.STRATEGY_TOGGLE}/${id}`, { status: newStatus })
      
      // 模拟更新成功
      strategy.status = newStatus
      strategy.updateTime = new Date().toISOString()
      updateStats()
      
      ElMessage.success(`策略${newStatus === 'ACTIVE' ? '启用' : '停用'}成功`)
      return {
        success: true,
        data: strategy
      }
    } catch (error) {
      console.error('切换策略状态失败:', error)
      ElMessage.error('切换策略状态失败')
      return {
        success: false,
        error: error.message
      }
    }
  }
  
  /**
   * 复制策略
   */
  const cloneStrategy = async (id, newName) => {
    try {
      loading.value = true
      
      const originalStrategy = strategyList.value.find(s => s.id === id)
      if (!originalStrategy) {
        throw new Error('原始策略不存在')
      }
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 800))
      
      // 这里应该是实际的API调用
      // const response = await request.post(API_ENDPOINTS.STRATEGY_CLONE, {
      //   originalId: id,
      //   newName
      // })
      
      // 模拟复制成功
      const clonedStrategy = {
        ...originalStrategy,
        id: Date.now(),
        strategyName: newName,
        status: 'INACTIVE', // 复制的新策略默认停用
        createTime: new Date().toISOString(),
        updateTime: new Date().toISOString()
      }
      
      strategyList.value.unshift(clonedStrategy)
      pagination.total++
      
      ElMessage.success('策略复制成功')
      return {
        success: true,
        data: clonedStrategy
      }
    } catch (error) {
      console.error('复制策略失败:', error)
      ElMessage.error('复制策略失败')
      return {
        success: false,
        error: error.message
      }
    } finally {
      loading.value = false
    }
  }
  
  /**
   * 计算价格
   */
  const calculatePrice = async (params) => {
    try {
      const cacheKey = JSON.stringify(params)
      if (calculationCache.value.has(cacheKey)) {
        return calculationCache.value.get(cacheKey)
      }
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 200))
      
      // 这里应该是实际的API调用
      // const response = await request.post(API_ENDPOINTS.CALCULATE_PRICE, params)
      
      // 模拟计算结果
      const result = performPriceCalculation(params)
      
      // 缓存结果
      calculationCache.value.set(cacheKey, result)
      
      return result
    } catch (error) {
      console.error('价格计算失败:', error)
      ElMessage.error('价格计算失败')
      throw error
    }
  }
  
  /**
   * 批量计算价格
   */
  const batchCalculatePrices = async (batchParams) => {
    try {
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 500))
      
      // 这里应该是实际的API调用
      // const response = await request.post(API_ENDPOINTS.BATCH_CALCULATE, batchParams)
      
      // 模拟批量计算结果
      const results = batchParams.map(params => ({
        ...params,
        result: performPriceCalculation(params)
      }))
      
      return {
        success: true,
        data: results
      }
    } catch (error) {
      console.error('批量价格计算失败:', error)
      ElMessage.error('批量价格计算失败')
      return {
        success: false,
        error: error.message
      }
    }
  }
  
  /**
   * 获取策略分析数据
   */
  const getStrategyAnalytics = async (id) => {
    try {
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 400))
      
      // 这里应该是实际的API调用
      // const response = await request.get(`${API_ENDPOINTS.STRATEGY_ANALYTICS}/${id}`)
      
      // 模拟分析数据
      const analyticsData = generateMockAnalytics()
      
      return {
        success: true,
        data: analyticsData
      }
    } catch (error) {
      console.error('获取策略分析数据失败:', error)
      ElMessage.error('获取策略分析数据失败')
      return {
        success: false,
        error: error.message
      }
    }
  }
  
  /**
   * 导出策略数据
   */
  const exportStrategies = async (filters = {}) => {
    try {
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      // 这里应该是实际的API调用
      // const response = await request.get(API_ENDPOINTS.STRATEGY_EXPORT, {
      //   params: filters,
      //   responseType: 'blob'
      // })
      
      // 模拟导出
      const blob = new Blob(['模拟导出数据'], { type: 'application/vnd.ms-excel' })
      const url = window.URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = `价格策略_${new Date().toISOString().slice(0, 10)}.xlsx`
      a.click()
      window.URL.revokeObjectURL(url)
      
      ElMessage.success('导出成功')
      return {
        success: true
      }
    } catch (error) {
      console.error('导出策略数据失败:', error)
      ElMessage.error('导出策略数据失败')
      return {
        success: false,
        error: error.message
      }
    }
  }
  
  // 辅助方法
  
  /**
   * 更新统计数据
   */
  const updateStats = () => {
    stats.activeStrategies = activeStrategies.value.length
    stats.totalDiscounts = strategyList.value.reduce((sum, s) => {
      return sum + (s.discountAmount || 0)
    }, 0)
    stats.conversionRate = Math.floor(Math.random() * 30) + 10
    stats.roi = Math.floor(Math.random() * 200) + 100
  }
  
  /**
   * 生成模拟策略数据
   */
  const generateMockStrategies = () => {
    return [
      {
        id: 1,
        strategyName: '新用户专享折扣',
        strategyType: 'DISCOUNT',
        channelType: 'B2C',
        userType: 'NEW_USER',
        priority: 1,
        minQuantity: 1,
        maxQuantity: null,
        discountRate: 0.9,
        fixedAmount: null,
        status: 'ACTIVE',
        startTime: '2024-01-01 00:00:00',
        endTime: '2024-12-31 23:59:59',
        createTime: '2024-01-01 10:00:00',
        updateTime: '2024-01-01 10:00:00',
        description: '为新用户提供首次购买9折优惠',
        discountAmount: 5000
      },
      {
        id: 2,
        strategyName: '批发阶梯价格',
        strategyType: 'TIERED',
        channelType: 'B2B',
        userType: 'ENTERPRISE',
        priority: 2,
        minQuantity: 100,
        maxQuantity: 500,
        discountRate: 0.85,
        fixedAmount: null,
        status: 'ACTIVE',
        startTime: '2024-01-01 00:00:00',
        endTime: '2024-12-31 23:59:59',
        createTime: '2024-01-01 10:00:00',
        updateTime: '2024-01-01 10:00:00',
        description: '根据购买数量提供阶梯折扣',
        discountAmount: 8000,
        tieredPrices: [
          { quantity: 100, discountRate: 0.95 },
          { quantity: 200, discountRate: 0.9 },
          { quantity: 500, discountRate: 0.85 }
        ]
      }
    ]
  }
  
  /**
   * 执行价格计算
   */
  const performPriceCalculation = (params) => {
    const { originalPrice, quantity, selectedStrategies } = params
    const originalTotal = originalPrice * quantity
    let totalDiscount = 0
    const appliedStrategies = []
    
    // 模拟计算逻辑
    selectedStrategies?.forEach(strategy => {
      let discount = 0
      switch (strategy.strategyType) {
        case 'DISCOUNT':
          if (!strategy.minQuantity || quantity >= strategy.minQuantity) {
            discount = originalPrice * quantity * (1 - strategy.discountRate)
          }
          break
        case 'FIXED_AMOUNT':
          if (!strategy.minQuantity || quantity >= strategy.minQuantity) {
            discount = strategy.fixedAmount
          }
          break
        case 'TIERED':
          // 阶梯价格逻辑
          const tier = strategy.tieredPrices?.find(t => quantity >= t.quantity)
          if (tier) {
            discount = originalPrice * quantity * (1 - tier.discountRate)
          }
          break
        // 其他策略类型...
      }
      
      if (discount > 0) {
        totalDiscount += discount
        appliedStrategies.push({
          ...strategy,
          discountAmount: discount
        })
      }
    })
    
    const finalPrice = Math.max(0, originalTotal - totalDiscount)
    
    return {
      originalTotal,
      discountAmount: totalDiscount,
      finalPrice,
      discountRate: originalTotal > 0 ? finalPrice / originalTotal : 1,
      appliedStrategies
    }
  }
  
  /**
   * 生成模拟分析数据
   */
  const generateMockAnalytics = () => {
    return {
      applicationCount: Math.floor(Math.random() * 1000) + 500,
      totalDiscount: Math.floor(Math.random() * 50000) + 10000,
      conversionRate: Math.floor(Math.random() * 30) + 10,
      avgOrderValue: Math.floor(Math.random() * 200) + 100,
      roi: Math.floor(Math.random() * 200) + 100,
      satisfactionScore: Math.floor(Math.random() * 2) + 4,
      userCoverage: Math.floor(Math.random() * 500) + 100,
      dailyApplications: Array.from({ length: 30 }, () => Math.floor(Math.random() * 50) + 10)
    }
  }
  
  /**
   * 重置搜索表单
   */
  const resetSearchForm = () => {
    Object.assign(searchForm, {
      strategyName: '',
      strategyType: '',
      channelType: '',
      userType: '',
      status: '',
      dateRange: []
    })
  }
  
  /**
   * 清理缓存
   */
  const clearCache = () => {
    calculationCache.value.clear()
  }
  
  return {
    // 响应式数据
    strategyList,
    currentStrategy,
    loading,
    stats,
    searchForm,
    pagination,
    
    // 计算属性
    filteredStrategies,
    activeStrategies,
    
    // 方法
    fetchStrategyList,
    createStrategy,
    updateStrategy,
    deleteStrategy,
    toggleStrategyStatus,
    cloneStrategy,
    calculatePrice,
    batchCalculatePrices,
    getStrategyAnalytics,
    exportStrategies,
    
    // 辅助方法
    resetSearchForm,
    clearCache,
    
    // 配置常量
    STRATEGY_TYPES,
    CHANNEL_TYPES,
    USER_TYPES,
    STRATEGY_STATUS
  }
}