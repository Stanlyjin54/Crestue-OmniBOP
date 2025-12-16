/**
 * 社交媒体商机获取API
 * 提供抖音、微信、小红书等平台的内容分析和商机提取功能
 */

import request from '@/utils/request';

/**
 * 分析抖音视频内容，提取商机信息
 */
export function analyzeDouyinContent(data) {
  return request({
    url: '/api/social-media/douyin/analyze',
    method: 'post',
    data
  });
}

/**
 * 分析微信公众号文章内容，提取商机信息
 */
export function analyzeWechatArticle(data) {
  return request({
    url: '/api/social-media/wechat/analyze',
    method: 'post',
    data
  });
}

/**
 * 分析小红书笔记内容，提取商机信息
 */
export function analyzeXiaohongshuNote(data) {
  return request({
    url: '/api/social-media/xiaohongshu/analyze',
    method: 'post',
    data
  });
}

/**
 * 批量分析社交媒体内容
 */
export function analyzeBatchContent(data) {
  return request({
    url: '/api/social-media/batch/analyze',
    method: 'post',
    data
  });
}

/**
 * 提取产品需求
 */
export function extractProductRequirements(content, platform) {
  return request({
    url: '/api/social-media/extract-requirements',
    method: 'post',
    params: { content, platform }
  });
}

/**
 * 提取品牌提及
 */
export function extractBrandMentions(content) {
  return request({
    url: '/api/social-media/extract-brands',
    method: 'post',
    params: { content }
  });
}

/**
 * 评估内容的商业价值
 */
export function evaluateCommercialValue(content) {
  return request({
    url: '/api/social-media/evaluate-commercial-value',
    method: 'post',
    data: content
  });
}

/**
 * 获取热门话题和趋势
 */
export function getTrendingTopics(platform, category, timeRange) {
  return request({
    url: '/api/social-media/trending-topics',
    method: 'get',
    params: { platform, category, timeRange }
  });
}

/**
 * 监控指定关键词的社交媒体提及
 */
export function monitorKeywordMentions(keywords, platforms, monitoringPeriod) {
  return request({
    url: '/api/social-media/monitor-keywords',
    method: 'post',
    data: { keywords, platforms, monitoringPeriod }
  });
}

/**
 * 生成社交媒体商机报告
 */
export function generateSocialMediaReport(startDate, endDate, platforms) {
  return request({
    url: '/api/social-media/generate-report',
    method: 'get',
    params: { startDate, endDate, platforms }
  });
}

/**
 * 设置社交媒体监控配置
 */
export function setMonitoringConfig(config) {
  return request({
    url: '/api/social-media/monitoring-config',
    method: 'post',
    data: config
  });
}

/**
 * 获取社交媒体监控状态
 */
export function getMonitoringStatus() {
  return request({
    url: '/api/social-media/monitoring-status',
    method: 'get'
  });
}

/**
 * 分析竞争对手的社交媒体表现
 */
export function analyzeCompetitorPerformance(competitors, platforms, analysisPeriod) {
  return request({
    url: '/api/social-media/competitor-analysis',
    method: 'post',
    data: { competitors, platforms, analysisPeriod }
  });
}

/**
 * 查询社交媒体内容列表
 */
export function getSocialMediaContents(params) {
  return request({
    url: '/api/social-media/contents',
    method: 'get',
    params
  });
}

/**
 * 获取社交媒体内容详情
 */
export function getSocialMediaContentDetail(id) {
  return request({
    url: `/api/social-media/contents/${id}`,
    method: 'get'
  });
}