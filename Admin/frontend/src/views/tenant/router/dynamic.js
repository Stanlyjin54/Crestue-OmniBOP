/**
 * 多租户管理动态路由配置
 */

// 路由配置
export const tenantRoutes = [
  {
    path: 'tenant',
    name: 'TenantManagement',
    component: () => import('@/views/tenant/management.vue'),
    meta: {
      title: '多租户管理',
      icon: 'OfficeBuilding',
      permission: 'tenant:view',
      keepAlive: true,
      showInMenu: true,
      menuOrder: 3
    }
  }
]

// 权限映射
export const tenantPermissions = {
  'tenant:view': {
    name: '查看租户',
    description: '查看租户列表和详情'
  },
  'tenant:create': {
    name: '创建租户',
    description: '创建新的租户'
  },
  'tenant:edit': {
    name: '编辑租户',
    description: '编辑租户信息和配置'
  },
  'tenant:delete': {
    name: '删除租户',
    description: '删除租户（软删除）'
  },
  'tenant:manage_users': {
    name: '管理租户用户',
    description: '管理租户下的用户'
  },
  'tenant:manage_resources': {
    name: '管理租户资源',
    description: '管理租户资源配额'
  },
  'tenant:view_analytics': {
    name: '查看租户分析',
    description: '查看租户使用分析和统计'
  },
  'tenant:export': {
    name: '导出租户数据',
    description: '导出租户数据'
  }
}

// 菜单配置
export const tenantMenuConfig = {
  id: 'tenant',
  title: '多租户管理',
  icon: 'OfficeBuilding',
  path: '/admin/tenant',
  children: [
    {
      id: 'tenant_list',
      title: '租户列表',
      path: '/admin/tenant',
      icon: 'List',
      permission: 'tenant:view'
    },
    {
      id: 'tenant_create',
      title: '创建租户',
      path: '/admin/tenant/create',
      icon: 'Plus',
      permission: 'tenant:create',
      hidden: true
    }
  ]
}

// API端点配置
export const tenantApiEndpoints = {
  // 租户管理
  getTenants: '/api/admin/tenant/tenants',
  getTenant: '/api/admin/tenant/tenants/{id}',
  createTenant: '/api/admin/tenant/tenants',
  updateTenant: '/api/admin/tenant/tenants/{id}',
  deleteTenant: '/api/admin/tenant/tenants/{id}',
  toggleTenantStatus: '/api/admin/tenant/tenants/{id}/status',
  getTenantStats: '/api/admin/tenant/tenants/{id}/stats',
  
  // 租户用户管理
  getTenantUsers: '/api/admin/tenant/tenants/{id}/users',
  addUserToTenant: '/api/admin/tenant/tenants/{id}/users',
  updateTenantUser: '/api/admin/tenant/tenants/{id}/users/{userId}',
  removeUserFromTenant: '/api/admin/tenant/tenants/{id}/users/{userId}',
  updateUserStatus: '/api/admin/tenant/tenants/{id}/users/{userId}/status',
  
  // 租户分析
  getTenantAnalytics: '/api/admin/tenant/analytics',
  getTenantUsageTrend: '/api/admin/tenant/tenants/{id}/usage-trend',
  getTenantResourceUsage: '/api/admin/tenant/tenants/{id}/resource-usage',
  
  // 租户导出
  exportTenants: '/api/admin/tenant/export',
  exportTenantUsers: '/api/admin/tenant/tenants/{id}/export'
}

// 快捷操作配置
export const tenantQuickActions = [
  {
    id: 'create_tenant',
    name: '创建租户',
    icon: 'Plus',
    type: 'primary',
    permission: 'tenant:create',
    handler: 'createTenant'
  },
  {
    id: 'export_data',
    name: '导出数据',
    icon: 'Download',
    type: 'default',
    permission: 'tenant:export',
    handler: 'exportData'
  },
  {
    id: 'batch_operations',
    name: '批量操作',
    icon: 'Operation',
    type: 'default',
    permission: 'tenant:edit',
    handler: 'batchOperations'
  },
  {
    id: 'view_analytics',
    name: '查看分析',
    icon: 'TrendCharts',
    type: 'default',
    permission: 'tenant:view_analytics',
    handler: 'viewAnalytics'
  }
]

