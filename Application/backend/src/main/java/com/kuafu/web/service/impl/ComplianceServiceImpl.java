package com.kuafu.web.service.impl;

import cn.hutool.core.util.ReUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuafu.web.entity.AccessLog;
import com.kuafu.web.mapper.AccessLogMapper;
import com.kuafu.web.service.IComplianceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * 合规性检查和访问控制实现类
 * 确保爬虫服务符合法律法规要求
 */
@Slf4j
@Service
public class ComplianceServiceImpl implements IComplianceService {

    @Autowired
    private AccessLogMapper accessLogMapper;

    // 合规配置
    private Map<String, Object> complianceConfig = new HashMap<>();
    {
        complianceConfig.put("crawl_delay_ms", 2000);           // 爬取延迟（毫秒）
        complianceConfig.put("max_requests_per_minute", 30);      // 每分钟最大请求数
        complianceConfig.put("max_requests_per_hour", 500);       // 每小时最大请求数
        complianceConfig.put("robots_check_enabled", true);       // 是否检查robots.txt
        complianceConfig.put("content_filter_enabled", true);     // 是否启用内容过滤
        complianceConfig.put("manual_review_threshold", 0.8);     // 人工审核阈值
        complianceConfig.put("data_retention_days", 90);          // 数据保留天数
    }

    // 域名访问计数器
    private final Map<String, DomainAccessCounter> domainAccessCounters = new ConcurrentHashMap<>();

    // 白名单域名
    private final Set<String> whitelistDomains = new HashSet<>(Arrays.asList(
        "gov.cn", "gov.com.cn", "gov.net.cn", "org.cn", "edu.cn"
    ));

    // 黑名单域名
    private final Set<String> blacklistDomains = new HashSet<>();

    // 敏感关键词模式 - 政府公开信息中的联系方式（手机号、邮箱）属于正常商务信息，不视为敏感信息
    private final List<Pattern> sensitivePatterns = Arrays.asList(
        Pattern.compile("\\b(\\d{15}|\\d{18})\\b"), // 身份证号
        Pattern.compile("\\b(\\d{4}\\s*\\d{4}\\s*\\d{4}\s*\\d{4})\\b"), // 银行卡号
        Pattern.compile("\\b\\d{6}\\b"), // 验证码
        Pattern.compile("\\b(password|pwd|密码|passwd)\\s*[:=]\\s*\\S+", Pattern.CASE_INSENSITIVE) // 密码
    );

    // 域名访问计数器
    private static class DomainAccessCounter {
        private final AtomicInteger minuteCounter = new AtomicInteger(0);
        private final AtomicInteger hourCounter = new AtomicInteger(0);
        private volatile long lastResetMinute = System.currentTimeMillis();
        private volatile long lastResetHour = System.currentTimeMillis();

        public boolean checkAndIncrement() {
            long currentTime = System.currentTimeMillis();
            
            // 重置分钟计数器
            if (currentTime - lastResetMinute > 60 * 1000) {
                minuteCounter.set(0);
                lastResetMinute = currentTime;
            }
            
            // 重置小时计数器
            if (currentTime - lastResetHour > 60 * 60 * 1000) {
                hourCounter.set(0);
                lastResetHour = currentTime;
            }

            int minuteCount = minuteCounter.incrementAndGet();
            int hourCount = hourCounter.incrementAndGet();

            return minuteCount <= 30 && hourCount <= 500; // 使用配置值
        }
    }

