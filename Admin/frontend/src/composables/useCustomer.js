import { ref, reactive, computed } from 'vue'
import { ElMessage } from 'element-plus'

// 客户类型枚举
export const CUSTOMER_TYPES = {
  INDIVIDUAL: 'individual',      // 个人客户
  ENTERPRISE: 'enterprise',      // 企业客户
  DISTRIBUTOR: 'distributor',      // 经销商
  SUPPLIER: 'supplier',          // 供应商
  PARTNER: 'partner'             // 合作伙伴
}

// 客户状态枚举
export const CUSTOMER_STATUS = {
  ACTIVE: 'active',               // 活跃
  INACTIVE: 'inactive',         // 非活跃
  SUSPENDED: 'suspended',        // 暂停
  PENDING: 'pending',           // 待审核
  BLACKLISTED: 'blacklisted'     // 黑名单
}

// 客户等级枚举
export const CUSTOMER_LEVELS = {
  NORMAL: 'normal',              // 普通客户
  VIP: 'vip',                    // VIP客户
  SVIP: 'svip',                  // SVIP客户
  DIAMOND: 'diamond',            // 钻石客户
  BLACK_GOLD: 'black_gold'       // 黑金客户
}

// 客户来源枚举
export const CUSTOMER_SOURCES = {
  WEBSITE: 'website',            // 官网
  WECHAT: 'wechat',             // 微信
  ALIPAY: 'alipay',             // 支付宝
  TAOBAO: 'taobao',             // 淘宝
  JD: 'jd',                     // 京东
  TIKTOK: 'tiktok',             // 抖音
  KUAISHOU: 'kuaishou',         // 快手
  OFFLINE: 'offline',           // 线下
  REFERRAL: 'referral',         // 推荐
  OTHER: 'other'                // 其他
}

// RFM分层枚举
export const RFM_LEVELS = {
  NEW: 'new',                    // 新客户
  ACTIVE: 'active',             // 活跃客户
  WAKE: 'wake',                 // 唤醒客户
  SLEEP: 'sleep',               // 睡眠客户
  LOST: 'lost'                  // 流失客户
}

// 企业类型枚举
export const ENTERPRISE_TYPES = {
  STATE_OWNED: 'state_owned',    // 国有企业
  PRIVATE: 'private',           // 民营企业
  FOREIGN: 'foreign',           // 外资企业
  JOINT_VENTURE: 'joint_venture', // 合资企业
  LISTED: 'listed',             // 上市公司
  STARTUP: 'startup'            // 初创企业
}

// 行业类型枚举
export const INDUSTRY_TYPES = {
  MANUFACTURING: 'manufacturing',      // 制造业
  TRADING: 'trading',                 // 贸易业
  RETAIL: 'retail',                   // 零售业
  WHOLESALE: 'wholesale',             // 批发业
  LOGISTICS: 'logistics',             // 物流业
  FINANCE: 'finance',                 // 金融业
  REAL_ESTATE: 'real_estate',         // 房地产
  TECHNOLOGY: 'technology',           // 科技业
  HEALTHCARE: 'healthcare',           // 医疗健康
  EDUCATION: 'education',             // 教育培训
  FOOD_BEVERAGE: 'food_beverage',     // 餐饮食品
  TEXTILE: 'textile',                 // 纺织服装
  AUTOMOTIVE: 'automotive',           // 汽车汽配
  CONSTRUCTION: 'construction',       // 建筑建材
  AGRICULTURE: 'agriculture',         // 农业农产
  OTHER: 'other'                      // 其他
}

