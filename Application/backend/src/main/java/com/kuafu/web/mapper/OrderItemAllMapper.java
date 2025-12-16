package com.kuafu.web.mapper;

import java.util.List;
import com.kuafu.web.entity.OrderItemAll;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;

/**
 * <p> 订单商品明细 Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Mapper
public interface OrderItemAllMapper extends BaseMapper<OrderItemAll> {
    public List<OrderItemAll> selectListNew(@Param("ew") Wrapper<OrderItemAll> queryWrapper);
}
