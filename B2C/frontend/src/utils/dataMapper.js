/**
 * 数据映射工具
 * 统一处理不同供应商平台的数据格式，转换为B2C标准
 */

export class DataMapper {
  // 主映射方法
  static mapProductData(sourceData, platform) {
    try {
      switch (platform) {
        case 'vipmro':
          return this.mapVIPMROData(sourceData)
        case 'gongbangbang':
          return this.mapGongBangBangData(sourceData)
        case 'b2c':
        default:
          return this.mapB2CData(sourceData)
      }
    } catch (error) {
      console.error(`数据映射失败 [${platform}]:`, error)
      throw new Error(`数据映射失败: ${error.message}`)
    }
  }

  // VIPMRO数据映射
  static mapVIPMROData(data) {
    return {
      // 基础字段映射
      id: data.id || data.product_id,
      name: data.name || data.product_title || data.product_name,
      description: data.description || data.product_desc || '',
      
      // 价格信息映射
      price: this.mapPriceData(data.price || data.price_info),
      originalPrice: data.originalPrice || data.price_info?.original_price || data.price?.original,
      
      // 库存信息映射
      stock: data.stock || data.inventory?.stock || data.stock_info?.available_stock || 0,
      sales: data.sales || data.sales_info?.total_sales || 0,
      
      // 供应商信息映射
      supplier: this.mapSupplierData(data.supplier || data.supplier_info),
      
      // 分类信息映射
      category: this.mapCategoryData(data.category || data.category_info),
      
      // 图片信息映射
      images: this.mapImageData(data.images || data.product_images),
      mainImage: data.mainImage || (data.images && data.images[0]) || data.product_images?.[0],
      
      // 规格信息映射
      specifications: data.specifications || {},
      
      // 工业品特色信息映射
      minOrderQuantity: data.minOrderQuantity || data.moq || 1,
      leadTime: data.leadTime || data.delivery_time || '7-15天',
      certifications: data.certifications || data.certification_info || [],
      
      // 品牌信息映射
      brand: data.brand || data.brand_info?.brand_name,
      model: data.model,
      
      // 来源信息
      source: 'vipmro',
      sourceUrl: data.sourceUrl || data.product_url,
      lastUpdated: data.lastUpdated || new Date().toISOString()
    }
  }

  // 工帮帮数据映射
  static mapGongBangBangData(data) {
    return {
      // 基础字段映射
      id: data.id || data.product_id,
      name: data.name || data.title,
      description: data.description || data.detail || '',
      
      // 价格信息映射
      price: this.mapPriceData(data.price),
      originalPrice: data.originalPrice,
      
      // 库存信息映射
      stock: data.stock || data.inventory?.stock || 0,
      sales: data.sales || 0,
      
      // 供应商信息映射
      supplier: this.mapSupplierData(data.supplier || {
        id: data.supplier_id,
        name: data.company_name,
        rating: data.supplier_rating,
        location: data.company_location
      }),
      
      // 分类信息映射
      category: this.mapCategoryData(data.category || {
        id: data.category_id,
        name: data.category
      }),
      
      // 图片信息映射
      images: this.mapImageData(data.images),
      mainImage: data.mainImage || (data.images && data.images[0]),
      
      // 规格信息映射
      specifications: this.parseSpecifications(data.specs) || data.specifications || {},
      
      // 工业品特色信息映射
      minOrderQuantity: data.minOrderQuantity || data.min_order_qty || 1,
      leadTime: data.leadTime || data.delivery_days || '7-15天',
      certifications: data.certifications || data.qualifications || [],
      
      // 品牌信息映射
      brand: data.brand,
      model: data.model,
      origin: data.origin,
      
      // 来源信息
      source: 'gongbangbang',
      sourceUrl: data.sourceUrl || data.product_url,
      lastUpdated: data.lastUpdated || new Date().toISOString()
    }
  }

  // B2C标准数据映射
  static mapB2CData(data) {
    return {
      id: data.id,
      name: data.name,
      description: data.description,
      price: data.price,
      originalPrice: data.originalPrice,
      stock: data.stock,
      sales: data.sales,
      supplier: data.supplier,
      category: data.category,
      images: data.images,
      mainImage: data.mainImage || (data.images && data.images[0]),
      specifications: data.specifications,
      minOrderQuantity: data.minOrderQuantity,
      leadTime: data.leadTime,
      certifications: data.certifications,
      brand: data.brand,
      model: data.model,
      source: 'b2c',
      lastUpdated: data.lastUpdated || new Date().toISOString()
    }
  }

