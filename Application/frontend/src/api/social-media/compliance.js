/**
 * 社交媒体合规性检查API
 * 提供内容合规检查、敏感内容检测、KOL合规检查等功能
 */

import request from '@/utils/request';

/**
 * 内容合规性检查
 */
export function checkContentCompliance(content, platform) {
  return request({
    url: '/api/social-media/compliance/check',
    method: 'post',
    data: { content, platform }
  });
}

/**
 * 批量内容合规性检查
 */
export function checkBatchContentCompliance(contents, platform) {
  return request({
    url: '/api/social-media/compliance/batch-check',
    method: 'post',
    data: { contents, platform }
  });
}

/**
 * 基础内容检查（适用于所有平台）
 */
export function checkBasicContentCompliance(content) {
  return request({
    url: '/api/social-media/compliance/basic-check',
    method: 'post',
    data: { content }
  });
}

/**
 * 平台特定规则检查
 */
export function checkPlatformSpecificRules(content, platform) {
  return request({
    url: '/api/social-media/compliance/platform-rules',
    method: 'post',
    data: { content, platform }
  });
}

/**
 * 敏感内容检测
 */
export function detectSensitiveContent(content) {
  return request({
    url: '/api/social-media/compliance/sensitive-content',
    method: 'post',
    data: { content }
  });
}

/**
 * 广告合规检查
 */
export function checkAdCompliance(content) {
  return request({
    url: '/api/social-media/compliance/advertisement',
    method: 'post',
    data: { content }
  });
}

/**
 * 版权检查
 */
export function checkCopyrightIssues(content) {
  return request({
    url: '/api/social-media/compliance/copyright',
    method: 'post',
    data: { content }
  });
}

/**
 * KOL/网红合规检查
 */
export function checkInfluencerCompliance(content, influencerInfo) {
  return request({
    url: '/api/social-media/compliance/influencer',
    method: 'post',
    data: { content, influencerInfo }
  });
}

/**
 * 生成合规报告
 */
export function generateComplianceReport(contentIds, platform) {
  return request({
    url: '/api/social-media/compliance/generate-report',
    method: 'post',
    data: { contentIds, platform }
  });
}

/**
 * 设置合规配置
 */
export function setComplianceConfig(config) {
  return request({
    url: '/api/social-media/compliance/config',
    method: 'post',
    data: config
  });
}

/**
 * 获取合规状态
 */
export function getComplianceStatus() {
  return request({
    url: '/api/social-media/compliance/compliance-status',
    method: 'get'
  });
}

/**
 * 获取平台合规规则
 */
export function getPlatformComplianceRules(platform) {
  return request({
    url: `/api/social-media/compliance/platform-rules/${platform}`,
    method: 'get'
  });
}

/**
 * 实时内容监控
 */
export function monitorContentRealTime(content, platform) {
  return request({
    url: '/api/social-media/compliance/realtime-monitor',
    method: 'post',
    data: { content, platform }
  });
}