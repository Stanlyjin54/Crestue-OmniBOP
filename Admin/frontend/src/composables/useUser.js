import { ref, computed } from 'vue'
import { request } from '@/utils/request'

// 用户状态枚举
export const USER_STATUS = {
  ACTIVE: 'active',
  INACTIVE: 'inactive',
  DISABLED: 'disabled',
  PENDING: 'pending'
}

// 用户状态标签
export const USER_STATUS_LABELS = {
  [USER_STATUS.ACTIVE]: '正常',
  [USER_STATUS.INACTIVE]: '未激活',
  [USER_STATUS.DISABLED]: '已禁用',
  [USER_STATUS.PENDING]: '待审核'
}

// 用户状态样式
export const USER_STATUS_STYLES = {
  [USER_STATUS.ACTIVE]: 'success',
  [USER_STATUS.INACTIVE]: 'warning',
  [USER_STATUS.DISABLED]: 'danger',
  [USER_STATUS.PENDING]: 'info'
}

// 性别枚举
export const GENDER = {
  MALE: 'male',
  FEMALE: 'female',
  OTHER: 'other',
  UNKNOWN: 'unknown'
}

// 性别标签
export const GENDER_LABELS = {
  [GENDER.MALE]: '男',
  [GENDER.FEMALE]: '女',
  [GENDER.OTHER]: '其他',
  [GENDER.UNKNOWN]: '未知'
}

// 部门枚举
export const DEPARTMENT = {
  ADMIN: 'admin',
  SALES: 'sales',
  FINANCE: 'finance',
  WAREHOUSE: 'warehouse',
  TECH: 'tech',
  MARKETING: 'marketing',
  HR: 'hr',
  CUSTOMER_SERVICE: 'customer_service'
}

// 部门标签
export const DEPARTMENT_LABELS = {
  [DEPARTMENT.ADMIN]: '管理员',
  [DEPARTMENT.SALES]: '销售部',
  [DEPARTMENT.FINANCE]: '财务部',
  [DEPARTMENT.WAREHOUSE]: '仓库部',
  [DEPARTMENT.TECH]: '技术部',
  [DEPARTMENT.MARKETING]: '市场部',
  [DEPARTMENT.HR]: '人事部',
  [DEPARTMENT.CUSTOMER_SERVICE]: '客服部'
}

// 职位枚举
export const POSITION = {
  SUPER_ADMIN: 'super_admin',
  ADMIN: 'admin',
  MANAGER: 'manager',
  SUPERVISOR: 'supervisor',
  STAFF: 'staff',
  INTERN: 'intern'
}

// 职位标签
export const POSITION_LABELS = {
  [POSITION.SUPER_ADMIN]: '超级管理员',
  [POSITION.ADMIN]: '管理员',
  [POSITION.MANAGER]: '经理',
  [POSITION.SUPERVISOR]: '主管',
  [POSITION.STAFF]: '员工',
  [POSITION.INTERN]: '实习生'
}

// 用户相关API端点
const API_ENDPOINTS = {
  // 用户管理
  LIST: '/api/admin/users',
  DETAIL: (id) => `/api/admin/users/${id}`,
  CREATE: '/api/admin/users',
  UPDATE: (id) => `/api/admin/users/${id}`,
  DELETE: (id) => `/api/admin/users/${id}`,
  BATCH_DELETE: '/api/admin/users/batch-delete',
  
  // 用户状态
  UPDATE_STATUS: (id) => `/api/admin/users/${id}/status`,
  UPDATE_PASSWORD: (id) => `/api/admin/users/${id}/password`,
  RESET_PASSWORD: (id) => `/api/admin/users/${id}/reset-password`,
  
  // 用户角色
  ROLES: (id) => `/api/admin/users/${id}/roles`,
  ASSIGN_ROLES: (id) => `/api/admin/users/${id}/roles`,
  
  // 用户权限
  PERMISSIONS: (id) => `/api/admin/users/${id}/permissions`,
  
  // 个人信息
  PROFILE: '/api/admin/users/profile',
  UPDATE_PROFILE: '/api/admin/users/profile',
  AVATAR: '/api/admin/users/avatar',
  
  // 导入导出
  EXPORT: '/api/admin/users/export',
  IMPORT: '/api/admin/users/import',
  IMPORT_TEMPLATE: '/api/admin/users/import-template',
  
  // 统计
  STATISTICS: '/api/admin/users/statistics'
}

