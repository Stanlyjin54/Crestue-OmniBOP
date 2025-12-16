/**
 * B2B格式化工具函数
 * 提供数字、时间、货币等格式化功能
 */

/**
 * 格式化数字
 * @param {number|string} number - 要格式化的数字
 * @param {object} options - 格式化选项
 * @returns {string} 格式化后的字符串
 */
export function formatNumber(number, options = {}) {
  const {
    decimals = 0,
    thousandSeparator = ',',
    decimalSeparator = '.',
    prefix = '',
    suffix = ''
  } = options;
  
  if (number === null || number === undefined || isNaN(number)) {
    return '0';
  }
  
  const num = parseFloat(number);
  
  // 处理大数字
  if (Math.abs(num) >= 100000000) {
    return `${prefix}${(num / 100000000).toFixed(2)}亿${suffix}`;
  } else if (Math.abs(num) >= 10000) {
    return `${prefix}${(num / 10000).toFixed(2)}万${suffix}`;
  }
  
  // 普通数字格式化
  const parts = num.toFixed(decimals).split('.');
  const integerPart = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, thousandSeparator);
  const decimalPart = parts[1] ? decimalSeparator + parts[1] : '';
  
  return `${prefix}${integerPart}${decimalPart}${suffix}`;
}

/**
 * 格式化货币
 * @param {number|string} amount - 金额
 * @param {string} currency - 货币代码
 * @param {object} options - 格式化选项
 * @returns {string} 格式化后的货币字符串
 */
export function formatCurrency(amount, currency = 'CNY', options = {}) {
  const {
    decimals = 2,
    thousandSeparator = ',',
    decimalSeparator = '.',
    showSymbol = true,
    symbolPosition = 'before' // before, after
  } = options;
  
  if (amount === null || amount === undefined || isNaN(amount)) {
    return showSymbol ? '¥0.00' : '0.00';
  }
  
  const num = parseFloat(amount);
  const formattedNumber = formatNumber(num, {
    decimals,
    thousandSeparator,
    decimalSeparator
  });
  
  // 货币符号映射
  const currencySymbols = {
    'CNY': '¥',
    'USD': '$',
    'EUR': '€',
    'GBP': '£',
    'JPY': '¥',
    'KRW': '₩',
    'HKD': 'HK$',
    'TWD': 'NT$'
  };
  
  const symbol = currencySymbols[currency] || currency;
  
  if (showSymbol) {
    return symbolPosition === 'before' 
      ? `${symbol}${formattedNumber}`
      : `${formattedNumber}${symbol}`;
  }
  
  return formattedNumber;
}

/**
 * 格式化百分比
 * @param {number|string} value - 百分比值
 * @param {object} options - 格式化选项
 * @returns {string} 格式化后的百分比字符串
 */
export function formatPercentage(value, options = {}) {
  const {
    decimals = 2,
    multiplier = 100,
    showSymbol = true
  } = options;
  
  if (value === null || value === undefined || isNaN(value)) {
    return showSymbol ? '0.00%' : '0.00';
  }
  
  const num = parseFloat(value);
  const percentage = (num * multiplier).toFixed(decimals);
  
  return showSymbol ? `${percentage}%` : percentage;
}

/**
 * 格式化时间
 * @param {Date|number|string} time - 时间
 * @param {string} format - 格式模板
 * @returns {string} 格式化后的时间字符串
 */
export function formatTime(time, format = 'YYYY-MM-DD HH:mm:ss') {
  if (!time) return '';
  
  let date;
  if (time instanceof Date) {
    date = time;
  } else if (typeof time === 'number') {
    // 处理时间戳（秒或毫秒）
    date = new Date(time.toString().length === 10 ? time * 1000 : time);
  } else if (typeof time === 'string') {
    date = new Date(time);
  } else {
    return '';
  }
  
  if (isNaN(date.getTime())) return '';
  
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');
  
  const replacements = {
    'YYYY': year,
    'YY': String(year).slice(-2),
    'MM': month,
    'M': date.getMonth() + 1,
    'DD': day,
    'D': date.getDate(),
    'HH': hours,
    'H': date.getHours(),
    'mm': minutes,
    'm': date.getMinutes(),
    'ss': seconds,
    's': date.getSeconds()
  };
  
  let result = format;
  Object.entries(replacements).forEach(([key, value]) => {
    result = result.replace(new RegExp(key, 'g'), value);
  });
  
  return result;
}

