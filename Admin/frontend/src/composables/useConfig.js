import { ref, reactive, computed } from 'vue'
import { ElMessage } from 'element-plus'

// 配置类型枚举
export const CONFIG_TYPES = {
  BUSINESS: 'business', // 业务参数
  SYSTEM: 'system', // 系统参数
  FINANCE: 'finance', // 财务参数
  INVENTORY: 'inventory', // 库存参数
  CUSTOMER: 'customer', // 客户参数
  ORDER: 'order', // 订单参数
  PAYMENT: 'payment', // 支付参数
  SHIPPING: 'shipping', // 物流参数
  NOTIFICATION: 'notification', // 通知参数
  SECURITY: 'security' // 安全参数
}

// 配置项类型枚举
export const CONFIG_ITEM_TYPES = {
  STRING: 'string',
  NUMBER: 'number',
  BOOLEAN: 'boolean',
  JSON: 'json',
  ARRAY: 'array',
  DATE: 'date',
  TIME: 'time',
  DATETIME: 'datetime',
  EMAIL: 'email',
  URL: 'url',
  PASSWORD: 'password',
  SELECT: 'select',
  MULTI_SELECT: 'multi_select',
  RADIO: 'radio',
  CHECKBOX: 'checkbox',
  TEXTAREA: 'textarea',
  RICH_TEXT: 'rich_text',
  FILE: 'file',
  IMAGE: 'image',
  COLOR: 'color',
  RANGE: 'range',
  SLIDER: 'slider'
}

// 配置状态枚举
export const CONFIG_STATUS = {
  ENABLED: 'enabled',
  DISABLED: 'disabled',
  MAINTENANCE: 'maintenance',
  DEPRECATED: 'deprecated'
}

