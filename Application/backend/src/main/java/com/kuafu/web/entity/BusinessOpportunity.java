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
 * <p>  商机线索 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("business_opportunity")
public class BusinessOpportunity  {
    @TableId(value = "business_opportunity_id", type = IdType.AUTO)
    @JsonProperty("business_opportunity_id")
    
    
    
    private Integer businessOpportunityId;
    @JsonProperty("customer_info_customer_info_id_1")
    @IsNotNullField(description = "客户ID")
    
    
    @TableField("customer_info_customer_info_id_1")
    private Integer customerInfoCustomerInfoId1;
    @JsonProperty("opportunity_name")
    @IsNotNullField(description = "商机名称")
    
    
    @TableField("opportunity_name")
    private String opportunityName;
    @JsonProperty("status")
    @IsNotNullField(description = "商机状态")
    
    
    @TableField("status")
    private String status;
    @JsonProperty("follow_up_suggestion")
    
    
    @TableField("follow_up_suggestion")
    private String followUpSuggestion;





}
