package com.kuafu.web.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.*;
import com.kuafu.common.entity.StaticResource;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kuafu.web.annotation.IsNotNullField;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import com.kuafu.common.annotation.*;
/**
 * <p>  客户信息 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("customer_info")
public class CustomerInfo  {
    @TableId(value = "customer_info_id", type = IdType.AUTO)
    @JsonProperty("customer_info_id")
    
    
    
    private Integer customerInfoId;
    @JsonProperty("customer_name")
    @IsNotNullField(description = "客户名称")
    
    
    @TableField("customer_name")
    private String customerName;
    @JsonProperty("phone")
    
    
    @TableField("phone")
    private String phone;
    @JsonProperty("email")
    
    
    @TableField("email")
    private String email;
    @JsonProperty("address")
    
    
    @TableField("address")
    private String address;
    @JsonProperty("rfm_level_enum_rfm_level_enum_id_1")
    
    
    @TableField("rfm_level_enum_rfm_level_enum_id_1")
    private Integer rfmLevelEnumRfmLevelEnumId1;
    @JsonProperty("customer_tag_enum_customer_tag_enum_id_1")
    
    
    @TableField("customer_tag_enum_customer_tag_enum_id_1")
    private Integer customerTagEnumCustomerTagEnumId1;
    @JsonProperty("wechat_id")
    
    
    @TableField("wechat_id")
    private String wechatId;
    @JsonProperty("wechat_data_encrypted")
    
    
    @TableField("wechat_data_encrypted")
    private String wechatDataEncrypted;
    @JsonProperty("source")
    
    
    @TableField("source")
    private String source;





}