// 业务参数配置项
export const BUSINESS_CONFIGS = [
  {
    key: 'company_name',
    name: '公司名称',
    type: CONFIG_ITEM_TYPES.STRING,
    category: CONFIG_TYPES.BUSINESS,
    required: true,
    defaultValue: '企业管理系统',
    description: '公司名称，将显示在系统各处'
  },
  {
    key: 'company_logo',
    name: '公司Logo',
    type: CONFIG_ITEM_TYPES.IMAGE,
    category: CONFIG_TYPES.BUSINESS,
    required: false,
    defaultValue: '',
    description: '公司Logo图片'
  },
  {
    key: 'company_favicon',
    name: '网站图标',
    type: CONFIG_ITEM_TYPES.IMAGE,
    category: CONFIG_TYPES.BUSINESS,
    required: false,
    defaultValue: '',
    description: '浏览器标签页图标'
  },
  {
    key: 'system_title',
    name: '系统标题',
    type: CONFIG_ITEM_TYPES.STRING,
    category: CONFIG_TYPES.BUSINESS,
    required: true,
    defaultValue: '企业管理系统',
    description: '系统标题，显示在浏览器标签页'
  },
  {
    key: 'business_hours',
    name: '营业时间',
    type: CONFIG_ITEM_TYPES.JSON,
    category: CONFIG_TYPES.BUSINESS,
    required: false,
    defaultValue: JSON.stringify([
      { day: 'monday', open: '09:00', close: '18:00' },
      { day: 'tuesday', open: '09:00', close: '18:00' },
      { day: 'wednesday', open: '09:00', close: '18:00' },
      { day: 'thursday', open: '09:00', close: '18:00' },
      { day: 'friday', open: '09:00', close: '18:00' },
      { day: 'saturday', open: '10:00', close: '16:00' },
      { day: 'sunday', open: 'closed', close: 'closed' }
    ]),
    description: '营业时间配置'
  },
  {
    key: 'timezone',
    name: '时区',
    type: CONFIG_ITEM_TYPES.SELECT,
    category: CONFIG_TYPES.BUSINESS,
    required: true,
    defaultValue: 'Asia/Shanghai',
    options: [
      { label: '北京时间 (UTC+8)', value: 'Asia/Shanghai' },
      { label: '东京时间 (UTC+9)', value: 'Asia/Tokyo' },
      { label: '首尔时间 (UTC+9)', value: 'Asia/Seoul' },
      { label: '新加坡时间 (UTC+8)', value: 'Asia/Singapore' },
      { label: '香港时间 (UTC+8)', value: 'Asia/Hong_Kong' },
      { label: '纽约时间 (UTC-5)', value: 'America/New_York' },
      { label: '洛杉矶时间 (UTC-8)', value: 'America/Los_Angeles' },
      { label: '伦敦时间 (UTC+0)', value: 'Europe/London' },
      { label: '巴黎时间 (UTC+1)', value: 'Europe/Paris' },
      { label: '悉尼时间 (UTC+10)', value: 'Australia/Sydney' }
    ],
    description: '系统时区设置'
  },
  {
    key: 'currency_code',
    name: '货币代码',
    type: CONFIG_ITEM_TYPES.SELECT,
    category: CONFIG_TYPES.BUSINESS,
    required: true,
    defaultValue: 'CNY',
    options: [
      { label: '人民币 (CNY)', value: 'CNY' },
      { label: '美元 (USD)', value: 'USD' },
      { label: '欧元 (EUR)', value: 'EUR' },
      { label: '英镑 (GBP)', value: 'GBP' },
      { label: '日元 (JPY)', value: 'JPY' },
      { label: '韩元 (KRW)', value: 'KRW' },
      { label: '港币 (HKD)', value: 'HKD' },
      { label: '台币 (TWD)', value: 'TWD' },
      { label: '新加坡元 (SGD)', value: 'SGD' },
      { label: '澳元 (AUD)', value: 'AUD' }
    ],
    description: '货币代码设置'
  },
  {
    key: 'currency_symbol',
    name: '货币符号',
    type: CONFIG_ITEM_TYPES.STRING,
    category: CONFIG_TYPES.BUSINESS,
    required: true,
    defaultValue: '¥',
    description: '货币符号显示'
  },
  {
    key: 'decimal_places',
    name: '小数位数',
    type: CONFIG_ITEM_TYPES.NUMBER,
    category: CONFIG_TYPES.BUSINESS,
    required: true,
    defaultValue: 2,
    min: 0,
    max: 6,
    description: '金额小数位数'
  },
  {
    key: 'thousands_separator',
    name: '千位分隔符',
    type: CONFIG_ITEM_TYPES.SELECT,
    category: CONFIG_TYPES.BUSINESS,
    required: true,
    defaultValue: ',',
    options: [
      { label: '逗号 (,)', value: ',' },
      { label: '空格 ( )', value: ' ' },
      { label: '点号 (.)', value: '.' },
      { label: '无', value: '' }
    ],
    description: '数字千位分隔符'
  },
  {
    key: 'date_format',
    name: '日期格式',
    type: CONFIG_ITEM_TYPES.SELECT,
    category: CONFIG_TYPES.BUSINESS,
    required: true,
    defaultValue: 'YYYY-MM-DD',
    options: [
      { label: 'YYYY-MM-DD (2024-01-01)', value: 'YYYY-MM-DD' },
      { label: 'DD/MM/YYYY (01/01/2024)', value: 'DD/MM/YYYY' },
      { label: 'MM/DD/YYYY (01/01/2024)', value: 'MM/DD/YYYY' },
      { label: 'YYYY年MM月DD日 (2024年01月01日)', value: 'YYYY年MM月DD日' },
      { label: 'YYYY/MM/DD (2024/01/01)', value: 'YYYY/MM/DD' }
    ],
    description: '日期显示格式'
  },
  {
    key: 'time_format',
    name: '时间格式',
    type: CONFIG_ITEM_TYPES.SELECT,
    category: CONFIG_TYPES.BUSINESS,
    required: true,
    defaultValue: '24',
    options: [
      { label: '24小时制 (14:30)', value: '24' },
      { label: '12小时制 (2:30 PM)', value: '12' }
    ],
    description: '时间显示格式'
  },
  {
    key: 'language',
    name: '系统语言',
    type: CONFIG_ITEM_TYPES.SELECT,
    category: CONFIG_TYPES.BUSINESS,
    required: true,
    defaultValue: 'zh-CN',
    options: [
      { label: '简体中文', value: 'zh-CN' },
      { label: '繁體中文', value: 'zh-TW' },
      { label: 'English', value: 'en-US' },
      { label: '日本語', value: 'ja-JP' },
      { label: '한국어', value: 'ko-KR' }
    ],
    description: '系统语言设置'
  },
  {
    key: 'country_code',
    name: '国家代码',
    type: CONFIG_ITEM_TYPES.SELECT,
    category: CONFIG_TYPES.BUSINESS,
    required: true,
    defaultValue: 'CN',
    options: [
      { label: '中国 (+86)', value: 'CN' },
      { label: '美国 (+1)', value: 'US' },
      { label: '日本 (+81)', value: 'JP' },
      { label: '韩国 (+82)', value: 'KR' },
      { label: '英国 (+44)', value: 'GB' },
      { label: '法国 (+33)', value: 'FR' },
      { label: '德国 (+49)', value: 'DE' },
      { label: '澳大利亚 (+61)', value: 'AU' },
      { label: '加拿大 (+1)', value: 'CA' },
      { label: '新加坡 (+65)', value: 'SG' }
    ],
    description: '国家代码设置'
  },
  {
    key: 'tax_rate',
    name: '税率 (%)',
    type: CONFIG_ITEM_TYPES.NUMBER,
    category: CONFIG_TYPES.BUSINESS,
    required: true,
    defaultValue: 13,
    min: 0,
    max: 100,
    description: '默认税率百分比'
  },
  {
    key: 'tax_included',
    name: '价格含税',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.BUSINESS,
    required: true,
    defaultValue: true,
    description: '价格是否包含税费'
  },
  {
    key: 'auto_invoice',
    name: '自动生成发票',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.BUSINESS,
    required: true,
    defaultValue: true,
    description: '订单完成后自动生成发票'
  },
  {
    key: 'invoice_prefix',
    name: '发票前缀',
    type: CONFIG_ITEM_TYPES.STRING,
    category: CONFIG_TYPES.BUSINESS,
    required: false,
    defaultValue: 'INV',
    description: '发票编号前缀'
  },
  {
    key: 'order_prefix',
    name: '订单前缀',
    type: CONFIG_ITEM_TYPES.STRING,
    category: CONFIG_TYPES.BUSINESS,
    required: false,
    defaultValue: 'ORD',
    description: '订单编号前缀'
  },
  {
    key: 'customer_prefix',
    name: '客户编号前缀',
    type: CONFIG_ITEM_TYPES.STRING,
    category: CONFIG_TYPES.BUSINESS,
    required: false,
    defaultValue: 'CUST',
    description: '客户编号前缀'
  },
  {
    key: 'product_prefix',
    name: '商品编号前缀',
    type: CONFIG_ITEM_TYPES.STRING,
    category: CONFIG_TYPES.BUSINESS,
    required: false,
    defaultValue: 'PROD',
    description: '商品编号前缀'
  },
  {
    key: 'enable_reviews',
    name: '启用评价系统',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.BUSINESS,
    required: true,
    defaultValue: true,
    description: '是否启用商品评价功能'
  },
  {
    key: 'review_auto_approve',
    name: '评价自动审核',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.BUSINESS,
    required: true,
    defaultValue: false,
    description: '评价是否自动审核通过'
  },
  {
    key: 'enable_wishlist',
    name: '启用收藏夹',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.BUSINESS,
    required: true,
    defaultValue: true,
    description: '是否启用商品收藏功能'
  },
  {
    key: 'enable_comparison',
    name: '启用商品对比',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.BUSINESS,
    required: true,
    defaultValue: true,
    description: '是否启用商品对比功能'
  },
  {
    key: 'enable_recommendations',
    name: '启用推荐系统',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.BUSINESS,
    required: true,
    defaultValue: true,
    description: '是否启用商品推荐功能'
  },
  {
    key: 'recommendation_algorithm',
    name: '推荐算法',
    type: CONFIG_ITEM_TYPES.SELECT,
    category: CONFIG_TYPES.BUSINESS,
    required: true,
    defaultValue: 'collaborative',
    options: [
      { label: '协同过滤', value: 'collaborative' },
      { label: '内容推荐', value: 'content' },
      { label: '混合推荐', value: 'hybrid' },
      { label: '热门推荐', value: 'popular' }
    ],
    description: '商品推荐算法类型'
  },
  {
    key: 'enable_social_login',
    name: '启用社交登录',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.BUSINESS,
    required: true,
    defaultValue: true,
    description: '是否启用第三方社交登录'
  },
  {
    key: 'enable_multi_language',
    name: '启用多语言',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.BUSINESS,
    required: true,
    defaultValue: true,
    description: '是否启用多语言支持'
  },
  {
    key: 'enable_multi_currency',
    name: '启用多货币',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.BUSINESS,
    required: true,
    defaultValue: false,
    description: '是否启用多货币支持'
  },
  {
    key: 'enable_geo_location',
    name: '启用地理位置',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.BUSINESS,
    required: true,
    defaultValue: true,
    description: '是否启用地理位置服务'
  },
  {
    key: 'enable_analytics',
    name: '启用数据分析',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.BUSINESS,
    required: true,
    defaultValue: true,
    description: '是否启用数据分析功能'
  },
  {
    key: 'analytics_provider',
    name: '分析服务提供商',
    type: CONFIG_ITEM_TYPES.SELECT,
    category: CONFIG_TYPES.BUSINESS,
    required: true,
    defaultValue: 'google',
    options: [
      { label: 'Google Analytics', value: 'google' },
      { label: '百度统计', value: 'baidu' },
      { label: '友盟', value: 'umeng' },
      { label: '腾讯分析', value: 'tencent' },
      { label: '自定义', value: 'custom' }
    ],
    description: '数据分析服务提供商'
  },
  {
    key: 'analytics_tracking_id',
    name: '分析跟踪ID',
    type: CONFIG_ITEM_TYPES.STRING,
    category: CONFIG_TYPES.BUSINESS,
    required: false,
    defaultValue: '',
    description: '数据分析跟踪ID'
  },
  {
    key: 'enable_affiliate',
    name: '启用联盟营销',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.BUSINESS,
    required: true,
    defaultValue: false,
    description: '是否启用联盟营销功能'
  },
  {
    key: 'affiliate_commission_rate',
    name: '联盟佣金比例 (%)',
    type: CONFIG_ITEM_TYPES.NUMBER,
    category: CONFIG_TYPES.BUSINESS,
    required: false,
    defaultValue: 5,
    min: 0,
    max: 50,
    description: '联盟营销佣金比例'
  },
  {
    key: 'enable_subscription',
    name: '启用订阅服务',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.BUSINESS,
    required: true,
    defaultValue: false,
    description: '是否启用订阅服务功能'
  },
  {
    key: 'subscription_billing_cycle',
    name: '订阅计费周期',
    type: CONFIG_ITEM_TYPES.SELECT,
    category: CONFIG_TYPES.BUSINESS,
    required: false,
    defaultValue: 'monthly',
    options: [
      { label: '每日', value: 'daily' },
      { label: '每周', value: 'weekly' },
      { label: '每月', value: 'monthly' },
      { label: '每季度', value: 'quarterly' },
      { label: '每年', value: 'yearly' }
    ],
    description: '订阅服务计费周期'
  }
]

