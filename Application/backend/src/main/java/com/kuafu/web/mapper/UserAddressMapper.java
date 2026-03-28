package com.kuafu.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuafu.web.entity.UserAddress;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户收货地址数据访问层
 */
@Mapper
public interface UserAddressMapper extends BaseMapper<UserAddress> {
}
