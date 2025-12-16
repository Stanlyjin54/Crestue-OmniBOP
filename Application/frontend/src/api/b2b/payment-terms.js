/**
 * B2B支付条款相关API
 * 支持账期、分期付款、信用支付等企业支付方式
 */

import { request } from '@/utils/request.js';

/**
 * 获取B2B支付条款列表
 * @param {Object} params - 查询参数
 * @param {number} params.current - 当前页码
 * @param {number} params.pageSize - 每页条数
 * @param {string} params.terms_name - 条款名称
 * @param {string} params.terms_type - 条款类型
 * @param {string} params.status - 条款状态
 * @param {number} params.customer_id - 客户ID
 * @returns {Promise} 返回支付条款列表
 */
export function getB2BPaymentTermsList(params) {
  return request({
    url: '/api/b2b/payment-terms/list',
    method: 'GET',
    params
  });
}

/**
 * 获取B2B支付条款详情
 * @param {number} termsId - 条款ID
 * @returns {Promise} 返回支付条款详情
 */
export function getB2BPaymentTermsDetail(termsId) {
  return request({
    url: `/api/b2b/payment-terms/${termsId}`,
    method: 'GET'
  });
}

/**
 * 创建B2B支付条款
 * @param {Object} data - 条款数据
 * @param {string} data.terms_name - 条款名称
 * @param {string} data.terms_type - 条款类型(credit/installment/period/combined)
 * @param {string} data.description - 条款描述
 * @param {number} data.credit_limit - 信用额度
 * @param {number} data.credit_period - 账期天数
 * @param {number} data.installment_periods - 分期期数
 * @param {number} data.down_payment_ratio - 首付比例
 * @param {number} data.interest_rate - 利率
 * @param {string} data.payment_method - 支付方式
 * @param {string} data.status - 条款状态
 * @returns {Promise} 返回创建结果
 */
export function createB2BPaymentTerms(data) {
  return request({
    url: '/api/b2b/payment-terms',
    method: 'POST',
    data
  });
}

/**
 * 更新B2B支付条款
 * @param {number} termsId - 条款ID
 * @param {Object} data - 更新数据
 * @returns {Promise} 返回更新结果
 */
export function updateB2BPaymentTerms(termsId, data) {
  return request({
    url: `/api/b2b/payment-terms/${termsId}`,
    method: 'PUT',
    data
  });
}

/**
 * 删除B2B支付条款
 * @param {number} termsId - 条款ID
 * @returns {Promise} 返回删除结果
 */
export function deleteB2BPaymentTerms(termsId) {
  return request({
    url: `/api/b2b/payment-terms/${termsId}`,
    method: 'DELETE'
  });
}

/**
 * 启用支付条款
 * @param {number} termsId - 条款ID
 * @returns {Promise} 返回启用结果
 */
export function enablePaymentTerms(termsId) {
  return request({
    url: `/api/b2b/payment-terms/${termsId}/enable`,
    method: 'POST'
  });
}

/**
 * 禁用支付条款
 * @param {number} termsId - 条款ID
 * @returns {Promise} 返回禁用结果
 */
export function disablePaymentTerms(termsId) {
  return request({
    url: `/api/b2b/payment-terms/${termsId}/disable`,
    method: 'POST'
  });
}

/**
 * 获取客户支付条款
 * @param {number} customerId - 客户ID
 * @returns {Promise} 返回客户支付条款
 */
export function getCustomerPaymentTerms(customerId) {
  return request({
    url: `/api/b2b/payment-terms/customer/${customerId}`,
    method: 'GET'
  });
}

/**
 * 分配支付条款给客户
 * @param {number} customerId - 客户ID
 * @param {number} termsId - 条款ID
 * @param {Object} data - 分配数据
 * @param {string} data.effective_date - 生效日期
 * @param {string} data.expiry_date - 到期日期
 * @param {string} data.remark - 备注
 * @returns {Promise} 返回分配结果
 */
