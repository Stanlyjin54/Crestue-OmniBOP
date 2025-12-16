package com.kuafu.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuafu.web.entity.BusinessOpportunity;
import com.kuafu.web.entity.ProcurementInfo;
import com.kuafu.web.mapper.BusinessOpportunityMapper;
import com.kuafu.web.mapper.ProcurementInfoMapper;
import com.kuafu.web.service.IBusinessOpportunityMatcherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商机线索匹配算法实现类
 * 基于关键词、行业、地区、预算等多维度进行智能匹配
 */
@Slf4j
@Service
public class BusinessOpportunityMatcherServiceImpl implements IBusinessOpportunityMatcherService {

    @Autowired
    private BusinessOpportunityMapper businessOpportunityMapper;

    @Autowired
    private ProcurementInfoMapper procurementInfoMapper;

    // 公司关键词配置（实际应该从数据库或配置文件获取）
    private static final List<String> COMPANY_KEYWORDS = Arrays.asList(
        "软件", "系统", "平台", "开发", "集成", "解决方案", "信息化", "数字化",
        "云计算", "大数据", "人工智能", "物联网", "区块链", "移动应用", "APP",
        "网站建设", "电商平台", "ERP", "CRM", "OA", "财务软件", "管理软件"
    );

    // 行业代码映射
    private static final Map<String, List<String>> INDUSTRY_MAPPING = new HashMap<>();
    static {
        INDUSTRY_MAPPING.put("I65", Arrays.asList("软件", "信息技术", "IT", "互联网", "系统集成"));
        INDUSTRY_MAPPING.put("I64", Arrays.asList("互联网", "网络", "在线", "平台", "电商"));
        INDUSTRY_MAPPING.put("M74", Arrays.asList("设计", "咨询", "技术服务", "专业技术"));
        INDUSTRY_MAPPING.put("F51", Arrays.asList("批发", "贸易", "销售", "经销"));
        INDUSTRY_MAPPING.put("L72", Arrays.asList("商务服务", "企业服务", "管理咨询"));
    }

    // 服务地区配置
    private static final List<String> SERVICE_REGIONS = Arrays.asList(
        "北京", "上海", "广州", "深圳", "杭州", "南京", "苏州", "成都", "武汉", "西安"
    );

    // 预算范围配置
    private static final Map<String, Long> BUDGET_RANGE = new HashMap<>();
    static {
        BUDGET_RANGE.put("min", 100000L);  // 10万元
        BUDGET_RANGE.put("max", 5000000L); // 500万元
    }

    // 匹配规则配置
    private Map<String, Object> matchRules = new HashMap<>();
    {
        matchRules.put("keyword_weight", 0.3);      // 关键词匹配权重
        matchRules.put("industry_weight", 0.25);    // 行业匹配权重
        matchRules.put("region_weight", 0.2);       // 地区匹配权重
        matchRules.put("budget_weight", 0.15);      // 预算匹配权重
        matchRules.put("deadline_weight", 0.1);    // 截止日期权重
        matchRules.put("min_score", 60);           // 最低匹配分数
    }

    @Override
    public BusinessOpportunity matchProcurementToOpportunity(ProcurementInfo procurementInfo) {
        log.info("开始匹配采购信息到商机: {}", procurementInfo.getTitle());
        
        try {
            // 构建公司档案（实际应该从数据库获取）
            Map<String, Object> companyProfile = buildCompanyProfile();
            
            // 计算匹配分数
            int matchScore = calculateMatchScore(procurementInfo, companyProfile);
            
            if (matchScore < (Integer) matchRules.get("min_score")) {
                log.info("匹配分数过低，跳过此商机: {} (分数: {})", procurementInfo.getTitle(), matchScore);
                return null;
            }

            // 创建商机线索
            BusinessOpportunity opportunity = createBusinessOpportunity(procurementInfo, matchScore);
            
            log.info("成功创建商机线索: {} (匹配分数: {})", opportunity.getName(), matchScore);
            return opportunity;
            
        } catch (Exception e) {
            log.error("匹配采购信息失败: {}", procurementInfo.getTitle(), e);
            return null;
        }
    }

