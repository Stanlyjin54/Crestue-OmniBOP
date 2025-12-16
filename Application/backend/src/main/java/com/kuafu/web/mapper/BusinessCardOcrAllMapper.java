package com.kuafu.web.mapper;

import java.util.List;
import com.kuafu.web.entity.BusinessCardOcrAll;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;

/**
 * <p> 名片OCR建档 Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Mapper
public interface BusinessCardOcrAllMapper extends BaseMapper<BusinessCardOcrAll> {
    public List<BusinessCardOcrAll> selectListNew(@Param("ew") Wrapper<BusinessCardOcrAll> queryWrapper);
}
