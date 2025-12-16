/**
 * 价格策略管理动态路由配置
 * 对应后端: PriceStrategyEngine.java
 */

export const priceStrategyRoutes = [
  {
    path: 'price-strategy',
    name: 'PriceStrategy',
    component: () => import('@/views/price_strategy/index.vue'),
    meta: {
      title: '价格策略管理',
      icon: 'PriceTag',
      roles: ['admin', 'manager'],
      keepAlive: true,
      permission: 'price:strategy:view'
    },
    children: [
      {
        path: 'create',
        name: 'CreatePriceStrategy',
        component: () => import('@/views/price_strategy/components/PriceStrategyFormDialog.vue'),
        meta: {
          title: '创建价格策略',
          hidden: true,
          permission: 'price:strategy:create'
        }
      },
      {
        path: 'edit/:id',
        name: 'EditPriceStrategy',
        component: () => import('@/views/price_strategy/components/PriceStrategyFormDialog.vue'),
        meta: {
          title: '编辑价格策略',
          hidden: true,
          permission: 'price:strategy:update'
        }
      },
      {
        path: 'calculate',
        name: 'CalculatePrice',
        component: () => import('@/views/price_strategy/components/PriceCalculationDialog.vue'),
        meta: {
          title: '价格计算',
          hidden: true,
          permission: 'price:strategy:calculate'
        }
      },
      {
        path: 'analysis/:id',
        name: 'StrategyAnalysis',
        component: () => import('@/views/price_strategy/components/StrategyAnalysisDialog.vue'),
        meta: {
          title: '策略效果分析',
          hidden: true,
          permission: 'price:strategy:analyze'
        }
      }
    ]
  }
]

/**
 * 价格策略管理权限映射
 */
export const PRICE_STRATEGY_PERMISSIONS = {
  VIEW: 'price:strategy:view',
  CREATE: 'price:strategy:create',
  UPDATE: 'price:strategy:update',
  DELETE: 'price:strategy:delete',
  CALCULATE: 'price:strategy:calculate',
  ANALYZE: 'price:strategy:analyze',
  EXPORT: 'price:strategy:export',
  IMPORT: 'price:strategy:import'
}

/**
 * 价格策略管理菜单配置
 */
export const PRICE_STRATEGY_MENU = {
  key: 'price_strategy',
  title: '价格策略管理',
  icon: 'PriceTag',
  route: '/dashboard/price-strategy',
  description: '管理动态定价策略和价格规则引擎',
  badge: {
    content: 'new',
    type: 'primary'
  },
  children: [
    {
      key: 'price_strategy_overview',
      title: '策略概览',
      route: '/dashboard/price-strategy',
      permission: PRICE_STRATEGY_PERMISSIONS.VIEW
    },
    {
      key: 'price_strategy_calculate',
      title: '价格计算',
      route: '/dashboard/price-strategy/calculate',
      permission: PRICE_STRATEGY_PERMISSIONS.CALCULATE
    },
    {
      key: 'price_strategy_analysis',
      title: '效果分析',
      route: '/dashboard/price-strategy/analysis',
      permission: PRICE_STRATEGY_PERMISSIONS.ANALYZE
    }
  ]
}

/**
 * 价格策略相关API端点
 */
export const PRICE_STRATEGY_API_ENDPOINTS = {
  // 策略管理
  LIST: '/api/price-strategy/list',
  CREATE: '/api/price-strategy/create',
  UPDATE: '/api/price-strategy/update',
  DELETE: '/api/price-strategy/delete',
  TOGGLE_STATUS: '/api/price-strategy/toggle-status',
  CLONE: '/api/price-strategy/clone',
  
  // 价格计算
  CALCULATE: '/api/price-strategy/calculate',
  BATCH_CALCULATE: '/api/price-strategy/batch-calculate',
  
  // 统计分析
  ANALYTICS: '/api/price-strategy/analytics',
  PERFORMANCE: '/api/price-strategy/performance',
  EXPORT: '/api/price-strategy/export',
  IMPORT: '/api/price-strategy/import',
  
  // 策略类型
  STRATEGY_TYPES: '/api/price-strategy/types',
  CHANNEL_TYPES: '/api/price-strategy/channel-types',
  USER_TYPES: '/api/price-strategy/user-types'
}

/**
 * 价格策略管理快捷操作
 */
export const PRICE_STRATEGY_QUICK_ACTIONS = [
  {
    key: 'create_strategy',
    label: '创建策略',
    icon: 'Plus',
    permission: PRICE_STRATEGY_PERMISSIONS.CREATE,
    action: 'navigate',
    route: '/dashboard/price-strategy/create'
  },
  {
    key: 'calculate_price',
    label: '价格计算',
    icon: 'Calculator',
    permission: PRICE_STRATEGY_PERMISSIONS.CALCULATE,
    action: 'dialog',
    component: 'PriceCalculationDialog'
  },
  {
    key: 'export_strategies',
    label: '导出策略',
    icon: 'Download',
    permission: PRICE_STRATEGY_PERMISSIONS.EXPORT,
    action: 'export'
  },
  {
    key: 'strategy_analysis',
    label: '效果分析',
    icon: 'DataAnalysis',
    permission: PRICE_STRATEGY_PERMISSIONS.ANALYZE,
    action: 'navigate',
    route: '/dashboard/price-strategy/analysis'
  }
]

/**
 * 价格策略统计指标
 */
export const PRICE_STRATEGY_METRICS = {
  ACTIVE_STRATEGIES: {
    key: 'active_strategies',
    label: '活跃策略',
    unit: '个',
    description: '当前启用的价格策略数量',
    color: '#67C23A'
  },
  TODAY_CALCULATIONS: {
    key: 'today_calculations',
    label: '今日计算',
    unit: '次',
    description: '今日价格计算次数',
    color: '#409EFF'
  },
  AVERAGE_DISCOUNT: {
    key: 'average_discount',
    label: '平均折扣',
    unit: '%',
    description: '策略平均折扣率',
    color: '#E6A23C'
  },
  TOTAL_DISCOUNTS: {
    key: 'total_discounts',
    label: '总优惠金额',
    unit: '元',
    description: '所有策略提供的优惠总金额',
    color: '#F56C6C'
  },
  CONVERSION_RATE: {
    key: 'conversion_rate',
    label: '转化率',
    unit: '%',
    description: '策略应用后的订单转化率',
    color: '#909399'
  },
  ROI: {
    key: 'roi',
    label: '投资回报率',
    unit: '%',
    description: '策略实施的投资回报率',
    color: '#67C23A'
  }
}

export default {
  priceStrategyRoutes,
  PRICE_STRATEGY_PERMISSIONS,
  PRICE_STRATEGY_MENU,
  PRICE_STRATEGY_API_ENDPOINTS,
  PRICE_STRATEGY_QUICK_ACTIONS,
  PRICE_STRATEGY_METRICS
}