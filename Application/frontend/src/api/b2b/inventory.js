/**
 * B2B库存管理相关API
 * 实现实时库存同步和预警机制
 */

import { request } from '@/utils/request.js';

/**
 * 获取库存列表
 * @param {Object} params - 查询参数
 * @param {number} params.current - 当前页码
 * @param {number} params.pageSize - 每页条数
 * @param {string} params.product_name - 商品名称
 * @param {string} params.category_id - 分类ID
 * @param {string} params.warehouse_id - 仓库ID
 * @param {string} params.low_stock - 是否低库存(true/false)
 * @param {string} params.out_of_stock - 是否缺货(true/false)
 * @returns {Promise} 返回库存列表数据
 */
export function getInventoryList(params) {
  return request({
    url: '/api/b2b/inventory/list',
    method: 'GET',
    params
  });
}

/**
 * 获取库存详情
 * @param {number} inventoryId - 库存ID
 * @returns {Promise} 返回库存详细信息
 */
export function getInventoryDetail(inventoryId) {
  return request({
    url: `/api/b2b/inventory/${inventoryId}`,
    method: 'GET'
  });
}

/**
 * 获取商品库存信息
 * @param {number} productId - 商品ID
 * @param {string} warehouseId - 仓库ID(可选)
 * @returns {Promise} 返回商品库存信息
 */
export function getProductInventory(productId, warehouseId = null) {
  const params = {};
  if (warehouseId) {
    params.warehouse_id = warehouseId;
  }
  return request({
    url: `/api/b2b/inventory/product/${productId}`,
    method: 'GET',
    params
  });
}

/**
 * 批量获取商品库存
 * @param {Array} productIds - 商品ID数组
 * @param {string} warehouseId - 仓库ID(可选)
 * @returns {Promise} 返回库存信息列表
 */
export function batchGetProductInventory(productIds, warehouseId = null) {
  const data = { product_ids: productIds };
  if (warehouseId) {
    data.warehouse_id = warehouseId;
  }
  return request({
    url: '/api/b2b/inventory/batch',
    method: 'POST',
    data
  });
}

/**
 * 更新库存数量
 * @param {number} inventoryId - 库存ID
 * @param {Object} data - 更新数据
 * @param {number} data.quantity - 库存数量
 * @param {string} data.update_type - 更新类型(set/increase/decrease)
 * @param {string} data.remark - 备注
 * @returns {Promise} 返回更新结果
 */
export function updateInventoryQuantity(inventoryId, data) {
  return request({
    url: `/api/b2b/inventory/${inventoryId}/quantity`,
    method: 'PUT',
    data
  });
}

/**
 * 调整库存
 * @param {Object} data - 调整数据
 * @param {number} data.product_id - 商品ID
 * @param {string} data.warehouse_id - 仓库ID
 * @param {number} data.adjust_quantity - 调整数量
 * @param {string} data.adjust_type - 调整类型(increase/decrease)
 * @param {string} data.reason - 调整原因
 * @param {string} data.remark - 备注
 * @returns {Promise} 返回调整结果
 */
export function adjustInventory(data) {
  return request({
    url: '/api/b2b/inventory/adjust',
    method: 'POST',
    data
  });
}

/**
 * 获取库存变动记录
 * @param {Object} params - 查询参数
 * @param {number} params.current - 当前页码
 * @param {number} params.pageSize - 每页条数
 * @param {number} params.product_id - 商品ID
 * @param {string} params.warehouse_id - 仓库ID
 * @param {string} params.change_type - 变动类型
 * @param {string} params.start_date - 开始日期
 * @param {string} params.end_date - 结束日期
 * @returns {Promise} 返回库存变动记录
 */
export function getInventoryChangeHistory(params) {
  return request({
    url: '/api/b2b/inventory/changes',
    method: 'GET',
    params
  });
}

/**
 * 获取库存预警设置
 * @returns {Promise} 返回预警设置
 */
export function getInventoryAlertSettings() {
  return request({
    url: '/api/b2b/inventory/alert-settings',
    method: 'GET'
  });
}

/**
 * 更新库存预警设置
 * @param {Object} data - 预警设置数据
 * @returns {Promise} 返回更新结果
 */
export function updateInventoryAlertSettings(data) {
  return request({
    url: '/api/b2b/inventory/alert-settings',
    method: 'PUT',
    data
  });
}

/**
 * 获取库存预警列表
 * @param {Object} params - 查询参数
 * @param {number} params.current - 当前页码
 * @param {number} params.pageSize - 每页条数
 * @param {string} params.alert_type - 预警类型(low_stock/out_of_stock/over_stock)
 * @param {string} params.status - 处理状态
 * @returns {Promise} 返回预警列表
 */
export function getInventoryAlerts(params) {
  return request({
    url: '/api/b2b/inventory/alerts',
    method: 'GET',
    params
  });
}

/**
 * 处理库存预警
 * @param {number} alertId - 预警ID
 * @param {Object} data - 处理数据
 * @param {string} data.action - 处理动作
 * @param {string} data.remark - 备注
 * @returns {Promise} 返回处理结果
 */
export function handleInventoryAlert(alertId, data) {
  return request({
    url: `/api/b2b/inventory/alerts/${alertId}/handle`,
    method: 'POST',
    data
  });
}

/**
 * 获取仓库列表
 * @returns {Promise} 返回仓库列表
 */
