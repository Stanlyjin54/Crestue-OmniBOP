package com.kuafuweb.web.service;

import com.kuafuweb.web.entity.BusinessOpportunity;
import java.util.List;
import java.util.Map;

/**
 * 人工审核和确认流程接口
 * 负责管理自动生成的商机线索的审核流程
 */
public interface IOpportunityReviewService {

    /**
     * 提交商机审核
     * @param opportunityId 商机ID
     * @param submitterId 提交人ID
     * @return 审核记录ID
     */
    Long submitForReview(Long opportunityId, Long submitterId);

    /**
     * 审核商机
     * @param reviewId 审核记录ID
     * @param reviewerId 审核人ID
     * @param decision 审核决定 (approve/reject)
     * @param comments 审核意见
     * @return 审核结果
     */
    boolean reviewOpportunity(Long reviewId, Long reviewerId, String decision, String comments);

    /**
     * 批量审核商机
     * @param reviewIds 审核记录ID列表
     * @param reviewerId 审核人ID
     * @param decision 审核决定
     * @param comments 审核意见
     * @return 审核成功的数量
     */
    int reviewOpportunitiesBatch(List<Long> reviewIds, Long reviewerId, String decision, String comments);

    /**
     * 获取待审核的商机列表
     * @param reviewerId 审核人ID
     * @param page 页码
     * @param size 每页大小
     * @return 待审核商机列表
     */
    List<Map<String, Object>> getPendingReviews(Long reviewerId, int page, int size);

    /**
     * 获取审核历史
     * @param opportunityId 商机ID
     * @return 审核历史记录
     */
    List<Map<String, Object>> getReviewHistory(Long opportunityId);

    /**
     * 获取审核统计信息
     * @return 审核统计信息
     */
    Map<String, Object> getReviewStatistics();

    /**
     * 设置审核人
     * @param opportunityId 商机ID
     * @param reviewerId 审核人ID
     * @return 是否设置成功
     */
    boolean assignReviewer(Long opportunityId, Long reviewerId);

    /**
     * 自动分配审核人
     * @param opportunityId 商机ID
     * @return 分配的审核人ID
     */
    Long autoAssignReviewer(Long opportunityId);

    /**
     * 获取审核配置
     * @return 审核配置信息
     */
    Map<String, Object> getReviewConfig();

    /**
     * 更新审核配置
     * @param config 新的审核配置
     */
    void updateReviewConfig(Map<String, Object> config);

    /**
     * 发送审核通知
     * @param reviewId 审核记录ID
     * @param notificationType 通知类型 (email/sms/system)
     */
    void sendReviewNotification(Long reviewId, String notificationType);

    /**
     * 检查审核权限
     * @param userId 用户ID
     * @param opportunityId 商机ID
     * @return 是否有审核权限
     */
    boolean hasReviewPermission(Long userId, Long opportunityId);
}