package com.kuafu.web.service.impl;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.kuafu.web.service.ISocialMediaComplianceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 社交媒体内容合规性检查服务实现类
 * 确保从抖音、微信、小红书等平台获取的内容符合法律法规和平台政策
 */
@Slf4j
@Service
public class SocialMediaComplianceServiceImpl implements ISocialMediaComplianceService {

    // 敏感内容模式
    private static final Map<String, Pattern> SENSITIVE_PATTERNS = new HashMap<>();
    static {
        // 政治敏感内容
        SENSITIVE_PATTERNS.put("political", Pattern.compile("(反党|反政府|颠覆国家|分裂国家|危害国家安全)", Pattern.CASE_INSENSITIVE));
        
        // 暴力恐怖内容
        SENSITIVE_PATTERNS.put("violence", Pattern.compile("(暴力|恐怖|血腥|杀戮|爆炸|炸弹|枪支)", Pattern.CASE_INSENSITIVE));
        
        // 色情低俗内容
        SENSITIVE_PATTERNS.put("pornography", Pattern.compile("(色情|淫秽|低俗|性暗示|裸体|成人用品)", Pattern.CASE_INSENSITIVE));
        
        // 虚假广告
        SENSITIVE_PATTERNS.put("false_advertising", Pattern.compile("(100%有效|包治百病|无副作用|永不反弹|立即见效)", Pattern.CASE_INSENSITIVE));
        
        // 金融诈骗
        SENSITIVE_PATTERNS.put("financial_fraud", Pattern.compile("(稳赚不赔|零风险投资|高收益无风险|保本高收益|内部消息)", Pattern.CASE_INSENSITIVE));
        
        // 医疗虚假
        SENSITIVE_PATTERNS.put("medical_false", Pattern.compile("(祖传秘方|包治百病|无副作用|永不复发|立即治愈)", Pattern.CASE_INSENSITIVE));
        
        // 违禁品
        SENSITIVE_PATTERNS.put("prohibited_items", Pattern.compile("(毒品|枪支|弹药|爆炸物|管制刀具|假币)", Pattern.CASE_INSENSITIVE));
        
        // 个人隐私过度暴露（仅检查身份证号、银行卡号等真正敏感信息）
        SENSITIVE_PATTERNS.put("privacy", Pattern.compile("(\\d{15}|\\d{18}|\\d{16,19})", Pattern.CASE_INSENSITIVE));
    }

    // 平台特定规则
    private static final Map<String, Map<String, Object>> PLATFORM_RULES = new HashMap<>();
    static {
        // 抖音规则
        Map<String, Object> douyinRules = new HashMap<>();
        douyinRules.put("max_video_duration", 600); // 10分钟
        douyinRules.put("min_followers_for_commercial", 1000);
        douyinRules.put("prohibited_categories", Arrays.asList("医疗", "药品", "保健品", "金融投资", "烟草", "酒精"));
        PLATFORM_RULES.put("douyin", douyinRules);
        
        // 微信规则
        Map<String, Object> wechatRules = new HashMap<>();
        wechatRules.put("min_article_length", 300);
        wechatRules.put("max_advertising_ratio", 0.3);
        wechatRules.put("prohibited_categories", Arrays.asList("色情", "赌博", "毒品", "枪支", "政治"));
        PLATFORM_RULES.put("wechat", wechatRules);
        
        // 小红书规则
        Map<String, Object> xiaohongshuRules = new HashMap<>();
        xiaohongshuRules.put("min_note_length", 50);
        xiaohongshuRules.put("max_hashtags", 10);
        xiaohongshuRules.put("prohibited_categories", Arrays.asList("医疗", "药品", "烟草", "酒精", "成人用品"));
        PLATFORM_RULES.put("xiaohongshu", xiaohongshuRules);
    }

    // 合规关键词库
    private static final Set<String> COMPLIANCE_KEYWORDS = new HashSet<>(Arrays.asList(
        "正品", "官方", "授权", "认证", "合格", "检验", "备案", "注册",
        "环保", "绿色", "健康", "安全", "无害", "天然", "有机",
        "专业", "专注", "品质", "服务", "创新", "技术", "研发"
    ));

