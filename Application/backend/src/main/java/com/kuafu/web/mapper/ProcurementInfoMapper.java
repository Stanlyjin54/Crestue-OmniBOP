package com.kuafu.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuafu.web.entity.ProcurementInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 政府采购信息Mapper接口
 */
@Mapper
public interface ProcurementInfoMapper extends BaseMapper<ProcurementInfo> {

    /**
     * 根据状态查询采购信息
     * 
     * @param status 状态
     * @return 采购信息列表
     */
    List<ProcurementInfo> selectByStatus(@Param("status") String status);

    /**
     * 根据地区查询采购信息
     * 
     * @param region 地区
     * @return 采购信息列表
     */
    List<ProcurementInfo> selectByRegion(@Param("region") String region);

    /**
     * 根据行业查询采购信息
     * 
     * @param industry 行业
     * @return 采购信息列表
     */
    List<ProcurementInfo> selectByIndustry(@Param("industry") String industry);

    /**
     * 根据发布日期范围查询采购信息
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 采购信息列表
     */
    List<ProcurementInfo> selectByPublishDateRange(@Param("startDate") LocalDateTime startDate, 
                                                   @Param("endDate") LocalDateTime endDate);

    /**
     * 根据截止日期范围查询采购信息
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 采购信息列表
     */
    List<ProcurementInfo> selectByDeadlineRange(@Param("startDate") LocalDateTime startDate, 
                                               @Param("endDate") LocalDateTime endDate);

    /**
     * 分页查询采购信息
     * 
     * @param page 分页参数
     * @return 采购信息分页结果
     */
    IPage<ProcurementInfo> selectPage(Page<ProcurementInfo> page);

    /**
     * 根据匹配得分范围查询采购信息
     * 
     * @param minScore 最小得分
     * @param maxScore 最大得分
     * @return 采购信息列表
     */
    List<ProcurementInfo> selectByMatchScoreRange(@Param("minScore") Double minScore, 
                                                  @Param("maxScore") Double maxScore);
}