/**
 * 格式化日期
 * @param {Date|number|string} date - 日期
 * @param {string} format - 格式模板
 * @returns {string} 格式化后的日期字符串
 */
export function formatDate(date, format = 'YYYY-MM-DD') {
  return formatTime(date, format);
}

/**
 * 格式化相对时间
 * @param {Date|number|string} time - 时间
 * @param {object} options - 格式化选项
 * @returns {string} 相对时间字符串
 */
export function formatRelativeTime(time, options = {}) {
  const {
    locale = 'zh-CN',
    thresholds = {}
  } = options;
  
  if (!time) return '';
  
  const date = new Date(time);
  if (isNaN(date.getTime())) return '';
  
  const now = new Date();
  const diff = now.getTime() - date.getTime();
  const seconds = Math.floor(diff / 1000);
  const minutes = Math.floor(seconds / 60);
  const hours = Math.floor(minutes / 60);
  const days = Math.floor(hours / 24);
  const months = Math.floor(days / 30);
  const years = Math.floor(days / 365);
  
  // 中文相对时间
  if (locale === 'zh-CN') {
    if (seconds < 60) return '刚刚';
    if (minutes < 60) return `${minutes}分钟前`;
    if (hours < 24) return `${hours}小时前`;
    if (days < 30) return `${days}天前`;
    if (months < 12) return `${months}个月前`;
    return `${years}年前`;
  }
  
  // 英文相对时间
  if (locale === 'en-US') {
    if (seconds < 60) return 'just now';
    if (minutes < 60) return `${minutes} minute${minutes > 1 ? 's' : ''} ago`;
    if (hours < 24) return `${hours} hour${hours > 1 ? 's' : ''} ago`;
    if (days < 30) return `${days} day${days > 1 ? 's' : ''} ago`;
    if (months < 12) return `${months} month${months > 1 ? 's' : ''} ago`;
    return `${years} year${years > 1 ? 's' : ''} ago`;
  }
  
  return formatTime(date, 'YYYY-MM-DD HH:mm');
}

/**
 * 格式化文件大小
 * @param {number} bytes - 字节数
 * @param {object} options - 格式化选项
 * @returns {string} 格式化后的文件大小字符串
 */
export function formatFileSize(bytes, options = {}) {
  const {
    decimals = 2,
    binary = false // 是否使用二进制（1024）
  } = options;
  
  if (bytes === 0) return '0 B';
  
  const k = binary ? 1024 : 1000;
  const sizes = binary 
    ? ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB']
    : ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
  
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  const size = (bytes / Math.pow(k, i)).toFixed(decimals);
  
  return `${size} ${sizes[i]}`;
}

/**
 * 格式化手机号
 * @param {string} phone - 手机号
 * @param {object} options - 格式化选项
 * @returns {string} 格式化后的手机号字符串
 */
export function formatPhone(phone, options = {}) {
  const {
    format = 'xxx-xxxx-xxxx', // 格式化模板
    separator = '-' // 分隔符
  } = options;
  
  if (!phone || typeof phone !== 'string') return phone;
  
  // 移除所有非数字字符
  const cleanPhone = phone.replace(/\D/g, '');
  
  if (cleanPhone.length !== 11) return phone;
  
  // 默认格式化：138-1234-5678
  if (format === 'xxx-xxxx-xxxx') {
    return `${cleanPhone.slice(0, 3)}${separator}${cleanPhone.slice(3, 7)}${separator}${cleanPhone.slice(7)}`;
  }
  
  // 自定义格式化
  let formatted = '';
  let phoneIndex = 0;
  
  for (let i = 0; i < format.length; i++) {
    if (format[i] === 'x' && phoneIndex < cleanPhone.length) {
      formatted += cleanPhone[phoneIndex++];
    } else if (format[i] === separator) {
      formatted += separator;
    } else {
      formatted += format[i];
    }
  }
  
  return formatted;
}

/**
 * 格式化银行卡号
 * @param {string} cardNumber - 银行卡号
 * @param {object} options - 格式化选项
 * @returns {string} 格式化后的银行卡号字符串
 */