// 系统参数配置项
export const SYSTEM_CONFIGS = [
  {
    key: 'session_timeout',
    name: '会话超时时间 (分钟)',
    type: CONFIG_ITEM_TYPES.NUMBER,
    category: CONFIG_TYPES.SYSTEM,
    required: true,
    defaultValue: 30,
    min: 5,
    max: 1440,
    description: '用户会话超时时间'
  },
  {
    key: 'password_min_length',
    name: '密码最小长度',
    type: CONFIG_ITEM_TYPES.NUMBER,
    category: CONFIG_TYPES.SYSTEM,
    required: true,
    defaultValue: 8,
    min: 4,
    max: 32,
    description: '密码最小长度要求'
  },
  {
    key: 'password_require_uppercase',
    name: '密码需要大写字母',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.SYSTEM,
    required: true,
    defaultValue: true,
    description: '密码是否需要包含大写字母'
  },
  {
    key: 'password_require_lowercase',
    name: '密码需要小写字母',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.SYSTEM,
    required: true,
    defaultValue: true,
    description: '密码是否需要包含小写字母'
  },
  {
    key: 'password_require_number',
    name: '密码需要数字',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.SYSTEM,
    required: true,
    defaultValue: true,
    description: '密码是否需要包含数字'
  },
  {
    key: 'password_require_special',
    name: '密码需要特殊字符',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.SYSTEM,
    required: true,
    defaultValue: true,
    description: '密码是否需要包含特殊字符'
  },
  {
    key: 'max_login_attempts',
    name: '最大登录尝试次数',
    type: CONFIG_ITEM_TYPES.NUMBER,
    category: CONFIG_TYPES.SYSTEM,
    required: true,
    defaultValue: 5,
    min: 1,
    max: 10,
    description: '最大登录尝试次数'
  },
  {
    key: 'lockout_duration',
    name: '账户锁定时间 (分钟)',
    type: CONFIG_ITEM_TYPES.NUMBER,
    category: CONFIG_TYPES.SYSTEM,
    required: true,
    defaultValue: 15,
    min: 1,
    max: 60,
    description: '账户锁定时间'
  },
  {
    key: 'enable_two_factor',
    name: '启用双因子认证',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.SYSTEM,
    required: true,
    defaultValue: false,
    description: '是否启用双因子认证'
  },
  {
    key: 'enable_captcha',
    name: '启用验证码',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.SYSTEM,
    required: true,
    defaultValue: true,
    description: '是否启用验证码'
  },
  {
    key: 'enable_audit_log',
    name: '启用审计日志',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.SYSTEM,
    required: true,
    defaultValue: true,
    description: '是否启用审计日志'
  },
  {
    key: 'log_retention_days',
    name: '日志保留天数',
    type: CONFIG_ITEM_TYPES.NUMBER,
    category: CONFIG_TYPES.SYSTEM,
    required: true,
    defaultValue: 90,
    min: 7,
    max: 365,
    description: '系统日志保留天数'
  },
  {
    key: 'backup_frequency',
    name: '备份频率',
    type: CONFIG_ITEM_TYPES.SELECT,
    category: CONFIG_TYPES.SYSTEM,
    required: true,
    defaultValue: 'daily',
    options: [
      { label: '每小时', value: 'hourly' },
      { label: '每日', value: 'daily' },
      { label: '每周', value: 'weekly' },
      { label: '每月', value: 'monthly' }
    ],
    description: '数据备份频率'
  },
  {
    key: 'backup_retention_days',
    name: '备份保留天数',
    type: CONFIG_ITEM_TYPES.NUMBER,
    category: CONFIG_TYPES.SYSTEM,
    required: true,
    defaultValue: 30,
    min: 1,
    max: 365,
    description: '备份文件保留天数'
  },
  {
    key: 'enable_maintenance_mode',
    name: '启用维护模式',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.SYSTEM,
    required: true,
    defaultValue: false,
    description: '是否启用系统维护模式'
  },
  {
    key: 'maintenance_message',
    name: '维护模式消息',
    type: CONFIG_ITEM_TYPES.TEXTAREA,
    category: CONFIG_TYPES.SYSTEM,
    required: false,
    defaultValue: '系统正在维护中，请稍后再试。',
    description: '维护模式显示的消息'
  },
  {
    key: 'api_rate_limit',
    name: 'API速率限制',
    type: CONFIG_ITEM_TYPES.NUMBER,
    category: CONFIG_TYPES.SYSTEM,
    required: true,
    defaultValue: 100,
    min: 10,
    max: 1000,
    description: 'API请求速率限制（每分钟）'
  },
  {
    key: 'enable_api_logging',
    name: '启用API日志',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.SYSTEM,
    required: true,
    defaultValue: true,
    description: '是否启用API请求日志'
  },
  {
    key: 'enable_error_reporting',
    name: '启用错误报告',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.SYSTEM,
    required: true,
    defaultValue: true,
    description: '是否启用错误报告'
  },
  {
    key: 'error_report_email',
    name: '错误报告邮箱',
    type: CONFIG_ITEM_TYPES.EMAIL,
    category: CONFIG_TYPES.SYSTEM,
    required: false,
    defaultValue: '',
    description: '接收错误报告的邮箱地址'
  },
  {
    key: 'enable_performance_monitoring',
    name: '启用性能监控',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.SYSTEM,
    required: true,
    defaultValue: true,
    description: '是否启用系统性能监控'
  },
  {
    key: 'performance_threshold',
    name: '性能阈值 (毫秒)',
    type: CONFIG_ITEM_TYPES.NUMBER,
    category: CONFIG_TYPES.SYSTEM,
    required: true,
    defaultValue: 1000,
    min: 100,
    max: 10000,
    description: '性能监控阈值'
  }
]

