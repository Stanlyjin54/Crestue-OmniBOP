import { ref, computed, inject } from 'vue'
import { permissionValidator } from '@/utils/security'

// 权限类型枚举
export const PERMISSION_TYPES = {
  // 系统管理
  SYSTEM_ADMIN: 'system:admin',
  SYSTEM_CONFIG: 'system:config',
  SYSTEM_LOG: 'system:log',
  SYSTEM_MONITOR: 'system:monitor',
  
  // 用户管理
  USER_VIEW: 'user:view',
  USER_CREATE: 'user:create',
  USER_UPDATE: 'user:update',
  USER_DELETE: 'user:delete',
  USER_ROLE: 'user:role',
  
  // 角色管理
  ROLE_VIEW: 'role:view',
  ROLE_CREATE: 'role:create',
  ROLE_UPDATE: 'role:update',
  ROLE_DELETE: 'role:delete',
  ROLE_PERMISSION: 'role:permission',
  
  // 客户管理
  CUSTOMER_VIEW: 'customer:view',
  CUSTOMER_CREATE: 'customer:create',
  CUSTOMER_UPDATE: 'customer:update',
  CUSTOMER_DELETE: 'customer:delete',
  CUSTOMER_EXPORT: 'customer:export',
  CUSTOMER_IMPORT: 'customer:import',
  
  // 订单管理
  ORDER_VIEW: 'order:view',
  ORDER_CREATE: 'order:create',
  ORDER_UPDATE: 'order:update',
  ORDER_DELETE: 'order:delete',
  ORDER_EXPORT: 'order:export',
  ORDER_APPROVE: 'order:approve',
  
  // 产品管理
  PRODUCT_VIEW: 'product:view',
  PRODUCT_CREATE: 'product:create',
  PRODUCT_UPDATE: 'product:update',
  PRODUCT_DELETE: 'product:delete',
  PRODUCT_CATEGORY: 'product:category',
  
  // 库存管理
  INVENTORY_VIEW: 'inventory:view',
  INVENTORY_UPDATE: 'inventory:update',
  INVENTORY_ADJUST: 'inventory:adjust',
  INVENTORY_WAREHOUSE: 'inventory:warehouse',
  
  // 财务管理
  FINANCE_VIEW: 'finance:view',
  FINANCE_CREATE: 'finance:create',
  FINANCE_UPDATE: 'finance:update',
  FINANCE_DELETE: 'finance:delete',
  FINANCE_EXPORT: 'finance:export',
  
  // 报表管理
  REPORT_VIEW: 'report:view',
  REPORT_CREATE: 'report:create',
  REPORT_UPDATE: 'report:update',
  REPORT_DELETE: 'report:delete',
  REPORT_EXPORT: 'report:export',
  
  // 消息管理
  MESSAGE_VIEW: 'message:view',
  MESSAGE_CREATE: 'message:create',
  MESSAGE_UPDATE: 'message:update',
  MESSAGE_DELETE: 'message:delete',
  
  // 文件管理
  FILE_VIEW: 'file:view',
  FILE_UPLOAD: 'file:upload',
  FILE_DOWNLOAD: 'file:download',
  FILE_DELETE: 'file:delete',
  
  // 操作日志
  LOG_VIEW: 'log:view',
  LOG_EXPORT: 'log:export',
  LOG_DELETE: 'log:delete',
  
  // 政府采购信息采集
  PROCUREMENT_VIEW: 'procurement:view',
  PROCUREMENT_CREATE: 'procurement:create',
  PROCUREMENT_UPDATE: 'procurement:update',
  PROCUREMENT_DELETE: 'procurement:delete',
  PROCUREMENT_EXPORT: 'procurement:export',
  PROCUREMENT_CRAWL: 'procurement:crawl',
  PROCUREMENT_MONITOR: 'procurement:monitor',
  
  // 社交媒体商机
  SOCIAL_MEDIA_VIEW: 'social_media:view',
  SOCIAL_MEDIA_CREATE: 'social_media:create',
  SOCIAL_MEDIA_UPDATE: 'social_media:update',
  SOCIAL_MEDIA_DELETE: 'social_media:delete',
  SOCIAL_MEDIA_MONITOR: 'social_media:monitor',
  SOCIAL_MEDIA_COMPLIANCE: 'social_media:compliance',
  SOCIAL_MEDIA_EXPORT: 'social_media:export'
}

