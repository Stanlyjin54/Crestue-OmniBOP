/**
 * 多租户管理和AI助手管理动态路由配置
 * 支持基于权限的路由加载和菜单生成
 */

// 多租户管理路由配置
export const tenantManagementRoutes = [
  {
    path: '/admin/tenant',
    name: 'TenantManagement',
    component: () => import('@/views/tenant/management.vue'),
    meta: {
      title: '多租户管理',
      icon: 'OfficeBuilding',
      permissions: ['tenant:view'],
      breadcrumb: ['系统管理', '多租户管理']
    },
    children: []
  }
]

// AI助手管理路由配置
export const aiAssistantManagementRoutes = [
  {
    path: '/admin/ai-assistant',
    name: 'AiAssistantManagement',
    component: () => import('@/views/ai_assistant/management.vue'),
    meta: {
      title: 'AI助手管理',
      icon: 'ChatDotRound',
      permissions: ['ai_assistant:view'],
      breadcrumb: ['系统管理', 'AI助手管理']
    },
    children: []
  }
]

// 合并所有管理路由
export const adminManagementRoutes = [
  ...tenantManagementRoutes,
  ...aiAssistantManagementRoutes
]

// 权限映射配置
export const permissionMapping = {
  // 多租户管理权限
  'tenant:view': {
    title: '查看租户',
    description: '查看租户列表和基本信息'
  },
  'tenant:create': {
    title: '创建租户',
    description: '创建新的租户'
  },
  'tenant:update': {
    title: '编辑租户',
    description: '编辑租户信息和配置'
  },
  'tenant:delete': {
    title: '删除租户',
    description: '删除租户及其数据'
  },
  'tenant:users': {
    title: '管理租户用户',
    description: '管理租户下的用户'
  },
  'tenant:resources': {
    title: '查看资源监控',
    description: '查看租户资源使用情况'
  },
  'tenant:analyze': {
    title: 'AI分析',
    description: '对租户数据进行AI分析'
  },
  'tenant:export': {
    title: '导出数据',
    description: '导出租户相关数据'
  },

  // AI助手管理权限
  'ai_assistant:view': {
    title: '查看AI助手',
    description: '查看AI助手日志和统计'
  },
  'ai_assistant:create': {
    title: '配置AI助手',
    description: '配置AI助手参数'
  },
  'ai_assistant:update': {
    title: '更新AI助手',
    description: '更新AI助手配置'
  },
  'ai_assistant:delete': {
    title: '删除AI助手',
    description: '删除AI助手日志'
  },
  'ai_assistant:export': {
    title: '导出AI助手数据',
    description: '导出AI助手相关数据'
  },
  'ai_assistant:analyze': {
    title: 'AI助手分析',
    description: '分析AI助手性能数据'
  }
}

// 菜单配置
export const menuConfig = [
  {
    id: 'system-management',
    title: '系统管理',
    icon: 'Setting',
    children: [
      {
        id: 'tenant-management',
        title: '多租户管理',
        icon: 'OfficeBuilding',
        route: '/admin/tenant',
        permissions: ['tenant:view'],
        badge: {
          text: 'NEW',
          type: 'success'
        }
      },
      {
        id: 'ai-assistant-management',
        title: 'AI助手管理',
        icon: 'ChatDotRound',
        route: '/admin/ai-assistant',
        permissions: ['ai_assistant:view']
      }
    ]
  }
]

// API端点配置
export const apiEndpoints = {
  // 多租户管理API
  tenant: {
    list: '/api/admin/tenant/list',
    create: '/api/admin/tenant/create',
    update: '/api/admin/tenant/update',
    delete: '/api/admin/tenant/delete',
    detail: '/api/admin/tenant/detail',
    users: '/api/admin/tenant/users',
    resources: '/api/admin/tenant/resources',
    status: '/api/admin/tenant/status',
    export: '/api/admin/tenant/export',
    analyze: '/api/admin/tenant/analyze'
  },

  // AI助手管理API
  aiAssistant: {
    logs: '/api/admin/ai-assistant/logs',
    statistics: '/api/admin/ai-assistant/statistics',
    performance: '/api/admin/ai-assistant/performance',
    config: '/api/admin/ai-assistant/config',
    export: '/api/admin/ai-assistant/export',
    analyze: '/api/admin/ai-assistant/analyze'
  }
}