    // 风险关键词库
    private static final Set<String> RISK_KEYWORDS = new HashSet<>(Arrays.asList(
        "最便宜", "最低价", "史上最低", "跳楼价", "血亏", "亏本", "倒闭",
        "绝对", "100%", "保证", "承诺", "一定", "肯定", "必须",
        "第一", "唯一", "最好", "最佳", "顶级", "终极", "完美"
    ));

    @Override
    public Map<String, Object> checkContentCompliance(String content, String platform, String contentType) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            log.info("开始检查社交媒体内容合规性，平台: {}, 类型: {}", platform, contentType);
            
            boolean isCompliant = true;
            List<String> violations = new ArrayList<>();
            List<String> warnings = new ArrayList<>();
            Map<String, Object> details = new HashMap<>();
            
            // 1. 基础内容检查
            Map<String, Object> basicCheck = checkBasicContent(content);
            if (!(Boolean) basicCheck.get("passed")) {
                isCompliant = false;
                violations.addAll((List<String>) basicCheck.get("violations"));
            }
            warnings.addAll((List<String>) basicCheck.get("warnings"));
            details.put("basicCheck", basicCheck);
            
            // 2. 平台特定规则检查
            if (platform != null) {
                Map<String, Object> platformCheck = checkPlatformSpecificRules(content, platform, contentType);
                if (!(Boolean) platformCheck.get("passed")) {
                    isCompliant = false;
                    violations.addAll((List<String>) platformCheck.get("violations"));
                }
                warnings.addAll((List<String>) platformCheck.get("warnings"));
                details.put("platformCheck", platformCheck);
            }
            
            // 3. 敏感内容检测
            Map<String, Object> sensitiveCheck = checkSensitiveContent(content);
            if (!(Boolean) sensitiveCheck.get("passed")) {
                isCompliant = false;
                violations.addAll((List<String>) sensitiveCheck.get("violations"));
            }
            details.put("sensitiveCheck", sensitiveCheck);
            
            // 4. 广告合规检查
            Map<String, Object> advertisingCheck = checkAdvertisingCompliance(content);
            if (!(Boolean) advertisingCheck.get("passed")) {
                isCompliant = false;
                violations.addAll((List<String>) advertisingCheck.get("violations"));
            }
            warnings.addAll((List<String>) advertisingCheck.get("warnings"));
            details.put("advertisingCheck", advertisingCheck);
            
            // 5. 版权检查
            Map<String, Object> copyrightCheck = checkCopyrightIssues(content);
            if (!(Boolean) copyrightCheck.get("passed")) {
                warnings.addAll((List<String>) copyrightCheck.get("warnings"));
            }
            details.put("copyrightCheck", copyrightCheck);
            
            // 6. 计算合规分数
            int complianceScore = calculateComplianceScore(content, isCompliant, violations.size(), warnings.size());
            
            result.put("isCompliant", isCompliant);
            result.put("complianceScore", complianceScore);
            result.put("violations", violations);
            result.put("warnings", warnings);
            result.put("details", details);
            result.put("checkTime", new Date());
            