// 角色权限映射
export const ROLE_PERMISSIONS = {
  // 超级管理员
  SUPER_ADMIN: Object.values(PERMISSION_TYPES),
  
  // 管理员
  ADMIN: [
    PERMISSION_TYPES.SYSTEM_CONFIG,
    PERMISSION_TYPES.USER_VIEW,
    PERMISSION_TYPES.USER_CREATE,
    PERMISSION_TYPES.USER_UPDATE,
    PERMISSION_TYPES.ROLE_VIEW,
    PERMISSION_TYPES.ROLE_CREATE,
    PERMISSION_TYPES.ROLE_UPDATE,
    PERMISSION_TYPES.CUSTOMER_VIEW,
    PERMISSION_TYPES.CUSTOMER_CREATE,
    PERMISSION_TYPES.CUSTOMER_UPDATE,
    PERMISSION_TYPES.ORDER_VIEW,
    PERMISSION_TYPES.ORDER_CREATE,
    PERMISSION_TYPES.ORDER_UPDATE,
    PERMISSION_TYPES.PRODUCT_VIEW,
    PERMISSION_TYPES.PRODUCT_CREATE,
    PERMISSION_TYPES.PRODUCT_UPDATE,
    PERMISSION_TYPES.INVENTORY_VIEW,
    PERMISSION_TYPES.INVENTORY_UPDATE,
    PERMISSION_TYPES.FINANCE_VIEW,
    PERMISSION_TYPES.FINANCE_CREATE,
    PERMISSION_TYPES.FINANCE_UPDATE,
    PERMISSION_TYPES.REPORT_VIEW,
    PERMISSION_TYPES.REPORT_CREATE,
    PERMISSION_TYPES.MESSAGE_VIEW,
    PERMISSION_TYPES.MESSAGE_CREATE,
    PERMISSION_TYPES.FILE_VIEW,
    PERMISSION_TYPES.FILE_UPLOAD,
    PERMISSION_TYPES.FILE_DOWNLOAD,
    PERMISSION_TYPES.LOG_VIEW,
    PERMISSION_TYPES.PROCUREMENT_VIEW,
    PERMISSION_TYPES.PROCUREMENT_CREATE,
    PERMISSION_TYPES.PROCUREMENT_UPDATE,
    PERMISSION_TYPES.PROCUREMENT_EXPORT,
    PERMISSION_TYPES.PROCUREMENT_CRAWL,
    PERMISSION_TYPES.PROCUREMENT_MONITOR,
    PERMISSION_TYPES.SOCIAL_MEDIA_VIEW,
    PERMISSION_TYPES.SOCIAL_MEDIA_CREATE,
    PERMISSION_TYPES.SOCIAL_MEDIA_UPDATE,
    PERMISSION_TYPES.SOCIAL_MEDIA_MONITOR,
    PERMISSION_TYPES.SOCIAL_MEDIA_COMPLIANCE,
    PERMISSION_TYPES.SOCIAL_MEDIA_EXPORT
  ],
  
  // 销售经理
  SALES_MANAGER: [
    PERMISSION_TYPES.CUSTOMER_VIEW,
    PERMISSION_TYPES.CUSTOMER_CREATE,
    PERMISSION_TYPES.CUSTOMER_UPDATE,
    PERMISSION_TYPES.CUSTOMER_EXPORT,
    PERMISSION_TYPES.ORDER_VIEW,
    PERMISSION_TYPES.ORDER_CREATE,
    PERMISSION_TYPES.ORDER_UPDATE,
    PERMISSION_TYPES.PRODUCT_VIEW,
    PERMISSION_TYPES.INVENTORY_VIEW,
    PERMISSION_TYPES.REPORT_VIEW,
    PERMISSION_TYPES.MESSAGE_VIEW,
    PERMISSION_TYPES.MESSAGE_CREATE,
    PERMISSION_TYPES.FILE_VIEW,
    PERMISSION_TYPES.FILE_UPLOAD,
    PERMISSION_TYPES.FILE_DOWNLOAD
  ],
  
  // 财务人员
  FINANCE_STAFF: [
    PERMISSION_TYPES.ORDER_VIEW,
    PERMISSION_TYPES.FINANCE_VIEW,
    PERMISSION_TYPES.FINANCE_CREATE,
    PERMISSION_TYPES.FINANCE_UPDATE,
    PERMISSION_TYPES.FINANCE_EXPORT,
    PERMISSION_TYPES.REPORT_VIEW,
    PERMISSION_TYPES.REPORT_EXPORT,
    PERMISSION_TYPES.CUSTOMER_VIEW,
    PERMISSION_TYPES.FILE_VIEW,
    PERMISSION_TYPES.FILE_DOWNLOAD
  ],
  
  // 仓库管理员
  WAREHOUSE_MANAGER: [
    PERMISSION_TYPES.PRODUCT_VIEW,
    PERMISSION_TYPES.INVENTORY_VIEW,
    PERMISSION_TYPES.INVENTORY_UPDATE,
    PERMISSION_TYPES.INVENTORY_ADJUST,
    PERMISSION_TYPES.ORDER_VIEW,
    PERMISSION_TYPES.REPORT_VIEW,
    PERMISSION_TYPES.FILE_VIEW,
    PERMISSION_TYPES.FILE_UPLOAD,
    PERMISSION_TYPES.FILE_DOWNLOAD
  ],
  
  // 普通员工
  STAFF: [
    PERMISSION_TYPES.CUSTOMER_VIEW,
    PERMISSION_TYPES.ORDER_VIEW,
    PERMISSION_TYPES.PRODUCT_VIEW,
    PERMISSION_TYPES.INVENTORY_VIEW,
    PERMISSION_TYPES.MESSAGE_VIEW,
    PERMISSION_TYPES.FILE_VIEW,
    PERMISSION_TYPES.FILE_DOWNLOAD
  ],
  
  // 只读用户
  READONLY: [
    PERMISSION_TYPES.CUSTOMER_VIEW,
    PERMISSION_TYPES.ORDER_VIEW,
    PERMISSION_TYPES.PRODUCT_VIEW,
    PERMISSION_TYPES.INVENTORY_VIEW,
    PERMISSION_TYPES.FINANCE_VIEW,
    PERMISSION_TYPES.REPORT_VIEW,
    PERMISSION_TYPES.MESSAGE_VIEW,
    PERMISSION_TYPES.FILE_VIEW,
    PERMISSION_TYPES.LOG_VIEW
  ]
}