// 财务参数配置项
export const FINANCE_CONFIGS = [
  {
    key: 'enable_online_payment',
    name: '启用在线支付',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.FINANCE,
    required: true,
    defaultValue: true,
    description: '是否启用在线支付功能'
  },
  {
    key: 'payment_gateway',
    name: '支付网关',
    type: CONFIG_ITEM_TYPES.SELECT,
    category: CONFIG_TYPES.FINANCE,
    required: true,
    defaultValue: 'alipay',
    options: [
      { label: '支付宝', value: 'alipay' },
      { label: '微信支付', value: 'wechat' },
      { label: '银联', value: 'unionpay' },
      { label: 'PayPal', value: 'paypal' },
      { label: 'Stripe', value: 'stripe' },
      { label: 'Square', value: 'square' }
    ],
    description: '支付服务提供商'
  },
  {
    key: 'payment_timeout',
    name: '支付超时时间 (分钟)',
    type: CONFIG_ITEM_TYPES.NUMBER,
    category: CONFIG_TYPES.FINANCE,
    required: true,
    defaultValue: 30,
    min: 5,
    max: 120,
    description: '支付超时时间'
  },
  {
    key: 'enable_refund',
    name: '启用退款',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.FINANCE,
    required: true,
    defaultValue: true,
    description: '是否启用退款功能'
  },
  {
    key: 'max_refund_days',
    name: '最大退款天数',
    type: CONFIG_ITEM_TYPES.NUMBER,
    category: CONFIG_TYPES.FINANCE,
    required: true,
    defaultValue: 7,
    min: 1,
    max: 365,
    description: '订单完成后最大退款天数'
  },
  {
    key: 'enable_partial_refund',
    name: '启用部分退款',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.FINANCE,
    required: true,
    defaultValue: true,
    description: '是否启用部分退款功能'
  },
  {
    key: 'enable_invoice',
    name: '启用发票',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.FINANCE,
    required: true,
    defaultValue: true,
    description: '是否启用发票功能'
  },
  {
    key: 'invoice_template',
    name: '发票模板',
    type: CONFIG_ITEM_TYPES.SELECT,
    category: CONFIG_TYPES.FINANCE,
    required: true,
    defaultValue: 'standard',
    options: [
      { label: '标准模板', value: 'standard' },
      { label: '商务模板', value: 'business' },
      { label: '简约模板', value: 'simple' },
      { label: '专业模板', value: 'professional' }
    ],
    description: '发票模板样式'
  },
  {
    key: 'enable_budgeting',
    name: '启用预算管理',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.FINANCE,
    required: true,
    defaultValue: false,
    description: '是否启用预算管理功能'
  },
  {
    key: 'budget_alert_threshold',
    name: '预算警告阈值 (%)',
    type: CONFIG_ITEM_TYPES.NUMBER,
    category: CONFIG_TYPES.FINANCE,
    required: false,
    defaultValue: 80,
    min: 50,
    max: 100,
    description: '预算使用警告阈值'
  },
  {
    key: 'enable_cost_tracking',
    name: '启用成本跟踪',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.FINANCE,
    required: true,
    defaultValue: true,
    description: '是否启用成本跟踪功能'
  },
  {
    key: 'enable_profit_analysis',
    name: '启用利润分析',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.FINANCE,
    required: true,
    defaultValue: true,
    description: '是否启用利润分析功能'
  },
  {
    key: 'enable_cash_flow',
    name: '启用现金流管理',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.FINANCE,
    required: true,
    defaultValue: true,
    description: '是否启用现金流管理功能'
  },
  {
    key: 'cash_flow_forecast_days',
    name: '现金流预测天数',
    type: CONFIG_ITEM_TYPES.NUMBER,
    category: CONFIG_TYPES.FINANCE,
    required: false,
    defaultValue: 30,
    min: 7,
    max: 365,
    description: '现金流预测天数'
  },
  {
    key: 'enable_financial_reporting',
    name: '启用财务报表',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.FINANCE,
    required: true,
    defaultValue: true,
    description: '是否启用财务报表功能'
  },
  {
    key: 'financial_report_frequency',
    name: '财务报表频率',
    type: CONFIG_ITEM_TYPES.SELECT,
    category: CONFIG_TYPES.FINANCE,
    required: true,
    defaultValue: 'monthly',
    options: [
      { label: '每日', value: 'daily' },
      { label: '每周', value: 'weekly' },
      { label: '每月', value: 'monthly' },
      { label: '每季度', value: 'quarterly' },
      { label: '每年', value: 'yearly' }
    ],
    description: '财务报表生成频率'
  },
  {
    key: 'enable_multi_currency',
    name: '启用多货币',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.FINANCE,
    required: true,
    defaultValue: false,
    description: '是否启用多货币支持'
  },
  {
    key: 'exchange_rate_source',
    name: '汇率来源',
    type: CONFIG_ITEM_TYPES.SELECT,
    category: CONFIG_TYPES.FINANCE,
    required: false,
    defaultValue: 'manual',
    options: [
      { label: '手动设置', value: 'manual' },
      { label: '中国人民银行', value: 'pbc' },
      { label: '中国银行', value: 'boc' },
      { label: '中国工商银行', value: 'icbc' },
      { label: '第三方API', value: 'api' }
    ],
    description: '汇率数据来源'
  },
  {
    key: 'exchange_rate_update_frequency',
    name: '汇率更新频率',
    type: CONFIG_ITEM_TYPES.SELECT,
    category: CONFIG_TYPES.FINANCE,
    required: false,
    defaultValue: 'daily',
    options: [
      { label: '每小时', value: 'hourly' },
      { label: '每日', value: 'daily' },
      { label: '每周', value: 'weekly' },
      { label: '每月', value: 'monthly' }
    ],
    description: '汇率更新频率'
  }
]

// 库存参数配置项
export const INVENTORY_CONFIGS = [
  {
    key: 'enable_multi_warehouse',
    name: '启用多仓库',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.INVENTORY,
    required: true,
    defaultValue: false,
    description: '是否启用多仓库管理'
  },
  {
    key: 'default_warehouse',
    name: '默认仓库',
    type: CONFIG_ITEM_TYPES.STRING,
    category: CONFIG_TYPES.INVENTORY,
    required: false,
    defaultValue: 'main',
    description: '默认仓库标识'
  },
  {
    key: 'enable_batch_management',
    name: '启用批次管理',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.INVENTORY,
    required: true,
    defaultValue: true,
    description: '是否启用商品批次管理'
  },
  {
    key: 'enable_serial_number',
    name: '启用序列号管理',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.INVENTORY,
    required: true,
    defaultValue: false,
    description: '是否启用商品序列号管理'
  },
  {
    key: 'enable_expiry_date',
    name: '启用有效期管理',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.INVENTORY,
    required: true,
    defaultValue: true,
    description: '是否启用商品有效期管理'
  },
  {
    key: 'expiry_alert_days',
    name: '有效期警告天数',
    type: CONFIG_ITEM_TYPES.NUMBER,
    category: CONFIG_TYPES.INVENTORY,
    required: true,
    defaultValue: 30,
    min: 1,
    max: 365,
    description: '商品即将到期警告天数'
  },
  {
    key: 'low_stock_threshold',
    name: '低库存阈值',
    type: CONFIG_ITEM_TYPES.NUMBER,
    category: CONFIG_TYPES.INVENTORY,
    required: true,
    defaultValue: 10,
    min: 0,
    max: 1000,
    description: '低库存警告阈值'
  },
  {
    key: 'out_of_stock_threshold',
    name: '缺货阈值',
    type: CONFIG_ITEM_TYPES.NUMBER,
    category: CONFIG_TYPES.INVENTORY,
    required: true,
    defaultValue: 0,
    min: 0,
    max: 100,
    description: '缺货警告阈值'
  },
  {
    key: 'enable_auto_reorder',
    name: '启用自动补货',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.INVENTORY,
    required: true,
    defaultValue: false,
    description: '是否启用自动补货功能'
  },
  {
    key: 'reorder_quantity',
    name: '补货数量',
    type: CONFIG_ITEM_TYPES.NUMBER,
    category: CONFIG_TYPES.INVENTORY,
    required: false,
    defaultValue: 100,
    min: 1,
    max: 10000,
    description: '自动补货数量'
  },
  {
    key: 'enable_stock_taking',
    name: '启用库存盘点',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.INVENTORY,
    required: true,
    defaultValue: true,
    description: '是否启用库存盘点功能'
  },
  {
    key: 'stock_taking_frequency',
    name: '盘点频率',
    type: CONFIG_ITEM_TYPES.SELECT,
    category: CONFIG_TYPES.INVENTORY,
    required: true,
    defaultValue: 'monthly',
    options: [
      { label: '每日', value: 'daily' },
      { label: '每周', value: 'weekly' },
      { label: '每月', value: 'monthly' },
      { label: '每季度', value: 'quarterly' },
      { label: '每年', value: 'yearly' }
    ],
    description: '库存盘点频率'
  },
  {
    key: 'enable_stock_transfer',
    name: '启用库存调拨',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.INVENTORY,
    required: true,
    defaultValue: true,
    description: '是否启用库存调拨功能'
  },
  {
    key: 'enable_stock_adjustment',
    name: '启用库存调整',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.INVENTORY,
    required: true,
    defaultValue: true,
    description: '是否启用库存调整功能'
  },
  {
    key: 'enable_stock_reporting',
    name: '启用库存报表',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.INVENTORY,
    required: true,
    defaultValue: true,
    description: '是否启用库存报表功能'
  },
  {
    key: 'stock_report_frequency',
    name: '库存报表频率',
    type: CONFIG_ITEM_TYPES.SELECT,
    category: CONFIG_TYPES.INVENTORY,
    required: true,
    defaultValue: 'weekly',
    options: [
      { label: '每日', value: 'daily' },
      { label: '每周', value: 'weekly' },
      { label: '每月', value: 'monthly' },
      { label: '每季度', value: 'quarterly' }
    ],
    description: '库存报表生成频率'
  },
  {
    key: 'enable_abc_analysis',
    name: '启用ABC分析',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.INVENTORY,
    required: true,
    defaultValue: true,
    description: '是否启用ABC库存分析'
  },
  {
    key: 'abc_a_percentage',
    name: 'A类商品占比 (%)',
    type: CONFIG_ITEM_TYPES.NUMBER,
    category: CONFIG_TYPES.INVENTORY,
    required: false,
    defaultValue: 80,
    min: 60,
    max: 90,
    description: 'A类商品占比'
  },
  {
    key: 'abc_b_percentage',
    name: 'B类商品占比 (%)',
    type: CONFIG_ITEM_TYPES.NUMBER,
    category: CONFIG_TYPES.INVENTORY,
    required: false,
    defaultValue: 15,
    min: 5,
    max: 30,
    description: 'B类商品占比'
  },
  {
    key: 'enable_fifo',
    name: '启用先进先出',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.INVENTORY,
    required: true,
    defaultValue: true,
    description: '是否启用先进先出库存管理'
  },
  {
    key: 'enable_lifo',
    name: '启用后进先出',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.INVENTORY,
    required: true,
    defaultValue: false,
    description: '是否启用后进先出库存管理'
  },
  {
    key: 'enable_weighted_average',
    name: '启用加权平均',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.INVENTORY,
    required: true,
    defaultValue: false,
    description: '是否启用加权平均库存管理'
  },
  {
    key: 'enable_specific_identification',
    name: '启用个别计价',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.INVENTORY,
    required: true,
    defaultValue: false,
    description: '是否启用个别计价库存管理'
  },
  {
    key: 'enable_barcode',
    name: '启用条码管理',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.INVENTORY,
    required: true,
    defaultValue: true,
    description: '是否启用条码管理'
  },
  {
    key: 'barcode_format',
    name: '条码格式',
    type: CONFIG_ITEM_TYPES.SELECT,
    category: CONFIG_TYPES.INVENTORY,
    required: true,
    defaultValue: 'ean13',
    options: [
      { label: 'EAN-13', value: 'ean13' },
      { label: 'EAN-8', value: 'ean8' },
      { label: 'UPC-A', value: 'upca' },
      { label: 'UPC-E', value: 'upce' },
      { label: 'Code 128', value: 'code128' },
      { label: 'Code 39', value: 'code39' },
      { label: 'QR Code', value: 'qrcode' }
    ],
    description: '条码格式类型'
  },
  {
    key: 'enable_rfid',
    name: '启用RFID',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.INVENTORY,
    required: true,
    defaultValue: false,
    description: '是否启用RFID管理'
  },
  {
    key: 'enable_warehouse_location',
    name: '启用库位管理',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.INVENTORY,
    required: true,
    defaultValue: true,
    description: '是否启用库位管理'
  },
  {
    key: 'enable_inventory_forecasting',
    name: '启用库存预测',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.INVENTORY,
    required: true,
    defaultValue: true,
    description: '是否启用库存预测功能'
  },
  {
    key: 'forecasting_horizon_days',
    name: '预测时间范围 (天)',
    type: CONFIG_ITEM_TYPES.NUMBER,
    category: CONFIG_TYPES.INVENTORY,
    required: false,
    defaultValue: 30,
    min: 7,
    max: 365,
    description: '库存预测时间范围'
  },
  {
    key: 'enable_demand_planning',
    name: '启用需求计划',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.INVENTORY,
    required: true,
    defaultValue: true,
    description: '是否启用需求计划功能'
  },
  {
    key: 'demand_planning_horizon_days',
    name: '需求计划时间范围 (天)',
    type: CONFIG_ITEM_TYPES.NUMBER,
    category: CONFIG_TYPES.INVENTORY,
    required: false,
    defaultValue: 60,
    min: 7,
    max: 365,
    description: '需求计划时间范围'
  }
]

