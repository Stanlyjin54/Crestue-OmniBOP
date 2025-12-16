/**
 * 第三方供应商API适配器
 * 支持多种供应商API的统一接入和管理
 */

import request from '@/utils/request'
import { ElMessage } from 'element-plus'

class SupplierAPIClient {
  constructor(config) {
    this.config = config
    this.baseURL = config.baseURL
    this.apiKey = config.apiKey
    this.timeout = config.timeout || 30000
    this.rateLimit = config.rateLimit || { requests: 100, period: 'hour' }
    
    // API调用计数
    this.callCount = 0
    this.periodStart = Date.now()
    
    // 数据缓存
    this.cache = new Map()
    this.cacheTimeout = config.cacheTimeout || 300000 // 5分钟
  }

  // 检查频率限制
  checkRateLimit() {
    const now = Date.now()
    const periodMs = this.rateLimit.period === 'hour' ? 3600000 : 86400000
    
    if (now - this.periodStart > periodMs) {
      this.callCount = 0
      this.periodStart = now
    }
    
    if (this.callCount >= this.rateLimit.requests) {
      throw new Error(`API调用频率超限: ${this.rateLimit.requests}/${this.rateLimit.period}`)
    }
    
    this.callCount++
  }

  // 获取缓存键
  getCacheKey(method, url, params) {
    return `${method}:${url}:${JSON.stringify(params || {})}`
  }

  // 缓存设置
  setCache(key, data) {
    this.cache.set(key, {
      data,
      timestamp: Date.now()
    })
  }

  // 缓存获取
  getCache(key) {
    const cached = this.cache.get(key)
    if (!cached) return null
    
    if (Date.now() - cached.timestamp > this.cacheTimeout) {
      this.cache.delete(key)
      return null
    }
    
    return cached.data
  }

  // 通用API请求方法
  async request(method, url, params = {}, options = {}) {
    try {
      this.checkRateLimit()
      
      const cacheKey = this.getCacheKey(method, url, params)
      const cached = this.getCache(cacheKey)
      if (cached && !options.forceRefresh) {
        return cached
      }

      const config = {
        method,
        url: `${this.baseURL}${url}`,
        timeout: this.timeout,
        headers: {
          'Authorization': `Bearer ${this.apiKey}`,
          'Content-Type': 'application/json',
          ...options.headers
        },
        ...options
      }

      if (method === 'GET') {
        config.params = params
      } else {
        config.data = params
      }

      const response = await request(config)
      const result = this.transformResponse(response.data, url)
      
      // 缓存成功响应
      if (response.status === 200) {
        this.setCache(cacheKey, result)
      }
      
      return result
      
    } catch (error) {
      console.error(`API请求失败 [${this.config.source}]:`, error)
      throw this.handleError(error)
    }
  }

  // 错误处理
  handleError(error) {
    const errorMap = {
      401: 'API密钥无效或已过期',
      403: 'API访问权限不足',
      404: '请求的资源不存在',
      429: 'API调用频率超限',
      500: '供应商服务器内部错误',
      503: '供应商服务暂时不可用'
    }

    const message = errorMap[error.response?.status] || error.message || '未知错误'
    return new Error(`[${this.config.source}] ${message}`)
  }

  // 响应数据转换
  transformResponse(data, endpoint) {
    const transformers = {
      '/products': this.transformProducts.bind(this),
      '/suppliers': this.transformSuppliers.bind(this),
      '/pricing': this.transformPricing.bind(this),
      '/orders': this.transformOrders.bind(this)
    }

    const transformer = transformers[endpoint] || this.transformDefault.bind(this)
    return transformer(data)
  }

  // 默认数据转换
  transformDefault(data) {
    return data
  }

