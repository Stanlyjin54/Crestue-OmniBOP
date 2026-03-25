package com.kuafu.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuafu.web.entity.OpportunityReview;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商机审核记录Mapper接口
 */
@Mapper
public interface OpportunityReviewMapper extends BaseMapper<OpportunityReview> {

    /**
     * 根据商机ID查询审核记录
     * 
     * @param opportunityId 商机ID
     * @return 审核记录列表
     */
    List<OpportunityReview> selectByOpportunityId(@Param("opportunityId") Long opportunityId);

    /**
     * 根据审核人ID查询待审核记录
     * 
     * @param reviewerId 审核人ID
     * @return 待审核记录列表
     */
    List<OpportunityReview> selectPendingByReviewerId(@Param("reviewerId") Long reviewerId);

    /**
     * 根据状态查询审核记录
     * 
     * @param status 审核状态
     * @return 审核记录列表
     */
    List<OpportunityReview> selectByStatus(@Param("status") String status);
}