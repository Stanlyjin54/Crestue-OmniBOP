package com.kuafu.web.mapper;

import java.util.List;
import com.kuafu.web.entity.CustomerInfoAll;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;

/**
 * <p> 客户信息 Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Mapper
public interface CustomerInfoAllMapper extends BaseMapper<CustomerInfoAll> {
    public List<CustomerInfoAll> selectListNew(@Param("ew") Wrapper<CustomerInfoAll> queryWrapper);
}
