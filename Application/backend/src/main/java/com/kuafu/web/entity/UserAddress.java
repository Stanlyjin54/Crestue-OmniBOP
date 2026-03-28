package com.kuafu.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * <p>用户收货地址</p>
 *
 * @author kuafuai
 * @description 消费者收货地址管理
 * @date 2025/06/18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_address")
public class UserAddress {

    @TableId(value = "address_id", type = IdType.AUTO)
    @JsonProperty("address_id")
    private Integer addressId;

    @JsonProperty("user_info_id")
    @TableField("user_info_id")
    private Integer userInfoId;

    @JsonProperty("receiver_name")
    @TableField("receiver_name")
    private String receiverName;

    @JsonProperty("receiver_phone")
    @TableField("receiver_phone")
    private String receiverPhone;

    @JsonProperty("province")
    @TableField("province")
    private String province;

    @JsonProperty("city")
    @TableField("city")
    private String city;

    @JsonProperty("district")
    @TableField("district")
    private String district;

    @JsonProperty("detail_address")
    @TableField("detail_address")
    private String detailAddress;

    @JsonProperty("zip_code")
    @TableField("zip_code")
    private String zipCode;

    @JsonProperty("is_default")
    @TableField("is_default")
    private Boolean isDefault;

    @JsonProperty("create_time")
    @TableField("create_time")
    private Date createTime;

    @JsonProperty("update_time")
    @TableField("update_time")
    private Date updateTime;
}
