package com.kuafu.web.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p> 爬虫统计信息 </p>
 *
 * @author kuafuai
 * @description 爬虫运行统计和合规监控
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrawlerStatistics {
    
    private Long totalCrawled;      // 总采集数量
    private Long todayCrawled;      // 今日采集数量
    private Long convertedToOpportunity; // 转换为商机的数量
    private Long pendingReview;     // 待审核数量
    private Double complianceRate;  // 合规率
    private String lastCrawlTime;   // 最后采集时间
    private String lastError;       // 最后错误信息
    private Long errorCount;        // 错误次数
    private String mostActiveSource; // 最活跃的来源
    private Long blockedCount;      // 被阻止次数（合规检查）
    
    // 合规统计
    private Long robotsTxtRespected; // 遵守robots.txt次数
    private Long rateLimitRespected; // 遵守频率限制次数
    private Long publicDataOnly;    // 仅采集公开数据次数
}