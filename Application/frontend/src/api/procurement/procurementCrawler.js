import request from '@/utils/request'

/**
 * 政府采购信息采集相关API
 * 对应后端IProcurementCrawlerService接口
 */

/**
 * 采集政府采购网信息
 * @param {Object} params - 采集参数
 * @param {string} params.keyword - 搜索关键词
 * @param {string} params.region - 地区
 * @param {number} params.days - 采集最近几天的数据
 * @returns {Promise} 返回采集结果
 */
export function crawlGovernmentProcurement(params) {
  return request({
    url: '/api/procurement/crawl/government',
    method: 'POST',
    data: params
  })
}

/**
 * 采集招标公告信息
 * @param {Object} params - 采集参数
 * @param {string} params.category - 招标类别
 * @param {string} params.region - 地区
 * @param {number} params.days - 采集最近几天的数据
 * @returns {Promise} 返回采集结果
 */
export function crawlTenderNotice(params) {
  return request({
    url: '/api/procurement/crawl/tender',
    method: 'POST',
    data: params
  })
}

/**
 * 智能匹配商机线索
 * @param {Object} procurementInfo - 采购信息
 * @returns {Promise} 返回匹配的商机线索列表
 */
export function matchBusinessOpportunities(procurementInfo) {
  return request({
    url: '/api/procurement/match/opportunities',
    method: 'POST',
    data: procurementInfo
  })
}

/**
 * 将采购信息转换为商机线索
 * @param {Object} procurementInfo - 采购信息
 * @returns {Promise} 返回商机线索
 */
export function convertToOpportunity(procurementInfo) {
  return request({
    url: '/api/procurement/convert/opportunity',
    method: 'POST',
    data: procurementInfo
  })
}

/**
 * 检查采集合规性
 * @param {string} url - 目标网址
 * @returns {Promise} 返回是否合规
 */
export function checkCompliance(url) {
  return request({
    url: '/api/procurement/check/compliance',
    method: 'GET',
    params: { url }
  })
}

/**
 * 获取采集统计信息
 * @returns {Promise} 返回统计信息
 */
export function getCrawlerStatistics() {
  return request({
    url: '/api/procurement/crawler/statistics',
    method: 'GET'
  })
}

/**
 * 获取采购信息列表
 * @param {Object} params - 查询参数
 * @param {number} params.current - 当前页码
 * @param {number} params.pageSize - 每页大小
 * @param {string} params.keyword - 关键词搜索
 * @param {string} params.region - 地区筛选
 * @param {string} params.procurementType - 采购类型筛选
 * @returns {Promise} 返回采购信息列表
 */
export function getProcurementList(params) {
  return request({
    url: '/api/procurement/list',
    method: 'GET',
    params
  })
}

/**
 * 获取采购信息详情
 * @param {number} id - 采购信息ID
 * @returns {Promise} 返回采购信息详情
 */
export function getProcurementDetail(id) {
  return request({
    url: `/api/procurement/detail/${id}`,
    method: 'GET'
  })
}

/**
 * 批量审核采购信息
 * @param {Object} params - 审核参数
 * @param {number[]} params.ids - 采购信息ID列表
 * @param {string} params.status - 审核状态 (approved/rejected)
 * @param {string} params.remark - 审核备注
 * @returns {Promise} 返回审核结果
 */
export function reviewProcurementBatch(params) {
  return request({
    url: '/api/procurement/review/batch',
    method: 'POST',
    data: params
  })
}

/**
 * 启动定时采集任务
 * @param {Object} params - 任务参数
 * @param {string} params.taskName - 任务名称
 * @param {string} params.cronExpression - cron表达式
 * @param {Object} params.crawlConfig - 采集配置
 * @returns {Promise} 返回任务创建结果
 */
export function startScheduledTask(params) {
  return request({
    url: '/api/procurement/task/scheduled',
    method: 'POST',
    data: params
  })
}

/**
 * 获取采集任务列表
 * @param {Object} params - 查询参数
 * @returns {Promise} 返回任务列表
 */
export function getCrawlTaskList(params) {
  return request({
    url: '/api/procurement/task/list',
    method: 'GET',
    params
  })
}

/**
 * 停止采集任务
 * @param {number} taskId - 任务ID
 * @returns {Promise} 返回操作结果
 */
export function stopCrawlTask(taskId) {
  return request({
    url: `/api/procurement/task/stop/${taskId}`,
    method: 'POST'
  })
}

/**
 * 获取采集日志
 * @param {Object} params - 查询参数
 * @param {number} params.taskId - 任务ID
 * @param {string} params.level - 日志级别
 * @param {string} params.startTime - 开始时间
 * @param {string} params.endTime - 结束时间
 * @returns {Promise} 返回采集日志
 */
export function getCrawlLogs(params) {
  return request({
    url: '/api/procurement/logs',
    method: 'GET',
    params
  })
}

/**
 * 导出采购信息
 * @param {Object} params - 导出参数
 * @param {string} params.format - 导出格式 (excel/csv)
 * @param {string} params.keyword - 关键词筛选
 * @param {string} params.region - 地区筛选
 * @param {string} params.dateRange - 日期范围
 * @returns {Promise} 返回导出文件
 */
export function exportProcurementData(params) {
  return request({
    url: '/api/procurement/export',
    method: 'POST',
    data: params,
    responseType: 'blob'
  })
}

export default {
  crawlGovernmentProcurement,
  crawlTenderNotice,
  matchBusinessOpportunities,
  convertToOpportunity,
  checkCompliance,
  getCrawlerStatistics,
  getProcurementList,
  getProcurementDetail,
  reviewProcurementBatch,
  startScheduledTask,
  getCrawlTaskList,
  stopCrawlTask,
  getCrawlLogs,
  exportProcurementData
}