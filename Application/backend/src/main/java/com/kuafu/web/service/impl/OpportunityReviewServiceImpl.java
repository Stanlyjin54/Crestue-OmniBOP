package com.kuafu.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuafu.web.entity.BusinessOpportunity;
import com.kuafu.web.entity.OpportunityReview;
import com.kuafu.web.entity.UserInfo;
import com.kuafu.web.mapper.BusinessOpportunityMapper;
import com.kuafu.web.mapper.OpportunityReviewMapper;
import com.kuafu.web.mapper.UserInfoMapper;
import com.kuafu.web.service.IOpportunityReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 人工审核和确认流程实现类
 * 负责管理自动生成的商机线索的审核流程
 */
@Slf4j
@Service
public class OpportunityReviewServiceImpl implements IOpportunityReviewService {

    @Autowired
    private OpportunityReviewMapper opportunityReviewMapper;

    @Autowired
    private BusinessOpportunityMapper businessOpportunityMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private JavaMailSender mailSender;

    // 审核配置
    private Map<String, Object> reviewConfig = new HashMap<>();
    {
        reviewConfig.put("review_timeout_hours", 24); // 审核超时时间（小时）
        reviewConfig.put("auto_assign", true);        // 是否自动分配审核人
        reviewConfig.put("notification_enabled", true); // 是否启用通知
        reviewConfig.put("batch_review_limit", 50);   // 批量审核限制
    }

    @Override
    @Transactional
    public Long submitForReview(Long opportunityId, Long submitterId) {
        log.info("提交商机审核: opportunityId={}, submitterId={}", opportunityId, submitterId);
        
        try {
            // 检查商机是否存在
            BusinessOpportunity opportunity = businessOpportunityMapper.selectById(opportunityId);
            if (opportunity == null) {
                log.error("商机不存在: {}", opportunityId);
                return null;
            }

            // 检查是否已有待审核记录
            QueryWrapper<OpportunityReview> existingWrapper = new QueryWrapper<>();
            existingWrapper.eq("opportunity_id", opportunityId);
            existingWrapper.eq("status", "pending");
            if (opportunityReviewMapper.selectCount(existingWrapper) > 0) {
                log.warn("商机已有待审核记录: {}", opportunityId);
                return null;
            }

            // 创建审核记录
            OpportunityReview review = new OpportunityReview();
            review.setOpportunityId(opportunityId);
            review.setSubmitterId(submitterId);
            review.setStatus("pending");
            review.setPriority(calculatePriority(opportunity));
            review.setDeadline(calculateDeadline());
            review.setSubmitTime(new Date());
            review.setCreateTime(new Date());
            review.setUpdateTime(new Date());
            review.setNotified(false);

            // 自动分配审核人
            if ((Boolean) reviewConfig.get("auto_assign")) {
                Long reviewerId = autoAssignReviewer(opportunityId);
                review.setReviewerId(reviewerId);
            }

            opportunityReviewMapper.insert(review);

            // 发送审核通知
            if ((Boolean) reviewConfig.get("notification_enabled")) {
                sendReviewNotification(review.getId(), "system");
            }

            // 更新商机状态
            opportunity.setStatus("under_review");
            businessOpportunityMapper.updateById(opportunity);

            log.info("成功提交商机审核: reviewId={}", review.getId());
            return review.getId();

        } catch (Exception e) {
            log.error("提交商机审核失败: opportunityId={}", opportunityId, e);
            return null;
        }
    }

