/**
 * 工帮帮 (GongBangBang) API适配器
 * 专门处理工邦邦工业品采购平台的API调用
 */

import request from '@/utils/request'
import { ElMessage } from 'element-plus'

export class GongBangBangAdapter {
  constructor(config) {
    this.config = config
    this.baseURL = config.baseURL
    this.apiKey = config.apiKey
    this.timeout = config.timeout || 30000
  }

  // 构建请求头
  buildHeaders() {
    return {
      'Authorization': `Bearer ${this.apiKey}`,
      'Content-Type': 'application/json',
      'X-API-Token': this.apiKey,
      'Accept': 'application/json',
      'User-Agent': 'SCRM-Procurement/1.0'
    }
  }

  // 构建请求参数
  buildParams(params = {}) {
    return {
      access_token: this.apiKey,
      timestamp: Date.now(),
      format: 'json',
      version: '2.0',
      ...params
    }
  }

  // 工帮帮专用API请求
  async request(method, endpoint, params = {}, options = {}) {
    try {
      const url = `${this.baseURL}${endpoint}`
      const headers = this.buildHeaders()
      
      const config = {
        method,
        url,
        headers,
        timeout: this.timeout,
        ...options
      }

      if (method === 'GET') {
        config.params = this.buildParams(params)
      } else {
        config.data = this.buildParams(params)
      }

      const response = await request(config)
      return this.transformGongBangBangResponse(response.data, endpoint)

    } catch (error) {
      console.error('工帮帮 API请求失败:', error)
      throw this.handleGongBangBangError(error)
    }
  }

  // 工帮帮响应数据转换
  transformGongBangBangResponse(data, endpoint) {
    // 工帮帮通常返回格式: { success: true, data: {...}, message: "success" }
    if (!data.success) {
      throw new Error(`工帮帮 API错误: ${data.message || '未知错误'}`)
    }

    switch (endpoint) {
      case '/search/products':
        return this.transformProducts(data.data)
      case '/suppliers':
        return this.transformSuppliers(data.data)
      case '/products/{id}/quote':
        return this.transformPricing(data.data)
      case '/orders':
        return this.transformOrder(data.data)
      default:
        return data.data
    }
  }

  // 产品数据转换
  transformProducts(data) {
    return {
      items: data.products?.map(product => ({
        id: product.id || product.product_id,
        name: product.title,
        description: product.description || product.detail,
        price: {
          current: parseFloat(product.price) || 0,
          currency: product.currency || 'CNY',
          priceType: product.price_type || 'unit'
        },
        supplier: {
          id: product.supplier_id,
          name: product.company_name,
          rating: product.supplier_rating || 0,
          location: product.company_location
        },
        category: {
          id: product.category_id,
          name: product.category
        },
        specifications: this.parseSpecifications(product.specs),
        images: product.images || [],
        inventory: {
          stock: product.stock || 0,
          status: product.stock_status || 'in_stock'
        },
        minOrderQuantity: product.min_order_qty || 1,
        leadTime: product.delivery_days || '7-15天',
        certifications: product.qualifications || [],
        brand: product.brand,
        model: product.model,
        origin: product.origin,
        sourceUrl: product.product_url,
        lastUpdated: new Date().toISOString()
      })) || [],
      total: data.total_count || 0,
      page: data.page_num || 1,
      pageSize: data.page_size || 20,
      filters: data.filters || {}
    }
  }

  // 规格数据解析
  parseSpecifications(specs) {
    if (!specs) return {}
    
    if (typeof specs === 'string') {
      try {
        return JSON.parse(specs)
      } catch {
        // 尝试解析为键值对
        const pairs = specs.split(',').map(s => s.trim().split(':'))
        const result = {}
        pairs.forEach(([key, value]) => {
          if (key && value) {
            result[key.trim()] = value.trim()
          }
        })
        return result
      }
    }
    
    return specs
  }

  // 供应商数据转换
  transformSuppliers(data) {
    return {
      items: data.suppliers?.map(supplier => ({
        id: supplier.supplier_id,
        name: supplier.company_name,
        rating: supplier.rating || 0,
        location: {
          province: supplier.location?.province,
          city: supplier.location?.city,
          address: supplier.location?.address,
          coordinate: supplier.location?.coordinate
        },
        contact: {
          person: supplier.contact_person,
          phone: supplier.contact_phone,
          email: supplier.contact_email,
          qq: supplier.contact_qq,
          wechat: supplier.contact_wechat
        },
        capabilities: {
          categories: supplier.categories || [],
          certifications: supplier.certifications || [],
          productionCapacity: supplier.production_capacity,
          serviceArea: supplier.service_area || []
        },
        businessInfo: {
          establishedYear: supplier.established_year,
          employeeCount: supplier.employee_count,
          annualRevenue: supplier.annual_revenue,
          mainProducts: supplier.main_products || []
        },
        performance: {
          onTimeDelivery: supplier.on_time_delivery_rate || 0,
          qualityScore: supplier.quality_score || 0,
          responseTime: supplier.avg_response_time || 0,
          orderCompletion: supplier.order_completion_rate || 0
        },
        verified: supplier.verified_status === 'verified',
        sourceUrl: supplier.supplier_url,
        lastUpdated: new Date().toISOString()
      })) || [],
      total: data.total_count || 0,
      page: data.page_num || 1,
      pageSize: data.page_size || 50
    }
  }

