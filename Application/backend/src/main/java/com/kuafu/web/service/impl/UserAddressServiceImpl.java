package com.kuafu.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuafu.web.entity.UserAddress;
import com.kuafu.web.mapper.UserAddressMapper;
import com.kuafu.web.service.UserAddressService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户收货地址服务实现
 */
@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {

    @Override
    public List<UserAddress> getAddressesByUserId(Integer userId) {
        LambdaQueryWrapper<UserAddress> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAddress::getUserInfoId, userId)
               .orderByDesc(UserAddress::getIsDefault)
               .orderByDesc(UserAddress::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public UserAddress getAddressById(Integer addressId) {
        return this.getById(addressId);
    }

    @Override
    public void cancelDefaultAddress(Integer userId) {
        LambdaQueryWrapper<UserAddress> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAddress::getUserInfoId, userId)
               .eq(UserAddress::getIsDefault, true);
        
        UserAddress address = new UserAddress();
        address.setIsDefault(false);
        this.update(address, wrapper);
    }

    @Override
    public UserAddress getDefaultAddress(Integer userId) {
        LambdaQueryWrapper<UserAddress> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAddress::getUserInfoId, userId)
               .eq(UserAddress::getIsDefault, true);
        return this.getOne(wrapper);
    }
}