// 客户参数配置项
export const CUSTOMER_CONFIGS = [
  {
    key: 'enable_customer_registration',
    name: '启用客户注册',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.CUSTOMER,
    required: true,
    defaultValue: true,
    description: '是否启用客户注册功能'
  },
  {
    key: 'enable_social_login',
    name: '启用社交登录',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.CUSTOMER,
    required: true,
    defaultValue: true,
    description: '是否启用第三方社交登录'
  },
  {
    key: 'enable_customer_verification',
    name: '启用客户验证',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.CUSTOMER,
    required: true,
    defaultValue: true,
    description: '是否启用客户邮箱/手机验证'
  },
  {
    key: 'enable_customer_approval',
    name: '启用客户审核',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.CUSTOMER,
    required: true,
    defaultValue: false,
    description: '是否启用客户注册审核'
  },
  {
    key: 'enable_customer_groups',
    name: '启用客户分组',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.CUSTOMER,
    required: true,
    defaultValue: true,
    description: '是否启用客户分组管理'
  },
  {
    key: 'enable_customer_tags',
    name: '启用客户标签',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.CUSTOMER,
    required: true,
    defaultValue: true,
    description: '是否启用客户标签管理'
  },
  {
    key: 'enable_customer_levels',
    name: '启用客户等级',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.CUSTOMER,
    required: true,
    defaultValue: true,
    description: '是否启用客户等级管理'
  },
  {
    key: 'enable_loyalty_program',
    name: '启用忠诚度计划',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.CUSTOMER,
    required: true,
    defaultValue: true,
    description: '是否启用客户忠诚度计划'
  },
  {
    key: 'loyalty_points_rate',
    name: '积分兑换比例',
    type: CONFIG_ITEM_TYPES.NUMBER,
    category: CONFIG_TYPES.CUSTOMER,
    required: false,
    defaultValue: 100,
    min: 1,
    max: 1000,
    description: '消费金额与积分兑换比例'
  },
  {
    key: 'enable_customer_analytics',
    name: '启用客户分析',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.CUSTOMER,
    required: true,
    defaultValue: true,
    description: '是否启用客户行为分析'
  },
  {
    key: 'enable_customer_segmentation',
    name: '启用客户细分',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.CUSTOMER,
    required: true,
    defaultValue: true,
    description: '是否启用客户细分功能'
  },
  {
    key: 'enable_customer_lifecycle',
    name: '启用客户生命周期',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.CUSTOMER,
    required: true,
    defaultValue: true,
    description: '是否启用客户生命周期管理'
  },
  {
    key: 'enable_customer_churn',
    name: '启用客户流失分析',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.CUSTOMER,
    required: true,
    defaultValue: true,
    description: '是否启用客户流失分析'
  },
  {
    key: 'churn_prediction_days',
    name: '流失预测天数',
    type: CONFIG_ITEM_TYPES.NUMBER,
    category: CONFIG_TYPES.CUSTOMER,
    required: false,
    defaultValue: 90,
    min: 30,
    max: 365,
    description: '客户流失预测时间范围'
  },
  {
    key: 'enable_customer_feedback',
    name: '启用客户反馈',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.CUSTOMER,
    required: true,
    defaultValue: true,
    description: '是否启用客户反馈功能'
  },
  {
    key: 'enable_customer_support',
    name: '启用客户支持',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.CUSTOMER,
    required: true,
    defaultValue: true,
    description: '是否启用客户支持功能'
  },
  {
    key: 'support_channels',
    name: '支持渠道',
    type: CONFIG_ITEM_TYPES.MULTI_SELECT,
    category: CONFIG_TYPES.CUSTOMER,
    required: false,
    defaultValue: ['email', 'phone', 'chat'],
    options: [
      { label: '邮件', value: 'email' },
      { label: '电话', value: 'phone' },
      { label: '在线聊天', value: 'chat' },
      { label: '短信', value: 'sms' },
      { label: '微信', value: 'wechat' },
      { label: 'QQ', value: 'qq' }
    ],
    description: '客户支持渠道'
  },
  {
    key: 'enable_customer_portal',
    name: '启用客户门户',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.CUSTOMER,
    required: true,
    defaultValue: true,
    description: '是否启用客户自助门户'
  },
  {
    key: 'enable_customer_api',
    name: '启用客户API',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.CUSTOMER,
    required: true,
    defaultValue: false,
    description: '是否启用客户API接口'
  },
  {
    key: 'enable_customer_export',
    name: '启用客户导出',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.CUSTOMER,
    required: true,
    defaultValue: true,
    description: '是否启用客户数据导出'
  },
  {
    key: 'enable_customer_import',
    name: '启用客户导入',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.CUSTOMER,
    required: true,
    defaultValue: true,
    description: '是否启用客户数据导入'
  },
  {
    key: 'enable_customer_backup',
    name: '启用客户备份',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.CUSTOMER,
    required: true,
    defaultValue: true,
    description: '是否启用客户数据备份'
  },
  {
    key: 'customer_backup_frequency',
    name: '客户备份频率',
    type: CONFIG_ITEM_TYPES.SELECT,
    category: CONFIG_TYPES.CUSTOMER,
    required: true,
    defaultValue: 'weekly',
    options: [
      { label: '每日', value: 'daily' },
      { label: '每周', value: 'weekly' },
      { label: '每月', value: 'monthly' }
    ],
    description: '客户数据备份频率'
  }
]

