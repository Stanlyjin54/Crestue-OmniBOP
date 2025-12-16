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
public class BusinessCardOcr    {
    @TableId(value = "business_card_ocr_id", type = IdType.AUTO)
    @JsonProperty(value = "businessCardOcrId")
    @Excel(name = "主键")

    private Integer businessCardOcrId;
    @JsonProperty(value = "customerInfoCustomerInfoId1")
    @Excel(name = "客户ID")
    @ExcelProperty(value = "客户ID")
    @TableField(value = "customer_info_customer_info_id_1")

    private Integer customerInfoCustomerInfoId1;
    @JsonProperty(value = "cardImageResourceKey")
    @Excel(cellType = ColumnType.IMAGE, name = "名片图片")
    @ExcelProperty(value = "名片图片")
    @TableField(value = "card_image_resource_key")

    private String cardImageResourceKey;
    @JsonProperty(value = "ocrResult")
    @Excel(name = "OCR识别结果")
    @ExcelProperty(value = "OCR识别结果")
    @TableField(value = "ocr_result")

    private String ocrResult;

    @TableField(exist = false)
    private List<StaticResource> cardImage;

}





