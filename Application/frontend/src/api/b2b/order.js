/**
 * B2B订单管理相关API
 * 支持批量下单、询价、合同管理等功能
 */

import { request } from '@/utils/request.js';

/**
 * 创建B2B订单
 * @param {Object} data - 订单数据
 * @param {string} data.order_type - 订单类型(standard/inquiry/contract)
 * @param {number} data.customer_id - 客户ID
 * @param {Array} data.items - 订单项
 * @param {string} data.delivery_date - 期望交付日期
 * @param {string} data.payment_terms - 支付条款
 * @param {string} data.remark - 备注信息
 * @param {string} data.contract_no - 合同编号(可选)
 * @returns {Promise} 返回创建结果
 */
export function createB2BOrder(data) {
  return request({
    url: '/api/b2b/order',
    method: 'POST',
    data
  });
}

/**
 * 获取B2B订单列表
 * @param {Object} params - 查询参数
 * @param {number} params.current - 当前页码
 * @param {number} params.pageSize - 每页条数
 * @param {string} params.order_no - 订单编号
 * @param {number} params.customer_id - 客户ID
 * @param {string} params.order_status - 订单状态
 * @param {string} params.order_type - 订单类型
 * @param {string} params.start_date - 开始日期
 * @param {string} params.end_date - 结束日期
 * @param {string} params.payment_status - 支付状态
 * @returns {Promise} 返回订单列表数据
 */
export function getB2BOrderList(params) {
  return request({
    url: '/api/b2b/order/list',
    method: 'GET',
    params
  });
}

/**
 * 获取B2B订单详情
 * @param {number} orderId - 订单ID
 * @returns {Promise} 返回订单详细信息
 */
export function getB2BOrderDetail(orderId) {
  return request({
    url: `/api/b2b/order/${orderId}`,
    method: 'GET'
  });
}

/**
 * 更新B2B订单
 * @param {number} orderId - 订单ID
 * @param {Object} data - 更新数据
 * @returns {Promise} 返回更新结果
 */
export function updateB2BOrder(orderId, data) {
  return request({
    url: `/api/b2b/order/${orderId}`,
    method: 'PUT',
    data
  });
}

/**
 * 取消B2B订单
 * @param {number} orderId - 订单ID
 * @param {string} reason - 取消原因
 * @returns {Promise} 返回取消结果
 */
export function cancelB2BOrder(orderId, reason) {
  return request({
    url: `/api/b2b/order/${orderId}/cancel`,
    method: 'POST',
    data: { reason }
  });
}

/**
 * 确认B2B订单
 * @param {number} orderId - 订单ID
 * @returns {Promise} 返回确认结果
 */
export function confirmB2BOrder(orderId) {
  return request({
    url: `/api/b2b/order/${orderId}/confirm`,
    method: 'POST'
  });
}

/**
 * 完成B2B订单
 * @param {number} orderId - 订单ID
 * @returns {Promise} 返回完成结果
 */
export function completeB2BOrder(orderId) {
  return request({
    url: `/api/b2b/order/${orderId}/complete`,
    method: 'POST'
  });
}

/**
 * 创建询价单
 * @param {Object} data - 询价数据
 * @param {number} data.customer_id - 客户ID
 * @param {Array} data.items - 询价项
 * @param {string} data.delivery_date - 期望交付日期
 * @param {string} data.remark - 备注信息
 * @returns {Promise} 返回创建结果
 */
export function createInquiry(data) {
  return request({
    url: '/api/b2b/order/inquiry',
    method: 'POST',
    data
  });
}

/**
 * 获取询价单列表
 * @param {Object} params - 查询参数
 * @returns {Promise} 返回询价单列表
 */
export function getInquiryList(params) {
  return request({
    url: '/api/b2b/order/inquiry/list',
    method: 'GET',
    params
  });
}

/**
 * 获取询价单详情
 * @param {number} inquiryId - 询价单ID
 * @returns {Promise} 返回询价单详情
 */
export function getInquiryDetail(inquiryId) {
  return request({
    url: `/api/b2b/order/inquiry/${inquiryId}`,
    method: 'GET'
  });
}

/**
 * 回复询价单
 * @param {number} inquiryId - 询价单ID
 * @param {Object} data - 回复数据
 * @param {Array} data.items - 报价项
 * @param {string} data.remark - 备注
 * @param {number} data.valid_days - 有效期天数
 * @returns {Promise} 返回回复结果
 */
export function replyInquiry(inquiryId, data) {
  return request({
    url: `/api/b2b/order/inquiry/${inquiryId}/reply`,
    method: 'POST',
    data
  });
}