            log.info("社交媒体内容合规性检查完成，合规状态: {}, 分数: {}", isCompliant, complianceScore);
            
        } catch (Exception e) {
            log.error("社交媒体内容合规性检查失败", e);
            result.put("isCompliant", false);
            result.put("complianceScore", 0);
            result.put("error", e.getMessage());
        }
        
        return result;
    }

    @Override
    public Map<String, Object> checkBasicContent(String content) {
        Map<String, Object> result = new HashMap<>();
        List<String> violations = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        if (StrUtil.isBlank(content)) {
            violations.add("内容不能为空");
            result.put("passed", false);
            result.put("violations", violations);
            result.put("warnings", warnings);
            return result;
        }
        
        boolean passed = true;
        
        // 检查内容长度
        if (content.length() < 10) {
            violations.add("内容长度不能少于10个字符");
            passed = false;
        }
        
        if (content.length() > 10000) {
            warnings.add("内容长度超过10000字符，可能影响用户体验");
        }
        
        // 检查特殊字符比例
        long specialCharCount = content.chars().filter(ch -> !Character.isLetterOrDigit(ch) && !Character.isWhitespace(ch)).count();
        if ((double) specialCharCount / content.length() > 0.3) {
            warnings.add("特殊字符比例过高，可能影响可读性");
        }
        
        // 检查重复内容
        if (hasRepeatedContent(content)) {
            warnings.add("内容存在重复，可能影响质量评分");
        }
        
        result.put("passed", passed);
        result.put("violations", violations);
        result.put("warnings", warnings);
        result.put("contentLength", content.length());
        result.put("specialCharRatio", (double) specialCharCount / content.length());
        
        return result;
    }

    @Override
    public Map<String, Object> checkPlatformSpecificRules(String content, String platform, String contentType) {
        Map<String, Object> result = new HashMap<>();
        List<String> violations = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        Map<String, Object> platformRules = PLATFORM_RULES.get(platform.toLowerCase());
        if (platformRules == null) {
            result.put("passed", true);
            result.put("violations", violations);
            result.put("warnings", warnings);
            result.put("message", "未知平台，跳过特定规则检查");
            return result;
        }
        
        boolean passed = true;
        
        // 检查禁止类别
        List<String> prohibitedCategories = (List<String>) platformRules.get("prohibited_categories");
        if (prohibitedCategories != null) {
            for (String category : prohibitedCategories) {
                if (content.contains(category)) {
                    violations.add("包含平台禁止的类别: " + category);
                    passed = false;
                }
            }
        }
        
        // 平台特定检查
        switch (platform.toLowerCase()) {
            case "douyin":
                // 抖音特定检查
                if ("video".equals(contentType)) {
                    Integer maxDuration = (Integer) platformRules.get("max_video_duration");
                    if (maxDuration != null) {
                        // 这里需要实际的视频时长，暂时跳过
                        warnings.add("请确保视频时长不超过" + maxDuration + "秒");
                    }
                }
                break;
                
            case "wechat":
                // 微信公众号文章检查
                if ("article".equals(contentType)) {
                    Integer minLength = (Integer) platformRules.get("min_article_length");
                    if (minLength != null && content.length() < minLength) {
                        violations.add("文章长度不能少于" + minLength + "字符");
                        passed = false;
                    }
                    
                    // 检查广告比例
                    Double maxAdRatio = (Double) platformRules.get("max_advertising_ratio");
                    if (maxAdRatio != null) {
                        double adRatio = calculateAdvertisingRatio(content);
                        if (adRatio > maxAdRatio) {
                            warnings.add("广告内容比例过高: " + String.format("%.1f%%", adRatio * 100));
                        }
                    }
                }
                break;
                
            case "xiaohongshu":
                // 小红书笔记检查
                if ("note".equals(contentType)) {
                    Integer minLength = (Integer) platformRules.get("min_note_length");
                    if (minLength != null && content.length() < minLength) {
                        violations.add("笔记长度不能少于" + minLength + "字符");
                        passed = false;
                    }
                    
                    // 检查标签数量
                    Integer maxHashtags = (Integer) platformRules.get("max_hashtags");
                    if (maxHashtags != null) {
                        int hashtagCount = countHashtags(content);
                        if (hashtagCount > maxHashtags) {
                            warnings.add("标签数量过多: " + hashtagCount + "个，建议不超过" + maxHashtags + "个");
                        }
                    }
                }
                break;
        }
        
        result.put("passed", passed);
        result.put("violations", violations);
        result.put("warnings", warnings);
        result.put("platform", platform);
        result.put("contentType", contentType);
        
        return result;
    }

    @Override
    public Map<String, Object> checkSensitiveContent(String content) {
        Map<String, Object> result = new HashMap<>();
        List<String> violations = new ArrayList<>();
        Map<String, List<String>> foundSensitive = new HashMap<>();
        
        boolean passed = true;
        
        // 检查各类敏感内容
        for (Map.Entry<String, Pattern> entry : SENSITIVE_PATTERNS.entrySet()) {
            String category = entry.getKey();
            Pattern pattern = entry.getValue();
            
            List<String> matches = ReUtil.findAll(pattern, content, 0);
            if (!matches.isEmpty()) {
                violations.add("检测到" + getSensitiveCategoryName(category) + "内容");
                foundSensitive.put(category, matches);
                passed = false;
            }
        }
        
        result.put("passed", passed);
        result.put("violations", violations);
        result.put("foundSensitive", foundSensitive);
        
        return result;
    }

    @Override
    public Map<String, Object> checkAdvertisingCompliance(String content) {
        Map<String, Object> result = new HashMap<>();
        List<String> violations = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        boolean passed = true;
        
        // 检查虚假广告关键词
        for (String riskKeyword : RISK_KEYWORDS) {
            if (content.contains(riskKeyword)) {
                violations.add("包含夸大或虚假广告词汇: " + riskKeyword);
                passed = false;
            }
        }
        
        // 检查是否标明广告
        boolean hasAdDisclaimer = content.contains("广告") || content.contains("推广") || content.contains(" sponsored ");
        if (!hasAdDisclaimer && isLikelyAdvertising(content)) {
            warnings.add("内容疑似广告，建议明确标注");
        }
        
        // 检查医疗、保健声明
        if (content.contains("治疗") || content.contains("治愈") || content.contains("疗效")) {
            violations.add("普通商品不得宣传医疗效果");
            passed = false;
        }
        
        result.put("passed", passed);
        result.put("violations", violations);
        result.put("warnings", warnings);
        result.put("hasAdDisclaimer", hasAdDisclaimer);
        
        return result;
    }

    @Override
    public Map<String, Object> checkCopyrightIssues(String content) {
        Map<String, Object> result = new HashMap<>();
        List<String> warnings = new ArrayList<>();
        
        // 检查可能的版权问题
        if (content.contains("转载") && !content.contains("授权")) {
            warnings.add("内容包含转载但未说明授权情况");
        }
        
        if (content.contains("图片来自网络") || content.contains("侵删")) {
            warnings.add("使用网络图片可能存在版权风险");
        }
        
        result.put("passed", true);
        result.put("violations", new ArrayList<>());
        result.put("warnings", warnings);
        
        return result;
    }

    @Override
    public Map<String, Object> checkInfluencerCompliance(String content, Map<String, Object> influencerInfo) {
        Map<String, Object> result = new HashMap<>();
        List<String> violations = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        boolean passed = true;
        
        // 检查KOL资质
        if (influencerInfo != null) {
            Integer followers = (Integer) influencerInfo.get("followers");
            Boolean hasCertification = (Boolean) influencerInfo.get("hasCertification");
            String category = (String) influencerInfo.get("category");
            
            if (followers != null && followers > 10000 && (hasCertification == null || !hasCertification)) {
                warnings.add("大V账号建议进行官方认证");
            }
            
            // 检查推广内容
            if (isLikelyAdvertising(content) && (hasCertification == null || !hasCertification)) {
                warnings.add("推广内容建议由认证账号发布");
            }
            
            // KOL特定合规要求
            if (followers != null && followers > 10000 && isLikelyAdvertising(content)) {
                // 大V需要明确标注广告性质
                boolean hasAdDisclosure = content.contains("广告") || content.contains("推广") || 
                                        content.contains("合作") || content.contains("赞助");
                if (!hasAdDisclosure) {
                    violations.add("大V发布推广内容需要明确标注广告或合作关系");
                    passed = false;
                }
            }
            
            // 医疗、金融等特殊领域KOL要求
            if ("医疗".equals(category) || "健康".equals(category)) {
                boolean hasMedicalDisclaimer = content.contains("仅供参考") || content.contains("请咨询专业医生") ||
                                             content.contains("不能替代专业医疗建议");
                if (!hasMedicalDisclaimer && (content.contains("治疗") || content.contains("治愈"))) {
                    violations.add("医疗类KOL需要添加免责声明");
                    passed = false;
                }
            }
            
            if ("金融".equals(category) || "投资".equals(category)) {
                boolean hasRiskDisclaimer = content.contains("投资有风险") || content.contains("入市需谨慎") ||
                                          content.contains("不构成投资建议");
                if (!hasRiskDisclaimer && (content.contains("收益") || content.contains("盈利"))) {
                    violations.add("金融类KOL需要添加风险提示");
                    passed = false;
                }
            }
        }
        
        result.put("passed", passed);
        result.put("violations", violations);
        result.put("warnings", warnings);
        
        return result;
    }

    @Override
    public Map<String, Object> generateComplianceReport(List<String> contentIds, String platform) {
        Map<String, Object> report = new HashMap<>();
        
        // 模拟报告生成
        report.put("totalContent", contentIds.size());
        report.put("compliantContent", (int)(contentIds.size() * 0.85));
        report.put("violatedContent", (int)(contentIds.size() * 0.1));
        report.put("warningContent", (int)(contentIds.size() * 0.05));
        report.put("platform", platform);
        report.put("reportDate", new Date());
        
        return report;
    }

    @Override
    public boolean setComplianceConfig(Map<String, Object> config) {
        log.info("设置社交媒体合规配置: {}", config);
        return true;
    }

    @Override
    public Map<String, Object> getComplianceStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("isActive", true);
        status.put("lastUpdate", new Date());
        status.put("totalChecks", 1000);
        status.put("complianceRate", "95.5%");
        return status;
    }

    // ============== 私有辅助方法 ==============

    /**
     * 计算合规分数
     */
    private int calculateComplianceScore(String content, boolean isCompliant, int violationCount, int warningCount) {
        if (!isCompliant) return 0;
        
        int baseScore = 100;
        baseScore -= violationCount * 20; // 每个违规扣20分
        baseScore -= warningCount * 5;   // 每个警告扣5分
        
        // 内容质量加分
        if (content.length() > 100) baseScore += 5;
        if (hasComplianceKeywords(content)) baseScore += 10;
        
        return Math.max(0, Math.min(100, baseScore));
    }

    /**
     * 检查是否有重复内容
     */
    private boolean hasRepeatedContent(String content) {
        String[] sentences = content.split("[。！？\\n]");
        Set<String> uniqueSentences = new HashSet<>();
        
        for (String sentence : sentences) {
            sentence = sentence.trim();
            if (sentence.length() > 10) {
                if (uniqueSentences.contains(sentence)) {
                    return true;
                }
                uniqueSentences.add(sentence);
            }
        }
        
        return false;
    }

    /**
     * 计算广告比例
     */
    private double calculateAdvertisingRatio(String content) {
        int totalLength = content.length();
        int adKeywords = 0;
        
        String[] adWords = {"购买", "下单", "优惠", "折扣", "促销", "特价", "限时", "抢购"};
        for (String word : adWords) {
            adKeywords += countOccurrences(content, word);
        }
        
        return (double) adKeywords * 2 / totalLength; // 粗略估算
    }

    /**
     * 统计标签数量
     */
    private int countHashtags(String content) {
        return ReUtil.findAll("#\\S+", content, 0).size();
    }

    /**
     * 判断是否可能是广告
     */
    private boolean isLikelyAdvertising(String content) {
        String[] adIndicators = {"购买", "下单", "优惠", "折扣", "促销", "特价", "限时", "抢购", "扫码", "链接"};
        int adCount = 0;
        
        for (String indicator : adIndicators) {
            if (content.contains(indicator)) {
                adCount++;
            }
        }
        
        return adCount >= 3;
    }

    /**
     * 统计词频
     */
    private int countOccurrences(String content, String word) {
        int count = 0;
        int index = 0;
        
        while ((index = content.indexOf(word, index)) != -1) {
            count++;
            index += word.length();
        }
        
        return count;
    }

    /**
     * 检查是否有合规关键词
     */
    private boolean hasComplianceKeywords(String content) {
        for (String keyword : COMPLIANCE_KEYWORDS) {
            if (content.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取敏感类别名称
     */
    private String getSensitiveCategoryName(String category) {
        Map<String, String> categoryNames = new HashMap<>();
        categoryNames.put("political", "政治敏感");
        categoryNames.put("violence", "暴力恐怖");
        categoryNames.put("pornography", "色情低俗");
        categoryNames.put("false_advertising", "虚假广告");
        categoryNames.put("financial_fraud", "金融诈骗");
        categoryNames.put("medical_false", "医疗虚假");
        categoryNames.put("prohibited_items", "违禁品");
        categoryNames.put("privacy", "个人隐私");
        
        return categoryNames.getOrDefault(category, "未知类别");
    }
}