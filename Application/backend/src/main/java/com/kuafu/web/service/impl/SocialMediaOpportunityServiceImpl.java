package com.kuafu.web.service.impl;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuafu.web.entity.BusinessOpportunity;
import com.kuafu.web.entity.SocialMediaContent;
import com.kuafu.web.mapper.SocialMediaContentMapper;
import com.kuafu.web.service.IBusinessOpportunityService;
import com.kuafu.web.service.ISocialMediaOpportunityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 社交媒体商机获取服务实现类
 * 支持抖音、微信、小红书等平台的种草内容分析和商机提取
 */
@Slf4j
@Service
public class SocialMediaOpportunityServiceImpl implements ISocialMediaOpportunityService {

    @Autowired
    private SocialMediaContentMapper socialMediaContentMapper;

    @Autowired
    private IBusinessOpportunityService businessOpportunityService;

    // 产品关键词库
    private static final Map<String, List<String>> PRODUCT_KEYWORDS = new HashMap<>();
    static {
        PRODUCT_KEYWORDS.put("办公用品", Arrays.asList("办公", "文具", "打印", "复印", "纸张", "笔", "文件夹", "订书机"));
        PRODUCT_KEYWORDS.put("IT设备", Arrays.asList("电脑", "笔记本", "服务器", "网络", "软件", "系统", "硬件", "数码"));
        PRODUCT_KEYWORDS.put("家具", Arrays.asList("办公桌", "椅子", "沙发", "柜子", "家具", "装修", "装饰"));
        PRODUCT_KEYWORDS.put("设备维护", Arrays.asList("维修", "保养", "维护", "检修", "更换", "升级", "改造"));
        PRODUCT_KEYWORDS.put("服务采购", Arrays.asList("服务", "咨询", "培训", "外包", "代理", "中介", "顾问"));
        PRODUCT_KEYWORDS.put("营销用品", Arrays.asList("广告", "宣传", "推广", "营销", "品牌", "包装", "印刷"));
    }

    // 品牌关键词库
    private static final Set<String> BRAND_KEYWORDS = new HashSet<>(Arrays.asList(
        "华为", "联想", "戴尔", "惠普", "苹果", "小米", "三星", "索尼", "佳能", "爱普生",
        "得力", "晨光", "齐心", "真彩", "广博", "三木", "金得利", "永丰", "英雄", "毕加索"
    ));

    // 需求信号词
    private static final Set<String> DEMAND_SIGNALS = new HashSet<>(Arrays.asList(
        "需要", "想要", "寻找", "求推荐", "求购", "采购", "购买", "打算", "计划", "准备",
        "考虑", "需求", "急需", "急求", "求介绍", "哪里买", "怎么买", "多少钱", "价格", "报价"
    ));

    // 情感分析关键词
    private static final Set<String> POSITIVE_WORDS = new HashSet<>(Arrays.asList(
        "好", "不错", "满意", "喜欢", "推荐", "优秀", "优质", "专业", "靠谱", "值得信赖",
        "赞", "棒", "完美", "超值", "划算", "实用", "方便", "高效", "质量好", "服务好"
    ));

    private static final Set<String> NEGATIVE_WORDS = new HashSet<>(Arrays.asList(
        "差", "不好", "失望", "垃圾", "坑", "骗", "贵", "不值", "后悔", "别买",
        "踩雷", "避雷", "不推荐", "差评", "投诉", "退货", "退款", "质量问题", "服务差"
    ));