/**
 * 确认询价单
 * @param {number} inquiryId - 询价单ID
 * @returns {Promise} 返回确认结果
 */
export function confirmInquiry(inquiryId) {
  return request({
    url: `/api/b2b/order/inquiry/${inquiryId}/confirm`,
    method: 'POST'
  });
}

/**
 * 拒绝询价单
 * @param {number} inquiryId - 询价单ID
 * @param {string} reason - 拒绝原因
 * @returns {Promise} 返回拒绝结果
 */
export function rejectInquiry(inquiryId, reason) {
  return request({
    url: `/api/b2b/order/inquiry/${inquiryId}/reject`,
    method: 'POST',
    data: { reason }
  });
}

/**
 * 创建合同
 * @param {Object} data - 合同数据
 * @param {number} data.customer_id - 客户ID
 * @param {string} data.contract_no - 合同编号
 * @param {string} data.contract_name - 合同名称
 * @param {string} data.start_date - 开始日期
 * @param {string} data.end_date - 结束日期
 * @param {number} data.total_amount - 合同总金额
 * @param {Array} data.items - 合同项
 * @param {string} data.remark - 备注
 * @returns {Promise} 返回创建结果
 */
export function createContract(data) {
  return request({
    url: '/api/b2b/order/contract',
    method: 'POST',
    data
  });
}

/**
 * 获取合同列表
 * @param {Object} params - 查询参数
 * @returns {Promise} 返回合同列表
 */
export function getContractList(params) {
  return request({
    url: '/api/b2b/order/contract/list',
    method: 'GET',
    params
  });
}

/**
 * 获取合同详情
 * @param {number} contractId - 合同ID
 * @returns {Promise} 返回合同详情
 */
export function getContractDetail(contractId) {
  return request({
    url: `/api/b2b/order/contract/${contractId}`,
    method: 'GET'
  });
}

/**
 * 更新合同
 * @param {number} contractId - 合同ID
 * @param {Object} data - 更新数据
 * @returns {Promise} 返回更新结果
 */
export function updateContract(contractId, data) {
  return request({
    url: `/api/b2b/order/contract/${contractId}`,
    method: 'PUT',
    data
  });
}

/**
 * 确认合同
 * @param {number} contractId - 合同ID
 * @returns {Promise} 返回确认结果
 */
export function confirmContract(contractId) {
  return request({
    url: `/api/b2b/order/contract/${contractId}/confirm`,
    method: 'POST'
  });
}

/**
 * 终止合同
 * @param {number} contractId - 合同ID
 * @param {string} reason - 终止原因
 * @returns {Promise} 返回终止结果
 */
export function terminateContract(contractId, reason) {
  return request({
    url: `/api/b2b/order/contract/${contractId}/terminate`,
    method: 'POST',
    data: { reason }
  });
}

/**
 * 批量创建订单
 * @param {Array} orders - 订单数组
 * @returns {Promise} 返回批量创建结果
 */
export function batchCreateOrders(orders) {
  return request({
    url: '/api/b2b/order/batch',
    method: 'POST',
    data: { orders }
  });
}

/**
 * 获取订单状态枚举
 * @returns {Promise} 返回订单状态枚举
 */
export function getOrderStatusEnum() {
  return request({
    url: '/api/b2b/order/status-enum',
    method: 'GET'
  });
}

/**
 * 获取订单类型枚举
 * @returns {Promise} 返回订单类型枚举
 */
export function getOrderTypeEnum() {
  return request({
    url: '/api/b2b/order/type-enum',
    method: 'GET'
  });
}

/**
 * 获取支付状态枚举
 * @returns {Promise} 返回支付状态枚举
 */
export function getPaymentStatusEnum() {
  return request({
    url: '/api/b2b/order/payment-status-enum',
    method: 'GET'
  });
}

/**
 * 导出订单数据
 * @param {Object} params - 导出参数
 * @returns {Promise} 返回导出文件
 */
export function exportOrders(params) {
  return request({
    url: '/api/b2b/order/export',
    method: 'GET',
    params,
    responseType: 'blob'
  });
}

/**
 * 获取订单统计
 * @param {Object} params - 统计参数
 * @param {string} params.start_date - 开始日期
 * @param {string} params.end_date - 结束日期
 * @param {string} params.group_by - 分组方式(day/month/year)
 * @returns {Promise} 返回订单统计数据
 */
export function getOrderStats(params) {
  return request({
    url: '/api/b2b/order/stats',
    method: 'GET',
    params
  });
}