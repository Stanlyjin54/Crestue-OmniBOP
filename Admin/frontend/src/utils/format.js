/**
 * 管理后台格式化工具函数集合
 * 基于B2B格式化工具进行扩展
 */

/**
 * 数字格式化
 * @param {number} num - 数字
 * @param {number} decimals - 小数位数
 * @param {string} thousandsSeparator - 千位分隔符
 * @returns {string} 格式化后的数字字符串
 */
export function formatNumber(num, decimals = 0, thousandsSeparator = ',') {
  if (isNaN(num)) return '0'
  
  const fixedNum = Number(num).toFixed(decimals)
  const parts = fixedNum.split('.')
  parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, thousandsSeparator)
  
  return parts.join('.')
}

/**
 * 货币格式化
 * @param {number} amount - 金额
 * @param {string} currency - 货币代码
 * @param {number} decimals - 小数位数
 * @returns {string} 格式化后的货币字符串
 */
export function formatCurrency(amount, currency = 'CNY', decimals = 2) {
  if (isNaN(amount)) return '¥0.00'
  
  const symbols = {
    'CNY': '¥',
    'USD': '$',
    'EUR': '€',
    'GBP': '£',
    'JPY': '¥'
  }
  
  const symbol = symbols[currency] || '¥'
  const formattedAmount = formatNumber(amount, decimals)
  
  return `${symbol}${formattedAmount}`
}

/**
 * 百分比格式化
 * @param {number} value - 数值
 * @param {number} decimals - 小数位数
 * @returns {string} 格式化后的百分比字符串
 */
export function formatPercent(value, decimals = 2) {
  if (isNaN(value)) return '0%'
  
  return `${formatNumber(value * 100, decimals)}%`
}

/**
 * 时间格式化
 * @param {Date|string|number} date - 时间
 * @param {string} format - 格式模板
 * @returns {string} 格式化后的时间字符串
 */
export function formatTime(date, format = 'YYYY-MM-DD HH:mm:ss') {
  const d = new Date(date)
  
  if (isNaN(d.getTime())) return ''
  
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')
  const seconds = String(d.getSeconds()).padStart(2, '0')
  
  return format
    .replace('YYYY', year)
    .replace('MM', month)
    .replace('DD', day)
    .replace('HH', hours)
    .replace('mm', minutes)
    .replace('ss', seconds)
}

/**
 * 相对时间格式化
 * @param {Date|string|number} date - 时间
 * @returns {string} 相对时间字符串
 */
export function formatRelativeTime(date) {
  const d = new Date(date)
  const now = new Date()
  const diff = now - d
  
  if (isNaN(diff)) return ''
  
  const seconds = Math.floor(diff / 1000)
  const minutes = Math.floor(seconds / 60)
  const hours = Math.floor(minutes / 60)
  const days = Math.floor(hours / 24)
  const months = Math.floor(days / 30)
  const years = Math.floor(months / 12)
  
  if (years > 0) return `${years}年前`
  if (months > 0) return `${months}个月前`
  if (days > 0) return `${days}天前`
  if (hours > 0) return `${hours}小时前`
  if (minutes > 0) return `${minutes}分钟前`
  if (seconds > 0) return `${seconds}秒前`
  
  return '刚刚'
}

/**
 * 文件大小格式化
 * @param {number} bytes - 字节数
 * @param {number} decimals - 小数位数
 * @returns {string} 格式化后的文件大小字符串
 */
export function formatFileSize(bytes, decimals = 2) {
  if (bytes === 0) return '0 Bytes'
  
  const k = 1024
  const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  
  return `${parseFloat((bytes / Math.pow(k, i)).toFixed(decimals))} ${sizes[i]}`
}

/**
 * 手机号格式化
 * @param {string} phone - 手机号
 * @returns {string} 格式化后的手机号
 */
export function formatPhone(phone) {
  if (!phone) return ''
  
  const cleaned = phone.replace(/\D/g, '')
  
  if (cleaned.length === 11) {
    return cleaned.replace(/(\d{3})(\d{4})(\d{4})/, '$1 $2 $3')
  }
  
  return phone
}

/**
 * 银行卡号格式化
 * @param {string} cardNo - 银行卡号
 * @returns {string} 格式化后的银行卡号
 */
export function formatBankCard(cardNo) {
  if (!cardNo) return ''
  
  const cleaned = cardNo.replace(/\D/g, '')
  
  // 每4位分组
  return cleaned.replace(/(\d{4})(?=\d)/g, '$1 ')
}

/**
 * 身份证号格式化
 * @param {string} idCard - 身份证号
 * @returns {string} 格式化后的身份证号
 */
export function formatIdCard(idCard) {
  if (!idCard) return ''
  
  const cleaned = idCard.replace(/\D/g, '')
  
  if (cleaned.length === 18) {
    return cleaned.replace(/(\d{6})(\d{8})(\d{4})/, '$1 $2 $3')
  }
  
  return idCard
}

/**
 * 地址格式化
 * @param {object} address - 地址对象
 * @returns {string} 格式化后的地址字符串
 */
