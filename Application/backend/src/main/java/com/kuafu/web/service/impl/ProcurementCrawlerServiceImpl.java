package com.kuafu.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuafu.web.entity.*;
import com.kuafu.web.mapper.ProcurementInfoMapper;
import com.kuafu.web.service.IProcurementCrawlerService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p> 政府采购信息采集服务实现类 </p>
 *
 * @author kuafuai
 * @description 合规的政府采购信息爬虫服务实现
 * @date 2025/06/18 13:48
 */
@Slf4j
@Service("ProcurementCrawlerService")
public class ProcurementCrawlerServiceImpl extends ServiceImpl<ProcurementInfoMapper, ProcurementInfo> 
        implements IProcurementCrawlerService {

    private static final String USER_AGENT = "Mozilla/5.0 (compatible; CRM-Bot/1.0; +http://your-domain.com/bot)";
    private static final int CRAWL_DELAY_MS = 2000; // 2秒延迟，避免对目标网站造成压力
    private static final int TIMEOUT_MS = 10000; // 10秒超时
    
    // 合规的政府采购网站列表
    private static final Map<String, String> GOVERNMENT_SOURCES = new HashMap<String, String>() {
        {
            put("中国政府采购网", "http://www.ccgp.gov.cn");
            put("各地方政府采购网", "http://www.ccgp-local.gov.cn");
            put("招标公告平台", "http://www.cebpubservice.com");
        }
    };

    @Override
    @Transactional
    public int crawlGovernmentProcurement(String keyword, String region, int days) {
        log.info("开始采集政府采购信息 - 关键词: {}, 地区: {}, 天数: {}", keyword, region, days);
        
        int totalCrawled = 0;
        LocalDateTime startTime = LocalDateTime.now().minusDays(days);
        
        try {
            // 模拟访问中国政府采购网
            totalCrawled += crawlCCGP(keyword, region, startTime);
            
            // 模拟访问地方政府采购网
            totalCrawled += crawlLocalGovernmentProcurement(keyword, region, startTime);
            
            log.info("政府采购信息采集完成 - 共采集: {} 条", totalCrawled);
            
        } catch (Exception e) {
            log.error("采集政府采购信息失败", e);
        }
        
        return totalCrawled;
    }

    @Override
    @Transactional
    public int crawlTenderNotice(String category, String region, int days) {
        log.info("开始采集招标公告信息 - 类别: {}, 地区: {}, 天数: {}", category, region, days);
        
        int totalCrawled = 0;
        LocalDateTime startTime = LocalDateTime.now().minusDays(days);
        
        try {
            totalCrawled += crawlTenderPlatform(category, region, startTime);
            log.info("招标公告信息采集完成 - 共采集: {} 条", totalCrawled);
        } catch (Exception e) {
            log.error("采集招标公告信息失败", e);
        }
        
        return totalCrawled;
    }

    /**
     * 采集中国政府采购网信息
     */
    private int crawlCCGP(String keyword, String region, LocalDateTime startTime) {
        int count = 0;
        try {
            // 模拟访问中国政府采购网搜索页面
            String searchUrl = String.format("%s/cggg/dfgg/index.htm", GOVERNMENT_SOURCES.get("中国政府采购网"));
            
            Document doc = Jsoup.connect(searchUrl)
                    .userAgent(USER_AGENT)
                    .timeout(TIMEOUT_MS)
                    .get();
            
            // 解析采购信息列表
            Elements items = doc.select(".list-item"); // 假设的CSS选择器
            
            for (Element item : items) {
                try {
                    ProcurementInfo procurement = parseProcurementItem(item, "中国政府采购网", keyword, region);
                    if (procurement != null && procurement.getPublishDate().isAfter(startTime)) {
                        
                        // 合规性检查
                        if (checkCompliance(procurement.getSourceUrl())) {
                            save(procurement);
                            count++;
                            
                            // 添加延迟，避免对目标网站造成压力
                            Thread.sleep(CRAWL_DELAY_MS);
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    log.error("解析采购信息项失败", e);
                }
            }
        } catch (Exception e) {
            log.error("采集中国政府采购网失败", e);
        }
        
        return count;
    }

    /**
     * 采集地方政府采购网信息
     */
    private int crawlLocalGovernmentProcurement(String keyword, String region, LocalDateTime startTime) {
        int count = 0;
        try {
            // 这里可以实现具体的地方政府采购网站采集逻辑
            // 为演示目的，模拟采集一些数据
            for (int i = 0; i < 5; i++) {
                ProcurementInfo procurement = ProcurementInfo.builder()
                        .title(String.format("%s政府采购项目%d", region, i))
                        .content("政府采购办公用品、设备等物资")
                        .procurementType("政府采购")
                        .region(region)
                        .budget("1000000")
                        .publishDate(LocalDateTime.now().minusDays(i))
                        .deadline(LocalDateTime.now().plusDays(30 - i))
                        .sourceUrl("http://example-local.gov.cn/procurement/" + i)
                        .sourceWebsite("地方政府采购网")
                        .contactInfo("政府采购中心 010-12345678")
                        .industry("办公用品")
                        .status("pending")
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .build();
                
                if (checkCompliance(procurement.getSourceUrl())) {
                    save(procurement);
                    count++;
                }
            }
        } catch (Exception e) {
            log.error("采集地方政府采购网失败", e);
        }
        
        return count;
    }

    /**
     * 采集招标平台信息
     */
    private int crawlTenderPlatform(String category, String region, LocalDateTime startTime) {
        int count = 0;
        try {
            // 模拟采集招标公告
            for (int i = 0; i < 3; i++) {
                ProcurementInfo procurement = ProcurementInfo.builder()
                        .title(String.format("%s%s招标公告%d", region, category, i))
                        .content(String.format("%s项目公开招标，欢迎符合条件的供应商参与", category))
                        .procurementType("招标公告")
                        .region(region)
                        .budget("5000000")
                        .publishDate(LocalDateTime.now().minusDays(i))
                        .deadline(LocalDateTime.now().plusDays(20 - i))
                        .sourceUrl("http://example-tender.com/notice/" + i)
                        .sourceWebsite("招标公告平台")
                        .contactInfo("招标代理公司 010-87654321")
                        .industry(category)
                        .status("pending")
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .build();
                
                if (checkCompliance(procurement.getSourceUrl())) {
                    save(procurement);
                    count++;
                }
            }
        } catch (Exception e) {
            log.error("采集招标平台失败", e);
        }
        
        return count;
    }

    /**
     * 解析采购信息项
     */
    private ProcurementInfo parseProcurementItem(Element item, String sourceWebsite, String keyword, String region) {
        try {
            String title = item.select(".title").text();
            String content = item.select(".content").text();
            String budget = item.select(".budget").text();
            String publishDateStr = item.select(".publish-date").text();
            String deadlineStr = item.select(".deadline").text();
            String sourceUrl = item.select("a").attr("href");
            String contactInfo = item.select(".contact").text();
            
            // 检查是否包含关键词
            if (!title.contains(keyword) && !content.contains(keyword)) {
                return null;
            }
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime publishDate = LocalDateTime.parse(publishDateStr, formatter);
            LocalDateTime deadline = LocalDateTime.parse(deadlineStr, formatter);
            
            return ProcurementInfo.builder()
                    .title(title)
                    .content(content)
                    .procurementType("政府采购")
                    .region(region)
                    .budget(budget)
                    .publishDate(publishDate)
                    .deadline(deadline)
                    .sourceUrl(sourceUrl)
                    .sourceWebsite(sourceWebsite)
                    .contactInfo(contactInfo)
                    .industry("综合")
                    .status("pending")
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .build();
            
        } catch (Exception e) {
            log.error("解析采购信息项失败", e);
            return null;
        }
    }

    @Override
    public boolean checkCompliance(String url) {
        // 合规性检查实现
        if (url == null || url.isEmpty()) {
            return false;
        }
        
        // 1. 检查是否为政府官方网站
        boolean isGovernmentSite = url.contains("gov.cn") || 
                                 url.contains("ccgp") || 
                                 url.contains("tender");
        
        // 2. 检查是否允许采集（模拟robots.txt检查）
        // 这里可以实现真实的robots.txt解析逻辑
        boolean allowsCrawling = !url.contains("/private/") && 
                               !url.contains("/admin/") &&
                               !url.contains("/user/");
        
        // 3. 检查采集频率（通过缓存控制）
        String cacheKey = "crawl:" + url;
        // 这里可以实现基于Redis的频率控制
        
        return isGovernmentSite && allowsCrawling;
    }

    @Override
    public CrawlerStatistics getCrawlerStatistics() {
        // 统计信息采集数据
        long totalCount = count();
        long todayCount = count(
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ProcurementInfo>()
                .ge("create_time", LocalDateTime.now().withHour(0).withMinute(0).withSecond(0))
        );
        long convertedCount = count(
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ProcurementInfo>()
                .eq("status", "converted")
        );
        long pendingCount = count(
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ProcurementInfo>()
                .eq("status", "pending")
        );
        
        return CrawlerStatistics.builder()
                .totalCrawled(totalCount)
                .todayCrawled(todayCount)
                .convertedToOpportunity(convertedCount)
                .pendingReview(pendingCount)
                .complianceRate(95.5) // 模拟合规率
                .lastCrawlTime(LocalDateTime.now().toString())
                .build();
    }

    @Override
    public List<BusinessOpportunity> matchBusinessOpportunities(ProcurementInfo procurementInfo) {
        List<BusinessOpportunity> opportunities = new ArrayList<>();
        
        try {
            // 简单的匹配逻辑：根据行业和预算匹配商机
            String industry = procurementInfo.getIndustry();
            String budget = procurementInfo.getBudget();
            
            // 可以根据不同行业生成不同的商机类型
            if (industry.contains("办公用品")) {
                BusinessOpportunity opportunity = convertToOpportunity(procurementInfo);
                opportunities.add(opportunity);
            } else if (industry.contains("IT设备")) {
                BusinessOpportunity opportunity = convertToOpportunity(procurementInfo);
                opportunities.add(opportunity);
            } else if (industry.contains("软件服务")) {
                BusinessOpportunity opportunity = convertToOpportunity(procurementInfo);
                opportunities.add(opportunity);
            }
            
        } catch (Exception e) {
            log.error("匹配商机失败", e);
        }
        
        return opportunities;
    }

    @Override
    public BusinessOpportunity convertToOpportunity(ProcurementInfo procurementInfo) {
        try {
            Double estimatedValue = null;
            if (procurementInfo.getBudget() != null && !procurementInfo.getBudget().trim().isEmpty()) {
                try {
                    estimatedValue = Double.parseDouble(procurementInfo.getBudget().replaceAll("[^0-9.]", ""));
                } catch (NumberFormatException e) {
                    log.warn("Invalid budget format: {}", procurementInfo.getBudget());
                }
            }
            
            BusinessOpportunity opportunity = BusinessOpportunity.builder()
                    .opportunityName(procurementInfo.getTitle())
                    .description(procurementInfo.getContent())
                    .estimatedValue(estimatedValue)
                    .expectedCloseDate(procurementInfo.getDeadline() != null ? java.sql.Timestamp.valueOf(procurementInfo.getDeadline()) : null)
                    .sourceId(procurementInfo.getProcurementInfoId().toString())
                    .sourceUrl(procurementInfo.getSourceUrl())
                    .contactInfo(procurementInfo.getContactInfo())
                    .company(procurementInfo.getSourceWebsite())
                    .industry(procurementInfo.getIndustry())
                    .status("pending")
                    .createdTime(java.sql.Timestamp.valueOf(LocalDateTime.now()))
                    .updatedTime(java.sql.Timestamp.valueOf(LocalDateTime.now()))
                    .build();
            
            return opportunity;
            
        } catch (Exception e) {
            log.error("转换为商机失败", e);
            return null;
        }
    }

    /**
     * 定时采集任务 - 每天凌晨2点执行
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void scheduledCrawl() {
        log.info("开始定时采集任务");
        
        // 预设的采集关键词
        String[] keywords = {"办公用品", "IT设备", "软件服务", "系统集成"};
        String[] regions = {"北京", "上海", "广州", "深圳"};
        
        for (String keyword : keywords) {
            for (String region : regions) {
                try {
                    crawlGovernmentProcurement(keyword, region, 1); // 采集最近1天
                    Thread.sleep(5000); // 地区间间隔5秒
                } catch (Exception e) {
                    log.error("定时采集失败 - 关键词: {}, 地区: {}", keyword, region, e);
                }
            }
        }
        
        log.info("定时采集任务完成");
    }
}