// 组合所有配置项
export const ALL_CONFIGS = [
  ...BUSINESS_CONFIGS,
  ...SYSTEM_CONFIGS,
  ...FINANCE_CONFIGS,
  ...INVENTORY_CONFIGS,
  ...CUSTOMER_CONFIGS,
  ...ORDER_CONFIGS,
  ...PAYMENT_CONFIGS,
  ...SHIPPING_CONFIGS
]

// 配置管理组合式函数
export function useConfig() {
  const loading = ref(false)
  const configs = ref([])
  const categories = ref([])
  const currentConfig = ref({})
  
  // 获取配置分类
  const getConfigCategories = () => {
    const categoryMap = {
      [CONFIG_TYPES.BUSINESS]: { name: '业务参数', icon: 'Business', color: '#409EFF' },
      [CONFIG_TYPES.SYSTEM]: { name: '系统参数', icon: 'Setting', color: '#67C23A' },
      [CONFIG_TYPES.FINANCE]: { name: '财务参数', icon: 'Money', color: '#E6A23C' },
      [CONFIG_TYPES.INVENTORY]: { name: '库存参数', icon: 'Box', color: '#F56C6C' },
      [CONFIG_TYPES.CUSTOMER]: { name: '客户参数', icon: 'User', color: '#909399' },
      [CONFIG_TYPES.ORDER]: { name: '订单参数', icon: 'Document', color: '#B3B3B3' },
      [CONFIG_TYPES.PAYMENT]: { name: '支付参数', icon: 'CreditCard', color: '#FF6B6B' },
      [CONFIG_TYPES.SHIPPING]: { name: '物流参数', icon: 'Truck', color: '#4ECDC4' },
      [CONFIG_TYPES.NOTIFICATION]: { name: '通知参数', icon: 'Bell', color: '#45B7D1' },
      [CONFIG_TYPES.SECURITY]: { name: '安全参数', icon: 'Lock', color: '#96CEB4' }
    }
    
    return Object.entries(categoryMap).map(([key, value]) => ({
      key,
      name: value.name,
      icon: value.icon,
      color: value.color,
      count: ALL_CONFIGS.filter(config => config.category === key).length
    }))
  }
  
  // 获取配置列表
  const getConfigs = async (category = null) => {
    loading.value = true
    try {
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 500))
      
      let filteredConfigs = ALL_CONFIGS
      if (category) {
        filteredConfigs = ALL_CONFIGS.filter(config => config.category === category)
      }
      
      configs.value = filteredConfigs.map(config => ({
        ...config,
        value: config.defaultValue,
        status: CONFIG_STATUS.ENABLED,
        updatedAt: new Date().toISOString(),
        updatedBy: 'system'
      }))
      
      categories.value = getConfigCategories()
      return configs.value
    } catch (error) {
      ElMessage.error('获取配置列表失败')
      throw error
    } finally {
      loading.value = false
    }
  }
  
  // 获取单个配置
  const getConfig = async (key) => {
    loading.value = true
    try {
      await new Promise(resolve => setTimeout(resolve, 300))
      
      const config = ALL_CONFIGS.find(c => c.key === key)
      if (!config) {
        throw new Error('配置项不存在')
      }
      
      currentConfig.value = {
        ...config,
        value: config.defaultValue,
        status: CONFIG_STATUS.ENABLED,
        updatedAt: new Date().toISOString(),
        updatedBy: 'system'
      }
      
      return currentConfig.value
    } catch (error) {
      ElMessage.error('获取配置失败')
      throw error
    } finally {
      loading.value = false
    }
  }
  
  // 更新配置
  const updateConfig = async (key, value) => {
    loading.value = true
    try {
      await new Promise(resolve => setTimeout(resolve, 500))
      
      const configIndex = configs.value.findIndex(c => c.key === key)
      if (configIndex !== -1) {
        configs.value[configIndex] = {
          ...configs.value[configIndex],
          value,
          updatedAt: new Date().toISOString(),
          updatedBy: 'admin'
        }
      }
      
      if (currentConfig.value.key === key) {
        currentConfig.value = {
          ...currentConfig.value,
          value,
          updatedAt: new Date().toISOString(),
          updatedBy: 'admin'
        }
      }
      
      ElMessage.success('配置更新成功')
      return true
    } catch (error) {
      ElMessage.error('配置更新失败')
      throw error
    } finally {
      loading.value = false
    }
  }
  
  // 批量更新配置
  const updateConfigs = async (updates) => {
    loading.value = true
    try {
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      updates.forEach(({ key, value }) => {
        const configIndex = configs.value.findIndex(c => c.key === key)
        if (configIndex !== -1) {
          configs.value[configIndex] = {
            ...configs.value[configIndex],
            value,
            updatedAt: new Date().toISOString(),
            updatedBy: 'admin'
          }
        }
      })
      
      ElMessage.success('配置批量更新成功')
      return true
    } catch (error) {
      ElMessage.error('配置批量更新失败')
      throw error
    } finally {
      loading.value = false
    }
  }
  
  // 重置配置
  const resetConfig = async (key) => {
    loading.value = true
    try {
      await new Promise(resolve => setTimeout(resolve, 300))
      
      const config = ALL_CONFIGS.find(c => c.key === key)
      if (!config) {
        throw new Error('配置项不存在')
      }
      
      await updateConfig(key, config.defaultValue)
      ElMessage.success('配置重置成功')
      return true
    } catch (error) {
      ElMessage.error('配置重置失败')
      throw error
    } finally {
      loading.value = false
    }
  }
  
  // 重置所有配置
  const resetAllConfigs = async () => {
    loading.value = true
    try {
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      configs.value = ALL_CONFIGS.map(config => ({
        ...config,
        value: config.defaultValue,
        status: CONFIG_STATUS.ENABLED,
        updatedAt: new Date().toISOString(),
        updatedBy: 'system'
      }))
      
      ElMessage.success('所有配置重置成功')
      return true
    } catch (error) {
      ElMessage.error('配置重置失败')
      throw error
    } finally {
      loading.value = false
    }
  }
  
  // 导出配置
  const exportConfigs = async (category = null) => {
    loading.value = true
    try {
      await new Promise(resolve => setTimeout(resolve, 500))
      
      let exportData = configs.value
      if (category) {
        exportData = configs.value.filter(config => config.category === category)
      }
      
      const dataStr = JSON.stringify(exportData, null, 2)
      const dataBlob = new Blob([dataStr], { type: 'application/json' })
      const url = URL.createObjectURL(dataBlob)
      
      const link = document.createElement('a')
      link.href = url
      link.download = `configs_${category || 'all'}_${new Date().toISOString().split('T')[0]}.json`
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      URL.revokeObjectURL(url)
      
      ElMessage.success('配置导出成功')
      return true
    } catch (error) {
      ElMessage.error('配置导出失败')
      throw error
    } finally {
      loading.value = false
    }
  }
  
  // 导入配置
  const importConfigs = async (file) => {
    loading.value = true
    try {
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      const reader = new FileReader()
      reader.onload = async (e) => {
        try {
          const importedData = JSON.parse(e.target.result)
          
          // 验证数据格式
          if (!Array.isArray(importedData)) {
            throw new Error('无效的配置文件格式')
          }
          
          // 更新配置
          importedData.forEach(config => {
            const existingIndex = configs.value.findIndex(c => c.key === config.key)
            if (existingIndex !== -1) {
              configs.value[existingIndex] = {
                ...configs.value[existingIndex],
                value: config.value,
                updatedAt: new Date().toISOString(),
                updatedBy: 'import'
              }
            }
          })
          
          ElMessage.success('配置导入成功')
        } catch (error) {
          ElMessage.error('配置文件解析失败')
        }
      }
      reader.readAsText(file)
      
      return true
    } catch (error) {
      ElMessage.error('配置导入失败')
      throw error
    } finally {
      loading.value = false
    }
  }
  
  // 验证配置值
  const validateConfigValue = (config, value) => {
    try {
      switch (config.type) {
        case CONFIG_ITEM_TYPES.NUMBER:
          const numValue = Number(value)
          if (isNaN(numValue)) {
            return { valid: false, message: '请输入有效的数字' }
          }
          if (config.min !== undefined && numValue < config.min) {
            return { valid: false, message: `数值不能小于 ${config.min}` }
          }
          if (config.max !== undefined && numValue > config.max) {
            return { valid: false, message: `数值不能大于 ${config.max}` }
          }
          break
          
        case CONFIG_ITEM_TYPES.EMAIL:
          const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
          if (!emailRegex.test(value)) {
            return { valid: false, message: '请输入有效的邮箱地址' }
          }
          break
          
        case CONFIG_ITEM_TYPES.URL:
          try {
            new URL(value)
          } catch {
            return { valid: false, message: '请输入有效的URL地址' }
          }
          break
          
        case CONFIG_ITEM_TYPES.JSON:
          try {
            JSON.parse(value)
          } catch {
            return { valid: false, message: '请输入有效的JSON格式' }
          }
          break
          
        case CONFIG_ITEM_TYPES.DATE:
          if (!/^\d{4}-\d{2}-\d{2}$/.test(value)) {
            return { valid: false, message: '请输入有效的日期格式 (YYYY-MM-DD)' }
          }
          break
          
        case CONFIG_ITEM_TYPES.TIME:
          if (!/^\d{2}:\d{2}:\d{2}$/.test(value)) {
            return { valid: false, message: '请输入有效的时间格式 (HH:MM:SS)' }
          }
          break
          
        case CONFIG_ITEM_TYPES.DATETIME:
          if (!/^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/.test(value)) {
            return { valid: false, message: '请输入有效的日期时间格式 (YYYY-MM-DD HH:MM:SS)' }
          }
          break
      }
      
      return { valid: true, message: '' }
    } catch (error) {
      return { valid: false, message: '配置值验证失败' }
    }
  }
  
  // 获取配置值
  const getConfigValue = (key) => {
    const config = configs.value.find(c => c.key === key)
    return config ? config.value : null
  }
  
  // 获取配置描述
  const getConfigDescription = (key) => {
    const config = ALL_CONFIGS.find(c => c.key === key)
    return config ? config.description : ''
  }
  
  // 获取配置选项
  const getConfigOptions = (key) => {
    const config = ALL_CONFIGS.find(c => c.key === key)
    return config ? config.options || [] : []
  }
  
  return {
    loading,
    configs,
    categories,
    currentConfig,
    CONFIG_TYPES,
    CONFIG_ITEM_TYPES,
    CONFIG_STATUS,
    BUSINESS_CONFIGS,
    SYSTEM_CONFIGS,
    FINANCE_CONFIGS,
    INVENTORY_CONFIGS,
    CUSTOMER_CONFIGS,
    ORDER_CONFIGS,
    PAYMENT_CONFIGS,
    SHIPPING_CONFIGS,
    ALL_CONFIGS,
    getConfigCategories,
    getConfigs,
    getConfig,
    updateConfig,
    updateConfigs,
    resetConfig,
    resetAllConfigs,
    exportConfigs,
    importConfigs,
    validateConfigValue,
    getConfigValue,
    getConfigDescription,
    getConfigOptions
  }
}