    @Override
    public List<BusinessOpportunity> matchProcurementsBatch(List<ProcurementInfo> procurementInfos) {
        return procurementInfos.stream()
            .map(this::matchProcurementToOpportunity)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    @Override
    public int calculateMatchScore(ProcurementInfo procurementInfo, Map<String, Object> companyProfile) {
        double totalScore = 0.0;

        // 关键词匹配 (30%)
        List<String> matchedKeywords = matchKeywords(procurementInfo, COMPANY_KEYWORDS);
        double keywordScore = (double) matchedKeywords.size() / COMPANY_KEYWORDS.size() * 100;
        totalScore += keywordScore * (Double) matchRules.get("keyword_weight");

        // 行业匹配 (25%)
        List<String> companyIndustries = (List<String>) companyProfile.get("industries");
        List<String> matchedIndustries = matchIndustries(procurementInfo, companyIndustries);
        double industryScore = matchedIndustries.isEmpty() ? 0 : 100;
        totalScore += industryScore * (Double) matchRules.get("industry_weight");

        // 地区匹配 (20%)
        boolean regionMatched = matchRegion(procurementInfo, SERVICE_REGIONS);
        double regionScore = regionMatched ? 100 : 0;
        totalScore += regionScore * (Double) matchRules.get("region_weight");

        // 预算匹配 (15%)
        boolean budgetMatched = matchBudget(procurementInfo, BUDGET_RANGE);
        double budgetScore = budgetMatched ? 100 : 0;
        totalScore += budgetScore * (Double) matchRules.get("budget_weight");

        // 截止日期匹配 (10%)
        double deadlineScore = calculateDeadlineScore(procurementInfo);
        totalScore += deadlineScore * (Double) matchRules.get("deadline_weight");

        return (int) Math.round(totalScore);
    }

    @Override
    public List<String> matchKeywords(ProcurementInfo procurementInfo, List<String> keywords) {
        String combinedText = (procurementInfo.getTitle() + " " + procurementInfo.getContent()).toLowerCase();
        
        return keywords.stream()
            .filter(keyword -> combinedText.contains(keyword.toLowerCase()))
            .collect(Collectors.toList());
    }

    @Override
    public List<String> matchIndustries(ProcurementInfo procurementInfo, List<String> industryCodes) {
        String combinedText = (procurementInfo.getTitle() + " " + procurementInfo.getContent()).toLowerCase();
        
        return industryCodes.stream()
            .filter(industryCode -> {
                List<String> industryKeywords = INDUSTRY_MAPPING.get(industryCode);
                if (industryKeywords != null) {
                    return industryKeywords.stream()
                        .anyMatch(keyword -> combinedText.contains(keyword.toLowerCase()));
                }
                return false;
            })
            .collect(Collectors.toList());
    }

    @Override
    public boolean matchRegion(ProcurementInfo procurementInfo, List<String> serviceRegions) {
        String region = procurementInfo.getRegion();
        if (region == null) return false;
        
        return serviceRegions.stream()
            .anyMatch(serviceRegion -> region.contains(serviceRegion));
    }

    @Override
    public boolean matchBudget(ProcurementInfo procurementInfo, Map<String, Long> budgetRange) {
        Long budget = procurementInfo.getBudget();
        if (budget == null) return true; // 没有预算信息的默认匹配
        
        Long minBudget = budgetRange.get("min");
        Long maxBudget = budgetRange.get("max");
        
        return budget >= minBudget && budget <= maxBudget;
    }

    @Override
    public Map<String, Object> getMatchRules() {
        return new HashMap<>(matchRules);
    }

    @Override
    public void updateMatchRules(Map<String, Object> rules) {
        this.matchRules.putAll(rules);
        log.info("更新匹配规则: {}", rules);
    }

    @Override
    public Map<String, Object> getMatchStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        // 总匹配数量
        QueryWrapper<BusinessOpportunity> wrapper = new QueryWrapper<>();
        wrapper.eq("source_type", "自动匹配");
        int totalMatched = businessOpportunityMapper.selectCount(wrapper);
        statistics.put("totalMatched", totalMatched);

        // 今日匹配数量
        wrapper = new QueryWrapper<>();
        wrapper.eq("source_type", "自动匹配");
        wrapper.likeRight("create_time", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        int todayMatched = businessOpportunityMapper.selectCount(wrapper);
        statistics.put("todayMatched", todayMatched);

        // 平均匹配分数
        wrapper = new QueryWrapper<>();
        wrapper.eq("source_type", "自动匹配");
        wrapper.select("AVG(match_score) as avg_score");
        Map<String, Object> result = businessOpportunityMapper.selectMaps(wrapper).stream().findFirst().orElse(new HashMap<>());
        Double avgScore = (Double) result.getOrDefault("avg_score", 0.0);
        statistics.put("averageMatchScore", String.format("%.2f", avgScore));

        // 高匹配分数商机数量（>=80分）
        wrapper = new QueryWrapper<>();
        wrapper.eq("source_type", "自动匹配");
        wrapper.ge("match_score", 80);
        int highScoreCount = businessOpportunityMapper.selectCount(wrapper);
        statistics.put("highScoreOpportunities", highScoreCount);

        return statistics;
    }

    /**
     * 构建公司档案（模拟数据，实际应该从数据库获取）
     */
    private Map<String, Object> buildCompanyProfile() {
        Map<String, Object> profile = new HashMap<>();
        profile.put("industries", Arrays.asList("I65", "I64", "M74")); // 信息技术、互联网、专业技术服务
        profile.put("keywords", COMPANY_KEYWORDS);
        profile.put("regions", SERVICE_REGIONS);
        profile.put("budget_range", BUDGET_RANGE);
        return profile;
    }

    /**
     * 创建商机线索
     */
    private BusinessOpportunity createBusinessOpportunity(ProcurementInfo procurementInfo, int matchScore) {
        BusinessOpportunity opportunity = new BusinessOpportunity();
        opportunity.setName("【自动匹配】" + procurementInfo.getTitle());
        opportunity.setCustomerName("政府采购");
        opportunity.setAmount(procurementInfo.getBudget() != null ? procurementInfo.getBudget().doubleValue() : 0.0);
        opportunity.setStage("初步接触");
        opportunity.setSource("自动匹配");
        opportunity.setSourceType("政府采购");
        opportunity.setDescription(procurementInfo.getContent());
        opportunity.setContactName(procurementInfo.getContactName());
        opportunity.setContactPhone(procurementInfo.getContactPhone());
        opportunity.setContactEmail(procurementInfo.getContactEmail());
        opportunity.setRegion(procurementInfo.getRegion());
        opportunity.setProcurementType(procurementInfo.getProcurementType());
        opportunity.setDeadline(procurementInfo.getDeadline());
        opportunity.setMatchScore(matchScore);
        opportunity.setStatus("active");
        opportunity.setCreateTime(new Date());
        opportunity.setUpdateTime(new Date());

        // 保存到数据库
        businessOpportunityMapper.insert(opportunity);
        
        return opportunity;
    }

    /**
     * 计算截止日期分数
     * 截止日期越近，分数越高
     */
    private double calculateDeadlineScore(ProcurementInfo procurementInfo) {
        Long deadline = procurementInfo.getDeadline();
        if (deadline == null) return 50; // 默认中等分数
        
        long currentTime = System.currentTimeMillis();
        long timeDiff = deadline - currentTime;
        
        // 转换为天数
        long daysDiff = timeDiff / (1000 * 60 * 60 * 24);
        
        if (daysDiff < 0) {
            return 0; // 已过期
        } else if (daysDiff <= 7) {
            return 100; // 一周内，紧急
        } else if (daysDiff <= 30) {
            return 80; // 一个月内，较紧急
        } else if (daysDiff <= 90) {
            return 60; // 三个月内，一般
        } else {
            return 30; // 三个月以上，不紧急
        }
    }
}