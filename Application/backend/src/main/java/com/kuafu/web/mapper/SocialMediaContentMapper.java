package com.kuafu.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuafu.web.entity.SocialMediaContent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 社交媒体内容数据访问层
 */
@Mapper
public interface SocialMediaContentMapper extends BaseMapper<SocialMediaContent> {

    /**
     * 根据平台和时间范围查询内容
     */
    @Select("SELECT * FROM social_media_content WHERE platform = #{platform} AND publish_time BETWEEN #{startTime} AND #{endTime} AND status = 'active'")
    List<SocialMediaContent> selectByPlatformAndTimeRange(@Param("platform") String platform, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 统计各平台的内容数量
     */
    @Select("SELECT platform, COUNT(*) as count FROM social_media_content WHERE status = 'active' GROUP BY platform")
    List<Map<String, Object>> countByPlatform();

    /**
     * 查询未处理的内容
     */
    @Select("SELECT * FROM social_media_content WHERE processed = false AND status = 'active' LIMIT #{limit}")
    List<SocialMediaContent> selectUnprocessedContent(@Param("limit") int limit);

    /**
     * 根据关键词搜索内容
     */
    @Select("SELECT * FROM social_media_content WHERE (title LIKE CONCAT('%', #{keyword}, '%') OR content LIKE CONCAT('%', #{keyword}, '%')) AND status = 'active'")
    List<SocialMediaContent> searchByKeyword(@Param("keyword") String keyword);

    /**
     * 统计商机转化情况
     */
    @Select("SELECT process_result, COUNT(*) as count FROM social_media_content WHERE processed = true GROUP BY process_result")
    List<Map<String, Object>> countOpportunityConversion();

    /**
     * 获取高价值内容（商业价值分数大于指定值）
     */
    @Select("SELECT * FROM social_media_content WHERE commercial_score >= #{minScore} AND status = 'active' ORDER BY commercial_score DESC")
    List<SocialMediaContent> selectHighValueContent(@Param("minScore") int minScore);

    /**
     * 根据作者统计内容数量
     */
    @Select("SELECT author_name, COUNT(*) as count, AVG(commercial_score) as avg_score FROM social_media_content WHERE status = 'active' GROUP BY author_name HAVING count >= #{minContentCount} ORDER BY count DESC")
    List<Map<String, Object>> countByAuthor(@Param("minContentCount") int minContentCount);

    /**
     * 清理过期的内容（软删除）
     */
    @Select("UPDATE social_media_content SET status = 'expired' WHERE publish_time < #{expireTime} AND status = 'active'")
    int expireOldContent(@Param("expireTime") Date expireTime);
}