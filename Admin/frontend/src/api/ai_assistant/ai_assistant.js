/**
 * AI助手管理相关API端点
 */

import request from '@/utils/request'

// AI助手日志统计
export function getAiAssistantLogs(params) {
  return request({
    url: '/api/admin/ai_assistant/logs',
    method: 'get',
    params
  })
}

export function getAiAssistantLogDetail(id) {
  return request({
    url: `/api/admin/ai_assistant/logs/${id}`,
    method: 'get'
  })
}

export function deleteAiAssistantLog(id) {
  return request({
    url: `/api/admin/ai_assistant/logs/${id}`,
    method: 'delete'
  })
}

export function batchDeleteAiAssistantLogs(ids) {
  return request({
    url: '/api/admin/ai_assistant/logs/batch',
    method: 'delete',
    data: { ids }
  })
}

// AI助手日志统计
export function getAiAssistantLogStatistics(params) {
  return request({
    url: '/api/admin/ai_assistant/statistics',
    method: 'get',
    params
  })
}

export function getAiAssistantUsageTrend(params) {
  return request({
    url: '/api/admin/ai_assistant/trend',
    method: 'get',
    params
  })
}

export function getAiAssistantTopUsers(params) {
  return request({
    url: '/api/admin/ai_assistant/top-users',
    method: 'get',
    params
  })
}

export function getAiAssistantPopularFeatures(params) {
  return request({
    url: '/api/admin/ai_assistant/popular-features',
    method: 'get',
    params
  })
}

// AI助手配置管理
export function getAiAssistantConfig() {
  return request({
    url: '/api/admin/ai_assistant/config',
    method: 'get'
  })
}

export function updateAiAssistantConfig(data) {
  return request({
    url: '/api/admin/ai_assistant/config',
    method: 'put',
    data
  })
}

// AI助手性能分析
export function getAiAssistantPerformance(params) {
  return request({
    url: '/api/admin/ai_assistant/performance',
    method: 'get',
    params
  })
}

// AI助手错误分析
export function getAiAssistantErrors(params) {
  return request({
    url: '/api/admin/ai_assistant/errors',
    method: 'get',
    params
  })
}

// AI助手导出
export function exportAiAssistantLogs(params) {
  return request({
    url: '/api/admin/ai_assistant/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

export function exportAiAssistantStatistics(params) {
  return request({
    url: '/api/admin/ai_assistant/statistics/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

export default {
  getAiAssistantLogs,
  getAiAssistantLogDetail,
  deleteAiAssistantLog,
  batchDeleteAiAssistantLogs,
  getAiAssistantLogStatistics,
  getAiAssistantUsageTrend,
  getAiAssistantTopUsers,
  getAiAssistantPopularFeatures,
  getAiAssistantConfig,
  updateAiAssistantConfig,
  getAiAssistantPerformance,
  getAiAssistantErrors,
  exportAiAssistantLogs,
  exportAiAssistantStatistics
}