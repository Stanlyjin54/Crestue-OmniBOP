package com.kuafu.web.service;

import com.kuafu.web.entity.BusinessOpportunity;
import com.kuafu.web.entity.ProcurementInfo;
import java.util.List;
import java.util.Map;

/**
 * 商机线索匹配算法接口
 * 负责将采购信息与企业产品服务进行智能匹配
 */
public interface IBusinessOpportunityMatcherService {

    /**
     * 匹配采购信息到商机线索
     * @param procurementInfo 采购信息
     * @return 匹配到的商机线索
     */
    BusinessOpportunity matchProcurementToOpportunity(ProcurementInfo procurementInfo);

    /**
     * 批量匹配采购信息
     * @param procurementInfos 采购信息列表
     * @return 匹配到的商机线索列表
     */
    List<BusinessOpportunity> matchProcurementsBatch(List<ProcurementInfo> procurementInfos);

    /**
     * 计算匹配分数
     * @param procurementInfo 采购信息
     * @param companyProfile 公司档案信息
     * @return 匹配分数 (0-100)
     */
    int calculateMatchScore(ProcurementInfo procurementInfo, Map<String, Object> companyProfile);

    /**
     * 关键词匹配
     * @param procurementInfo 采购信息
     * @param keywords 关键词列表
     * @return 匹配到的关键词列表
     */
    List<String> matchKeywords(ProcurementInfo procurementInfo, List<String> keywords);

    /**
     * 行业匹配
     * @param procurementInfo 采购信息
     * @param industryCodes 行业代码列表
     * @return 匹配到的行业代码
     */
    List<String> matchIndustries(ProcurementInfo procurementInfo, List<String> industryCodes);

    /**
     * 地区匹配
     * @param procurementInfo 采购信息
     * @param serviceRegions 服务地区列表
     * @return 是否匹配
     */
    boolean matchRegion(ProcurementInfo procurementInfo, List<String> serviceRegions);

    /**
     * 预算匹配
     * @param procurementInfo 采购信息
     * @param budgetRange 预算范围
     * @return 是否匹配
     */
    boolean matchBudget(ProcurementInfo procurementInfo, Map<String, Long> budgetRange);

    /**
     * 获取匹配规则配置
     * @return 匹配规则配置
     */
    Map<String, Object> getMatchRules();

    /**
     * 更新匹配规则配置
     * @param rules 新的匹配规则
     */
    void updateMatchRules(Map<String, Object> rules);

    /**
     * 获取匹配统计信息
     * @return 匹配统计信息
     */
    Map<String, Object> getMatchStatistics();
}