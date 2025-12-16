package com.kuafuweb.web.entity;

import java.util.Date;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kuafuweb.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.kuafuweb.common.domin.StaticResource;
import java.util.List;
import com.kuafuweb.common.annotation.Excel.*;



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
public class AdRecommendInfo    {
    @TableId(value = "ad_recommend_info_id", type = IdType.AUTO)
    @JsonProperty(value = "adRecommendInfoId")
    @Excel(name = "主键")

    private Integer adRecommendInfoId;
    @JsonProperty(value = "adTitle")
    @Excel(name = "广告标题")
    @ExcelProperty(value = "广告标题")
    @TableField(value = "ad_title")

    private String adTitle;
    @JsonProperty(value = "adImageResourceKey")
    @Excel(cellType = ColumnType.IMAGE, name = "广告图片")
    @ExcelProperty(value = "广告图片")
    @TableField(value = "ad_image_resource_key")

    private String adImageResourceKey;
    @JsonProperty(value = "adUrl")
    @Excel(name = "跳转链接")
    @ExcelProperty(value = "跳转链接")
    @TableField(value = "ad_url")

    private String adUrl;
    @JsonProperty(value = "cpcFee")
    @Excel(name = "CPC费用")
    @ExcelProperty(value = "CPC费用")
    @TableField(value = "cpc_fee")

    private Double cpcFee;

    @TableField(exist = false)
    private List<StaticResource> adImage;

}