// 快捷操作配置
export const quickActions = {
  tenant: [
    {
      key: 'create',
      label: '创建租户',
      icon: 'Plus',
      permission: 'tenant:create',
      action: 'navigate',
      target: '/admin/tenant/create'
    },
    {
      key: 'bulk-import',
      label: '批量导入',
      icon: 'Upload',
      permission: 'tenant:create',
      action: 'modal',
      modal: 'TenantImportDialog'
    },
    {
      key: 'export-all',
      label: '导出全部',
      icon: 'Download',
      permission: 'tenant:export',
      action: 'api',
      endpoint: 'tenant:export'
    }
  ],
  aiAssistant: [
    {
      key: 'clear-logs',
      label: '清理日志',
      icon: 'Delete',
      permission: 'ai_assistant:delete',
      action: 'api',
      endpoint: 'ai_assistant:logs:clear'
    },
    {
      key: 'export-logs',
      label: '导出日志',
      icon: 'Download',
      permission: 'ai_assistant:export',
      action: 'api',
      endpoint: 'ai_assistant:export'
    }
  ]
}

// 统计指标配置
export const statisticsConfig = {
  tenant: {
    totalTenants: {
      label: '总租户数',
      type: 'number',
      format: 'integer',
      color: '#409EFF'
    },
    activeTenants: {
      label: '活跃租户',
      type: 'number',
      format: 'integer',
      color: '#67C23A'
    },
    totalUsers: {
      label: '租户用户总数',
      type: 'number',
      format: 'integer',
      color: '#E6A23C'
    },
    resourceUtilization: {
      label: '资源利用率',
      type: 'percentage',
      format: 'percentage',
      color: '#F56C6C'
    }
  },
  aiAssistant: {
    totalRequests: {
      label: '总请求数',
      type: 'number',
      format: 'integer',
      color: '#409EFF'
    },
    avgResponseTime: {
      label: '平均响应时间',
      type: 'number',
      format: 'milliseconds',
      color: '#E6A23C'
    },
    successRate: {
      label: '成功率',
      type: 'percentage',
      format: 'percentage',
      color: '#67C23A'
    },
    activeUsers: {
      label: '活跃用户',
      type: 'number',
      format: 'integer',
      color: '#F56C6C'
    }
  }
}

// 筛选条件配置
export const filterConfig = {
  tenant: [
    {
      key: 'tenantName',
      label: '租户名称',
      type: 'input',
      placeholder: '请输入租户名称',
      defaultValue: ''
    },
    {
      key: 'tenantType',
      label: '租户类型',
      type: 'select',
      placeholder: '请选择租户类型',
      options: [
        { label: '个人版', value: 'personal' },
        { label: '企业版', value: 'enterprise' },
        { label: '专业版', value: 'professional' },
        { label: '定制版', value: 'custom' }
      ],
      defaultValue: ''
    },
    {
      key: 'status',
      label: '状态',
      type: 'select',
      placeholder: '请选择状态',
      options: [
        { label: '活跃', value: 'ACTIVE' },
        { label: '暂停', value: 'SUSPENDED' },
        { label: '已过期', value: 'EXPIRED' }
      ],
      defaultValue: ''
    },
    {
      key: 'contactEmail',
      label: '联系人邮箱',
      type: 'input',
      placeholder: '请输入联系人邮箱',
      defaultValue: ''
    }
  ],
  aiAssistant: [
    {
      key: 'dateRange',
      label: '时间范围',
      type: 'daterange',
      placeholder: '请选择时间范围',
      defaultValue: []
    },
    {
      key: 'functionType',
      label: '功能类型',
      type: 'select',
      placeholder: '请选择功能类型',
      options: [
        { label: '智能采购建议', value: 'procurement' },
        { label: '智能分析', value: 'analysis' },
        { label: '社媒营销', value: 'social_marketing' },
        { label: '支付优化', value: 'payment' }
      ],
      defaultValue: ''
    },
    {
      key: 'status',
      label: '状态',
      type: 'select',
      placeholder: '请选择状态',
      options: [
        { label: '成功', value: 'success' },
        { label: '失败', value: 'failed' },
        { label: '进行中', value: 'processing' }
      ],
      defaultValue: ''
    }
  ]
}

