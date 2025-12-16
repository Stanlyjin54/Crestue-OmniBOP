package com.kuafu.web.entity;

import java.util.Date;
import java.math.BigDecimal;
import java.util.List;
import com.kuafu.common.entity.StaticResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



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
public class AdRecommendInfoAll {
    @TableField(value="ari.ad_recommend_info_id")
    @JsonProperty("ad_recommend_info_id")
    private Integer adRecommendInfoId;
    @TableField(value="ari.ad_url")
    @JsonProperty("ad_url")
    private String adUrl;
    @TableField(value="ari.cpc_fee")
    @JsonProperty("cpc_fee")
    private Double cpcFee;
    @TableField(value="ari.ad_title")
    @JsonProperty("ad_title")
    private String adTitle;


@TableField(value="ad_image")
@JsonProperty("ad_image")
private List<StaticResource> adImage;







}
