package com.kuafu.web.mapper;

import java.util.List;
import com.kuafu.web.entity.OrderInfoAll;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;

/**
 * <p> 订单信息 Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Mapper
public interface OrderInfoAllMapper extends BaseMapper<OrderInfoAll> {
    public List<OrderInfoAll> selectListNew(@Param("ew") Wrapper<OrderInfoAll> queryWrapper);
}
