/**
 * B2B价格策略相关API
 * 实现B2B动态定价和促销策略
 */

import { request } from '@/utils/request.js';

/**
 * 获取价格策略列表
 * @param {Object} params - 查询参数
 * @param {number} params.current - 当前页码
 * @param {number} params.pageSize - 每页条数
 * @param {string} params.strategy_name - 策略名称
 * @param {string} params.strategy_type - 策略类型
 * @param {string} params.status - 策略状态
 * @param {string} params.start_date - 开始日期
 * @param {string} params.end_date - 结束日期
 * @returns {Promise} 返回价格策略列表
 */
export function getPriceStrategyList(params) {
  return request({
    url: '/api/b2b/price-strategy/list',
    method: 'GET',
    params
  });
}

/**
 * 获取价格策略详情
 * @param {number} strategyId - 策略ID
 * @returns {Promise} 返回价格策略详情
 */
export function getPriceStrategyDetail(strategyId) {
  return request({
    url: `/api/b2b/price-strategy/${strategyId}`,
    method: 'GET'
  });
}

/**
 * 创建价格策略
 * @param {Object} data - 策略数据
 * @param {string} data.strategy_name - 策略名称
 * @param {string} data.strategy_type - 策略类型(discount/markup/tiered/dynamic)
 * @param {string} data.description - 策略描述
 * @param {number} data.priority - 优先级
 * @param {string} data.start_date - 开始日期
 * @param {string} data.end_date - 结束日期
 * @param {string} data.status - 策略状态
 * @param {Array} data.conditions - 策略条件
 * @param {Array} data.actions - 策略动作
 * @returns {Promise} 返回创建结果
 */
export function createPriceStrategy(data) {
  return request({
    url: '/api/b2b/price-strategy',
    method: 'POST',
    data
  });
}

/**
 * 更新价格策略
 * @param {number} strategyId - 策略ID
 * @param {Object} data - 更新数据
 * @returns {Promise} 返回更新结果
 */
export function updatePriceStrategy(strategyId, data) {
  return request({
    url: `/api/b2b/price-strategy/${strategyId}`,
    method: 'PUT',
    data
  });
}

/**
 * 删除价格策略
 * @param {number} strategyId - 策略ID
 * @returns {Promise} 返回删除结果
 */
export function deletePriceStrategy(strategyId) {
  return request({
    url: `/api/b2b/price-strategy/${strategyId}`,
    method: 'DELETE'
  });
}

/**
 * 启用价格策略
 * @param {number} strategyId - 策略ID
 * @returns {Promise} 返回启用结果
 */
export function enablePriceStrategy(strategyId) {
  return request({
    url: `/api/b2b/price-strategy/${strategyId}/enable`,
    method: 'POST'
  });
}

/**
 * 禁用价格策略
 * @param {number} strategyId - 策略ID
 * @returns {Promise} 返回禁用结果
 */
export function disablePriceStrategy(strategyId) {
  return request({
    url: `/api/b2b/price-strategy/${strategyId}/disable`,
    method: 'POST'
  });
}

/**
 * 计算商品价格
 * @param {Object} data - 计算参数
 * @param {number} data.product_id - 商品ID
 * @param {number} data.quantity - 购买数量
 * @param {number} data.customer_id - 客户ID
 * @param {string} data.customer_level - 客户等级
 * @param {string} data.order_type - 订单类型
 * @param {string} data.channel - 销售渠道
 * @returns {Promise} 返回计算结果
 */
export function calculateProductPrice(data) {
  return request({
    url: '/api/b2b/price-strategy/calculate',
    method: 'POST',
    data
  });
}

/**
 * 批量计算商品价格
 * @param {Array} products - 商品数组
 * @returns {Promise} 返回批量计算结果
 */
export function batchCalculatePrice(products) {
  return request({
    url: '/api/b2b/price-strategy/batch-calculate',
    method: 'POST',
    data: { products }
  });
}