  // 价格数据映射
  static mapPriceData(priceData) {
    if (!priceData) return { current: 0, original: 0, currency: 'CNY' }
    
    // 如果是简单数字
    if (typeof priceData === 'number') {
      return { current: priceData, original: 0, currency: 'CNY' }
    }
    
    // 如果是对象
    const current = parseFloat(priceData.current || priceData.price || 0)
    const original = parseFloat(priceData.original || priceData.original_price || 0)
    
    return {
      current,
      original: original || current,
      currency: priceData.currency || 'CNY',
      priceType: priceData.priceType || 'unit',
      tierPricing: priceData.tierPricing || []
    }
  }

  // 供应商数据映射
  static mapSupplierData(supplierData) {
    if (!supplierData) return null
    
    return {
      id: supplierData.id || supplierData.supplier_id,
      name: supplierData.name || supplierData.supplier_name || supplierData.company_name,
      rating: parseFloat(supplierData.rating || supplierData.supplier_rating || 0),
      location: this.mapLocationData(supplierData.location),
      contact: supplierData.contact,
      verified: supplierData.verified || false
    }
  }

  // 地理位置数据映射
  static mapLocationData(locationData) {
    if (!locationData) return null
    
    if (typeof locationData === 'string') {
      return { address: locationData }
    }
    
    return {
      province: locationData.province,
      city: locationData.city,
      district: locationData.district,
      address: locationData.address,
      coordinate: locationData.coordinate
    }
  }

  // 分类数据映射
  static mapCategoryData(categoryData) {
    if (!categoryData) return null
    
    if (typeof categoryData === 'string') {
      return { id: null, name: categoryData }
    }
    
    return {
      id: categoryData.id || categoryData.category_id,
      name: categoryData.name || categoryData.category_name || categoryData.category
    }
  }

  // 图片数据映射
  static mapImageData(imagesData) {
    if (!imagesData) return []
    
    if (typeof imagesData === 'string') {
      return [imagesData]
    }
    
    if (Array.isArray(imagesData)) {
      return imagesData.filter(img => typeof img === 'string')
    }
    
    return []
  }

  // 规格数据解析
  static parseSpecifications(specs) {
    if (!specs) return {}
    
    if (typeof specs === 'object') {
      return specs
    }
    
    if (typeof specs === 'string') {
      try {
        // 尝试JSON解析
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
    
    return {}
  }

  // 批量数据映射
  static async mapBatchData(sourceDataArray, platform, onProgress = null) {
    const results = []
    const total = sourceDataArray.length
    
    for (let i = 0; i < sourceDataArray.length; i++) {
      try {
        const mappedData = this.mapProductData(sourceDataArray[i], platform)
        results.push(mappedData)
        
        if (onProgress) {
          onProgress({
            current: i + 1,
            total,
            percentage: Math.round(((i + 1) / total) * 100),
            item: mappedData
          })
        }
      } catch (error) {
        console.error(`批量映射第${i + 1}项失败:`, error)
        // 跳过失败的项目，继续处理
      }
    }
    
    return results
  }

  // 数据验证
  static validateProductData(productData) {
    const errors = []
    
    if (!productData.id) {
      errors.push('缺少商品ID')
    }
    
    if (!productData.name) {
      errors.push('缺少商品名称')
    }
    
    if (!productData.price || productData.price.current <= 0) {
      errors.push('价格信息无效')
    }
    
    if (productData.stock < 0) {
      errors.push('库存信息无效')
    }
    
    return {
      valid: errors.length === 0,
      errors
    }
  }

  // 数据清洗
  static cleanProductData(productData) {
    return {
      ...productData,
      name: (productData.name || '').trim(),
      description: (productData.description || '').trim(),
      price: {
        ...productData.price,
        current: Math.max(0, parseFloat(productData.price?.current || 0)),
        original: Math.max(0, parseFloat(productData.price?.original || productData.price?.current || 0))
      },
      stock: Math.max(0, parseInt(productData.stock || 0)),
      sales: Math.max(0, parseInt(productData.sales || 0)),
      minOrderQuantity: Math.max(1, parseInt(productData.minOrderQuantity || 1))
    }
  }
}

export default DataMapper