export function formatBankCard(cardNumber, options = {}) {
  const {
    showLast = 4, // 显示后几位
    maskChar = '*' // 掩码字符
  } = options;
  
  if (!cardNumber || typeof cardNumber !== 'string') return cardNumber;
  
  // 移除所有非数字字符
  const cleanCard = cardNumber.replace(/\D/g, '');
  
  if (cleanCard.length < showLast) return cardNumber;
  
  // 格式化：每4位一组
  const formatted = cleanCard.match(/.{1,4}/g).join(' ');
  
  // 掩码处理
  if (showLast > 0) {
    const maskedLength = cleanCard.length - showLast;
    const masked = maskChar.repeat(Math.ceil(maskedLength / 4)).slice(0, Math.ceil(maskedLength / 4));
    const lastDigits = cleanCard.slice(-showLast);
    
    return `${masked} ${lastDigits.match(/.{1,4}/g).join(' ')}`.trim();
  }
  
  return formatted;
}

/**
 * 格式化身份证号
 * @param {string} idCard - 身份证号
 * @param {object} options - 格式化选项
 * @returns {string} 格式化后的身份证号字符串
 */
export function formatIdCard(idCard, options = {}) {
  const {
    showLast = 4, // 显示后几位
    maskChar = '*' // 掩码字符
  } = options;
  
  if (!idCard || typeof idCard !== 'string') return idCard;
  
  // 移除所有非数字和X字符
  const cleanId = idCard.replace(/[^\dX]/gi, '');
  
  if (cleanId.length !== 18) return idCard;
  
  // 掩码处理
  if (showLast > 0) {
    const maskedLength = cleanId.length - showLast;
    const masked = maskChar.repeat(maskedLength);
    const lastDigits = cleanId.slice(-showLast);
    return `${masked}${lastDigits}`;
  }
  
  return cleanId;
}

/**
 * 格式化地址
 * @param {object} address - 地址对象
 * @param {object} options - 格式化选项
 * @returns {string} 格式化后的地址字符串
 */
export function formatAddress(address, options = {}) {
  const {
    showFull = true, // 是否显示完整地址
    separator = '' // 分隔符
  } = options;
  
  if (!address || typeof address !== 'object') return '';
  
  const parts = [];
  
  if (address.province) parts.push(address.province);
  if (address.city && address.city !== address.province) parts.push(address.city);
  if (address.district) parts.push(address.district);
  if (showFull && address.detail) parts.push(address.detail);
  
  return parts.join(separator || '');
}

/**
 * 格式化企业名称
 * @param {string} companyName - 企业名称
 * @param {object} options - 格式化选项
 * @returns {string} 格式化后的企业名称字符串
 */
export function formatCompanyName(companyName, options = {}) {
  const {
    maxLength = 20, // 最大长度
    ellipsis = '...' // 省略符号
  } = options;
  
  if (!companyName || typeof companyName !== 'string') return companyName;
  
  // 移除常见后缀
  const cleanName = companyName
    .replace(/有限公司$/g, '')
    .replace(/股份有限公司$/g, '')
    .replace(/有限责任公司$/g, '')
    .replace(/集团$/g, '')
    .replace(/公司$/g, '')
    .trim();
  
  if (cleanName.length <= maxLength) return cleanName;
  
  return cleanName.slice(0, maxLength - ellipsis.length) + ellipsis;
}

/**
 * 格式化订单号
 * @param {string} orderNo - 订单号
 * @param {object} options - 格式化选项
 * @returns {string} 格式化后的订单号字符串
 */
export function formatOrderNo(orderNo, options = {}) {
  const {
    showLength = 8, // 显示长度
    position = 'end', // 显示位置：start, end, middle
    separator = '' // 分隔符
  } = options;
  
  if (!orderNo || typeof orderNo !== 'string') return orderNo;
  
  if (orderNo.length <= showLength) return orderNo;
  
  if (position === 'start') {
    return orderNo.slice(0, showLength) + separator + '...';
  } else if (position === 'end') {
    return '...' + separator + orderNo.slice(-showLength);
  } else if (position === 'middle') {
    const startLength = Math.ceil(showLength / 2);
    const endLength = Math.floor(showLength / 2);
    return orderNo.slice(0, startLength) + separator + '...' + separator + orderNo.slice(-endLength);
  }
  
  return orderNo;
}

