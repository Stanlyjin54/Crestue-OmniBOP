package com.kuafu.web.mapper;

import java.util.List;
import com.kuafu.web.entity.AiAssistantLogAll;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;

/**
 * <p> AI助手日志 Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Mapper
public interface AiAssistantLogAllMapper extends BaseMapper<AiAssistantLogAll> {
    public List<AiAssistantLogAll> selectListNew(@Param("ew") Wrapper<AiAssistantLogAll> queryWrapper);
}