export function useUser() {
  // 状态
  const users = ref([])
  const currentUser = ref(null)
  const loading = ref(false)
  const error = ref(null)
  
  // 分页
  const pagination = ref({
    page: 1,
    pageSize: 10,
    total: 0,
    pages: 0
  })
  
  // 搜索条件
  const searchForm = ref({
    keyword: '',
    status: '',
    department: '',
    position: '',
    role: ''
  })
  
  // 排序
  const sort = ref({
    field: 'created_at',
    order: 'desc'
  })
  
  // 统计信息
  const statistics = ref({
    total: 0,
    active: 0,
    inactive: 0,
    disabled: 0,
    pending: 0
  })
  
  // 计算属性
  const activeUsers = computed(() => {
    return users.value.filter(user => user.status === USER_STATUS.ACTIVE)
  })
  
  const inactiveUsers = computed(() => {
    return users.value.filter(user => user.status === USER_STATUS.INACTIVE)
  })
  
  const disabledUsers = computed(() => {
    return users.value.filter(user => user.status === USER_STATUS.DISABLED)
  })
  
  const pendingUsers = computed(() => {
    return users.value.filter(user => user.status === USER_STATUS.PENDING)
  })
  
  // 获取用户列表
  const fetchUsers = async (params = {}) => {
    loading.value = true
    error.value = null
    
    try {
      const queryParams = {
        page: pagination.value.page,
        pageSize: pagination.value.pageSize,
        ...searchForm.value,
        ...sort.value,
        ...params
      }
      
      const response = await request.get(API_ENDPOINTS.LIST, { params: queryParams })
      
      if (response.code === 200) {
        users.value = response.data.list || []
        pagination.value = {
          ...pagination.value,
          total: response.data.total || 0,
          pages: response.data.pages || 0
        }
        
        return response.data
      } else {
        throw new Error(response.message || '获取用户列表失败')
      }
    } catch (err) {
      error.value = err.message
      console.error('获取用户列表失败:', err)
      throw err
    } finally {
      loading.value = false
    }
  }
  
  // 获取用户详情
  const fetchUser = async (id) => {
    loading.value = true
    error.value = null
    
    try {
      const response = await request.get(API_ENDPOINTS.DETAIL(id))
      
      if (response.code === 200) {
        currentUser.value = response.data
        return response.data
      } else {
        throw new Error(response.message || '获取用户详情失败')
      }
    } catch (err) {
      error.value = err.message
      console.error('获取用户详情失败:', err)
      throw err
    } finally {
      loading.value = false
    }
  }
  
  // 创建用户
  const createUser = async (userData) => {
    loading.value = true
    error.value = null
    
    try {
      const response = await request.post(API_ENDPOINTS.CREATE, userData)
      
      if (response.code === 200) {
        // 刷新用户列表
        await fetchUsers()
        return response.data
      } else {
        throw new Error(response.message || '创建用户失败')
      }
    } catch (err) {
      error.value = err.message
      console.error('创建用户失败:', err)
      throw err
    } finally {
      loading.value = false
    }
  }
  
  // 更新用户
  const updateUser = async (id, userData) => {
    loading.value = true
    error.value = null
    
    try {
      const response = await request.put(API_ENDPOINTS.UPDATE(id), userData)
      
      if (response.code === 200) {
        // 更新本地数据
        const index = users.value.findIndex(user => user.id === id)
        if (index !== -1) {
          users.value[index] = { ...users.value[index], ...userData }
        }
        
        // 如果更新的是当前用户，也更新currentUser
        if (currentUser.value && currentUser.value.id === id) {
          currentUser.value = { ...currentUser.value, ...userData }
        }
        
        return response.data
      } else {
        throw new Error(response.message || '更新用户失败')
      }
    } catch (err) {
      error.value = err.message
      console.error('更新用户失败:', err)
      throw err
    } finally {
      loading.value = false
    }
  }
  
  // 删除用户
  const deleteUser = async (id) => {
    loading.value = true
    error.value = null
    
    try {
      const response = await request.delete(API_ENDPOINTS.DELETE(id))
      
      if (response.code === 200) {
        // 从本地列表中移除
        const index = users.value.findIndex(user => user.id === id)
        if (index !== -1) {
          users.value.splice(index, 1)
        }
        
        // 更新统计
        await fetchStatistics()
        
        return response.data
      } else {
        throw new Error(response.message || '删除用户失败')
      }
    } catch (err) {
      error.value = err.message
      console.error('删除用户失败:', err)
      throw err
    } finally {
      loading.value = false
    }
  }
  
  // 批量删除用户
  const batchDeleteUsers = async (ids) => {
    loading.value = true
    error.value = null
    
    try {
      const response = await request.post(API_ENDPOINTS.BATCH_DELETE, { ids })
      
      if (response.code === 200) {
        // 从本地列表中移除
        users.value = users.value.filter(user => !ids.includes(user.id))
        
        // 更新统计
        await fetchStatistics()
        
        return response.data
      } else {
        throw new Error(response.message || '批量删除用户失败')
      }
    } catch (err) {
      error.value = err.message
      console.error('批量删除用户失败:', err)
      throw err
    } finally {
      loading.value = false
    }
  }
  
  // 更新用户状态
  const updateUserStatus = async (id, status) => {
    loading.value = true
    error.value = null
    
    try {
      const response = await request.put(API_ENDPOINTS.UPDATE_STATUS(id), { status })
      
      if (response.code === 200) {
        // 更新本地数据
        const index = users.value.findIndex(user => user.id === id)
        if (index !== -1) {
          users.value[index].status = status
        }
        
        // 如果更新的是当前用户，也更新currentUser
        if (currentUser.value && currentUser.value.id === id) {
          currentUser.value.status = status
        }
        
        // 更新统计
        await fetchStatistics()
        
        return response.data
      } else {
        throw new Error(response.message || '更新用户状态失败')
      }
    } catch (err) {
      error.value = err.message
      console.error('更新用户状态失败:', err)
      throw err
    } finally {
      loading.value = false
    }
  }
  
  // 更新用户密码
  const updateUserPassword = async (id, passwordData) => {
    loading.value = true
    error.value = null
    
    try {
      const response = await request.put(API_ENDPOINTS.UPDATE_PASSWORD(id), passwordData)
      
      if (response.code === 200) {
        return response.data
      } else {
        throw new Error(response.message || '更新密码失败')
      }
    } catch (err) {
      error.value = err.message
      console.error('更新密码失败:', err)
      throw err
    } finally {
      loading.value = false
    }
  }
  
  // 重置用户密码
  const resetUserPassword = async (id) => {
    loading.value = true
    error.value = null
    
    try {
      const response = await request.post(API_ENDPOINTS.RESET_PASSWORD(id))
      
      if (response.code === 200) {
        return response.data
      } else {
        throw new Error(response.message || '重置密码失败')
      }
    } catch (err) {
      error.value = err.message
      console.error('重置密码失败:', err)
      throw err
    } finally {
      loading.value = false
    }
  }
  
  // 获取用户角色
  const fetchUserRoles = async (id) => {
    loading.value = true
    error.value = null
    
    try {
      const response = await request.get(API_ENDPOINTS.ROLES(id))
      
      if (response.code === 200) {
        return response.data
      } else {
        throw new Error(response.message || '获取用户角色失败')
      }
    } catch (err) {
      error.value = err.message
      console.error('获取用户角色失败:', err)
      throw err
    } finally {
      loading.value = false
    }
  }
  
  // 分配用户角色
  const assignUserRoles = async (id, roleIds) => {
    loading.value = true
    error.value = null
    
    try {
      const response = await request.put(API_ENDPOINTS.ASSIGN_ROLES(id), { roleIds })
      
      if (response.code === 200) {
        return response.data
      } else {
        throw new Error(response.message || '分配角色失败')
      }
    } catch (err) {
      error.value = err.message
      console.error('分配角色失败:', err)
      throw err
    } finally {
      loading.value = false
    }
  }
  
  // 获取用户权限
  const fetchUserPermissions = async (id) => {
    loading.value = true
    error.value = null
    
    try {
      const response = await request.get(API_ENDPOINTS.PERMISSIONS(id))
      
      if (response.code === 200) {
        return response.data
      } else {
        throw new Error(response.message || '获取用户权限失败')
      }
    } catch (err) {
      error.value = err.message
      console.error('获取用户权限失败:', err)
      throw err
    } finally {
      loading.value = false
    }
  }
  
  // 获取个人信息
  const fetchProfile = async () => {
    loading.value = true
    error.value = null
    
    try {
      const response = await request.get(API_ENDPOINTS.PROFILE)
      
      if (response.code === 200) {
        currentUser.value = response.data
        return response.data
      } else {
        throw new Error(response.message || '获取个人信息失败')
      }
    } catch (err) {
      error.value = err.message
      console.error('获取个人信息失败:', err)
      throw err
    } finally {
      loading.value = false
    }
  }
  
  // 更新个人信息
  const updateProfile = async (profileData) => {
    loading.value = true
    error.value = null
    
    try {
      const response = await request.put(API_ENDPOINTS.UPDATE_PROFILE, profileData)
      
      if (response.code === 200) {
        currentUser.value = { ...currentUser.value, ...profileData }
        return response.data
      } else {
        throw new Error(response.message || '更新个人信息失败')
      }
    } catch (err) {
      error.value = err.message
      console.error('更新个人信息失败:', err)
      throw err
    } finally {
      loading.value = false
    }
  }
  
  // 更新头像
  const updateAvatar = async (file) => {
    loading.value = true
    error.value = null
    
    try {
      const formData = new FormData()
      formData.append('avatar', file)
      
      const response = await request.post(API_ENDPOINTS.AVATAR, formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
      
      if (response.code === 200) {
        if (currentUser.value) {
          currentUser.value.avatar = response.data.avatar
        }
        return response.data
      } else {
        throw new Error(response.message || '更新头像失败')
      }
    } catch (err) {
      error.value = err.message
      console.error('更新头像失败:', err)
      throw err
    } finally {
      loading.value = false
    }
  }
  
  // 导出用户
  const exportUsers = async (params = {}) => {
    loading.value = true
    error.value = null
    
    try {
      const response = await request.get(API_ENDPOINTS.EXPORT, {
        params: { ...searchForm.value, ...params },
        responseType: 'blob'
      })
      
      // 创建下载链接
      const blob = new Blob([response.data], { type: 'application/vnd.ms-excel' })
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.download = `用户列表_${new Date().toLocaleDateString()}.xlsx`
      link.click()
      window.URL.revokeObjectURL(url)
      
      return response.data
    } catch (err) {
      error.value = err.message
      console.error('导出用户失败:', err)
      throw err
    } finally {
      loading.value = false
    }
  }
  
  // 导入用户
  const importUsers = async (file) => {
    loading.value = true
    error.value = null
    
    try {
      const formData = new FormData()
      formData.append('file', file)
      
      const response = await request.post(API_ENDPOINTS.IMPORT, formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
      
      if (response.code === 200) {
        // 刷新用户列表
        await fetchUsers()
        return response.data
      } else {
        throw new Error(response.message || '导入用户失败')
      }
    } catch (err) {
      error.value = err.message
      console.error('导入用户失败:', err)
      throw err
    } finally {
      loading.value = false
    }
  }
  
  // 获取导入模板
  const getImportTemplate = async () => {
    loading.value = true
    error.value = null
    
    try {
      const response = await request.get(API_ENDPOINTS.IMPORT_TEMPLATE, {
        responseType: 'blob'
      })
      
      // 创建下载链接
      const blob = new Blob([response.data], { type: 'application/vnd.ms-excel' })
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.download = '用户导入模板.xlsx'
      link.click()
      window.URL.revokeObjectURL(url)
      
      return response.data
    } catch (err) {
      error.value = err.message
      console.error('获取导入模板失败:', err)
      throw err
    } finally {
      loading.value = false
    }
  }
  
  // 获取用户统计
  const fetchStatistics = async () => {
    loading.value = true
    error.value = null
    
    try {
      const response = await request.get(API_ENDPOINTS.STATISTICS)
      
      if (response.code === 200) {
        statistics.value = response.data
        return response.data
      } else {
        throw new Error(response.message || '获取用户统计失败')
      }
    } catch (err) {
      error.value = err.message
      console.error('获取用户统计失败:', err)
      throw err
    } finally {
      loading.value = false
    }
  }
  
  // 搜索用户
  const searchUsers = async (params = {}) => {
    searchForm.value = { ...searchForm.value, ...params }
    pagination.value.page = 1
    return await fetchUsers()
  }
  
  // 重置搜索
  const resetSearch = () => {
    searchForm.value = {
      keyword: '',
      status: '',
      department: '',
      position: '',
      role: ''
    }
    pagination.value.page = 1
    return fetchUsers()
  }
  
  // 改变分页
  const handlePageChange = (page) => {
    pagination.value.page = page
    return fetchUsers()
  }
  
  // 改变每页数量
  const handleSizeChange = (pageSize) => {
    pagination.value.pageSize = pageSize
    pagination.value.page = 1
    return fetchUsers()
  }
  
  // 排序
  const handleSortChange = ({ field, order }) => {
    sort.value = { field, order }
    return fetchUsers()
  }
  
  // 获取状态标签
  const getStatusLabel = (status) => {
    return USER_STATUS_LABELS[status] || status
  }
  
  // 获取状态样式
  const getStatusStyle = (status) => {
    return USER_STATUS_STYLES[status] || 'info'
  }
  
  // 获取性别标签
  const getGenderLabel = (gender) => {
    return GENDER_LABELS[gender] || gender
  }
  
  // 获取部门标签
  const getDepartmentLabel = (department) => {
    return DEPARTMENT_LABELS[department] || department
  }
  
  // 获取职位标签
  const getPositionLabel = (position) => {
    return POSITION_LABELS[position] || position
  }
  
  return {
    // 状态
    users,
    currentUser,
    loading,
    error,
    pagination,
    searchForm,
    sort,
    statistics,
    
    // 计算属性
    activeUsers,
    inactiveUsers,
    disabledUsers,
    pendingUsers,
    
    // 方法
    fetchUsers,
    fetchUser,
    createUser,
    updateUser,
    deleteUser,
    batchDeleteUsers,
    updateUserStatus,
    updateUserPassword,
    resetUserPassword,
    fetchUserRoles,
    assignUserRoles,
    fetchUserPermissions,
    fetchProfile,
    updateProfile,
    updateAvatar,
    exportUsers,
    importUsers,
    getImportTemplate,
    fetchStatistics,
    searchUsers,
    resetSearch,
    handlePageChange,
    handleSizeChange,
    handleSortChange,
    
    // 工具函数
    getStatusLabel,
    getStatusStyle,
    getGenderLabel,
    getDepartmentLabel,
    getPositionLabel,
    
    // 常量
    USER_STATUS,
    USER_STATUS_LABELS,
    USER_STATUS_STYLES,
    GENDER,
    GENDER_LABELS,
    DEPARTMENT,
    DEPARTMENT_LABELS,
    POSITION,
    POSITION_LABELS
  }
}