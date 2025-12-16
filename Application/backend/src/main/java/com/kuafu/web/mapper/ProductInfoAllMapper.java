package com.kuafu.web.mapper;

import java.util.List;
import com.kuafu.web.entity.ProductInfoAll;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;

/**
 * <p> 商品信息 Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Mapper
public interface ProductInfoAllMapper extends BaseMapper<ProductInfoAll> {
    public List<ProductInfoAll> selectListNew(@Param("ew") Wrapper<ProductInfoAll> queryWrapper);
}
