// 安全工具函数

// 加密工具
export class CryptoUtil {
  constructor() {
    this.algorithm = 'AES-GCM'
    this.keyLength = 256
  }

  // 生成随机密钥
  async generateKey() {
    return await window.crypto.subtle.generateKey(
      {
        name: this.algorithm,
        length: this.keyLength
      },
      true,
      ['encrypt', 'decrypt']
    )
  }

  // 加密数据
  async encrypt(data, key) {
    const encoder = new TextEncoder()
    const dataBuffer = encoder.encode(JSON.stringify(data))
    
    const iv = window.crypto.getRandomValues(new Uint8Array(12))
    
    const encrypted = await window.crypto.subtle.encrypt(
      {
        name: this.algorithm,
        iv: iv
      },
      key,
      dataBuffer
    )

    return {
      encrypted: Array.from(new Uint8Array(encrypted)),
      iv: Array.from(iv)
    }
  }

  // 解密数据
  async decrypt(encryptedData, key) {
    const { encrypted, iv } = encryptedData
    
    const decrypted = await window.crypto.subtle.decrypt(
      {
        name: this.algorithm,
        iv: new Uint8Array(iv)
      },
      key,
      new Uint8Array(encrypted)
    )

    const decoder = new TextDecoder()
    const jsonString = decoder.decode(decrypted)
    return JSON.parse(jsonString)
  }

  // 导出密钥
  async exportKey(key) {
    const exported = await window.crypto.subtle.exportKey('raw', key)
    return Array.from(new Uint8Array(exported))
  }

  // 导入密钥
  async importKey(keyData) {
    return await window.crypto.subtle.importKey(
      'raw',
      new Uint8Array(keyData),
      {
        name: this.algorithm,
        length: this.keyLength
      },
      true,
      ['encrypt', 'decrypt']
    )
  }
}

// 简单的哈希函数（用于非密码用途）
export function simpleHash(str) {
  let hash = 0
  if (str.length === 0) return hash
  
  for (let i = 0; i < str.length; i++) {
    const char = str.charCodeAt(i)
    hash = ((hash << 5) - hash) + char
    hash = hash & hash // 转换为32位整数
  }
  
  return hash.toString(36)
}

// 数据脱敏
export function desensitizeData(data, type = 'default') {
  if (!data) return ''
  
  switch (type) {
    case 'phone':
      return data.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2')
    
    case 'email':
      const [localPart, domain] = data.split('@')
      if (localPart.length <= 3) {
        return `${localPart}****@${domain}`
      }
      return `${localPart.slice(0, 3)}****@${domain}`
    
    case 'idCard':
      return data.replace(/(\d{4})\d{10}(\w{4})/, '$1**********$2')
    
    case 'bankCard':
      return data.replace(/(\d{4})\d{8,12}(\d{4})/, '$1****$2')
    
    case 'name':
      if (data.length <= 2) {
        return data.charAt(0) + '*'
      }
      return data.charAt(0) + '*'.repeat(data.length - 2) + data.charAt(data.length - 1)
    
    default:
      if (data.length <= 4) {
        return '*'.repeat(data.length)
      }
      return data.slice(0, 2) + '*'.repeat(data.length - 4) + data.slice(-2)
  }
}

