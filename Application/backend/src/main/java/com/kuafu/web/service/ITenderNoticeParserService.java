package com.kuafu.web.service;

import com.kuafu.web.entity.ProcurementInfo;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 招标公告智能解析器接口
 * 负责从招标公告文本中提取关键信息
 */
public interface ITenderNoticeParserService {

    /**
     * 解析招标公告内容
     * @param title 公告标题
     * @param content 公告正文内容
     * @param sourceUrl 来源URL
     * @return 解析后的采购信息
     */
    ProcurementInfo parseTenderNotice(String title, String content, String sourceUrl);

    /**
     * 批量解析招标公告
     * @param tenderNotices 招标公告列表
     * @return 解析后的采购信息列表
     */
    List<ProcurementInfo> parseTenderNoticesBatch(List<Map<String, String>> tenderNotices);

    /**
     * 提取项目预算信息
     * @param content 公告内容
     * @return 预算金额（元）
     */
    String extractBudget(String content);

    /**
     * 提取投标截止日期
     * @param content 公告内容
     * @return 截止日期
     */
    LocalDateTime extractDeadline(String content);

    /**
     * 提取采购类型
     * @param title 公告标题
     * @param content 公告内容
     * @return 采购类型
     */
    String extractProcurementType(String title, String content);

    /**
     * 提取项目地区
     * @param content 公告内容
     * @return 项目地区
     */
    String extractRegion(String content);

    /**
     * 提取联系人信息
     * @param content 公告内容
     * @return 联系人信息映射
     */
    Map<String, String> extractContactInfo(String content);

    /**
     * 验证公告内容的完整性
     * @param procurementInfo 采购信息
     * @return 验证结果
     */
    boolean validateProcurementInfo(ProcurementInfo procurementInfo);

    /**
     * 获取解析统计信息
     * @return 解析统计信息
     */
    Map<String, Object> getParserStatistics();
}