export function useCustomer() {
  const loading = ref(false)
  const customers = ref([])
  const currentCustomer = ref(null)
  const pagination = reactive({
    page: 1,
    limit: 10,
    total: 0
  })

  // 搜索条件
  const searchForm = reactive({
    keyword: '',
    customerType: '',
    status: '',
    level: '',
    source: '',
    rfmLevel: '',
    enterpriseType: '',
    industry: '',
    dateRange: []
  })

  // 获取客户列表
  const fetchCustomers = async (params = {}) => {
    loading.value = true
    try {
      const queryParams = {
        page: pagination.page,
        limit: pagination.limit,
        ...searchForm,
        ...params
      }
      
      // 模拟API调用
      const response = await mockApi.getCustomers(queryParams)
      customers.value = response.data
      pagination.total = response.total
      
      return response
    } catch (error) {
      ElMessage.error('获取客户列表失败')
      throw error
    } finally {
      loading.value = false
    }
  }

  // 获取客户详情
  const getCustomerDetail = async (id) => {
    loading.value = true
    try {
      const response = await mockApi.getCustomerDetail(id)
      currentCustomer.value = response.data
      return response.data
    } catch (error) {
      ElMessage.error('获取客户详情失败')
      throw error
    } finally {
      loading.value = false
    }
  }

  // 创建客户
  const createCustomer = async (customerData) => {
    loading.value = true
    try {
      const response = await mockApi.createCustomer(customerData)
      ElMessage.success('客户创建成功')
      await fetchCustomers()
      return response.data
    } catch (error) {
      ElMessage.error('创建客户失败')
      throw error
    } finally {
      loading.value = false
    }
  }

  // 更新客户
  const updateCustomer = async (id, customerData) => {
    loading.value = true
    try {
      const response = await mockApi.updateCustomer(id, customerData)
      ElMessage.success('客户更新成功')
      await fetchCustomers()
      return response.data
    } catch (error) {
      ElMessage.error('更新客户失败')
      throw error
    } finally {
      loading.value = false
    }
  }

  // 删除客户
  const deleteCustomer = async (id) => {
    loading.value = true
    try {
      await mockApi.deleteCustomer(id)
      ElMessage.success('客户删除成功')
      await fetchCustomers()
    } catch (error) {
      ElMessage.error('删除客户失败')
      throw error
    } finally {
      loading.value = false
    }
  }

  // 批量删除客户
  const batchDeleteCustomers = async (ids) => {
    loading.value = true
    try {
      await mockApi.batchDeleteCustomers(ids)
      ElMessage.success('批量删除成功')
      await fetchCustomers()
    } catch (error) {
      ElMessage.error('批量删除失败')
      throw error
    } finally {
      loading.value = false
    }
  }

  // 更新客户状态
  const updateCustomerStatus = async (id, status) => {
    loading.value = true
    try {
      await mockApi.updateCustomerStatus(id, status)
      ElMessage.success('状态更新成功')
      await fetchCustomers()
    } catch (error) {
      ElMessage.error('状态更新失败')
      throw error
    } finally {
      loading.value = false
    }
  }

  // 客户标签管理
  const updateCustomerTags = async (id, tags) => {
    loading.value = true
    try {
      await mockApi.updateCustomerTags(id, tags)
      ElMessage.success('标签更新成功')
      await getCustomerDetail(id)
    } catch (error) {
      ElMessage.error('标签更新失败')
      throw error
    } finally {
      loading.value = false
    }
  }

  // 客户等级管理
  const updateCustomerLevel = async (id, level) => {
    loading.value = true
    try {
      await mockApi.updateCustomerLevel(id, level)
      ElMessage.success('等级更新成功')
      await getCustomerDetail(id)
    } catch (error) {
      ElMessage.error('等级更新失败')
      throw error
    } finally {
      loading.value = false
    }
  }

  // 获取客户统计
  const getCustomerStats = async () => {
    try {
      const response = await mockApi.getCustomerStats()
      return response.data
    } catch (error) {
      ElMessage.error('获取客户统计失败')
      throw error
    }
  }

  // 导出客户数据
  const exportCustomers = async (params = {}) => {
    loading.value = true
    try {
      const response = await mockApi.exportCustomers({
        ...searchForm,
        ...params
      })
      ElMessage.success('导出成功')
      return response
    } catch (error) {
      ElMessage.error('导出失败')
      throw error
    } finally {
      loading.value = false
    }
  }

  // 导入客户数据
  const importCustomers = async (file) => {
    loading.value = true
    try {
      const response = await mockApi.importCustomers(file)
      ElMessage.success('导入成功')
      await fetchCustomers()
      return response
    } catch (error) {
      ElMessage.error('导入失败')
      throw error
    } finally {
      loading.value = false
    }
  }

  // 重置搜索条件
  const resetSearch = () => {
    Object.keys(searchForm).forEach(key => {
      if (key === 'dateRange') {
        searchForm[key] = []
      } else {
        searchForm[key] = ''
      }
    })
  }

  // 重置分页
  const resetPagination = () => {
    pagination.page = 1
    pagination.limit = 10
    pagination.total = 0
  }

  return {
    // 状态
    loading,
    customers,
    currentCustomer,
    pagination,
    searchForm,

    // 枚举
    CUSTOMER_TYPES,
    CUSTOMER_STATUS,
    CUSTOMER_LEVELS,
    CUSTOMER_SOURCES,
    RFM_LEVELS,
    ENTERPRISE_TYPES,
    INDUSTRY_TYPES,

    // 方法
    fetchCustomers,
    getCustomerDetail,
    createCustomer,
    updateCustomer,
    deleteCustomer,
    batchDeleteCustomers,
    updateCustomerStatus,
    updateCustomerTags,
    updateCustomerLevel,
    getCustomerStats,
    exportCustomers,
    importCustomers,
    resetSearch,
    resetPagination
  }
}