export function getWarehouseList() {
  return request({
    url: '/api/b2b/inventory/warehouse',
    method: 'GET'
  });
}

/**
 * 获取仓库详情
 * @param {string} warehouseId - 仓库ID
 * @returns {Promise} 返回仓库详情
 */
export function getWarehouseDetail(warehouseId) {
  return request({
    url: `/api/b2b/inventory/warehouse/${warehouseId}`,
    method: 'GET'
  });
}

/**
 * 获取商品在不同仓库的库存
 * @param {number} productId - 商品ID
 * @returns {Promise} 返回多仓库库存信息
 */
export function getProductInventoryByWarehouse(productId) {
  return request({
    url: `/api/b2b/inventory/product/${productId}/warehouses`,
    method: 'GET'
  });
}

/**
 * 库存调拨
 * @param {Object} data - 调拨数据
 * @param {number} data.product_id - 商品ID
 * @param {string} data.from_warehouse_id - 源仓库ID
 * @param {string} data.to_warehouse_id - 目标仓库ID
 * @param {number} data.quantity - 调拨数量
 * @param {string} data.reason - 调拨原因
 * @param {string} data.remark - 备注
 * @returns {Promise} 返回调拨结果
 */
export function transferInventory(data) {
  return request({
    url: '/api/b2b/inventory/transfer',
    method: 'POST',
    data
  });
}

/**
 * 获取库存统计
 * @param {Object} params - 统计参数
 * @param {string} params.warehouse_id - 仓库ID(可选)
 * @param {string} params.category_id - 分类ID(可选)
 * @param {string} params.start_date - 开始日期
 * @param {string} params.end_date - 结束日期
 * @returns {Promise} 返回库存统计数据
 */
export function getInventoryStats(params = {}) {
  return request({
    url: '/api/b2b/inventory/stats',
    method: 'GET',
    params
  });
}

/**
 * 获取低库存商品列表
 * @param {Object} params - 查询参数
 * @param {number} params.current - 当前页码
 * @param {number} params.pageSize - 每页条数
 * @param {string} params.warehouse_id - 仓库ID(可选)
 * @returns {Promise} 返回低库存商品列表
 */
export function getLowStockProducts(params = {}) {
  return request({
    url: '/api/b2b/inventory/low-stock',
    method: 'GET',
    params
  });
}

/**
 * 获取缺货商品列表
 * @param {Object} params - 查询参数
 * @param {number} params.current - 当前页码
 * @param {number} params.pageSize - 每页条数
 * @param {string} params.warehouse_id - 仓库ID(可选)
 * @returns {Promise} 返回缺货商品列表
 */
export function getOutOfStockProducts(params = {}) {
  return request({
    url: '/api/b2b/inventory/out-of-stock',
    method: 'GET',
    params
  });
}

/**
 * 获取库存周转率
 * @param {Object} params - 查询参数
 * @param {string} params.start_date - 开始日期
 * @param {string} params.end_date - 结束日期
 * @param {string} params.warehouse_id - 仓库ID(可选)
 * @param {string} params.category_id - 分类ID(可选)
 * @returns {Promise} 返回库存周转率数据
 */
export function getInventoryTurnover(params = {}) {
  return request({
    url: '/api/b2b/inventory/turnover',
    method: 'GET',
    params
  });
}

/**
 * 获取安全库存建议
 * @param {number} productId - 商品ID
 * @param {string} warehouseId - 仓库ID
 * @returns {Promise} 返回安全库存建议
 */
export function getSafetyStockRecommendation(productId, warehouseId) {
  return request({
    url: `/api/b2b/inventory/product/${productId}/safety-stock`,
    method: 'GET',
    params: { warehouse_id: warehouseId }
  });
}

/**
 * 设置安全库存
 * @param {number} productId - 商品ID
 * @param {string} warehouseId - 仓库ID
 * @param {Object} data - 安全库存数据
 * @param {number} data.min_stock - 最小库存
 * @param {number} data.max_stock - 最大库存
 * @param {number} data.reorder_point - 再订货点
 * @param {number} data.reorder_quantity - 再订货数量
 * @returns {Promise} 返回设置结果
 */
export function setSafetyStock(productId, warehouseId, data) {
  return request({
    url: `/api/b2b/inventory/product/${productId}/safety-stock`,
    method: 'PUT',
    data: { ...data, warehouse_id: warehouseId }
  });
}

/**
 * 库存盘点
 * @param {Object} data - 盘点数据
 * @param {string} data.warehouse_id - 仓库ID
 * @param {Array} data.items - 盘点项
 * @param {string} data.remark - 备注
 * @returns {Promise} 返回盘点结果
 */
export function inventoryCheck(data) {
  return request({
    url: '/api/b2b/inventory/check',
    method: 'POST',
    data
  });
}

/**
 * 获取库存盘点记录
 * @param {Object} params - 查询参数
 * @param {number} params.current - 当前页码
 * @param {number} params.pageSize - 每页条数
 * @param {string} params.warehouse_id - 仓库ID
 * @param {string} params.start_date - 开始日期
 * @param {string} params.end_date - 结束日期
 * @returns {Promise} 返回盘点记录
 */
export function getInventoryCheckRecords(params) {
  return request({
    url: '/api/b2b/inventory/check-records',
    method: 'GET',
    params
  });
}