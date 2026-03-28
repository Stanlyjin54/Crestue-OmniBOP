/**
 * B2B客户管理相关API
 * 支持企业客户、客户等级、信用管理等功能
 */

import request from '@/utils/request.js';

/**
 * 获取B2B客户列表
 * @param {Object} params - 查询参数
 * @param {number} params.current - 当前页码
 * @param {number} params.pageSize - 每页条数
 * @param {string} params.customer_name - 客户名称
 * @param {string} params.company_name - 企业名称
 * @param {string} params.customer_type - 客户类型(B2B/B2C)
 * @param {string} params.credit_level - 信用等级
 * @param {string} params.status - 客户状态
 * @returns {Promise} 返回客户列表数据
 */
export function getB2BCustomerList(params) {
  return request({
    url: '/api/b2b/customer/list',
    method: 'GET',
    params
  });
}

/**
 * 获取B2B客户详情
 * @param {number} customerId - 客户ID
 * @returns {Promise} 返回客户详细信息
 */
export function getB2BCustomerDetail(customerId) {
  return request({
    url: `/api/b2b/customer/${customerId}`,
    method: 'GET'
  });
}

/**
 * 创建B2B客户
 * @param {Object} data - 客户数据
 * @param {string} data.customer_name - 客户名称
 * @param {string} data.company_name - 企业名称
 * @param {string} data.customer_type - 客户类型
 * @param {string} data.contact_person - 联系人
 * @param {string} data.contact_phone - 联系电话
 * @param {string} data.contact_email - 联系邮箱
 * @param {string} data.company_address - 企业地址
 * @param {string} data.business_license - 营业执照号
 * @param {string} data.tax_number - 税务登记号
 * @param {number} data.credit_limit - 信用额度
 * @param {string} data.credit_level - 信用等级
 * @param {string} data.payment_terms - 支付条款
 * @param {number} data.sales_rep_id - 销售代表ID
 * @returns {Promise} 返回创建结果
 */
export function createB2BCustomer(data) {
  return request({
    url: '/api/b2b/customer',
    method: 'POST',
    data
  });
}

/**
 * 更新B2B客户信息
 * @param {number} customerId - 客户ID
 * @param {Object} data - 更新数据
 * @returns {Promise} 返回更新结果
 */
export function updateB2BCustomer(customerId, data) {
  return request({
    url: `/api/b2b/customer/${customerId}`,
    method: 'PUT',
    data
  });
}

/**
 * 删除B2B客户
 * @param {number} customerId - 客户ID
 * @returns {Promise} 返回删除结果
 */
export function deleteB2BCustomer(customerId) {
  return request({
    url: `/api/b2b/customer/${customerId}`,
    method: 'DELETE'
  });
}

/**
 * 获取客户信用信息
 * @param {number} customerId - 客户ID
 * @returns {Promise} 返回客户信用信息
 */
export function getCustomerCreditInfo(customerId) {
  return request({
    url: `/api/b2b/customer/${customerId}/credit`,
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
export function updateCustomerCredit(customerId, data) {
  return request({
    url: `/api/b2b/customer/${customerId}/credit`,
    method: 'PUT',
    data
  });
}

/**
 * 获取客户交易统计
 * @param {number} customerId - 客户ID
 * @param {string} period - 统计周期(month/quarter/year)
 * @returns {Promise} 返回交易统计数据
 */
export function getCustomerTransactionStats(customerId, period = 'month') {
  return request({
    url: `/api/b2b/customer/${customerId}/stats`,
    method: 'GET',
    params: { period }
  });
}

/**
 * 获取客户等级配置
 * @returns {Promise} 返回客户等级配置
 */
export function getCustomerLevelConfig() {
  return request({
    url: '/api/b2b/customer/level-config',
    method: 'GET'
  });
}

/**
 * 获取客户类型枚举
 * @returns {Promise} 返回客户类型枚举
 */
export function getCustomerTypeEnum() {
  return request({
    url: '/api/b2b/customer/types',
    method: 'GET'
  });
}

/**
 * 获取信用等级枚举
 * @returns {Promise} 返回信用等级枚举
 */
export function getCreditLevelEnum() {
  return request({
    url: '/api/b2b/customer/credit-levels',
    method: 'GET'
  });
}

/**
 * 获取支付条款枚举
 * @returns {Promise} 返回支付条款枚举
 */
export function getPaymentTermsEnum() {
  return request({
    url: '/api/b2b/customer/payment-terms',
    method: 'GET'
  });
}

/**
 * 批量导入客户
 * @param {FormData} fileData - 导入文件数据
 * @returns {Promise} 返回导入结果
 */
export function importCustomers(fileData) {
  return request({
    url: '/api/b2b/customer/import',
    method: 'POST',
    data: fileData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
}

/**
 * 导出客户数据
 * @param {Object} params - 导出参数
 * @returns {Promise} 返回导出文件
 */
export function exportCustomers(params) {
  return request({
    url: '/api/b2b/customer/export',
    method: 'GET',
    params,
    responseType: 'blob'
  });
}

/**
 * 获取客户联系人列表
 * @param {number} customerId - 客户ID
 * @returns {Promise} 返回联系人列表
 */
export function getCustomerContacts(customerId) {
  return request({
    url: `/api/b2b/customer/${customerId}/contacts`,
    method: 'GET'
  });
}

/**
 * 添加客户联系人
 * @param {number} customerId - 客户ID
 * @param {Object} data - 联系人数据
 * @returns {Promise} 返回添加结果
 */
export function addCustomerContact(customerId, data) {
  return request({
    url: `/api/b2b/customer/${customerId}/contacts`,
    method: 'POST',
    data
  });
}

/**
 * 更新客户联系人
 * @param {number} customerId - 客户ID
 * @param {number} contactId - 联系人ID
 * @param {Object} data - 更新数据
 * @returns {Promise} 返回更新结果
 */
export function updateCustomerContact(customerId, contactId, data) {
  return request({
    url: `/api/b2b/customer/${customerId}/contacts/${contactId}`,
    method: 'PUT',
    data
  });
}

/**
 * 删除客户联系人
 * @param {number} customerId - 客户ID
 * @param {number} contactId - 联系人ID
 * @returns {Promise} 返回删除结果
 */
export function deleteCustomerContact(customerId, contactId) {
  return request({
    url: `/api/b2b/customer/${customerId}/contacts/${contactId}`,
    method: 'DELETE'
  });
}