export function formatAddress(address) {
  if (!address) return ''
  
  const { province, city, district, detail } = address
  const parts = [province, city, district, detail].filter(Boolean)
  
  return parts.join(' ')
}

/**
 * 企业名称格式化
 * @param {string} companyName - 企业名称
 * @param {number} maxLength - 最大长度
 * @returns {string} 格式化后的企业名称
 */
export function formatCompanyName(companyName, maxLength = 20) {
  if (!companyName) return ''
  
  if (companyName.length <= maxLength) {
    return companyName
  }
  
  return companyName.substring(0, maxLength) + '...'
}

/**
 * 订单号格式化
 * @param {string} orderNo - 订单号
 * @returns {string} 格式化后的订单号
 */
export function formatOrderNo(orderNo) {
  if (!orderNo) return ''
  
  // 显示前8位和后4位，中间用****代替
  if (orderNo.length > 12) {
    return orderNo.substring(0, 8) + '****' + orderNo.substring(orderNo.length - 4)
  }
  
  return orderNo
}

/**
 * 状态格式化
 * @param {string|number} status - 状态值
 * @param {object} statusMap - 状态映射对象
 * @returns {string} 格式化后的状态字符串
 */
export function formatStatus(status, statusMap = {}) {
  const defaultMap = {
    '0': '禁用',
    '1': '启用',
    'active': '活跃',
    'inactive': '非活跃',
    'pending': '待处理',
    'approved': '已通过',
    'rejected': '已拒绝'
  }
  
  const map = { ...defaultMap, ...statusMap }
  return map[status] || status || '未知'
}

/**
 * 优先级格式化
 * @param {string|number} priority - 优先级
 * @returns {string} 格式化后的优先级字符串
 */
export function formatPriority(priority) {
  const priorityMap = {
    '1': '低',
    '2': '中',
    '3': '高',
    'low': '低',
    'medium': '中',
    'high': '高'
  }
  
  return priorityMap[priority] || priority || '中'
}

/**
 * 评分格式化
 * @param {number} score - 评分
 * @param {number} maxScore - 满分
 * @param {number} decimals - 小数位数
 * @returns {string} 格式化后的评分字符串
 */
export function formatScore(score, maxScore = 5, decimals = 1) {
  if (isNaN(score)) return '0.0'
  
  const validScore = Math.min(Math.max(score, 0), maxScore)
  return `${validScore.toFixed(decimals)}/${maxScore}`
}

/**
 * 文本长度格式化
 * @param {string} text - 文本
 * @param {number} maxLength - 最大长度
 * @returns {string} 格式化后的文本
 */
export function formatTextLength(text, maxLength = 50) {
  if (!text) return ''
  
  if (text.length <= maxLength) {
    return text
  }
  
  return text.substring(0, maxLength) + '...'
}

/**
 * 标签格式化
 * @param {array} tags - 标签数组
 * @param {number} maxCount - 最大显示数量
 * @returns {string} 格式化后的标签字符串
 */
export function formatTags(tags, maxCount = 3) {
  if (!Array.isArray(tags) || tags.length === 0) return ''
  
  const displayTags = tags.slice(0, maxCount)
  const result = displayTags.join(', ')
  
  if (tags.length > maxCount) {
    return `${result} +${tags.length - maxCount}`
  }
  
  return result
}

/**
 * 管理员后台专用格式化函数
 */
export const adminFormats = {
  // 权限级别格式化
  formatPermissionLevel: (level) => {
    const levelMap = {
      '1': '超级管理员',
      '2': '管理员',
      '3': '普通用户',
      'super': '超级管理员',
      'admin': '管理员',
      'user': '普通用户'
    }
    return levelMap[level] || level || '未知'
  },

  // 操作类型格式化
  formatOperationType: (type) => {
    const typeMap = {
      'create': '创建',
      'update': '更新',
      'delete': '删除',
      'view': '查看',
      'export': '导出',
      'import': '导入'
    }
    return typeMap[type] || type || '未知'
  },

  // 登录状态格式化
  formatLoginStatus: (status) => {
    const statusMap = {
      'online': '在线',
      'offline': '离线',
      'busy': '忙碌',
      'away': '离开'
    }
    return statusMap[status] || status || '未知'
  },

  // 系统模块格式化
  formatSystemModule: (module) => {
    const moduleMap = {
      'user': '用户管理',
      'product': '商品管理',
      'order': '订单管理',
      'inventory': '库存管理',
      'customer': '客户管理',
      'finance': '财务管理',
      'report': '报表分析',
      'system': '系统设置'
    }
    return moduleMap[module] || module || '未知模块'
  }
}

// 默认导出所有格式化函数
export default {
  formatNumber,
  formatCurrency,
  formatPercent,
  formatTime,
  formatRelativeTime,
  formatFileSize,
  formatPhone,
  formatBankCard,
  formatIdCard,
  formatAddress,
  formatCompanyName,
  formatOrderNo,
  formatStatus,
  formatPriority,
  formatScore,
  formatTextLength,
  formatTags,
  ...adminFormats
}