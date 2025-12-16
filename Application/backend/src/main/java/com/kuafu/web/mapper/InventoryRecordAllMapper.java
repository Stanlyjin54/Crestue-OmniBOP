package com.kuafu.web.mapper;

import java.util.List;
import com.kuafu.web.entity.InventoryRecordAll;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;

/**
 * <p> 库存记录 Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Mapper
public interface InventoryRecordAllMapper extends BaseMapper<InventoryRecordAll> {
    public List<InventoryRecordAll> selectListNew(@Param("ew") Wrapper<InventoryRecordAll> queryWrapper);
}