/**
 * 获取促销策略列表
 * @param {Object} params - 查询参数
 * @returns {Promise} 返回促销策略列表
 */
export function getPromotionStrategyList(params) {
  return request({
    url: '/api/b2b/price-strategy/promotion/list',
    method: 'GET',
    params
  });
}

/**
 * 创建促销策略
 * @param {Object} data - 促销数据
 * @returns {Promise} 返回创建结果
 */
export function createPromotionStrategy(data) {
  return request({
    url: '/api/b2b/price-strategy/promotion',
    method: 'POST',
    data
  });
}

/**
 * 获取客户等级价格策略
 * @param {string} customerLevel - 客户等级
 * @returns {Promise} 返回客户等级价格策略
 */
export function getCustomerLevelPriceStrategy(customerLevel) {
  return request({
    url: `/api/b2b/price-strategy/customer-level/${customerLevel}`,
    method: 'GET'
  });
}

/**
 * 获取渠道价格策略
 * @param {string} channel - 销售渠道
 * @returns {Promise} 返回渠道价格策略
 */
export function getChannelPriceStrategy(channel) {
  return request({
    url: `/api/b2b/price-strategy/channel/${channel}`,
    method: 'GET'
  });
}

/**
 * 获取阶梯定价策略
 * @param {number} productId - 商品ID
 * @returns {Promise} 返回阶梯定价策略
 */
export function getTieredPricingStrategy(productId) {
  return request({
    url: `/api/b2b/price-strategy/tiered/${productId}`,
    method: 'GET'
  });
}

/**
 * 创建阶梯定价策略
 * @param {number} productId - 商品ID
 * @param {Object} data - 阶梯定价数据
 * @returns {Promise} 返回创建结果
 */
export function createTieredPricingStrategy(productId, data) {
  return request({
    url: `/api/b2b/price-strategy/tiered/${productId}`,
    method: 'POST',
    data
  });
}

/**
 * 获取动态定价策略
 * @param {number} productId - 商品ID
 * @returns {Promise} 返回动态定价策略
 */
export function getDynamicPricingStrategy(productId) {
  return request({
    url: `/api/b2b/price-strategy/dynamic/${productId}`,
    method: 'GET'
  });
}

/**
 * 创建动态定价策略
 * @param {number} productId - 商品ID
 * @param {Object} data - 动态定价数据
 * @returns {Promise} 返回创建结果
 */
export function createDynamicPricingStrategy(productId, data) {
  return request({
    url: `/api/b2b/price-strategy/dynamic/${productId}`,
    method: 'POST',
    data
  });
}

/**
 * 获取价格策略类型枚举
 * @returns {Promise} 返回策略类型枚举
 */
export function getPriceStrategyTypeEnum() {
  return request({
    url: '/api/b2b/price-strategy/types',
    method: 'GET'
  });
}

/**
 * 获取价格策略状态枚举
 * @returns {Promise} 返回策略状态枚举
 */
export function getPriceStrategyStatusEnum() {
  return request({
    url: '/api/b2b/price-strategy/status-enum',
    method: 'GET'
  });
}

/**
 * 获取价格条件类型枚举
 * @returns {Promise} 返回条件类型枚举
 */
export function getPriceConditionTypeEnum() {
  return request({
    url: '/api/b2b/price-strategy/condition-types',
    method: 'GET'
  });
}

/**
 * 获取价格动作类型枚举
 * @returns {Promise} 返回动作类型枚举
 */
export function getPriceActionTypeEnum() {
  return request({
    url: '/api/b2b/price-strategy/action-types',
    method: 'GET'
  });
}

/**
 * 获取价格策略统计
 * @param {Object} params - 统计参数
 * @param {string} params.start_date - 开始日期
 * @param {string} params.end_date - 结束日期
 * @returns {Promise} 返回策略统计数据
 */
export function getPriceStrategyStats(params = {}) {
  return request({
    url: '/api/b2b/price-strategy/stats',
    method: 'GET',
    params
  });
}