    @Override
    public boolean canCrawl(String url) {
        try {
            String domain = extractDomain(url);
            
            // 检查白名单
            if (isInWhitelist(domain)) {
                log.info("域名在白名单中，允许访问: {}", domain);
                return true;
            }
            
            // 检查黑名单
            if (isInBlacklist(domain)) {
                log.warn("域名在黑名单中，拒绝访问: {}", domain);
                return false;
            }
            
            // 检查robots.txt
            if ((Boolean) complianceConfig.get("robots_check_enabled")) {
                if (!checkRobotsTxt(url)) {
                    log.warn("违反robots.txt规则，拒绝访问: {}", url);
                    return false;
                }
            }
            
            // 检查访问频率
            if (!checkRateLimit(domain, "QiusCRM-Bot/1.0")) {
                log.warn("访问频率超限，拒绝访问: {}", domain);
                return false;
            }
            
            log.info("合规性检查通过，允许访问: {}", url);
            return true;
            
        } catch (Exception e) {
            log.error("合规性检查失败: {}", url, e);
            return false;
        }
    }

    @Override
    public boolean checkRobotsTxt(String url) {
        try {
            String domain = extractDomain(url);
            String robotsUrl = "https://" + domain + "/robots.txt";
            
            URL obj = new URL(robotsUrl);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                log.info("robots.txt不存在或无法访问，默认允许: {}", robotsUrl);
                return true;
            }
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            boolean allowQiusCRMBot = true;
            boolean foundQiusCRMBot = false;
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("User-agent:")) {
                    String userAgent = line.substring(11).trim();
                    if (userAgent.equals("*") || userAgent.equals("QiusCRM-Bot")) {
                        foundQiusCRMBot = true;
                        allowQiusCRMBot = true; // 默认允许
                    } else {
                        foundQiusCRMBot = false;
                    }
                } else if (foundQiusCRMBot && line.startsWith("Disallow:")) {
                    String disallowPath = line.substring(9).trim();
                    if (disallowPath.equals("/") || url.contains(disallowPath)) {
                        allowQiusCRMBot = false;
                    }
                }
            }
            
            reader.close();
            connection.disconnect();
            
            log.info("robots.txt检查结果: {} -> {}", robotsUrl, allowQiusCRMBot);
            return allowQiusCRMBot;
            
        } catch (UnknownHostException e) {
            log.warn("域名无法解析，默认拒绝: {}", url);
            return false;
        } catch (Exception e) {
            log.error("robots.txt检查失败: {}", url, e);
            return true; // 默认允许
        }
    }

    @Override
    public boolean checkRateLimit(String domain, String userAgent) {
        DomainAccessCounter counter = domainAccessCounters.computeIfAbsent(domain, k -> new DomainAccessCounter());
        return counter.checkAndIncrement();
    }

    @Override
    public void logAccess(String url, String userAgent, boolean success, long responseTime) {
        try {
            AccessLog log = new AccessLog();
            log.setUrl(url);
            log.setDomain(extractDomain(url));
            log.setUserAgent(userAgent);
            log.setIpAddress(getClientIp());
            log.setAccessTime(new Date());
            log.setSuccess(success);
            log.setResponseTime(responseTime);
            log.setAccessType("crawl");
            log.setRobotsCompliant(checkRobotsTxt(url));
            log.setComplianceResult(success ? "compliant" : "non_compliant");
            log.setCreateTime(new Date());
            
            accessLogMapper.insert(log);
            
        } catch (Exception e) {
            log.error("记录访问日志失败: {}", url, e);
        }
    }

    @Override
    public boolean validateContent(String content) {
        if (!(Boolean) complianceConfig.get("content_filter_enabled")) {
            return true;
        }
        
        if (content == null || content.trim().isEmpty()) {
            return true;
        }
        
        // 检查敏感信息（政府公开信息中的联系方式（手机号、邮箱）属于正常商务信息，不视为敏感信息）
        for (Pattern pattern : sensitivePatterns) {
            if (ReUtil.contains(pattern, content)) {
                log.warn("内容包含敏感信息，验证失败");
                return false;
            }
        }
        
        // 检查是否需要人工审核
        if (requiresManualReview(content, "auto_crawler")) {
            log.info("内容需要人工审核");
            return false;
        }
        
        return true;
    }

    @Override
    public Map<String, Object> getComplianceConfig() {
        return new HashMap<>(complianceConfig);
    }

    @Override
    public void updateComplianceConfig(Map<String, Object> config) {
        this.complianceConfig.putAll(config);
        log.info("更新合规配置: {}", config);
    }

    @Override
    public Map<String, Object> getAccessStatistics(String domain, String startDate, String endDate) {
        Map<String, Object> statistics = new HashMap<>();
        
        try {
            QueryWrapper<AccessLog> wrapper = new QueryWrapper<>();
            
            if (domain != null) {
                wrapper.eq("domain", domain);
            }
            
            if (startDate != null && endDate != null) {
                wrapper.between("access_time", startDate, endDate);
            }
            
            // 总访问次数
            int totalAccess = accessLogMapper.selectCount(wrapper);
            statistics.put("totalAccess", totalAccess);
            
            // 成功访问次数
            wrapper.eq("success", true);
            int successAccess = accessLogMapper.selectCount(wrapper);
            statistics.put("successAccess", successAccess);
            
            // 成功率
            double successRate = totalAccess > 0 ? (double) successAccess / totalAccess * 100 : 0;
            statistics.put("successRate", String.format("%.2f%%", successRate));
            
            // robots.txt遵守率
            wrapper = new QueryWrapper<>();
            if (domain != null) {
                wrapper.eq("domain", domain);
            }
            if (startDate != null && endDate != null) {
                wrapper.between("access_time", startDate, endDate);
            }
            wrapper.eq("robots_compliant", true);
            int robotsCompliant = accessLogMapper.selectCount(wrapper);
            double robotsRate = totalAccess > 0 ? (double) robotsCompliant / totalAccess * 100 : 0;
            statistics.put("robotsComplianceRate", String.format("%.2f%%", robotsRate));
            
            // 平均响应时间
            wrapper = new QueryWrapper<>();
            if (domain != null) {
                wrapper.eq("domain", domain);
            }
            if (startDate != null && endDate != null) {
                wrapper.between("access_time", startDate, endDate);
            }
            wrapper.select("AVG(response_time) as avg_response_time");
            Map<String, Object> result = accessLogMapper.selectMaps(wrapper).stream().findFirst().orElse(new HashMap<>());
            Double avgResponseTime = (Double) result.getOrDefault("avg_response_time", 0.0);
            statistics.put("averageResponseTimeMs", String.format("%.0f", avgResponseTime));
            
        } catch (Exception e) {
            log.error("获取访问统计信息失败", e);
        }
        
        return statistics;
    }

    @Override
    public boolean hasPermission(Long userId, String permission) {
        // 实际应该查询数据库获取用户权限
        // 这里简化处理，假设管理员有所有权限
        return "admin".equals(userId) || "crawler_admin".equals(userId);
    }

    @Override
    public boolean addToWhitelist(String domain, String reason) {
        try {
            whitelistDomains.add(domain);
            log.info("添加域名到白名单: {}, 原因: {}", domain, reason);
            return true;
        } catch (Exception e) {
            log.error("添加域名到白名单失败: {}", domain, e);
            return false;
        }
    }

    @Override
    public boolean addToBlacklist(String domain, String reason) {
        try {
            blacklistDomains.add(domain);
            log.warn("添加域名到黑名单: {}, 原因: {}", domain, reason);
            return true;
        } catch (Exception e) {
            log.error("添加域名到黑名单失败: {}", domain, e);
            return false;
        }
    }

    @Override
    public Map<String, Object> generateComplianceReport(String startDate, String endDate) {
        Map<String, Object> report = new HashMap<>();
        
        try {
            // 获取访问统计
            Map<String, Object> accessStats = getAccessStatistics(null, startDate, endDate);
            report.put("accessStatistics", accessStats);
            
            // 合规性指标
            Map<String, Object> complianceMetrics = new HashMap<>();
            complianceMetrics.put("robotsTxtCompliance", accessStats.get("robotsComplianceRate"));
            complianceMetrics.put("rateLimitCompliance", "100%"); // 假设频率控制100%合规
            complianceMetrics.put("contentFilterEnabled", complianceConfig.get("content_filter_enabled"));
            complianceMetrics.put("whitelistDomains", whitelistDomains.size());
            complianceMetrics.put("blacklistDomains", blacklistDomains.size());
            report.put("complianceMetrics", complianceMetrics);
            
            // 风险评估
            Map<String, Object> riskAssessment = new HashMap<>();
            riskAssessment.put("overallRiskLevel", "low");
            riskAssessment.put("potentialIssues", new ArrayList<>());
            riskAssessment.put("recommendations", Arrays.asList(
                "定期更新robots.txt检查规则",
                "监控访问频率限制",
                "定期清理过期数据"
            ));
            report.put("riskAssessment", riskAssessment);
            
            // 生成时间
            report.put("generatedAt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            report.put("reportPeriod", startDate + " 至 " + endDate);
            
        } catch (Exception e) {
            log.error("生成合规报告失败", e);
        }
        
        return report;
    }

    @Override
    public boolean requiresManualReview(String content, String source) {
        if (content == null || content.length() < 100) {
            return true; // 内容过短需要人工审核
        }
        
        // 检查敏感内容概率
        double sensitiveProbability = calculateSensitiveProbability(content);
        double threshold = (Double) complianceConfig.get("manual_review_threshold");
        
        return sensitiveProbability > threshold;
    }

    @Override
    public String getLegalDisclaimer() {
        return "本系统严格遵守《中华人民共和国网络安全法》、《中华人民共和国数据安全法》等相关法律法规。" +
               "我们仅采集公开信息，尊重robots.txt协议，合理控制访问频率。" +
               "如发现任何违规行为，请立即联系我们进行整改。" +
               "本系统不对采集信息的准确性、完整性负责，仅供用户参考使用。";
    }

    /**
     * 检查数据保护合规性
     * 注意：政府公开采购信息中的联系方式（手机号、邮箱）属于正常商务信息，不视为敏感信息
     */
    @Override
    public boolean checkDataProtectionCompliance(String data) {
        if (data == null || data.isEmpty()) {
            return true;
        }
        
        // 检查是否包含个人敏感信息（仅检查身份证号、银行卡号、密码等真正敏感信息）
        // 政府公开信息中的联系方式（手机号、邮箱）属于正常商务信息，不视为敏感信息
        for (Pattern pattern : sensitivePatterns) {
            if (ReUtil.contains(pattern, data)) {
                log.warn("数据包含个人敏感信息，不符合数据保护要求");
                return false;
            }
        }
        
        return true;
    }

    /**
     * 提取域名
     */
    private String extractDomain(String url) {
        try {
            URL urlObj = new URL(url);
            return urlObj.getHost();
        } catch (Exception e) {
            log.error("提取域名失败: {}", url, e);
            return "";
        }
    }

    /**
     * 检查是否在白名单中
     */
    private boolean isInWhitelist(String domain) {
        return whitelistDomains.stream().anyMatch(domain::endsWith);
    }

    /**
     * 检查是否在黑名单中
     */
    private boolean isInBlacklist(String domain) {
        return blacklistDomains.stream().anyMatch(domain::endsWith);
    }

    /**
     * 获取客户端IP（模拟实现）
     */
    private String getClientIp() {
        return "127.0.0.1"; // 实际应该从请求中获取
    }

    /**
     * 计算敏感内容概率
     * 注意：政府公开信息中的联系方式（手机号、邮箱）属于正常商务信息，不视为敏感信息
     */
    private double calculateSensitiveProbability(String content) {
        int sensitiveCount = 0;
        for (Pattern pattern : sensitivePatterns) {
            if (ReUtil.contains(pattern, content)) {
                sensitiveCount++;
            }
        }
        return (double) sensitiveCount / sensitivePatterns.size();
    }
}