// 表格列配置
export const tableColumns = {
  tenant: [
    {
      prop: 'tenantName',
      label: '租户名称',
      width: 200,
      sortable: true,
      show: true
    },
    {
      prop: 'tenantType',
      label: '租户类型',
      width: 120,
      sortable: true,
      show: true,
      render: (value) => {
        const typeMap = {
          personal: { label: '个人版', color: '#409EFF' },
          enterprise: { label: '企业版', color: '#67C23A' },
          professional: { label: '专业版', color: '#E6A23C' },
          custom: { label: '定制版', color: '#F56C6C' }
        }
        return typeMap[value]?.label || value
      }
    },
    {
      prop: 'status',
      label: '状态',
      width: 100,
      sortable: true,
      show: true,
      render: (value) => {
        const statusMap = {
          ACTIVE: { label: '活跃', type: 'success' },
          SUSPENDED: { label: '暂停', type: 'warning' },
          EXPIRED: { label: '已过期', type: 'danger' }
        }
        return statusMap[value]?.label || value
      }
    },
    {
      prop: 'userCount',
      label: '用户数',
      width: 100,
      sortable: true,
      show: true
    },
    {
      prop: 'storageUsage',
      label: '存储使用',
      width: 150,
      sortable: false,
      show: true,
      render: (value) => {
        return `${value.used}GB / ${value.total}GB`
      }
    },
    {
      prop: 'expireTime',
      label: '过期时间',
      width: 160,
      sortable: true,
      show: true
    },
    {
      prop: 'createTime',
      label: '创建时间',
      width: 160,
      sortable: true,
      show: true
    }
  ],
  aiAssistant: [
    {
      prop: 'requestId',
      label: '请求ID',
      width: 120,
      sortable: true,
      show: true
    },
    {
      prop: 'functionType',
      label: '功能类型',
      width: 120,
      sortable: true,
      show: true,
      render: (value) => {
        const typeMap = {
          procurement: '智能采购建议',
          analysis: '智能分析',
          social_marketing: '社媒营销',
          payment: '支付优化'
        }
        return typeMap[value] || value
      }
    },
    {
      prop: 'status',
      label: '状态',
      width: 100,
      sortable: true,
      show: true,
      render: (value) => {
        const statusMap = {
          success: { label: '成功', type: 'success' },
          failed: { label: '失败', type: 'danger' },
          processing: { label: '进行中', type: 'warning' }
        }
        return statusMap[value]?.label || value
      }
    },
    {
      prop: 'responseTime',
      label: '响应时间',
      width: 120,
      sortable: true,
      show: true,
      render: (value) => `${value}ms`
    },
    {
      prop: 'userId',
      label: '用户ID',
      width: 120,
      sortable: true,
      show: true
    },
    {
      prop: 'createTime',
      label: '创建时间',
      width: 160,
      sortable: true,
      show: true
    }
  ]
}

// 导出配置
export const exportConfig = {
  tenant: {
    filename: '租户管理数据',
    fields: [
      { key: 'tenantName', label: '租户名称' },
      { key: 'tenantType', label: '租户类型' },
      { key: 'status', label: '状态' },
      { key: 'userCount', label: '用户数' },
      { key: 'storageUsage', label: '存储使用' },
      { key: 'expireTime', label: '过期时间' },
      { key: 'createTime', label: '创建时间' }
    ]
  },
  aiAssistant: {
    filename: 'AI助手日志数据',
    fields: [
      { key: 'requestId', label: '请求ID' },
      { key: 'functionType', label: '功能类型' },
      { key: 'status', label: '状态' },
      { key: 'responseTime', label: '响应时间' },
      { key: 'userId', label: '用户ID' },
      { key: 'createTime', label: '创建时间' }
    ]
  }
}

// 操作日志配置
export const operationLogs = {
  tenant: [
    {
      key: 'create',
      label: '创建租户',
      icon: 'Plus',
      permission: 'tenant:create'
    },
    {
      key: 'update',
      label: '编辑租户',
      icon: 'Edit',
      permission: 'tenant:update'
    },
    {
      key: 'delete',
      label: '删除租户',
      icon: 'Delete',
      permission: 'tenant:delete'
    },
    {
      key: 'toggle-status',
      label: '切换状态',
      icon: 'Switch',
      permission: 'tenant:update'
    },
    {
      key: 'manage-users',
      label: '管理用户',
      icon: 'User',
      permission: 'tenant:users'
    }
  ],
  aiAssistant: [
    {
      key: 'view-log',
      label: '查看日志',
      icon: 'View',
      permission: 'ai_assistant:view'
    },
    {
      key: 'delete-log',
      label: '删除日志',
      icon: 'Delete',
      permission: 'ai_assistant:delete'
    },
    {
      key: 'export-data',
      label: '导出数据',
      icon: 'Download',
      permission: 'ai_assistant:export'
    }
  ]
}

export default {
  tenantManagementRoutes,
  aiAssistantManagementRoutes,
  adminManagementRoutes,
  permissionMapping,
  menuConfig,
  apiEndpoints,
  quickActions,
  statisticsConfig,
  filterConfig,
  tableColumns,
  exportConfig,
  operationLogs
}