// 权限组合
export const PERMISSION_GROUPS = {
  // 系统管理权限组
  SYSTEM_MANAGEMENT: [
    PERMISSION_TYPES.SYSTEM_ADMIN,
    PERMISSION_TYPES.SYSTEM_CONFIG,
    PERMISSION_TYPES.SYSTEM_LOG,
    PERMISSION_TYPES.SYSTEM_MONITOR
  ],
  
  // 用户管理权限组
  USER_MANAGEMENT: [
    PERMISSION_TYPES.USER_VIEW,
    PERMISSION_TYPES.USER_CREATE,
    PERMISSION_TYPES.USER_UPDATE,
    PERMISSION_TYPES.USER_DELETE,
    PERMISSION_TYPES.USER_ROLE,
    PERMISSION_TYPES.ROLE_VIEW,
    PERMISSION_TYPES.ROLE_CREATE,
    PERMISSION_TYPES.ROLE_UPDATE,
    PERMISSION_TYPES.ROLE_DELETE,
    PERMISSION_TYPES.ROLE_PERMISSION
  ],
  
  // 客户管理权限组
  CUSTOMER_MANAGEMENT: [
    PERMISSION_TYPES.CUSTOMER_VIEW,
    PERMISSION_TYPES.CUSTOMER_CREATE,
    PERMISSION_TYPES.CUSTOMER_UPDATE,
    PERMISSION_TYPES.CUSTOMER_DELETE,
    PERMISSION_TYPES.CUSTOMER_EXPORT,
    PERMISSION_TYPES.CUSTOMER_IMPORT
  ],
  
  // 订单管理权限组
  ORDER_MANAGEMENT: [
    PERMISSION_TYPES.ORDER_VIEW,
    PERMISSION_TYPES.ORDER_CREATE,
    PERMISSION_TYPES.ORDER_UPDATE,
    PERMISSION_TYPES.ORDER_DELETE,
    PERMISSION_TYPES.ORDER_EXPORT,
    PERMISSION_TYPES.ORDER_APPROVE
  ],
  
  // 产品管理权限组
  PRODUCT_MANAGEMENT: [
    PERMISSION_TYPES.PRODUCT_VIEW,
    PERMISSION_TYPES.PRODUCT_CREATE,
    PERMISSION_TYPES.PRODUCT_UPDATE,
    PERMISSION_TYPES.PRODUCT_DELETE,
    PERMISSION_TYPES.PRODUCT_CATEGORY
  ],
  
  // 库存管理权限组
  INVENTORY_MANAGEMENT: [
    PERMISSION_TYPES.INVENTORY_VIEW,
    PERMISSION_TYPES.INVENTORY_UPDATE,
    PERMISSION_TYPES.INVENTORY_ADJUST,
    PERMISSION_TYPES.INVENTORY_WAREHOUSE
  ],
  
  // 财务管理权限组
  FINANCE_MANAGEMENT: [
    PERMISSION_TYPES.FINANCE_VIEW,
    PERMISSION_TYPES.FINANCE_CREATE,
    PERMISSION_TYPES.FINANCE_UPDATE,
    PERMISSION_TYPES.FINANCE_DELETE,
    PERMISSION_TYPES.FINANCE_EXPORT
  ],
  
  // 报表管理权限组
  REPORT_MANAGEMENT: [
    PERMISSION_TYPES.REPORT_VIEW,
    PERMISSION_TYPES.REPORT_CREATE,
    PERMISSION_TYPES.REPORT_UPDATE,
    PERMISSION_TYPES.REPORT_DELETE,
    PERMISSION_TYPES.REPORT_EXPORT
  ],
  
  // 文件管理权限组
  FILE_MANAGEMENT: [
    PERMISSION_TYPES.FILE_VIEW,
    PERMISSION_TYPES.FILE_UPLOAD,
    PERMISSION_TYPES.FILE_DOWNLOAD,
    PERMISSION_TYPES.FILE_DELETE
  ],
  
  // 操作日志
  LOG_MANAGEMENT: [
    PERMISSION_TYPES.LOG_VIEW,
    PERMISSION_TYPES.LOG_EXPORT,
    PERMISSION_TYPES.LOG_DELETE
  ],
  
  // 政府采购管理权限组
  PROCUREMENT_MANAGEMENT: [
    PERMISSION_TYPES.PROCUREMENT_VIEW,
    PERMISSION_TYPES.PROCUREMENT_CREATE,
    PERMISSION_TYPES.PROCUREMENT_UPDATE,
    PERMISSION_TYPES.PROCUREMENT_DELETE,
    PERMISSION_TYPES.PROCUREMENT_EXPORT,
    PERMISSION_TYPES.PROCUREMENT_CRAWL,
    PERMISSION_TYPES.PROCUREMENT_MONITOR
  ],
  
  // 社交媒体管理权限组
  SOCIAL_MEDIA_MANAGEMENT: [
    PERMISSION_TYPES.SOCIAL_MEDIA_VIEW,
    PERMISSION_TYPES.SOCIAL_MEDIA_CREATE,
    PERMISSION_TYPES.SOCIAL_MEDIA_UPDATE,
    PERMISSION_TYPES.SOCIAL_MEDIA_DELETE,
    PERMISSION_TYPES.SOCIAL_MEDIA_MONITOR,
    PERMISSION_TYPES.SOCIAL_MEDIA_COMPLIANCE,
    PERMISSION_TYPES.SOCIAL_MEDIA_EXPORT
  ]
}

