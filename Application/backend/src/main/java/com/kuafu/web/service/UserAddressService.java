package com.kuafu.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kuafu.web.entity.UserAddress;

import java.util.List;

/**
 * 用户收货地址服务接口
 */
public interface UserAddressService extends IService<UserAddress> {

    /**
     * 获取用户的收货地址列表
     */
    List<UserAddress> getAddressesByUserId(Integer userId);

    /**
     * 获取地址详情
     */
    UserAddress getAddressById(Integer addressId);

    /**
     * 取消用户的默认地址
     */
    void cancelDefaultAddress(Integer userId);

    /**
     * 获取用户默认地址
     */
    UserAddress getDefaultAddress(Integer userId);
}
