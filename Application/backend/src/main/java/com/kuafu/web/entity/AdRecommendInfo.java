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
 * <p>  广告推荐 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("ad_recommend_info")
public class AdRecommendInfo  {
    @TableId(value = "ad_recommend_info_id", type = IdType.AUTO)
    @JsonProperty("ad_recommend_info_id")
    
    
    
    private Integer adRecommendInfoId;
    @JsonProperty("ad_title")
    @IsNotNullField(description = "广告标题")
    
    
    @TableField("ad_title")
    private String adTitle;
    @JsonProperty("ad_url")
    
    
    @TableField("ad_url")
    private String adUrl;
    @JsonProperty("cpc_fee")
    
    
    @TableField("cpc_fee")
    private Double cpcFee;



@TableField(exist = false)
@JsonProperty("ad_image")
private List<StaticResource> adImage;



}