  // 产品数据转换
  transformProducts(rawData) {
    return rawData.items?.map(item => ({
      id: item.id || item.product_id,
      name: item.title || item.name,
      description: item.description || item.details,
      price: this.parsePrice(item.price || item.amount),
      currency: item.currency || 'CNY',
      supplier: {
        id: item.supplier_id,
        name: item.supplier_name,
        rating: item.supplier_rating || 0,
        location: item.supplier_location
      },
      category: this.mapCategory(item.category || item.cat_name),
      specifications: this.standardizeSpecifications(item.specs || item.specifications),
      images: item.images || item.pictures || [],
      minOrderQuantity: item.moq || item.min_order_qty || 1,
      leadTime: item.lead_time || item.delivery_time,
      certifications: item.certifications || [],
      source: this.config.source,
      sourceUrl: item.url || item.product_url,
      lastUpdated: new Date().toISOString()
    })) || []
  }

  // 供应商数据转换
  transformSuppliers(rawData) {
    return rawData.items?.map(item => ({
      id: item.id || item.supplier_id,
      name: item.company_name || item.name,
      rating: item.rating || item.score || 0,
      location: item.location || item.address,
      contact: {
        person: item.contact_person,
        phone: item.phone || item.tel,
        email: item.email || item.mail,
        website: item.website
      },
      certifications: item.certifications || [],
      categories: item.categories || [],
      capabilities: item.capabilities || [],
      source: this.config.source,
      verified: item.verified || false,
      lastUpdated: new Date().toISOString()
    })) || []
  }

  // 价格数据转换
  transformPricing(rawData) {
    return rawData.items?.map(item => ({
      productId: item.product_id,
      supplierId: item.supplier_id,
      price: this.parsePrice(item.price),
      currency: item.currency || 'CNY',
      quantity: item.quantity,
      validUntil: item.valid_until,
      moq: item.moq || 1,
      leadTime: item.lead_time,
      source: this.config.source,
      lastUpdated: new Date().toISOString()
    })) || []
  }

  // 订单数据转换
  transformOrders(rawData) {
    return rawData.items?.map(item => ({
      id: item.order_id,
      supplierId: item.supplier_id,
      products: item.items || [],
      totalAmount: this.parsePrice(item.total_amount),
      currency: item.currency || 'CNY',
      status: item.status,
      orderDate: item.order_date,
      deliveryDate: item.delivery_date,
      source: this.config.source,
      trackingNumber: item.tracking_number
    })) || []
  }

  // 价格解析
  parsePrice(price) {
    if (typeof price === 'string') {
      return parseFloat(price.replace(/[^0-9.-]/g, ''))
    }
    return parseFloat(price) || 0
  }

  // 分类映射
  mapCategory(category) {
    const categoryMap = {
      'electronics': '电子产品',
      'office': '办公用品',
      'construction': '建筑工程',
      'manufacturing': '制造业',
      'services': '服务类'
    }
    return categoryMap[category] || category || '其他'
  }

  // 规格标准化
  standardizeSpecifications(specs) {
    if (!specs) return {}
    
    if (typeof specs === 'string') {
      try {
        return JSON.parse(specs)
      } catch {
        return { description: specs }
      }
    }
    
    return specs
  }

  // 获取产品列表
  async getProducts(params = {}) {
    return this.request('GET', '/products', params)
  }

  // 获取产品详情
  async getProductDetail(id) {
    return this.request('GET', `/products/${id}`)
  }

  // 获取供应商列表
  async getSuppliers(params = {}) {
    return this.request('GET', '/suppliers', params)
  }

  // 获取供应商详情
  async getSupplierDetail(id) {
    return this.request('GET', `/suppliers/${id}`)
  }

  // 获取价格信息
  async getPricing(params = {}) {
    return this.request('GET', '/pricing', params)
  }

  // 搜索产品
  async searchProducts(keyword, params = {}) {
    return this.request('GET', '/search/products', { keyword, ...params })
  }

  // 批量询价
  async requestQuotes(productIds, quantity) {
    return this.request('POST', '/quotes/batch', {
      product_ids: productIds,
      quantity
    })
  }

  // 下单
  async placeOrder(orderData) {
    return this.request('POST', '/orders', orderData)
  }

  // 获取订单状态
  async getOrderStatus(orderId) {
    return this.request('GET', `/orders/${orderId}/status`)
  }
}