// 统计指标配置
export const tenantStatisticsConfig = {
  totalTenants: {
    label: '总租户数',
    value: 0,
    unit: '个',
    color: '#409EFF',
    icon: 'OfficeBuilding'
  },
  activeTenants: {
    label: '活跃租户',
    value: 0,
    unit: '个',
    color: '#67C23A',
    icon: 'Check'
  },
  inactiveTenants: {
    label: '非活跃租户',
    value: 0,
    unit: '个',
    color: '#E6A23C',
    icon: 'Warning'
  },
  totalUsers: {
    label: '总用户数',
    value: 0,
    unit: '人',
    color: '#F56C6C',
    icon: 'User'
  },
  totalStorage: {
    label: '总存储使用',
    value: 0,
    unit: 'GB',
    color: '#909399',
    icon: 'Files'
  },
  totalApiCalls: {
    label: '总API调用',
    value: 0,
    unit: '次',
    color: '#909399',
    icon: 'Connection'
  }
}

// 筛选条件配置
export const tenantFilterConfig = [
  {
    field: 'tenantType',
    label: '租户类型',
    type: 'select',
    options: [
      { label: '企业版', value: 'ENTERPRISE' },
      { label: '专业版', value: 'PROFESSIONAL' },
      { label: '基础版', value: 'BASIC' },
      { label: '试用版', value: 'TRIAL' },
      { label: '系统', value: 'SYSTEM' }
    ]
  },
  {
    field: 'status',
    label: '租户状态',
    type: 'select',
    options: [
      { label: '活跃', value: 'ACTIVE' },
      { label: '非活跃', value: 'INACTIVE' },
      { label: '暂停', value: 'SUSPENDED' },
      { label: '待审核', value: 'PENDING' }
    ]
  },
  {
    field: 'industry',
    label: '行业',
    type: 'select',
    options: [
      { label: '互联网', value: '互联网' },
      { label: '电商', value: '电商' },
      { label: '金融', value: '金融' },
      { label: '制造业', value: '制造业' },
      { label: '教育', value: '教育' },
      { label: '医疗', value: '医疗' },
      { label: '房地产', value: '房地产' },
      { label: '其他', value: '其他' }
    ]
  },
  {
    field: 'createdAt',
    label: '创建时间',
    type: 'daterange',
    format: 'YYYY-MM-DD'
  },
  {
    field: 'isolationLevel',
    label: '隔离级别',
    type: 'select',
    options: [
      { label: '表级隔离', value: 'TABLE' },
      { label: '行级隔离', value: 'ROW' },
      { label: '列级隔离', value: 'COLUMN' },
      { label: '数据库隔离', value: 'DATABASE' }
    ]
  }
]

// 表格列配置
export const tenantTableColumns = [
  {
    prop: 'tenantId',
    label: '租户ID',
    width: '120',
    fixed: false,
    sortable: true,
    show: true
  },
  {
    prop: 'tenantName',
    label: '租户名称',
    width: '150',
    fixed: false,
    sortable: true,
    show: true,
    formatter: (row, column, cellValue) => {
      return cellValue || '-'
    }
  },
  {
    prop: 'tenantType',
    label: '租户类型',
    width: '100',
    fixed: false,
    sortable: true,
    show: true,
    formatter: (row, column, cellValue) => {
      const typeMap = {
        ENTERPRISE: '企业版',
        PROFESSIONAL: '专业版',
        BASIC: '基础版',
        TRIAL: '试用版',
        SYSTEM: '系统'
      }
      return typeMap[cellValue] || cellValue
    }
  },
  {
    prop: 'status',
    label: '状态',
    width: '100',
    fixed: false,
    sortable: true,
    show: true,
    formatter: (row, column, cellValue) => {
      const statusMap = {
        ACTIVE: '活跃',
        INACTIVE: '非活跃',
        SUSPENDED: '暂停',
        PENDING: '待审核'
      }
      return statusMap[cellValue] || cellValue
    }
  },
  {
    prop: 'companyName',
    label: '公司名称',
    width: '150',
    fixed: false,
    sortable: false,
    show: true,
    formatter: (row, column, cellValue) => {
      return cellValue || '-'
    }
  },
  {
    prop: 'contactEmail',
    label: '联系邮箱',
    width: '180',
    fixed: false,
    sortable: false,
    show: true,
    formatter: (row, column, cellValue) => {
      return cellValue || '-'
    }
  },
  {
    prop: 'stats.userCount',
    label: '用户数',
    width: '80',
    fixed: false,
    sortable: true,
    show: true,
    formatter: (row, column, cellValue) => {
      return row.stats?.userCount || 0
    }
  },
  {
    prop: 'quota.maxUsers',
    label: '最大用户数',
    width: '100',
    fixed: false,
    sortable: true,
    show: true,
    formatter: (row, column, cellValue) => {
      return row.quota?.maxUsers || 0
    }
  },
  {
    prop: 'quota.maxStorage',
    label: '存储配额',
    width: '100',
    fixed: false,
    sortable: true,
    show: true,
    formatter: (row, column, cellValue) => {
      const storage = row.quota?.maxStorage || 0
      return storage > 0 ? `${storage}GB` : '-'
    }
  },
  {
    prop: 'createdAt',
    label: '创建时间',
    width: '180',
    fixed: false,
    sortable: true,
    show: true,
    formatter: (row, column, cellValue) => {
      return cellValue ? new Date(cellValue).toLocaleString('zh-CN') : '-'
    }
  },
  {
    prop: 'actions',
    label: '操作',
    width: '280',
    fixed: 'right',
    sortable: false,
    show: true
  }
]