// XSS防护
export function sanitizeInput(input) {
  if (typeof input !== 'string') return input
  
  const map = {
    '&': '&amp;',
    '<': '&lt;',
    '>': '&gt;',
    '"': '&quot;',
    "'": '&#x27;',
    '/': '&#x2F;'
  }
  
  const reg = /[&<>"'/]/gi
  return input.replace(reg, (match) => map[match])
}

// SQL注入防护（基础）
export function sanitizeSQL(input) {
  if (typeof input !== 'string') return input
  
  // 移除SQL关键字和特殊字符
  const sqlKeywords = [
    'SELECT', 'INSERT', 'UPDATE', 'DELETE', 'DROP', 'CREATE', 'ALTER',
    'EXEC', 'UNION', 'SCRIPT', 'DECLARE', 'TRUNCATE', 'GRANT', 'REVOKE'
  ]
  
  let sanitized = input
  
  // 转义特殊字符
  sanitized = sanitized.replace(/['"`;\\]/g, '')
  
  // 移除SQL关键字（不区分大小写）
  sqlKeywords.forEach(keyword => {
    const regex = new RegExp(keyword, 'gi')
    sanitized = sanitized.replace(regex, '')
  })
  
  return sanitized.trim()
}

// CSRF令牌生成
export function generateCSRFToken() {
  const array = new Uint8Array(32)
  window.crypto.getRandomValues(array)
  return Array.from(array, byte => byte.toString(16).padStart(2, '0')).join('')
}

// 验证文件类型
export function validateFileType(file, allowedTypes) {
  if (!file || !allowedTypes || !Array.isArray(allowedTypes)) {
    return false
  }
  
  const fileExtension = file.name.split('.').pop().toLowerCase()
  const fileType = file.type.toLowerCase()
  
  return allowedTypes.some(type => {
    if (type.startsWith('.')) {
      return fileExtension === type.slice(1).toLowerCase()
    }
    return fileType.includes(type.toLowerCase())
  })
}

// 验证文件大小
export function validateFileSize(file, maxSizeInMB) {
  if (!file || !maxSizeInMB) {
    return false
  }
  
  const maxSizeInBytes = maxSizeInMB * 1024 * 1024
  return file.size <= maxSizeInBytes
}

// 安全下载文件
export function secureDownload(url, filename) {
  // 验证URL安全性
  try {
    const urlObj = new URL(url)
    if (!['http:', 'https:'].includes(urlObj.protocol)) {
      throw new Error('不安全的协议')
    }
  } catch (error) {
    console.error('URL验证失败:', error)
    return
  }
  
  // 创建安全的下载链接
  const link = document.createElement('a')
  link.href = url
  link.download = filename || 'download'
  link.rel = 'noopener noreferrer'
  link.target = '_blank'
  
  // 添加点击事件监听器
  link.addEventListener('click', (event) => {
    // 阻止默认行为
    event.preventDefault()
    
    // 验证域名白名单（可选）
    const allowedDomains = ['your-domain.com', 'trusted-domain.com']
    const urlDomain = new URL(url).hostname
    
    if (allowedDomains.length > 0 && !allowedDomains.some(domain => urlDomain.includes(domain))) {
      console.error('域名不在白名单中')
      return
    }
    
    // 执行下载
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
  })
  
  // 触发点击
  link.click()
}

// 权限验证
export class PermissionValidator {
  constructor() {
    this.permissions = new Set()
  }

  setPermissions(permissions) {
    this.permissions = new Set(permissions)
  }

  hasPermission(permission) {
    return this.permissions.has(permission)
  }

  hasAnyPermission(...permissions) {
    return permissions.some(permission => this.hasPermission(permission))
  }

  hasAllPermissions(...permissions) {
    return permissions.every(permission => this.hasPermission(permission))
  }

  validateRoute(to, from, next) {
    const requiredPermission = to.meta?.requiredPermission
    
    if (!requiredPermission) {
      next()
      return
    }

    if (this.hasPermission(requiredPermission)) {
      next()
    } else {
      next('/403')
    }
  }
}

// JWT工具
export class JWTUtil {
  static decode(token) {
    try {
      const base64Url = token.split('.')[1]
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
      const jsonPayload = decodeURIComponent(
        atob(base64)
          .split('')
          .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
          .join('')
      )
      
      return JSON.parse(jsonPayload)
    } catch (error) {
      console.error('JWT解码失败:', error)
      return null
    }
  }

  static isExpired(token) {
    const payload = this.decode(token)
    if (!payload || !payload.exp) {
      return true
    }
    
    const currentTime = Math.floor(Date.now() / 1000)
    return payload.exp < currentTime
  }

  static getExpirationTime(token) {
    const payload = this.decode(token)
    if (!payload || !payload.exp) {
      return null
    }
    
    return payload.exp * 1000 // 转换为毫秒
  }
}

// 输入验证
export class InputValidator {
  static email(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    return emailRegex.test(email)
  }

  static phone(phone) {
    const phoneRegex = /^1[3-9]\d{9}$/
    return phoneRegex.test(phone)
  }

  static idCard(idCard) {
    const idCardRegex = /^[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{3}[\dX]$/
    return idCardRegex.test(idCard)
  }

  static bankCard(bankCard) {
    const bankCardRegex = /^\d{16,19}$/
    return bankCardRegex.test(bankCard)
  }

  static password(password) {
    // 至少8位，包含大小写字母、数字和特殊字符
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/
    return passwordRegex.test(password)
  }

  static url(url) {
    try {
      new URL(url)
      return true
    } catch {
      return false
    }
  }

  static ip(ip) {
    const ipRegex = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/
    return ipRegex.test(ip)
  }
}

// 安全存储
export class SecureStorage {
  constructor(storageKey = 'secure_data') {
    this.storageKey = storageKey
    this.crypto = new CryptoUtil()
    this.key = null
  }

  async init() {
    // 尝试从本地存储获取密钥，如果没有则生成新的
    const storedKey = localStorage.getItem(`${this.storageKey}_key`)
    
    if (storedKey) {
      try {
        const keyData = JSON.parse(storedKey)
        this.key = await this.crypto.importKey(keyData)
      } catch (error) {
        console.error('密钥导入失败:', error)
        this.key = await this.generateAndStoreKey()
      }
    } else {
      this.key = await this.generateAndStoreKey()
    }
  }

  async generateAndStoreKey() {
    const key = await this.crypto.generateKey()
    const keyData = await this.crypto.exportKey(key)
    localStorage.setItem(`${this.storageKey}_key`, JSON.stringify(keyData))
    return key
  }

  async set(key, value) {
    if (!this.key) {
      await this.init()
    }

    try {
      const encrypted = await this.crypto.encrypt(value, this.key)
      localStorage.setItem(`${this.storageKey}_${key}`, JSON.stringify(encrypted))
      return true
    } catch (error) {
      console.error('数据加密失败:', error)
      return false
    }
  }

  async get(key) {
    if (!this.key) {
      await this.init()
    }

    try {
      const encryptedData = localStorage.getItem(`${this.storageKey}_${key}`)
      if (!encryptedData) {
        return null
      }

      const parsedData = JSON.parse(encryptedData)
      return await this.crypto.decrypt(parsedData, this.key)
    } catch (error) {
      console.error('数据解密失败:', error)
      return null
    }
  }

  remove(key) {
    localStorage.removeItem(`${this.storageKey}_${key}`)
  }

  clear() {
    // 清除所有相关数据
    const keys = []
    for (let i = 0; i < localStorage.length; i++) {
      const key = localStorage.key(i)
      if (key && key.startsWith(this.storageKey)) {
        keys.push(key)
      }
    }
    keys.forEach(key => localStorage.removeItem(key))
  }
}

// 使用示例
export const cryptoUtil = new CryptoUtil()
export const permissionValidator = new PermissionValidator()
export const secureStorage = new SecureStorage()