// 模拟API
const mockApi = {
  async getCustomers(params) {
    await new Promise(resolve => setTimeout(resolve, 500))
    
    const mockData = Array.from({ length: params.limit || 10 }, (_, i) => ({
      id: i + 1 + (params.page - 1) * params.limit,
      customerName: `客户${i + 1}`,
      customerType: Object.values(CUSTOMER_TYPES)[i % 5],
      phone: `138${String(Math.floor(Math.random() * 100000000)).padStart(8, '0')}`,
      email: `customer${i + 1}@example.com`,
      status: Object.values(CUSTOMER_STATUS)[i % 5],
      level: Object.values(CUSTOMER_LEVELS)[i % 5],
      source: Object.values(CUSTOMER_SOURCES)[i % 8],
      rfmLevel: Object.values(RFM_LEVELS)[i % 5],
      totalOrderAmount: Math.floor(Math.random() * 100000),
      orderCount: Math.floor(Math.random() * 100),
      lastOrderDate: new Date(Date.now() - Math.random() * 86400000 * 365).toISOString(),
      createdAt: new Date(Date.now() - Math.random() * 86400000 * 365).toISOString(),
      enterpriseType: i % 2 === 0 ? Object.values(ENTERPRISE_TYPES)[i % 6] : null,
      industry: i % 2 === 0 ? Object.values(INDUSTRY_TYPES)[i % 15] : null,
      companyName: i % 2 === 0 ? `公司${i + 1}` : null,
      creditLimit: i % 2 === 0 ? Math.floor(Math.random() * 1000000) : null,
      creditUsed: i % 2 === 0 ? Math.floor(Math.random() * 500000) : null
    }))

    return {
      code: 0,
      data: mockData,
      total: 120
    }
  },

  async getCustomerDetail(id) {
    await new Promise(resolve => setTimeout(resolve, 300))
    
    const mockCustomer = {
      id,
      customerName: `客户${id}`,
      customerType: CUSTOMER_TYPES.ENTERPRISE,
      phone: `138${String(Math.floor(Math.random() * 100000000)).padStart(8, '0')}`,
      email: `customer${id}@example.com`,
      address: `地址${id}`,
      status: CUSTOMER_STATUS.ACTIVE,
      level: CUSTOMER_LEVELS.VIP,
      source: CUSTOMER_SOURCES.WEBSITE,
      rfmLevel: RFM_LEVELS.ACTIVE,
      wechatId: `wechat${id}`,
      wechatDataEncrypted: 'encrypted_data',
      createdAt: new Date(Date.now() - Math.random() * 86400000 * 365).toISOString(),
      updatedAt: new Date().toISOString(),
      tags: ['重要客户', '长期合作'],
      remark: '这是一个重要客户',
      totalOrderAmount: Math.floor(Math.random() * 100000),
      orderCount: Math.floor(Math.random() * 100),
      lastOrderDate: new Date(Date.now() - Math.random() * 86400000 * 30).toISOString(),
      // B2B企业信息
      companyName: `公司${id}`,
      enterpriseType: ENTERPRISE_TYPES.PRIVATE,
      industry: INDUSTRY_TYPES.MANUFACTURING,
      businessLicense: `BL${String(id).padStart(8, '0')}`,
      taxNumber: `TN${String(id).padStart(10, '0')}`,
      legalRepresentative: `法人${id}`,
      registeredCapital: Math.floor(Math.random() * 10000000),
      establishedDate: new Date(Date.now() - Math.random() * 86400000 * 365 * 10).toISOString(),
      companyAddress: `公司地址${id}`,
      companyPhone: `400${String(Math.floor(Math.random() * 10000000)).padStart(7, '0')}`,
      companyEmail: `company${id}@example.com`,
      companyWebsite: `www.company${id}.com`,
      employeesCount: Math.floor(Math.random() * 1000) + 10,
      annualRevenue: Math.floor(Math.random() * 100000000),
      creditLimit: Math.floor(Math.random() * 1000000),
      creditUsed: Math.floor(Math.random() * 500000),
      paymentMethod: '账期支付',
      paymentTerms: 30,
      contacts: [
        {
          name: `联系人1`,
          position: '采购经理',
          phone: `138${String(Math.floor(Math.random() * 100000000)).padStart(8, '0')}`,
          email: `contact1@company${id}.com`,
          isPrimary: true
        },
        {
          name: `联系人2`,
          position: '财务经理',
          phone: `139${String(Math.floor(Math.random() * 100000000)).padStart(8, '0')}`,
          email: `contact2@company${id}.com`,
          isPrimary: false
        }
      ]
    }

    return {
      code: 0,
      data: mockCustomer
    }
  },

  async createCustomer(data) {
    await new Promise(resolve => setTimeout(resolve, 400))
    return {
      code: 0,
      data: { id: Date.now(), ...data },
      message: '创建成功'
    }
  },

  async updateCustomer(id, data) {
    await new Promise(resolve => setTimeout(resolve, 400))
    return {
      code: 0,
      data: { id, ...data },
      message: '更新成功'
    }
  },

  async deleteCustomer(id) {
    await new Promise(resolve => setTimeout(resolve, 300))
    return {
      code: 0,
      message: '删除成功'
    }
  },

  async batchDeleteCustomers(ids) {
    await new Promise(resolve => setTimeout(resolve, 500))
    return {
      code: 0,
      message: '批量删除成功'
    }
  },

  async updateCustomerStatus(id, status) {
    await new Promise(resolve => setTimeout(resolve, 300))
    return {
      code: 0,
      message: '状态更新成功'
    }
  },

  async updateCustomerTags(id, tags) {
    await new Promise(resolve => setTimeout(resolve, 300))
    return {
      code: 0,
      message: '标签更新成功'
    }
  },

  async updateCustomerLevel(id, level) {
    await new Promise(resolve => setTimeout(resolve, 300))
    return {
      code: 0,
      message: '等级更新成功'
    }
  },

  async getCustomerStats() {
    await new Promise(resolve => setTimeout(resolve, 300))
    return {
      code: 0,
      data: {
        totalCustomers: 1200,
        activeCustomers: 856,
        newCustomersThisMonth: 156,
        enterpriseCustomers: 423,
        totalOrderAmount: 15680000,
        averageOrderAmount: 13040
      }
    }
  },

  async exportCustomers(params) {
    await new Promise(resolve => setTimeout(resolve, 1000))
    return {
      code: 0,
      data: {
        downloadUrl: '/download/customers.xlsx'
      },
      message: '导出成功'
    }
  },

  async importCustomers(file) {
    await new Promise(resolve => setTimeout(resolve, 2000))
    return {
      code: 0,
      data: {
        successCount: 150,
        failCount: 5,
        failReasons: []
      },
      message: '导入成功'
    }
  }
}