// 导出配置
export const tenantExportConfig = {
  fileName: 'tenants_{timestamp}.xlsx',
  sheetName: '租户列表',
  columns: [
    { prop: 'tenantId', label: '租户ID' },
    { prop: 'tenantName', label: '租户名称' },
    { prop: 'tenantType', label: '租户类型' },
    { prop: 'status', label: '状态' },
    { prop: 'companyName', label: '公司名称' },
    { prop: 'contactEmail', label: '联系邮箱' },
    { prop: 'contactPhone', label: '联系电话' },
    { prop: 'industry', label: '行业' },
    { prop: 'stats.userCount', label: '用户数' },
    { prop: 'quota.maxUsers', label: '最大用户数' },
    { prop: 'quota.maxStorage', label: '存储配额(GB)' },
    { prop: 'quota.maxApiCalls', label: 'API调用配额' },
    { prop: 'isolationLevel', label: '隔离级别' },
    { prop: 'createdAt', label: '创建时间' }
  ],
  options: {
    includeStats: true,
    includeQuota: true,
    includeFeatures: false,
    dateFormat: 'YYYY-MM-DD HH:mm:ss'
  }
}

// 租户操作日志配置
export const tenantOperationLogConfig = {
  actions: {
    CREATE: '创建租户',
    UPDATE: '更新租户',
    DELETE: '删除租户',
    TOGGLE_STATUS: '切换租户状态',
    ADD_USER: '添加用户',
    REMOVE_USER: '移除用户',
    UPDATE_USER: '更新用户',
    UPDATE_QUOTA: '更新配额',
    VIEW_DETAIL: '查看详情',
    EXPORT_DATA: '导出数据'
  },
  details: {
    CREATE: '创建了租户 {tenantName}',
    UPDATE: '更新了租户 {tenantName} 的信息',
    DELETE: '删除了租户 {tenantName}',
    TOGGLE_STATUS: '将租户 {tenantName} 状态从 {oldStatus} 切换到 {newStatus}',
    ADD_USER: '向租户 {tenantName} 添加了用户 {userName}',
    REMOVE_USER: '从租户 {tenantName} 移除了用户 {userName}',
    UPDATE_USER: '更新了租户 {tenantName} 下用户 {userName} 的信息',
    UPDATE_QUOTA: '更新了租户 {tenantName} 的配额设置',
    VIEW_DETAIL: '查看了租户 {tenantName} 的详细信息',
    EXPORT_DATA: '导出了租户数据'
  }
}

export default {
  tenantRoutes,
  tenantPermissions,
  tenantMenuConfig,
  tenantApiEndpoints,
  tenantQuickActions,
  tenantStatisticsConfig,
  tenantFilterConfig,
  tenantTableColumns,
  tenantExportConfig,
  tenantOperationLogConfig
}