    @Override
    @Transactional
    public boolean reviewOpportunity(Long reviewId, Long reviewerId, String decision, String comments) {
        log.info("审核商机: reviewId={}, reviewerId={}, decision={}", reviewId, reviewerId, decision);
        
        try {
            // 获取审核记录
            OpportunityReview review = opportunityReviewMapper.selectById(reviewId);
            if (review == null) {
                log.error("审核记录不存在: {}", reviewId);
                return false;
            }

            // 检查审核权限
            if (!hasReviewPermission(reviewerId, review.getOpportunityId())) {
                log.error("用户没有审核权限: reviewerId={}, opportunityId={}", reviewerId, review.getOpportunityId());
                return false;
            }

            // 更新审核记录
            review.setReviewerId(reviewerId);
            review.setDecision(decision);
            review.setComments(comments);
            review.setStatus("completed");
            review.setReviewTime(new Date());
            review.setUpdateTime(new Date());

            opportunityReviewMapper.updateById(review);

            // 更新商机状态
            BusinessOpportunity opportunity = businessOpportunityMapper.selectById(review.getOpportunityId());
            if ("approve".equals(decision)) {
                opportunity.setStatus("approved");
                opportunity.setApprovedBy(reviewerId);
                opportunity.setApprovedTime(new Date());
            } else if ("reject".equals(decision)) {
                opportunity.setStatus("rejected");
                // BusinessOpportunity实体没有setRejectedBy和setRejectedTime方法
                // 可以使用reviewedBy和reviewDate字段来存储拒绝信息
                opportunity.setReviewedBy(reviewerId);
                opportunity.setReviewDate(new Date());
                opportunity.setReviewComments(comments);
            }
            businessOpportunityMapper.updateById(opportunity);

            log.info("成功审核商机: reviewId={}, decision={}", reviewId, decision);
            return true;

        } catch (Exception e) {
            log.error("审核商机失败: reviewId={}", reviewId, e);
            return false;
        }
    }

    @Override
    @Transactional
    public int reviewOpportunitiesBatch(List<Long> reviewIds, Long reviewerId, String decision, String comments) {
        log.info("批量审核商机: reviewIds={}, reviewerId={}, decision={}", reviewIds.size(), reviewerId, decision);
        
        int successCount = 0;
        int batchLimit = (Integer) reviewConfig.get("batch_review_limit");
        
        // 限制批量审核数量
        if (reviewIds.size() > batchLimit) {
            reviewIds = reviewIds.subList(0, batchLimit);
        }

        for (Long reviewId : reviewIds) {
            if (reviewOpportunity(reviewId, reviewerId, decision, comments)) {
                successCount++;
            }
        }

        log.info("批量审核完成: 成功{}/总{}", successCount, reviewIds.size());
        return successCount;
    }

