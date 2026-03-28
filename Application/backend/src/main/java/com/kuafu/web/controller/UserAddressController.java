package com.kuafu.web.controller;

import com.kuafu.common.domin.ResultUtils;
import com.kuafu.common.login.SecurityUtils;
import com.kuafu.web.entity.UserAddress;
import com.kuafu.web.service.UserAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 用户收货地址管理
 */
@RestController
@RequestMapping("/user/addresses")
@Api(tags = "用户收货地址")
@RequiredArgsConstructor
public class UserAddressController {

    private final UserAddressService userAddressService;

    /**
     * 获取当前用户的收货地址列表
     */
    @GetMapping
    @ApiOperation("获取收货地址列表")
    public Object getAddressList() {
        Integer userId = SecurityUtils.getUserId().intValue();
        List<UserAddress> list = userAddressService.getAddressesByUserId(userId);
        return ResultUtils.success(list);
    }

    /**
     * 获取单个地址详情
     */
    @GetMapping("/{addressId}")
    @ApiOperation("获取地址详情")
    public Object getAddressDetail(@PathVariable Integer addressId) {
        Integer userId = SecurityUtils.getUserId().intValue();
        UserAddress address = userAddressService.getAddressById(addressId);
        if (address == null || !address.getUserInfoId().equals(userId)) {
            return ResultUtils.error("地址不存在或无权限");
        }
        return ResultUtils.success(address);
    }

    /**
     * 添加收货地址
     */
    @PostMapping
    @ApiOperation("添加收货地址")
    public Object addAddress(@RequestBody UserAddress address) {
        Integer userId = SecurityUtils.getUserId().intValue();
        address.setUserInfoId(userId);
        address.setCreateTime(new Date());
        address.setUpdateTime(new Date());
        
        // 如果设置为默认地址，取消其他默认地址
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            userAddressService.cancelDefaultAddress(userId);
        }
        
        boolean saved = userAddressService.save(address);
        return saved ? ResultUtils.success(address) : ResultUtils.error("添加失败");
    }

    /**
     * 更新收货地址
     */
    @PutMapping("/{addressId}")
    @ApiOperation("更新收货地址")
    public Object updateAddress(@PathVariable Integer addressId, @RequestBody UserAddress address) {
        Integer userId = SecurityUtils.getUserId().intValue();
        UserAddress existing = userAddressService.getAddressById(addressId);
        if (existing == null || !existing.getUserInfoId().equals(userId)) {
            return ResultUtils.error("地址不存在或无权限");
        }
        
        address.setAddressId(addressId);
        address.setUserInfoId(userId);
        address.setUpdateTime(new Date());
        
        // 如果设置为默认地址，取消其他默认地址
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            userAddressService.cancelDefaultAddress(userId);
        }
        
        boolean updated = userAddressService.updateById(address);
        return updated ? ResultUtils.success(address) : ResultUtils.error("更新失败");
    }

    /**
     * 删除收货地址
     */
    @DeleteMapping("/{addressId}")
    @ApiOperation("删除收货地址")
    public Object deleteAddress(@PathVariable Integer addressId) {
        Integer userId = SecurityUtils.getUserId().intValue();
        UserAddress existing = userAddressService.getAddressById(addressId);
        if (existing == null || !existing.getUserInfoId().equals(userId)) {
            return ResultUtils.error("地址不存在或无权限");
        }
        
        boolean deleted = userAddressService.removeById(addressId);
        return deleted ? ResultUtils.success("删除成功") : ResultUtils.error("删除失败");
    }

    /**
     * 设置默认地址
     */
    @PutMapping("/{addressId}/default")
    @ApiOperation("设置默认地址")
    public Object setDefaultAddress(@PathVariable Integer addressId) {
        Integer userId = SecurityUtils.getUserId().intValue();
        UserAddress existing = userAddressService.getAddressById(addressId);
        if (existing == null || !existing.getUserInfoId().equals(userId)) {
            return ResultUtils.error("地址不存在或无权限");
        }
        
        userAddressService.cancelDefaultAddress(userId);
        existing.setIsDefault(true);
        existing.setUpdateTime(new Date());
        boolean updated = userAddressService.updateById(existing);
        return updated ? ResultUtils.success("设置成功") : ResultUtils.error("设置失败");
    }
}