/**
 * 格式化状态文本
 * @param {string|number} status - 状态值
 * @param {object} statusMap - 状态映射
 * @param {object} options - 格式化选项
 * @returns {object} 格式化后的状态对象
 */
export function formatStatus(status, statusMap = {}, options = {}) {
  const {
    defaultText = '未知',
    defaultType = 'info'
  } = options;
  
  const statusConfig = statusMap[status] || {
    text: defaultText,
    type: defaultType
  };
  
  return {
    text: statusConfig.text || defaultText,
    type: statusConfig.type || defaultType,
    color: statusConfig.color || getStatusColor(statusConfig.type || defaultType),
    icon: statusConfig.icon || getStatusIcon(statusConfig.type || defaultType)
  };
}

// 获取状态颜色
function getStatusColor(type) {
  const colorMap = {
    primary: '#409eff',
    success: '#67c23a',
    warning: '#e6a23c',
    danger: '#f56c6c',
    info: '#909399'
  };
  return colorMap[type] || colorMap.info;
}

// 获取状态图标
function getStatusIcon(type) {
  const iconMap = {
    primary: 'info-circle',
    success: 'check-circle',
    warning: 'warning',
    danger: 'close-circle',
    info: 'info-circle'
  };
  return iconMap[type] || iconMap.info;
}

/**
 * 格式化优先级
 * @param {string|number} priority - 优先级
 * @param {object} options - 格式化选项
 * @returns {object} 格式化后的优先级对象
 */
export function formatPriority(priority, options = {}) {
  const priorityMap = {
    1: { text: '低', type: 'info', icon: 'arrow-down' },
    2: { text: '中', type: 'warning', icon: 'minus' },
    3: { text: '高', type: 'danger', icon: 'arrow-up' },
    4: { text: '紧急', type: 'danger', icon: 'bell' }
  };
  
  return formatStatus(priority, priorityMap, options);
}

/**
 * 格式化评分
 * @param {number} score - 评分
 * @param {object} options - 格式化选项
 * @returns {object} 格式化后的评分对象
 */
export function formatRating(score, options = {}) {
  const {
    maxScore = 5,
    showStars = true
  } = options;
  
  if (score === null || score === undefined) {
    return {
      score: 0,
      stars: 0,
      text: '暂无评分',
      type: 'info'
    };
  }
  
  const normalizedScore = Math.min(Math.max(score, 0), maxScore);
  const stars = Math.round(normalizedScore);
  
  let type = 'danger';
  if (normalizedScore >= 4) type = 'success';
  else if (normalizedScore >= 3) type = 'warning';
  else if (normalizedScore >= 2) type = 'info';
  
  return {
    score: normalizedScore,
    stars,
    text: `${normalizedScore.toFixed(1)}/${maxScore}`,
    type,
    showStars
  };
}

/**
 * 格式化文本长度
 * @param {string} text - 文本
 * @param {object} options - 格式化选项
 * @returns {string} 格式化后的文本
 */
export function formatTextLength(text, options = {}) {
  const {
    maxLength = 50,
    ellipsis = '...',
    showLength = true
  } = options;
  
  if (!text || typeof text !== 'string') return '';
  
  if (text.length <= maxLength) return text;
  
  const truncated = text.slice(0, maxLength - ellipsis.length) + ellipsis;
  
  return showLength ? `${truncated} (${text.length})` : truncated;
}

/**
 * 格式化标签
 * @param {array} tags - 标签数组
 * @param {object} options - 格式化选项
 * @returns {array} 格式化后的标签数组
 */
export function formatTags(tags, options = {}) {
  const {
    maxCount = 5,
    maxLength = 10,
    colorScheme = 'gradient'
  } = options;
  
  if (!Array.isArray(tags)) return [];
  
  const colors = ['primary', 'success', 'warning', 'danger', 'info'];
  
  return tags.slice(0, maxCount).map((tag, index) => {
    const text = typeof tag === 'string' ? tag : tag.text;
    const color = typeof tag === 'object' ? tag.color : colors[index % colors.length];
    
    return {
      text: text.length > maxLength ? text.slice(0, maxLength) + '...' : text,
      color,
      type: color
    };
  });
}