export function assignPaymentTermsToCustomer(customerId, termsId, data) {
  return request({
    url: `/api/b2b/payment-terms/customer/${customerId}/assign`,
    method: 'POST',
    data: { terms_id: termsId, ...data }
  });
}

/**
 * 创建信用支付
 * @param {Object} data - 信用支付数据
 * @param {number} data.order_id - 订单ID
 * @param {number} data.customer_id - 客户ID
 * @param {number} data.amount - 支付金额
 * @param {string} data.credit_terms - 信用条款
 * @param {string} data.due_date - 到期日期
 * @param {string} data.remark - 备注
 * @returns {Promise} 返回创建结果
 */
export function createCreditPayment(data) {
  return request({
    url: '/api/b2b/payment/credit',
    method: 'POST',
    data
  });
}

/**
 * 创建分期付款
 * @param {Object} data - 分期付款数据
 * @param {number} data.order_id - 订单ID
 * @param {number} data.customer_id - 客户ID
 * @param {number} data.total_amount - 总金额
 * @param {number} data.down_payment - 首付金额
 * @param {number} data.installment_periods - 分期期数
 * @param {number} data.interest_rate - 利率
 * @param {Array} data.schedule - 还款计划
 * @returns {Promise} 返回创建结果
 */
export function createInstallmentPayment(data) {
  return request({
    url: '/api/b2b/payment/installment',
    method: 'POST',
    data
  });
}

/**
 * 获取分期付款详情
 * @param {number} installmentId - 分期付款ID
 * @returns {Promise} 返回分期付款详情
 */
export function getInstallmentPaymentDetail(installmentId) {
  return request({
    url: `/api/b2b/payment/installment/${installmentId}`,
    method: 'GET'
  });
}

/**
 * 更新分期付款状态
 * @param {number} installmentId - 分期付款ID
 * @param {Object} data - 更新数据
 * @param {string} data.status - 状态
 * @param {string} data.remark - 备注
 * @returns {Promise} 返回更新结果
 */
export function updateInstallmentPaymentStatus(installmentId, data) {
  return request({
    url: `/api/b2b/payment/installment/${installmentId}/status`,
    method: 'PUT',
    data
  });
}

/**
 * 创建账期支付
 * @param {Object} data - 账期支付数据
 * @param {number} data.order_id - 订单ID
 * @param {number} data.customer_id - 客户ID
 * @param {number} data.amount - 支付金额
 * @param {number} data.credit_days - 账期天数
 * @param {string} data.due_date - 到期日期
 * @param {string} data.remark - 备注
 * @returns {Promise} 返回创建结果
 */
export function createPeriodPayment(data) {
  return request({
    url: '/api/b2b/payment/period',
    method: 'POST',
    data
  });
}

/**
 * 获取账期支付详情
 * @param {number} periodPaymentId - 账期支付ID
 * @returns {Promise} 返回账期支付详情
 */
export function getPeriodPaymentDetail(periodPaymentId) {
  return request({
    url: `/api/b2b/payment/period/${periodPaymentId}`,
    method: 'GET'
  });
}

/**
 * 更新账期支付状态
 * @param {number} periodPaymentId - 账期支付ID
 * @param {Object} data - 更新数据
 * @param {string} data.status - 状态
 * @param {string} data.remark - 备注
 * @returns {Promise} 返回更新结果
 */
export function updatePeriodPaymentStatus(periodPaymentId, data) {
  return request({
    url: `/api/b2b/payment/period/${periodPaymentId}/status`,
    method: 'PUT',
    data
  });
}

/**
 * 获取客户信用信息
 * @param {number} customerId - 客户ID
 * @returns {Promise} 返回客户信用信息
 */
export function getCustomerCreditInfo(customerId) {
  return request({
    url: `/api/b2b/payment/customer/${customerId}/credit`,
    method: 'GET'
  });
}

/**
 * 更新客户信用额度
 * @param {number} customerId - 客户ID
 * @param {Object} data - 信用数据
 * @param {number} data.credit_limit - 信用额度
 * @param {string} data.credit_level - 信用等级
 * @param {string} data.validity_period - 有效期
 * @param {string} data.remark - 备注
 * @returns {Promise} 返回更新结果
 */
