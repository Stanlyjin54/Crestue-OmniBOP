package com.kuafu.web.service;

import com.kuafu.web.entity.ProcurementInfo;
import com.kuafu.web.entity.BusinessOpportunity;
import com.kuafu.web.entity.CrawlerStatistics;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * <p> 政府采购信息采集服务 </p>
 *
 * @author kuafuai
 * @description 合规的政府采购信息爬虫服务
 * @date 2025/06/18 13:48
 */
public interface IProcurementCrawlerService extends IService<ProcurementInfo> {

    /**
     * 采集政府采购网信息
     * @param keyword 搜索关键词
     * @param region 地区
     * @param days 采集最近几天的数据
     * @return 采集到的信息数量
     */
    int crawlGovernmentProcurement(String keyword, String region, int days);

    /**
     * 采集招标公告信息
     * @param category 招标类别
     * @param region 地区
     * @param days 采集最近几天的数据
     * @return 采集到的信息数量
     */
    int crawlTenderNotice(String category, String region, int days);

    /**
     * 智能匹配商机线索
     * @param procurementInfo 采购信息
     * @return 匹配的商机线索列表
     */
    List<BusinessOpportunity> matchBusinessOpportunities(ProcurementInfo procurementInfo);

    /**
     * 将采购信息转换为商机线索
     * @param procurementInfo 采购信息
     * @return 商机线索
     */
    BusinessOpportunity convertToOpportunity(ProcurementInfo procurementInfo);

    /**
     * 检查采集合规性
     * @param url 目标网址
     * @return 是否合规
     */
    boolean checkCompliance(String url);

    /**
     * 获取采集统计信息
     * @return 统计信息
     */
    CrawlerStatistics getCrawlerStatistics();
}