package com.kuafu.web.mapper;

import java.util.List;
import com.kuafu.web.entity.PurchaseStatusEnum;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p> 采购状态枚举 Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Mapper
public interface PurchaseStatusEnumMapper extends BaseMapper<PurchaseStatusEnum> {

}