// 订单参数配置项
export const ORDER_CONFIGS = [
  {
    key: 'enable_order_approval',
    name: '启用订单审核',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.ORDER,
    required: true,
    defaultValue: false,
    description: '是否启用订单审核功能'
  },
  {
    key: 'enable_order_cancellation',
    name: '启用订单取消',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.ORDER,
    required: true,
    defaultValue: true,
    description: '是否启用订单取消功能'
  },
  {
    key: 'enable_order_modification',
    name: '启用订单修改',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.ORDER,
    required: true,
    defaultValue: true,
    description: '是否启用订单修改功能'
  },
  {
    key: 'enable_order_splitting',
    name: '启用订单拆分',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.ORDER,
    required: true,
    defaultValue: true,
    description: '是否启用订单拆分功能'
  },
  {
    key: 'enable_order_merging',
    name: '启用订单合并',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.ORDER,
    required: true,
    defaultValue: true,
    description: '是否启用订单合并功能'
  },
  {
    key: 'enable_partial_shipping',
    name: '启用部分发货',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.ORDER,
    required: true,
    defaultValue: true,
    description: '是否启用部分发货功能'
  },
  {
    key: 'enable_backorder',
    name: '启用缺货订单',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.ORDER,
    required: true,
    defaultValue: true,
    description: '是否启用缺货订单功能'
  },
  {
    key: 'enable_preorder',
    name: '启用预售订单',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.ORDER,
    required: true,
    defaultValue: false,
    description: '是否启用预售订单功能'
  },
  {
    key: 'enable_dropshipping',
    name: '启用代发货',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.ORDER,
    required: true,
    defaultValue: false,
    description: '是否启用代发货功能'
  },
  {
    key: 'enable_order_tracking',
    name: '启用订单跟踪',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.ORDER,
    required: true,
    defaultValue: true,
    description: '是否启用订单跟踪功能'
  },
  {
    key: 'enable_order_notifications',
    name: '启用订单通知',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.ORDER,
    required: true,
    defaultValue: true,
    description: '是否启用订单状态通知'
  },
  {
    key: 'order_notification_channels',
    name: '订单通知渠道',
    type: CONFIG_ITEM_TYPES.MULTI_SELECT,
    category: CONFIG_TYPES.ORDER,
    required: false,
    defaultValue: ['email', 'sms'],
    options: [
      { label: '邮件', value: 'email' },
      { label: '短信', value: 'sms' },
      { label: '微信', value: 'wechat' },
      { label: 'APP推送', value: 'push' }
    ],
    description: '订单状态通知渠道'
  },
  {
    key: 'enable_order_analytics',
    name: '启用订单分析',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.ORDER,
    required: true,
    defaultValue: true,
    description: '是否启用订单分析功能'
  },
  {
    key: 'enable_order_forecasting',
    name: '启用订单预测',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.ORDER,
    required: true,
    defaultValue: true,
    description: '是否启用订单预测功能'
  },
  {
    key: 'order_forecasting_horizon_days',
    name: '订单预测时间范围 (天)',
    type: CONFIG_ITEM_TYPES.NUMBER,
    category: CONFIG_TYPES.ORDER,
    required: false,
    defaultValue: 30,
    min: 7,
    max: 365,
    description: '订单预测时间范围'
  },
  {
    key: 'enable_order_optimization',
    name: '启用订单优化',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.ORDER,
    required: true,
    defaultValue: true,
    description: '是否启用订单优化功能'
  },
  {
    key: 'enable_order_routing',
    name: '启用订单路由',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.ORDER,
    required: true,
    defaultValue: true,
    description: '是否启用订单路由功能'
  },
  {
    key: 'enable_order_prioritization',
    name: '启用订单优先级',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.ORDER,
    required: true,
    defaultValue: true,
    description: '是否启用订单优先级管理'
  },
  {
    key: 'enable_order_batching',
    name: '启用订单批处理',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.ORDER,
    required: true,
    defaultValue: true,
    description: '是否启用订单批处理功能'
  },
  {
    key: 'order_batch_size',
    name: '订单批处理大小',
    type: CONFIG_ITEM_TYPES.NUMBER,
    category: CONFIG_TYPES.ORDER,
    required: false,
    defaultValue: 50,
    min: 10,
    max: 1000,
    description: '订单批处理大小'
  },
  {
    key: 'enable_order_export',
    name: '启用订单导出',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.ORDER,
    required: true,
    defaultValue: true,
    description: '是否启用订单数据导出'
  },
  {
    key: 'enable_order_import',
    name: '启用订单导入',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.ORDER,
    required: true,
    defaultValue: true,
    description: '是否启用订单数据导入'
  },
  {
    key: 'enable_order_backup',
    name: '启用订单备份',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.ORDER,
    required: true,
    defaultValue: true,
    description: '是否启用订单数据备份'
  },
  {
    key: 'order_backup_frequency',
    name: '订单备份频率',
    type: CONFIG_ITEM_TYPES.SELECT,
    category: CONFIG_TYPES.ORDER,
    required: true,
    defaultValue: 'daily',
    options: [
      { label: '每日', value: 'daily' },
      { label: '每周', value: 'weekly' },
      { label: '每月', value: 'monthly' }
    ],
    description: '订单数据备份频率'
  }
]

