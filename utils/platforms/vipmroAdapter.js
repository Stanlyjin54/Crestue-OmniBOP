/**
 * 京东工品汇 (VIPMRO) API适配器
 * 专门处理京东工业品采购平台的API调用
 */

import request from '@/utils/request'
import { ElMessage } from 'element-plus'

export class VIPMROAdapter {
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
      'X-API-Key': this.apiKey,
      'Accept': 'application/json',
      'User-Agent': 'SCRM-Procurement/1.0'
    }
  }

  // 构建请求参数
  buildParams(params = {}) {
    const defaultParams = {
      app_key: this.apiKey,
      timestamp: Date.now(),
      format: 'json',
      v: '1.0',
      ...params
    }
    return defaultParams
  }

  // VIPMRO专用API请求
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
      return this.transformVIPMROResponse(response.data, endpoint)

    } catch (error) {
      console.error('VIPMRO API请求失败:', error)
      throw this.handleVIPMROError(error)
    }
  }

  // VIPMRO响应数据转换
  transformVIPMROResponse(data, endpoint) {
    // VIPMRO通常返回格式: { code: 200, msg: "success", data: {...} }
    if (data.code !== 200) {
      throw new Error(`VIPMRO API错误: ${data.msg || '未知错误'}`)
    }

    switch (endpoint) {
      case '/products/search':
        return this.transformProducts(data.data)
      case '/suppliers/list':
        return this.transformSuppliers(data.data)
      case '/products/{id}/pricing':
        return this.transformPricing(data.data)
      case '/orders/create':
        return this.transformOrder(data.data)
      default:
        return data.data
    }
  }

  // 产品数据转换
  transformProducts(data) {
    return {
      items: data.products?.map(product => ({
        id: product.product_id,
        name: product.product_title,
        description: product.product_desc,
        price: {
          current: parseFloat(product.price_info.current_price) || 0,
          original: parseFloat(product.price_info.original_price) || 0,
          currency: product.price_info.currency || 'CNY'
        },
        supplier: {
          id: product.supplier_info.supplier_id,
          name: product.supplier_info.supplier_name,
          rating: product.supplier_info.supplier_rating || 0
        },
        category: {
          id: product.category_info.category_id,
          name: product.category_info.category_name
        },
        specifications: product.specifications || {},
        images: product.images || [],
        inventory: {
          stock: product.stock_info.available_stock || 0,
          reserved: product.stock_info.reserved_stock || 0
        },
        minOrderQuantity: product.moq || 1,
        leadTime: product.delivery_time || '7-15天',
        certifications: product.certification_info || [],
        brand: product.brand_info?.brand_name,
        model: product.model,
        sourceUrl: product.product_url,
        lastUpdated: new Date().toISOString()
      })) || [],
      total: data.total_count || 0,
      page: data.page_num || 1,
      pageSize: data.page_size || 20
    }
  }

  // 供应商数据转换
  transformSuppliers(data) {
    return {
      items: data.suppliers?.map(supplier => ({
        id: supplier.supplier_id,
        name: supplier.company_name,
        rating: supplier.supplier_rating || 0,
        location: {
          province: supplier.location.province,
          city: supplier.location.city,
          district: supplier.location.district,
          address: supplier.location.address
        },
        contact: {
          person: supplier.contact_person,
          phone: supplier.contact_phone,
          email: supplier.contact_email
        },
        capabilities: {
          categories: supplier.capability_info?.categories || [],
          certifications: supplier.capability_info?.certifications || [],
          serviceArea: supplier.capability_info?.service_area || []
        },
        performance: {
          onTimeDelivery: supplier.performance_info?.on_time_delivery_rate || 0,
          qualityScore: supplier.performance_info?.quality_score || 0,
          responseTime: supplier.performance_info?.avg_response_time || 0
        },
        verified: supplier.verified_status === 'verified',
        sourceUrl: supplier.supplier_url,
        lastUpdated: new Date().toISOString()
      })) || [],
      total: data.total_count || 0
    }
  }

  // 价格数据转换
  transformPricing(data) {
    return {
      items: data.pricing?.map(price => ({
        productId: price.product_id,
        supplierId: price.supplier_id,
        price: {
          unit: parseFloat(price.unit_price) || 0,
          total: parseFloat(price.total_price) || 0,
          currency: price.currency || 'CNY'
        },
        quantity: price.quantity || 1,
        validUntil: price.valid_until,
        moq: price.moq || 1,
        tierPricing: price.tier_pricing || [],
        discount: price.discount_info || {},
        deliveryInfo: {
          leadTime: price.delivery_time,
          shippingCost: parseFloat(price.shipping_cost) || 0,
          freeShippingThreshold: price.free_shipping_threshold
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
        shippingMethod: data.shipping_method
      },
      createdAt: data.create_time,
      updatedAt: data.update_time,
      trackingNumber: data.tracking_number,
      sourceUrl: data.order_url
    }
  }

  // VIPMRO错误处理
  handleVIPMROError(error) {
    const errorMap = {
      400: '请求参数错误',
      401: 'API密钥无效',
      403: '访问权限不足',
      404: '资源不存在',
      429: 'API调用频率超限',
      500: 'VIPMRO服务器内部错误',
      503: 'VIPMRO服务暂时不可用'
    }

    const status = error.response?.status
    const message = errorMap[status] || error.message || 'VIPMRO API未知错误'
    
    return new Error(`[VIPMRO] ${message}`)
  }

  // 搜索产品
  async searchProducts(keyword, params = {}) {
    return this.request('GET', '/products/search', {
      keyword,
      page_size: params.limit || 20,
      page_num: params.page || 1,
      category_id: params.categoryId,
      brand: params.brand,
      price_range: params.priceRange,
      supplier_rating: params.minRating,
      sort_by: params.sortBy || 'relevance',
      sort_order: params.sortOrder || 'desc'
    })
  }

  // 获取供应商列表
  async getSuppliers(params = {}) {
    return this.request('GET', '/suppliers/list', {
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
    return this.request('GET', `/products/${productId}/pricing`, {
      quantity,
      supplier_id: params.supplierId
    })
  }

  // 创建采购订单
  async createOrder(orderData) {
    return this.request('POST', '/orders/create', {
      items: orderData.items,
      delivery_address: orderData.deliveryAddress,
      payment_method: orderData.paymentMethod,
      supplier_id: orderData.supplierId,
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
  async requestQuotes(productIds, quantity, supplierId) {
    return this.request('POST', '/quotes/batch', {
      product_ids: productIds,
      quantity,
      supplier_id: supplierId
    })
  }

  // 健康检查
  async healthCheck() {
    try {
      await this.request('GET', '/health')
      return { status: 'healthy', platform: 'VIPMRO' }
    } catch (error) {
      return { 
        status: 'unhealthy', 
        platform: 'VIPMRO',
        error: error.message 
      }
    }
  }
}