// 供应商API管理器
export class SupplierAPIManager {
  constructor() {
    this.clients = new Map()
    this.configs = this.loadConfigs()
    this.initializeClients()
  }

  // 加载配置
  loadConfigs() {
    return [
      {
        source: '1688',
        name: '阿里巴巴1688',
        baseURL: 'https://api.1688.com',
        apiKey: process.env.VUE_APP_1688_API_KEY,
        timeout: 30000,
        rateLimit: { requests: 1000, period: 'hour' },
        cacheTimeout: 300000,
        mappings: {
          productName: 'title',
          price: 'price',
          supplierName: 'company_name'
        }
      },
      {
        source: 'vipmro',
        name: '京东工品汇',
        baseURL: 'https://api.vipmro.com/v1',
        apiKey: process.env.VUE_APP_VIPMRO_API_KEY,
        timeout: 30000,
        rateLimit: { requests: 500, period: 'hour' },
        cacheTimeout: 300000,
        adapter: 'vipmro', // 指定专用适配器
        mappings: {
          productName: 'product_title',
          price: 'price_info.current_price',
          supplierName: 'supplier_info.supplier_name',
          category: 'category_info.category_name',
          specifications: 'specifications',
          minOrderQuantity: 'moq',
          leadTime: 'delivery_time',
          certifications: 'certification_info'
        }
      },
      {
        source: 'gongbangbang',
        name: '工帮帮',
        baseURL: 'https://api.gongbangbang.com/v2',
        apiKey: process.env.VUE_APP_GBB_API_TOKEN,
        timeout: 30000,
        rateLimit: { requests: 300, period: 'hour' },
        cacheTimeout: 300000,
        adapter: 'gongbangbang', // 指定专用适配器
        mappings: {
          productName: 'title',
          price: 'price',
          supplierName: 'company_name',
          category: 'category',
          specifications: 'specs',
          images: 'images',
          minOrderQuantity: 'min_order_qty',
          leadTime: 'delivery_days',
          certifications: 'qualifications'
        }
      },
      {
        source: 'ccgp',
        name: '中国政府采购网',
        baseURL: 'http://api.ccgp.gov.cn',
        apiKey: process.env.VUE_APP_CCGp_API_KEY,
        timeout: 30000,
        rateLimit: { requests: 100, period: 'hour' },
        cacheTimeout: 600000,
        mappings: {
          productName: 'product_name',
          price: 'budget_amount',
          supplierName: 'winner_name'
        }
      },
      {
        source: 'jd_enterprise',
        name: '京东企业购',
        baseURL: 'https://api.jd.com/enterprise',
        apiKey: process.env.VUE_APP_JD_API_KEY,
        timeout: 30000,
        rateLimit: { requests: 500, period: 'hour' },
        cacheTimeout: 300000
      }
    ]
  }

  // 初始化客户端
  initializeClients() {
    this.configs.forEach(config => {
      try {
        this.clients.set(config.source, new SupplierAPIClient(config))
      } catch (error) {
        console.error(`初始化API客户端失败 [${config.source}]:`, error)
      }
    })
  }

  // 获取可用数据源
  getAvailableSources() {
    return Array.from(this.clients.keys()).map(key => {
      const client = this.clients.get(key)
      const config = this.configs.find(c => c.source === key)
      return {
        source: key,
        name: config?.name || key,
        available: !!client
      }
    })
  }

  // 跨平台产品搜索
  async searchProductsAcrossPlatforms(keyword, params = {}) {
    const results = []
    const errors = []

    for (const [source, client] of this.clients) {
      try {
        const products = await client.searchProducts(keyword, params)
        results.push({
          source,
          products,
          count: products.length
        })
      } catch (error) {
        errors.push({
          source,
          error: error.message
        })
      }
    }

    return {
      results,
      errors,
      totalResults: results.reduce((sum, r) => sum + r.count, 0)
    }
  }