  // 价格数据转换
  transformPricing(data) {
    return {
      items: data.quotes?.map(quote => ({
        productId: quote.product_id,
        supplierId: quote.supplier_id,
        price: {
          unit: parseFloat(quote.unit_price) || 0,
          total: parseFloat(quote.total_price) || 0,
          currency: quote.currency || 'CNY'
        },
        quantity: quote.quantity || 1,
        validUntil: quote.valid_until,
        moq: quote.min_order_qty || 1,
        tierPricing: quote.tier_pricing || [],
        terms: {
          paymentTerms: quote.payment_terms,
          deliveryTerms: quote.delivery_terms,
          warrantyTerms: quote.warranty_terms
        },
        quoteInfo: {
          quoteId: quote.quote_id,
          validDays: quote.valid_days,
          remarks: quote.remarks
        },
        lastUpdated: new Date().toISOString()
      })) || []
    }
  }

  // 订单数据转换
  transformOrder(data) {
    return {
      orderId: data.order_id,
      status: data.order_status,
      items: data.order_items || [],
      totalAmount: parseFloat(data.total_amount) || 0,
      currency: data.currency || 'CNY',
      paymentMethod: data.payment_method,
      deliveryInfo: {
        address: data.delivery_address,
        estimatedDelivery: data.estimated_delivery_date,
        shippingMethod: data.shipping_method,
        shippingCost: parseFloat(data.shipping_cost) || 0
      },
      supplier: {
        id: data.supplier_id,
        name: data.supplier_name
      },
      createdAt: data.create_time,
      updatedAt: data.update_time,
      trackingNumber: data.tracking_number,
      sourceUrl: data.order_url,
      remarks: data.remarks
    }
  }

  // 工帮帮错误处理
  handleGongBangBangError(error) {
    const errorMap = {
      400: '请求参数错误',
      401: 'API令牌无效',
      403: '访问权限不足',
      404: '资源不存在',
      429: 'API调用频率超限',
      500: '工帮帮服务器内部错误',
      503: '工帮帮服务暂时不可用'
    }

    const status = error.response?.status
    const message = errorMap[status] || error.message || '工帮帮 API未知错误'
    
    return new Error(`[工帮帮] ${message}`)
  }

  // 搜索产品
  async searchProducts(keyword, params = {}) {
    return this.request('GET', '/search/products', {
      keyword,
      page_size: params.limit || 20,
      page_num: params.page || 1,
      category_id: params.categoryId,
      brand: params.brand,
      price_min: params.minPrice,
      price_max: params.maxPrice,
      supplier_rating: params.minRating,
      location: params.location,
      sort_by: params.sortBy || 'relevance',
      sort_order: params.sortOrder || 'desc'
    })
  }

  // 获取供应商列表
  async getSuppliers(params = {}) {
    return this.request('GET', '/suppliers', {
      category_id: params.categoryId,
      location: params.location,
      rating_min: params.minRating,
      verified_only: params.verifiedOnly || false,
      page_size: params.limit || 50,
      page_num: params.page || 1
    })
  }

  // 获取产品价格
  async getPricing(productId, quantity = 1) {
    return this.request('GET', `/products/${productId}/quote`, {
      quantity
    })
  }

  // 创建采购订单
  async createOrder(orderData) {
    return this.request('POST', '/orders', {
      supplier_id: orderData.supplierId,
      items: orderData.items,
      delivery_address: orderData.deliveryAddress,
      payment_method: orderData.paymentMethod,
      remarks: orderData.remarks
    })
  }

  // 获取订单状态
  async getOrderStatus(orderId) {
    return this.request('GET', `/orders/${orderId}/status`)
  }

  // 获取产品详情
  async getProductDetail(productId) {
    return this.request('GET', `/products/${productId}/detail`)
  }

  // 获取供应商详情
  async getSupplierDetail(supplierId) {
    return this.request('GET', `/suppliers/${supplierId}/detail`)
  }

  // 批量询价
  async requestQuotes(productIds, quantity) {
    return this.request('POST', '/quotes/batch', {
      product_ids: productIds,
      quantity
    })
  }

  // 获取询价历史
  async getQuoteHistory(params = {}) {
    return this.request('GET', '/quotes/history', {
      page_size: params.limit || 20,
      page_num: params.page || 1,
      start_date: params.startDate,
      end_date: params.endDate
    })
  }

  // 健康检查
  async healthCheck() {
    try {
      await this.request('GET', '/health')
      return { status: 'healthy', platform: '工帮帮' }
    } catch (error) {
      return { 
        status: 'unhealthy', 
        platform: '工帮帮',
        error: error.message 
      }
    }
  }
}