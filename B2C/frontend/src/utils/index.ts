// 格式化价格
export const formatPrice = (price: number): string => {
  return `¥${price.toFixed(2)}`
}

// 格式化原价（带删除线）
export const formatOriginalPrice = (price: number): string => {
  return `¥${price.toFixed(2)}`
}

// 格式化数量
export const formatQuantity = (quantity: number): string => {
  if (quantity >= 10000) {
    return `${(quantity / 10000).toFixed(1)}万+`
  } else if (quantity >= 1000) {
    return `${(quantity / 1000).toFixed(1)}千+`
  }
  return quantity.toString()
}

// 格式化日期
export const formatDate = (date: string | Date, format = 'YYYY-MM-DD HH:mm:ss'): string => {
  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')
  const seconds = String(d.getSeconds()).padStart(2, '0')
  
  return format
    .replace('YYYY', year.toString())
    .replace('MM', month)
    .replace('DD', day)
    .replace('HH', hours)
    .replace('mm', minutes)
    .replace('ss', seconds)
}

// 格式化相对时间
export const formatRelativeTime = (date: string | Date): string => {
  const now = new Date()
  const target = new Date(date)
  const diff = now.getTime() - target.getTime()
  
  const seconds = Math.floor(diff / 1000)
  const minutes = Math.floor(seconds / 60)
  const hours = Math.floor(minutes / 60)
  const days = Math.floor(hours / 24)
  
  if (days > 0) {
    return `${days}天前`
  } else if (hours > 0) {
    return `${hours}小时前`
  } else if (minutes > 0) {
    return `${minutes}分钟前`
  } else {
    return '刚刚'
  }
}

// 生成随机字符串
export const generateRandomString = (length = 8): string => {
  const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789'
  let result = ''
  for (let i = 0; i < length; i++) {
    result += chars.charAt(Math.floor(Math.random() * chars.length))
  }
  return result
}

// 防抖函数
export const debounce = <T extends (...args: any[]) => any>(
  func: T,
  wait: number
): ((...args: Parameters<T>) => void) => {
  let timeout: number | null = null
  
  return (...args: Parameters<T>) => {
    if (timeout) clearTimeout(timeout)
    timeout = setTimeout(() => func(...args), wait)
  }
}

// 节流函数
export const throttle = <T extends (...args: any[]) => any>(
  func: T,
  limit: number
): ((...args: Parameters<T>) => void) => {
  let inThrottle: boolean
  
  return (...args: Parameters<T>) => {
    if (!inThrottle) {
      func(...args)
      inThrottle = true
      setTimeout(() => inThrottle = false, limit)
    }
  }
}

// 导出性能优化工具
export * from './performance'
export * from './cache'