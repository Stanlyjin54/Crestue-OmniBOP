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
 * <p>  名片OCR建档 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("business_card_ocr")
public class BusinessCardOcr  {
    @TableId(value = "business_card_ocr_id", type = IdType.AUTO)
    @JsonProperty("business_card_ocr_id")
    
    
    
    private Integer businessCardOcrId;
    @JsonProperty("customer_info_customer_info_id_1")
    @IsNotNullField(description = "客户ID")
    
    
    @TableField("customer_info_customer_info_id_1")
    private Integer customerInfoCustomerInfoId1;
    @JsonProperty("ocr_result")
    @IsNotNullField(description = "OCR识别结果")
    
    
    @TableField("ocr_result")
    private String ocrResult;



@TableField(exist = false)
@JsonProperty("card_image")
private List<StaticResource> cardImage;



}
