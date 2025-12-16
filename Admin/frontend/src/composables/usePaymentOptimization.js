import { ref, reactive, computed } from 'vue'
import { ElMessage } from 'element-plus'

/**
 * 支付系统优化组合式函数
 * 提供B2B/B2C支付场景的优化和管理功能
 */
export function usePaymentOptimization() {
  // 响应式数据
  const loading = ref(false)
  const paymentScenarios = ref([])
  const paymentAnalytics = ref({})
  const paymentMethods = ref([])
  const transactionData = ref([])
  const customerSegments = ref([])
  
  // 筛选和搜索参数
  const filterParams = reactive({
    scenarioType: '',
    paymentMethod: '',
    status: '',
    dateRange: [],
    keyword: ''
  })
  
  // 分页参数
  const pagination = reactive({
    currentPage: 1,
    pageSize: 20,
    total: 0
  })
  
  // 支付场景配置
  const scenarioConfig = reactive({
    scenarioTypes: [
      { value: 'b2b', label: 'B2B企业支付', color: '#409EFF' },
      { value: 'b2c', label: 'B2C个人支付', color: '#67C23A' },
      { value: 'b2g', label: 'B2G政府支付', color: '#E6A23C' },
      { value: 'c2c', label: 'C2C个人对个人', color: '#F56C6C' }
    ],
    paymentMethods: [
      { 
        id: 'alipay', 
        name: '支付宝', 
        icon: 'alipay', 
        color: '#1677FF',
        category: 'digital_wallet'
      },
      { 
        id: 'wechat_pay', 
        name: '微信支付', 
        icon: 'wechat', 
        color: '#07C160',
        category: 'digital_wallet'
      },
      { 
        id: 'unionpay', 
        name: '银联支付', 
        icon: 'unionpay', 
        color: '#E60012',
        category: 'bank_card'
      },
      { 
        id: 'bank_transfer', 
        name: '银行转账', 
        icon: 'bank', 
        color: '#1890FF',
        category: 'bank_transfer'
      },
      { 
        id: 'credit_card', 
        name: '信用卡', 
        icon: 'credit_card', 
        color: '#722ED1',
        category: 'bank_card'
      },
      { 
        id: 'digital_currency', 
        name: '数字货币', 
        icon: 'digital_currency', 
        color: '#52C41A',
        category: 'digital_currency'
      }
    ],
    businessTypes: [
      { value: 'ecommerce', label: '电商购物' },
      { value: 'subscription', label: '订阅服务' },
      { value: 'education', label: '在线教育' },
      { value: 'healthcare', label: '医疗服务' },
      { value: 'travel', label: '旅游出行' },
      { value: 'entertainment', label: '娱乐服务' },
      { value: 'real_estate', label: '房地产' },
      { value: 'automotive', label: '汽车服务' }
    ],
    optimizationTargets: [
      { value: 'conversion_rate', label: '转化率优化' },
      { value: 'payment_speed', label: '支付速度优化' },
      { value: 'security', label: '安全性优化' },
      { value: 'cost_reduction', label: '成本降低' },
      { value: 'user_experience', label: '用户体验优化' }
    ]
  })
  
  // 计算属性
  const filteredScenarios = computed(() => {
    let result = paymentScenarios.value
    
    if (filterParams.scenarioType) {
      result = result.filter(scenario => scenario.type === filterParams.scenarioType)
    }
    
    if (filterParams.paymentMethod) {
      result = result.filter(scenario => 
        scenario.supportedMethods.includes(filterParams.paymentMethod)
      )
    }
    
    if (filterParams.status) {
      result = result.filter(scenario => scenario.status === filterParams.status)
    }
    
    if (filterParams.keyword) {
      result = result.filter(scenario =>
        scenario.name.toLowerCase().includes(filterParams.keyword.toLowerCase()) ||
        scenario.description.toLowerCase().includes(filterParams.keyword.toLowerCase())
      )
    }
    
    return result
  })
  
  const activeScenarios = computed(() =>
    paymentScenarios.value.filter(scenario => scenario.status === 'active')
  )
  
  const totalTransactions = computed(() =>
    transactionData.value.reduce((sum, transaction) => sum + transaction.amount, 0)
  )
  
  const averageConversionRate = computed(() => {
    const activeTransactions = transactionData.value.filter(t => t.status === 'completed')
    if (activeTransactions.length === 0) return 0
    return (activeTransactions.length / transactionData.value.length * 100).toFixed(2)
  })
  
  // 模拟数据
  const mockScenarios = [
    {
      id: 'scenario_1',
      name: '企业级B2B采购支付',
      type: 'b2b',
      businessType: 'ecommerce',
      description: '针对企业客户的大额采购支付场景',
      supportedMethods: ['bank_transfer', 'unionpay', 'digital_currency'],
      status: 'active',
      optimizationTarget: 'cost_reduction',
      conversionRate: 85.6,
      averageAmount: 50000,
      transactionVolume: 120,
      createdAt: '2024-03-01T10:00:00Z',
      updatedAt: '2024-03-15T14:30:00Z',
      features: [
        { name: '分期付款', enabled: true, impact: 'high' },
        { name: '信用额度', enabled: true, impact: 'high' },
        { name: '自动对账', enabled: true, impact: 'medium' },
        { name: '风险控制', enabled: true, impact: 'high' }
      ],
      performance: {
        successRate: 98.5,
        averageProcessingTime: '2.3分钟',
        customerSatisfaction: 4.6,
        costReduction: 15.2
      }
    },
    {
      id: 'scenario_2',
      name: '消费者电商购物支付',
      type: 'b2c',
      businessType: 'ecommerce',
      description: '面向个人消费者的电商购物支付',
      supportedMethods: ['alipay', 'wechat_pay', 'credit_card'],
      status: 'active',
      optimizationTarget: 'conversion_rate',
      conversionRate: 92.3,
      averageAmount: 350,
      transactionVolume: 1850,
      createdAt: '2024-03-01T10:00:00Z',
      updatedAt: '2024-03-15T14:30:00Z',
      features: [
        { name: '一键支付', enabled: true, impact: 'high' },
        { name: '分期免息', enabled: true, impact: 'medium' },
        { name: '优惠券集成', enabled: true, impact: 'high' },
        { name: '快捷退款', enabled: true, impact: 'medium' }
      ],
      performance: {
        successRate: 99.2,
        averageProcessingTime: '15秒',
        customerSatisfaction: 4.8,
        costReduction: 8.5
      }
    },
    {
      id: 'scenario_3',
      name: '在线教育订阅支付',
      type: 'b2c',
      businessType: 'education',
      description: '在线教育平台的订阅付费服务',
      supportedMethods: ['alipay', 'wechat_pay', 'credit_card'],
      status: 'optimizing',
      optimizationTarget: 'user_experience',
      conversionRate: 78.9,
      averageAmount: 199,
      transactionVolume: 680,
      createdAt: '2024-03-05T09:00:00Z',
      updatedAt: '2024-03-14T16:20:00Z',
      features: [
        { name: '自动续费', enabled: true, impact: 'high' },
        { name: '试用期免费', enabled: true, impact: 'high' },
        { name: '学习进度同步', enabled: true, impact: 'medium' },
        { name: '退款保障', enabled: true, impact: 'low' }
      ],
      performance: {
        successRate: 97.8,
        averageProcessingTime: '25秒',
        customerSatisfaction: 4.5,
        costReduction: 12.1
      }
    }
  ]
  
  // 方法
  const fetchPaymentScenarios = async (params = {}) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      // 更新分页参数
      if (params.page) pagination.currentPage = params.page
      if (params.pageSize) pagination.pageSize = params.pageSize
      
      // 模拟数据
      paymentScenarios.value = mockScenarios
      pagination.total = mockScenarios.length
      
      return {
        scenarios: mockScenarios,
        total: mockScenarios.length,
        page: pagination.currentPage,
        pageSize: pagination.pageSize
      }
    } catch (error) {
      ElMessage.error('获取支付场景列表失败')
      throw error
    } finally {
      loading.value = false
    }
  }
  
  const createPaymentScenario = async (scenarioData) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 1500))
      
      const newScenario = {
        id: 'scenario_' + Date.now(),
        ...scenarioData,
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
        performance: {
          successRate: 0,
          averageProcessingTime: '0秒',
          customerSatisfaction: 0,
          costReduction: 0
        },
        transactionVolume: 0
      }
      
      paymentScenarios.value.unshift(newScenario)
      ElMessage.success('支付场景创建成功')
      
      return newScenario
    } catch (error) {
      ElMessage.error('创建支付场景失败')
      throw error
    } finally {
      loading.value = false
    }
  }
  
  const updatePaymentScenario = async (id, updates) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      const index = paymentScenarios.value.findIndex(s => s.id === id)
      if (index !== -1) {
        paymentScenarios.value[index] = {
          ...paymentScenarios.value[index],
          ...updates,
          updatedAt: new Date().toISOString()
        }
      }
      
      ElMessage.success('支付场景更新成功')
      return paymentScenarios.value[index]
    } catch (error) {
      ElMessage.error('更新支付场景失败')
      throw error
    } finally {
      loading.value = false
    }
  }
  
  const deletePaymentScenario = async (id) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 800))
      
      const index = paymentScenarios.value.findIndex(s => s.id === id)
      if (index !== -1) {
        paymentScenarios.value.splice(index, 1)
      }
      
      ElMessage.success('支付场景删除成功')
    } catch (error) {
      ElMessage.error('删除支付场景失败')
      throw error
    } finally {
      loading.value = false
    }
  }
  
  const fetchPaymentAnalytics = async (params = {}) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      paymentAnalytics.value = {
        overview: {
          totalScenarios: paymentScenarios.value.length,
          activeScenarios: activeScenarios.value.length,
          totalTransactions: totalTransactions.value,
          averageConversionRate: parseFloat(averageConversionRate.value),
          totalRevenue: totalTransactions.value * 0.8,
          averageProcessingTime: '1.2分钟'
        },
        scenarioPerformance: [
          { scenario: 'B2B企业支付', volume: 120, revenue: 6000000, rate: 85.6 },
          { scenario: 'B2C个人支付', volume: 1850, revenue: 647500, rate: 92.3 },
          { scenario: '在线教育支付', volume: 680, revenue: 135320, rate: 78.9 }
        ],
        methodDistribution: [
          { method: '支付宝', count: 1250, percentage: 45.2, color: '#1677FF' },
          { method: '微信支付', count: 980, percentage: 35.4, color: '#07C160' },
          { method: '银联支付', count: 320, percentage: 11.6, color: '#E60012' },
          { method: '信用卡', count: 200, percentage: 7.2, color: '#722ED1' }
        ],
        conversionTrends: [
          { date: '2024-03-01', b2b: 82.5, b2c: 89.2, overall: 85.8 },
          { date: '2024-03-02', b2b: 84.1, b2c: 90.5, overall: 87.3 },
          { date: '2024-03-03', b2b: 85.6, b2c: 92.3, overall: 88.9 },
          { date: '2024-03-04', b2b: 83.2, b2c: 91.8, overall: 87.5 },
          { date: '2024-03-05', b2b: 86.4, b2c: 93.1, overall: 89.7 }
        ],
        optimizationSuggestions: [
          {
            scenario: 'B2B企业支付',
            issue: '支付成功率偏低',
            suggestion: '建议增加备用支付渠道',
            impact: 'high',
            estimatedImprovement: '+3.2%'
          },
          {
            scenario: '在线教育支付',
            issue: '转化率有待提升',
            suggestion: '优化支付页面设计，增加信任标识',
            impact: 'medium',
            estimatedImprovement: '+5.8%'
          }
        ]
      }
      
      return paymentAnalytics.value
    } catch (error) {
      ElMessage.error('获取支付分析数据失败')
      throw error
    } finally {
      loading.value = false
    }
  }
  
  const optimizePaymentScenario = async (scenarioId, optimizationConfig) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 2000))
      
      const scenario = paymentScenarios.value.find(s => s.id === scenarioId)
      if (scenario) {
        // 模拟优化效果
        scenario.performance = {
          successRate: Math.min(99.9, scenario.performance.successRate + Math.random() * 2),
          averageProcessingTime: Math.max(10, parseFloat(scenario.performance.averageProcessingTime) - Math.random() * 30) + '秒',
          customerSatisfaction: Math.min(5, scenario.performance.customerSatisfaction + Math.random() * 0.3),
          costReduction: scenario.performance.costReduction + Math.random() * 5
        }
        
        scenario.conversionRate = Math.min(99.9, scenario.conversionRate + Math.random() * 3)
        scenario.status = 'optimized'
        scenario.updatedAt = new Date().toISOString()
      }
      
      ElMessage.success('支付场景优化完成')
      return scenario
    } catch (error) {
      ElMessage.error('支付场景优化失败')
      throw error
    } finally {
      loading.value = false
    }
  }
  
  const exportPaymentData = async (params = {}) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 1500))
      
      // 模拟文件下载
      const blob = new Blob(['模拟的支付系统数据'], { type: 'text/csv' })
      const url = URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = `支付系统数据_${new Date().toISOString().split('T')[0]}.csv`
      a.click()
      URL.revokeObjectURL(url)
      
      ElMessage.success('数据导出成功')
    } catch (error) {
      ElMessage.error('数据导出失败')
      throw error
    } finally {
      loading.value = false
    }
  }
  
  // 工具方法
  const getScenarioTypeLabel = (type) => {
    const typeMap = {
      b2b: 'B2B企业支付',
      b2c: 'B2C个人支付',
      b2g: 'B2G政府支付',
      c2c: 'C2C个人对个人'
    }
    return typeMap[type] || type
  }
  
  const getScenarioTypeColor = (type) => {
    const typeMap = {
      b2b: '#409EFF',
      b2c: '#67C23A',
      b2g: '#E6A23C',
      c2c: '#F56C6C'
    }
    return typeMap[type] || '#909399'
  }
  
  const getStatusLabel = (status) => {
    const statusMap = {
      draft: '草稿',
      active: '活跃',
      optimizing: '优化中',
      optimized: '已优化',
      suspended: '已暂停'
    }
    return statusMap[status] || status
  }
  
  const getStatusType = (status) => {
    const statusMap = {
      draft: 'info',
      active: 'success',
      optimizing: 'warning',
      optimized: 'success',
      suspended: 'danger'
    }
    return statusMap[status] || 'info'
  }
  
  const formatAmount = (amount) => {
    return new Intl.NumberFormat('zh-CN', {
      style: 'currency',
      currency: 'CNY',
      minimumFractionDigits: 0
    }).format(amount)
  }
  
  const formatNumber = (num) => {
    if (num >= 10000) {
      return (num / 10000).toFixed(1) + '万'
    }
    return num.toLocaleString()
  }
  
  return {
    // 响应式数据
    loading: readonly(loading),
    paymentScenarios: readonly(paymentScenarios),
    paymentAnalytics: readonly(paymentAnalytics),
    paymentMethods: readonly(paymentMethods),
    transactionData: readonly(transactionData),
    customerSegments: readonly(customerSegments),
    filterParams,
    pagination,
    scenarioConfig,
    
    // 计算属性
    filteredScenarios,
    activeScenarios,
    totalTransactions,
    averageConversionRate,
    
    // 方法
    fetchPaymentScenarios,
    createPaymentScenario,
    updatePaymentScenario,
    deletePaymentScenario,
    fetchPaymentAnalytics,
    optimizePaymentScenario,
    exportPaymentData,
    
    // 工具方法
    getScenarioTypeLabel,
    getScenarioTypeColor,
    getStatusLabel,
    getStatusType,
    formatAmount,
    formatNumber
  }
}