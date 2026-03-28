/**
 * B2B商品目录相关API
 * 支持批发价格、阶梯定价、商品展示等功能
 */

import request from '@/utils/request.js';

/**
 * 获取B2B商品列表
 * @param {Object} params - 查询参数
 * @param {number} params.current - 当前页码
 * @param {number} params.pageSize - 每页条数
 * @param {string} params.product_name - 商品名称
 * @param {string} params.category_id - 分类ID
 * @param {string} params.brand - 品牌
 * @param {string} params.status - 商品状态
 * @param {string} params.price_range - 价格区间
 * @param {string} params.customer_level - 客户等级(用于获取对应价格)
 * @returns {Promise} 返回商品列表数据
 */
export function getB2BProductList(params) {
  return request({
    url: '/api/b2b/product/list',
    method: 'GET',
    params
  });
}

/**
 * 获取B2B商品详情
 * @param {number} productId - 商品ID
 * @param {string} customerLevel - 客户等级
 * @returns {Promise} 返回商品详细信息
 */
export function getB2BProductDetail(productId, customerLevel = null) {
  const params = {};
  if (customerLevel) {
    params.customer_level = customerLevel;
  }
  return request({
    url: `/api/b2b/product/${productId}`,
    method: 'GET',
    params
  });
}

/**
 * 获取商品阶梯价格
 * @param {number} productId - 商品ID
 * @param {string} customerLevel - 客户等级
 * @returns {Promise} 返回阶梯价格信息
 */
export function getProductTieredPricing(productId, customerLevel = null) {
  const params = {};
  if (customerLevel) {
    params.customer_level = customerLevel;
  }
  return request({
    url: `/api/b2b/product/${productId}/tiered-pricing`,
    method: 'GET',
    params
  });
}

/**
 * 获取商品批发价格
 * @param {number} productId - 商品ID
 * @param {number} quantity - 购买数量
 * @param {string} customerLevel - 客户等级
 * @returns {Promise} 返回批发价格信息
 */
export function getWholesalePrice(productId, quantity, customerLevel = null) {
  const params = { quantity };
  if (customerLevel) {
    params.customer_level = customerLevel;
  }
  return request({
    url: `/api/b2b/product/${productId}/wholesale-price`,
    method: 'GET',
    params
  });
}

/**
 * 获取商品分类列表
 * @param {string} parentId - 父分类ID
 * @returns {Promise} 返回分类列表
 */
export function getProductCategoryList(parentId = null) {
  const params = {};
  if (parentId) {
    params.parent_id = parentId;
  }
  return request({
    url: '/api/b2b/product/category',
    method: 'GET',
    params
  });
}

/**
 * 获取商品品牌列表
 * @returns {Promise} 返回品牌列表
 */
export function getProductBrandList() {
  return request({
    url: '/api/b2b/product/brand',
    method: 'GET'
  });
}

/**
 * 获取商品库存信息
 * @param {number} productId - 商品ID
 * @returns {Promise} 返回库存信息
 */
export function getProductInventory(productId) {
  return request({
    url: `/api/b2b/product/${productId}/inventory`,
    method: 'GET'
  });
}

/**
 * 批量获取商品库存
 * @param {Array} productIds - 商品ID数组
 * @returns {Promise} 返回库存信息列表
 */
export function batchGetProductInventory(productIds) {
  return request({
    url: '/api/b2b/product/inventory/batch',
    method: 'POST',
    data: { product_ids: productIds }
  });
}

/**
 * 获取商品价格策略
 * @param {number} productId - 商品ID
 * @returns {Promise} 返回价格策略信息
 */
export function getProductPriceStrategy(productId) {
  return request({
    url: `/api/b2b/product/${productId}/price-strategy`,
    method: 'GET'
  });
}

/**
 * 获取客户等级价格
 * @param {number} productId - 商品ID
 * @param {string} customerLevel - 客户等级
 * @returns {Promise} 返回客户等级价格
 */
export function getCustomerLevelPrice(productId, customerLevel) {
  return request({
    url: `/api/b2b/product/${productId}/customer-level-price`,
    method: 'GET',
    params: { customer_level: customerLevel }
  });
}

/**
 * 获取商品规格参数
 * @param {number} productId - 商品ID
 * @returns {Promise} 返回商品规格参数
 */
export function getProductSpecifications(productId) {
  return request({
    url: `/api/b2b/product/${productId}/specifications`,
    method: 'GET'
  });
}

/**
 * 搜索商品
 * @param {Object} params - 搜索参数
 * @param {string} params.keyword - 搜索关键词
 * @param {string} params.category - 分类
 * @param {string} params.brand - 品牌
 * @param {string} params.price_range - 价格区间
 * @param {string} params.customer_level - 客户等级
 * @returns {Promise} 返回搜索结果
 */
export function searchProducts(params) {
  return request({
    url: '/api/b2b/product/search',
    method: 'GET',
    params
  });
}

/**
 * 获取推荐商品
 * @param {string} customerLevel - 客户等级
 * @param {number} limit - 推荐数量
 * @returns {Promise} 返回推荐商品列表
 */
export function getRecommendedProducts(customerLevel = null, limit = 10) {
  const params = { limit };
  if (customerLevel) {
    params.customer_level = customerLevel;
  }
  return request({
    url: '/api/b2b/product/recommendations',
    method: 'GET',
    params
  });
}

/**
 * 获取热销商品
 * @param {string} customerLevel - 客户等级
 * @param {number} limit - 数量限制
 * @param {string} period - 统计周期(week/month/quarter)
 * @returns {Promise} 返回热销商品列表
 */
export function getHotProducts(customerLevel = null, limit = 10, period = 'month') {
  const params = { limit, period };
  if (customerLevel) {
    params.customer_level = customerLevel;
  }
  return request({
    url: '/api/b2b/product/hot',
    method: 'GET',
    params
  });
}

/**
 * 获取新品推荐
 * @param {string} customerLevel - 客户等级
 * @param {number} limit - 数量限制
 * @param {number} days - 新品天数
 * @returns {Promise} 返回新品列表
 */
export function getNewProducts(customerLevel = null, limit = 10, days = 30) {
  const params = { limit, days };
  if (customerLevel) {
    params.customer_level = customerLevel;
  }
  return request({
    url: '/api/b2b/product/new',
    method: 'GET',
    params
  });
}

/**
 * 获取商品评价
 * @param {number} productId - 商品ID
 * @param {Object} params - 查询参数
 * @returns {Promise} 返回商品评价列表
 */
export function getProductReviews(productId, params = {}) {
  return request({
    url: `/api/b2b/product/${productId}/reviews`,
    method: 'GET',
    params
  });
}

/**
 * 获取商品图片
 * @param {number} productId - 商品ID
 * @returns {Promise} 返回商品图片列表
 */
export function getProductImages(productId) {
  return request({
    url: `/api/b2b/product/${productId}/images`,
    method: 'GET'
  });
}

/**
 * 获取商品价格历史
 * @param {number} productId - 商品ID
 * @param {Object} params - 查询参数
 * @param {string} params.start_date - 开始日期
 * @param {string} params.end_date - 结束日期
 * @param {string} params.customer_level - 客户等级
 * @returns {Promise} 返回价格历史数据
 */
export function getProductPriceHistory(productId, params = {}) {
  return request({
    url: `/api/b2b/product/${productId}/price-history`,
    method: 'GET',
    params
  });
}