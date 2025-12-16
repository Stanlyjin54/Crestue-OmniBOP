/**
 * 权限管理组合式函数
 * 基于B2B权限系统扩展，提供角色权限管理功能
 */

import { ref, computed, reactive } from 'vue'
import { ElMessage } from 'element-plus'

// 权限常量定义
export const PERMISSIONS = {
  // 用户管理权限
  USER_VIEW: 'user:view',
  USER_CREATE: 'user:create',
  USER_UPDATE: 'user:update',
  USER_DELETE: 'user:delete',
  
  // 角色管理权限
  ROLE_VIEW: 'role:view',
  ROLE_CREATE: 'role:create',
  ROLE_UPDATE: 'role:update',
  ROLE_DELETE: 'role:delete',
  
  // 权限管理
  PERMISSION_VIEW: 'permission:view',
  PERMISSION_CREATE: 'permission:create',
  PERMISSION_UPDATE: 'permission:update',
  PERMISSION_DELETE: 'permission:delete',
  
  // 商品管理
  PRODUCT_VIEW: 'product:view',
  PRODUCT_CREATE: 'product:create',
  PRODUCT_UPDATE: 'product:update',
  PRODUCT_DELETE: 'product:delete',
  
  // 订单管理
  ORDER_VIEW: 'order:view',
  ORDER_CREATE: 'order:create',
  ORDER_UPDATE: 'order:update',
  ORDER_DELETE: 'order:delete',
  
  // 客户管理
  CUSTOMER_VIEW: 'customer:view',
  CUSTOMER_CREATE: 'customer:create',
  CUSTOMER_UPDATE: 'customer:update',
  CUSTOMER_DELETE: 'customer:delete',
  
  // 系统管理
  SYSTEM_CONFIG: 'system:config',
  SYSTEM_LOG: 'system:log',
  SYSTEM_MONITOR: 'system:monitor'
}

// 角色常量定义
export const ROLES = {
  SUPER_ADMIN: 'super_admin',
  ADMIN: 'admin',
  MANAGER: 'manager',
  SALES: 'sales',
  CUSTOMER_SERVICE: 'customer_service',
  WAREHOUSE: 'warehouse',
  FINANCE: 'finance'
}

// 角色配置
export const ROLE_CONFIG = {
  [ROLES.SUPER_ADMIN]: {
    name: '超级管理员',
    description: '拥有系统所有权限',
    level: 1,
    permissions: Object.values(PERMISSIONS)
  },
  [ROLES.ADMIN]: {
    name: '管理员',
    description: '系统管理员，除系统配置外所有权限',
    level: 2,
    permissions: [
      PERMISSIONS.USER_VIEW, PERMISSIONS.USER_CREATE, PERMISSIONS.USER_UPDATE,
      PERMISSIONS.ROLE_VIEW, PERMISSIONS.ROLE_CREATE, PERMISSIONS.ROLE_UPDATE,
      PERMISSIONS.PERMISSION_VIEW,
      PERMISSIONS.PRODUCT_VIEW, PERMISSIONS.PRODUCT_CREATE, PERMISSIONS.PRODUCT_UPDATE,
      PERMISSIONS.ORDER_VIEW, PERMISSIONS.ORDER_CREATE, PERMISSIONS.ORDER_UPDATE,
      PERMISSIONS.CUSTOMER_VIEW, PERMISSIONS.CUSTOMER_CREATE, PERMISSIONS.CUSTOMER_UPDATE
    ]
  },
  [ROLES.MANAGER]: {
    name: '业务经理',
    description: '业务部门经理，管理销售团队',
    level: 3,
    permissions: [
      PERMISSIONS.USER_VIEW,
      PERMISSIONS.PRODUCT_VIEW,
      PERMISSIONS.ORDER_VIEW, PERMISSIONS.ORDER_CREATE, PERMISSIONS.ORDER_UPDATE,
      PERMISSIONS.CUSTOMER_VIEW, PERMISSIONS.CUSTOMER_CREATE, PERMISSIONS.CUSTOMER_UPDATE
    ]
  },
  [ROLES.SALES]: {
    name: '销售人员',
    description: '销售业务人员',
    level: 4,
    permissions: [
      PERMISSIONS.PRODUCT_VIEW,
      PERMISSIONS.ORDER_VIEW, PERMISSIONS.ORDER_CREATE,
      PERMISSIONS.CUSTOMER_VIEW, PERMISSIONS.CUSTOMER_CREATE
    ]
  },
  [ROLES.CUSTOMER_SERVICE]: {
    name: '客服人员',
    description: '客户服务支持',
    level: 4,
    permissions: [
      PERMISSIONS.ORDER_VIEW,
      PERMISSIONS.CUSTOMER_VIEW, PERMISSIONS.CUSTOMER_UPDATE
    ]
  },
  [ROLES.WAREHOUSE]: {
    name: '仓库管理员',
    description: '库存和仓库管理',
    level: 4,
    permissions: [
      PERMISSIONS.PRODUCT_VIEW,
      PERMISSIONS.ORDER_VIEW, PERMISSIONS.ORDER_UPDATE
    ]
  },
  [ROLES.FINANCE]: {
    name: '财务人员',
    description: '财务和账务管理',
    level: 4,
    permissions: [
      PERMISSIONS.ORDER_VIEW,
      PERMISSIONS.CUSTOMER_VIEW
    ]
  }
}