    @Override
    public List<Map<String, Object>> getPendingReviews(Long reviewerId, int page, int size) {
        try {
            IPage<OpportunityReview> pageObj = new Page<>(page, size);
            QueryWrapper<OpportunityReview> wrapper = new QueryWrapper<>();
            
            if (reviewerId != null) {
                wrapper.eq("reviewer_id", reviewerId);
            }
            wrapper.eq("status", "pending");
            wrapper.orderByDesc("priority", "create_time");

            IPage<OpportunityReview> reviewPage = opportunityReviewMapper.selectPage(pageObj, wrapper);
            
            return reviewPage.getRecords().stream()
                .map(this::convertReviewToMap)
                .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("获取待审核商机列表失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Map<String, Object>> getReviewHistory(Long opportunityId) {
        try {
            QueryWrapper<OpportunityReview> wrapper = new QueryWrapper<>();
            wrapper.eq("opportunity_id", opportunityId);
            wrapper.orderByDesc("create_time");

            List<OpportunityReview> reviews = opportunityReviewMapper.selectList(wrapper);
            
            return reviews.stream()
                .map(this::convertReviewToMap)
                .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("获取审核历史失败: opportunityId={}", opportunityId, e);
            return new ArrayList<>();
        }
    }

    @Override
    public Map<String, Object> getReviewStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        // 待审核数量
        QueryWrapper<OpportunityReview> pendingWrapper = new QueryWrapper<>();
        pendingWrapper.eq("status", "pending");
        Long pendingCount = opportunityReviewMapper.selectCount(pendingWrapper);
        statistics.put("pendingCount", pendingCount);

        // 今日审核数量
        QueryWrapper<OpportunityReview> todayWrapper = new QueryWrapper<>();
        todayWrapper.likeRight("review_time", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        Long todayReviewed = opportunityReviewMapper.selectCount(todayWrapper);
        statistics.put("todayReviewed", todayReviewed);

        // 审核通过率
        QueryWrapper<OpportunityReview> approvedWrapper = new QueryWrapper<>();
        approvedWrapper.eq("decision", "approve");
        Long approvedCount = opportunityReviewMapper.selectCount(approvedWrapper);
        
        QueryWrapper<OpportunityReview> totalWrapper = new QueryWrapper<>();
        totalWrapper.eq("status", "completed");
        Long totalReviewed = opportunityReviewMapper.selectCount(totalWrapper);
        
        double approvalRate = totalReviewed > 0 ? (double) approvedCount / totalReviewed * 100 : 0;
        statistics.put("approvalRate", String.format("%.2f%%", approvalRate));

        // 平均审核时间（小时）
        List<OpportunityReview> completedReviews = opportunityReviewMapper.selectList(totalWrapper);
        double avgReviewTime = completedReviews.stream()
            .filter(review -> review.getReviewTime() != null && review.getSubmitTime() != null)
            .mapToLong(review -> (review.getReviewTime().getTime() - review.getSubmitTime().getTime()) / (1000 * 60 * 60))
            .average()
            .orElse(0.0);
        statistics.put("averageReviewTimeHours", String.format("%.1f", avgReviewTime));

        return statistics;
    }

    @Override
    public boolean assignReviewer(Long opportunityId, Long reviewerId) {
        try {
            QueryWrapper<OpportunityReview> wrapper = new QueryWrapper<>();
            wrapper.eq("opportunity_id", opportunityId);
            wrapper.eq("status", "pending");
            
            OpportunityReview review = opportunityReviewMapper.selectOne(wrapper);
            if (review != null) {
                review.setReviewerId(reviewerId);
                review.setUpdateTime(new Date());
                opportunityReviewMapper.updateById(review);
                
                // 发送通知
                sendReviewNotification(review.getId(), "system");
                
                log.info("成功分配审核人: opportunityId={}, reviewerId={}", opportunityId, reviewerId);
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            log.error("分配审核人失败: opportunityId={}, reviewerId={}", opportunityId, reviewerId, e);
            return false;
        }
    }

    @Override
    public Long autoAssignReviewer(Long opportunityId) {
        try {
            // 获取有审核权限的用户列表
            QueryWrapper<UserInfo> userWrapper = new QueryWrapper<>();
            userWrapper.eq("status", "active");
            userWrapper.like("role", "reviewer");
            List<UserInfo> reviewers = userInfoMapper.selectList(userWrapper);
            
            if (reviewers.isEmpty()) {
                log.warn("没有找到审核人");
                return null;
            }

            // 简单的轮询分配算法
            Long minWorkloadReviewer = reviewers.stream()
                .min(Comparator.comparingLong(userInfo -> getReviewerWorkload(userInfo.getUserInfoId().longValue())))
                .map(userInfo -> userInfo.getUserInfoId().longValue())
                .orElse(null);

            if (minWorkloadReviewer != null) {
                assignReviewer(opportunityId, minWorkloadReviewer);
            }

            return minWorkloadReviewer;
            
        } catch (Exception e) {
            log.error("自动分配审核人失败: opportunityId={}", opportunityId, e);
            return null;
        }
    }

    @Override
    public Map<String, Object> getReviewConfig() {
        return new HashMap<>(reviewConfig);
    }

    @Override
    public void updateReviewConfig(Map<String, Object> config) {
        this.reviewConfig.putAll(config);
        log.info("更新审核配置: {}", config);
    }

    @Override
    public void sendReviewNotification(Long reviewId, String notificationType) {
        try {
            OpportunityReview review = opportunityReviewMapper.selectById(reviewId);
            if (review == null || review.getReviewerId() == null) {
                return;
            }

            UserInfo reviewer = userInfoMapper.selectById(review.getReviewerId());
            if (reviewer == null || reviewer.getEmail() == null) {
                return;
            }

            BusinessOpportunity opportunity = businessOpportunityMapper.selectById(review.getOpportunityId());
            if (opportunity == null) {
                return;
            }

            String subject = "新的商机审核任务";
            String content = String.format(
                "您有一个新的商机审核任务：\n\n商机名称：%s\n预算金额：%.2f元\n提交时间：%s\n优先级：%s\n\n请及时处理。",
                opportunity.getOpportunityName(),
                opportunity.getEstimatedValue() != null ? opportunity.getEstimatedValue() : 0.0,
                new SimpleDateFormat("yyyy-MM-dd HH:mm").format(review.getSubmitTime()),
                review.getPriority()
            );

            if ("email".equals(notificationType) && mailSender != null) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(reviewer.getEmail());
                message.setSubject(subject);
                message.setText(content);
                mailSender.send(message);
            }

            // 标记为已通知
            review.setNotified(true);
            opportunityReviewMapper.updateById(review);

            log.info("发送审核通知: reviewId={}, type={}", reviewId, notificationType);
            
        } catch (Exception e) {
            log.error("发送审核通知失败: reviewId={}", reviewId, e);
        }
    }

    @Override
    public boolean hasReviewPermission(Long userId, Long opportunityId) {
        try {
            // 获取审核记录
            QueryWrapper<OpportunityReview> wrapper = new QueryWrapper<>();
            wrapper.eq("opportunity_id", opportunityId);
            wrapper.eq("status", "pending");
            OpportunityReview review = opportunityReviewMapper.selectOne(wrapper);
            
            if (review == null) {
                return false;
            }

            // 检查是否是分配的审核人
            if (review.getReviewerId() != null && review.getReviewerId().equals(userId)) {
                return true;
            }

            // 检查用户角色
            UserInfo user = userInfoMapper.selectById(userId);
            if (user != null && user.getRole() != null && user.getRole().contains("admin")) {
                return true;
            }

            return false;
            
        } catch (Exception e) {
            log.error("检查审核权限失败: userId={}, opportunityId={}", userId, opportunityId, e);
            return false;
        }
    }

    /**
     * 计算审核优先级
     */
    private String calculatePriority(BusinessOpportunity opportunity) {
        // 基于预算金额和截止日期计算优先级
        double amount = opportunity.getEstimatedValue() != null ? opportunity.getEstimatedValue() : 0;
        Date deadline = opportunity.getExpectedCloseDate();
        
        if (amount >= 1000000) { // 100万以上
            return "high";
        } else if (deadline != null && (deadline.getTime() - System.currentTimeMillis()) < 7 * 24 * 60 * 60 * 1000) { // 7天内截止
            return "high";
        } else if (amount >= 500000) { // 50万以上
            return "medium";
        } else {
            return "low";
        }
    }

    /**
     * 计算审核期限
     */
    private Date calculateDeadline() {
        int timeoutHours = (Integer) reviewConfig.get("review_timeout_hours");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, timeoutHours);
        return calendar.getTime();
    }

    /**
     * 获取审核人的工作量
     */
    private long getReviewerWorkload(Long reviewerId) {
        QueryWrapper<OpportunityReview> wrapper = new QueryWrapper<>();
        wrapper.eq("reviewer_id", reviewerId);
        wrapper.eq("status", "pending");
        return opportunityReviewMapper.selectCount(wrapper);
    }

    /**
     * 转换审核记录为Map
     */
    private Map<String, Object> convertReviewToMap(OpportunityReview review) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", review.getId());
        result.put("opportunityId", review.getOpportunityId());
        result.put("submitterId", review.getSubmitterId());
        result.put("reviewerId", review.getReviewerId());
        result.put("status", review.getStatus());
        result.put("decision", review.getDecision());
        result.put("comments", review.getComments());
        result.put("submitTime", review.getSubmitTime());
        result.put("reviewTime", review.getReviewTime());
        result.put("priority", review.getPriority());
        result.put("deadline", review.getDeadline());
        result.put("notified", review.getNotified());
        result.put("createTime", review.getCreateTime());
        result.put("updateTime", review.getUpdateTime());

        // 添加商机信息
        BusinessOpportunity opportunity = businessOpportunityMapper.selectById(review.getOpportunityId());
        if (opportunity != null) {
            result.put("opportunityName", opportunity.getOpportunityName());
            result.put("opportunityAmount", opportunity.getEstimatedValue());
            result.put("opportunityStatus", opportunity.getStatus());
        }

        // 添加用户信息
        if (review.getSubmitterId() != null) {
            UserInfo submitter = userInfoMapper.selectById(review.getSubmitterId());
            if (submitter != null) {
                result.put("submitterName", submitter.getUsername());
            }
        }

        if (review.getReviewerId() != null) {
            UserInfo reviewer = userInfoMapper.selectById(review.getReviewerId());
            if (reviewer != null) {
                result.put("reviewerName", reviewer.getUsername());
            }
        }

        return result;
    }
}