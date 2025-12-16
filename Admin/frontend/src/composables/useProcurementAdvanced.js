import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'

/**
 * 采购管理高级功能组合式函数
 * 包含供应商管理、合同管理、采购分析、风险控制等功能
 */
export function useProcurementAdvanced() {
  // 响应式数据
  const loading = ref(false)
  
  // 供应商管理数据
  const suppliers = ref([])
  const selectedSuppliers = ref([])
  
  // 合同管理数据
  const contracts = ref([])
  const contractTemplates = ref([])
  
  // 采购分析数据
  const procurementAnalytics = ref({
    totalSpend: 0,
    costSavings: 0,
    supplierPerformance: [],
    categoryAnalysis: [],
    riskMetrics: {},
    complianceScore: 0
  })
  
  // 筛选和搜索参数
  const filterParams = ref({
    search: '',
    category: '',
    status: '',
    dateRange: [],
    supplier: '',
    riskLevel: '',
    costRange: []
  })
  
  // 分页参数
  const pagination = ref({
    currentPage: 1,
    pageSize: 20,
    total: 0
  })

  // 配置数据
  const supplierCategories = ref([
    { id: 'electronics', name: '电子产品', description: '计算机、通讯设备等' },
    { id: 'office_supplies', name: '办公用品', description: '文具、办公家具等' },
    { id: 'construction', name: '建筑工程', description: '建材、工程服务等' },
    { id: 'services', name: '服务类', description: '咨询、维护等服务' },
    { id: 'manufacturing', name: '制造业', description: '原材料、零部件等' }
  ])

  const riskLevels = ref([
    { id: 'low', name: '低风险', color: '#67C23A' },
    { id: 'medium', name: '中风险', color: '#E6A23C' },
    { id: 'high', name: '高风险', color: '#F56C6C' },
    { id: 'critical', name: '极高风险', color: '#C00' }
  ])

  const contractStatuses = ref([
    { id: 'draft', name: '草稿', color: '#909399' },
    { id: 'pending_review', name: '待审核', color: '#E6A23C' },
    { id: 'active', name: '生效中', color: '#67C23A' },
    { id: 'expired', name: '已过期', color: '#F56C6C' },
    { id: 'terminated', name: '已终止', color: '#C00' }
  ])

  const procurementStatuses = ref([
    { id: 'planning', name: '计划中', color: '#909399' },
    { id: 'rfp', name: '招标中', color: '#409EFF' },
    { id: 'evaluation', name: '评估中', color: '#E6A23C' },
    { id: 'negotiation', name: '谈判中', color: '#9B59B6' },
    { id: 'contracting', name: '签约中', color: '#1ABC9C' },
    { id: 'delivered', name: '已交付', color: '#67C23A' },
    { id: 'completed', name: '已完成', color: '#27AE60' },
    { id: 'cancelled', name: '已取消', color: '#E74C3C' }
  ])

  // 计算属性
  const filteredSuppliers = computed(() => {
    let result = suppliers.value

    if (filterParams.value.search) {
      const search = filterParams.value.search.toLowerCase()
      result = result.filter(supplier => 
        supplier.name.toLowerCase().includes(search) ||
        supplier.code.toLowerCase().includes(search) ||
        supplier.contactPerson.toLowerCase().includes(search)
      )
    }

    if (filterParams.value.category) {
      result = result.filter(supplier => supplier.category === filterParams.value.category)
    }

    if (filterParams.value.riskLevel) {
      result = result.filter(supplier => supplier.riskLevel === filterParams.value.riskLevel)
    }

    if (filterParams.value.status) {
      result = result.filter(supplier => supplier.status === filterParams.value.status)
    }

    return result
  })

  const activeSuppliers = computed(() => {
    return suppliers.value.filter(supplier => supplier.status === 'active')
  })

  const highRiskSuppliers = computed(() => {
    return suppliers.value.filter(supplier => 
      supplier.riskLevel === 'high' || supplier.riskLevel === 'critical'
    )
  })

  const supplierPerformanceScore = computed(() => {
    if (suppliers.value.length === 0) return 0
    const totalScore = suppliers.value.reduce((sum, supplier) => {
      return sum + (supplier.performance?.overallScore || 0)
    }, 0)
    return Math.round(totalScore / suppliers.value.length)
  })

  // 模拟数据生成
  const generateMockSuppliers = () => {
    const mockSuppliers = []
    const names = [
      '华为技术有限公司', '阿里巴巴集团', '腾讯科技', '百度在线', '京东集团',
      '美团', '字节跳动', '小米科技', '联想集团', '海尔集团',
      '格力电器', '中国石化', '中国建筑', '中国移动', '中国联通'
    ]
    
    for (let i = 0; i < 50; i++) {
      mockSuppliers.push({
        id: `supplier_${i + 1}`,
        code: `SUP${String(i + 1).padStart(4, '0')}`,
        name: names[i % names.length] + ` (${i + 1})`,
        category: supplierCategories.value[Math.floor(Math.random() * supplierCategories.value.length)].id,
        status: Math.random() > 0.2 ? 'active' : 'inactive',
        riskLevel: riskLevels.value[Math.floor(Math.random() * riskLevels.value.length)].id,
        contactPerson: `联系人${i + 1}`,
        contactPhone: `138${String(Math.floor(Math.random() * 100000000)).padStart(8, '0')}`,
        contactEmail: `contact${i + 1}@example.com`,
        address: `地址${i + 1}，城市${Math.floor(i / 10) + 1}`,
        annualTurnover: Math.floor(Math.random() * 100000000) + 1000000,
        employeeCount: Math.floor(Math.random() * 10000) + 50,
        establishedYear: 2000 + Math.floor(Math.random() * 24),
        certifications: ['ISO9001', 'ISO14001', 'OHSAS18001'].slice(0, Math.floor(Math.random() * 3) + 1),
        performance: {
          overallScore: Math.floor(Math.random() * 30) + 70,
          quality: Math.floor(Math.random() * 30) + 70,
          delivery: Math.floor(Math.random() * 30) + 70,
          service: Math.floor(Math.random() * 30) + 70,
          cost: Math.floor(Math.random() * 30) + 70
        },
        financialHealth: Math.random() > 0.5 ? 'good' : 'average',
        complianceRecord: Math.random() > 0.3 ? 'compliant' : 'minor_issues',
        lastAuditDate: new Date(2024, Math.floor(Math.random() * 12), Math.floor(Math.random() * 28) + 1).toISOString(),
        contracts: Math.floor(Math.random() * 10) + 1,
        totalValue: Math.floor(Math.random() * 50000000) + 1000000,
        createdAt: new Date(2023, Math.floor(Math.random() * 12), Math.floor(Math.random() * 28) + 1).toISOString(),
        updatedAt: new Date(2024, Math.floor(Math.random() * 12), Math.floor(Math.random() * 28) + 1).toISOString()
      })
    }
    
    return mockSuppliers
  }

  const generateMockContracts = () => {
    const mockContracts = []
    const contractTypes = ['框架协议', '采购合同', '服务合同', '维护合同', '租赁合同']
    
    for (let i = 0; i < 30; i++) {
      mockContracts.push({
        id: `contract_${i + 1}`,
        contractNumber: `CT${String(i + 1).padStart(4, '0')}`,
        title: `${contractTypes[i % contractTypes.length]} - 合同${i + 1}`,
        type: contractTypes[i % contractTypes.length],
        supplierId: `supplier_${Math.floor(Math.random() * 50) + 1}`,
        supplierName: `供应商${Math.floor(Math.random() * 50) + 1}`,
        status: contractStatuses.value[Math.floor(Math.random() * contractStatuses.value.length)].id,
        value: Math.floor(Math.random() * 10000000) + 100000,
        currency: 'CNY',
        startDate: new Date(2024, Math.floor(Math.random() * 12), 1).toISOString(),
        endDate: new Date(2025, Math.floor(Math.random() * 12), 1).toISOString(),
        autoRenewal: Math.random() > 0.5,
        renewalPeriod: Math.random() > 0.5 ? 12 : 24,
        paymentTerms: '30天账期',
        deliveryTerms: 'FOB',
        penaltyClause: Math.random() > 0.5,
        warrantyPeriod: Math.floor(Math.random() * 24) + 6,
        keyTerms: [
          '质量标准符合国家标准',
          '按时交付，超期罚款',
          '提供完善的售后服务',
          '价格锁定12个月'
        ],
        attachments: [
          { name: '合同正文.pdf', size: '2.5MB', url: '#' },
          { name: '技术规格书.pdf', size: '1.8MB', url: '#' }
        ],
        signatory: `签署人${i + 1}`,
        createdBy: `采购员${Math.floor(Math.random() * 10) + 1}`,
        createdAt: new Date(2024, Math.floor(Math.random() * 12), Math.floor(Math.random() * 28) + 1).toISOString(),
        updatedAt: new Date(2024, Math.floor(Math.random() * 12), Math.floor(Math.random() * 28) + 1).toISOString()
      })
    }
    
    return mockContracts
  }

  // 核心方法
  const fetchSuppliers = async (params = {}) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      if (suppliers.value.length === 0) {
        suppliers.value = generateMockSuppliers()
      }
      
      pagination.value.total = filteredSuppliers.value.length
      
    } catch (error) {
      ElMessage.error('获取供应商数据失败')
      console.error('Fetch suppliers error:', error)
    } finally {
      loading.value = false
    }
  }

  const createSupplier = async (supplierData) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 800))
      
      const newSupplier = {
        id: `supplier_${Date.now()}`,
        code: `SUP${String(suppliers.value.length + 1).padStart(4, '0')}`,
        status: 'active',
        performance: {
          overallScore: 80,
          quality: 80,
          delivery: 80,
          service: 80,
          cost: 80
        },
        contracts: 0,
        totalValue: 0,
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
        ...supplierData
      }
      
      suppliers.value.unshift(newSupplier)
      ElMessage.success('供应商创建成功')
      
      return newSupplier
      
    } catch (error) {
      ElMessage.error('创建供应商失败')
      throw error
    } finally {
      loading.value = false
    }
  }

  const updateSupplier = async (id, supplierData) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 600))
      
      const index = suppliers.value.findIndex(s => s.id === id)
      if (index !== -1) {
        suppliers.value[index] = {
          ...suppliers.value[index],
          ...supplierData,
          updatedAt: new Date().toISOString()
        }
      }
      
      ElMessage.success('供应商更新成功')
      
    } catch (error) {
      ElMessage.error('更新供应商失败')
      throw error
    } finally {
      loading.value = false
    }
  }

  const deleteSupplier = async (id) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 500))
      
      const index = suppliers.value.findIndex(s => s.id === id)
      if (index !== -1) {
        suppliers.value.splice(index, 1)
      }
      
      ElMessage.success('供应商删除成功')
      
    } catch (error) {
      ElMessage.error('删除供应商失败')
      throw error
    } finally {
      loading.value = false
    }
  }

  const batchDeleteSuppliers = async (ids) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      suppliers.value = suppliers.value.filter(supplier => !ids.includes(supplier.id))
      
      ElMessage.success(`成功删除 ${ids.length} 个供应商`)
      
    } catch (error) {
      ElMessage.error('批量删除供应商失败')
      throw error
    } finally {
      loading.value = false
    }
  }

  const fetchContracts = async (params = {}) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 800))
      
      if (contracts.value.length === 0) {
        contracts.value = generateMockContracts()
      }
      
    } catch (error) {
      ElMessage.error('获取合同数据失败')
      console.error('Fetch contracts error:', error)
    } finally {
      loading.value = false
    }
  }

  const createContract = async (contractData) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 700))
      
      const newContract = {
        id: `contract_${Date.now()}`,
        contractNumber: `CT${String(contracts.value.length + 1).padStart(4, '0')}`,
        status: 'draft',
        attachments: [],
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
        ...contractData
      }
      
      contracts.value.unshift(newContract)
      ElMessage.success('合同创建成功')
      
      return newContract
      
    } catch (error) {
      ElMessage.error('创建合同失败')
      throw error
    } finally {
      loading.value = false
    }
  }

  const updateContract = async (id, contractData) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 600))
      
      const index = contracts.value.findIndex(c => c.id === id)
      if (index !== -1) {
        contracts.value[index] = {
          ...contracts.value[index],
          ...contractData,
          updatedAt: new Date().toISOString()
        }
      }
      
      ElMessage.success('合同更新成功')
      
    } catch (error) {
      ElMessage.error('更新合同失败')
      throw error
    } finally {
      loading.value = false
    }
  }

  const deleteContract = async (id) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 500))
      
      const index = contracts.value.findIndex(c => c.id === id)
      if (index !== -1) {
        contracts.value.splice(index, 1)
      }
      
      ElMessage.success('合同删除成功')
      
    } catch (error) {
      ElMessage.error('删除合同失败')
      throw error
    } finally {
      loading.value = false
    }
  }

  const fetchProcurementAnalytics = async () => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 1200))
      
      procurementAnalytics.value = {
        totalSpend: 125000000,
        costSavings: 8500000,
        supplierPerformance: [
          { supplier: '华为技术', score: 95, category: 'electronics' },
          { supplier: '阿里巴巴', score: 92, category: 'services' },
          { supplier: '腾讯科技', score: 89, category: 'electronics' },
          { supplier: '联想集团', score: 87, category: 'electronics' },
          { supplier: '海尔集团', score: 85, category: 'manufacturing' }
        ],
        categoryAnalysis: [
          { category: '电子产品', spend: 45000000, percentage: 36 },
          { category: '办公用品', spend: 25000000, percentage: 20 },
          { category: '建筑工程', spend: 20000000, percentage: 16 },
          { category: '服务类', spend: 20000000, percentage: 16 },
          { category: '制造业', spend: 15000000, percentage: 12 }
        ],
        riskMetrics: {
          highRiskSuppliers: 8,
          complianceIssues: 12,
          overdueContracts: 5,
          riskScore: 72
        },
        complianceScore: 88
      }
      
    } catch (error) {
      ElMessage.error('获取采购分析数据失败')
      console.error('Fetch analytics error:', error)
    } finally {
      loading.value = false
    }
  }

  const exportData = async (type, filters = {}) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 2000))
      
      // 这里应该实现实际的导出逻辑
      ElMessage.success(`${type}数据导出成功`)
      
    } catch (error) {
      ElMessage.error('数据导出失败')
      throw error
    } finally {
      loading.value = false
    }
  }

  const generateReport = async (reportType, parameters = {}) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 3000))
      
      ElMessage.success(`${reportType}报告生成成功`)
      
      return {
        reportId: `report_${Date.now()}`,
        downloadUrl: '#',
        generatedAt: new Date().toISOString()
      }
      
    } catch (error) {
      ElMessage.error('报告生成失败')
      throw error
    } finally {
      loading.value = false
    }
  }

  const assessSupplierRisk = async (supplierId) => {
    try {
      // 模拟风险评估
      await new Promise(resolve => setTimeout(resolve, 1500))
      
      const riskFactors = [
        '财务状况评估',
        '历史合规记录',
        '市场稳定性分析',
        '供应链风险评估',
        '质量管理体系评估'
      ]
      
      const assessment = {
        supplierId,
        overallRisk: ['low', 'medium', 'high', 'critical'][Math.floor(Math.random() * 4)],
        riskScore: Math.floor(Math.random() * 40) + 60,
        riskFactors: riskFactors.map(factor => ({
          factor,
          score: Math.floor(Math.random() * 30) + 70,
          status: Math.random() > 0.3 ? 'pass' : 'warning'
        })),
        recommendations: [
          '建议加强供应商监督',
          '定期评估供应商财务状况',
          '建立供应商风险预警机制'
        ],
        assessedAt: new Date().toISOString()
      }
      
      ElMessage.success('供应商风险评估完成')
      return assessment
      
    } catch (error) {
      ElMessage.error('风险评估失败')
      throw error
    }
  }

  const optimizeProcurement = async (optimizationType) => {
    try {
      loading.value = true
      
      // 模拟优化分析
      await new Promise(resolve => setTimeout(resolve, 2000))
      
      const optimizations = {
        cost: {
          potentialSavings: 2500000,
          recommendations: [
            '通过批量采购降低单价',
            '寻找替代供应商降低成本',
            '优化物流配送方案'
          ]
        },
        supplier: {
          supplierCount: 25,
          recommendations: [
            '整合供应商数量，提高议价能力',
            '发展战略供应商合作关系',
            '建立供应商绩效评估体系'
          ]
        },
        process: {
          efficiency: 85,
          recommendations: [
            '优化采购审批流程',
            '实施电子化采购',
            '建立供应商自助服务平台'
          ]
        }
      }
      
      ElMessage.success('采购优化分析完成')
      return optimizations[optimizationType] || optimizations.cost
      
    } catch (error) {
      ElMessage.error('优化分析失败')
      throw error
    } finally {
      loading.value = false
    }
  }

  // 工具方法
  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('zh-CN', {
      style: 'currency',
      currency: 'CNY'
    }).format(amount)
  }

  const formatNumber = (num) => {
    return new Intl.NumberFormat('zh-CN').format(num)
  }

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('zh-CN')
  }

  const getRiskLevelColor = (level) => {
    const levelObj = riskLevels.value.find(l => l.id === level)
    return levelObj ? levelObj.color : '#909399'
  }

  const getStatusColor = (status, type = 'contract') => {
    const statusArray = type === 'contract' ? contractStatuses.value : procurementStatuses.value
    const statusObj = statusArray.find(s => s.id === status)
    return statusObj ? statusObj.color : '#909399'
  }

  const getSupplierCategoryName = (categoryId) => {
    const category = supplierCategories.value.find(c => c.id === categoryId)
    return category ? category.name : categoryId
  }

  // 重置筛选参数
  const resetFilters = () => {
    filterParams.value = {
      search: '',
      category: '',
      status: '',
      dateRange: [],
      supplier: '',
      riskLevel: '',
      costRange: []
    }
  }

  return {
    // 响应式数据
    loading,
    suppliers,
    selectedSuppliers,
    contracts,
    contractTemplates,
    procurementAnalytics,
    filterParams,
    pagination,
    
    // 配置数据
    supplierCategories,
    riskLevels,
    contractStatuses,
    procurementStatuses,
    
    // 计算属性
    filteredSuppliers,
    activeSuppliers,
    highRiskSuppliers,
    supplierPerformanceScore,
    
    // 供应商管理方法
    fetchSuppliers,
    createSupplier,
    updateSupplier,
    deleteSupplier,
    batchDeleteSuppliers,
    
    // 合同管理方法
    fetchContracts,
    createContract,
    updateContract,
    deleteContract,
    
    // 分析和优化方法
    fetchProcurementAnalytics,
    exportData,
    generateReport,
    assessSupplierRisk,
    optimizeProcurement,
    
    // 工具方法
    formatCurrency,
    formatNumber,
    formatDate,
    getRiskLevelColor,
    getStatusColor,
    getSupplierCategoryName,
    resetFilters
  }
}