    @Override
    public List<BusinessOpportunity> analyzeDouyinContent(String videoUrl, String videoTitle, String videoDescription, Map<String, Object> authorInfo) {
        try {
            log.info("开始分析抖音视频内容: {}", videoTitle);
            
            // 保存原始内容
            SocialMediaContent content = new SocialMediaContent();
            content.setPlatform("douyin");
            content.setContentType("video");
            content.setTitle(videoTitle);
            content.setContent(videoDescription);
            content.setOriginalUrl(videoUrl);
            content.setAuthorId((String) authorInfo.get("authorId"));
            content.setAuthorName((String) authorInfo.get("authorName"));
            content.setAuthorFollowers((Integer) authorInfo.getOrDefault("followers", 0));
            content.setLikes((Integer) authorInfo.getOrDefault("likes", 0));
            content.setComments((Integer) authorInfo.getOrDefault("comments", 0));
            content.setShares((Integer) authorInfo.getOrDefault("shares", 0));
            content.setViews((Integer) authorInfo.getOrDefault("views", 0));
            content.setPublishTime((Date) authorInfo.get("publishTime"));
            content.setCollectTime(new Date());
            content.setProcessed(false);
            content.setStatus("active");
            
            // 提取关键词和标签
            String fullContent = videoTitle + " " + videoDescription;
            content.setKeywords(extractKeywords(fullContent));
            content.setTags(JSONUtil.toJsonStr(extractTags(fullContent)));
            
            // 情感分析
            content.setSentiment(analyzeSentiment(fullContent));
            
            // 商业价值评估
            Map<String, Object> commercialData = evaluateCommercialValue(content);
            content.setCommercialScore((Integer) commercialData.get("score"));
            content.setConversionPotential((String) commercialData.get("potential"));
            
            socialMediaContentMapper.insert(content);
            
            // 提取商机
            List<BusinessOpportunity> opportunities = extractOpportunitiesFromContent(content);
            
            // 标记为已处理
            content.setProcessed(true);
            content.setProcessResult(opportunities.isEmpty() ? "no_opportunity" : "opportunity_found");
            content.setProcessTime(new Date());
            socialMediaContentMapper.updateById(content);
            
            log.info("抖音视频分析完成，找到 {} 个商机", opportunities.size());
            return opportunities;
            
        } catch (Exception e) {
            log.error("分析抖音视频内容失败: {}", videoTitle, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<BusinessOpportunity> analyzeWechatArticle(String articleUrl, String articleTitle, String articleContent, Map<String, Object> authorInfo) {
        try {
            log.info("开始分析微信公众号文章: {}", articleTitle);
            
            SocialMediaContent content = new SocialMediaContent();
            content.setPlatform("wechat");
            content.setContentType("article");
            content.setTitle(articleTitle);
            content.setContent(articleContent);
            content.setOriginalUrl(articleUrl);
            content.setAuthorId((String) authorInfo.get("authorId"));
            content.setAuthorName((String) authorInfo.get("authorName"));
            content.setViews((Integer) authorInfo.getOrDefault("readCount", 0));
            content.setLikes((Integer) authorInfo.getOrDefault("likeCount", 0));
            content.setComments((Integer) authorInfo.getOrDefault("commentCount", 0));
            content.setFavorites((Integer) authorInfo.getOrDefault("favoriteCount", 0));
            content.setPublishTime((Date) authorInfo.get("publishTime"));
            content.setCollectTime(new Date());
            content.setProcessed(false);
            content.setStatus("active");
            
            String fullContent = articleTitle + " " + articleContent;
            content.setKeywords(extractKeywords(fullContent));
            content.setTags(JSONUtil.toJsonStr(extractTags(fullContent)));
            content.setSentiment(analyzeSentiment(fullContent));
            
            Map<String, Object> commercialData = evaluateCommercialValue(content);
            content.setCommercialScore((Integer) commercialData.get("score"));
            content.setConversionPotential((String) commercialData.get("potential"));
            
            socialMediaContentMapper.insert(content);
            
            List<BusinessOpportunity> opportunities = extractOpportunitiesFromContent(content);
            
            content.setProcessed(true);
            content.setProcessResult(opportunities.isEmpty() ? "no_opportunity" : "opportunity_found");
            content.setProcessTime(new Date());
            socialMediaContentMapper.updateById(content);
            
            log.info("微信公众号文章分析完成，找到 {} 个商机", opportunities.size());
            return opportunities;
            
        } catch (Exception e) {
            log.error("分析微信公众号文章失败: {}", articleTitle, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<BusinessOpportunity> analyzeXiaohongshuNote(String noteUrl, String noteTitle, String noteContent, Map<String, Object> authorInfo) {
        try {
            log.info("开始分析小红书笔记: {}", noteTitle);
            
            SocialMediaContent content = new SocialMediaContent();
            content.setPlatform("xiaohongshu");
            content.setContentType("note");
            content.setTitle(noteTitle);
            content.setContent(noteContent);
            content.setOriginalUrl(noteUrl);
            content.setAuthorId((String) authorInfo.get("authorId"));
            content.setAuthorName((String) authorInfo.get("authorName"));
            content.setAuthorFollowers((Integer) authorInfo.getOrDefault("followers", 0));
            content.setLikes((Integer) authorInfo.getOrDefault("likes", 0));
            content.setComments((Integer) authorInfo.getOrDefault("comments", 0));
            content.setFavorites((Integer) authorInfo.getOrDefault("collects", 0));
            content.setShares((Integer) authorInfo.getOrDefault("shares", 0));
            content.setPublishTime((Date) authorInfo.get("publishTime"));
            content.setCollectTime(new Date());
            content.setProcessed(false);
            content.setStatus("active");
            
            String fullContent = noteTitle + " " + noteContent;
            content.setKeywords(extractKeywords(fullContent));
            content.setTags(JSONUtil.toJsonStr(extractTags(fullContent)));
            content.setSentiment(analyzeSentiment(fullContent));
            
            Map<String, Object> commercialData = evaluateCommercialValue(content);
            content.setCommercialScore((Integer) commercialData.get("score"));
            content.setConversionPotential((String) commercialData.get("potential"));
            
            socialMediaContentMapper.insert(content);
            
            List<BusinessOpportunity> opportunities = extractOpportunitiesFromContent(content);
            
            content.setProcessed(true);
            content.setProcessResult(opportunities.isEmpty() ? "no_opportunity" : "opportunity_found");
            content.setProcessTime(new Date());
            socialMediaContentMapper.updateById(content);
            
            log.info("小红书笔记分析完成，找到 {} 个商机", opportunities.size());
            return opportunities;
            
        } catch (Exception e) {
            log.error("分析小红书笔记失败: {}", noteTitle, e);
            return new ArrayList<>();
        }
    }

    @Override
    public Map<String, List<BusinessOpportunity>> analyzeBatchContent(List<SocialMediaContent> socialMediaContents) {
        Map<String, List<BusinessOpportunity>> results = new HashMap<>();
        
        for (SocialMediaContent content : socialMediaContents) {
            try {
                List<BusinessOpportunity> opportunities = extractOpportunitiesFromContent(content);
                results.put(content.getId().toString(), opportunities);
                
                // 更新处理状态
                content.setProcessed(true);
                content.setProcessResult(opportunities.isEmpty() ? "no_opportunity" : "opportunity_found");
                content.setProcessTime(new Date());
                socialMediaContentMapper.updateById(content);
                
            } catch (Exception e) {
                log.error("批量处理社交媒体内容失败: {}", content.getId(), e);
                results.put(content.getId().toString(), new ArrayList<>());
            }
        }
        
        return results;
    }

    @Override
    public List<Map<String, Object>> extractProductRequirements(String content, String platform) {
        List<Map<String, Object>> requirements = new ArrayList<>();
        
        if (StrUtil.isBlank(content)) {
            return requirements;
        }
        
        // 检查需求信号词
        Set<String> foundSignals = new HashSet<>();
        for (String signal : DEMAND_SIGNALS) {
            if (content.contains(signal)) {
                foundSignals.add(signal);
            }
        }
        
        if (!foundSignals.isEmpty()) {
            // 检查产品类别
            for (Map.Entry<String, List<String>> entry : PRODUCT_KEYWORDS.entrySet()) {
                String category = entry.getKey();
                List<String> keywords = entry.getValue();
                
                List<String> foundKeywords = keywords.stream()
                    .filter(keyword -> content.contains(keyword))
                    .collect(Collectors.toList());
                
                if (!foundKeywords.isEmpty()) {
                    Map<String, Object> requirement = new HashMap<>();
                    requirement.put("category", category);
                    requirement.put("keywords", foundKeywords);
                    requirement.put("signals", new ArrayList<>(foundSignals));
                    requirement.put("confidence", calculateConfidence(content, foundKeywords, foundSignals));
                    requirements.add(requirement);
                }
            }
        }
        
        return requirements;
    }

    @Override
    public List<Map<String, Object>> extractBrandMentions(String content) {
        List<Map<String, Object>> brandMentions = new ArrayList<>();
        
        if (StrUtil.isBlank(content)) {
            return brandMentions;
        }
        
        for (String brand : BRAND_KEYWORDS) {
            if (content.contains(brand)) {
                Map<String, Object> mention = new HashMap<>();
                mention.put("brand", brand);
                mention.put("sentiment", analyzeBrandSentiment(content, brand));
                mention.put("context", extractContext(content, brand));
                brandMentions.add(mention);
            }
        }
        
        return brandMentions;
    }

    @Override
    public Map<String, Object> evaluateCommercialValue(SocialMediaContent content) {
        Map<String, Object> evaluation = new HashMap<>();
        
        int score = 0;
        String potential = "low";
        
        // 基础分数（根据互动数据）
        if (content.getViews() != null && content.getViews() > 10000) score += 20;
        if (content.getLikes() != null && content.getLikes() > 1000) score += 15;
        if (content.getComments() != null && content.getComments() > 100) score += 15;
        if (content.getShares() != null && content.getShares() > 50) score += 10;
        
        // 作者影响力
        if (content.getAuthorFollowers() != null && content.getAuthorFollowers() > 10000) score += 20;
        
        // 内容质量分数
        String fullContent = content.getTitle() + " " + content.getContent();
        List<Map<String, Object>> requirements = extractProductRequirements(fullContent, content.getPlatform());
        if (!requirements.isEmpty()) score += 20;
        
        // 情感倾向
        if ("positive".equals(content.getSentiment())) score += 10;
        else if ("negative".equals(content.getSentiment())) score -= 10;
        
        // 确定转化潜力
        if (score >= 80) potential = "high";
        else if (score >= 50) potential = "medium";
        
        evaluation.put("score", Math.max(0, Math.min(100, score)));
        evaluation.put("potential", potential);
        evaluation.put("factors", Arrays.asList(
            "互动数据: " + (content.getViews() != null ? content.getViews() : 0) + " 观看",
            "作者影响力: " + (content.getAuthorFollowers() != null ? content.getAuthorFollowers() : 0) + " 粉丝",
            "产品需求: " + (requirements.isEmpty() ? "未识别" : "已识别" + requirements.size() + "个"),
            "情感倾向: " + content.getSentiment()
        ));
        
        return evaluation;
    }

    @Override
    public List<Map<String, Object>> getTrendingTopics(String platform, String category, String timeRange) {
        List<Map<String, Object>> trendingTopics = new ArrayList<>();
        
        try {
            QueryWrapper<SocialMediaContent> wrapper = new QueryWrapper<>();
            wrapper.eq("platform", platform);
            wrapper.eq("status", "active");
            
            // 根据时间范围筛选
            Date endDate = new Date();
            Date startDate = calculateStartDate(timeRange);
            wrapper.between("publish_time", startDate, endDate);
            
            List<SocialMediaContent> contents = socialMediaContentMapper.selectList(wrapper);
            
            // 话题统计
            Map<String, Integer> topicCount = new HashMap<>();
            Map<String, Integer> topicEngagement = new HashMap<>();
            
            for (SocialMediaContent content : contents) {
                String tags = content.getTags();
                if (StrUtil.isNotBlank(tags)) {
                    try {
                        List<String> tagList = JSONUtil.toList(JSONUtil.parseArray(tags), String.class);
                        for (String tag : tagList) {
                            topicCount.put(tag, topicCount.getOrDefault(tag, 0) + 1);
                            int engagement = (content.getLikes() != null ? content.getLikes() : 0) + 
                                           (content.getComments() != null ? content.getComments() : 0) +
                                           (content.getShares() != null ? content.getShares() : 0);
                            topicEngagement.put(tag, topicEngagement.getOrDefault(tag, 0) + engagement);
                        }
                    } catch (Exception e) {
                        log.warn("解析标签失败: {}", tags, e);
                    }
                }
            }
            
            // 排序并返回热门话题
            topicCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(20)
                .forEach(entry -> {
                    Map<String, Object> topic = new HashMap<>();
                    topic.put("topic", entry.getKey());
                    topic.put("count", entry.getValue());
                    topic.put("engagement", topicEngagement.getOrDefault(entry.getKey(), 0));
                    trendingTopics.add(topic);
                });
                
        } catch (Exception e) {
            log.error("获取热门话题失败", e);
        }
        
        return trendingTopics;
    }

    @Override
    public Map<String, Object> monitorKeywordMentions(List<String> keywords, List<String> platforms, int monitoringPeriod) {
        Map<String, Object> monitoringResult = new HashMap<>();
        
        try {
            QueryWrapper<SocialMediaContent> wrapper = new QueryWrapper<>();
            wrapper.in("platform", platforms);
            wrapper.eq("status", "active");
            
            // 构建关键词查询条件
            if (!keywords.isEmpty()) {
                StringBuilder keywordCondition = new StringBuilder();
                for (int i = 0; i < keywords.size(); i++) {
                    if (i > 0) keywordCondition.append(" OR ");
                    keywordCondition.append("title LIKE '%").append(keywords.get(i)).append("%'");
                    keywordCondition.append(" OR content LIKE '%").append(keywords.get(i)).append("%'");
                }
                wrapper.apply("(" + keywordCondition.toString() + ")");
            }
            
            Date endDate = new Date();
            Date startDate = new Date(System.currentTimeMillis() - monitoringPeriod * 60 * 60 * 1000L);
            wrapper.between("publish_time", startDate, endDate);
            
            List<SocialMediaContent> mentions = socialMediaContentMapper.selectList(wrapper);
            
            monitoringResult.put("totalMentions", mentions.size());
            monitoringResult.put("keywords", keywords);
            monitoringResult.put("platforms", platforms);
            monitoringResult.put("monitoringPeriod", monitoringPeriod);
            monitoringResult.put("mentions", mentions);
            
        } catch (Exception e) {
            log.error("监控关键词提及失败", e);
        }
        
        return monitoringResult;
    }

    @Override
    public Map<String, Object> generateSocialMediaReport(String startDate, String endDate, List<String> platforms) {
        Map<String, Object> report = new HashMap<>();
        
        try {
            QueryWrapper<SocialMediaContent> wrapper = new QueryWrapper<>();
            wrapper.in("platform", platforms);
            wrapper.eq("status", "active");
            wrapper.between("publish_time", startDate, endDate);
            
            List<SocialMediaContent> contents = socialMediaContentMapper.selectList(wrapper);
            
            // 基础统计
            int totalContent = contents.size();
            int processedContent = (int) contents.stream().filter(c -> c.getProcessed() != null && c.getProcessed()).count();
            int opportunityFound = (int) contents.stream().filter(c -> "opportunity_found".equals(c.getProcessResult())).count();
            
            // 平台分布
            Map<String, Long> platformDistribution = contents.stream()
                .collect(Collectors.groupingBy(SocialMediaContent::getPlatform, Collectors.counting()));
            
            // 平均商业价值分数
            double avgCommercialScore = contents.stream()
                .filter(c -> c.getCommercialScore() != null)
                .mapToInt(SocialMediaContent::getCommercialScore)
                .average()
                .orElse(0.0);
            
            report.put("totalContent", totalContent);
            report.put("processedContent", processedContent);
            report.put("opportunityFound", opportunityFound);
            report.put("conversionRate", totalContent > 0 ? String.format("%.2f%%", (double) opportunityFound / totalContent * 100) : "0%");
            report.put("platformDistribution", platformDistribution);
            report.put("averageCommercialScore", String.format("%.1f", avgCommercialScore));
            report.put("reportPeriod", startDate + " 至 " + endDate);
            report.put("platforms", platforms);
            
        } catch (Exception e) {
            log.error("生成社交媒体报告失败", e);
        }
        
        return report;
    }

    @Override
    public boolean setMonitoringConfig(Map<String, Object> config) {
        // 保存监控配置到数据库或缓存
        log.info("设置社交媒体监控配置: {}", config);
        return true;
    }

    @Override
    public Map<String, Object> getMonitoringStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("isRunning", true);
        status.put("monitoredPlatforms", Arrays.asList("douyin", "wechat", "xiaohongshu"));
        status.put("lastUpdateTime", new Date());
        status.put("totalMonitoredContent", socialMediaContentMapper.selectCount(null));
        return status;
    }

    @Override
    public Map<String, Object> analyzeCompetitorPerformance(List<String> competitors, List<String> platforms, String analysisPeriod) {
        Map<String, Object> analysis = new HashMap<>();
        
        // 竞争对手分析逻辑
        Map<String, Object> competitorData = new HashMap<>();
        for (String competitor : competitors) {
            Map<String, Object> data = new HashMap<>();
            data.put("mentions", 0); // 提及次数
            data.put("engagement", 0); // 互动总量
            data.put("sentiment", "neutral"); // 情感倾向
            competitorData.put(competitor, data);
        }
        
        analysis.put("competitors", competitorData);
        analysis.put("analysisPeriod", analysisPeriod);
        analysis.put("platforms", platforms);
        
        return analysis;
    }

    // ============== 私有辅助方法 ==============

    /**
     * 从内容中提取商机
     */
    private List<BusinessOpportunity> extractOpportunitiesFromContent(SocialMediaContent content) {
        List<BusinessOpportunity> opportunities = new ArrayList<>();
        
        String fullContent = content.getTitle() + " " + content.getContent();
        List<Map<String, Object>> requirements = extractProductRequirements(fullContent, content.getPlatform());
        
        for (Map<String, Object> requirement : requirements) {
            try {
                String category = (String) requirement.get("category");
                List<String> keywords = (List<String>) requirement.get("keywords");
                List<String> signals = (List<String>) requirement.get("signals");
                Double confidence = (Double) requirement.get("confidence");
                
                if (confidence > 0.6) { // 置信度阈值
                    BusinessOpportunity opportunity = new BusinessOpportunity();
                    opportunity.setTitle("【" + content.getPlatform() + "】" + content.getTitle());
                    opportunity.setDescription(buildOpportunityDescription(content, category, keywords, signals));
                    opportunity.setCategory(category);
                    opportunity.setSource("social_media_" + content.getPlatform());
                    opportunity.setSourceId(content.getId().toString());
                    opportunity.setSourceUrl(content.getOriginalUrl());
                    opportunity.setContactInfo(content.getAuthorName());
                    opportunity.setCompany(content.getAuthorName());
                    opportunity.setStatus("new");
                    opportunity.setPriority(calculatePriority(content, confidence));
                    opportunity.setScore((int) (confidence * 100));
                    opportunity.setTags(String.join(",", keywords));
                    opportunity.setCreatedTime(new Date());
                    opportunity.setUpdatedTime(new Date());
                    
                    opportunities.add(opportunity);
                }
            } catch (Exception e) {
                log.error("提取商机失败", e);
            }
        }
        
        return opportunities;
    }

    /**
     * 提取关键词
     */
    private String extractKeywords(String content) {
        Set<String> keywords = new HashSet<>();
        
        // 产品关键词
        for (List<String> productKeywords : PRODUCT_KEYWORDS.values()) {
            for (String keyword : productKeywords) {
                if (content.contains(keyword)) {
                    keywords.add(keyword);
                }
            }
        }
        
        // 品牌关键词
        for (String brand : BRAND_KEYWORDS) {
            if (content.contains(brand)) {
                keywords.add(brand);
            }
        }
        
        // 需求信号词
        for (String signal : DEMAND_SIGNALS) {
            if (content.contains(signal)) {
                keywords.add(signal);
            }
        }
        
        return String.join(",", keywords);
    }

    /**
     * 提取标签
     */
    private List<String> extractTags(String content) {
        List<String> tags = new ArrayList<>();
        
        // 简单的标签提取逻辑
        if (content.contains("#")) {
            List<String> hashTags = ReUtil.findAll("#\\S+", content, 0);
            tags.addAll(hashTags);
        }
        
        return tags;
    }

    /**
     * 情感分析
     */
    private String analyzeSentiment(String content) {
        int positiveCount = 0;
        int negativeCount = 0;
        
        for (String word : POSITIVE_WORDS) {
            if (content.contains(word)) {
                positiveCount++;
            }
        }
        
        for (String word : NEGATIVE_WORDS) {
            if (content.contains(word)) {
                negativeCount++;
            }
        }
        
        if (positiveCount > negativeCount) return "positive";
        else if (negativeCount > positiveCount) return "negative";
        else return "neutral";
    }

    /**
     * 计算置信度
     */
    private double calculateConfidence(String content, List<String> keywords, Set<String> signals) {
        double keywordScore = (double) keywords.size() / 5; // 最多5分
        double signalScore = (double) signals.size() / 3;  // 最多3分
        double lengthScore = Math.min(content.length() / 1000.0, 2.0); // 长度分数，最多2分
        
        return Math.min(1.0, (keywordScore + signalScore + lengthScore) / 10.0);
    }

    /**
     * 分析品牌情感
     */
    private String analyzeBrandSentiment(String content, String brand) {
        String context = extractContext(content, brand);
        return analyzeSentiment(context);
    }

    /**
     * 提取上下文
     */
    private String extractContext(String content, String keyword) {
        int index = content.indexOf(keyword);
        if (index == -1) return "";
        
        int start = Math.max(0, index - 50);
        int end = Math.min(content.length(), index + keyword.length() + 50);
        
        return content.substring(start, end);
    }

    /**
     * 计算开始日期
     */
    private Date calculateStartDate(String timeRange) {
        long now = System.currentTimeMillis();
        switch (timeRange) {
            case "7d":
                return new Date(now - 7 * 24 * 60 * 60 * 1000L);
            case "30d":
                return new Date(now - 30 * 24 * 60 * 60 * 1000L);
            case "90d":
                return new Date(now - 90 * 24 * 60 * 60 * 1000L);
            default:
                return new Date(now - 7 * 24 * 60 * 60 * 1000L);
        }
    }

    /**
     * 构建商机描述
     */
    private String buildOpportunityDescription(SocialMediaContent content, String category, List<String> keywords, List<String> signals) {
        StringBuilder description = new StringBuilder();
        description.append("来自").append(content.getPlatform()).append("的商机线索\n");
        description.append("内容标题：").append(content.getTitle()).append("\n");
        description.append("产品类别：").append(category).append("\n");
        description.append("相关关键词：").append(String.join(", ", keywords)).append("\n");
        description.append("需求信号：").append(String.join(", ", signals)).append("\n");
        description.append("作者：").append(content.getAuthorName()).append("\n");
        description.append("互动数据：").append(content.getLikes()).append("赞 ")
                  .append(content.getComments()).append("评论 ")
                  .append(content.getShares()).append("分享\n");
        description.append("原文链接：").append(content.getOriginalUrl());
        
        return description.toString();
    }

    /**
     * 计算优先级
     */
    private String calculatePriority(SocialMediaContent content, double confidence) {
        int score = content.getCommercialScore() != null ? content.getCommercialScore() : 0;
        score += (int) (confidence * 30);
        
        if (score >= 80) return "high";
        else if (score >= 50) return "medium";
        else return "low";
    }
}