import { ref, reactive, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { useUser } from './useUser'

// 多租户相关的API端点
const API_ENDPOINTS = {
  // 租户管理
  TENANT_LIST: '/api/tenant/list',
  TENANT_CREATE: '/api/tenant/create',
  TENANT_UPDATE: '/api/tenant/update',
  TENANT_DELETE: '/api/tenant/delete',
  TENANT_DETAIL: '/api/tenant/detail',
  TENANT_SWITCH: '/api/tenant/switch',
  
  // 租户用户管理
  TENANT_USER_LIST: '/api/tenant/users',
  TENANT_USER_ADD: '/api/tenant/users/add',
  TENANT_USER_REMOVE: '/api/tenant/users/remove',
  TENANT_USER_UPDATE: '/api/tenant/users/update',
  
  // 租户资源管理
  TENANT_RESOURCES: '/api/tenant/resources',
  TENANT_QUOTA: '/api/tenant/quota',
  TENANT_USAGE: '/api/tenant/usage',
  
  // 租户数据
  TENANT_DATA_LIST: '/api/tenant/data',
  TENANT_DATA_EXPORT: '/api/tenant/data/export',
  TENANT_DATA_CLEAR: '/api/tenant/data/clear',
  
  // 租户权限
  TENANT_PERMISSIONS: '/api/tenant/permissions',
  TENANT_ROLES: '/api/tenant/roles'
}

// 租户类型配置
export const TENANT_TYPES = {
  ENTERPRISE: {
    value: 'ENTERPRISE',
    label: '企业租户',
    description: '大型企业客户，完整功能访问',
    color: '#409EFF',
    features: ['full_access', 'custom_branding', 'api_access', 'priority_support']
  },
  PROFESSIONAL: {
    value: 'PROFESSIONAL',
    label: '专业版租户',
    description: '中小企业客户，标准功能',
    color: '#67C23A',
    features: ['standard_access', 'basic_api', 'email_support']
  },
  BASIC: {
    value: 'BASIC',
    label: '基础版租户',
    description: '小型客户，基础功能',
    color: '#E6A23C',
    features: ['basic_access', 'limited_api']
  },
  TRIAL: {
    value: 'TRIAL',
    label: '试用租户',
    description: '新用户试用版本',
    color: '#909399',
    features: ['trial_access', 'limited_features']
  }
}

// 租户状态配置
export const TENANT_STATUS = {
  ACTIVE: {
    value: 'ACTIVE',
    label: '活跃',
    tagType: 'success',
    description: '租户服务正常运行',
    icon: 'Check'
  },
  SUSPENDED: {
    value: 'SUSPENDED',
    label: '已暂停',
    tagType: 'warning',
    description: '租户服务已暂停',
    icon: 'Warning'
  },
  EXPIRED: {
    value: 'EXPIRED',
    label: '已过期',
    tagType: 'danger',
    description: '租户服务已过期',
    icon: 'Clock'
  },
  CANCELLED: {
    value: 'CANCELLED',
    label: '已取消',
    tagType: 'info',
    description: '租户服务已取消',
    icon: 'Close'
  }
}

// 隔离级别配置
export const ISOLATION_LEVELS = {
  DATABASE: {
    value: 'DATABASE',
    label: '数据库隔离',
    description: '每个租户独立数据库',
    security: 'high',
    performance: 'medium',
    complexity: 'medium'
  },
  SCHEMA: {
    value: 'SCHEMA',
    label: 'Schema隔离',
    description: '每个租户独立Schema',
    security: 'high',
    performance: 'high',
    complexity: 'medium'
  },
  TABLE: {
    value: 'TABLE',
    label: '表级隔离',
    description: '租户ID字段隔离',
    security: 'medium',
    performance: 'high',
    complexity: 'low'
  },
  COLUMN: {
    value: 'COLUMN',
    label: '列级隔离',
    description: '租户字段隔离',
    security: 'low',
    performance: 'high',
    complexity: 'low'
  }
}

/**
 * 多租户管理组合式函数
 */
export function useTenant() {
  const { userInfo } = useUser()
  
  // 响应式数据
  const tenantList = ref([])
  const currentTenant = ref(null)
  const tenantUsers = ref([])
  const tenantResources = ref({})
  const tenantUsage = ref({})
  const loading = ref(false)
  
  // 统计数据
  const stats = reactive({
    totalTenants: 0,
    activeTenants: 0,
    suspendedTenants: 0,
    totalUsers: 0,
    avgUsersPerTenant: 0,
    totalResources: 0,
    resourceUtilization: 0
  })
  
  // 搜索和筛选
  const searchForm = reactive({
    tenantName: '',
    tenantType: '',
    status: '',
    contactEmail: '',
    dateRange: [],
    expired: false
  })
  
  // 分页
  const pagination = reactive({
    currentPage: 1,
    pageSize: 20,
    total: 0
  })
  
  // 创建租户表单
  const createForm = reactive({
    tenantName: '',
    tenantType: 'BASIC',
    contactEmail: '',
    contactPhone: '',
    companyName: '',
    industry: '',
    address: '',
    description: '',
    quota: {
      maxUsers: 100,
      maxStorage: 10, // GB
      maxApiCalls: 10000,
      maxProjects: 10
    },
    features: [],
    billingCycle: 'monthly',
    autoRenew: true
  })
  
  // 计算属性
  const filteredTenants = computed(() => {
    let filtered = [...tenantList.value]
    
    // 按租户名称筛选
    if (searchForm.tenantName) {
      filtered = filtered.filter(t => 
        t.tenantName.includes(searchForm.tenantName)
      )
    }
    
    // 按租户类型筛选
    if (searchForm.tenantType) {
      filtered = filtered.filter(t => t.tenantType === searchForm.tenantType)
    }
    
    // 按状态筛选
    if (searchForm.status) {
      filtered = filtered.filter(t => t.status === searchForm.status)
    }
    
    // 按联系人邮箱筛选
    if (searchForm.contactEmail) {
      filtered = filtered.filter(t => 
        t.contactEmail.includes(searchForm.contactEmail)
      )
    }
    
    // 筛选过期租户
    if (searchForm.expired) {
      const now = new Date()
      filtered = filtered.filter(t => 
        new Date(t.expireTime) < now
      )
    }
    
    return filtered
  })
  
  const activeTenants = computed(() => 
    tenantList.value.filter(t => t.status === 'ACTIVE')
  )
  
  const suspendedTenants = computed(() => 
    tenantList.value.filter(t => t.status === 'SUSPENDED')
  )
  
  // 方法
  
  /**
   * 获取租户列表
   */
  const fetchTenantList = async (params = {}) => {
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
      // const response = await request.get(API_ENDPOINTS.TENANT_LIST, { params: requestParams })
      
      // 模拟数据
      const mockData = generateMockTenants()
      tenantList.value = mockData
      pagination.total = mockData.length
      
      updateStats()
      
      return {
        success: true,
        data: mockData,
        total: pagination.total
      }
    } catch (error) {
      console.error('获取租户列表失败:', error)
      ElMessage.error('获取租户列表失败')
      return {
        success: false,
        error: error.message
      }
    } finally {
      loading.value = false
    }
  }
  
  /**
   * 获取租户详情
   */
  const fetchTenantDetail = async (tenantId) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 300))
      
      // 这里应该是实际的API调用
      // const response = await request.get(`${API_ENDPOINTS.TENANT_DETAIL}/${tenantId}`)
      
      // 模拟租户详情
      const tenantDetail = tenantList.value.find(t => t.id === tenantId)
      if (!tenantDetail) {
        throw new Error('租户不存在')
      }
      
      currentTenant.value = tenantDetail
      
      return {
        success: true,
        data: tenantDetail
      }
    } catch (error) {
      console.error('获取租户详情失败:', error)
      ElMessage.error('获取租户详情失败')
      return {
        success: false,
        error: error.message
      }
    } finally {
      loading.value = false
    }
  }
  
  /**
   * 创建租户
   */
  const createTenant = async (tenantData) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      // 这里应该是实际的API调用
      // const response = await request.post(API_ENDPOINTS.TENANT_CREATE, tenantData)
      
      // 模拟创建成功
      const newTenant = {
        id: Date.now(),
        ...tenantData,
        status: 'ACTIVE',
        createTime: new Date().toISOString(),
        updateTime: new Date().toISOString(),
        expireTime: new Date(Date.now() + 365 * 24 * 60 * 60 * 1000).toISOString(), // 默认一年后过期
        lastLoginTime: null,
        totalUsers: 0,
        totalProjects: 0,
        storageUsed: 0
      }
      
      tenantList.value.unshift(newTenant)
      pagination.total++
      updateStats()
      
      ElMessage.success('租户创建成功')
      return {
        success: true,
        data: newTenant
      }
    } catch (error) {
      console.error('创建租户失败:', error)
      ElMessage.error('创建租户失败')
      return {
        success: false,
        error: error.message
      }
    } finally {
      loading.value = false
    }
  }
  
  /**
   * 更新租户
   */
  const updateTenant = async (tenantId, tenantData) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 800))
      
      // 这里应该是实际的API调用
      // const response = await request.put(`${API_ENDPOINTS.TENANT_UPDATE}/${tenantId}`, tenantData)
      
      // 模拟更新成功
      const index = tenantList.value.findIndex(t => t.id === tenantId)
      if (index !== -1) {
        tenantList.value[index] = {
          ...tenantList.value[index],
          ...tenantData,
          updateTime: new Date().toISOString()
        }
        updateStats()
      }
      
      ElMessage.success('租户更新成功')
      return {
        success: true,
        data: tenantList.value[index]
      }
    } catch (error) {
      console.error('更新租户失败:', error)
      ElMessage.error('更新租户失败')
      return {
        success: false,
        error: error.message
      }
    } finally {
      loading.value = false
    }
  }
  
  /**
   * 删除租户
   */
  const deleteTenant = async (tenantId) => {
    try {
      loading.value = true
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 500))
      
      // 这里应该是实际的API调用
      // const response = await request.delete(`${API_ENDPOINTS.TENANT_DELETE}/${tenantId}`)
      
      // 模拟删除成功
      const index = tenantList.value.findIndex(t => t.id === tenantId)
      if (index !== -1) {
        tenantList.value.splice(index, 1)
        pagination.total--
        updateStats()
      }
      
      ElMessage.success('租户删除成功')
      return {
        success: true
      }
    } catch (error) {
      console.error('删除租户失败:', error)
      ElMessage.error('删除租户失败')
      return {
        success: false,
        error: error.message
      }
    } finally {
      loading.value = false
    }
  }
  
  /**
   * 切换租户状态
   */
  const toggleTenantStatus = async (tenantId) => {
    try {
      const tenant = tenantList.value.find(t => t.id === tenantId)
      if (!tenant) {
        throw new Error('租户不存在')
      }
      
      const newStatus = tenant.status === 'ACTIVE' ? 'SUSPENDED' : 'ACTIVE'
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 300))
      
      // 这里应该是实际的API调用
      // const response = await request.patch(`${API_ENDPOINTS.TENANT_SWITCH}/${tenantId}`, { status: newStatus })
      
      // 模拟更新成功
      tenant.status = newStatus
      tenant.updateTime = new Date().toISOString()
      updateStats()
      
      ElMessage.success(`租户${newStatus === 'ACTIVE' ? '启用' : '暂停'}成功`)
      return {
        success: true,
        data: tenant
      }
    } catch (error) {
      console.error('切换租户状态失败:', error)
      ElMessage.error('切换租户状态失败')
      return {
        success: false,
        error: error.message
      }
    }
  }
  
  /**
   * 获取租户用户列表
   */
  const fetchTenantUsers = async (tenantId) => {
    try {
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 400))
      
      // 这里应该是实际的API调用
      // const response = await request.get(`${API_ENDPOINTS.TENANT_USER_LIST}/${tenantId}`)
      
      // 模拟用户数据
      const mockUsers = generateMockUsers()
      tenantUsers.value = mockUsers
      
      return {
        success: true,
        data: mockUsers
      }
    } catch (error) {
      console.error('获取租户用户列表失败:', error)
      ElMessage.error('获取租户用户列表失败')
      return {
        success: false,
        error: error.message
      }
    }
  }
  
  /**
   * 获取租户资源使用情况
   */
  const fetchTenantResources = async (tenantId) => {
    try {
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 300))
      
      // 这里应该是实际的API调用
      // const response = await request.get(`${API_ENDPOINTS.TENANT_RESOURCES}/${tenantId}`)
      
      // 模拟资源数据
      const mockResources = {
        users: { used: 45, limit: 100 },
        storage: { used: 7.5, limit: 10 }, // GB
        apiCalls: { used: 8500, limit: 10000 },
        projects: { used: 6, limit: 10 }
      }
      
      tenantResources.value = mockResources
      tenantUsage.value = calculateUsagePercentage(mockResources)
      
      return {
        success: true,
        data: mockResources
      }
    } catch (error) {
      console.error('获取租户资源信息失败:', error)
      ElMessage.error('获取租户资源信息失败')
      return {
        success: false,
        error: error.message
      }
    }
  }
  
  /**
   * 设置当前租户上下文
   */
  const setCurrentTenant = (tenantId) => {
    const tenant = tenantList.value.find(t => t.id === tenantId)
    if (tenant) {
      currentTenant.value = tenant
      // 这里应该设置全局的租户上下文
      localStorage.setItem('currentTenantId', tenantId.toString())
      ElMessage.success(`已切换到租户：${tenant.tenantName}`)
    }
  }
  
  /**
   * 获取当前租户ID
   */
  const getCurrentTenantId = () => {
    return currentTenant.value?.id || parseInt(localStorage.getItem('currentTenantId') || '0')
  }
  
  /**
   * 检查租户权限
   */
  const checkTenantPermission = (permission) => {
    const currentTenantInfo = currentTenant.value
    if (!currentTenantInfo) return false
    
    const tenantType = TENANT_TYPES[currentTenantInfo.tenantType]
    if (!tenantType) return false
    
    return tenantType.features.includes(permission)
  }
  
  /**
   * 检查租户资源配额
   */
  const checkTenantQuota = (resourceType, amount) => {
    const resources = tenantResources.value[resourceType]
    if (!resources) return false
    
    return (resources.used + amount) <= resources.limit
  }
  
  // 辅助方法
  
  /**
   * 更新统计数据
   */
  const updateStats = () => {
    stats.totalTenants = tenantList.value.length
    stats.activeTenants = activeTenants.value.length
    stats.suspendedTenants = suspendedTenants.value.length
    stats.totalUsers = tenantList.value.reduce((sum, t) => sum + (t.totalUsers || 0), 0)
    stats.avgUsersPerTenant = stats.totalTenants > 0 ? Math.round(stats.totalUsers / stats.totalTenants) : 0
    stats.totalResources = tenantList.value.length
    stats.resourceUtilization = Math.round(Math.random() * 40) + 60 // 60-100%
  }
  
  /**
   * 计算资源使用百分比
   */
  const calculateUsagePercentage = (resources) => {
    const usage = {}
    Object.keys(resources).forEach(key => {
      const resource = resources[key]
      usage[key] = {
        ...resource,
        percentage: Math.round((resource.used / resource.limit) * 100)
      }
    })
    return usage
  }
  
  /**
   * 生成模拟租户数据
   */
  const generateMockTenants = () => {
    return [
      {
        id: 1,
        tenantName: '腾讯科技有限公司',
        tenantType: 'ENTERPRISE',
        status: 'ACTIVE',
        contactEmail: 'admin@tencent.com',
        contactPhone: '13800138000',
        companyName: '腾讯科技有限公司',
        industry: '互联网',
        address: '广东省深圳市南山区科技园',
        description: '大型互联网公司客户',
        maxUsers: 1000,
        maxStorage: 1000,
        maxApiCalls: 1000000,
        maxProjects: 100,
        totalUsers: 856,
        totalProjects: 78,
        storageUsed: 750.5,
        createTime: '2024-01-01 10:00:00',
        updateTime: '2024-01-15 14:30:00',
        expireTime: '2025-01-01 23:59:59',
        lastLoginTime: '2024-01-20 09:15:00'
      },
      {
        id: 2,
        tenantName: '阿里巴巴集团',
        tenantType: 'PROFESSIONAL',
        status: 'ACTIVE',
        contactEmail: 'contact@alibaba.com',
        contactPhone: '13900139000',
        companyName: '阿里巴巴集团',
        industry: '电商',
        address: '浙江省杭州市西湖区文三路969号',
        description: '电商行业客户',
        maxUsers: 500,
        maxStorage: 500,
        maxApiCalls: 500000,
        maxProjects: 50,
        totalUsers: 423,
        totalProjects: 35,
        storageUsed: 320.8,
        createTime: '2024-01-05 10:00:00',
        updateTime: '2024-01-18 16:20:00',
        expireTime: '2024-12-31 23:59:59',
        lastLoginTime: '2024-01-19 14:45:00'
      }
    ]
  }
  
  /**
   * 生成模拟用户数据
   */
  const generateMockUsers = () => {
    return [
      {
        id: 1,
        userName: '张三',
        email: 'zhangsan@company.com',
        role: 'tenant_admin',
        status: 'ACTIVE',
        lastLoginTime: '2024-01-20 09:00:00',
        createTime: '2024-01-01 10:00:00'
      },
      {
        id: 2,
        userName: '李四',
        email: 'lisi@company.com',
        role: 'tenant_user',
        status: 'ACTIVE',
        lastLoginTime: '2024-01-19 16:30:00',
        createTime: '2024-01-05 14:20:00'
      }
    ]
  }
  
  /**
   * 重置搜索表单
   */
  const resetSearchForm = () => {
    Object.assign(searchForm, {
      tenantName: '',
      tenantType: '',
      status: '',
      contactEmail: '',
      dateRange: [],
      expired: false
    })
  }
  
  /**
   * 重置创建表单
   */
  const resetCreateForm = () => {
    Object.assign(createForm, {
      tenantName: '',
      tenantType: 'BASIC',
      contactEmail: '',
      contactPhone: '',
      companyName: '',
      industry: '',
      address: '',
      description: '',
      quota: {
        maxUsers: 100,
        maxStorage: 10,
        maxApiCalls: 10000,
        maxProjects: 10
      },
      features: [],
      billingCycle: 'monthly',
      autoRenew: true
    })
  }
  
  return {
    // 响应式数据
    tenantList,
    currentTenant,
    tenantUsers,
    tenantResources,
    tenantUsage,
    loading,
    stats,
    searchForm,
    pagination,
    createForm,
    
    // 计算属性
    filteredTenants,
    activeTenants,
    suspendedTenants,
    
    // 方法
    fetchTenantList,
    fetchTenantDetail,
    createTenant,
    updateTenant,
    deleteTenant,
    toggleTenantStatus,
    fetchTenantUsers,
    fetchTenantResources,
    setCurrentTenant,
    getCurrentTenantId,
    checkTenantPermission,
    checkTenantQuota,
    
    // 辅助方法
    resetSearchForm,
    resetCreateForm,
    
    // 配置常量
    TENANT_TYPES,
    TENANT_STATUS,
    ISOLATION_LEVELS
  }
}