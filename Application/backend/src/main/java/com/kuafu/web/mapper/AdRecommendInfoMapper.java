package com.kuafu.web.mapper;

import java.util.List;
import com.kuafu.web.entity.AdRecommendInfo;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p> 广告推荐 Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Mapper
public interface AdRecommendInfoMapper extends BaseMapper<AdRecommendInfo> {

}