// 权限检查函数
export function usePermission() {
  const permissions = ref([])
  const roles = ref([])
  const isLoading = ref(false)
  
  // 当前用户权限
  const currentPermissions = computed(() => {
    return new Set(permissions.value)
  })
  
  // 当前用户角色
  const currentRoles = computed(() => {
    return new Set(roles.value)
  })
  
  // 是否有权限
  const hasPermission = (permission) => {
    if (!permission) return false
    return currentPermissions.value.has(permission)
  }
  
  // 是否有任意权限
  const hasAnyPermission = (...perms) => {
    if (!perms || perms.length === 0) return false
    return perms.some(permission => hasPermission(permission))
  }
  
  // 是否有所有权限
  const hasAllPermissions = (...perms) => {
    if (!perms || perms.length === 0) return false
    return perms.every(permission => hasPermission(permission))
  }
  
  // 是否有角色
  const hasRole = (role) => {
    if (!role) return false
    return currentRoles.value.has(role)
  }
  
  // 是否有任意角色
  const hasAnyRole = (...roles) => {
    if (!roles || roles.length === 0) return false
    return roles.some(role => hasRole(role))
  }
  
  // 是否有所有角色
  const hasAllRoles = (...roles) => {
    if (!roles || roles.length === 0) return false
    return roles.every(role => hasRole(role))
  }
  
  // 获取角色权限
  const getRolePermissions = (role) => {
    return ROLE_PERMISSIONS[role] || []
  }
  
  // 检查权限组
  const hasPermissionGroup = (group) => {
    const groupPermissions = PERMISSION_GROUPS[group]
    if (!groupPermissions) return false
    
    return groupPermissions.some(permission => hasPermission(permission))
  }
  
  // 加载用户权限
  const loadPermissions = async () => {
    isLoading.value = true
    try {
      // 这里应该从API获取用户权限
      // 模拟数据
      const mockPermissions = [
        PERMISSION_TYPES.CUSTOMER_VIEW,
        PERMISSION_TYPES.CUSTOMER_CREATE,
        PERMISSION_TYPES.CUSTOMER_UPDATE,
        PERMISSION_TYPES.ORDER_VIEW,
        PERMISSION_TYPES.ORDER_CREATE,
        PERMISSION_TYPES.PRODUCT_VIEW,
        PERMISSION_TYPES.REPORT_VIEW
      ]
      
      permissions.value = mockPermissions
      permissionValidator.setPermissions(mockPermissions)
      
      return mockPermissions
    } catch (error) {
      console.error('加载权限失败:', error)
      permissions.value = []
      return []
    } finally {
      isLoading.value = false
    }
  }
  
  // 加载用户角色
  const loadRoles = async () => {
    try {
      // 这里应该从API获取用户角色
      // 模拟数据
      const mockRoles = ['SALES_MANAGER']
      roles.value = mockRoles
      return mockRoles
    } catch (error) {
      console.error('加载角色失败:', error)
      roles.value = []
      return []
    }
  }
  
  // 验证路由权限
  const validateRoutePermission = (route) => {
    const requiredPermission = route.meta?.requiredPermission
    const requiredRole = route.meta?.requiredRole
    
    if (requiredPermission && !hasPermission(requiredPermission)) {
      return false
    }
    
    if (requiredRole && !hasRole(requiredRole)) {
      return false
    }
    
    return true
  }
  
  // 获取权限名称
  const getPermissionName = (permission) => {
    const permissionNames = {
      [PERMISSION_TYPES.SYSTEM_ADMIN]: '系统管理员',
      [PERMISSION_TYPES.SYSTEM_CONFIG]: '系统配置',
      [PERMISSION_TYPES.SYSTEM_LOG]: '系统日志',
      [PERMISSION_TYPES.SYSTEM_MONITOR]: '系统监控',
      [PERMISSION_TYPES.USER_VIEW]: '用户查看',
      [PERMISSION_TYPES.USER_CREATE]: '用户创建',
      [PERMISSION_TYPES.USER_UPDATE]: '用户更新',
      [PERMISSION_TYPES.USER_DELETE]: '用户删除',
      [PERMISSION_TYPES.USER_ROLE]: '用户角色管理',
      [PERMISSION_TYPES.ROLE_VIEW]: '角色查看',
      [PERMISSION_TYPES.ROLE_CREATE]: '角色创建',
      [PERMISSION_TYPES.ROLE_UPDATE]: '角色更新',
      [PERMISSION_TYPES.ROLE_DELETE]: '角色删除',
      [PERMISSION_TYPES.ROLE_PERMISSION]: '角色权限管理',
      [PERMISSION_TYPES.CUSTOMER_VIEW]: '客户查看',
      [PERMISSION_TYPES.CUSTOMER_CREATE]: '客户创建',
      [PERMISSION_TYPES.CUSTOMER_UPDATE]: '客户更新',
      [PERMISSION_TYPES.CUSTOMER_DELETE]: '客户删除',
      [PERMISSION_TYPES.CUSTOMER_EXPORT]: '客户导出',
      [PERMISSION_TYPES.CUSTOMER_IMPORT]: '客户导入',
      [PERMISSION_TYPES.ORDER_VIEW]: '订单查看',
      [PERMISSION_TYPES.ORDER_CREATE]: '订单创建',
      [PERMISSION_TYPES.ORDER_UPDATE]: '订单更新',
      [PERMISSION_TYPES.ORDER_DELETE]: '订单删除',
      [PERMISSION_TYPES.ORDER_EXPORT]: '订单导出',
      [PERMISSION_TYPES.ORDER_APPROVE]: '订单审批',
      [PERMISSION_TYPES.PRODUCT_VIEW]: '产品查看',
      [PERMISSION_TYPES.PRODUCT_CREATE]: '产品创建',
      [PERMISSION_TYPES.PRODUCT_UPDATE]: '产品更新',
      [PERMISSION_TYPES.PRODUCT_DELETE]: '产品删除',
      [PERMISSION_TYPES.PRODUCT_CATEGORY]: '产品分类管理',
      [PERMISSION_TYPES.INVENTORY_VIEW]: '库存查看',
      [PERMISSION_TYPES.INVENTORY_UPDATE]: '库存更新',
      [PERMISSION_TYPES.INVENTORY_ADJUST]: '库存调整',
      [PERMISSION_TYPES.INVENTORY_WAREHOUSE]: '仓库管理',
      [PERMISSION_TYPES.FINANCE_VIEW]: '财务查看',
      [PERMISSION_TYPES.FINANCE_CREATE]: '财务创建',
      [PERMISSION_TYPES.FINANCE_UPDATE]: '财务更新',
      [PERMISSION_TYPES.FINANCE_DELETE]: '财务删除',
      [PERMISSION_TYPES.FINANCE_EXPORT]: '财务导出',
      [PERMISSION_TYPES.REPORT_VIEW]: '报表查看',
      [PERMISSION_TYPES.REPORT_CREATE]: '报表创建',
      [PERMISSION_TYPES.REPORT_UPDATE]: '报表更新',
      [PERMISSION_TYPES.REPORT_DELETE]: '报表删除',
      [PERMISSION_TYPES.REPORT_EXPORT]: '报表导出',
      [PERMISSION_TYPES.MESSAGE_VIEW]: '消息查看',
      [PERMISSION_TYPES.MESSAGE_CREATE]: '消息创建',
      [PERMISSION_TYPES.MESSAGE_UPDATE]: '消息更新',
      [PERMISSION_TYPES.MESSAGE_DELETE]: '消息删除',
      [PERMISSION_TYPES.FILE_VIEW]: '文件查看',
      [PERMISSION_TYPES.FILE_UPLOAD]: '文件上传',
      [PERMISSION_TYPES.FILE_DOWNLOAD]: '文件下载',
      [PERMISSION_TYPES.FILE_DELETE]: '文件删除',
      [PERMISSION_TYPES.LOG_VIEW]: '日志查看',
      [PERMISSION_TYPES.LOG_EXPORT]: '日志导出',
      [PERMISSION_TYPES.LOG_DELETE]: '日志删除',
      
      // 政府采购信息采集
      [PERMISSION_TYPES.PROCUREMENT_VIEW]: '政府采购查看',
      [PERMISSION_TYPES.PROCUREMENT_CREATE]: '政府采购创建',
      [PERMISSION_TYPES.PROCUREMENT_UPDATE]: '政府采购更新',
      [PERMISSION_TYPES.PROCUREMENT_DELETE]: '政府采购删除',
      [PERMISSION_TYPES.PROCUREMENT_EXPORT]: '政府采购导出',
      [PERMISSION_TYPES.PROCUREMENT_CRAWL]: '政府采购爬取',
      [PERMISSION_TYPES.PROCUREMENT_MONITOR]: '政府采购监控',
      
      // 社交媒体商机
      [PERMISSION_TYPES.SOCIAL_MEDIA_VIEW]: '社交媒体查看',
      [PERMISSION_TYPES.SOCIAL_MEDIA_CREATE]: '社交媒体创建',
      [PERMISSION_TYPES.SOCIAL_MEDIA_UPDATE]: '社交媒体更新',
      [PERMISSION_TYPES.SOCIAL_MEDIA_DELETE]: '社交媒体删除',
      [PERMISSION_TYPES.SOCIAL_MEDIA_MONITOR]: '社交媒体监控',
      [PERMISSION_TYPES.SOCIAL_MEDIA_COMPLIANCE]: '社交媒体合规',
      [PERMISSION_TYPES.SOCIAL_MEDIA_EXPORT]: '社交媒体导出'
    }
    
    return permissionNames[permission] || permission
  }
  
  // 获取角色名称
  const getRoleName = (role) => {
    const roleNames = {
      SUPER_ADMIN: '超级管理员',
      ADMIN: '管理员',
      SALES_MANAGER: '销售经理',
      FINANCE_STAFF: '财务人员',
      WAREHOUSE_MANAGER: '仓库管理员',
      STAFF: '普通员工',
      READONLY: '只读用户'
    }
    
    return roleNames[role] || role
  }
  
  // 获取权限组名称
  const getPermissionGroupName = (group) => {
    const groupNames = {
      SYSTEM_MANAGEMENT: '系统管理',
      USER_MANAGEMENT: '用户管理',
      CUSTOMER_MANAGEMENT: '客户管理',
      ORDER_MANAGEMENT: '订单管理',
      PRODUCT_MANAGEMENT: '产品管理',
      INVENTORY_MANAGEMENT: '库存管理',
      FINANCE_MANAGEMENT: '财务管理',
      REPORT_MANAGEMENT: '报表管理',
      FILE_MANAGEMENT: '文件管理',
      LOG_MANAGEMENT: '日志管理',
      PROCUREMENT_MANAGEMENT: '政府采购管理',
      SOCIAL_MEDIA_MANAGEMENT: '社交媒体管理'
    }
    
    return groupNames[group] || group
  }
  
  return {
    // 状态
    permissions,
    roles,
    isLoading,
    
    // 计算属性
    currentPermissions,
    currentRoles,
    
    // 方法
    hasPermission,
    hasAnyPermission,
    hasAllPermissions,
    hasRole,
    hasAnyRole,
    hasAllRoles,
    getRolePermissions,
    hasPermissionGroup,
    loadPermissions,
    loadRoles,
    validateRoutePermission,
    getPermissionName,
    getRoleName,
    getPermissionGroupName,
    
    // 常量
    PERMISSION_TYPES,
    ROLE_PERMISSIONS,
    PERMISSION_GROUPS
  }
}