export function updateCustomerCreditLimit(customerId, data) {
  return request({
    url: `/api/b2b/payment/customer/${customerId}/credit-limit`,
    method: 'PUT',
    data
  });
}

/**
 * 获取客户支付记录
 * @param {number} customerId - 客户ID
 * @param {Object} params - 查询参数
 * @param {number} params.current - 当前页码
 * @param {number} params.pageSize - 每页条数
 * @param {string} params.payment_type - 支付类型
 * @param {string} params.status - 支付状态
 * @param {string} params.start_date - 开始日期
 * @param {string} params.end_date - 结束日期
 * @returns {Promise} 返回支付记录
 */
export function getCustomerPaymentHistory(customerId, params = {}) {
  return request({
    url: `/api/b2b/payment/customer/${customerId}/history`,
    method: 'GET',
    params
  });
}

/**
 * 获取支付条款类型枚举
 * @returns {Promise} 返回支付条款类型枚举
 */
export function getPaymentTermsTypeEnum() {
  return request({
    url: '/api/b2b/payment-terms/types',
    method: 'GET'
  });
}

/**
 * 获取支付状态枚举
 * @returns {Promise} 返回支付状态枚举
 */
export function getB2BPaymentStatusEnum() {
  return request({
    url: '/api/b2b/payment/status-enum',
    method: 'GET'
  });
}

/**
 * 获取分期付款状态枚举
 * @returns {Promise} 返回分期付款状态枚举
 */
export function getInstallmentStatusEnum() {
  return request({
    url: '/api/b2b/payment/installment/status-enum',
    method: 'GET'
  });
}

/**
 * 获取账期支付状态枚举
 * @returns {Promise} 返回账期支付状态枚举
 */
export function getPeriodPaymentStatusEnum() {
  return request({
    url: '/api/b2b/payment/period/status-enum',
    method: 'GET'
  });
}

/**
 * 获取B2B支付统计
 * @param {Object} params - 统计参数
 * @param {string} params.start_date - 开始日期
 * @param {string} params.end_date - 结束日期
 * @param {string} params.customer_id - 客户ID(可选)
 * @param {string} params.payment_type - 支付类型(可选)
 * @returns {Promise} 返回支付统计数据
 */
export function getB2BPaymentStats(params = {}) {
  return request({
    url: '/api/b2b/payment/stats',
    method: 'GET',
    params
  });
}

/**
 * 检查客户支付能力
 * @param {number} customerId - 客户ID
 * @param {number} amount - 支付金额
 * @param {string} paymentType - 支付类型
 * @returns {Promise} 返回检查结果
 */
export function checkCustomerPaymentAbility(customerId, amount, paymentType) {
  return request({
    url: `/api/b2b/payment/customer/${customerId}/check-ability`,
    method: 'GET',
    params: { amount, payment_type: paymentType }
  });
}

/**
 * 创建支付提醒
 * @param {number} paymentId - 支付ID
 * @param {Object} data - 提醒数据
 * @param {string} data.reminder_type - 提醒类型
 * @param {string} data.reminder_date - 提醒日期
 * @param {string} data.remark - 备注
 * @returns {Promise} 返回创建结果
 */
export function createPaymentReminder(paymentId, data) {
  return request({
    url: `/api/b2b/payment/${paymentId}/reminder`,
    method: 'POST',
    data
  });
}

/**
 * 获取支付提醒列表
 * @param {Object} params - 查询参数
 * @param {number} params.current - 当前页码
 * @param {number} params.pageSize - 每页条数
 * @param {string} params.status - 提醒状态
 * @param {string} params.reminder_type - 提醒类型
 * @returns {Promise} 返回支付提醒列表
 */
export function getPaymentReminders(params = {}) {
  return request({
    url: '/api/b2b/payment/reminders',
    method: 'GET',
    params
  });
}