// 支付参数配置项
export const PAYMENT_CONFIGS = [
  {
    key: 'enable_credit_card',
    name: '启用信用卡支付',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.PAYMENT,
    required: true,
    defaultValue: true,
    description: '是否启用信用卡支付'
  },
  {
    key: 'enable_debit_card',
    name: '启用借记卡支付',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.PAYMENT,
    required: true,
    defaultValue: true,
    description: '是否启用借记卡支付'
  },
  {
    key: 'enable_bank_transfer',
    name: '启用银行转账',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.PAYMENT,
    required: true,
    defaultValue: true,
    description: '是否启用银行转账支付'
  },
  {
    key: 'enable_cash_on_delivery',
    name: '启用货到付款',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.PAYMENT,
    required: true,
    defaultValue: true,
    description: '是否启用货到付款'
  },
  {
    key: 'enable_digital_wallet',
    name: '启用数字钱包',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.PAYMENT,
    required: true,
    defaultValue: true,
    description: '是否启用数字钱包支付'
  },
  {
    key: 'enable_cryptocurrency',
    name: '启用加密货币',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.PAYMENT,
    required: true,
    defaultValue: false,
    description: '是否启用加密货币支付'
  },
  {
    key: 'enable_installment',
    name: '启用分期付款',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.PAYMENT,
    required: true,
    defaultValue: false,
    description: '是否启用分期付款功能'
  },
  {
    key: 'installment_periods',
    name: '分期期数',
    type: CONFIG_ITEM_TYPES.MULTI_SELECT,
    category: CONFIG_TYPES.PAYMENT,
    required: false,
    defaultValue: [3, 6, 12],
    options: [
      { label: '3期', value: 3 },
      { label: '6期', value: 6 },
      { label: '12期', value: 12 },
      { label: '24期', value: 24 },
      { label: '36期', value: 36 }
    ],
    description: '可选的分期期数'
  },
  {
    key: 'enable_subscription_payment',
    name: '启用订阅支付',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.PAYMENT,
    required: true,
    defaultValue: false,
    description: '是否启用订阅支付功能'
  },
  {
    key: 'enable_recurring_payment',
    name: '启用定期支付',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.PAYMENT,
    required: true,
    defaultValue: false,
    description: '是否启用定期支付功能'
  },
  {
    key: 'enable_payment_retry',
    name: '启用支付重试',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.PAYMENT,
    required: true,
    defaultValue: true,
    description: '是否启用支付失败重试'
  },
  {
    key: 'payment_retry_attempts',
    name: '支付重试次数',
    type: CONFIG_ITEM_TYPES.NUMBER,
    category: CONFIG_TYPES.PAYMENT,
    required: false,
    defaultValue: 3,
    min: 1,
    max: 10,
    description: '支付失败重试次数'
  },
  {
    key: 'payment_retry_interval',
    name: '支付重试间隔 (小时)',
    type: CONFIG_ITEM_TYPES.NUMBER,
    category: CONFIG_TYPES.PAYMENT,
    required: false,
    defaultValue: 24,
    min: 1,
    max: 168,
    description: '支付失败重试间隔时间'
  },
  {
    key: 'enable_payment_fraud_detection',
    name: '启用支付欺诈检测',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.PAYMENT,
    required: true,
    defaultValue: true,
    description: '是否启用支付欺诈检测'
  },
  {
    key: 'enable_payment_encryption',
    name: '启用支付加密',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.PAYMENT,
    required: true,
    defaultValue: true,
    description: '是否启用支付数据加密'
  },
  {
    key: 'enable_payment_logging',
    name: '启用支付日志',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.PAYMENT,
    required: true,
    defaultValue: true,
    description: '是否启用支付操作日志'
  },
  {
    key: 'payment_log_retention_days',
    name: '支付日志保留天数',
    type: CONFIG_ITEM_TYPES.NUMBER,
    category: CONFIG_TYPES.PAYMENT,
    required: false,
    defaultValue: 365,
    min: 30,
    max: 1825,
    description: '支付日志保留天数'
  }
]

// 物流参数配置项
export const SHIPPING_CONFIGS = [
  {
    key: 'enable_shipping',
    name: '启用物流配送',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.SHIPPING,
    required: true,
    defaultValue: true,
    description: '是否启用物流配送功能'
  },
  {
    key: 'enable_free_shipping',
    name: '启用免费配送',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.SHIPPING,
    required: true,
    defaultValue: true,
    description: '是否启用免费配送'
  },
  {
    key: 'free_shipping_threshold',
    name: '免费配送门槛',
    type: CONFIG_ITEM_TYPES.NUMBER,
    category: CONFIG_TYPES.SHIPPING,
    required: false,
    defaultValue: 99,
    min: 0,
    max: 1000,
    description: '免费配送最低消费金额'
  },
  {
    key: 'enable_express_shipping',
    name: '启用快递配送',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.SHIPPING,
    required: true,
    defaultValue: true,
    description: '是否启用快递配送'
  },
  {
    key: 'enable_same_day_delivery',
    name: '启用当日达',
    type: CONFIG_ITEM_TYPES.BOOLEAN,
    category: CONFIG_TYPES.SHIPPING,
    required: true,
    defaultValue: false,
    description: '是否启用当日达配送服务'
  }
]