// 权限指令
export function createPermissionDirective(app) {
  app.directive('permission', {
    mounted(el, binding) {
      const { value } = binding
      const { hasPermission } = usePermission()
      
      if (!value) {
        console.warn('v-permission 指令需要权限值')
        return
      }
      
      if (!hasPermission(value)) {
        el.parentNode && el.parentNode.removeChild(el)
      }
    }
  })
  
  app.directive('role', {
    mounted(el, binding) {
      const { value } = binding
      const { hasRole } = usePermission()
      
      if (!value) {
        console.warn('v-role 指令需要角色值')
        return
      }
      
      if (!hasRole(value)) {
        el.parentNode && el.parentNode.removeChild(el)
      }
    }
  })
  
  app.directive('any-permission', {
    mounted(el, binding) {
      const { value } = binding
      const { hasAnyPermission } = usePermission()
      
      if (!value || !Array.isArray(value)) {
        console.warn('v-any-permission 指令需要权限数组')
        return
      }
      
      if (!hasAnyPermission(...value)) {
        el.parentNode && el.parentNode.removeChild(el)
      }
    }
  })
  
  app.directive('all-permissions', {
    mounted(el, binding) {
      const { value } = binding
      const { hasAllPermissions } = usePermission()
      
      if (!value || !Array.isArray(value)) {
        console.warn('v-all-permissions 指令需要权限数组')
        return
      }
      
      if (!hasAllPermissions(...value)) {
        el.parentNode && el.parentNode.removeChild(el)
      }
    }
  })
}