// 权限管理组合式函数
export function useAuth() {
  // 当前用户信息
  const currentUser = ref({
    id: 1,
    username: 'admin',
    role: ROLES.SUPER_ADMIN,
    permissions: ROLE_CONFIG[ROLES.SUPER_ADMIN].permissions
  })

  // 权限检查函数
  const hasPermission = (permission) => {
    if (!currentUser.value) return false
    return currentUser.value.permissions.includes(permission)
  }

  // 角色检查函数
  const hasRole = (role) => {
    if (!currentUser.value) return false
    return currentUser.value.role === role
  }

  // 多个权限检查（满足任意一个）
  const hasAnyPermission = (...permissions) => {
    return permissions.some(permission => hasPermission(permission))
  }

  // 多个权限检查（满足所有）
  const hasAllPermissions = (...permissions) => {
    return permissions.every(permission => hasPermission(permission))
  }

  // 角色级别检查（大于等于指定级别）
  const hasRoleLevel = (level) => {
    if (!currentUser.value) return false
    const userRoleConfig = ROLE_CONFIG[currentUser.value.role]
    return userRoleConfig && userRoleConfig.level <= level
  }

  // 获取用户角色信息
  const getUserRoleInfo = computed(() => {
    if (!currentUser.value) return null
    return ROLE_CONFIG[currentUser.value.role] || null
  })

  // 获取用户权限列表
  const getUserPermissions = computed(() => {
    if (!currentUser.value) return []
    return currentUser.value.permissions || []
  })

  // 设置当前用户
  const setCurrentUser = (user) => {
    currentUser.value = user
  }

  // 权限验证包装函数
  const requirePermission = (permission, callback) => {
    if (hasPermission(permission)) {
      return callback()
    } else {
      ElMessage.error('权限不足，无法执行此操作')
      return false
    }
  }

  return {
    // 状态
    currentUser,
    
    // 权限检查
    hasPermission,
    hasRole,
    hasAnyPermission,
    hasAllPermissions,
    hasRoleLevel,
    
    // 计算属性
    getUserRoleInfo,
    getUserPermissions,
    
    // 方法
    setCurrentUser,
    requirePermission
  }
}

// 用户管理组合式函数
export function useUserManagement() {
  const { currentUser, hasPermission, hasRole } = useAuth()
  
  // 用户列表
  const users = ref([])
  const loading = ref(false)
  const total = ref(0)
  
  // 查询参数
  const queryParams = reactive({
    page: 1,
    pageSize: 10,
    username: '',
    phone: '',
    role: '',
    status: ''
  })

  // 获取用户列表
  const fetchUsers = async () => {
    if (!hasPermission(PERMISSIONS.USER_VIEW)) {
      ElMessage.error('没有查看用户列表的权限')
      return
    }

    loading.value = true
    try {
      // 模拟API调用
      const mockUsers = [
        {
          id: 1,
          username: 'admin',
          phone: '13800138000',
          avatar: '',
          role: ROLES.SUPER_ADMIN,
          status: 'active',
          createTime: '2024-01-01 10:00:00',
          lastLoginTime: '2024-12-20 15:30:00'
        },
        {
          id: 2,
          username: 'sales_manager',
          phone: '13800138001',
          avatar: '',
          role: ROLES.MANAGER,
          status: 'active',
          createTime: '2024-01-15 14:20:00',
          lastLoginTime: '2024-12-19 09:15:00'
        }
      ]
      
      users.value = mockUsers
      total.value = mockUsers.length
    } catch (error) {
      ElMessage.error('获取用户列表失败')
    } finally {
      loading.value = false
    }
  }

  // 创建用户
  const createUser = async (userData) => {
    if (!hasPermission(PERMISSIONS.USER_CREATE)) {
      ElMessage.error('没有创建用户的权限')
      return false
    }

    try {
      // 模拟API调用
      const newUser = {
        id: Date.now(),
        ...userData,
        createTime: new Date().toLocaleString()
      }
      users.value.unshift(newUser)
      ElMessage.success('用户创建成功')
      return true
    } catch (error) {
      ElMessage.error('用户创建失败')
      return false
    }
  }

  // 更新用户
  const updateUser = async (id, userData) => {
    if (!hasPermission(PERMISSIONS.USER_UPDATE)) {
      ElMessage.error('没有更新用户的权限')
      return false
    }

    try {
      const index = users.value.findIndex(user => user.id === id)
      if (index !== -1) {
        users.value[index] = { ...users.value[index], ...userData }
        ElMessage.success('用户更新成功')
        return true
      }
      return false
    } catch (error) {
      ElMessage.error('用户更新失败')
      return false
    }
  }

  // 删除用户
  const deleteUser = async (id) => {
    if (!hasPermission(PERMISSIONS.USER_DELETE)) {
      ElMessage.error('没有删除用户的权限')
      return false
    }

    try {
      users.value = users.value.filter(user => user.id !== id)
      ElMessage.success('用户删除成功')
      return true
    } catch (error) {
      ElMessage.error('用户删除失败')
      return false
    }
  }

  // 重置用户密码
  const resetUserPassword = async (id) => {
    if (!hasPermission(PERMISSIONS.USER_UPDATE)) {
      ElMessage.error('没有重置密码的权限')
      return false
    }

    try {
      ElMessage.success('密码重置成功，默认密码：123456')
      return true
    } catch (error) {
      ElMessage.error('密码重置失败')
      return false
    }
  }

  return {
    // 状态
    users,
    loading,
    total,
    queryParams,
    
    // 方法
    fetchUsers,
    createUser,
    updateUser,
    deleteUser,
    resetUserPassword
  }
}