  // 获取产品比较
  async compareProduct(productName, quantity = 1) {
    const comparison = []

    for (const [source, client] of this.clients) {
      try {
        const products = await client.searchProducts(productName, { limit: 5 })
        const pricing = await client.getPricing({ product_name: productName, quantity })
        
        comparison.push({
          source,
          products: products.slice(0, 3), // 取前3个产品
          pricing: pricing.slice(0, 3)
        })
      } catch (error) {
        comparison.push({
          source,
          error: error.message,
          products: [],
          pricing: []
        })
      }
    }

    return this.analyzeComparison(comparison)
  }

  // 分析比较结果
  analyzeComparison(comparison) {
    const allProducts = []
    const priceAnalysis = []

    comparison.forEach(sourceData => {
      if (sourceData.error) return

      sourceData.products.forEach(product => {
        allProducts.push({
          ...product,
          platform: sourceData.source
        })
      })

      sourceData.pricing.forEach(price => {
        priceAnalysis.push({
          ...price,
          platform: sourceData.source
        })
      })
    })

    // 价格分析
    const sortedByPrice = priceAnalysis.sort((a, b) => a.price - b.price)
    const averagePrice = priceAnalysis.reduce((sum, p) => sum + p.price, 0) / priceAnalysis.length

    return {
      totalProducts: allProducts.length,
      products: allProducts,
      priceAnalysis: {
        lowest: sortedByPrice[0],
        highest: sortedByPrice[sortedByPrice.length - 1],
        average: averagePrice,
        all: sortedByPrice
      },
      platforms: comparison.length,
      errors: comparison.filter(c => c.error).length
    }
  }

  // 智能推荐供应商
  async recommendSuppliers(requirements) {
    const recommendations = []

    for (const [source, client] of this.clients) {
      try {
        const suppliers = await client.getSuppliers(requirements)
        const scored = suppliers.map(supplier => ({
          ...supplier,
          score: this.calculateSupplierScore(supplier, requirements),
          platform: source
        }))
        
        recommendations.push(...scored)
      } catch (error) {
        console.error(`获取供应商推荐失败 [${source}]:`, error)
      }
    }

    // 按评分排序
    return recommendations
      .sort((a, b) => b.score - a.score)
      .slice(0, 10)
  }

  // 计算供应商评分
  calculateSupplierScore(supplier, requirements) {
    let score = 0

    // 基础评分
    score += (supplier.rating || 0) * 0.3

    // 分类匹配
    if (supplier.categories?.includes(requirements.category)) {
      score += 20
    }

    // 地理位置评分
    if (supplier.location?.includes(requirements.region)) {
      score += 15
    }

    // 认证评分
    score += (supplier.certifications?.length || 0) * 5

    // 验证状态评分
    if (supplier.verified) {
      score += 10
    }

    return Math.min(score, 100)
  }

  // 批量同步数据
  async syncData(source, dataType) {
    const client = this.clients.get(source)
    if (!client) {
      throw new Error(`数据源 ${source} 不可用`)
    }

    const syncResults = []

    try {
      switch (dataType) {
        case 'products':
          const products = await client.getProducts({ limit: 100 })
          syncResults.push(...products)
          break

        case 'suppliers':
          const suppliers = await client.getSuppliers({ limit: 100 })
          syncResults.push(...suppliers)
          break

        case 'pricing':
          const pricing = await client.getPricing({ limit: 100 })
          syncResults.push(...pricing)
          break
      }

      ElMessage.success(`${source} ${dataType} 数据同步成功，共 ${syncResults.length} 条记录`)
      return syncResults

    } catch (error) {
      ElMessage.error(`${source} ${dataType} 数据同步失败: ${error.message}`)
      throw error
    }
  }

  // 健康检查
  async healthCheck() {
    const healthStatus = {}

    for (const [source, client] of this.clients) {
      try {
        // 简单的API调用测试
        await client.request('GET', '/health', {}, { timeout: 5000 })
        healthStatus[source] = {
          status: 'healthy',
          latency: '< 5s'
        }
      } catch (error) {
        healthStatus[source] = {
          status: 'unhealthy',
          error: error.message
        }
      }
    }

    return healthStatus
  }
}

// 单例导出
export default new SupplierAPIManager()